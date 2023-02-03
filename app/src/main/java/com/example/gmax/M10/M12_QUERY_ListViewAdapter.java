package com.example.gmax.M10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gmax.R;

import java.util.ArrayList;

public class M12_QUERY_ListViewAdapter extends BaseAdapter {

    private ArrayList<M12_QUERY> listViewItem = new ArrayList<M12_QUERY>();

    public M12_QUERY_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_m12_query, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView INSPECT_REQ_NO     = (TextView) convertView.findViewById(R.id.INSPECT_REQ_NO);
        TextView MVMT_RCPT_DT       = (TextView) convertView.findViewById(R.id.MVMT_RCPT_DT);
        TextView INSPECT_GOOD_QTY   = (TextView) convertView.findViewById(R.id.INSPECT_GOOD_QTY);
        TextView INSPECT_BAD_QTY    = (TextView) convertView.findViewById(R.id.INSPECT_BAD_QTY);
        TextView BP_NM              = (TextView) convertView.findViewById(R.id.BP_NM);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        M12_QUERY item = listViewItem.get(position);

        INSPECT_REQ_NO.setText(item.getINSPECT_REQ_NO());
        MVMT_RCPT_DT.setText(item.getMVMT_RCPT_DT());
        INSPECT_GOOD_QTY.setText(item.getINSPECT_GOOD_QTY());
        INSPECT_BAD_QTY.setText(item.getINSPECT_BAD_QTY());
        BP_NM.setText(item.getBP_NM());

        return convertView;
    }

    public void add_Item(String INSPECT_REQ_NO, String MVMT_RCPT_DT, String INSPECT_GOOD_QTY, String INSPECT_BAD_QTY, String BP_NM) {
        M12_QUERY item = new M12_QUERY(INSPECT_REQ_NO, MVMT_RCPT_DT, INSPECT_GOOD_QTY, INSPECT_BAD_QTY, BP_NM);

        listViewItem.add(item);
    }

    protected void addQueryItem(M12_QUERY item) {
        listViewItem.add(item);
    }
}
