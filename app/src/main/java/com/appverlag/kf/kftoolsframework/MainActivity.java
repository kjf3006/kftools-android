package com.appverlag.kf.kftoolsframework;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.codewaves.stickyheadergrid.StickyHeaderGridAdapter;
import com.codewaves.stickyheadergrid.StickyHeaderGridLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StickyHeaderGridLayoutManager(1));
        recyclerView.setAdapter(new Adapter());

    }

     /*
    adapter
    */

    private static class Adapter extends StickyHeaderGridAdapter {

        private List<Section> data;

        public Adapter() {
            setupData();
        }

        private void setupData() {
            data = new ArrayList<>();

            data.add(new Section("UI-Components", Arrays.asList("KFThemes", "KFFormComponents", "KFYoutube")));
            data.add(new Section("Data-Handling", Arrays.asList("KFCache", "KFManagedObjects")));
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

                Intent intent = new Intent(v.getContext(), ThemeActivity.class);
                v.getContext().startActivity(intent);
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


        private static class Section {

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
            if (id.equals("KFFormComponents")) {

            }
        }
    }

}
