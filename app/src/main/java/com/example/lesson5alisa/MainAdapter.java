package com.example.lesson5alisa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>  {

    public List<MainModel> list;
    public Context context;
    private ItemClickListener listener;


    public MainAdapter(List<MainModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void addData(MainModel model){
        list.add(model);
        notifyDataSetChanged();
    }
    public void changeData(MainModel model, int position) {
        list.set(position, model);
        notifyItemChanged(position);
    }


    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_recycler_item,parent,false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, final int position) {
        holder.onBind(list.get(position));
        holder.popMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.card_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                list.remove(list.get(position));
                                break;
                            case R.id.call:
                                String number = list.get(position).getNumber();
                                Uri call = Uri.parse("Телефон: " + number);
                                Intent intent = new Intent(Intent.ACTION_DIAL, call);
                                context.startActivity(intent);
                                break;
                        }
                        return false;
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtName;
        TextView txtPhone;
        ImageView imageView;
        ImageView popMenu;
        MainModel model;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            imageView = itemView.findViewById(R.id.imageView);
            popMenu = itemView.findViewById(R.id.pop_menu);
        }

        public void onBind(MainModel model) {
            this.model = model;
            txtPhone.setText(model.getNumber());
            txtName.setText(model.getName());

            if (model.getImageVIew() != null) {
                Glide.with(context)
                        .load(model.getImageVIew())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageView);
            }
        }

        @Override
        public void onClick(View view) {
            if (listener !=null){
                listener.onItemClick(getAdapterPosition());
            }
        }
    }

    public void setOnClickListener(ItemClickListener mListener){
        this.listener = mListener;
    }

    public interface ItemClickListener{
        void onItemClick(int position);
    }
}
