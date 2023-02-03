package com.example.gmax.M30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gmax.R;

import java.util.ArrayList;

public class M31_HDR_ListViewAdapter extends BaseAdapter {

    private ArrayList<M31_HDR> listViewItem = new ArrayList<M31_HDR>();

    public M31_HDR_ListViewAdapter()
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
            convertView = inflater.inflate(R.layout.list_view_m31_hdr, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView dlv_no = (TextView) convertView.findViewById(R.id.dlv_no);
        TextView ser_no = (TextView) convertView.findViewById(R.id.ser_no);
        TextView item_cd = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm = (TextView) convertView.findViewById(R.id.item_nm);
        TextView dlv_qty = (TextView) convertView.findViewById(R.id.dlv_qty);
        TextView confirm_dlv_qty = (TextView) convertView.findViewById(R.id.confirm_dlv_qty);
        TextView inspect_flg = (TextView) convertView.findViewById(R.id.inspect_flg);
        TextView pur_type = (TextView) convertView.findViewById(R.id.pur_type);
        TextView spec = (TextView) convertView.findViewById(R.id.spec);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        M31_HDR item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        dlv_no.setText(item.getDLV_NO());
        ser_no.setText(item.getSER_NO());
        item_cd.setText(item.getITEM_CD());
        item_nm.setText(item.getITEM_NM());
        dlv_qty.setText(item.getDLV_QTY());
        confirm_dlv_qty.setText(item.getCONFIRM_DLV_QTY());
        inspect_flg.setText(item.getINSPECT_FLG());
        pur_type.setText(item.getPUR_TYPE());
        spec.setText(item.getSPEC());

        return convertView;
    }

    public void add_Item(String DLV_NO, String SER_NO, String ITEM_CD, String ITEM_NM, String DLV_QTY, String CONFIRM_DLV_QTY, String INSPECT_FLG, String PUR_TYPE, String SPEC)
    {
        M31_HDR item = new M31_HDR();

        item.setDLV_NO(DLV_NO);
        item.setSER_NO(SER_NO);
        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setDLV_QTY(DLV_QTY);
        item.setCONFIRM_DLV_QTY(CONFIRM_DLV_QTY);
        item.setINSPECT_FLG(INSPECT_FLG);
        item.setPUR_TYPE(PUR_TYPE);
        item.setSPEC(SPEC);

        listViewItem.add(item);
    }



}
