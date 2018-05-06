package com.usman.treasurehuntgame.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.usman.treasurehuntgame.R;

import java.util.ArrayList;



// This class is made because we are using customAdapter to make listview because we are simply using default arraylist

public class StageAdapter extends ArrayAdapter<String> {
    private static String TAG = "StageAdapter";
    private Context mContext;
    int mResource;

    public StageAdapter(Context context, @LayoutRes int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String name = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvName = (TextView) convertView.findViewById(R.id.stage_name_tv);
        tvName.setText(name);

        return convertView;
    }

}