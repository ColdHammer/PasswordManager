package com.coldhammer.teg.passwordkeeper.CustomRecyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.coldhammer.teg.passwordkeeper.ItemPage;
import com.coldhammer.teg.passwordkeeper.MainActivity;
import com.coldhammer.teg.passwordkeeper.PasswordItem;
import com.coldhammer.teg.passwordkeeper.R;
import com.coldhammer.teg.passwordkeeper.Serializer.GObjectSerializer;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Coldhammer on 1/9/2020.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> implements Filterable {
    public GObjectSerializer keySet;
    private GObjectSerializer displayedKeySet;
    private Context context;
    private RecyclerViewAdapter subContext;
    private Filter filter = null;
    private static final String TAG = "RecyclerViewAdapter";

    public class Filter extends android.widget.Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            if(charSequence == null || charSequence.length() == 0)
            {
                results.values = keySet;
                results.count = keySet.size();
            }else{
                GObjectSerializer itemList = new GObjectSerializer();
                for(HashMap<String, String> item: keySet)
                {
                    String toCompare = item.get("username") + item.get("service");
                    if(toCompare
                            .toUpperCase()
                            .contains(charSequence
                                    .toString()
                                    .toUpperCase()))
                    {
                        itemList.add(item);
                    }
                }
                results.values = itemList;
                results.count = itemList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if(filterResults.count != 0)
            {
                displayedKeySet = (GObjectSerializer) filterResults.values;
                notifyDataSetChanged();
            }
        }
    }

    public void update(HashMap<String, String> item, int index)
    {
        HashMap<String,String> set = displayedKeySet.get(index);
        int position = keySet.indexOf(set);
        Log.d(TAG, "update: " + String.format("Index: %1$d, Position: %2$d", index, position));
        displayedKeySet.update(index, item);
        keySet.update(position, item);
        notifyDataSetChanged();
    }

    public void add(HashMap<String, String> item)
    {
        Log.d(TAG, "add: " + GObjectSerializer.keySetToStringBuffer(item, null).toString());
        Log.d(TAG, "add | dataString: " + keySet.toString());
        //displayedKeySet.add(item);
        keySet.add(item);
        notifyDataSetChanged();
    }

    public void remove(int index)
    {
        HashMap<String, String> set = displayedKeySet.get(index);
        displayedKeySet.remove(set);
        keySet.remove(set);
        notifyDataSetChanged();
    }

    public RecyclerViewAdapter(Context context)
    {
        this.context = context;
        subContext = this;
        keySet = new GObjectSerializer();
        displayedKeySet = new GObjectSerializer();
    }
    public RecyclerViewAdapter(Context context, GObjectSerializer serializer){

        this.context = context;
        subContext = this;
        keySet = serializer;
        displayedKeySet = serializer;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.password_item, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.updateView(displayedKeySet, position);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).launchItemPage(false, GObjectSerializer.keySetToStringBuffer(displayedKeySet.get(position), null).toString(), position);
            }
        });
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(TAG, "onLongClick: " + String.valueOf(position));
                remove(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return displayedKeySet.size();
    }

    @Override
    public Filter getFilter() {

        if(filter == null) filter = new Filter();
        return filter;
    }
}
