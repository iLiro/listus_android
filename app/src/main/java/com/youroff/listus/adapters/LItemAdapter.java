package com.youroff.listus.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youroff.listus.R;
import com.youroff.listus.databinding.ItemRowBinding;
import com.youroff.listus.models.LItem;

import java.util.List;

public class LItemAdapter extends RecyclerView.Adapter<LItemAdapter.LItemHolder>  {

    private LayoutInflater inflater;
    private List<LItem> items;
    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public LItemAdapter(List<LItem> items, Context c) {
        inflater = LayoutInflater.from(c);
        this.items = items;
    }

    @Override
    public LItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_row, parent, true);
        return new LItemHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(LItemHolder holder, int position) {
        LItem item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class LItemHolder extends RecyclerView.ViewHolder {
        private CardView container;
        private ItemRowBinding binding;

        public LItemHolder(View itemView, ItemRowBinding binding) {
            super(itemView);
            this.binding = binding;
            container = (CardView)itemView.findViewById(R.id.container);
            container.setOnClickListener((i) -> {
                itemClickCallback.onItemClick(getAdapterPosition());
            });
        }

        public void setItem(LItem item) {
            binding.setItem(item);
        }
    }
}
