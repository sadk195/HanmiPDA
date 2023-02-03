package com.example.gmax.M20;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.gmax.BaseActivity;
import com.example.gmax.DBAccess;
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


public class M21_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson, sJson_hdr, sJson_select_location_master;

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private String INSPECT_REQ_NO_EX;

    //== View 선언(EditText) ==//
    private EditText INSPECT_REQ_NO, INSP_DT, PRODT_ORDER_NO, ITEM_CD, ITEM_NM, STS_NM, WK_NM, G_QTY, B_QTY, QTY, OPR_NO, DECISION, QTY1, TOP_LOCATION;
    private EditText save_location, in_dt;

    //== View 선언(Button) ==//
    private Button btn_save;

    //== 바코드 관련 변수 선언 ==//
    private ImageView img_barcode;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m21_dtl);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        //ScanData에서 스캔 한 값을 받아 QM_10의 INSP_REQ_NO(검사의뢰번호)를 받아서 QM_11로 받아 오는 소스
        INSPECT_REQ_NO_EX   = getIntent().getStringExtra("INSP_REQ_NO");
        vMenuNm             = getIntent().getStringExtra("MENU_NM");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어온 코드

        //== ID 값 바인딩 ==//
        INSPECT_REQ_NO      = (EditText) findViewById(R.id.INSPECT_REQ_NO);
        INSP_DT             = (EditText) findViewById(R.id.INSP_DT);
        PRODT_ORDER_NO      = (EditText) findViewById(R.id.PRODT_ORDER_NO);
        ITEM_CD             = (EditText) findViewById(R.id.ITEM_CD);
        ITEM_NM             = (EditText) findViewById(R.id.ITEM_NM);
        STS_NM              = (EditText) findViewById(R.id.STS_NM);
        WK_NM               = (EditText) findViewById(R.id.WK_NM);
        G_QTY               = (EditText) findViewById(R.id.G_QTY);
        B_QTY               = (EditText) findViewById(R.id.B_QTY);
        QTY                 = (EditText) findViewById(R.id.QTY);
        OPR_NO              = (EditText) findViewById(R.id.OPR_NO);
        DECISION            = (EditText) findViewById(R.id.DECISION);
        QTY1                = (EditText) findViewById(R.id.qty1);
        TOP_LOCATION        = (EditText) findViewById(R.id.top_location);

        in_dt               = (EditText) findViewById(R.id.in_dt);
        save_location       = (EditText) findViewById(R.id.save_location);
        img_barcode         = (ImageView) findViewById(R.id.img_barcode);
        btn_save            = (Button) findViewById(R.id.btn_save);
    }

    private void initializeCalendar() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {
        img_barcode.setOnClickListener(qrClickListener);

        in_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, in_dt, cal);
            }
        });

        in_dt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) { //do your work here }
                    //Start();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
                start();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (save_location.getText().toString().equals("")) {
                    TGSClass.AlertMessage(getApplicationContext(), "입고할 적치장을 선택해주세요.");
                    return;
                } else {
                    dbQuery_SELECT_LOCATION_MASTER();

                    if (sJson_select_location_master.equals("[]")) {
                        TGSClass.AlertMessage(getApplicationContext(), "입력하신 입고 적치장의 정보가 없습니다.\n다시 확인해 주시기 바랍니다.");
                        return;
                    } else {
                        String str_INSP_DT = INSP_DT.getText().toString();
                        int INSP_YYYY = Integer.parseInt(str_INSP_DT.substring(0, 4));
                        int INSP_MM = Integer.parseInt(str_INSP_DT.substring(5, 7));
                        int INSP_DD = Integer.parseInt(str_INSP_DT.substring(8, 10));

                        if (INSP_YYYY > cal.get(Calendar.YEAR)) {
                            if (INSP_MM > cal.get(Calendar.MONTH)) {
                                if (INSP_DD > cal.get(Calendar.DAY_OF_MONTH)) {
                                    TGSClass.AlertMessage(getApplicationContext(), "입고등록일은 검사결과등록일 보다 이전일 수 없습니다.");
                                    return;
                                }
                            }
                        }

                        String cud_flag     = "C";
                        String flag         = "CONFIRM";
                        String plant_cd     = vPLANT_CD;
                        String ref_no       = PRODT_ORDER_NO.getText().toString();
                        String ref_seq      = OPR_NO.getText().toString();
                        String insp_req_no  = INSPECT_REQ_NO.getText().toString();
                        String decision     = DECISION.getText().toString();
                        String in_dt_st     = df.format(cal.getTime());
                        String wk_id        = "";
                        String qty          = QTY.getText().toString();
                        String b_qty        = B_QTY.getText().toString();

                        fncSet_list_bl(cud_flag, flag, plant_cd, ref_no, ref_seq, insp_req_no, decision, in_dt_st, wk_id, qty, b_qty);

                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(M21_DTL_Activity.this);
                        mAlert.setTitle("생산입고등록\n(최종검사 입고등록)")
                                .setMessage(result_msg)
                                .setCancelable(false) // Dialog 밖이나 뒤로가기 막기위한 소스 true : 풀기, false : 막기
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (result_msg.contains("입고완료")) {
                                            if (dbSave_HDR() == true) {
                                                try {
                                                    JSONArray ja_hdr = new JSONArray(sJson_hdr);
                                                    JSONObject jObject_hdr = ja_hdr.getJSONObject(0);

                                                    String RTN_ITEM_DOCUMENT_NO = jObject_hdr.getString("RTN_ITEM_DOCUMENT_NO");

                                                    /* ZZ_WMS_I_GOODS_MOVEMENT_DTL INSERT 구문 */

                                                    JSONArray ja = new JSONArray(sJson);

                                                    JSONObject jObject = ja.getJSONObject(0);

                                                    String sl_cd                = jObject.getString("SL_CD");           //창고코드
                                                    String item_cd              = jObject.getString("ITEM_CD");         //품목코드
                                                    String tracking_no          = jObject.getString("TRACKING_NO");     //트래킹번호
                                                    String lot_no               = jObject.getString("LOT_NO");          //롯트번호
                                                    String lot_sub_no           = jObject.getString("LOT_SUB_NO");      //롯트순번
                                                    String qty                  = jObject.getString("QTY");             //검사수량 = 검사 후 입고수량
                                                    String basic_unit           = jObject.getString("BASIC_UNIT");      //재고단위
                                                    String save_location_st     = save_location.getText().toString();          //적치장
                                                    String bad_on_hand_qty      = jObject.getString("B_QTY");           //불량수량

                                                    dbSave_DTL(RTN_ITEM_DOCUMENT_NO, sl_cd, item_cd, tracking_no, lot_no, lot_sub_no, qty, basic_unit, save_location_st, bad_on_hand_qty);
                                                } catch (JSONException ex) {
                                                    TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
                                                } catch (Exception e1) {
                                                    TGSClass.AlertMessage(getApplicationContext(), "catch : e1");
                                                }

                                                // 저장 후 결과 값 돌려주기
                                                Intent resultIntent = new Intent();
                                                // 결과처리 후 부른 Activity에 보낼 값
                                                resultIntent.putExtra("SIGN", "EXIT");
                                                // 부른 Activity에게 결과 값 반환
                                                setResult(RESULT_OK, resultIntent);
                                                // 현재 Activity 종료
                                                finish();
                                            }
                                        } else {
                                            return;
                                        }
                                    }
                                })
                                .create().show();
                    }
                }
            }
        });
    }

    private void initializeData() {
        in_dt.setText(df.format(cal.getTime()));

        if (!INSPECT_REQ_NO_EX.equals("")) {
            dbQuery_get_INSP_REQ_NO_INFO(INSPECT_REQ_NO_EX);
            save_location.requestFocus();
        }
    }

    private void start() {
        boolean jSonType = TGSClass.isJsonData(sJson);
        if (!jSonType) return;

        if (!sJson.equals("[]")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                JSONObject jObject = ja.getJSONObject(0);

                INSPECT_REQ_NO.setText(jObject.getString("INSPECT_REQ_NO")); //검사요청번호
                INSP_DT.setText(jObject.getString("INSP_DT")); //검사일자
                PRODT_ORDER_NO.setText(jObject.getString("PRODT_ORDER_NO")); //품번
                ITEM_CD.setText(jObject.getString("ITEM_CD")); //품명
                ITEM_NM.setText(jObject.getString("ITEM_NM")); //진행상태
                STS_NM.setText(jObject.getString("STS_NM")); //검사원 명

                WK_NM.setText(jObject.getString("WK_NM"));
                G_QTY.setText(jObject.getString("G_QTY"));
                B_QTY.setText(jObject.getString("B_QTY"));
                QTY.setText(jObject.getString("QTY"));
                OPR_NO.setText(jObject.getString("OPR_NO"));
                DECISION.setText(jObject.getString("DECISION"));

                QTY1.setText(jObject.getString("SUM_QTY"));
                TOP_LOCATION.setText(jObject.getString("TOP_LOCATION"));

            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage());
            }
        }
    }

    private void dbQuery_get_INSP_REQ_NO_INFO(final String INSPECT_REQ_NO_EX) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_INSP_REQ_NO_INFO = new Thread() {
            public void run() {
                String vSTS = "D";

                String sql = " EXEC XUSP_APK_QM21_GET_LIST ";
                sql += "  @FLAG = 'GRID1'";
                sql += "  ,@PLANT_CD = '" + vPLANT_CD + "'";
                sql += "  ,@PRODT_ORDER_NO = '" + INSPECT_REQ_NO_EX + "'";
                sql += "  ,@STS = '" + vSTS + "'";
                sql += "  ,@ITEM_CD = ''";

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
        workThd_INSP_REQ_NO_INFO.start();   //스레드 시작
        try {
            workThd_INSP_REQ_NO_INFO.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

            try {
                start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InterruptedException ex) {

        }
    }

    private boolean fncSet_list_bl(final String cud_flag, final String flag, final String plant_cd, final String ref_no, final String ref_seq
            , final String insp_req_no, final String decision, final String insp_dt, final String wk_id, final String qty, final String b_qty) {
        Thread workThd_fncSet_list_bl = new Thread() {
            public void run() {
                String cud_flag_parm        = cud_flag;
                String flag_parm            = flag;
                String plant_cd_parm        = plant_cd;
                String ref_no_parm          = ref_no;
                String ref_seq_parm         = ref_seq;
                String insp_req_no_parm     = insp_req_no;
                String decision_parm        = decision;
                String insp_dt_parm         = insp_dt;
                String wk_id_parm           = wk_id;
                String qty_parm             = qty;
                String b_qty_parm           = b_qty;

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("cud_flag");
                parm.setValue(cud_flag_parm);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("flag");
                parm2.setValue(flag_parm);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("plant_cd");
                parm3.setValue(plant_cd_parm);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("ref_no");
                parm4.setValue(ref_no_parm);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("ref_seq");
                parm5.setValue(ref_seq_parm);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("insp_req_no");
                parm6.setValue(insp_req_no_parm);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("decision");
                parm7.setValue(decision_parm);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("insp_dt");
                parm8.setValue(insp_dt_parm);
                parm8.setType(String.class);

                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("wk_id");
                parm9.setValue(wk_id_parm);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("qty");
                parm10.setValue(qty_parm);
                parm10.setType(String.class);

                PropertyInfo parm11 = new PropertyInfo();
                parm11.setName("b_qty");
                parm11.setValue(b_qty_parm);
                parm11.setType(String.class);

                PropertyInfo parm12 = new PropertyInfo();
                parm12.setName("unit_cd");
                parm12.setValue(vUNIT_CD);
                parm12.setType(String.class);

                PropertyInfo parm13 = new PropertyInfo();
                parm13.setName("user_id");
                parm13.setValue(vUSER_ID);
                parm13.setType(String.class);

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

                result_msg = dba.SendHttpMessage("BL_Prodt_Warehousing_Out_save_ANDROID", pParms);
            }
        };
        workThd_fncSet_list_bl.start();   //스레드 시작
        try {
            workThd_fncSet_list_bl.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }

    private void dbQuery_SELECT_LOCATION_MASTER() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_SELECT_LOCATION_MASTER = new Thread() {
            public void run() {
                String sql = " SELECT TOP 1 LOCATION_CD ";
                sql += " FROM ZZ_WMS_LOCATION_MASTER";
                sql += " WHERE PLANT_CD = '" + vPLANT_CD + "'";
                sql += " AND LOCATION_CD = '" + save_location.getText().toString() + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                sJson_select_location_master = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThd_SELECT_LOCATION_MASTER.start();   //스레드 시작
        try {
            workThd_SELECT_LOCATION_MASTER.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private boolean dbSave_HDR() {
        Thread workThd_dbSave_HDR = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_HDR_SET ";
                sql += "@TRNS_TYPE = 'PR'";
                sql += ",@MOV_TYPE = 'R01'";
                sql += ",@DOCUMENT_DT = '" + in_dt.getText().toString() + "'";
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

                sJson_hdr = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThd_dbSave_HDR.start();   //스레드 시작
        try {
            workThd_dbSave_HDR.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
        }

        if (sJson.equals("[]")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean dbSave_DTL(final String ITEM_DOCUMENT_NO, final String SL_CD, final String ITEM_CD, final String TRACKING_NO,
                              final String LOT_NO, final String LOT_SUB_NO, final String QTY, final String BASIC_UNIT,
                              final String LOCATION, final String BAD_ON_HAND_QTY) {
        Thread workThd_dbSave_DTL = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_WMS_I_GOODS_MOVEMENT_DTL_SET_CALCUATE_ANDROID ";
                sql += "@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "'";    //채번
                // += ",@DOCUMENT_YEAR =";                                  //프로시저 안에서 적용
                sql += ",@TRNS_TYPE = 'ST'";                                  //변경유형
                sql += ",@MOV_TYPE = 'T73'";                                   //이동유형
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";                  //공장코드
                sql += ",@DOCUMENT_DT = '" + in_dt.getText().toString() + "'";            //이동일자(t)

                /* I_ONHAND_STOCK_DETAIL 에서 바인딩 받아야 하므로 ListView에 조회되도록 SELECT 프로시저에 DTL항목 추가하고 바인딩 한 후 가져와야됨*/
                sql += ",@SL_CD = '" + SL_CD + "'";                             //창고코드
                sql += ",@ITEM_CD = '" + ITEM_CD + "'";                         //품목코드
                sql += ",@TRACKING_NO = '" + TRACKING_NO + "'";                 //TRACKING_NO
                sql += ",@LOT_NO = '" + LOT_NO + "'";                           //LOT_NO
                sql += ",@LOT_SUB_NO = " + LOT_SUB_NO;                    //LOT_SUB_NO
                sql += ",@QTY = " + Double.parseDouble(QTY);                                  //양품수량
                sql += ",@BASE_UNIT = '" + BASIC_UNIT + "'";                    //재고단위
                sql += ",@LOC_CD = '" + LOCATION + "'";                         //기존의 적치장
                sql += ",@TRNS_TRACKING_NO = '" + TRACKING_NO + "'";            /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_NO = '" + LOT_NO + "'";                      /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_SUB_NO = " + LOT_SUB_NO;                     /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_PLANT_CD = '" + vPLANT_CD + "'";                 /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_SL_CD = '" + SL_CD + "'";                        /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_ITEM_CD = '" + ITEM_CD + "'";                    /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOC_CD = '" + LOCATION + "'";   //이동할 적치장

                sql += ",@BAD_ON_HAND_QTY = " + BAD_ON_HAND_QTY;         //불량 수량
                sql += ",@MOVE_QTY = " + Double.parseDouble(QTY);          //이동 수량
                sql += ",@DEBIT_CREDIT_FLAG = 'D'";                     //증가 감소

                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";
                sql += ",@EXTRA_FIELD1 = 'ANDROID'";
                sql += ",@EXTRA_FIELD2 = 'M21_DTL_Activity'";

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
        workThd_dbSave_DTL.start();   //스레드 시작
        try {
            workThd_dbSave_DTL.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }
}