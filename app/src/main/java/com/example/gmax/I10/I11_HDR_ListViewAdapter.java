package com.example.gmax.I10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gmax.R;

import java.util.ArrayList;

public class I11_HDR_ListViewAdapter extends BaseAdapter {

    private ArrayList<I11_HDR> listViewItem = new ArrayList<I11_HDR>();

    public I11_HDR_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_i11_hdr, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView location           = (TextView) convertView.findViewById(R.id.location);
        TextView item_cd            = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm            = (TextView) convertView.findViewById(R.id.item_nm);
        TextView good_on_hand_qty   = (TextView) convertView.findViewById(R.id.good_on_hand_qty);
        TextView item_acct_nm       = (TextView) convertView.findViewById(R.id.item_acct_nm);

        TextView sl_cd              = (TextView) convertView.findViewById(R.id.sl_cd);
        TextView sl_nm              = (TextView) convertView.findViewById(R.id.sl_nm);
        TextView plant_cd           = (TextView) convertView.findViewById(R.id.plant_cd);
        TextView tracking_no        = (TextView) convertView.findViewById(R.id.tracking_no);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I11_HDR item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        location.setText(item.getLOCATION());
        item_cd.setText(item.getITEM_CD());
        item_nm.setText(item.getITEM_NM());
        good_on_hand_qty.setText(item.getGOOD_ON_HAND_QTY());
        item_acct_nm.setText(item.getITEM_ACCT_NM());

        sl_cd.setText(item.getSL_CD());
        sl_nm.setText(item.getSL_NM());
        plant_cd.setText(item.getPLANT_CD());
        tracking_no.setText(item.getTRACKING_NO());

        return convertView;
    }

    public void add_Loading_Place_Item(String LOCATION, String ITEM_CD, String ITEM_NM, String GOOD_ON_HAND_QTY, String ITEM_ACCT_NM, String SL_CD, String PLANT_CD, String TRACKING_NO) {
        I11_HDR item = new I11_HDR();

        item.setLOCATION(LOCATION);
        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setGOOD_ON_HAND_QTY(GOOD_ON_HAND_QTY);
        item.setITEM_ACCT_NM(ITEM_ACCT_NM);
        item.setSL_CD(SL_CD);
        item.setPLANT_CD(PLANT_CD);
        item.setTRACKING_NO(TRACKING_NO);

        listViewItem.add(item);
    }

    protected void addHDRItem(I11_HDR item) {
        listViewItem.add(item);
    }
}
