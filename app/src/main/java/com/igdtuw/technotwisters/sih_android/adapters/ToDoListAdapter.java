package com.igdtuw.technotwisters.sih_android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.model.ToDoListContents;

import java.util.ArrayList;

public class ToDoListAdapter extends ArrayAdapter<ToDoListContents> {

    Context context;
    ArrayList<ToDoListContents> objects;
    public ToDoListAdapter(Context context, ArrayList<ToDoListContents> objects) {
        super(context, 0, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context, R.layout.to_do_list_layout , null);
        }
        TextView titleView = (TextView) convertView.findViewById(R.id.titleTextView);
        TextView dateView = (TextView) convertView.findViewById(R.id.dateTextView);
        titleView.setText(objects.get(position).getTitle());
        dateView.setText(objects.get(position).getDate());
        convertView.setBackgroundColor(objects.get(position).getColor());
        return convertView;
    }
}
