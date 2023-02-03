package com.example.gmax.S10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gmax.R;

import java.util.ArrayList;

public class S12_LOT_ListViewAdapter extends BaseAdapter {

    private ArrayList<S12_LOT> listViewItem = new ArrayList<S12_LOT>();

    public S12_LOT_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_s12_lot, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView lot_no   = (TextView) convertView.findViewById(R.id.lot_no);
        TextView scan_qty = (TextView) convertView.findViewById(R.id.scan_qty);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        S12_LOT item2 = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        lot_no.setText(item2.getLOT_NO());
        scan_qty.setText(item2.getSCAN_QTY());

        return convertView;
    }

    public void addShipmentItem(String LOT_NO, String SCAN_QTY) {
        S12_LOT item = new S12_LOT();

        item.setLOT_NO(LOT_NO);
        item.setSCAN_QTY(SCAN_QTY);

        listViewItem.add(item);
    }

    protected void addShipmentHDRItem(S12_LOT item) {
        listViewItem.add(item);
    }
}
