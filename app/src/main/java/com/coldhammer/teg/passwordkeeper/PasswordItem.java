package com.coldhammer.teg.passwordkeeper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.coldhammer.teg.passwordkeeper.CustomAdaptor.CustomAdaptorItem;
import com.coldhammer.teg.passwordkeeper.Serializer.GObject;
import com.coldhammer.teg.passwordkeeper.Serializer.GObjectSerializer;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Coldhammer on 12/31/2019.
 */

public class PasswordItem extends CustomAdaptorItem implements GObject {
    HashMap<String, String> keyValues;
    public static final int AMOUNT_OF_VIEWS = 4;
    public static final int LAYOUT_ID = R.layout.password_item;
    ViewHolder vh;

    public static PasswordItem getItem(String string, Context context)
    {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
        return new PasswordItem(LAYOUT_ID, context, GObjectSerializer.createHashMap(string));
    }

    public PasswordItem(int layoutId, Context context, HashMap<String, String> keySet) {
        super(layoutId, context);
        this.keyValues = keySet;
    }

    @Override
    public String toString()
    {
        return GObjectSerializer.keySetToStringBuffer(keyValues, null).toString();
    }

    @Override
    public HashMap<String, String> keyValueToSerialize() {
        return keyValues;
    }

    class ViewHolder implements Serializable{
        TextView service;
        TextView username;
    }

    @Override
    public Object getHandler(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.service = view.findViewById(R.id.service);
        viewHolder.username = view.findViewById(R.id.username);
        return viewHolder;
    }

    public void refreshValues(HashMap<String, String> keyValues)
    {
        this.keyValues = keyValues;
        setUpText(vh);
    }

    @Override
    public String getStringToFilter() {
        return keyValues.get("username") + keyValues.get("service");
    }

    @Override
    public void updateView(Object view) {
        vh = (ViewHolder) view;
        setUpText(vh);
    }

    public void setUpText(ViewHolder vh)
    {
        vh.service.setText(keyValues.get("service"));
        vh.username.setText(keyValues.get("username"));
    }
}
