package com.example.gmax.I70;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.gmax.R;

import java.util.ArrayList;

public class I73_ListViewAdapter extends BaseAdapter {

    private ArrayList<I73_ARRAYLIST> listViewItem = new ArrayList<I73_ARRAYLIST>();

    public I73_ListViewAdapter()
    {

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_i73, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        CheckedTextView chk = (CheckedTextView) convertView.findViewById(R.id.CHK_TEXT);
        TextView SL_NM = (TextView) convertView.findViewById(R.id.SL_NM);
        TextView LOCATION = (TextView) convertView.findViewById(R.id.LOCATION);
        TextView ITEM_NM = (TextView) convertView.findViewById(R.id.ITEM_NM);
        TextView QTY = (TextView) convertView.findViewById(R.id.QTY);
        TextView TRACKING_NO = (TextView) convertView.findViewById(R.id.TRACKING_NO);
        TextView INV_NO = (TextView) convertView.findViewById(R.id.INV_NO);
        TextView INV_SEQ = (TextView) convertView.findViewById(R.id.INV_SEQ);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I73_ARRAYLIST item = listViewItem.get(position);

        if (item.getCHK() == "Y") {
            chk.setSelected(true);
            chk.setChecked(true);
        }
        else {
            chk.setChecked(false);
        }

        // 아이템 내 각 위젯에 데이터 반영
        chk.setText(item.getCHK());
        SL_NM.setText(item.getSL_NM());
        LOCATION.setText(item.getLOCATION());
        ITEM_NM.setText(item.getITEM_CD());
        QTY.setText(item.getQTY());
        TRACKING_NO.setText(item.getTRACKING_NO());
        INV_NO.setText(item.getINV_NO());
        INV_SEQ.setText(item.getINV_SEQ());

        return convertView;

    }

    public void addItem(String pCHK, String pSL_CD,String pSL_NM, String pLOCATION, String pITEM_CD, String pITEM_NM, String pQTY, String pTRACKING_NO, String pINV_NO, String pINV_SEQ) {
        I73_ARRAYLIST item = new I73_ARRAYLIST();

        item.setCHK(pCHK);
        item.setSL_CD(pSL_CD);
        item.setSL_NM(pSL_NM);
        item.setLOCATION(pLOCATION);
        item.setITEM_CD(pITEM_CD);
        item.setITEM_NM(pITEM_NM);
        item.setQTY(pQTY);
        item.setTRACKING_NO(pTRACKING_NO);
        item.setINV_NO(pINV_NO);
        item.setINV_SEQ(pINV_SEQ);

        listViewItem.add(item);
    }
}
