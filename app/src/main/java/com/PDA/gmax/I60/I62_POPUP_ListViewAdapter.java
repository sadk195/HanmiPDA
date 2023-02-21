package com.PDA.gmax.I60;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.PDA.gmax.R;

import java.util.ArrayList;

public class I62_POPUP_ListViewAdapter extends BaseAdapter {

    private ArrayList<I62_POPUP> listViewItem = new ArrayList<I62_POPUP>();

    public I62_POPUP_ListViewAdapter()
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
            convertView = inflater.inflate(R.layout.list_view_i62_popup, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        CheckedTextView chk = (CheckedTextView) convertView.findViewById(R.id.chk);
        TextView location = (TextView) convertView.findViewById(R.id.location);
        TextView item_cd = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm = (TextView) convertView.findViewById(R.id.item_nm);
        TextView tracking_no = (TextView) convertView.findViewById(R.id.tracking_no);
        TextView lot_no = (TextView) convertView.findViewById(R.id.lot_no);
        TextView lot_sub_no = (TextView) convertView.findViewById(R.id.lot_sub_no);
        TextView good_on_hand_qty = (TextView) convertView.findViewById(R.id.good_on_hand_qty);
        TextView basic_unit = (TextView) convertView.findViewById(R.id.basic_unit);
        TextView sl_cd = (TextView) convertView.findViewById(R.id.sl_cd);
        TextView sl_nm = (TextView) convertView.findViewById(R.id.sl_nm);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I62_POPUP item = listViewItem.get(position);

        if (item.getCHK() == "Y") {
            chk.setSelected(true);
            chk.setChecked(true);
        }
        else {
            chk.setChecked(false);
        }

        // 아이템 내 각 위젯에 데이터 반영
        chk.setText(item.getCHK());
        location.setText(item.getLOCATION());
        item_cd.setText(item.getITEM_CD());
        item_nm.setText(item.getITEM_NM());
        tracking_no.setText(item.getTRACKING_NO());
        lot_no.setText(item.getLOT_NO());
        lot_sub_no.setText(item.getLOT_SUB_NO());
        good_on_hand_qty.setText(item.getGOOD_ON_HAND_QTY().replace(".0",""));
        basic_unit.setText(item.getBASIC_UNIT());
        sl_cd.setText(item.getSL_CD());
        sl_nm.setText(item.getSL_NM());

        return convertView;
    }

    public void add_Loading_Place_Item(String CHK, String LOCATION, String ITEM_CD, String ITEM_NM, String TRACKING_NO
                                    , String LOT_NO, String LOT_SUB_NO, String GOOD_ON_HAND_QTY, String BASIC_UNIT, String SL_CD, String SL_NM)
    {
        I62_POPUP item = new I62_POPUP();

        item.setCHK(CHK);
        item.setLOCATION(LOCATION);
        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setTRACKING_NO(TRACKING_NO);
        item.setLOT_NO(LOT_NO);
        item.setLOT_SUB_NO(LOT_SUB_NO);
        item.setGOOD_ON_HAND_QTY(GOOD_ON_HAND_QTY);
        item.setBASIC_UNIT(BASIC_UNIT);
        item.setSL_CD(SL_CD);
        item.setSL_NM(SL_NM);

        listViewItem.add(item);
    }



}
