package com.example.tqapp.common;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public abstract class BindAdapter<VB extends ViewBinding, Data> extends RecyclerView.Adapter<BindHolder<VB>> {
    private List<Data> data = new ArrayList<>();

    public List<Data> getData() {
        return data;
    }

    @NonNull
    @Override
    public BindHolder<VB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BindHolder<>(createHolder(parent));
    }

    public abstract VB createHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(@NonNull BindHolder<VB> holder, int position) {
        Data d = data.get(position);
        bind(holder.getVb(), d, position);
    }

    public abstract void bind(VB vb, Data data, int position);

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refresh(List<Data> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void loadImage(ImageView view, String image) {
        Glide.with(view).load(image).into(view);
    }
}
