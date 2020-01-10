package com.coldhammer.teg.passwordkeeper.CustomAdaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.io.Serializable;

public abstract class CustomAdaptorItem extends RecyclerView.ViewHolder{
    protected long Id;
    protected int viewID;
    protected int layoutId;
    protected Context context;

    public CustomAdaptorItem(int layoutId, Context context)
    {
        super(inflateView(context, layoutId));
        this.context = context;
        this.layoutId = layoutId;
        Id = 0;
    }

    public View inflateView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutId(), null);
        return view;
    }

    public static View inflateView(Context context, int layoutId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutId, null);
        return view;
    }


    public void setViewId(int id){viewID = id;}
    public int getViewId(){return viewID;}
    public long getId(){return Id;}
    public void setId(long id){Id = id;}
    public int getLayoutId(){return layoutId;}

    public abstract Object getHandler(View view);
    public abstract String getStringToFilter();
    public abstract void updateView(Object view);
}
