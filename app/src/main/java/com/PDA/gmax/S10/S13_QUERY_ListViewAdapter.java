package com.PDA.gmax.S10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.gmax.R;

import java.util.ArrayList;

public class S13_QUERY_ListViewAdapter extends BaseAdapter {

    private ArrayList<S13_QUERY> listViewItem = new ArrayList<S13_QUERY>();

    public S13_QUERY_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_s13_query, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView dn_no           = (TextView) convertView.findViewById(R.id.dn_no);
        TextView bp_nm              = (TextView) convertView.findViewById(R.id.bp_nm);
        TextView promise_dt         = (TextView) convertView.findViewById(R.id.promise_dt);
        TextView sales_grp_nm           = (TextView) convertView.findViewById(R.id.sales_grp_nm);
        TextView trans_meth          = (TextView) convertView.findViewById(R.id.trans_meth);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        S13_QUERY item2 = listViewItem.get(position);

        dn_no.setText(item2.getDN_NO());
        bp_nm.setText(item2.getBP_NM());
        promise_dt.setText(item2.getPROMISE_DT());
        sales_grp_nm.setText(item2.getSALES_GRP_NM());
        trans_meth.setText(item2.getTRANS_METH());

        return convertView;
    }

    public void addShipmentItem(String DN_NO, String MOV_TYPE, String MOV_TYPE_NM, String SHIP_TO_PARTY, String BP_NM, String PROMISE_DT,
      String DLVY_DT, String ACTUAL_GI_DT, String GOODS_MV_NO, String SALES_GRP, String SALES_GRP_NM,
       String TRANS_METH, String TRANS_METH_NM, String SO_NO, String DN_REQ_NO) {

        S13_QUERY item = new S13_QUERY();

        item.setDN_NO(DN_NO);
        item.setMOV_TYPE(MOV_TYPE);
        item.setMOV_TYPE_NM(MOV_TYPE_NM);
        item.setSHIP_TO_PARTY(SHIP_TO_PARTY);
        item.setBP_NM(BP_NM);
        item.setPROMISE_DT(PROMISE_DT);
        item.setDLVY_DT(DLVY_DT);
        item.setACTUAL_GI_DT(ACTUAL_GI_DT);
        item.setGOODS_MV_NO(GOODS_MV_NO);
        item.setSALES_GRP(SALES_GRP);
        item.setSALES_GRP_NM(SALES_GRP_NM);
        item.setTRANS_METH(TRANS_METH);
        item.setTRANS_METH_NM(TRANS_METH_NM);
        item.setSO_NO(SO_NO);

        listViewItem.add(item);
    }

    protected void addShipmentHDRItem(S13_QUERY item) {
        listViewItem.add(item);
    }
}
