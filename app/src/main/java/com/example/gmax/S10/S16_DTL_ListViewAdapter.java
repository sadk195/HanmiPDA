package com.example.gmax.S10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gmax.R;

import java.util.ArrayList;

public class S16_DTL_ListViewAdapter extends BaseAdapter {

    private ArrayList<S16_DTL> listViewItem = new ArrayList<S16_DTL>();

    public S16_DTL_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_s16_dtl, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        //CheckedTextView chk = (CheckedTextView) convertView.findViewById(R.id.chk);
        TextView item_cd            = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm            = (TextView) convertView.findViewById(R.id.item_nm);
        TextView gi_qty             = (TextView) convertView.findViewById(R.id.gi_qty);
        TextView good_on_hand_qty   = (TextView) convertView.findViewById(R.id.good_on_hand_qty);
        TextView location           = (TextView) convertView.findViewById(R.id.location);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        S16_DTL item = listViewItem.get(position);

        /*
        // 아이템 내 각 위젯에 데이터 반영
        if (item.getCHK() == "Y") {
            chk.setSelected(true);
        }
        else {
            chk.setChecked(false);
        }

         */
        item_cd.setText(item.getITEM_CD());
        item_nm.setText(item.getITEM_NM());
        gi_qty.setText(item.getGI_QTY());
        good_on_hand_qty.setText(item.getGOOD_ON_HAND_QTY());
        location.setText(item.getLOCATION());

        return convertView;
    }

    public void add_Shipment_Dtl_Item(String ITEM_CD, String ITEM_NM, String GI_QTY, String good_on_hand_qty) {
        S16_DTL item = new S16_DTL();

        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setGI_QTY(GI_QTY);
        item.setGOOD_ON_HAND_QTY(good_on_hand_qty);

        listViewItem.add(item);
    }

    public void add_Shipment_Dtl_Item(S16_DTL Item) {
        listViewItem.add(Item);
    }

}
