package com.PDA.gmax.I70.M20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.gmax.R;

import java.util.ArrayList;

public class M22_QUERY_ListViewAdapter extends BaseAdapter {

    private ArrayList<M22_QUERY> listViewItem = new ArrayList<M22_QUERY>();

    public M22_QUERY_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_m22_query, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView INSPECT_REQ_NO     = (TextView) convertView.findViewById(R.id.INSPECT_REQ_NO);
        TextView INSP_QTY           = (TextView) convertView.findViewById(R.id.INSP_QTY);
        TextView G_QTY              = (TextView) convertView.findViewById(R.id.G_QTY);
        TextView B_QTY              = (TextView) convertView.findViewById(R.id.B_QTY);
        TextView PRODT_ORDER_NO     = (TextView) convertView.findViewById(R.id.PRODT_ORDER_NO);
        TextView LOCATION           = (TextView) convertView.findViewById(R.id.LOCATION);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        M22_QUERY item = listViewItem.get(position);

        INSPECT_REQ_NO.setText(item.getINSPECT_REQ_NO());
        INSP_QTY.setText(item.getINSP_QTY());
        G_QTY.setText(item.getG_QTY());
        B_QTY.setText(item.getB_QTY());
        PRODT_ORDER_NO.setText(item.getPRODT_ORDER_NO());
        LOCATION.setText(item.getLOCATION());

        return convertView;
    }

    public void add_Item(String INSPECT_REQ_NO, String INSP_QTY, String G_QTY, String B_QTY, String PRODT_ORDER_NO, String LOCATION) {
        M22_QUERY item = new M22_QUERY();

        item.setINSPECT_REQ_NO(INSPECT_REQ_NO);
        item.setINSP_QTY(INSP_QTY);
        item.setG_QTY(G_QTY);
        item.setB_QTY(B_QTY);
        item.setPRODT_ORDER_NO(PRODT_ORDER_NO);
        item.setLOCATION(LOCATION);

        listViewItem.add(item);
    }

    protected void addQueryItem(M22_QUERY item) {
        listViewItem.add(item);
    }
}
