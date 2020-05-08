package com.example.dietkuy;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder3 extends RecyclerView.ViewHolder {

    TextView makanan, berat, kalori;
    View view;

    public ViewHolder3(@NonNull View itemView) {
        super(itemView);
        view = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

        makanan = itemView.findViewById(R.id.Makanan);
        berat = itemView.findViewById(R.id.Berat);
        kalori = itemView.findViewById(R.id.Kalori);
    }

    private ViewHolder3.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder3.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
