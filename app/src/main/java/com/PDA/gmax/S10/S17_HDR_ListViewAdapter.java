package com.PDA.gmax.S10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.gmax.R;

import java.util.ArrayList;

public class S17_HDR_ListViewAdapter extends BaseAdapter {

    private ArrayList<S17_HDR> listViewItem = new ArrayList<S17_HDR>();

    public S17_HDR_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_s17_hdr, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView item_cd            = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm            = (TextView) convertView.findViewById(R.id.item_nm);
        TextView req_qty            = (TextView) convertView.findViewById(R.id.req_qty);
        TextView wms_qty            = (TextView) convertView.findViewById(R.id.wms_qty);
        TextView good_on_hand_qty   = (TextView) convertView.findViewById(R.id.good_on_hand_qty);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        S17_HDR item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        item_cd.setText(item.getITEM_CD());
        item_nm.setText(item.getITEM_NM());
        wms_qty.setText(item.getWMS_QTY());
        req_qty.setText(item.getREQ_QTY());
        good_on_hand_qty.setText(item.getGOOD_ON_HAND_QTY());

        return convertView;
    }

    public void addShipmentItem(String ITEM_CD, String ITEM_NM, String WMS_QTY, String GOOD_ON_HAND_QTY, String REQ_QTY) {
        S17_HDR item = new S17_HDR();

        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setWMS_QTY(WMS_QTY);
        item.setGOOD_ON_HAND_QTY(GOOD_ON_HAND_QTY);
        item.setREQ_QTY(REQ_QTY);

        listViewItem.add(item);
    }

    protected void addHDRItem(S17_HDR item) {
        listViewItem.add(item);
    }

}
