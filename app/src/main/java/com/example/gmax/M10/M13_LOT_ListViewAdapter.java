package com.example.gmax.M10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.gmax.R;

import java.util.ArrayList;

public class M13_LOT_ListViewAdapter extends BaseAdapter {


    private ArrayList<M13_DTL> listViewItem = new ArrayList<M13_DTL>();
    public M13_LOT_ListViewAdapter( ) {
    }

    @Override
    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    public int getCount() {
        return listViewItem.size();
    }
    public int getChkCount() {
        int cnt = 0;
        for(M13_DTL dtl : listViewItem){
            if(dtl.getCHK()){cnt++;}
        }
        return cnt;
    }

    public int getUnChkCount() {
        int cnt = 0;
        for(M13_DTL dtl : listViewItem){
            if(!dtl.getCHK()){cnt++;}
        }
        return cnt;
    }



    @Override
    public Object getItem(int position) {
        return listViewItem.get(position);
    }

    public ArrayList<M13_DTL> getLotArray() {
        return listViewItem;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    //리스트에서 체크된 항목 수 표시를 위해 textview를 메인에서 받아와 사용
    TextView textView;
    public void setTextView(TextView TextView){
        textView = TextView;
    }

    private void setChkcnt(){

        if(textView == null){
            return;
        }
        textView.setText(String.valueOf(getChkCount()));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_m13_lot, parent, false);
        }

        View view = convertView;
        // 아이템 내 View들을 저장할 Holder 생성


        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView lot_no          = (TextView) convertView.findViewById(R.id.lot_no);
        CheckBox chk             = (CheckBox) convertView.findViewById(R.id.chk);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        M13_DTL item2 = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        lot_no.setText(item2.getLOT_NO());
        chk.setChecked(item2.getCHK());


        //체크박스 체크하여 따로 저장 가능하도록 설정
        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item2.setCHK(chk.isChecked());
                //체크 된 항목 수 표시
                setChkcnt();
            }
        });

        return convertView;
    }

    public void addShipmentItem(String LOT_NO, boolean CHK ) {
        M13_DTL item = new M13_DTL();

        item.setLOT_NO(LOT_NO);
        item.setCHK(CHK);

        listViewItem.add(item);
    }

    protected void addShipmentHDRItem(M13_DTL item) {
        listViewItem.add(item);
    }
    protected void updatePkgItem(int idx,M13_DTL item) {
        listViewItem.set(idx,item);
    }

    //lot번호 받아서 체크박스 값 변경
    public void setCheck(String lot)
    {
        int position = 0;

        for(M13_DTL dtl : listViewItem){
            if(dtl.getLOT_NO().equals(lot)){

                break;
            }
            position++;
        }

        boolean temp = listViewItem.get(position).getCHK();
        if(temp){
            listViewItem.get(position).setCHK(false);
        }
        else{
            listViewItem.get(position).setCHK(true);
        }
    }

}
