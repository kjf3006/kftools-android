package com.appverlag.kf.kftoolsframework;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.appverlag.kf.kftools.images.ImageContainer;
import com.appverlag.kf.kftools.network.ConnectionManager;
import com.appverlag.kf.kftools.network.HTTPStatusCodeResponseInterceptor;
import com.appverlag.kf.kftools.network.ResponseFileSerializer;
import com.appverlag.kf.kftools.network.ResponseImageSerializer;
import com.appverlag.kf.kftools.network.ResponseJSONSerializer;
import com.appverlag.kf.kftools.other.KFLog;
import com.appverlag.kf.kftools.ui.KFLoadingView;
import com.appverlag.kf.kftools.ui.images.ImageGalleryFragment;
import com.codewaves.stickyheadergrid.StickyHeaderGridAdapter;
import com.codewaves.stickyheadergrid.StickyHeaderGridLayoutManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    private ViewGroup rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StickyHeaderGridLayoutManager(1));
        recyclerView.setAdapter(new Adapter());

        rootView = (ViewGroup) recyclerView.getParent();

        ConnectionManager.shared().addRequestInterceptor(new APIRequestInterceptor());

        Request request = new Request.Builder().url("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf").build();
        ConnectionManager.shared().send(request, new ResponseFileSerializer(), response -> {
            if (response.success()) {
                KFLog.d("NETWORK_TEST", "Did load data to file: " + response.value.getAbsolutePath());
            }
            else {
                KFLog.d("NETWORK_TEST", "Error: " + response.error.getLocalizedMessage());
            }
        });

        Request imageRequest = new Request.Builder().url("https://media.istockphoto.com/id/952696392/de/vektor/tv-test-karte.jpg?s=612x612&w=0&k=20&c=2H27UPACVlrYcQ9jX1cSWUmk95thrEyUUrOR5DfiXcs=").build();
        ConnectionManager.shared().send(imageRequest, new ResponseImageSerializer(new Size(100, 100)), response -> {
            if (response.success()) {
                KFLog.d("NETWORK_TEST", "Did load image with size: " + response.value.getWidth() + " / " + response.value.getHeight());
            }
            else {
                KFLog.d("NETWORK_TEST", "Error: " + response.error.getLocalizedMessage());
            }
        });
    }


     /*
    adapter
    */

    private class Adapter extends StickyHeaderGridAdapter {

        private List<Section> data;

        public Adapter() {
            setupData();
        }

        private void setupData() {
            data = new ArrayList<>();

            data.add(new Section("UI-Components", Arrays.asList("KFThemes", "ImageGallery", "KFFormComponents", "KFYoutube", "KFLoadingView")));
            data.add(new Section("Data-Handling", Arrays.asList("KFCache", "KFManagedObjects")));
            data.add(new Section("Network", Arrays.asList("ConnectionManager")));
            notifyAllSectionsDataSetChanged();
        }

        @Override
        public int getSectionCount() {
            return data.size();
        }

        @Override
        public int getSectionItemCount(int section) {
            return  data.get(section).rowCount();
        }

        @Override
        public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_section_header, parent, false));
        }

        @Override
        public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
            final ItemViewHolder holder = new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_section_item, parent, false));

            holder.itemView.setOnClickListener(v -> {
                final int section = getAdapterPositionSection(holder.getBindingAdapterPosition());
                final int offset = getItemSectionOffset(section, holder.getBindingAdapterPosition());
                String id = data.get(section).rows.get(offset);

//                Intent intent = new Intent(v.getContext(), ThemeActivity.class);
//                v.getContext().startActivity(intent);
                handleSelection(id);
            });

            return holder;
        }

        @Override
        public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int section) {
            Section s = data.get(section);
            TextView textView = viewHolder.itemView.findViewById(R.id.textView);
            textView.setText(s.title);
        }

        @Override
        public void onBindItemViewHolder(ItemViewHolder viewHolder, int section, int offset) {
            String r = data.get(section).rows.get(offset);
            TextView textView = viewHolder.itemView.findViewById(R.id.textView);
            textView.setText(r);
        }


        private class Section {

            public Section(String title, List<String>rows) {
                this.title = title;
                this.rows = rows;
            }

            public int rowCount() {
                return rows.size();
            }

            String title;
            List<String> rows;
        }

        private void handleSelection(String id) {
            if (id.equals("KFLoadingView")) {
                KFLoadingView loadingView = new KFLoadingView(MainActivity.this);
                loadingView.showInView(MainActivity.this.rootView);
            }
            else if (id.equals("ConnectionManager")) {
                testNetwork();
            }
            else if (id.equals("KFThemes")) {
                Intent intent = new Intent(MainActivity.this, ThemeActivity.class);
                startActivity(intent);
            }
            else if (id.equals("ImageGallery")) {
                ImageGalleryFragment fragment = new ImageGalleryFragment(Arrays.asList(
                        ImageContainer.url("https://media.idownloadblog.com/wp-content/uploads/2021/09/Apple-September-Event-California-Streaming-BasicAppleGuy-iDownloadBlog-6K.png"),
                        ImageContainer.url("https://restado.de/wp-content/uploads/test.jpg"),
                        ImageContainer.url("https://upload.wikimedia.org/wikipedia/commons/thumb/6/66/SMPTE_Color_Bars.svg/1200px-SMPTE_Color_Bars.svg.png")
                ), 0);
                fragment.show(getSupportFragmentManager(), null);
            }
        }

        private void testNetwork() {
            Request request = new Request.Builder().url("https://dummyjson.com/carts").build();

            ConnectionManager.shared().addResponseInterceptor(new HTTPStatusCodeResponseInterceptor());

            ConnectionManager.shared().send(request, new ResponseJSONSerializer(), response -> {
                Log.d("NETWORK REQUEST", response.request.toString());
                Log.d("NETWORK REQUEST", response.request.headers().toString());
                if (!response.success() && response.error != null) {
                    Snackbar snackbar = Snackbar.make(rootView, response.error.getLocalizedMessage(), Snackbar.LENGTH_LONG);
                    snackbar.setAction("TEST", v -> {});
                    snackbar.show();
                    Log.d("NETWORK ERROR", response.error.getLocalizedMessage());
                }
                else {
                    Log.d("NETWORK SUCCESS", response.value.toString());
                }
            });
        }
    }

}
