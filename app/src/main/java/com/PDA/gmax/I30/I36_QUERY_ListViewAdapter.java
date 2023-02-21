package com.PDA.gmax.I30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.gmax.R;

import java.util.ArrayList;

public class I36_QUERY_ListViewAdapter extends BaseAdapter {

    private ArrayList<I36_QUERY> listViewItem = new ArrayList<I36_QUERY>();

    public I36_QUERY_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_i36_query, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView item_cd            = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm            = (TextView) convertView.findViewById(R.id.item_nm);
        TextView qty                = (TextView) convertView.findViewById(R.id.qty);
        TextView update_location    = (TextView) convertView.findViewById(R.id.update_location);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I36_QUERY item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        item_cd.setText(item.getITEM_CD());
        item_nm.setText(item.getITEM_NM());
        qty.setText(item.getQTY());
        update_location.setText(item.getUPDATE_LOCATION());

        return convertView;
    }

    public void add_Loading_Place_Query_Item(String ITEM_CD, String ITEM_NM, String QTY, String UPDATE_LOCATION) {
        I36_QUERY item = new I36_QUERY();

        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setQTY(QTY);
        item.setUPDATE_LOCATION(UPDATE_LOCATION);

        listViewItem.add(item);
    }

    protected void addQueryItem(I36_QUERY item) {
        listViewItem.add(item);
    }

}
