package com.PDA.Hanmi.I30;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.PDA.Hanmi.R;

import java.util.ArrayList;

public class I37_HDR_ListViewAdapter extends BaseAdapter {

    private ArrayList<I37_HDR> listViewItem = new ArrayList<I37_HDR>();

    public I37_HDR_ListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.list_view_i37_hdr, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView prodt_order_no     = (TextView) convertView.findViewById(R.id.prodt_order_no);
        TextView opr_no             = (TextView) convertView.findViewById(R.id.opr_no);
        TextView item_cd            = (TextView) convertView.findViewById(R.id.item_cd);
        TextView item_nm            = (TextView) convertView.findViewById(R.id.item_nm);
        TextView tracking_no        = (TextView) convertView.findViewById(R.id.tracking_no);
        TextView req_qty            = (TextView) convertView.findViewById(R.id.req_qty);
        TextView base_unit          = (TextView) convertView.findViewById(R.id.base_unit);
        TextView sl_cd              = (TextView) convertView.findViewById(R.id.sl_cd);
        TextView sl_nm              = (TextView) convertView.findViewById(R.id.sl_nm);
        TextView issued_qty         = (TextView) convertView.findViewById(R.id.issued_qty);
        TextView remain_qty         = (TextView) convertView.findViewById(R.id.remain_qty);
        TextView req_no             = (TextView) convertView.findViewById(R.id.req_no);
        TextView issue_mthd         = (TextView) convertView.findViewById(R.id.issue_mthd);
        TextView resv_status        = (TextView) convertView.findViewById(R.id.resv_status);
        TextView out_qty            = (TextView) convertView.findViewById(R.id.out_qty);
        TextView seq_no             = (TextView) convertView.findViewById(R.id.seq_no);
        TextView location           = (TextView) convertView.findViewById(R.id.location);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        I37_HDR item = listViewItem.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        prodt_order_no.setText(item.getPRODT_ORDER_NO());
        opr_no.setText(item.getOPR_NO());
        item_cd.setText(item.getITEM_CD());
        item_nm.setText(item.getITEM_NM());
        tracking_no.setText(item.getTRACKING_NO());
        req_qty.setText(item.getREQ_QTY());
        base_unit.setText(item.getBASE_UNIT());
        sl_cd.setText(item.getSL_CD());
        sl_nm.setText(item.getSL_NM());
        issued_qty.setText(item.getISSUED_QTY());
        remain_qty.setText(item.getREMAIN_QTY());
        req_no.setText(item.getREQ_NO());
        issue_mthd.setText(item.getISSUE_MTHD());
        resv_status.setText(item.getRESV_STATUS());

        String location_st = item.getLOCATION();
        if (location_st.equals("출고대기장")) {
            out_qty.setText(item.getOUT_QTY());
        } else {
            out_qty.setText("");
        }
        seq_no.setText(item.getSEQ_NO());
        location.setText(item.getLOCATION());

        return convertView;
    }

    public void add_Item(String PRODT_ORDER_NO, String OPR_NO, String ITEM_CD, String ITEM_NM, String TRACKING_NO, String REQ_QTY, String BASE_UNIT,
                         String SL_CD, String SL_NM, String ISSUED_QTY, String REMAIN_QTY, String REQ_NO, String ISSUE_MTHD,
                         String RESV_STATUS, String OUT_QTY, String BAD_ON_HAND_QTY, String SEQ_NO, String LOCATION) {
        I37_HDR item = new I37_HDR();

        item.setPRODT_ORDER_NO(PRODT_ORDER_NO);
        item.setOPR_NO(OPR_NO);
        item.setITEM_CD(ITEM_CD);
        item.setITEM_NM(ITEM_NM);
        item.setTRACKING_NO(TRACKING_NO);
        item.setREQ_QTY(REQ_QTY);
        item.setBASE_UNIT(BASE_UNIT);
        item.setSL_CD(SL_CD);
        item.setSL_NM(SL_NM);
        item.setISSUED_QTY(ISSUED_QTY);
        item.setREMAIN_QTY(REMAIN_QTY);
        item.setREQ_NO(REQ_NO);
        item.setISSUE_MTHD(ISSUE_MTHD);
        item.setRESV_STATUS(RESV_STATUS);
        item.setOUT_QTY(OUT_QTY);
        item.setBAD_ON_HAND_QTY(BAD_ON_HAND_QTY);
        item.setSEQ_NO(SEQ_NO);
        item.setLOCATION(LOCATION);

        listViewItem.add(item);
    }

    protected void addHDRItem(I37_HDR item) {
        listViewItem.add(item);
    }

}
