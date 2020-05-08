package com.example.dietkuy;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter3 extends RecyclerView.Adapter<ViewHolder3> implements Filterable {

    TambahResep tambahResep;
    List<ProductModel3> model3List;
    List<ProductModel3> model3ListAll;

    public CustomAdapter3(TambahResep tambahResep, List<ProductModel3> model3List) {
        this.tambahResep = tambahResep;
        this.model3List = model3List;
        model3ListAll = new ArrayList<>(model3List);
    }

    @NonNull
    @Override
    public ViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_single, parent, false);

        ViewHolder3 viewHolder3 = new ViewHolder3(itemView);
        viewHolder3.setOnClickListener(new ViewHolder3.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String titleMakanan = model3List.get(position).getMakanan();
                String titleKalori = String.valueOf(model3List.get(position).getKalori());
                String titleBerat = String.valueOf(model3List.get(position).getBerat());
                String titleID = model3List.get(position).getDocID();

                Intent intent = new Intent(tambahResep, RecipeList.class);
                intent.putExtra("NamaMakanan", titleMakanan);
                intent.putExtra("JumlahKalori", titleKalori);
                intent.putExtra("BeratMakanan", titleBerat);
                intent.putExtra("DocID", titleID);
                tambahResep.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        return viewHolder3;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder3 holder, int position) {
        holder.makanan.setText(model3List.get(position).getMakanan());
        holder.berat.setText(model3List.get(position).getBerat() + "gr");
        holder.kalori.setText(model3List.get(position).getKalori() + "kal");
    }

    @Override
    public int getItemCount() {
        return model3List.size();
    }

    @Override
    public Filter getFilter() {
        return AdapterFilter;
    }

    private Filter AdapterFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ProductModel3> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(model3ListAll);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ProductModel3 model3 : model3ListAll) {
                    if (model3.getMakanan().toLowerCase().contains(filterPattern)) {
                        filteredList.add(model3);
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
            model3List.clear();
            model3List.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
