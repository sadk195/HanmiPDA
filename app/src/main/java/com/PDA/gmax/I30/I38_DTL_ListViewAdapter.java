package com.PDA.gmax.I30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.gmax.R;

import java.util.ArrayList;

public class I38_DTL_ListViewAdapter extends BaseAdapter {

    private ArrayList<I38_DTL> listViewItem = new ArrayList<I38_DTL>();

    public I38_DTL_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_i38_dtl, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView tracking_no        = (TextView) convertView.findViewById(R.id.tracking_no);
        TextView lot_no             = (TextView) convertView.findViewById(R.id.lot_no);
        TextView good_on_hand_qty   = (TextView) convertView.findViewById(R.id.good_on_hand_qty);
        TextView bad_on_hand_qty    = (TextView) convertView.findViewById(R.id.bad_on_hand_qty);

        TextView sl_cd              = (TextView) convertView.findViewById(R.id.sl_cd);
        TextView item_cd            = (TextView) convertView.findViewById(R.id.item_cd);
        TextView lot_sub_no         = (TextView) convertView.findViewById(R.id.lot_sub_no);
        TextView basic_unit         = (TextView) convertView.findViewById(R.id.basic_unit);
        TextView location           = (TextView) convertView.findViewById(R.id.location);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I38_DTL item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        tracking_no.setText(item.getTRACKING_NO());
        lot_no.setText(item.getLOT_NO());
        good_on_hand_qty.setText(item.getGOOD_ON_HAND_QTY());
        bad_on_hand_qty.setText(item.getBAD_ON_HAND_QTY());

        sl_cd.setText(item.getSL_CD());
        item_cd.setText(item.getITEM_CD());
        lot_sub_no.setText(item.getLOT_SUB_NO());
        basic_unit.setText(item.getBASIC_UNIT());
        location.setText(item.getLOCATION());

        return convertView;
    }

    public void add_Item(String TRACKING_NO, String LOT_NO, String GOOD_ON_HAND_QTY, String BAD_ON_HAND_QTY, String SL_CD, String ITEM_CD, String LOT_SUB_NO, String BASIC_UNIT, String LOCATION) {
        I38_DTL item = new I38_DTL();

        item.setTRACKING_NO(TRACKING_NO);
        item.setLOT_NO(LOT_NO);
        item.setGOOD_ON_HAND_QTY(GOOD_ON_HAND_QTY);
        item.setBAD_ON_HAND_QTY(BAD_ON_HAND_QTY);

        item.setSL_CD(SL_CD);
        item.setITEM_CD(ITEM_CD);
        item.setLOT_SUB_NO(LOT_SUB_NO);
        item.setBASIC_UNIT(BASIC_UNIT);
        item.setLOCATION(LOCATION);

        listViewItem.add(item);
    }

    protected void addDTLItem(I38_DTL item) {
        listViewItem.add(item);
    }

}
