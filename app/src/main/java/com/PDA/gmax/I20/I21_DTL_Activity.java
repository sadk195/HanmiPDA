package com.PDA.gmax.I20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.DBAccess;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class I21_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonHDR = "", sJsonDTL = "", location_check ="";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private I21_HDR vGetHDRItem;

    //== View 선언(TextView) ==//
    private TextView storage_location, location, item_cd, item_nm, good_on_hand_qty;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(EditText) ==//
    private EditText move_date, txt_Scan_move_location, move_qty;

    //== View 선언(Button) ==//
    private Button btn_add, btn_save;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i21_dtl);

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

        vGetHDRItem             = (I21_HDR) getIntent().getSerializableExtra("HDR");

        //== ID 값 바인딩 ==//
        storage_location        = (TextView) findViewById(R.id.storage_location);
        location                = (TextView) findViewById(R.id.location);
        item_cd                 = (TextView) findViewById(R.id.item_cd);
        item_nm                 = (TextView) findViewById(R.id.item_nm);
        good_on_hand_qty        = (TextView) findViewById(R.id.good_on_hand_qty);

        listview                = (ListView) findViewById(R.id.listOrder);

        txt_Scan_move_location  = (EditText) findViewById(R.id.txt_Scan_move_location);
        move_date               = (EditText) findViewById(R.id.move_date);
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
                // 기존 소스 dbSave()로 이동
                dbSave("EXIT");
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 기존 소스 dbSave()로 이동
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
            I21_DTL_ListViewAdapter listViewAdapter = new I21_DTL_ListViewAdapter(); //

            /*
            if (ja.length() == 1) {
                JSONObject jObject = ja.getJSONObject(0);

                String CHK = "Y";
                String TRACKING_NO = jObject.getString("TRACKING_NO");  //Tracking 번호
                String LOT_NO = jObject.getString("LOT_NO");  //Lot번호
                String GOOD_ON_HAND_QTY = jObject.getString("GOOD_ON_HAND_QTY");  //양품수량
                String BAD_ON_HAND_QTY = jObject.getString("BAD_ON_HAND_QTY");  //불량수량

                String SL_CD = jObject.getString("SL_CD");  //창고코드
                String ITEM_CD = jObject.getString("ITEM_CD");  //품목코드
                String LOT_SUB_NO = jObject.getString("LOT_SUB_NO");  //LOT_SUB_NO
                String BASIC_UNIT = jObject.getString("BASIC_UNIT");  //재고단위
                String LOCATION = jObject.getString("LOCATION");  //재고단위

                listViewAdapter.add_Loading_Place_Dtl_Item(CHK, TRACKING_NO, LOT_NO, GOOD_ON_HAND_QTY, BAD_ON_HAND_QTY, SL_CD, ITEM_CD, LOT_SUB_NO, BASIC_UNIT, LOCATION);
            } else {
                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    String CHK = jObject.getString("CHK");  //체크 이벤트
                    String TRACKING_NO = jObject.getString("TRACKING_NO");  //Tracking 번호
                    String LOT_NO = jObject.getString("LOT_NO");  //Lot번호
                    String GOOD_ON_HAND_QTY = jObject.getString("GOOD_ON_HAND_QTY");  //양품수량
                    String BAD_ON_HAND_QTY = jObject.getString("BAD_ON_HAND_QTY");  //불량수량

                    String SL_CD = jObject.getString("SL_CD");  //창고코드
                    String ITEM_CD = jObject.getString("ITEM_CD");  //품목코드
                    String LOT_SUB_NO = jObject.getString("LOT_SUB_NO");  //LOT_SUB_NO
                    String BASIC_UNIT = jObject.getString("BASIC_UNIT");  //재고단위
                    String LOCATION = jObject.getString("LOCATION");  //재고단위

                    listViewAdapter.add_Loading_Place_Dtl_Item(CHK, TRACKING_NO, LOT_NO, GOOD_ON_HAND_QTY, BAD_ON_HAND_QTY, SL_CD, ITEM_CD, LOT_SUB_NO, BASIC_UNIT, LOCATION);
                }
            }
             */
            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I21_DTL item = new I21_DTL();

                if (ja.length() == 1) {
                    item.CHK = "Y";
                } else {
                    item.CHK = jObject.getString("CHK");  //체크 이벤트
                }

                item.TRACKING_NO        = jObject.getString("TRACKING_NO");         //Tracking 번호
                item.LOT_NO             = jObject.getString("LOT_NO");              //Lot번호
                item.GOOD_ON_HAND_QTY   = jObject.getString("GOOD_ON_HAND_QTY");    //양품수량
                item.BAD_ON_HAND_QTY    = jObject.getString("BAD_ON_HAND_QTY");     //불량수량

                item.SL_CD              = jObject.getString("SL_CD");               //창고코드
                item.ITEM_CD            = jObject.getString("ITEM_CD");             //품목코드
                item.LOT_SUB_NO         = jObject.getString("LOT_SUB_NO");          //LOT_SUB_NO
                item.BASIC_UNIT         = jObject.getString("BASIC_UNIT");          //재고단위
                item.LOCATION           = jObject.getString("LOCATION");            //적치장

                listViewAdapter.addDTLItem(item);
            }

            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {

                    I21_DTL_ListViewAdapter listViewAdapter = (I21_DTL_ListViewAdapter) parent.getAdapter();
                    I21_DTL item = (I21_DTL) listViewAdapter.getItem(position);

                    CheckedTextView chk = (CheckedTextView) v.findViewById(R.id.chk);

                    if (chk.isChecked() == true) {
                        chk.setChecked(false);
                        item.CHK = "";
                    } else {
                        chk.setChecked(true);
                        item.CHK = "Y";
                    }

                    /*
                    I21_DTL vItem = (I21_DTL) parent.getItemAtPosition(position);

                    CheckedTextView chk = (CheckedTextView) v.findViewById(R.id.chk);

                    if (chk.isChecked() == true) {
                        chk.setChecked(false);
                        vItem.CHK = "";
                    } else {
                        chk.setChecked(true);
                        vItem.CHK = "Y";
                    }
                     */
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
                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DTL_GET_LIST_I21 ";
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

    private void LOCATION_Check() {
        Thread workThd_LOCATION_Check = new Thread() {
            public void run() {
                String txt_Scan_move_location_st = txt_Scan_move_location.getText().toString();         //입력한 이동할 적티장 정보

                String sql = "  SELECT LOCATION_CD ";
                sql += "        FROM ZZ_WMS_LOCATION_MASTER";
                sql += "        WHERE PLANT_CD = '" + vPLANT_CD + "'";
                sql += "        AND   LOCATION_CD =  '" + txt_Scan_move_location_st + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                location_check = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThd_LOCATION_Check.start();   //스레드 시작
        try {
            workThd_LOCATION_Check.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private void dbSave(String val) {
        String txt_Scan_move_location_data = txt_Scan_move_location.getText().toString();
        String move_qty_st = move_qty.getText().toString();

        LOCATION_Check();

        if (txt_Scan_move_location_data.equals("") || move_qty_st.isEmpty()) {
            TGSClass.AlertMessage(getApplicationContext(), "이동적치장 또는 이동수량을 입력하여 주시기 바랍니다.");
        } else if (location_check.equals("[]")) {   //입력한 이동할 적치장의 정보가 ZZ_WMS_LOCATION_MASTER (적치장 정보 마스터)에 없으면 리턴
            TGSClass.AlertMessage(getApplicationContext(), "입력하신 이동 할 적치장의 정보가 없습니다.");
        } else if (sJson.equals("[]")) {
            TGSClass.AlertMessage(getApplicationContext(), "이동 할 정보가 없습니다.");
        } else {
            I21_DTL_ListViewAdapter listViewAdapter = (I21_DTL_ListViewAdapter) listview.getAdapter();

            Double move_qty_int = Double.parseDouble(move_qty.getText().toString());

            for (int idx2 = 0; idx2 < listViewAdapter.getCount(); idx2++) {
                I21_DTL item = (I21_DTL) listViewAdapter.getItem(idx2);

                if (item.getCHK() == "Y") {
                    double qty = Double.parseDouble(item.getGOOD_ON_HAND_QTY());

                    if (qty < move_qty_int) {
                        TGSClass.AlertMessage(getApplicationContext(), "현재고수량보다 이동수량이 초과되었습니다.");
                        return;
                    }
                }
            }

            if (dbSave_HDR() == true) {
                try {
                    JSONArray ja = new JSONArray(sJsonHDR);
                    int idx = 0;
                    JSONObject jObject = ja.getJSONObject(idx);

                    String RTN_ITEM_DOCUMENT_NO = jObject.getString("RTN_ITEM_DOCUMENT_NO");

                    I21_DTL_ListViewAdapter listViewAdapter2 = (I21_DTL_ListViewAdapter) listview.getAdapter();

                    for (int idx2 = 0; idx2 < listViewAdapter2.getCount(); idx2++) {
                        I21_DTL item = (I21_DTL) listViewAdapter2.getItem(idx2);

                        if (item.getCHK() == "Y") {
                            String sl_cd = item.getSL_CD();
                            String item_cd = item.getITEM_CD();
                            String tracking_no = item.getTRACKING_NO();
                            String lot_no = item.getLOT_NO();
                            String lot_sub_no = item.getLOT_SUB_NO();
                            String qty = item.getGOOD_ON_HAND_QTY();
                            String basic_unit = item.getBASIC_UNIT();
                            String location = item.getLOCATION();
                            String bad_on_hand_qty = item.getBAD_ON_HAND_QTY();

                            dbSave_DTL(RTN_ITEM_DOCUMENT_NO, sl_cd, item_cd, tracking_no, lot_no, lot_sub_no, qty, basic_unit, location, bad_on_hand_qty);
                        }
                    }
                    TGSClass.AlertMessage(getApplicationContext(), RTN_ITEM_DOCUMENT_NO + "자동입력번호로 저장되었습니다.");

                    // 저장 후 결과 값 돌려주기
                    Intent resultIntent = new Intent();
                    // 결과처리 후 부른 Activity에 보낼 값
                    resultIntent.putExtra("SIGN", val);
                    // 부른 Activity에게 결과 값 반환
                    setResult(RESULT_OK, resultIntent);
                    // 현재 Activity 종료
                    finish();
                } catch (JSONException ex) {
                    TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
                } catch (Exception e1) {
                    TGSClass.AlertMessage(getApplicationContext(), "catch : e1");
                }
            } else {
                TGSClass.AlertMessage(getApplicationContext(), "저장 되지 않았습니다. 데이터를 정확히 입력하였는지 확인해주시기 바랍니다!");
                return;
            }
        }
    }

    private boolean dbSave_HDR() {
        Thread workThd_dbSave_HDR = new Thread() {
            public void run() {
                String move_date_st = move_date.getText().toString();

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_HDR_SET ";
                sql += "@TRNS_TYPE = ''";
                sql += ",@MOV_TYPE = ''";
                sql += ",@DOCUMENT_DT = '" + move_date_st + "'";
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";
                sql += ",@RTN_ITEM_DOCUMENT_NO = ''";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonHDR = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThd_dbSave_HDR.start();   //스레드 시작
        try {
            workThd_dbSave_HDR.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
        }

        if (sJsonHDR.equals("[]")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean dbSave_DTL(final String ITEM_DOCUMENT_NO, final String SL_CD, final String ITEM_CD, final String TRACKING_NO, final String LOT_NO, final String LOT_SUB_NO, final String QTY, final String BASIC_UNIT, final String LOCATION, final String BAD_ON_HAND_QTY) {
        Thread workThd_dbSave_DTL = new Thread() {
            public void run() {
                String move_date_data = move_date.getText().toString();

                String txt_Scan_move_location_st = txt_Scan_move_location.getText().toString();

                String move_qty_data = move_qty.getText().toString();

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DTL_SET ";
                sql += "@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "'";    //채번
                // += ",@DOCUMENT_YEAR =";                                  //프로시저 안에서 적용
                sql += ",@TRNS_TYPE = ''";                                  //변경유형
                sql += ",@MOV_TYPE = ''";                                   //이동유형
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";                  //공장코드
                sql += ",@DOCUMENT_DT = '" + move_date_data + "'";            //이동일자

                /* I_ONHAND_STOCK_DETAIL 에서 바인딩 받아야 하므로 ListView에 조회되도록 SELECT 프로시저에 DTL항목 추가하고 바인딩 한 후 가져와야됨*/
                sql += ",@SL_CD = '" + SL_CD + "'";                             //창고코드
                sql += ",@ITEM_CD = '" + ITEM_CD + "'";                         //품목코드
                sql += ",@TRACKING_NO = '" + TRACKING_NO + "'";                 //TRACKING_NO
                sql += ",@LOT_NO = '" + LOT_NO + "'";                           //LOT_NO
                sql += ",@LOT_SUB_NO = " + LOT_SUB_NO;                    //LOT_SUB_NO
                sql += ",@QTY = " + QTY;                                  //양품수량
                sql += ",@BASE_UNIT = '" + BASIC_UNIT + "'";                    //재고단위
                sql += ",@LOC_CD = '" + LOCATION + "'";                         //기존의 적치장
                sql += ",@TRNS_TRACKING_NO = '" + TRACKING_NO + "'";            /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_NO = '" + LOT_NO + "'";                      /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_SUB_NO = " + LOT_SUB_NO;              /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_PLANT_CD = '" + vPLANT_CD + "'";                 /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_SL_CD = '" + SL_CD + "'";                        /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_ITEM_CD = '" + ITEM_CD + "'";                    /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOC_CD = '" + txt_Scan_move_location_st + "'";   //이동할 적치장

                sql += ",@BAD_ON_HAND_QTY = " + BAD_ON_HAND_QTY;         //불량 수량
                sql += ",@MOVE_QTY = " + move_qty_data;                  //이동 수량

                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";
                sql += ",@EXTRA_FIELD1 = 'ANDROID'";
                sql += ",@EXTRA_FIELD2 = 'I21_DTL_Activity'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonDTL = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThd_dbSave_DTL.start();   //스레드 시작
        try {
            workThd_dbSave_DTL.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }
}