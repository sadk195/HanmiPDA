package com.example.gmax.I20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gmax.BaseActivity;
import com.example.gmax.DBAccess;
import com.example.gmax.R;
import com.example.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class I22_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private I22_HDR vGetHDRItem;

    //== View 선언(TextView) ==//
    private TextView storage_location, location, item_cd, item_nm, good_on_hand_qty;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(EditText) ==//
    private EditText move_date, txt_Scan_move_SL_CD, move_qty;

    //== View 선언(Button) ==//
    private Button btn_add, btn_save;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i22_dtl);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }
    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID                 = getIntent().getStringExtra("MENU_ID");
        vMenuNm                 = getIntent().getStringExtra("MENU_NM");
        vMenuRemark             = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand           = getIntent().getStringExtra("START_COMMAND");

        vGetHDRItem             = (I22_HDR)getIntent().getSerializableExtra("HDR");

        //== ID 값 바인딩 ==//
        storage_location        = (TextView) findViewById(R.id.storage_location);
        location                = (TextView) findViewById(R.id.location);
        item_cd                 = (TextView) findViewById(R.id.item_cd);
        item_nm                 = (TextView) findViewById(R.id.item_nm);
        good_on_hand_qty        = (TextView) findViewById(R.id.good_on_hand_qty);

        listview                = (ListView) findViewById(R.id.listOrder);

        move_date               = (EditText) findViewById(R.id.move_date);
        txt_Scan_move_SL_CD     = (EditText) findViewById(R.id.txt_Scan_move_SL_CD);
        move_qty                = (EditText) findViewById(R.id.move_qty);

        btn_save                = (Button) findViewById(R.id.btn_save);
        btn_add                 = (Button) findViewById(R.id.btn_add);
    }

    private void initializeCalendar() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {

        move_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, move_date, cal);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 기존 소스 dbSave()로 옮김
                dbSave("EXIT");
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbSave("ADD");
            }
        });
    }

    private void initializeData() {

        storage_location.setText(vGetHDRItem.getSL_CD());
        location.setText(vGetHDRItem.getLOCATION());
        item_cd.setText(vGetHDRItem.getITEM_CD());
        item_nm.setText(vGetHDRItem.getITEM_NM());
        good_on_hand_qty.setText(vGetHDRItem.getGOOD_ON_HAND_QTY());

        move_date.setText(df.format(cal.getTime()));

        start();
    }

    private void start() {
        dbQuery(vGetHDRItem.getPLANT_CD(), vGetHDRItem.getITEM_CD(), vGetHDRItem.getSL_CD(), vGetHDRItem.getTRACKING_NO(), vGetHDRItem.getLOCATION());

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            I22_DTL_ListViewAdapter listViewAdapter = new I22_DTL_ListViewAdapter(); //
            I22_DTL item = new I22_DTL();

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                item.CHK                = ja.length() == 1 ? "Y" : jObject.getString("CHK");    //체크 이벤트
                item.TRACKING_NO        = jObject.getString("TRACKING_NO");                     //Tracking 번호
                item.LOT_NO             = jObject.getString("LOT_NO");                          //Lot번호
                item.GOOD_ON_HAND_QTY   = jObject.getString("GOOD_ON_HAND_QTY");                //양품수량
                item.BAD_ON_HAND_QTY    = jObject.getString("BAD_ON_HAND_QTY");                 //불량수량

                item.SL_CD              = jObject.getString("SL_CD");                           //창고코드
                item.ITEM_CD            = jObject.getString("ITEM_CD");                         //품목코드
                item.LOT_SUB_NO         = jObject.getString("LOT_SUB_NO");                      //LOT_SUB_NO
                item.BASIC_UNIT         = jObject.getString("BASIC_UNIT");                      //재고단위
                item.LOCATION           = jObject.getString("LOCATION");                        //재고단위

                listViewAdapter.addDTLItem(item);
            }

            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    I22_DTL item = (I22_DTL) parent.getItemAtPosition(position);
                    CheckedTextView chk = (CheckedTextView) v.findViewById(R.id.chk);

                    if (chk.isChecked() == true) {
                        chk.setChecked(false);
                        item.CHK = "";
                    } else {
                        chk.setChecked(true);
                        item.CHK = "Y";
                    }
                }
            });
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private void dbQuery(final String plant_cd_data, final String item_cd_data, final String sl_cd_data, final String tracking_no_data, final String location_cd) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DTL_GET_LIST ";
                sql += " @PLANT_CD = '" + plant_cd_data + "' ";
                sql += " ,@ITEM_CD = '" + item_cd_data + "' ";
                sql += " ,@SL_CD = '" + sl_cd_data + "' ";
                sql += " ,@TRACKING_NO = '" + tracking_no_data + "' ";
                sql += " ,@LOCATION_CD = '" + location_cd + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThd_dbQuery.start();   //스레드 시작
        try {
            workThd_dbQuery.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private void dbSave(String val) {
        String txt_Scan_move_SL_CD_data = txt_Scan_move_SL_CD.getText().toString();

        String move_qty_st = move_qty.getText().toString();

        if (txt_Scan_move_SL_CD_data == "" || move_qty_st.isEmpty()) {
            TGSClass.AlertMessage(getApplicationContext(), "이동적치장 또는 이동수량을 입력하여 주시기 바랍니다.");
        } else {
            I22_DTL_ListViewAdapter listViewAdapter = (I22_DTL_ListViewAdapter) listview.getAdapter();
            Double move_qty_int = Double.parseDouble(move_qty.getText().toString());

            for (int idx2 = 0; idx2 < listViewAdapter.getCount(); idx2++) {
                I22_DTL item = (I22_DTL) listViewAdapter.getItem(idx2);

                if (item.getCHK() == "Y") {
                    double qty = Double.parseDouble(item.getGOOD_ON_HAND_QTY());

                    if (qty < move_qty_int) {
                        TGSClass.AlertMessage(getApplicationContext(), "현재고수량보다 이동수량이 초과되었습니다.");
                        return;
                    }
                }
            }

            try {
                I22_DTL_ListViewAdapter listViewAdapter2 = (I22_DTL_ListViewAdapter) listview.getAdapter();

                for (int idx2 = 0; idx2 < listViewAdapter2.getCount(); idx2++) {
                    I22_DTL item = (I22_DTL) listViewAdapter2.getItem(idx2);

                    if (item.getCHK() == "Y") {
                        String sl_cd            = item.getSL_CD();
                        String item_cd          = item.getITEM_CD();
                        String tracking_no      = item.getTRACKING_NO();
                        String lot_no           = item.getLOT_NO();
                        String lot_sub_no       = item.getLOT_SUB_NO();
                        String qty              = item.getGOOD_ON_HAND_QTY();
                        String basic_unit       = item.getBASIC_UNIT();
                        String location         = item.getLOCATION();
                        String bad_on_hand_qty  = item.getBAD_ON_HAND_QTY();

                        BL_DATASET_SELECT(sl_cd, item_cd, tracking_no, lot_no, lot_sub_no, qty, basic_unit, location, bad_on_hand_qty);
                        TGSClass.AlertMessage(getApplicationContext(), result_msg);
                    }
                }

                // 저장 후 결과 값 돌려주기
                Intent resultIntent = new Intent();
                // 결과처리 후 부른 Activity에 보낼 값
                resultIntent.putExtra("SIGN", val);
                // 부른 Activity에게 결과 값 반환
                setResult(RESULT_OK, resultIntent);
                // 현재 Activity 종료
                finish();
            } catch (Exception e1) {
                TGSClass.AlertMessage(getApplicationContext(), e1.getMessage());
            }
        }
    }

    private boolean BL_DATASET_SELECT(final String SL_CD, final String ITEM_CD, final String TRACKING_NO, final String LOT_NO, final String LOT_SUB_NO, final String QTY, final String BASIC_UNIT, final String LOCATION, final String BAD_ON_HAND_QTY) {
        Thread workThd_BL_DATASET_SELECT = new Thread() {
            public void run() {

                String move_date_data = move_date.getText().toString();

                String move_qty_data = move_qty.getText().toString();

                String txt_Scan_move_SL_CD_data = txt_Scan_move_SL_CD.getText().toString();

                String cud_flag = "C";
                String plant_cd = vPLANT_CD;
                String trans_plant_cd = vPLANT_CD;
                String item_cd = ITEM_CD;
                String tracking_no = TRACKING_NO;
                String trns_tracking_no = TRACKING_NO;
                String lot_no = LOT_NO;
                int lot_sub_no = Integer.parseInt(LOT_SUB_NO);
                String major_sl_cd = SL_CD;
                String issued_sl_cd = txt_Scan_move_SL_CD_data;
                String qty = move_qty_data;
                String document_dt = move_date_data;

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("cud_flag");
                parm.setValue(cud_flag);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("plant_cd");
                parm2.setValue(plant_cd);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("trans_plant_cd");
                parm3.setValue(trans_plant_cd);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("item_cd");
                parm4.setValue(item_cd);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("tracking_no");
                parm5.setValue(tracking_no);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("trns_tracking_no");
                parm6.setValue(trns_tracking_no);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("lot_no");
                parm7.setValue(lot_no);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("lot_sub_no");
                parm8.setValue(lot_sub_no);
                parm8.setType(Integer.class);

                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("major_sl_cd");
                parm9.setValue(major_sl_cd);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("issued_sl_cd");
                parm10.setValue(issued_sl_cd);
                parm10.setType(String.class);

                PropertyInfo parm11 = new PropertyInfo();
                parm11.setName("qty");
                parm11.setValue(qty);
                parm11.setType(String.class);

                PropertyInfo parm12 = new PropertyInfo();
                parm12.setName("document_dt");
                parm12.setValue(document_dt);
                parm12.setType(String.class);

                PropertyInfo parm13 = new PropertyInfo();
                parm13.setName("unit_cd");
                parm13.setValue(vUNIT_CD);
                parm13.setType(String.class);

                PropertyInfo parm14 = new PropertyInfo();
                parm14.setName("user_id");
                parm14.setValue(vUSER_ID);
                parm14.setType(String.class);

                pParms.add(parm);
                pParms.add(parm2);
                pParms.add(parm3);
                pParms.add(parm4);
                pParms.add(parm5);
                pParms.add(parm6);
                pParms.add(parm7);
                pParms.add(parm8);
                pParms.add(parm9);
                pParms.add(parm10);
                pParms.add(parm11);
                pParms.add(parm12);
                pParms.add(parm13);
                pParms.add(parm14);

                result_msg = dba.SendHttpMessage("BL_Set_INVENTORY_MOVE_ANDROID", pParms);
            }


        };
        workThd_BL_DATASET_SELECT.start();   //스레드 시작
        try {
            workThd_BL_DATASET_SELECT.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }
}