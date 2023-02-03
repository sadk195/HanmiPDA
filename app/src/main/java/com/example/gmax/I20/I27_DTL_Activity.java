package com.example.gmax.I20;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gmax.BaseActivity;
import com.example.gmax.DBAccess;
import com.example.gmax.GetComboData;
import com.example.gmax.R;
import com.example.gmax.TGSClass;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class I27_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonCombo = "", sJsonHDR = "", sJsonDTL = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private I27_HDR vGetHDRItem;

    //== View 선언(TextView) ==//
    private TextView txt_item_cd, txt_item_nm, txt_spec, txt_location_nm, txt_tracking_no, txt_good_qty, txt_down, txt_sl_nm;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Spinner) ==//
    private Spinner spinner_storage;

    //== View 선언(EditText) ==//
    private EditText move_date, move_qty;

    //== View 선언(Button) ==//
    private Button btn_save;

    //== View 선언(Layout) ==//
    private LinearLayout box_view2;
    private RelativeLayout layout_btn;

    //== View 선언(CheckBox) ==//
    private CheckBox chk_out_storage;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    //== Dialog ==//
    private DBQueryList dbQueryList;

    //== Spinner에 필요한 변수 ==//
    private String str_storage;

    //== itme 선택 시 몇 번째 item을 선택한지 판단하는 변수 선언 ==//
    private int sel_item_idx;

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(this);
        mAlert.setTitle(txt_item_nm.getText())
                .setMessage("해당 품목의 재고이동을 취소하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i27_dtl);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID = getIntent().getStringExtra("MENU_ID");
        vMenuNm = getIntent().getStringExtra("MENU_NM");
        vMenuRemark = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand = getIntent().getStringExtra("START_COMMAND");

        vGetHDRItem = (I27_HDR) getIntent().getSerializableExtra("HDR");

        //== ID 값 바인딩 ==//
        txt_item_cd = (TextView) findViewById(R.id.txt_item_cd);
        txt_item_nm = (TextView) findViewById(R.id.txt_item_nm);
        txt_spec = (TextView) findViewById(R.id.txt_spec);
        txt_location_nm = (TextView) findViewById(R.id.txt_location_nm);
        txt_tracking_no = (TextView) findViewById(R.id.txt_tracking_no);
        txt_good_qty = (TextView) findViewById(R.id.txt_good_qty);

        listview = (ListView) findViewById(R.id.listOrder);

        txt_down = (TextView) findViewById(R.id.txt_down);
        txt_sl_nm = (TextView) findViewById(R.id.txt_sl_nm);
        move_date = (EditText) findViewById(R.id.move_date);
        spinner_storage = (Spinner) findViewById(R.id.spinner_storage);
        chk_out_storage = (CheckBox) findViewById(R.id.chk_out_storage);
        move_qty = (EditText) findViewById(R.id.move_qty);

        btn_save = (Button) findViewById(R.id.btn_save);

        box_view2 = (LinearLayout) findViewById(R.id.box_view2);
        layout_btn = (RelativeLayout) findViewById(R.id.layout_btn);
    }

    private void initializeCalendar() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {
        //== 날짜 이벤트 ==//
        move_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, move_date, cal);
            }
        });

        //== 외부창고 Check 이벤트 ==//
        chk_out_storage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //== 체크박스 클릭 시 창고 목록(Spinner) 재조회
                dbQuery_getComboData();
            }
        });

        //== 내리기 버튼 이벤트 ==//
        txt_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //== 레이아웃 숨기기 ==//
                box_view2.setVisibility(View.GONE);
                layout_btn.setVisibility(View.GONE);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 기존 소스 dbSave()로 옮김
                dbSave("EXIT");
            }
        });
    }

    private void initializeData() {
        txt_item_cd.setText(vGetHDRItem.getITEM_CD());
        txt_item_nm.setText(vGetHDRItem.getITEM_NM());
        txt_spec.setText(vGetHDRItem.getSPEC());
        txt_location_nm.setText(vGetHDRItem.getLOCATION_NM());
        txt_tracking_no.setText(vGetHDRItem.getTRACKING_NO());
        txt_good_qty.setText(decimalForm.format(vGetHDRItem.getSUM_GOOD_ON_HAND_QTY()));

        move_date.setText(df.format(cal.getTime()));

        dbQuery_getComboData();

        start();

        sel_item_idx = 0;
    }

    private void start() {
        dbQueryList();
    }

    public void dbQueryList() {
        progressStart(this);

        dbQueryList = new DBQueryList();
        dbQueryList.start();
    }

    private class DBQueryList extends Thread {
        @Override
        public void run() {
            try {
                String sql = " EXEC XUSP_APK_I27_GET_HDR_LIST ";
                sql += " @PLANT_CD = '" + "H1" + "' ";
                sql += ", @ITEM_CD = '" + vGetHDRItem.getITEM_CD() + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);
                handler.sendMessage(handler.obtainMessage());
            } catch (Exception ex) {
                TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
                progressEnd();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            boolean retry = true;
            while (retry) {
                try {
                    dbQueryList.join();

                    JSONArray ja = new JSONArray(sJson);

                    // 빈 데이터 리스트 생성.
                    //final ArrayList<String> items = new ArrayList<String>();
                    I27_DTL_ListViewAdapter listViewAdapter = new I27_DTL_ListViewAdapter();
                    for (int idx = 0; idx < ja.length(); idx++) {
                        JSONObject jObject = ja.getJSONObject(idx);//

                        I27_DTL item = new I27_DTL();

                        item.ITEM_CD = jObject.getString("ITEM_CD");                             // 품번
                        item.ITEM_NM = jObject.getString("ITEM_NM");                             // 품명
                        item.SL_CD = jObject.getString("SL_CD");                               // 창고코드
                        item.SL_NM = jObject.getString("SL_NM");                               // 창고명
                        item.GOOD_ON_HAND_QTY = decimalForm.format(jObject.getInt("GOOD_ON_HAND_QTY"));   // 양품수량
                        item.BAD_ON_HAND_QTY = decimalForm.format(jObject.getInt("BAD_ON_HAND_QTY"));    // 불량품수량
                        item.TRACKING_NO = jObject.getString("TRACKING_NO");                         // T/K
                        item.LOT_NO = jObject.getString("LOT_NO");                              // LOT번호
                        item.LOT_SUB_NO = jObject.getString("LOT_SUB_NO");                          // LOT순번
                        item.BASIC_UNIT = jObject.getString("BASIC_UNIT");                          // 재고단위
                        item.LOCATION = jObject.getString("LOCATION");                            // 적치장

                        listViewAdapter.addDTLItem(item);
                    }
                    listview.setAdapter(listViewAdapter);

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView parent, View v, int position, long id) {
                            I27_DTL vItem = (I27_DTL) parent.getItemAtPosition(position);
                            //== 선택 된 item의 position을 전역변수에 저장(재고이동시 선택한 item의 position값이 필요함) ==//
                            sel_item_idx = position;
                            //TGSClass.AlertMessage(getApplicationContext(), position + "번째 ITEM CLICK, 양품 : " + vItem.GOOD_ON_HAND_QTY + "개");
                            //== 레이아웃 보이기 ==//
                            box_view2.setVisibility(View.VISIBLE);
                            layout_btn.setVisibility(View.VISIBLE);
                            //== 창고명 세팅 ==//
                            txt_sl_nm.setText(vItem.SL_NM);
                            //== 이동일자 세팅 ==//
                            move_date.setText(df.format(cal.getTime()));
                            //== 이동수량 세팅 ==//
                            String good_qty = vItem.GOOD_ON_HAND_QTY.replace(",", "");
                            move_qty.setText(good_qty);
                            //== 이동할 창고에 포커스 ==//
                            spinner_storage.requestFocus();
                        }
                    });
                } catch (JSONException ex) {
                    TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
                } catch (Exception e1) {
                    TGSClass.AlertMessage(getApplicationContext(), e1.getMessage());
                }
                progressEnd();

                retry = false;
            }
        }
    };

    private void dbQuery_getComboData() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_dbQuery_getComboData = new Thread() {
            public void run() {
                String chk = chk_out_storage.isChecked() ? "Y" : "N";

                String sql = " EXEC XUSP_APK_STORAGE_GET_COMBODATA ";
                sql += " @FLAG= '" + chk + "'";
                sql += ", @PLANT_CD= '" + "H1" + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonCombo = dba.SendHttpMessage("GetSQLData", pParms);
            }

        };
        workThd_dbQuery_getComboData.start();   //스레드 시작
        try {
            workThd_dbQuery_getComboData.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }

        try {
            JSONArray ja = new JSONArray(sJsonCombo);

            final ArrayList<GetComboData> lstItem = new ArrayList<>();

            /*기본값 세팅*/
            GetComboData itemBase = new GetComboData();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jObject = ja.getJSONObject(i);

                GetComboData item = new GetComboData();
                item.setNUM(jObject.getInt("NUM"));
                item.setMINOR_CD(jObject.getString("CODE"));
                item.setMINOR_NM(jObject.getString("NAME"));

                lstItem.add(item);
            }
            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, lstItem);

            spinner_storage.setAdapter(adapter);
            //로딩시 기본값 세팅
            spinner_storage.setSelection(adapter.getPosition(itemBase));
            //콤보박스 클릭 이벤트 정의
            spinner_storage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    str_storage = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
                    //Start();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (JSONException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        } catch (Exception ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }
    }

    private void dbSave(String val) {
        GetComboData spinner_sl_cd = (GetComboData) spinner_storage.getSelectedItem();

        String str_move_qty = move_qty.getText().toString();

        if (spinner_sl_cd.getMINOR_CD().equals("") || str_move_qty.isEmpty()) {
            TGSClass.AlertMessage(getApplicationContext(), "이동창고 또는 이동수량을 입력하여 주시기 바랍니다.");
            return;
        } else {
            double double_move_qty = Double.parseDouble(move_qty.getText().toString());

            I27_DTL_ListViewAdapter listViewAdapter = (I27_DTL_ListViewAdapter) listview.getAdapter();
            I27_DTL item = (I27_DTL) listViewAdapter.getItem(sel_item_idx);

            if (item.getSL_CD().equals(spinner_sl_cd.getMINOR_CD())) {
                TGSClass.AlertMessage(getApplicationContext(), "동일한 창고로 이동할 수 없습니다.");
                return;
            }

            double qty = Double.parseDouble(item.getGOOD_ON_HAND_QTY());

            if (qty < double_move_qty) {
                TGSClass.AlertMessage(getApplicationContext(), "현재고수량보다 이동수량이 초과되었습니다.");
                return;
            }

            try {
                String sl_cd = item.getSL_CD();
                String item_cd = item.getITEM_CD();
                String tracking_no = item.getTRACKING_NO();
                String lot_no = item.getLOT_NO();
                String lot_sub_no = item.getLOT_SUB_NO();

                //== STEP 1. BL 실행 ==//
                BL_DATASET_SELECT(sl_cd, item_cd, tracking_no, lot_no, lot_sub_no);
                //== STEP 2. BL 실행 후 반환 메시지 확인 ==//
                if (result_msg.equals("") || result_msg.isEmpty()) {
                    TGSClass.AlertMessage(getApplicationContext(), result_msg);
                    return;
                } else if (result_msg.contains("재고이동 성공")) {
                    //== STEP 3. 뭐해야할까? ==//
                    if (!dbSave_HDR()) {
                        return;
                    } else {
                        try {
                            JSONArray ja_HDR = new JSONArray(sJsonHDR);
                            JSONObject jObj_HDR = ja_HDR.getJSONObject(0);

                            String RTN_ITEM_DOCUMENT_NO = jObj_HDR.getString("RTN_ITEM_DOCUMENT_NO");
                            String dtl_sl_cd = item.getSL_CD();
                            String dtl_item_cd = item.getITEM_CD();
                            String dtl_tracking_no = item.getTRACKING_NO();
                            String dtl_lot_no = item.getLOT_NO();
                            String dtl_lot_sub_no = item.getLOT_SUB_NO();
                            String dtl_qty = item.getGOOD_ON_HAND_QTY();
                            String dtl_basic_unit = item.getBASIC_UNIT();
                            String dtl_location = item.getLOCATION();
                            String dtl_bad_on_hand_qty = item.getBAD_ON_HAND_QTY();

                            dbSave_DTL(RTN_ITEM_DOCUMENT_NO, dtl_sl_cd, dtl_item_cd, dtl_tracking_no, dtl_lot_no, dtl_lot_sub_no, dtl_qty, dtl_basic_unit, dtl_location, dtl_bad_on_hand_qty);
                        } catch (JSONException exJson) {
                            TGSClass.AlertMessage(getApplicationContext(), exJson.getMessage());
                        } catch (Exception ex) {
                            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
                        }
                        String msg = "(" + item.getITEM_CD() + ")" + item.getITEM_NM() + "의 재고가 이동되었습니다.";
                        TGSClass.AlertMessage(getApplicationContext(), msg, 2000);

                        //== STEP 4. 현재 Activity 종료 ==//
                        // 저장 후 결과 값 돌려주기
                        Intent resultIntent = new Intent();
                        // 결과처리 후 부른 Activity에 보낼 값
                        resultIntent.putExtra("SIGN", val);
                        // 부른 Activity에게 결과 값 반환
                        setResult(RESULT_OK, resultIntent);
                        // 현재 Activity 종료
                        finish();
                    }
                } else {
                    TGSClass.AlertMessage(getApplicationContext(), result_msg);
                    return;
                }
            } catch (Exception e1) {
                TGSClass.AlertMessage(getApplicationContext(), e1.getMessage());
            }
        }
    }

    //== BL 만드는 메서드(I22_DTL 참고) ==//
    private boolean BL_DATASET_SELECT(final String SL_CD, final String ITEM_CD, final String TRACKING_NO, final String LOT_NO, final String LOT_SUB_NO) {
        Thread wkThd_BL_DATASET_SELECT = new Thread() {
            public void run() {

                String str_move_date = move_date.getText().toString();
                GetComboData spinner_sl_cd = (GetComboData) spinner_storage.getSelectedItem();
                String str_move_qty = move_qty.getText().toString();

                String cud_flag = "C";
                String plant_cd = "H1";
                String trans_plant_cd = "H1";
                String item_cd = ITEM_CD;
                String tracking_no = TRACKING_NO;
                String trns_tracking_no = TRACKING_NO;
                String lot_no = LOT_NO;
                int lot_sub_no = Integer.parseInt(LOT_SUB_NO);
                String major_sl_cd = SL_CD;
                String issued_sl_cd = spinner_sl_cd.getMINOR_CD();
                String qty = str_move_qty;
                String document_dt = str_move_date;

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
        wkThd_BL_DATASET_SELECT.start();   //스레드 시작
        try {
            wkThd_BL_DATASET_SELECT.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }

    //== 수불정보_HDR 저장(?) ==//
    private boolean dbSave_HDR() {
        Thread wkThd_dbSave_HDR = new Thread() {
            public void run() {
                String str_move_date = move_date.getText().toString();

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_HDR_SET ";
                sql += "@RTN_ITEM_DOCUMENT_NO = ''";
                sql += ",@DOCUMENT_YEAR = ''";
                sql += ",@TRNS_TYPE = 'ST'";
                sql += ",@MOV_TYPE = 'T61'";
                sql += ",@DOCUMENT_DT = '" + str_move_date + "'";
                sql += ",@RECORD_NO = ''";

                sql += ",@PLANT_CD = '" + "H1" + "'";
                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";

                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";

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
        wkThd_dbSave_HDR.start();   //스레드 시작
        try {
            wkThd_dbSave_HDR.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
        }

        if (sJsonHDR.equals("[]")) {
            return false;
        } else {
            return true;
        }
    }

    //== 수불정보_DTL 저장(?) ==//
    private boolean dbSave_DTL(final String ITEM_DOCUMENT_NO, final String SL_CD, final String ITEM_CD, final String TRACKING_NO, final String LOT_NO,
                               final String LOT_SUB_NO, final String QTY, final String BASIC_UNIT, final String LOCATION, final String BAD_ON_HAND_QTY) {
        Thread wkThd_dbSave_DTL = new Thread() {
            public void run() {
                GetComboData spinner_sl_cd = (GetComboData) spinner_storage.getSelectedItem();

                String str_move_date = move_date.getText().toString();

                String str_move_qty = move_qty.getText().toString();

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DTL_SET ";
                sql += "@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "'";        // 채번
                sql += ",@DOCUMENT_YEAR = ''";                                  // 년도
                sql += ",@TRNS_TYPE = 'ST'";                                      // 변경유형
                sql += ",@MOV_TYPE = 'T61'";                                       // 이동유형
                sql += ",@PLANT_CD = '" + "H1" + "'";                      // 공장코드

                sql += ",@DOCUMENT_DT = '" + str_move_date + "'";               // 이동일자

                /* I_ONHAND_STOCK_DETAIL 에서 바인딩 받아야 하므로 ListView에 조회되도록 SELECT 프로시저에 DTL항목 추가하고 바인딩 한 후 가져와야됨*/
                sql += ",@SL_CD = '" + SL_CD + "'";                             // 창고코드
                sql += ",@ITEM_CD = '" + ITEM_CD + "'";                         // 품목코드
                sql += ",@TRACKING_NO = '" + TRACKING_NO + "'";                 // TRACKING_NO
                sql += ",@LOT_NO = '" + LOT_NO + "'";                           // LOT_NO
                sql += ",@LOT_SUB_NO = " + LOT_SUB_NO;                          // LOT_SUB_NO
                sql += ",@QTY = " + QTY;                                        // 양품수량
                sql += ",@BASE_UNIT = '" + BASIC_UNIT + "'";                    // 재고단위
                sql += ",@LOC_CD = '" + LOCATION + "'";                         // 기존의 적치장
                sql += ",@TRNS_TRACKING_NO = '" + TRACKING_NO + "'";            /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_NO = '" + LOT_NO + "'";                      /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_SUB_NO = " + LOT_SUB_NO;                     /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_PLANT_CD = '" + "H1" + "'";                 /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_SL_CD = '" + spinner_sl_cd.getMINOR_CD() + "'";  /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_ITEM_CD = '" + ITEM_CD + "'";                    /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/

                sql += ",@RECORD_NO = ''";                                      // 제조오더
                sql += ",@EXTRA_FIELD1 = 'ANDROID'";
                sql += ",@EXTRA_FIELD2 = 'I27_DTL_Activity'";

                sql += ",@BAD_ON_HAND_QTY = " + BAD_ON_HAND_QTY;                // 불량 수량
                sql += ",@MOVE_QTY = " + str_move_qty;                          // 이동 수량

                sql += ",@TRNS_LOC_CD = '*'";                                   // 이동할 적치장
                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";

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
        wkThd_dbSave_DTL.start();   //스레드 시작
        try {
            wkThd_dbSave_DTL.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }
}