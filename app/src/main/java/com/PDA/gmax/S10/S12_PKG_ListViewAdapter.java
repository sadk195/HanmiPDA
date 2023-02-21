package com.PDA.gmax.S10;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.gmax.R;

import java.util.ArrayList;

public class S12_PKG_ListViewAdapter extends BaseAdapter {

    private ArrayList<S12_PKG> listViewItem = new ArrayList<S12_PKG>();

    public S12_PKG_ListViewAdapter() {

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

    public void ClearItem() { listViewItem.clear(); }


    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_s12_pkg, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView item_cd            = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm            = (TextView) convertView.findViewById(R.id.item_nm);
        TextView req_qty            = (TextView) convertView.findViewById(R.id.req_qty);
        //TextView req_stock          = (TextView) convertView.findViewById(R.id.req_stock);
        TextView packing_cnt        = (TextView) convertView.findViewById(R.id.packing_cnt);
        TextView qty                = (TextView) convertView.findViewById(R.id.qty);
        TextView carton_no          = (TextView) convertView.findViewById(R.id.carton_no);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        S12_PKG item2 = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        item_cd.setText(item2.getITEM_CD());
        item_nm.setText(item2.getITEM_NM());
        req_qty.setText(item2.getREQ_QTY());
        //req_stock.setText(item2.getREQ_STOCK());

        packing_cnt.setText(item2.getPACKING_CNT());
        qty.setText(item2.getQTY());
        carton_no.setText(item2.getCARTON_NO());

        if(parent.isSelected()){
            item_cd.setBackgroundColor(Color.parseColor("#e5e5e5"));
        }
        return convertView;
    }



    public void addShipmentItem(String ITEM_CD, String ITEM_NM, String REQ_QTY, String PACKING_CNT, String QTY,String CONT_NO) {
        S12_PKG item = new S12_PKG();

        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setREQ_QTY(REQ_QTY);
        //item.setREQ_STOCK(REQ_STOCK);
        item.setPACKING_CNT(PACKING_CNT);
        item.setQTY(QTY);
        item.setCARTON_NO(CONT_NO);
        listViewItem.add(item);
    }

    protected void addShipmentPKGItem(S12_PKG item) {
        listViewItem.add(item);
    }

    public void setShipmentItem(String ITEM_CD,String PACKING_CNT,String QTY) {


        System.out.println("setshipmentitem");
        int listIdx=0;
        for (S12_PKG pkg:listViewItem) {
            if(pkg.ITEM_CD.equals(ITEM_CD)){
                break;
            }
            listIdx++;
        }
        System.out.println("listIdx:"+listIdx);

        S12_PKG item = (S12_PKG)listViewItem.get((listIdx));

        item.setITEM_CD(ITEM_CD);
        //item.setITEM_NM(ITEM_NM);
        //item.setREQ_QTY(REQ_QTY);
        //item.setREQ_STOCK(REQ_STOCK);
        item.setPACKING_CNT(PACKING_CNT);
        item.setQTY(QTY);

        System.out.println("ITEM_CD:"+ITEM_CD);
        System.out.println("PACKING_CNT:"+PACKING_CNT);
        System.out.println("QTY:"+QTY);

        listViewItem.set(listIdx,item);
    }

    protected void setShipmentPKGItem(S12_PKG item) {
        int listIdx=0;
        for (S12_PKG pkg:listViewItem) {
            if(pkg.ITEM_CD.equals(item.ITEM_CD))
            {
                break;
            }
            listIdx++;
        }
        listViewItem.set(listIdx,item);
    }


}
