package com.example.gmax.S10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gmax.R;

import java.util.ArrayList;

public class S17_Shipment_Status_ListViewAdapter extends BaseAdapter {

    private ArrayList<S17_Shipment_Status> listViewItem = new ArrayList<S17_Shipment_Status>();

    public S17_Shipment_Status_ListViewAdapter()
    {

    }

    @Override
    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    public int getCount() { return listViewItem.size(); }

    @Override
    public Object getItem(int position) { return listViewItem.get(position); }

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
            convertView = inflater.inflate(R.layout.list_view_s17_shipment_status, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView dn_req_no = (TextView) convertView.findViewById(R.id.dn_req_no);
        TextView bp_nm = (TextView) convertView.findViewById(R.id.bp_nm);
        TextView promise_dt = (TextView) convertView.findViewById(R.id.promise_dt);
        TextView wms_good_on_hand_qty = (TextView) convertView.findViewById(R.id.wms_good_on_hand_qty);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        S17_Shipment_Status item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        dn_req_no.setText(item.getDN_REQ_NO());
        bp_nm.setText(item.getSHIP_TO_PARTY_NM());
        promise_dt.setText(item.getPROMISE_DT());
        wms_good_on_hand_qty.setText(item.getWMS_GOOD_ON_HAND_QTY());

        return convertView;
    }

    public void add_Item(String SHIP_TO_PARTY_NM, String DN_REQ_NO, String PROMISE_DT, String WMS_GOOD_ON_HAND_QTY)
    {
        S17_Shipment_Status item = new S17_Shipment_Status();

        item.setSHIP_TO_PARTY_NM(SHIP_TO_PARTY_NM);
        item.setDN_REQ_NO(DN_REQ_NO);
        item.setPROMISE_DT(PROMISE_DT);
        item.setWMS_GOOD_ON_HAND_QTY(WMS_GOOD_ON_HAND_QTY);

        listViewItem.add(item);
    }



}
