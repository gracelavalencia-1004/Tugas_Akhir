package com.example.dietkuy;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter2 extends RecyclerView.Adapter<ViewHolder2> {

    CatatanHarian catatanHarian;
    List<ProductModel2> model2List;
    Context context;

    public CustomAdapter2(CatatanHarian catatanHarian, List<ProductModel2> model2List) {
        this.catatanHarian = catatanHarian;
        this.model2List = model2List;
    }

    @NonNull
    @Override
    public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single2, parent, false);

        ViewHolder2 viewHolder2 = new ViewHolder2(itemView);
        viewHolder2.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        return viewHolder2;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder2 holder, int position) {
        holder.namaMakanan.setText(model2List.get(position).getMakanan());
        holder.jumlahKalori.setText(model2List.get(position).getKalori());
    }

    @Override
    public int getItemCount() {
        return model2List.size();
    }
}
