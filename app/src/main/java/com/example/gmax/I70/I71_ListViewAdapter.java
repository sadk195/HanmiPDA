package com.example.gmax.I70;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gmax.R;

import java.util.ArrayList;

public class I71_ListViewAdapter extends BaseAdapter {

    private ArrayList<I71_ARRAYLIST> listViewItem = new ArrayList<I71_ARRAYLIST>();

    public I71_ListViewAdapter()
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
            convertView = inflater.inflate(R.layout.list_view_i71, parent, false);
        }



        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        //CheckedTextView CHK = (CheckedTextView) convertView.findViewById(R.id.chk);
        TextView CNT = (TextView) convertView.findViewById(R.id.CNT);
        TextView INV_NO = (TextView) convertView.findViewById(R.id.INV_NO);
        //TextView SL_CD = (TextView) convertView.findViewById(R.id.SL_CD);
        TextView SL_NM = (TextView) convertView.findViewById(R.id.SL_NM);
        //TextView WC_CD = (TextView) convertView.findViewById(R.id.WC_CD);
        TextView WC_NM = (TextView) convertView.findViewById(R.id.WC_NM);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I71_ARRAYLIST item = listViewItem.get(position);
        /*
        if (item.getCHK() == "Y") {
            CHK.setSelected(true);
            CHK.setChecked(true);
        }
        else {
            CHK.setChecked(false);
        }

        */

        // 아이템 내 각 위젯에 데이터 반영
        //CHK.setText(item.getCHK());
        CNT.setText(item.getCNT());
        INV_NO.setText(item.getINV_NO());
        SL_NM.setText(item.getSL_NM());
        WC_NM.setText(item.getWC_NM());



        return convertView;

    }

    public void addItem(String pCNT, String pINV_NO, String pSL_CD, String pSL_NM, String pWC_CD, String pWC_NM) {
        I71_ARRAYLIST item = new I71_ARRAYLIST();

        //item.setCNT(pCHK);
        item.setCNT(pCNT);
        item.setINV_NO(pINV_NO);
        item.setSL_CD(pSL_CD);
        item.setSL_NM(pSL_NM);
        item.setWC_CD(pWC_CD);
        item.setWC_NM(pWC_NM);

        listViewItem.add(item);
    }
}
