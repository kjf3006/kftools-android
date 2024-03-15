package com.appverlag.kf.kftools.ui.loading;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.other.DensityUtils;
import com.appverlag.kf.kftools.other.SystemColor;
import com.appverlag.kf.kftools.other.TextAppereance;

public class LoadingView extends FrameLayout {

    @NonNull
    private LoadingState loadingState = LoadingState.NONE;
    private OnRetryClickListener onRetryClickListener;

    private LoadingStateView loadingStateView;
    private ErrorStateView errorStateView;
    private ProgressStateView progressStateView;
    private View noneStateView;

    private View currentStateView;

    private ViewGroup bondView;

    public LoadingView(@NonNull Context context) {
        super(context);
        setup(context);
    }

    private void setup(Context context) {
        loadingStateView = new LoadingStateView(context);
        errorStateView = new ErrorStateView(context);
        progressStateView = new ProgressStateView(context);
        noneStateView = new View(context);

        errorStateView.getRetryButton().setOnClickListener(v -> {
            if (onRetryClickListener != null) {
                onRetryClickListener.onRetryClick(this);
            }
        });

        setBackgroundColor(SystemColor.SYSTEM_BACKGROUND.getValue());

        updateForState();
    }

    public void showInView(@NonNull ViewGroup viewGroup) {

        ViewGroup parentToAdd = resolveParentToAdd(viewGroup);

        // already added to view, nothing to do
        if (parentToAdd == getParent()) {
            return;
        }

        // currently shown in other view
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }

        MarginLayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parentToAdd.addView(this, layoutParams);
    }

    private ViewGroup resolveParentToAdd(@NonNull ViewGroup viewGroup) {
        // cannot add multiple views to ScrollView
        if (viewGroup instanceof ScrollView || viewGroup instanceof RecyclerView) {
            return resolveParentToAdd((ViewGroup) viewGroup.getParent());
        }
        return viewGroup;
    }

    public void hide() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    private View getViewForState(LoadingState loadingState) {
        return switch (loadingState) {
            case NONE -> noneStateView;
            case ERROR -> errorStateView;
            case LOADING -> loadingStateView;
            case PROGRESS -> progressStateView;
        };
    }

    private void updateForState() {
        updateStateViews();
        updateBondView();
        View stateView = getViewForState(loadingState);
        if (currentStateView != null) {
            if (currentStateView == stateView) {
                return;
            }
            removeView(currentStateView);
        }

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(DensityUtils.dpToPx(310), DensityUtils.dpToPx(300));
        layoutParams.gravity = Gravity.CENTER;
        addView(stateView, layoutParams);
        currentStateView = stateView;
    }

    private void updateStateViews() {
        if (loadingState == LoadingState.ERROR && loadingState.getError() != null) {
            errorStateView.getTextView().setText(loadingState.getError().getLocalizedMessage());
        }
    }

    private void updateBondView() {
        if (bondView == null) {
            return;
        }
        if (loadingState == LoadingState.NONE) {
            hide();
        }
        else {
            showInView(bondView);
        }
    }

    @NonNull
    public LoadingState getLoadingState() {
        return loadingState;
    }

    public void setLoadingState(@NonNull LoadingState loadingState) {
        this.loadingState = loadingState;
        updateForState();
    }

    public OnRetryClickListener getOnRetryClickListener() {
        return onRetryClickListener;
    }

    public void setOnRetryClickListener(OnRetryClickListener onRetryClickListener) {
        this.onRetryClickListener = onRetryClickListener;
        errorStateView.getRetryButton().setVisibility(onRetryClickListener != null ? VISIBLE : GONE);
    }

    public ViewGroup getBondView() {
        return bondView;
    }

    public void bindTo(ViewGroup bondView) {
        this.bondView = bondView;
    }

    public void bindTo(View view) {
        if (!(view instanceof ViewGroup)) {
            throw new RuntimeException("ViewBinding getRoot() must return a ViewGroup instance.");
        }
        bindTo((ViewGroup) view);
    }

    public void bindTo(ViewBinding viewBinding) {
        if (!(viewBinding.getRoot() instanceof ViewGroup)) {
            throw new RuntimeException("ViewBinding getRoot() must return a ViewGroup instance.");
        }
        bindTo((ViewGroup) viewBinding.getRoot());
    }

    public interface OnRetryClickListener {
        void onRetryClick(LoadingView loadingView);
    }

    public final static class LoadingStateView extends FrameLayout {

        private final TextView textView;

        private final ProgressBar progressBar;

        public LoadingStateView(@NonNull Context context) {
            super(context);

            textView = new TextView(context);
            textView.setText("Laden ...");
            textView.setTextAppearance(context, TextAppereance.CAPTION1.getValue());
            textView.setTextColor(SystemColor.SECONDARY_LABEL.getValue());
            textView.setGravity(Gravity.CENTER);

            progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyle);
            progressBar.setIndeterminate(true);
            progressBar.setPadding(0,0,0, DensityUtils.dpToPx(8));

            Drawable moddedDrawable = progressBar.getIndeterminateDrawable().mutate();
            moddedDrawable.setColorFilter(SystemColor.SYSTEM_GRAY2.getValue(), android.graphics.PorterDuff.Mode.MULTIPLY);
            progressBar.setIndeterminateDrawable(moddedDrawable);

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(progressBar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(textView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            addView(linearLayout, layoutParams);

        }

        public TextView getTextView() {
            return textView;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }
    }

    public final static class ErrorStateView extends FrameLayout {

        private final TextView textViewTitle;
        private final TextView textView;
        private final Button retryButton;

        public ErrorStateView(@NonNull Context context) {
            super(context);

            textView = new TextView(context);
            textView.setTextAppearance(context, TextAppereance.BODY.getValue());
            textView.setTextColor(SystemColor.SECONDARY_LABEL.getValue());
            textView.setGravity(Gravity.CENTER);

            textViewTitle = new TextView(context);
            textViewTitle.setText("Vorgang nicht möglich");
            textViewTitle.setTypeface(null, Typeface.BOLD);
            textViewTitle.setTextAppearance(context, TextAppereance.TITLE2.getValue());
            textViewTitle.setTextColor(SystemColor.SECONDARY_LABEL.getValue());
            textViewTitle.setGravity(Gravity.CENTER);

            retryButton = new Button(context, null, 0, 0);
            retryButton.setTextColor(SystemColor.LINK.getValue());
            retryButton.setPadding(0, DensityUtils.dpToPx(8), 0, 0);
            retryButton.setCompoundDrawablePadding(DensityUtils.dpToPx(8));
            retryButton.setText("Erneut versuchen");
            retryButton.setVisibility(GONE);

            Drawable retryButtonImage = ContextCompat.getDrawable(context, R.drawable.sf_arrow_counterclockwise_24);
            retryButtonImage = DrawableCompat.wrap(retryButtonImage);
            DrawableCompat.setTint(retryButtonImage.mutate(), SystemColor.LINK.getValue());
            retryButtonImage.setBounds(0, 0, 16, 16);

            retryButton.setCompoundDrawablesWithIntrinsicBounds(retryButtonImage, null, null, null);
            retryButton.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayoutParams.gravity = Gravity.CENTER;

            LinearLayout.LayoutParams linearLayoutParamsButton = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayoutParamsButton.gravity = Gravity.CENTER;

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(textViewTitle, linearLayoutParams);
            linearLayout.addView(textView, linearLayoutParams);
            linearLayout.addView(retryButton, linearLayoutParamsButton);

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            addView(linearLayout, layoutParams);
        }

        public TextView getTextViewTitle() {
            return textViewTitle;
        }

        public TextView getTextView() {
            return textView;
        }

        public Button getRetryButton() {
            return retryButton;
        }
    }

    public final static class ProgressStateView extends FrameLayout {

        public ProgressStateView(@NonNull Context context) {
            super(context);
            TextView textView = new TextView(context);
            textView.setText("ProgressStateView");
            addView(textView);
        }
    }
}
