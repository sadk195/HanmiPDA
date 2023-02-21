package com.PDA.gmax.M10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.gmax.R;

import java.util.ArrayList;

public class M14_QUERY_ListViewAdapter extends BaseAdapter {

    private ArrayList<M14_QUERY> listViewItem = new ArrayList<M14_QUERY>();

    public M14_QUERY_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_m14_query, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView item_cd       = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm       = (TextView) convertView.findViewById(R.id.item_nm);
        TextView spec          = (TextView) convertView.findViewById(R.id.spec);
        TextView dn_no         = (TextView) convertView.findViewById(R.id.dn_no);
        TextView seq_no        = (TextView) convertView.findViewById(R.id.seq_no);
        TextView po_no         = (TextView) convertView.findViewById(R.id.po_no);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        M14_QUERY item2 = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        item_cd .setText(item2.getITEM_CD());
        item_nm .setText(item2.getITEM_NM());
        spec.setText(item2.getSPEC());
        dn_no.setText(item2.getDN_NO());
        seq_no.setText(item2.getSEQ_NO());
        po_no.setText(item2.getPO_NO());

        return convertView;
    }

    public void addShipmentItem(String ITEM_CD, String ITEM_NM, String SPEC, String DN_NO, String SEQ_NO, String PO_NO) {

        M14_QUERY item = new M14_QUERY();

        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setSPEC(SPEC);
        item.setDN_NO(DN_NO);
        item.setSEQ_NO(SEQ_NO);
        item.setPO_NO(PO_NO);

        listViewItem.add(item);
    }

    protected void addShipmentHDRItem(M14_QUERY item) {
        listViewItem.add(item);
    }
}
