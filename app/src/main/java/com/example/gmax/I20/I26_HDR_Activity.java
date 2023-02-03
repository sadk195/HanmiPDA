package com.example.gmax.I20;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gmax.BaseActivity;
import com.example.gmax.DBAccess;
import com.example.gmax.R;
import com.example.gmax.TGSClass;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;


public class I26_HDR_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonHDR = "", sJsonDTL = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private String MOV_REQ_NO;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(EditText) ==//
    private EditText MOVE_REQ_NO, REQ_LOCATION;

    //== View 선언(TextView) ==//
    private TextView REQ_DT, TRANS_TYPE, MOVE_TYPE, ISSUED_SL_CD, REQ_SL_CD, REQ_PERSON_NAME;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    private Button btn_save, btn_query;

    //== 날짜관련 변수 선언 ==//
    private DateFormat now;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i26_hdr);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();

        //DLVY_DT.setText(saveYear + "-" + saveMonth + "-" + saveDay);
        /*
        DLVY_DT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v);
            }
        });

        DLVY_DT.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
        */
        //initData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID             = getIntent().getStringExtra("MENU_ID");
        vMenuNm             = getIntent().getStringExtra("MENU_NM");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("START_COMMAND");

        MOV_REQ_NO          = getIntent().getStringExtra("vMOV_REQ_NO");

        //== ID 값 바인딩 ==//
        MOVE_REQ_NO         = (EditText) findViewById(R.id.move_req_no);        // 출고요청번호
        TRANS_TYPE          = (TextView) findViewById(R.id.trans_type);         // 출고유형
        REQ_DT              = (TextView) findViewById(R.id.req_dt);             // 요청일
        MOVE_TYPE           = (TextView) findViewById(R.id.move_type);          // 수불유형
        ISSUED_SL_CD        = (TextView) findViewById(R.id.issued_sl_cd);       // 출고창고
        REQ_SL_CD           = (TextView) findViewById(R.id.req_sl_cd);          // 요청창고
        REQ_PERSON_NAME     = (TextView) findViewById(R.id.req_person_name);    // 요청자
        REQ_LOCATION        = (EditText) findViewById(R.id.req_location);       // 적치장
        listview            = (ListView) findViewById(R.id.listOrder);

        btn_save            = (Button) findViewById(R.id.btn_save);             // 저장버튼
        btn_query           = (Button) findViewById(R.id.btn_query);            // 조회버튼
    }

    private void initializeCalendar() {
        TimeZone tz;
        now = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        tz = TimeZone.getTimeZone("Asia/Seoul");
        now.setTimeZone(tz);

        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MOVE_REQ_NO.getText().toString().equals("")) {
                    TGSClass.AlertMessage(getApplicationContext(), "출고요청번호를 스캔해주시기 바랍니다.");
                    return;
                } else {
                    start();
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void initializeData() {
        MOVE_REQ_NO.requestFocus();
    }

    private void start() {
        String move_req_no = MOVE_REQ_NO.getText().toString();

        dbQuery_get_INSP_REQ_NO_INFO(move_req_no);

        if (sJson.equals("") || !TGSClass.isJsonData(sJson) || sJson.equals("[]")) {
            TGSClass.AlertMessage(getApplicationContext(), "출고이동요청 정보가 없습니다.");
            return;
        } else {
            setHDR();
            setDTL();
        }
    }

    private void dbQuery_get_INSP_REQ_NO_INFO(final String MOV_REQ_NO) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThread_query_INSP_REQ_NO_INFO = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_APK_MOV_REQ_LIST ";
                sql += "  @PLANT_CD = '" + vPLANT_CD + "'";
                sql += "  ,@MOV_REQ_NO = '" + MOV_REQ_NO + "'";

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
        workThread_query_INSP_REQ_NO_INFO.start();   //스레드 시작
        try {
            workThread_query_INSP_REQ_NO_INFO.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private void setHDR() {
        try {
            JSONArray ja = new JSONArray(sJson);

            JSONObject jObject = ja.getJSONObject(0);

            MOVE_REQ_NO.setText(jObject.getString("MV_REQ_NO"));            // 출고이동요청번호
            REQ_DT.setText(jObject.getString("REQ_DT"));                    // 요청일?
            TRANS_TYPE.setText(jObject.getString("TRNS_TYPE"));             // 출고유형
            MOVE_TYPE.setText(jObject.getString("MOV_TYPE"));               // 수불유형
            REQ_SL_CD.setText(jObject.getString("MV_REQ_SL_CD"));              // 요청창고
            ISSUED_SL_CD.setText(jObject.getString("ISSUE_SL_CD"));         // 출고창고
            REQ_PERSON_NAME.setText(jObject.getString("MV_REQ_PERSON_NM"));    // 요청자

        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private void setDTL() {
        try {
            JSONArray ja = new JSONArray(sJson);

            I26_HDR_ListViewAdapter listViewAdapter = new I26_HDR_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);
                I26_HDR item = new I26_HDR();

                item.MV_REQ_SEQ             = jObject.getString("MV_REQ_SEQ");              //== 출고이동요청순번
                item.ITEM_CD                = jObject.getString("ITEM_CD");                 //== 품목코드
                item.ITEM_NM                = jObject.getString("ITEM_NM");                 //== 품목코드명
                item.MV_REQ_QTY             = jObject.getString("MV_REQ_QTY");              //== 출고이동요청수량
                item.MV_REQ_UNIT            = jObject.getString("MV_REQ_UNIT");             //== 출고이동요청단위
                item.DLVY_DT                = jObject.getString("DLVY_DT");                 //== 납기일
                item.TRACKING_NO            = jObject.getString("TRACKING_NO");             //== TRACKING_NO

                item.ISSUE_SL_CD            = jObject.getString("ISSUE_SL_CD");             //== 출고창고
                item.TRNS_TYPE              = jObject.getString("TRNS_TYPE");               //== 수불구분
                item.MOV_TYPE               = jObject.getString("MOV_TYPE");                //== 수불유형
                item.MV_REQ_SL_CD           = jObject.getString("MV_REQ_SL_CD");            //== 출고요청창고코드
                item.MV_REQ_PLANT_CD        = jObject.getString("MV_REQ_PLANT_CD");         //== 출고요청공장코드

                item.LOT_NO                 = jObject.getString("LOT_NO");                  //== LOT NO
                item.LOT_SUB_NO             = jObject.getString("LOT_SUB_NO");              //== LOT SUB NO
                item.ISSUE_LOCATION         = jObject.getString("ISSUE_LOCATION");          //== 출고적치장코드
                item.ISSUE_GOOD_ONHAND_QTY  = jObject.getString("ISSUE_GOOD_ONHAND_QTY");   //== 출고양품수량
                item.ISSUE_BAD_ONHAND_QTY   = jObject.getString("ISSUE_BAD_ONHAND_QTY");    //== 출고불량품수량

                item.REQ_QTY                = jObject.getString("REQ_QTY");                 //== 출고이동요청잔여수량
                item.ISSUE_QTY              = jObject.getString("ISSUE_QTY");               //== 출고수량
                item.MV_REQ_ONHAND_QTY      = jObject.getString("MV_REQ_ONHAND_QTY");       //== 현재고(요청창고)

                item.TRNS_TRACKING_NO       = jObject.getString("TRNS_TRACKING_NO");        //== 이동한TRACKING NO
                item.TRNS_LOT_NO            = jObject.getString("TRNS_LOT_NO");             //== 이동한LOT NO
                item.TRNS_LOT_SUB_NO        = jObject.getString("TRNS_LOT_SUB_NO");         //== 이동한LOT SUB NO
                item.TRNS_ITEM_CD           = jObject.getString("TRNS_ITEM_CD");            //== 이동한품목코드

                listViewAdapter.addItem(item);
            }
            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    I26_HDR vItem = (I26_HDR) parent.getItemAtPosition(position);

                    String msg = vItem.getISSUE_LOCATION();

                    TGSClass.AlertMessage(getApplicationContext(), msg, 10000);
                }
            });

            TGSClass.AlertMessage(getApplicationContext(), ja.length() + " 건 조회가 되었습니다.");
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (NullPointerException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    //== 저장 ==//
    private void save() {
        I26_HDR_ListViewAdapter Adap = (I26_HDR_ListViewAdapter) listview.getAdapter();

        int rowCnt = listview.getCount();
        // 난수 생성 후 String 형변환
        int maxnum = 99999;
        int minnum = 10000;
        Random rd = new Random();
        int rdNum = rd.nextInt(maxnum - minnum +1) + minnum;
        final String rdn = String.valueOf(rdNum);

        Date date = new Date();
        final String daynow = now.format(date);
        String strkey = daynow + rdn;

        ex_temp_delete(strkey);

        for (int i = 0; i < rowCnt; i++) {
            I26_HDR vItem = (I26_HDR) Adap.getItem(i);
            // 여기서부터 save로직 들어감
            String mv_qty   = vItem.getMV_REQ_QTY();
            String mv_seq   = vItem.getMV_REQ_SEQ();
            String mv_no    = MOVE_REQ_NO.getText().toString();

            ex_temp(mv_qty, mv_seq, mv_no, strkey);
        }
        // BL SEND
        if (bl_send(strkey, "A")) {
            TGSClass.AlertMessage(getApplicationContext(), result_msg);
            if (result_msg.contains("처리완료")) {
                if (!dbSave_HDR()) {
                    return;
                } else {
                    try {
                        JSONArray ja_HDR = new JSONArray(sJsonHDR);
                        JSONObject jObj_HDR = ja_HDR.getJSONObject(0);

                        String RTN_ITEM_DOCUMENT_NO     = jObj_HDR.getString("RTN_ITEM_DOCUMENT_NO");

                        for (int idx = 0; idx < rowCnt; idx++) {
                            I26_HDR item = (I26_HDR) Adap.getItem(idx);

                            //== HDR ==//
                            String dtl_trns_type        = item.getTRNS_TYPE();          //== 출고유형구분
                            String dtl_mov_type         = item.getMOV_TYPE();           //== 수불유형구분
                            String dtl_issue_sl_cd      = item.getISSUE_SL_CD();        //== 출고창고코드
                            String dtl_item_cd          = item.getITEM_CD();            //== 품목코드
                            String dtl_tracking_no      = item.getTRACKING_NO();        //== TRACKING NO
                            String dtl_lot_no           = item.getLOT_NO();             //== LOT NO
                            String dtl_lot_sub_no       = item.getLOT_SUB_NO();         //== LOT SUB NO
                            String dtl_qty              = item.getMV_REQ_ONHAND_QTY();  //== 현재고(요청창고)
                            String dtl_basic_unit       = item.getMV_REQ_UNIT();        //== 재고단위
                            String dtl_issue_location   = item.getISSUE_LOCATION();     //== 출고적치장코드

                            String dtl_trns_tracking_no = item.getTRNS_TRACKING_NO();   //== 출고요청TRACKING NO
                            String dtl_trns_lot_no      = item.getTRNS_LOT_NO();        //== 출고요청 LOT NO
                            String dtl_trns_lot_sub_no  = item.getTRNS_LOT_SUB_NO();    //== 출고요청 LOT SUB NO
                            String dtl_trns_plant_cd    = item.getMV_REQ_PLANT_CD();    //== 출고요청공장코드
                            String dtl_trns_sl_cd       = item.getMV_REQ_SL_CD();       //== 출고요청창고코드
                            String dtl_trns_item_cd     = item.getTRNS_ITEM_CD();       //== 출고요청품목코드
                                                                                        //== 출고요청적치장코드
                            String dtl_trns_qty         = item.getMV_REQ_QTY();         //== 요청량
                            String dtl_trns_bad_qty     = item.getISSUE_BAD_ONHAND_QTY();  //==

                            dbSave_DTL(RTN_ITEM_DOCUMENT_NO, dtl_trns_type, dtl_mov_type, dtl_issue_sl_cd, dtl_item_cd, dtl_tracking_no,
                                    dtl_lot_no, dtl_lot_sub_no, dtl_qty, dtl_basic_unit, dtl_issue_location, dtl_trns_tracking_no,
                                    dtl_trns_lot_no, dtl_trns_lot_sub_no, dtl_trns_plant_cd, dtl_trns_sl_cd, dtl_trns_item_cd, dtl_trns_qty, dtl_trns_bad_qty);
                        }
                    } catch (JSONException exJson) {
                        TGSClass.AlertMessage(getApplicationContext(), exJson.getMessage());
                        return;
                    } catch (Exception ex) {
                        TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
                        return;
                    }

                    // 초기화
                    MOVE_REQ_NO.setText(null);
                    ISSUED_SL_CD.setText(null);
                    REQ_SL_CD.setText(null);
                    TRANS_TYPE.setText(null);
                    MOVE_TYPE.setText(null);
                    REQ_DT.setText(null);
                    REQ_PERSON_NAME.setText(null);
                    // listview 초기화
                    I26_HDR_ListViewAdapter listViewAdapter = new I26_HDR_ListViewAdapter(); //
                    listview.setAdapter(listViewAdapter);
                }
            }
        } else {
            MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(I26_HDR_Activity.this);
            mAlert.setTitle("출고이동 불가")
                    .setMessage(result_msg)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //팝업 종료
                        }
                    })
                    .create().show();
            TGSClass.AlertMessage(getApplicationContext(), result_msg);
        }
    }

    private void ex_temp_delete(final String keycd) {
        Thread wkThd_EX_TEMP_DEL = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_APK_MOV_REQ_BL_TEMP_DELETE ";

                sql += "  @KEYCD = '" + keycd + "'";

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
        wkThd_EX_TEMP_DEL.start();   //스레드 시작
        try {
            wkThd_EX_TEMP_DEL.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private boolean bl_send(final String strkey, final String Type) {
        Thread wkThd_BL_SEND = new Thread() {
            public void run() {

                String strKeyCd = strkey;
                String istrType = Type; // 현재 A로만 넘김

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("strKeyCd");
                parm.setValue(strkey);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("istrType");
                parm2.setValue(Type);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("unit_cd");
                parm3.setValue(vUNIT_CD);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("user_id");
                parm4.setValue(vUSER_ID);
                parm4.setType(String.class);

                pParms.add(parm);
                pParms.add(parm2);
                pParms.add(parm3);
                pParms.add(parm4);

                result_msg = dba.SendHttpMessage("BL_MOVE_REQ", pParms);

            }


        };
        wkThd_BL_SEND.start();   //스레드 시작
        try {
            wkThd_BL_SEND.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.
            if (result_msg.contains("처리완료")) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException ex) {
            return false;
        }
    }

    private void ex_temp(final String mv_qty, final String mv_seq, final String mv_no, final String strkey) {
        Thread wkThd_EX_TEMP = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_APK_MOV_REQ_BL_TEMP_CREATE ";
                sql += "  @PLANT_CD = '" + vPLANT_CD + "'";
                sql += "  ,@MOV_REQ_NO = '" + mv_no + "'";
                sql += "  ,@MOV_QTY = '" + mv_qty + "'";
                sql += "  ,@MOV_REQ_SEQ = '" + mv_seq + "'";
                sql += "  ,@KEYCD = '" + strkey + "'";
                sql += "  ,@USER_ID = '" + vUSER_ID + "'";

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
        wkThd_EX_TEMP.start();   //스레드 시작
        try {
            wkThd_EX_TEMP.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    //== 수불정보_HDR 저장(?) ==//
    private boolean dbSave_HDR() {
        Thread wkThd_dbSave_HDR = new Thread() {
            public void run() {
                String str_move_date = df.format(cal.getTime());

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_HDR_SET ";
                sql += "@RTN_ITEM_DOCUMENT_NO = ''";
                sql += ",@DOCUMENT_YEAR = ''";
                sql += ",@TRNS_TYPE = '" + TRANS_TYPE.getText().toString() + "'";
                sql += ",@MOV_TYPE = '" + MOVE_TYPE.getText().toString() + "'";
                sql += ",@DOCUMENT_DT = '" + str_move_date + "'";
                sql += ",@RECORD_NO = ''";

                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";
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
    private boolean dbSave_DTL(final String ITEM_DOCUMENT_NO, final String TRNS_TYPE, final String MOV_TYPE, final String ISSUE_SL_CD, final String ITEM_CD,
                               final String TRACKING_NO, final String LOT_NO, final String LOT_SUB_NO, final String QTY, final String BASIC_UNIT, final String LOCATION,
                               final String TRNS_TRACKING_NO, final String TRNS_LOT_NO, final String TRNS_LOT_SUB_NO, final String TRNS_PLANT_CD, final String TRNS_SL_CD,
                               final String TRNS_ITEM_CD, final String TRNS_QTY, final String TRNS_BAD_QTY) {
        Thread wkThd_dbSave_DTL = new Thread() {
            public void run() {

                String str_move_date = df.format(cal.getTime());
                String str_move_location = REQ_LOCATION.getText().toString();

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DTL_SET ";
                sql += "@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "'";        // 채번
                sql += ",@DOCUMENT_YEAR = ''";                                  // 년도
                sql += ",@TRNS_TYPE = '" + TRNS_TYPE + "'";                     // 변경유형
                sql += ",@MOV_TYPE = '" + MOV_TYPE + "'";                       // 이동유형
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";                      // 공장코드

                sql += ",@DOCUMENT_DT = '" + str_move_date + "'";               // 이동일자

                /* I_ONHAND_STOCK_DETAIL 에서 바인딩 받아야 하므로 ListView에 조회되도록 SELECT 프로시저에 DTL항목 추가하고 바인딩 한 후 가져와야됨*/
                sql += ",@SL_CD = '" + ISSUE_SL_CD + "'";                       // 창고코드
                sql += ",@ITEM_CD = '" + ITEM_CD + "'";                         // 품목코드
                sql += ",@TRACKING_NO = '" + TRACKING_NO + "'";                 // TRACKING_NO
                sql += ",@LOT_NO = '" + LOT_NO + "'";                           // LOT_NO
                sql += ",@LOT_SUB_NO = " + LOT_SUB_NO;                          // LOT_SUB_NO
                sql += ",@QTY = " + QTY;                                        // 양품수량
                sql += ",@BASE_UNIT = '" + BASIC_UNIT + "'";                    // 재고단위
                sql += ",@LOC_CD = '" + LOCATION + "'";                         // 기존의 적치장
                /*
                sql += ",@TRNS_TRACKING_NO = '" + TRACKING_NO + "'";            //== 적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음
                sql += ",@TRNS_LOT_NO = '" + TRNS_LOT_NO + "'";                 //== 적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음
                sql += ",@TRNS_LOT_SUB_NO = " + TRNS_LOT_SUB_NO;                //== 적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음
                sql += ",@TRNS_PLANT_CD = '" + vPLANT_CD + "'";                 //== 적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음
                sql += ",@TRNS_SL_CD = '" + ISSUE_SL_CD + "'";                  //== 적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음
                sql += ",@TRNS_ITEM_CD = '" + ITEM_CD + "'";                    //== 적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음
                */
                sql += ",@TRNS_TRACKING_NO = '" + TRNS_TRACKING_NO + "'";       /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_NO = '" + TRNS_LOT_NO + "'";                 /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_SUB_NO = " + TRNS_LOT_SUB_NO;                /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_PLANT_CD = '" + TRNS_PLANT_CD + "'";             /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_SL_CD = '" + TRNS_SL_CD + "'";                   /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_ITEM_CD = '" + TRNS_ITEM_CD + "'";               /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/

                sql += ",@RECORD_NO = ''";                                      // 제조오더
                sql += ",@EXTRA_FIELD1 = 'ANDROID'";
                sql += ",@EXTRA_FIELD2 = 'I26_HDR_Activity'";

                sql += ",@BAD_ON_HAND_QTY = " + TRNS_BAD_QTY;                // 불량 수량
                sql += ",@MOVE_QTY = " + TRNS_QTY;                             // 이동 수량

                sql += ",@TRNS_LOC_CD = '" + str_move_location + "'";                                   // 이동할 적치장
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


