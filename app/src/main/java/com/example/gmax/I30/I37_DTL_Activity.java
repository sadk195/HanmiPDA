package com.example.gmax.I30;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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


public class I37_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJson_hdr = "", sJson_DataSET = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private I37_HDR vGetHDRItem;

    //== View 선언(TextView) ==//
    private TextView item_cd, tracking_no, out_qty_query, resvd_issued;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(EditText) ==//
    private EditText out_date, out_qty_insert;

    //== View 선언(Button) ==//
    private Button btn_add, btn_save;

    //== 날짜 관련 변수 선언 ==//
    private Calendar cal;

    //== BL 관련 변수 선언 ==//
    public String PRODT_ORDER_NO_DataSET = "", OPR_NO_DataSET = "", SL_CD_DataSET = "";
    public String ITEM_CD_DataSET = "", TRACKING_NO_DataSET = "", LOT_NO_DataSET = "";
    public String LOT_SUB_NO_DataSET = "", ENTRY_QTY_DataSET = "", ENTRY_UNIT_DataSET = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i37_dtl);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID         = getIntent().getStringExtra("MENU_ID");
        vMenuNm         = getIntent().getStringExtra("MENU_NM");
        vMenuRemark     = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand   = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== HDR 값 바인딩 ==//
        vGetHDRItem         = (I37_HDR)getIntent().getSerializableExtra("HDR");

        //== ID 값 바인딩 ==//
        item_cd             = (TextView) findViewById(R.id.item_cd);
        tracking_no         = (TextView) findViewById(R.id.tracking_no);
        out_qty_query       = (TextView) findViewById(R.id.out_qty_query);
        resvd_issued        = (TextView) findViewById(R.id.resvd_issued);

        listview            = (ListView) findViewById(R.id.listOrder);

        out_date            = (EditText) findViewById(R.id.out_date);
        out_qty_insert      = (EditText) findViewById(R.id.out_qty_insert);

        btn_save            = (Button) findViewById(R.id.btn_save);
        btn_add             = (Button) findViewById(R.id.btn_add);
    }

    private void initializeCalendar() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {
        out_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, out_date, cal);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 기존 소스는 DBSave() 메서드로 옮김
                dbSave("EXIT");
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 기존 소스는 DBSave() 메서드로 옮김
                dbSave("ADD");
            }
        });
    }

    private void initializeData() {
        out_date.setText(df.format(cal.getTime()));

        String req_qty = vGetHDRItem.getREQ_QTY();
        String issued_qty = vGetHDRItem.getISSUED_QTY();

        double req_qty_du = Double.parseDouble(req_qty);
        double issued_qty_du = Double.parseDouble(issued_qty);
        double req_issued = req_qty_du - issued_qty_du;

        String req_issued_st = String.valueOf(req_issued);

        // 가져온 값 할당
        item_cd.setText(vGetHDRItem.getITEM_CD());
        tracking_no.setText(vGetHDRItem.getTRACKING_NO());
        out_qty_query.setText(vGetHDRItem.getOUT_QTY());
        resvd_issued.setText(req_issued_st);

        out_qty_insert.setText(req_issued_st.replace(".0", ""));

        start();
    }

    private void start() {
        dbQuery(vGetHDRItem.getITEM_CD());

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            I37_DTL_ListViewAdapter listViewAdapter = new I37_DTL_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I37_DTL item = new I37_DTL();

                item.ITEM_CD      = jObject.getString("ITEM_CD");  //
                item.ITEM_NM      = jObject.getString("ITEM_NM");  //
                item.GOOD_QTY     = jObject.getString("GOOD_QTY");  //
                item.SL_CD        = jObject.getString("SL_CD");  //
                item.SL_NM        = jObject.getString("SL_NM");  //
                item.TRACKING_NO  = jObject.getString("TRACKING_NO");  //
                //item.GOOD_ON_HAND_QTY = jObject.getString("GOOD_ON_HAND_QTY");  //
                item.WMS_QTY      = jObject.getString("WMS_QTY");  //

                listViewAdapter.addDTLItem(item);
            }
            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {

                }
            });
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private void dbQuery(final String vitem_cd) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_I_ONHAND_STOCK_GET_ANDROID_I37_DTL ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@ITEM_CD = '" + vitem_cd +"' ";

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
        }
        catch (InterruptedException ex) {

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

    private void dbSave(String val) {
        String out_qty_query_st = out_qty_query.getText().toString();
        double out_qty_query_num = Double.valueOf(out_qty_query_st);

        String out_qty_insert_st = out_qty_insert.getText().toString();
        double out_qty_insert_num = Double.valueOf(out_qty_insert_st);

        String out_date_data = out_date.getText().toString();

        if(out_qty_insert_num != 0) { // 입력한 출고량이 0이 아닐때
            if(out_qty_query_num < out_qty_insert_num) { //입력한 출고량이 출고가능 수량보다 많을 경우 리턴
                TGSClass.AlertMessage(getApplicationContext(), "입력한 출고량이 출고가능수량 보다 많습니다.");
                return;
            }
            else {
                String CUD_FLAG = "U";
                String PRODT_ORDER_NO   = vGetHDRItem.getPRODT_ORDER_NO();
                String OPR_NO           = vGetHDRItem.getOPR_NO();
                String ITEM_CD          = vGetHDRItem.getITEM_CD();
                String SEQ_NO           = vGetHDRItem.getSEQ_NO();
                String OUT_QTY          = out_qty_insert_st;
                String REPORT_DT        = out_date_data;

                String LOCATION         = vGetHDRItem.getLOCATION();
                String BAD_ON_HAND_QTY  = vGetHDRItem.getBAD_ON_HAND_QTY();

                if(BL_DATASET_SELECT(CUD_FLAG, vPLANT_CD, PRODT_ORDER_NO, OPR_NO, ITEM_CD, SEQ_NO, OUT_QTY, REPORT_DT, vUNIT_CD) == true) {  //생산출고 BL
                    dataSET();
                    //2021-01-28 예정
                    //기존 ERP BL을 태우면  ZZ_WMS_I_GOODS_MOVEMENT_HDR 같은 사이트 테이블에는 INSERT 되지 않으므로 직접 인서트나 업데이트 프로시저를 제작 2021-01-27 정영진 주임

                    if (result_msg.contains("생산출고 완료")) {
                        if(dbSave_HDR() == true) {
                            try {
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

                                dbSave_DTL(RTN_ITEM_DOCUMENT_NO, sl_cd, item_cd, tracking_no, lot_no, lot_sub_no, qty, basic_unit, location, bad_on_hand_qty);
                                TGSClass.AlertMessage(getApplicationContext(), RTN_ITEM_DOCUMENT_NO + "자동입력번호로 저장되었습니다.");

                                setResult(true, val);
                            } catch (JSONException ex) {
                                TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
                            } catch (Exception e1) {
                                TGSClass.AlertMessage(getApplicationContext(), "catch : e1");
                            }
                        }
                    } else {
                        TGSClass.AlertMessage(getApplicationContext(), result_msg);
                        setResult(false);
                        return;
                    }
                } else {
                    TGSClass.AlertMessage(getApplicationContext(), result_msg);
                    setResult(false);
                    return;
                }
            }
        } else {
            TGSClass.AlertMessage(getApplicationContext(), "입력한 출고량이 0입니다.");
            setResult(false);
            return;
        }

    }

    private boolean dbSave_HDR() {
        Thread workThd_dbSave_HDR = new Thread() {
            public void run() {

                String out_date_st = out_date.getText().toString();

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

                String out_date_data = out_date.getText().toString();

                String out_qty_insert_data = out_qty_insert.getText().toString();

                String PRODT_ORDER_NO = vGetHDRItem.getPRODT_ORDER_NO();

                String sql = " EXEC XUSP_WMS_I_GOODS_MOVEMENT_DTL_SET_CALCUATE_ANDROID ";
                sql += "@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "'";    //채번
                // += ",@DOCUMENT_YEAR =";                                  //프로시저 안에서 적용
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
                sql += ",@MOVE_QTY = " + Integer.parseInt(out_qty_insert_data);                  //이동 수량
                sql += ",@DEBIT_CREDIT_FLAG = 'C'";                  //이동 수량

                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";
                sql += ",@EXTRA_FIELD1 = 'ANDROID'";
                sql += ",@EXTRA_FIELD2 = 'I37_DTL_Activity'";

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
        }
        catch (InterruptedException ex) {

        }
        return true;
    }


    private void dataSET() {

        dbQuery_dataSET();

        try {
            JSONArray ja = new JSONArray(sJson_DataSET);

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                PRODT_ORDER_NO_DataSET  = jObject.getString("PRODT_ORDER_NO");  //체크 이벤트
                OPR_NO_DataSET          = jObject.getString("OPR_NO");  //Tracking 번호
                SL_CD_DataSET           = jObject.getString("SL_CD");  //Lot번호
                ITEM_CD_DataSET         = jObject.getString("ITEM_CD");  //양품수량
                TRACKING_NO_DataSET     = jObject.getString("TRACKING_NO");  //불량수량

                LOT_NO_DataSET          = jObject.getString("LOT_NO");  //창고코드
                LOT_SUB_NO_DataSET      = jObject.getString("LOT_SUB_NO");  //품목코드
                ENTRY_QTY_DataSET       = jObject.getString("ENTRY_QTY");  //LOT_SUB_NO
                ENTRY_UNIT_DataSET      = jObject.getString("ENTRY_UNIT");  //BASIC_UNIT
            }
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private void dbQuery_dataSET() {
        Thread workThd_dbQuery_dataSET = new Thread() {
            public void run() {

                String out_qty_query_st = out_qty_query.getText().toString();
                double out_qty_query_num = Double.valueOf(out_qty_query_st);

                String out_qty_insert_st = out_qty_insert.getText().toString();
                double out_qty_insert_num = Double.valueOf(out_qty_insert_st);

                String out_date_data = out_date.getText().toString();

                String CUD_FLAG         = "U";
                String PRODT_ORDER_NO   = vGetHDRItem.getPRODT_ORDER_NO();
                String OPR_NO           = vGetHDRItem.getOPR_NO();
                String ITEM_CD          = vGetHDRItem.getITEM_CD();
                String SEQ_NO           = vGetHDRItem.getSEQ_NO();
                String OUT_QTY          = out_qty_insert_st;
                String REPORT_DT        = out_date_data;

                String sql = " EXEC dbo.XUSP_MES_APP_P10_GET_BL1 ";
                sql += " @CUD_FLAG = '" + CUD_FLAG + "'";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "'";
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
        workThd_dbQuery_dataSET.start();   //스레드 시작
        try {
            workThd_dbQuery_dataSET.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.
        } catch (InterruptedException ex) {

        }
    }

    private void setResult(boolean bool) {
        setResult(bool, "");
    }

    private void setResult(boolean bool, String val) {
        // 저장 후 결과 값 돌려주기
        Intent resultIntent = new Intent();

        if (bool) {
            // 결과처리 후 부른 Activity에 보낼 값
            resultIntent.putExtra("SIGN", val);
            // 부른 Activity에게 결과 값 반환
            setResult(RESULT_OK, resultIntent);
        } else {
            // 부른 Activity에게 결과 값 반환
            setResult(RESULT_CANCELED, resultIntent);
        }

        // 현재 Activity 종료 (validation에 걸려 return되어도 무조건 현재 activity가 꺼진다. 수정이 필요)
        finish();
    }
}