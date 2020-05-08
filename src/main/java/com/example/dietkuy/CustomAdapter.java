package com.example.dietkuy;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> implements Filterable {

    private Cari cari;
    private List<ProductModel> modelList;
    private List<ProductModel> modelListAll;

    public CustomAdapter(Cari cari, List<ProductModel> modelList) {
        this.cari = cari;
        this.modelList = modelList;
        modelListAll = new ArrayList<>(modelList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_single, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String titleMakanan = modelList.get(position).getMakanan();
                String titleKalori = String.valueOf(modelList.get(position).getKalori());
                String titleBerat = String.valueOf(modelList.get(position).getBerat());

                Intent intent = new Intent(cari, FoodList.class);
                intent.putExtra("NamaMakanan", titleMakanan);
                intent.putExtra("JumlahKalori", titleKalori);
                intent.putExtra("BeratMakanan", titleBerat);
                cari.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.makanan.setText(modelList.get(position).getMakanan());
        holder.berat.setText(modelList.get(position).getBerat() + "gr");
        holder.kalori.setText(modelList.get(position).getKalori() + "kal");
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ProductModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(modelListAll);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ProductModel model : modelListAll) {
                    if (model.getMakanan().toLowerCase().contains(filterPattern)) {
                        filteredList.add(model);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            modelList.clear();
            modelList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
