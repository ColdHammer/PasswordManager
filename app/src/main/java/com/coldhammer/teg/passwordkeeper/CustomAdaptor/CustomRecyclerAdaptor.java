package com.coldhammer.teg.passwordkeeper.CustomAdaptor;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;

import java.util.ArrayList;

/**
 * Created by Coldhammer on 12/5/2019.
 */

public class CustomRecyclerAdaptor extends RecyclerView.Adapter implements Filterable {
    ArrayList<CustomAdaptorItem> itemList;
    ArrayList<CustomAdaptorItem> itemBackUpList;
    int amountOfViews;
    Filter filter;
    ArrayList<Integer> LayoutIds;

    public class Filter extends android.widget.Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            if(charSequence == null || charSequence.length() == 0)
            {
                results.values = itemBackUpList;
                results.count = itemBackUpList.size();
            }else{
                ArrayList<CustomAdaptorItem> nItemList = new ArrayList<>();
                for(CustomAdaptorItem item : itemBackUpList)
                {
                    if(item.getStringToFilter().toUpperCase().contains(charSequence.toString().toUpperCase()))
                    {
                        nItemList.add(item);
                    }
                }
                results.values = nItemList;
                results.count = nItemList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if(filterResults.count !=0)
            {
                itemList = (ArrayList<CustomAdaptorItem>) filterResults.values;
                notifyDataSetChanged();
            }
        }
    }


    public void remove(CustomAdaptorItem item)
    {
        itemList.remove(item);
        notifyDataSetChanged();
    }

    public void remove(int index)
    {
        itemList.remove(index);
        notifyDataSetChanged();
    }

    public CustomRecyclerAdaptor(int amountOfViews) {
        super();
        itemList = new ArrayList<>();
        itemBackUpList = new ArrayList<>();
        this.amountOfViews = amountOfViews;
        LayoutIds = new ArrayList<>();
    }

    public int add(CustomAdaptorItem item)
    {
        if(!LayoutIds.contains(item.getLayoutId())){
            if(LayoutIds.size() >= amountOfViews) return 0;
            LayoutIds.add(item.getLayoutId());
            item.setViewId(LayoutIds.indexOf(item.getLayoutId()));
        }else {
            item.setViewId(LayoutIds.indexOf(item.getLayoutId()));
        }
        itemList.add(item);
        itemBackUpList.add(item);
        notifyDataSetChanged();
        return itemList.size() -1;
    }

    public int getAmountOfItems()
    {
        return amountOfViews;
    }


    public ArrayList<CustomAdaptorItem> getItemList()
    {
        return itemList;
    }

    public int getCount() {
        return itemList.size();
    }

    public CustomAdaptorItem getCastedItem(int i)
    {
        return (CustomAdaptorItem) getItem(i);
    }

    public void setList(ArrayList<CustomAdaptorItem> list)
    {
        itemList = list;
        itemBackUpList = list;
    }

    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getCastedItem(i).getId();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return getCastedItem(position).getViewId();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        Object handler;
        if(view == null)
        {
            view = getCastedItem(i).inflateView();
            handler = getCastedItem(i).getHandler(view);
            view.setTag(handler);
        }else{
            handler = view.getTag();
        }
        getCastedItem(i).updateView(handler);
        return view;
    }

    @Override
    public Filter getFilter() {
        if(filter == null)
        {
            filter = new Filter();
        }
        return filter;
    }


}

