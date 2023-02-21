package com.PDA.gmax.S10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.gmax.R;

import java.util.ArrayList;

public class S11_DTL_ListViewAdapter extends BaseAdapter {

    private ArrayList<S11_DTL> listViewItem = new ArrayList<S11_DTL>();

    public S11_DTL_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_s11_dtl, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView item_cd            = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm            = (TextView) convertView.findViewById(R.id.item_nm);
        TextView spec               = (TextView) convertView.findViewById(R.id.spec);
        TextView req_qty            = (TextView) convertView.findViewById(R.id.req_qty);
        TextView gi_qty             = (TextView) convertView.findViewById(R.id.gi_qty);
        TextView good_on_hand_qty   = (TextView) convertView.findViewById(R.id.good_on_hand_qty);
        TextView sl_nm              = (TextView) convertView.findViewById(R.id.sl_nm);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        S11_DTL item2 = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        item_cd.setText(item2.getITEM_CD());
        item_nm.setText(item2.getITEM_NM());
        spec.setText(item2.getSPEC());
        req_qty.setText(item2.getREQ_QTY());
        gi_qty.setText(item2.getGI_QTY());
        good_on_hand_qty.setText(item2.getGOOD_ON_HAND_QTY());
        sl_nm.setText(item2.getSL_NM());
        return convertView;
    }

    public void addShipmentItem(String ITEM_CD,String ITEM_NM, String SPEC, String REQ_QTY, String GI_QTY,
                                String GOOD_ON_HAND_QTY, String SL_CD, String SL_NM, String DN_REQ_SEQ) {
        S11_DTL item = new S11_DTL();

        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setSPEC(SPEC);
        item.setREQ_QTY(REQ_QTY);
        item.setGI_QTY(GI_QTY);
        item.setGOOD_ON_HAND_QTY(GOOD_ON_HAND_QTY);
        item.setSL_CD(SL_CD);
        item.setSL_NM(SL_NM);
        item.setDN_REQ_SEQ(DN_REQ_SEQ);

        listViewItem.add(item);
    }

    protected void addShipmentHDRItem(S11_DTL item) {
        listViewItem.add(item);
    }
}
