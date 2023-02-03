package com.example.gmax.S10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gmax.R;

import java.util.ArrayList;

public class S12_HDR_ListViewAdapter extends BaseAdapter {

    private ArrayList<S12_HDR> listViewItem = new ArrayList<S12_HDR>();

    public S12_HDR_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_s12_hdr, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView ship_to_party_nm   = (TextView) convertView.findViewById(R.id.ship_to_party_nm);
        TextView dn_no              = (TextView) convertView.findViewById(R.id.dn_no);
        TextView detail_count       = (TextView) convertView.findViewById(R.id.detail_count);

        TextView ship_to_party      = (TextView) convertView.findViewById(R.id.ship_to_party);
        TextView mov_type           = (TextView) convertView.findViewById(R.id.mov_type);
        TextView so_type            = (TextView) convertView.findViewById(R.id.so_type);
        TextView plant_cd           = (TextView) convertView.findViewById(R.id.plant_cd);
        TextView sl_cd              = (TextView) convertView.findViewById(R.id.sl_cd);
        TextView trans_meth         = (TextView) convertView.findViewById(R.id.trans_meth);

        TextView sl_nm              = (TextView) convertView.findViewById(R.id.sl_nm);

        TextView start_dt           = (TextView) convertView.findViewById(R.id.start_dt);
        TextView end_dt             = (TextView) convertView.findViewById(R.id.end_dt);
        TextView dn_req_no          = (TextView) convertView.findViewById(R.id.dn_req_no);
        TextView promise_dt         = (TextView) convertView.findViewById(R.id.promise_dt);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        S12_HDR item2 = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        ship_to_party_nm.setText(item2.getSHIP_TO_PARTY_NM());
        dn_no.setText(item2.getDN_NO());
        detail_count.setText(item2.getDE_COUNT());

        ship_to_party.setText(item2.getSHIP_TO_PARTY());
        mov_type.setText(item2.getMOV_TYPE());
        so_type.setText(item2.getSO_TYPE());
        plant_cd.setText(item2.getPLANT_CD());
        sl_cd.setText(item2.getSL_CD());
        trans_meth.setText(item2.getTRANS_METH());

        start_dt.setText(item2.getSTART_DT());
        end_dt.setText(item2.getEND_DT());

        dn_req_no.setText(item2.getDN_REQ_NO());
        promise_dt.setText(item2.getPROMISE_DT());

        return convertView;
    }

    public void addShipmentItem(String SHIP_TO_PARTY_NM, String DN_NO, String DE_COUNT, String SHIP_TO_PARTY, String MOV_TYPE, String SO_TYPE, String PLANT_CD, String SL_CD, String TRANS_METH, String START_DT, String END_DT, String DN_REQ_NO, String PROMISE_DT) {
        S12_HDR item = new S12_HDR();

        item.setDN_NO(DN_NO);
        item.setSHIP_TO_PARTY_NM(SHIP_TO_PARTY_NM);
        item.setDE_COUNT(DE_COUNT);
        item.setSHIP_TO_PARTY(SHIP_TO_PARTY);
        item.setMOV_TYPE(MOV_TYPE);
        item.setSO_TYPE(SO_TYPE);
        item.setPLANT_CD(PLANT_CD);
        item.setSL_CD(SL_CD);
        item.setTRANS_METH(TRANS_METH);
        item.setSTART_DT(START_DT);
        item.setEND_DT(END_DT);
        item.setDN_REQ_NO(DN_REQ_NO);
        item.setPROMISE_DT(PROMISE_DT);

        listViewItem.add(item);
    }

    protected void addShipmentHDRItem(S12_HDR item) {
        listViewItem.add(item);
    }
}
