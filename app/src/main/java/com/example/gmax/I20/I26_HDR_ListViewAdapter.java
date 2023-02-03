package com.example.gmax.I20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gmax.R;

import java.util.ArrayList;

public class I26_HDR_ListViewAdapter extends BaseAdapter {

    private ArrayList<I26_HDR> listViewItem = new ArrayList<I26_HDR>();

    public I26_HDR_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_i26_hdr, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView mv_req_seq         = (TextView) convertView.findViewById(R.id.mv_req_seq);
        TextView item_cd            = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm            = (TextView) convertView.findViewById(R.id.item_nm);
        TextView mv_req_qty         = (TextView) convertView.findViewById(R.id.mv_req_qty);
        TextView dlvy_dt            = (TextView) convertView.findViewById(R.id.dlvy_dt);

        TextView issue_sl_cd        = (TextView) convertView.findViewById(R.id.issue_sl_cd);
        TextView issue_location     = (TextView) convertView.findViewById(R.id.issue_location);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I26_HDR item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        mv_req_seq.setText(item.getMV_REQ_SEQ());
        item_cd.setText(item.getITEM_CD());
        item_nm.setText(item.getITEM_NM());
        mv_req_qty.setText(item.getMV_REQ_QTY());
        dlvy_dt.setText(item.getDLVY_DT());

        issue_sl_cd.setText(item.getISSUE_SL_CD());
        issue_location.setText(item.getISSUE_LOCATION());

        return convertView;
    }

    protected void addPlaceDtlItem(I26_HDR item) {
        listViewItem.add(item);
    }

    protected void add_Item(String MV_REQ_SEQ, String ITEM_CD, String ITEM_NM, String MV_REQ_QTY, String DLVY_DT) {
        I26_HDR item = new I26_HDR();

        item.setMV_REQ_SEQ(MV_REQ_SEQ);
        item.setMV_REQ_QTY(MV_REQ_QTY);
        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setDLVY_DT(DLVY_DT);

        listViewItem.add(item);
    }

    protected void addItem(I26_HDR item) {
        listViewItem.add(item);
    }
}
