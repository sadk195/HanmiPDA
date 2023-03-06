package com.PDA.Hanmi.I10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.Hanmi.R;

import java.util.ArrayList;

public class I13_QUERY_ListViewAdapter extends BaseAdapter {

    private ArrayList<I13_QUERY> listViewItem = new ArrayList<I13_QUERY>();

    public I13_QUERY_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_i13_query, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView ITEM_CD            = (TextView) convertView.findViewById(R.id.ITEM_CD);
        TextView OPT                = (TextView) convertView.findViewById(R.id.OPT);
        TextView GOOD_ON_HAND_QTY   = (TextView) convertView.findViewById(R.id.GOOD_ON_HAND_QTY);
        TextView BAD_ON_HAND_QTY    = (TextView) convertView.findViewById(R.id.BAD_ON_HAND_QTY);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I13_QUERY item = listViewItem.get(position);

        ITEM_CD.setText(item.getITEM_CD());
        OPT.setText(item.getOPT());
        GOOD_ON_HAND_QTY.setText(item.getGOOD_ON_HAND_QTY());
        BAD_ON_HAND_QTY.setText(item.getBAD_ON_HAND_QTY());

        return convertView;
    }

    public void add_Item(String ITEM_CD, String OPT, String GOOD_ON_HAND_QTY, String BAD_ON_HAND_QTY) {
        I13_QUERY item = new I13_QUERY();

        item.setITEM_CD(ITEM_CD);
        item.setOPT(OPT);
        item.setGOOD_ON_HAND_QTY(GOOD_ON_HAND_QTY);
        item.setBAD_ON_HAND_QTY(BAD_ON_HAND_QTY);

        listViewItem.add(item);
    }

    protected void addQueryItem(I13_QUERY item) {
        listViewItem.add(item);
    }
}
