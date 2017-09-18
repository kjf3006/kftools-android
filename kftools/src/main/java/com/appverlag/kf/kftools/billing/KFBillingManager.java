package com.appverlag.kf.kftools.billing;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 16.09.17.
 */
public class KFBillingManager {

    private static final String LOG_TAG = "KFBillingManager";
    private static KFBillingManager instance;

    private BillingClient billingClient;
    private final String encodedKey;
    private boolean isServiceConnected;

    private final Handler handler;
    private final ArrayList<WeakReference<KFBillingManagerUpdater>> observer = new ArrayList<>();

    private final List<Purchase> purchases = new ArrayList<>();
    private Set<String> tokensToBeConsumed;



    /*
    *** initalisation ***
     */

    private KFBillingManager (Context context, String encodedKey) {

        Context appContext = context.getApplicationContext();

        this.encodedKey = encodedKey;
        billingClient = new BillingClient.Builder(appContext).setListener(purchasesUpdatedListener).build();
        handler = new Handler(appContext.getMainLooper());

        startServiceConnection(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "Setup successful. Querying inventory.");
                queryPurchases();
            }
        });

    }

    public static KFBillingManager getInstance () {
        return KFBillingManager.instance;
    }

    public static void initialise(Context context, String encodedKey) {
        if (KFBillingManager.instance == null) {
            KFBillingManager.instance = new KFBillingManager (context, encodedKey);
        }
    }

    /*
    update purchases
     */

    private void queryPurchases() {
        executeServiceRequest(new Runnable() {
            @Override
            public void run() {
                Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
                if (areSubscriptionsSupported()) {
                    Purchase.PurchasesResult subscriptionResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);

                    if (subscriptionResult.getResponseCode() == BillingClient.BillingResponse.OK) {
                        purchasesResult.getPurchasesList().addAll(subscriptionResult.getPurchasesList());
                    }
                } else if (purchasesResult.getResponseCode() == BillingClient.BillingResponse.OK) {
                    Log.i(LOG_TAG, "Skipped subscription purchases query since they are not supported");
                } else {
                    Log.w(LOG_TAG, "queryPurchases() got an error response code: " + purchasesResult.getResponseCode());
                }

                if (billingClient == null || purchasesResult.getResponseCode() != BillingClient.BillingResponse.OK) {
                    Log.w(LOG_TAG, "Billing client was null or result code (" + purchasesResult.getResponseCode() + ") was bad - quitting");
                    return;
                }

                Log.d(LOG_TAG, "Query inventory was successful.");

                purchases.clear();
                purchasesUpdatedListener.onPurchasesUpdated(BillingClient.BillingResponse.OK, purchasesResult.getPurchasesList());
            }
        });
    }


    /**
     *  user functions: purchase or subscription flow
     */

    public void initiatePurchaseFlow(final Activity activity, final String skuId, final ArrayList<String> oldSkus, final @BillingClient.SkuType String billingType) {

        executeServiceRequest(new Runnable() {
            @Override
            public void run() {
                BillingFlowParams purchaseParams = new BillingFlowParams.Builder()
                        .setSku(skuId).setType(billingType).setOldSkus(oldSkus).build();
                billingClient.launchBillingFlow(activity, purchaseParams);
            }
        });
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public boolean hasPurchaseWithSkuID(String skuID) {
        for (Purchase purchase : purchases) {
            if (purchase.getSku().equals(skuID)) return true;
        }
        return false;
    }

    public Purchase getPurchaseWithSkuID(String skuID) {
        for (Purchase purchase : purchases) {
            if (purchase.getSku().equals(skuID)) return purchase;
        }
        return null;
    }

    public void consumeAsync(final String purchaseToken) {
        if (tokensToBeConsumed == null) {
            tokensToBeConsumed = new HashSet<>();
        }
        else if (tokensToBeConsumed.contains(purchaseToken)) {
            Log.i(LOG_TAG, "Token was already scheduled to be consumed - skipping...");
            return;
        }
        tokensToBeConsumed.add(purchaseToken);


        executeServiceRequest(new Runnable() {
            @Override
            public void run() {
                billingClient.consumeAsync(purchaseToken, new ConsumeResponseListener() {
                    @Override
                    public void onConsumeResponse(String purchaseToken, int resultCode) {
                        Log.d(LOG_TAG, "Consumption finished. result code: " + resultCode + ". purchase token: " + purchaseToken);
                        tokensToBeConsumed.remove(purchaseToken);
                    }
                });
            }
        });
    }


    public void querySkuDetailsAsync(@BillingClient.SkuType final String itemType, final List<String> skuList, final SkuDetailsResponseListener listener) {

        executeServiceRequest(new Runnable() {
            @Override
            public void run() {
                billingClient.querySkuDetailsAsync(itemType, skuList, new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(SkuDetails.SkuDetailsResult result) {
                                listener.onSkuDetailsResponse(result);
                            }
                });
            }
        });
    }


    /*
    *** PurchasesUpdatedListener ***
     */

    private final PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(int responseCode, List<Purchase> purchases) {
            if (responseCode == BillingClient.BillingResponse.OK) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
                Log.i(LOG_TAG, "onPurchasesUpdated() - user cancelled the purchase flow - skipping");
            } else {
                Log.w(LOG_TAG, "onPurchasesUpdated() got unknown resultCode: " + responseCode);
            }
            notifyDataChange();
        }
    };

    private void handlePurchase(Purchase purchase) {
        if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
            Log.i(LOG_TAG, "Got a purchase: " + purchase + "; but signature is bad. Skipping...");
            return;
        }

        Log.d(LOG_TAG, "Got a verified purchase: " + purchase);

        purchases.add(purchase);
    }



    /*
    *** start & retry ***
     */


    private void startServiceConnection(final Runnable executeOnSuccess) {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {

                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    isServiceConnected = true;
                    if (executeOnSuccess != null) {
                        executeOnSuccess.run();
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                isServiceConnected = false;
            }
        });
    }

    private void executeServiceRequest(Runnable runnable) {
        if (isServiceConnected) {
            runnable.run();
        } else {
            startServiceConnection(runnable);
        }
    }

    /*
    helper
     */

    private boolean areSubscriptionsSupported() {
        int responseCode = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if (responseCode != BillingClient.BillingResponse.OK) {
            Log.w(LOG_TAG, "areSubscriptionsSupported() got an error response: " + responseCode);
        }
        return responseCode == BillingClient.BillingResponse.OK;
    }

    /*
    verify
     */

    private boolean verifyValidSignature(String signedData, String signature) {
        try {
            return KFBillingManagerSecurity.verifyPurchase(encodedKey, signedData, signature);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Got an exception trying to validate a purchase: " + e);
            return false;
        }
    }


    /*
    *** observer ***
     */

    public interface KFBillingManagerUpdater {
        void billingsDidUpdate();
    }

    public static void addObserver(KFBillingManagerUpdater obj) {
        instance.observer.add(new WeakReference<>(obj));
    }

    private void notifyDataChange () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Iterator<WeakReference<KFBillingManagerUpdater>> iterator = observer.iterator();
                while (iterator.hasNext()) {
                    KFBillingManagerUpdater updater = iterator.next().get();
                    if (updater == null) iterator.remove();
                    else updater.billingsDidUpdate();
                }
            }
        });
    }

    /*
    *** helper ***
     */

    private void runOnUiThread(Runnable r) {
        handler.post(r);
    }
}
