package com.PDA.Hanmi.I30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.Hanmi.R;

import java.util.ArrayList;

public class I34_QUERY_ListViewAdapter extends BaseAdapter {

    private ArrayList<I34_QUERY> listViewItem = new ArrayList<I34_QUERY>();

    public I34_QUERY_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_i34_query, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView prodt_order_no     = (TextView) convertView.findViewById(R.id.prodt_order_no);
        TextView item_cd            = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm            = (TextView) convertView.findViewById(R.id.item_nm);
        TextView qty                = (TextView) convertView.findViewById(R.id.qty);
        TextView tracking_no        = (TextView) convertView.findViewById(R.id.tracking_no);
        TextView document_dt        = (TextView) convertView.findViewById(R.id.document_dt);
        TextView sl_nm              = (TextView) convertView.findViewById(R.id.sl_nm);
        TextView wc_nm              = (TextView) convertView.findViewById(R.id.wc_nm);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I34_QUERY item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        prodt_order_no.setText(item.getPRODT_ORDER_NO());
        item_cd.setText(item.getITEM_CD());
        item_nm.setText(item.getITEM_NM());
        qty.setText(item.getQTY());
        tracking_no.setText(item.getTRACKING_NO());
        document_dt.setText(item.getDOCUMENT_DT());
        sl_nm.setText(item.getSL_NM());
        wc_nm.setText(item.getWC_NM());

        return convertView;
    }

    public void add_item(String PRODT_ORDER_NO, String DOCUMENT_DT, String ITEM_CD, String ITEM_NM, String QTY, String TRACKING_NO, String SL_NM, String WC_NM) {
        I34_QUERY item = new I34_QUERY();

        item.setPRODT_ORDER_NO(PRODT_ORDER_NO);
        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setQTY(QTY);
        item.setTRACKING_NO(TRACKING_NO);
        item.setDOCUMENT_DT(DOCUMENT_DT);
        item.setSL_NM(SL_NM);
        item.setWC_NM(WC_NM);

        listViewItem.add(item);
    }

    protected void addQueryItem(I34_QUERY item) {
        listViewItem.add(item);
    }

}
