package com.example.gmax;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class String_ListViewAdapter extends BaseAdapter {

    private ArrayList<String> listViewItem = new ArrayList<String>();

    public String_ListViewAdapter() {

    }

    @Override
    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    public int getCount() {
        return listViewItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_s12_lotsearch, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView St_textview   = (TextView) convertView.findViewById(R.id.St_textview);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        String item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        St_textview.setText(item);

        return convertView;
    }

    public void addListItem(String text) {

        listViewItem.add(text);
    }

}
