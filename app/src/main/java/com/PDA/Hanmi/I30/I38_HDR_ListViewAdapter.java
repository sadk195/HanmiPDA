package com.PDA.Hanmi.I30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.Hanmi.R;

import java.util.ArrayList;

public class I38_HDR_ListViewAdapter extends BaseAdapter {

    private ArrayList<I38_HDR> listViewItem = new ArrayList<I38_HDR>();

    public I38_HDR_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_i38_hdr, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        TextView item_cd                = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm                = (TextView) convertView.findViewById(R.id.item_nm);
        TextView remain_qty             = (TextView) convertView.findViewById(R.id.remain_qty);
        TextView issued_qty             = (TextView) convertView.findViewById(R.id.issued_qty);
        TextView location               = (TextView) convertView.findViewById(R.id.location);

        TextView sl_cd                  = (TextView) convertView.findViewById(R.id.sl_cd);
        TextView qty                    = (TextView) convertView.findViewById(R.id.qty);
        TextView tracking_no            = (TextView) convertView.findViewById(R.id.tracking_no);
        TextView wms_good_on_hand_qty   = (TextView) convertView.findViewById(R.id.wms_good_on_hand_qty);
        TextView prodt_order_no         = (TextView) convertView.findViewById(R.id.prodt_order_no);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I38_HDR item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        item_cd.setText(item.getITEM_CD());
        item_nm.setText(item.getITEM_NM());
        remain_qty.setText(item.getREQ_QTY());
        issued_qty.setText(item.getISSUED_QTY());
        location.setText(item.getLOCATION());

        sl_cd.setText(item.getSL_CD());
        qty.setText(item.getOUT_QTY());
        tracking_no.setText(item.getTRACKING_NO());
        wms_good_on_hand_qty.setText(item.getWMS_GOOD_ON_HAND_QTY());
        prodt_order_no.setText(item.getPRODT_ORDER_NO());

        return convertView;
    }

    public void add_Item(String ITEM_CD, String ITEM_NM, String REQ_QTY, String ISSUED_QTY, String LOCATION, String SL_CD, String OUT_QTY, String TRACKING_NO, String PRODT_ORDER_NO, String REMAIN_QTY, String WMS_GOOD_ON_HAND_QTY) {
        I38_HDR item = new I38_HDR();

        item.setPRODT_ORDER_NO(PRODT_ORDER_NO);
        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setREQ_QTY(REQ_QTY);
        item.setISSUED_QTY(ISSUED_QTY);
        item.setLOCATION(LOCATION);

        item.setSL_CD(SL_CD);
        item.setOUT_QTY(OUT_QTY);
        item.setTRACKING_NO(TRACKING_NO);
        item.setPRODT_ORDER_NO(PRODT_ORDER_NO);

        item.setREMAIN_QTY(REMAIN_QTY);
        item.setWMS_GOOD_ON_HAND_QTY(WMS_GOOD_ON_HAND_QTY);

        listViewItem.add(item);
    }

    protected void addHDRItem(I38_HDR item) {
        listViewItem.add(item);
    }

}
