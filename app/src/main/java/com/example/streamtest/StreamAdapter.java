package com.example.streamtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import helppocket.ItemMapping;

/**
 * Created by User on 01.03.2016.
 */
public class StreamAdapter extends BaseAdapter {
    List<ItemMapping> itemMappingList;
    Context context;
    LayoutInflater layoutInflater;

    public StreamAdapter(List<ItemMapping> itemMappingList, Context context) {
        this.itemMappingList = itemMappingList;
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static class ViewHolder {
        TextView textViewSportName;
        TextView textViewStreamName;
        ImageView imageView;
    }

    @Override
    public int getCount() {
        return itemMappingList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemMappingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewSportName = (TextView) convertView.findViewById(R.id.textViewSportInfo);
            viewHolder.textViewStreamName = (TextView) convertView.findViewById(R.id.textViewStream);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageViewList);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ItemMapping itemMapping = (ItemMapping) getItem(position);
        String sportName = itemMapping.getSportType();
        String streamName = itemMapping.getStreamName();

        viewHolder.textViewSportName.setText(sportName);
        viewHolder.textViewStreamName.setText(streamName);
        viewHolder.imageView.setBackgroundResource(R.drawable.tux);

        return convertView;
    }


}
