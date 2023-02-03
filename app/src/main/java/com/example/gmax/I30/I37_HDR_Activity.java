package com.example.gmax.I30;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

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


public class I37_HDR_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJson_DataSET = "", sJson_hdr = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(EditText) ==//
    private EditText prodtorder_opr_no;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(TextView) ==//
    private TextView item_cd, item_nm, tracking_no, prodt_qty, remain_qty, good_qty, bad_qty;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    public Button btn_query, btn_save;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int I37_DTL_REQUEST_CODE = 0;

    //
    public String PRODT_ORDER_NO_DataSET = "";
    public String OPR_NO_DataSET = "";
    public String SL_CD_DataSET = "";
    public String ITEM_CD_DataSET = "";
    public String TRACKING_NO_DataSET = "";
    public String LOT_NO_DataSET = "";
    public String LOT_SUB_NO_DataSET = "";
    public String ENTRY_QTY_DataSET = "";
    public String ENTRY_UNIT_DataSET = "";
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i37_hdr);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID             = getIntent().getStringExtra("MENU_ID");
        vMenuNm             = getIntent().getStringExtra("MENU_NM");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== ID 값 바인딩 ==//
        prodtorder_opr_no   = (EditText) findViewById(R.id.prodtorder_opr_no);
        img_barcode         = (ImageView) findViewById(R.id.img_barcode);

        item_cd             = (TextView) findViewById(R.id.item_cd);
        item_nm             = (TextView) findViewById(R.id.item_nm);
        tracking_no         = (TextView) findViewById(R.id.tracking_no);
        prodt_qty           = (TextView) findViewById(R.id.prodt_qty);
        remain_qty          = (TextView) findViewById(R.id.remain_qty);
        good_qty            = (TextView) findViewById(R.id.good_qty);
        bad_qty             = (TextView) findViewById(R.id.bad_qty);

        listview            = (ListView) findViewById(R.id.listOrder);

        btn_query           = (Button) findViewById(R.id.btn_query);
        btn_save            = (Button) findViewById((R.id.btn_save));
    }

    private void initializeCalendar() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {
        //== 제조오더번호 이벤트 ==//
        prodtorder_opr_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    start();
                    return true;
                }
                return false;
            }
        });

        //== 조회 버튼 이벤트 ==//
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        //== QR코드 관련 이벤트 ==//
        img_barcode.setOnClickListener(qrClickListener);

        //== 일괄처리 버튼 이벤트 ==//
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                I37_HDR_ListViewAdapter Adap = (I37_HDR_ListViewAdapter) listview.getAdapter();

                int ct = listview.getCount();

                for (int i = 0; i < ct; i++) {

                    I37_HDR vItem = (I37_HDR) Adap.getItem(i);
                    String rem_qty = vItem.getREMAIN_QTY();
                    String loc = vItem.getLOCATION().trim();
                    String gi_qty = vItem.getOUT_QTY();

                    double re_qty = Double.parseDouble(rem_qty);
                    double gii_qty = Double.parseDouble(gi_qty);
                    //날짜

                    if (re_qty > 0 && loc.equals("출고대기장")) {
                        //출고
                        String CUD_FLAG = "U";
                        String PRODT_ORDER_NO = vItem.getPRODT_ORDER_NO();
                        String OPR_NO = vItem.getOPR_NO();
                        String ITEM_CD = vItem.getITEM_CD();
                        String SEQ_NO = vItem.getSEQ_NO();
                        String OUT_QTY = rem_qty;

                        String REPORT_DT = df.format(cal.getTime());

                        String LOCATION = vItem.getLOCATION();
                        String BAD_ON_HAND_QTY = vItem.getBAD_ON_HAND_QTY();

                        //출고 HDR 처리 (BL)
                        if (BL_DATASET_SELECT(CUD_FLAG, vPLANT_CD, PRODT_ORDER_NO, OPR_NO, ITEM_CD, SEQ_NO, OUT_QTY, REPORT_DT, vUNIT_CD) == true) {
                            dataSET(CUD_FLAG, vPLANT_CD, PRODT_ORDER_NO, OPR_NO, ITEM_CD, SEQ_NO, OUT_QTY, REPORT_DT, vUNIT_CD);

                            if (result_msg.contains("생산출고 완료")) {
                                if (dbSave_HDR() == true) {
                                    try {
                                        //출고 DTL 처리 (SITE)
                                        JSONArray ja = new JSONArray(sJson_hdr);
                                        int idx = 0;
                                        JSONObject jObject = ja.getJSONObject(idx);

                                        String RTN_ITEM_DOCUMENT_NO = jObject.getString("RTN_ITEM_DOCUMENT_NO");

                                        String sl_cd            = SL_CD_DataSET;
                                        String item_cd          = ITEM_CD_DataSET;
                                        String tracking_no      = TRACKING_NO_DataSET;
                                        String lot_no           = LOT_NO_DataSET;
                                        String lot_sub_no       = LOT_SUB_NO_DataSET;
                                        String qty              = ENTRY_QTY_DataSET;
                                        String basic_unit       = ENTRY_UNIT_DataSET;
                                        String location         = LOCATION;
                                        String bad_on_hand_qty  = BAD_ON_HAND_QTY;
                                        String order_no         = PRODT_ORDER_NO_DataSET;

                                        dbSave_DTL(RTN_ITEM_DOCUMENT_NO, sl_cd, item_cd, tracking_no, lot_no, lot_sub_no, qty, basic_unit, location, bad_on_hand_qty, order_no);

                                    } catch (JSONException ex) {
                                        TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
                                    } catch (Exception e1) {
                                        TGSClass.AlertMessage(getApplicationContext(), "catch : e1");
                                    }
                                }
                            } else {
                                TGSClass.AlertMessage(getApplicationContext(), result_msg);
                                return;
                            }
                        } else {
                            TGSClass.AlertMessage(getApplicationContext(), "오류 발생. 담당자에게 문의");
                            return;
                        }
                    } else {
                        // 출고할 게 없으니까 Skip
                    }
                }

                TGSClass.AlertMessage(getApplicationContext(), "출고처리 완료");
                // 저장 후 결과 값 돌려주기
                Intent resultIntent = new Intent();
                // 결과처리 후 부른 Activity에 보낼 값
                resultIntent.putExtra("SIGN", "ADD");
                // 부른 Activity에게 결과 값 반환
                setResult(RESULT_OK, resultIntent);
                // 현재 Activity 종료 (validation에 걸려 return되어도 무조건 현재 activity가 꺼진다. 수정이 필요)
                finish();
            }


        });
    }

    private void initializeData() {

    }

    private void start() {
        String prodtorder_no_st = prodtorder_opr_no.getText().toString();

        if (!prodtorder_no_st.equals("")) {
            if (prodtorder_no_st.contains("-")) {
                String[] strArray = TGSClass.transHyphen(prodtorder_no_st);

                String prodtorder_no = strArray[0];
                String opr_no = strArray[1];

                dbQuery_PRODT_ORDER_INFO(prodtorder_no, opr_no);

                if (sJson.equals("")) {
                    TGSClass.AlertMessage(getApplicationContext(), "제조오더 정보가 없습니다.");
                    return;
                } else {
                    dbQuery_HDR(prodtorder_no, opr_no);
                }
            } else {
                TGSClass.AlertMessage(getApplicationContext(), "제조오더번호를 정확히 입력하여주시기 바랍니다.");
            }
        } else {
            TGSClass.AlertMessage(getApplicationContext(), "제조오더번호를 스캔해주시기 바랍니다.");
            return;
        }
    }

    private void dbQuery_PRODT_ORDER_INFO(final String prodt_order_no, final String opr_no) {
        Thread workThd_dbQuery_PRODT_ORDER_INFO = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_MES_LOCATION_PROD_OUT_ANDROID ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@PRODT_ORDER_NO = '" + prodt_order_no + "' ";

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
        workThd_dbQuery_PRODT_ORDER_INFO.start();   //스레드 시작
        try {
            workThd_dbQuery_PRODT_ORDER_INFO.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

            boolean jSonType = TGSClass.isJsonData(sJson);
            if (!jSonType) return;

            SELECT_PRODT_ORDER_INFO();

        } catch (InterruptedException ex) {

        }
    }

    private void SELECT_PRODT_ORDER_INFO() {
        try {
            JSONArray ja = new JSONArray(sJson);

            if (ja.length() == 0) return;

            JSONObject jObject = ja.getJSONObject(0);

            item_cd.setText(jObject.getString("ITEM_CD"));
            item_nm.setText(jObject.getString("ITEM_NM"));
            tracking_no.setText(jObject.getString("TRACKING_NO"));
            prodt_qty.setText(jObject.getString("PRODT_ORDER_QTY"));
            remain_qty.setText(jObject.getString("REMAIN_QTY"));
            good_qty.setText(jObject.getString("GOOD_QTY"));
            bad_qty.setText(jObject.getString("BAD_QTY"));
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private void dbQuery_HDR(final String prodt_order_no, final String opr_no) {
        Thread workThd_dbQuery_HDR = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_MES_LOCATION_PROD_OUT_ANDROID_QUERY_HDR ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@PRODT_ORDER_NO = '" + prodt_order_no + "' ";
                sql += " ,@OPR_NO = '" + opr_no + "' ";

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
        workThd_dbQuery_HDR.start();   //스레드 시작
        try {
            workThd_dbQuery_HDR.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

            boolean jSonType = TGSClass.isJsonData(sJson);

            if (!jSonType) return;

            SELECT_HDR();

        } catch (InterruptedException ex) {

        }
    }

    private void SELECT_HDR() {
        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            I37_HDR_ListViewAdapter listViewAdapter = new I37_HDR_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I37_HDR item = new I37_HDR();

                item.LOCATION           = jObject.getString("LOCATION");        // 적치장코드
                item.PRODT_ORDER_NO     = jObject.getString("PRODT_ORDER_NO");  // 제조오더번호
                item.OPR_NO             = jObject.getString("OPR_NO");          // 공정순서
                item.ITEM_CD            = jObject.getString("ITEM_CD");         // 품번
                item.ITEM_NM            = jObject.getString("ITEM_NM");         // 품명
                item.TRACKING_NO        = jObject.getString("TRACKING_NO");     // Tracking 번호
                item.REQ_QTY            = jObject.getString("REQ_QTY");         // 요청수량
                item.BASE_UNIT          = jObject.getString("BASE_UNIT");        // 기본단위
                item.SL_CD              = jObject.getString("SL_CD");           // 창고코드
                item.SL_NM              = jObject.getString("SL_NM");           // 창고명
                item.ISSUED_QTY         = jObject.getString("ISSUED_QTY");      // 납품수량(출고처리된수량)
                item.REMAIN_QTY         = jObject.getString("REMAIN_QTY");      // 잔량(출고처리할수량)
                item.REQ_NO             = jObject.getString("REQ_NO");          // 트레킹번호
                item.ISSUE_MTHD         = jObject.getString("ISSUE_MTHD");      // 트레킹번호
                item.RESV_STATUS        = jObject.getString("RESV_STATUS");     // 트레킹번호
                item.OUT_QTY            = jObject.getString("OUT_QTY");         // 적치장 재고 수량
                item.BAD_ON_HAND_QTY    = jObject.getString("BAD_ON_HAND_QTY"); //
                item.SEQ_NO             = jObject.getString("SEQ_NO");          // 트레킹번호

                listViewAdapter.addHDRItem(item);
            }

            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    I37_HDR vItem = (I37_HDR) parent.getItemAtPosition(position);

                    if (!vItem.getLOCATION().equals("출고대기장")) {
                        TGSClass.AlertMessage(getApplicationContext(), "선택하신 품목의 적치장이 [출고대기장]으로 이동되지 않았습니다.");
                    } else {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I37_DTL_Activity.class);
                        intent.putExtra("HDR", vItem);
                        startActivityForResult(intent, I37_DTL_REQUEST_CODE);
                    }
                }
            });

            TGSClass.AlertMessage(getApplicationContext(), ja.length() + " 건 조회 되었습니다.");
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (NullPointerException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private boolean dbSave_DTL(final String ITEM_DOCUMENT_NO, final String SL_CD, final String ITEM_CD, final String TRACKING_NO,
                              final String LOT_NO, final String LOT_SUB_NO, final String QTY, final String BASIC_UNIT,
                              final String LOCATION, final String BAD_ON_HAND_QTY, final String ORDER_NO) {
        Thread dtlThread = new Thread() {
            public void run() {
                //날짜
                String out_date_data = df.format(cal.getTime());

                String out_qty_insert_data = QTY.replace(".0", "").trim();

                String PRODT_ORDER_NO = ORDER_NO;

                String sql = " EXEC XUSP_WMS_I_GOODS_MOVEMENT_DTL_SET_CALCUATE_ANDROID ";
                sql += "@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "'";    //채번

                sql += ",@TRNS_TYPE = 'PI'";                                  //변경유형
                sql += ",@MOV_TYPE = 'I01'";                                   //이동유형
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";                  //공장코드
                sql += ",@DOCUMENT_DT = '" + out_date_data + "'";            //이동일자

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
                sql += ",@TRNS_LOC_CD = '" + LOCATION + "'";   //이동할 적치장

                sql += ",@RECORD_NO = '" + PRODT_ORDER_NO + "'";   //이동할 적치장

                sql += ",@BAD_ON_HAND_QTY = " + BAD_ON_HAND_QTY;         //불량 수량
                //sql += ",@MOVE_QTY = " + Double.parseDouble(out_qty_insert_data);                  //이동 수량
                sql += ",@MOVE_QTY = " + Integer.valueOf(out_qty_insert_data);                  //이동 수량
                sql += ",@DEBIT_CREDIT_FLAG = 'C'";                  //이동 수량

                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";
                sql += ",@EXTRA_FIELD1 = 'ANDROID'";
                sql += ",@EXTRA_FIELD2 = 'I37_HDR_Activity'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);

                result_msg.toString();
            }
        };
        dtlThread.start();   //스레드 시작
        try {
            dtlThread.join();  //dtlThread 종료될때까지 Main 쓰레드를 정지함.
        } catch (InterruptedException ex) {

        }
        return true;
    }

    private boolean dbSave_HDR() {
        Thread saveHDRThread = new Thread() {
            public void run() {
                //날짜
                String out_date_st = df.format(cal.getTime());

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_HDR_SET ";
                sql += "@TRNS_TYPE = 'PI'";
                sql += ",@MOV_TYPE = 'I01'";
                sql += ",@DOCUMENT_DT = '" + out_date_st + "'";
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
        saveHDRThread.start();   //스레드 시작
        try {
            saveHDRThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
        }

        if (sJson_hdr.equals("[]")) {
            // if (sJson.equals("[]")) {
            return false;
        } else {
            return true;
        }
    }

    private void dataSET(final String CUD_FLAG, final String vPlant_CD, final String PRODT_ORDER_NO,
                        final String OPR_NO, final String ITEM_CD, final String SEQ_NO,
                        final String OUT_QTY, final String REPORT_DT, final String vUnit_CD) {
        //
        Thread dataThread = new Thread() {
            public void run() {
                String sql = " EXEC dbo.XUSP_MES_APP_P10_GET_BL1 ";
                sql += " @CUD_FLAG = '" + CUD_FLAG + "'";
                sql += " ,@PLANT_CD = '" + vPlant_CD + "'";
                sql += " ,@PRODT_ORDER_NO = '" + PRODT_ORDER_NO + "'";
                sql += " ,@OPR_NO = '" + OPR_NO + "'";
                sql += " ,@ITEM_CD = '" + ITEM_CD + "'";
                sql += " ,@SEQ_NO = '" + SEQ_NO + "'";
                sql += " ,@OUT_QTY = " + OUT_QTY;
                sql += " ,@REPORT_DT = '" + REPORT_DT + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_DataSET = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        dataThread.start();   //스레드 시작
        try {
            dataThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.
        } catch (InterruptedException ex) {

        }
        //

        try {
            JSONArray ja = new JSONArray(sJson_DataSET);

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                PRODT_ORDER_NO_DataSET  = jObject.getString("PRODT_ORDER_NO");  // 체크 이벤트
                OPR_NO_DataSET          = jObject.getString("OPR_NO");          // Tracking 번호
                SL_CD_DataSET           = jObject.getString("SL_CD");           // Lot번호
                ITEM_CD_DataSET         = jObject.getString("ITEM_CD");         // 양품수량
                TRACKING_NO_DataSET     = jObject.getString("TRACKING_NO");     // 불량수량

                LOT_NO_DataSET          = jObject.getString("LOT_NO");          // 창고코드
                LOT_SUB_NO_DataSET      = jObject.getString("LOT_SUB_NO");      // 품목코드
                ENTRY_QTY_DataSET       = jObject.getString("ENTRY_QTY");       // LOT_SUB_NO
                ENTRY_UNIT_DataSET      = jObject.getString("ENTRY_UNIT");      // BASIC_UNIT
            }
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private boolean BL_DATASET_SELECT(final String CUD_FLAG, final String vPlant_CD, final String PRODT_ORDER_NO,
                                     final String OPR_NO, final String ITEM_CD, final String SEQ_NO,
                                     final String OUT_QTY, final String REPORT_DT, final String vUnit_CD) {
        Thread workThd_BL_DATASET_SELECT = new Thread() {
            public void run() {
                String cud_flag         = CUD_FLAG;
                String plant_cd         = vPlant_CD;
                String prodt_order_no   = PRODT_ORDER_NO;
                String opr_no           = OPR_NO;
                String item_cd          = ITEM_CD;
                String seq_no           = SEQ_NO;
                String out_qty          = OUT_QTY;
                String report_dt        = REPORT_DT;
                String unit_cd          = vUnit_CD;

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
                parm3.setName("prodt_order_no");
                parm3.setValue(prodt_order_no);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("opr_no");
                parm4.setValue(opr_no);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("item_cd");
                parm5.setValue(item_cd);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("seq_no");
                parm6.setValue(seq_no);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("out_qty");
                parm7.setValue(out_qty);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("report_dt");
                parm8.setValue(report_dt);
                parm8.setType(String.class);

                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("unit_cd");
                parm9.setValue(unit_cd);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("user_id");
                parm10.setValue(vUSER_ID);
                parm10.setType(String.class);

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

                result_msg = dba.SendHttpMessage("BL_SetPartListOut_ANDROID", pParms);
            }
        };
        workThd_BL_DATASET_SELECT.start();   //스레드 시작
        try {
            workThd_BL_DATASET_SELECT.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case I37_DTL_REQUEST_CODE:
                    String sign = data.getStringExtra("SIGN");
                    if (sign.equals("EXIT")) {
                        Toast.makeText(I37_HDR_Activity.this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (sign.equals("ADD")) {
                        Toast.makeText(I37_HDR_Activity.this, "추가 되었습니다.", Toast.LENGTH_SHORT).show();
                        start();
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case I37_DTL_REQUEST_CODE:
                    // Toast.makeText(I35_HDR_Activity.this, "취소", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}