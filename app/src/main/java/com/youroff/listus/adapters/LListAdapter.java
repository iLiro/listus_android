package com.youroff.listus.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youroff.listus.R;
import com.youroff.listus.models.LList;

import java.util.List;

public class LListAdapter extends RecyclerView.Adapter<LListAdapter.LListHolder>  {

    private LayoutInflater inflater;
    private List<LList> lists;
    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public LListAdapter(List<LList> lists, Context c) {
        inflater = LayoutInflater.from(c);
        this.lists = lists;
    }

    @Override
    public LListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_row, parent, false);
        return new LListHolder(view);
    }

    @Override
    public void onBindViewHolder(LListHolder holder, int position) {
        LList list = lists.get(position);
        holder.listName.setText(list.getName());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class LListHolder extends RecyclerView.ViewHolder {

        private TextView listName;
        private CardView container;

        public LListHolder(View itemView) {
            super(itemView);

            listName = (TextView)itemView.findViewById(R.id.listName);
            container = (CardView)itemView.findViewById(R.id.container);
            container.setOnClickListener((i) -> {
                itemClickCallback.onItemClick(getAdapterPosition());
            });
        }
    }
}
