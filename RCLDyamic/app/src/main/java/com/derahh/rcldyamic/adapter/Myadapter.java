package com.derahh.rcldyamic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.derahh.rcldyamic.ILoadMore;
import com.derahh.rcldyamic.R;
import com.derahh.rcldyamic.model.Item;

import java.util.List;

class LoadingViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressBar);
    }
}

class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName, tvLength;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_name);
        tvLength = itemView.findViewById(R.id.tv_length);
    }
}

public class Myadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    ILoadMore loadMore;
    boolean isLoading;
    Activity activity;
    List<Item> items;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;

    public Myadapter(RecyclerView recyclerView, Activity activity, List<Item> items) {
        this.activity = activity;
        this.items = items;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null)
                        loadMore.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_layout, parent,false);
            return new ItemViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_loading, parent,false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Item item = items.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.tvName.setText(item.getName());
            viewHolder.tvLength.setText(String.valueOf(item.getLength()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoaded() {
        isLoading = false;
    }
}
