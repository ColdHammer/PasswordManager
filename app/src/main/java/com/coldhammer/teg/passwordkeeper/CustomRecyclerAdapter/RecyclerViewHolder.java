package com.coldhammer.teg.passwordkeeper.CustomRecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coldhammer.teg.passwordkeeper.R;
import com.coldhammer.teg.passwordkeeper.Serializer.GObject;
import com.coldhammer.teg.passwordkeeper.Serializer.GObjectSerializer;

import java.util.HashMap;

/**
 * Created by Coldhammer on 1/9/2020.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder{
    private TextView etService;
    private TextView etUsername;
    public LinearLayout linearLayout;
    private Context context;
    public RecyclerViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        etService = itemView.findViewById(R.id.service);
        etUsername = itemView.findViewById(R.id.username);
        linearLayout = itemView.findViewById(R.id.item_layout);
    }

    public void updateView(GObjectSerializer keySet, int position)
    {
        etService.setText(keySet.getString(position, "service"));
        etUsername.setText(keySet.getString(position, "username"));
    }
}
