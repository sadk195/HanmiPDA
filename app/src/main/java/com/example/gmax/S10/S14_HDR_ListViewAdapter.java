package com.example.gmax.S10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gmax.R;

import java.util.ArrayList;

public class S14_HDR_ListViewAdapter extends BaseAdapter {

    private ArrayList<S14_HDR> listViewItem = new ArrayList<S14_HDR>();

    public S14_HDR_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_s14_hdr, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView item_cd            = (TextView) convertView.findViewById(R.id.item_cd);
        TextView dn_req_no          = (TextView) convertView.findViewById(R.id.dn_req_no);
        TextView sl_nm              = (TextView) convertView.findViewById(R.id.sl_nm);
        TextView item_nm            = (TextView) convertView.findViewById(R.id.item_nm);

        TextView good_on_hand_qty   = (TextView) convertView.findViewById(R.id.good_on_hand_qty);
        TextView location           = (TextView) convertView.findViewById(R.id.location);
        TextView location_qty       = (TextView) convertView.findViewById(R.id.location_qty);
        TextView REQ_QTY2           = (TextView) convertView.findViewById(R.id.REQ_QTY2);
        TextView DN_GI_QTY          = (TextView) convertView.findViewById(R.id.DN_GI_QTY);
        /*
        TextView so_type = (TextView) convertView.findViewById(R.id.so_type);
        TextView plant_cd = (TextView) convertView.findViewById(R.id.plant_cd);

        TextView sl_nm = (TextView) convertView.findViewById(R.id.sl_nm);
        TextView dn_req_no = (TextView) convertView.findViewById(R.id.dn_req_no);
        TextView promise_dt = (TextView) convertView.findViewById(R.id.promise_dt);

         */

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        S14_HDR item2 = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        item_cd.setText(item2.getITEM_CD());
        dn_req_no.setText(item2.getDN_REQ_NO());
        sl_nm.setText(item2.getSL_NM());
        item_nm.setText(item2.getITEM_NM());
        good_on_hand_qty.setText(item2.getGOOD_ON_HAND_QTY());
        location.setText(item2.getLOCATION());
        location_qty.setText(item2.getLOCATION_QTY());
        REQ_QTY2.setText(item2.getREQ_QTY2());
        DN_GI_QTY.setText(item2.getDN_GI_QTY());

        return convertView;
    }

    public void addShipmentItem(String DN_REQ_NO, String PLANT_CD, String SL_CD, String SL_NM, String ITEM_CD, String ITEM_NM, String REQ_QTY, String GOOD_ON_HAND_QTY, String LOCATION, String TRACKING_NO, String LOCATION_QTY) {
        S14_HDR item = new S14_HDR();

        item.setDN_REQ_NO(DN_REQ_NO);
        item.setPLANT_CD(PLANT_CD);
        item.setSL_CD(SL_CD);
        item.setSL_NM(SL_NM);
        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setREQ_QTY(REQ_QTY);
        item.setGOOD_ON_HAND_QTY(GOOD_ON_HAND_QTY);
        item.setLOCATION(LOCATION);
        item.setTRACKING_NO(TRACKING_NO);
        item.setLOCATION_QTY(LOCATION_QTY);

        listViewItem.add(item);
    }

    public void addShipmentHdrItem(S14_HDR item) {
        listViewItem.add(item);
    }

}
