package com.PDA.gmax.S10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.gmax.R;

import java.util.ArrayList;

public class S11_QUERY_ListViewAdapter extends BaseAdapter {

    private ArrayList<S11_QUERY> listViewItem = new ArrayList<S11_QUERY>();

    public S11_QUERY_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_s11_query, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView insrt_dt      = (TextView) convertView.findViewById(R.id.insrt_dt);
        TextView bp_nm         = (TextView) convertView.findViewById(R.id.bp_nm);
        TextView promise_dt    = (TextView) convertView.findViewById(R.id.promise_dt);
        TextView item_cnt      = (TextView) convertView.findViewById(R.id.item_cnt);
        TextView dn_req_no     = (TextView) convertView.findViewById(R.id.dn_req_no);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        S11_QUERY item2 = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        insrt_dt.setText(item2.getINSRT_DT());
        bp_nm.setText(item2.getBP_NM());
        promise_dt.setText(item2.getPROMISE_DT());

        item_cnt.setText(item2.getITEM_CNT());
        dn_req_no.setText(item2.getDN_REQ_NO());
        return convertView;
    }

    public void addShipmentItem(String DN_REQ_NO,String INSRT_DT, String SHIP_TO_PARTY, String BP_NM, String SALES_GRP,
                                String SALES_GRP_NM, String PROMISE_DT, String ITEM_CNT, String DLVY_DT) {
        S11_QUERY item = new S11_QUERY();

        item.setDN_REQ_NO(DN_REQ_NO);
        item.setINSRT_DT(INSRT_DT);
        item.setSHIP_TO_PARTY(SHIP_TO_PARTY);
        item.setBP_NM(BP_NM);
        item.setSALES_GRP(SALES_GRP);
        item.setSALES_GRP_NM(SALES_GRP_NM);
        item.setPROMISE_DT(PROMISE_DT);
        item.setITEM_CNT(ITEM_CNT);
        item.setDLVY_DT(DLVY_DT);

        listViewItem.add(item);
    }

    protected void addShipmentHDRItem(S11_QUERY item) {
        listViewItem.add(item);
    }
}
