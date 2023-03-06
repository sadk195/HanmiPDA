package com.PDA.Hanmi.I30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.Hanmi.R;

import java.util.ArrayList;

public class I37_DTL_ListViewAdapter extends BaseAdapter {

    private ArrayList<I37_DTL> listViewItem = new ArrayList<I37_DTL>();

    public I37_DTL_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_i37_dtl, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView item_cd        = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm        = (TextView) convertView.findViewById(R.id.item_nm);
        TextView good_qty       = (TextView) convertView.findViewById(R.id.good_qty);
        TextView sl_cd          = (TextView) convertView.findViewById(R.id.sl_cd);
        TextView sl_nm          = (TextView) convertView.findViewById(R.id.sl_nm);

        TextView tracking_no    = (TextView) convertView.findViewById(R.id.tracking_no);
        //TextView good_on_hand_qty = (TextView) convertView.findViewById(R.id.good_on_hand_qty);
        TextView wms_qty        = (TextView) convertView.findViewById(R.id.wms_qty);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I37_DTL item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        item_cd.setText(item.getITEM_CD());
        item_nm.setText(item.getITEM_NM());
        good_qty.setText(item.getGOOD_QTY());
        sl_cd.setText(item.getSL_CD());
        sl_nm.setText(item.getSL_NM());
        tracking_no.setText(item.getTRACKING_NO());
        //good_on_hand_qty.setText(item.getGOOD_ON_HAND_QTY());
        wms_qty.setText(item.getWMS_QTY());

        return convertView;
    }

    public void add_Item(String ITEM_CD, String ITEM_NM, String GOOD_QTY, String SL_CD, String SL_NM, String TRACKING_NO, String WMS_QTY) {
        I37_DTL item = new I37_DTL();

        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setGOOD_QTY(GOOD_QTY);
        item.setSL_CD(SL_CD);
        item.setSL_NM(SL_NM);
        item.setTRACKING_NO(TRACKING_NO);
        //item.setGOOD_ON_HAND_QTY(GOOD_ON_HAND_QTY);
        item.setWMS_QTY(WMS_QTY);

        listViewItem.add(item);
    }

    protected void addDTLItem(I37_DTL item) {
        listViewItem.add(item);
    }

}
