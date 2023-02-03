package com.example.gmax.M40;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gmax.R;

import java.util.ArrayList;

public class M41_QUERY_ListViewAdapter extends BaseAdapter {

    private ArrayList<M41_QUERY> listViewItem = new ArrayList<M41_QUERY>();

    public M41_QUERY_ListViewAdapter() {

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

    public void ClearItem() { listViewItem.clear(); }


    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_m41_query, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        LinearLayout ContentView    = (LinearLayout)convertView.findViewById(R.id.ContentView);
        TextView code              = (TextView) convertView.findViewById(R.id.code);        //품명
        TextView no                = (TextView) convertView.findViewById(R.id.no);          //넓이

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        M41_QUERY item2 = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        code.setText(item2.getCODE());
        no.setText(item2.getNO());

        return convertView;
    }


    public void addItem(String CODE,String NO) {
        M41_QUERY item = new M41_QUERY();

        item.setCODE(CODE);
        item.setNO(NO);

    }

    protected void addPkgItem(M41_QUERY item) {
        listViewItem.add(item);
    }


}
