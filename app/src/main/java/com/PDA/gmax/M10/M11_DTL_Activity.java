package com.PDA.gmax.M10;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.DBAccess;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class M11_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJson_hdr = "", sJson_select_location_master = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private String INSPECT_REQ_NO;

    MaterialToolbar materialToolbar;

    //== View 선언(TextView) ==//
    private TextView insp_req_no, item_cd, item_nm, STS, INSPECTOR_NM;
    private TextView inspect_good_qty, inspect_bad_qty, mvmt_rcpt_qty, blank;
    private TextView bp_cd, bp_nm, MVMT_NO, M_STS, INSP_RESULT_NO, SL_CD_FOR_GOOD;
    private TextView SL_CD_FOR_DEFECT, qty, top_location;

    //== TextInputLayout ==//
    TextInputLayout start_dt, in_dt, save_location;
    TextInputEditText et_start_dt, et_in_dt;

    //== View 선언(EditText) ==//
//    private EditText insp_req_no, start_dt, item_cd, item_nm, STS, INSPECTOR_NM;
//    private EditText inspect_good_qty, inspect_bad_qty, mvmt_rcpt_qty, blank;
//    private EditText bp_cd, bp_nm, MVMT_NO, M_STS, INSP_RESULT_NO, SL_CD_FOR_GOOD;
//    private EditText SL_CD_FOR_DEFECT, in_dt, save_location, qty, top_location;

    //== View 선언(ImageView) ==//
//    private ImageView img_barcode;

    //== BottomSheetDialog ==//
    View bottomSheetView;
    BottomSheetDialog bsd;

    //== View 선언(Button) ==//
//    private Button btn_save;
    FloatingActionButton btn_save;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal1, cal2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m11_dtl);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
        //initData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        //ScanData에서 스캔 한 값을 받아 QM_10의 INSP_REQ_NO(검사의뢰번호)를 받아서 QM_11로 받아 오는 소스
        INSPECT_REQ_NO      = getIntent().getStringExtra("INSP_REQ_NO");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("JOB_CD");

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bottomSheetView = inflater.inflate(R.layout.bottom_sheet_dialog_layout, null, false);

        bsd = new BottomSheetDialog(this);
        bsd.setContentView(bottomSheetView);

//        bsd = new com.example.gmax.BottomSheetDialog("M11_DTL");

        //== ID 값 바인딩 ==//
        materialToolbar     = findViewById(R.id.materialToolbar);

        insp_req_no         = findViewById(R.id.insp_req_no);        // 검사요청번호
        item_cd             = findViewById(R.id.item_cd);            // 품번
        item_nm             = findViewById(R.id.item_nm);            // 품명
        STS                 = findViewById(R.id.STS);                // 진행상태
        INSPECTOR_NM        = findViewById(R.id.INSPECTOR_NM);       // 검사원
        inspect_good_qty    = findViewById(R.id.inspect_good_qty);   // 양품수량
        inspect_bad_qty     = findViewById(R.id.inspect_bad_qty);    // 불량수량
        mvmt_rcpt_qty       = findViewById(R.id.mvmt_rcpt_qty);      // 요청수량
        blank               = findViewById(R.id.blank);              // -빈칸-
        bp_cd               = findViewById(R.id.bp_cd);              // 고객사 코드
        bp_nm               = findViewById(R.id.bp_nm);              // 고객사명
        MVMT_NO             = findViewById(R.id.MVMT_NO);            //
        M_STS               = findViewById(R.id.M_STS);              // 검사상태
        INSP_RESULT_NO      = findViewById(R.id.INSP_RESULT_NO);     //
        SL_CD_FOR_GOOD      = findViewById(R.id.SL_CD_FOR_GOOD);     //
        SL_CD_FOR_DEFECT    = findViewById(R.id.SL_CD_FOR_DEFECT);   //

        /**
        start_dt            = findViewById(R.id.start_dt);           // 검사일자
        in_dt               = findViewById(R.id.in_dt);              // 입고일자
        save_location       = findViewById(R.id.save_location);      // 적치장
        */
        qty                 = findViewById(R.id.qty);                // 적치장 총합 재고수량
        top_location        = findViewById(R.id.top_location);       // 최다 재고수량이 있는 적치장코드

//        img_barcode         = (ImageView) findViewById(R.id.img_barcode);       // 바코드
        btn_save            = findViewById(R.id.btn_save);             // 저장버튼

        start_dt            = bottomSheetView.findViewById(R.id.start_dt);
        et_start_dt         = bottomSheetView.findViewById(R.id.et_start_dt);
        in_dt               = bottomSheetView.findViewById(R.id.in_dt);
        et_in_dt            = bottomSheetView.findViewById(R.id.et_in_dt);
        save_location       = bottomSheetView.findViewById(R.id.save_location);
    }

    private void initializeCalendar() {
        simpleDF = new SimpleDateFormat("yyyy-MM-dd");

        cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal1.setTime(new Date());

        cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal2.setTime(new Date());
    }

    private void initializeListener() {

        /**
        start_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openPopupDate(v, start_dt, cal1);
                openDate(start_dt, cal1);
            }
        });
        in_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openPopupDate(v, in_dt, cal2);
                openDate(v, in_dt, cal2);
            }
        });
        */
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription().equals(materialToolbar.getNavigationContentDescription())) {
                    /**
                    final Snackbar snackbar = Snackbar.make(v, "입고등록을 취소하시겠습니까?", Snackbar.LENGTH_LONG);
                    snackbar.setAction("취소", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                            // 저장 후 결과 값 돌려주기
                            Intent resultIntent = new Intent();
                            // 부른 Activity에게 결과 값 반환
                            setResult(RESULT_CANCELED, resultIntent);
                            // 현재 Activity 종료
                            finish();
                        }
                    }).show();
                    */
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bsd.show(getSupportFragmentManager(), bsd.getTag());
                bsd.show();
            }
        });

        et_start_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDate(v, cal1);
            }
        });

        et_in_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDate(v, cal2);
            }
        });

        MaterialButton btn_m11_save = bottomSheetView.findViewById(R.id.btn_m11_save);
        btn_m11_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (save_location.getEditText().getText().toString().equals("")) {
                    TGSClass.AlertMessage(getApplicationContext(), "입고할 적치장을 선택해주세요.");
                    save_location.requestFocus();
                    return;
                } else {
                    dbQuery_SELECT_LOCATION_MASTER();

                    if (sJson_select_location_master.equals("[]")) {
                        TGSClass.AlertMessage(getApplicationContext(), "입력하신 입고 적치장의 정보가 없습니다.\n다시 확인해 주시기 바랍니다.");
                        return;
                    } else {
                        save();
                    }
                }
            }
        });

        /**
        in_dt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) { //do your work here }
                    //start();
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
                    save_location.requestFocus();
                    return;
                } else {
                    dbQuery_SELECT_LOCATION_MASTER();

                    if (sJson_select_location_master.equals("[]")) {
                        TGSClass.AlertMessage(getApplicationContext(), "입력하신 입고 적치장의 정보가 없습니다.\n다시 확인해 주시기 바랍니다.");
                        return;
                    } else {
                        save();
                    }
                }
            }
        });

        img_barcode.setOnClickListener(qrClickListener);
        */
    }

    private void initializeData() {
        //== 날짜 셋팅 ==//
        /**
        start_dt.setText(df.format(cal1.getTime()));
        in_dt.setText(df.format(cal2.getTime()));
        */
        start_dt.getEditText().setText(df.format(cal1.getTime()));
        in_dt.getEditText().setText(df.format(cal2.getTime()));

        if (!TextUtils.isEmpty(INSPECT_REQ_NO)) {
            dbQuery_get_INSP_REQ_NO_INFO(INSPECT_REQ_NO);
            save_location.requestFocus();
        }
    }

    private void start() {
        if (!sJson.equals("[]")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    insp_req_no.setText(jObject.getString("INSPECT_REQ_NO"));           // 검사요청번호
                    start_dt.getEditText().setText(jObject.getString("MVMT_RCPT_DT"));                // 기간
                    item_cd.setText(jObject.getString("ITEM_CD"));                      // 품번
                    item_nm.setText(jObject.getString("ITEM_NM"));                      // 품명
                    STS.setText(jObject.getString("Q_STS_NM"));                         // 진행상태
                    INSPECTOR_NM.setText(jObject.getString("INSPECTOR_NM"));            // 검사원 명

                    M_STS.setText(jObject.getString("M_STS"));                          // 검사상태
                    INSP_RESULT_NO.setText(jObject.getString("INSP_RESULT_NO"));        //
                    SL_CD_FOR_GOOD.setText(jObject.getString("SL_CD_FOR_GOOD"));        //
                    SL_CD_FOR_DEFECT.setText(jObject.getString("SL_CD_FOR_DEFECT"));    //

                    inspect_good_qty.setText(jObject.getString("INSPECT_GOOD_QTY"));    // 양품수량
                    inspect_bad_qty.setText(jObject.getString("INSPECT_BAD_QTY"));      // 불량수량
                    mvmt_rcpt_qty.setText(jObject.getString("MVMT_RCPT_QTY"));          // 요청수량
                    bp_cd.setText(jObject.getString("BP_CD"));                          // 고객사 코드
                    bp_nm.setText(jObject.getString("BP_NM"));                          // 고객사명

                    qty.setText(jObject.getString("SUM_QTY"));                          // 적치장 총합 재고수량
                    top_location.setText(jObject.getString("TOP_LOCATION"));            // 최다 재고수량이 있는 적치장코드

                    MVMT_NO.setText(jObject.getString("MVMT_NO"));                      //
                }
            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage());
            }
        }
    }

    private void dbQuery_get_INSP_REQ_NO_INFO(final String pINSP_REQ_NO) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_query_INSP_REQ_NO_INFO = new Thread() {
            public void run() {
                String vITEM_CD = "";
                String vSTS = "";
                String vWK_ID = "";

                String sql = " EXEC XUSP_APK_QM11_GET_LIST ";
                sql += "  @PLANT_CD = '" + vPLANT_CD + "'";
                sql += "  ,@MVMT_RCPT_NO = '" + pINSP_REQ_NO + "'";
                sql += "  ,@ITEM_CD = '" + vITEM_CD + "'";
                sql += "  ,@STS = '" + vSTS + "'";
                sql += "  ,@WK_ID = '" + vWK_ID + "'";

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
        workThd_query_INSP_REQ_NO_INFO.start();   //스레드 시작
        try {
            workThd_query_INSP_REQ_NO_INFO.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

            try {
                start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (InterruptedException ex) {

        }
        //Start();
    }

    private boolean fncSet_list_bl(final String CUD_FLAG, final String in_dt, final String mvmt_no) {
        Thread workThd_fncSet_list_bl = new Thread() {
            public void run() {

                String cud_flag_parm    = CUD_FLAG;
//                String in_dt_parm       = in_dt;
                String mvmt_no_st       = mvmt_no;
                String unit_cd          = vUNIT_CD;

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("cud_flag");
                parm.setValue(cud_flag_parm);
                parm.setType(String.class);

//                PropertyInfo parm2 = new PropertyInfo();
//                parm2.setName("in_dt");
//                parm2.setValue(in_dt_parm);
//                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("mvmt_no");
                parm3.setValue(mvmt_no_st);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("unit_cd");
                parm4.setValue(unit_cd);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("user_id");
                parm5.setValue(vUSER_ID);
                parm5.setType(String.class);

                pParms.add(parm);
//                pParms.add(parm2);
                pParms.add(parm3);
                pParms.add(parm4);
                pParms.add(parm5);

                result_msg = dba.SendHttpMessage("BL_SetInspectResult_ANDROID", pParms);
            }
        };
        workThd_fncSet_list_bl.start();   //스레드 시작
        try {
            workThd_fncSet_list_bl.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }

    private boolean dbQuery_GET_BL(final String cud_flag, final String plant_cd, final String insp_req_no, final String insp_result_no, final String in_dt, final String sl_cd_for_good
            , final String sl_cd_for_defect, final String pCommandSend) {
        Thread workThd_GET_BL = new Thread() {
            public void run() {
                String cud_flag_parm            = cud_flag;
                String plant_cd_parm            = plant_cd;
                String insp_req_no_parm         = insp_req_no;
                String insp_result_no_parm      = insp_result_no;
//                String in_dt_parm               = in_dt;
                String sl_cd_for_good_parm      = sl_cd_for_good;
                String sl_cd_for_defect_parm    = sl_cd_for_defect;
                String pCommandSend_parm        = pCommandSend;
                String unit_cd                  = vUNIT_CD;

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("cud_flag");
                parm.setValue(cud_flag_parm);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("plant_cd");
                parm2.setValue(plant_cd_parm);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("insp_req_no");
                parm3.setValue(insp_req_no_parm);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("insp_result_no");
                parm4.setValue(insp_result_no_parm);
                parm4.setType(String.class);

//                PropertyInfo parm5 = new PropertyInfo();
//                parm5.setName("in_dt");
//                parm5.setValue(in_dt_parm);
//                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("sl_cd_for_good");
                parm6.setValue(sl_cd_for_good_parm);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("sl_cd_for_defect");
                parm7.setValue(sl_cd_for_defect_parm);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("pCommandSend");
                parm8.setValue(pCommandSend_parm);
                parm8.setType(Integer.class);

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
//                pParms.add(parm5);
                pParms.add(parm6);
                pParms.add(parm7);
                pParms.add(parm8);
                pParms.add(parm9);
                pParms.add(parm10);

                result_msg = dba.SendHttpMessage("BL_Test_Results_ANDROID", pParms);
            }
        };
        workThd_GET_BL.start();   //스레드 시작
        try {
            workThd_GET_BL.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

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
//                sql += " AND LOCATION_CD = '" + save_location.getEditText().getText().toString() + "'";

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

    //== 저장 ==//
    private void save() {
        if (M_STS.getText().toString().equals("IF")) {
//            String in_dt_st     = in_dt.getEditText().getText().toString();
            String MVMT_NO_st   = MVMT_NO.getText().toString();

//            fncSet_list_bl("C", in_dt_st, MVMT_NO_st);

            MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(M11_DTL_Activity.this);
            mAlert.setTitle("구매입고등록\n(검사완료품 입고등록)")
                    .setMessage(result_msg)
                    .setCancelable(false) // Dialog 밖이나 뒤로가기 막기위한 소스 true : 풀기, false : 막기
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (result_msg.contains("입고저장")) {
                                if (dbSave_HDR() == true) {
                                    try {
                                        JSONArray ja_hdr = new JSONArray(sJson_hdr);
                                        JSONObject jObject_hdr = ja_hdr.getJSONObject(0);

                                        String RTN_ITEM_DOCUMENT_NO = jObject_hdr.getString("RTN_ITEM_DOCUMENT_NO");

                                        JSONArray ja = new JSONArray(sJson);

                                        for (int idx = 0; idx < ja.length(); idx++) {
                                            JSONObject jObject = ja.getJSONObject(idx);

                                            String sl_cd            = jObject.getString("SL_CD");               // 창고코드
                                            String item_cd          = jObject.getString("ITEM_CD");             // 품목코드
                                            String tracking_no      = jObject.getString("TRACKING_NO");         // 트래킹번호
                                            String lot_no           = jObject.getString("LOT_NO");              // 롯트번호
                                            String lot_sub_no       = jObject.getString("LOT_SUB_NO");          // 롯트순번
                                            String qty              = jObject.getString("INSPECT_GOOD_QTY");    // 검사수량 = 검사 후 입고수량
                                            String basic_unit       = jObject.getString("BASIC_UNIT");          // 재고단위
//                                            String save_location_st = save_location.getEditText().getText().toString();              // 적치장
                                            String bad_on_hand_qty  = jObject.getString("INSPECT_BAD_QTY");     // 불량수량

//                                            dbSave_DTL(RTN_ITEM_DOCUMENT_NO, sl_cd, item_cd, tracking_no, lot_no, lot_sub_no, qty, basic_unit, save_location_st, bad_on_hand_qty);
                                        }
                                    } catch (JSONException ex) {
                                        TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
                                    } catch (Exception e1) {
                                        TGSClass.AlertMessage(getApplicationContext(), "catch : e1");
                                    }
                                    // 저장 후 결과 값 돌려주기
                                    Intent resultIntent = new Intent();
                                    // 결과처리 후 부른 Activity에 보낼 값
                                    resultIntent.putExtra("M_STS", "IF");
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
        } else {
            String CUD_FLAG             = "C";
            String insp_req_no_st       = insp_req_no.getText().toString();
            String INSP_RESULT_NO_st    = INSP_RESULT_NO.getText().toString();
//            String in_dt_st             = in_dt.getEditText().getText().toString();       //입고일자
            String SL_CD_FOR_GOOD_st    = SL_CD_FOR_GOOD.getText().toString();
            String SL_CD_FOR_DEFECT_st  = SL_CD_FOR_DEFECT.getText().toString();
            String pCommandSend         = "CONFIRM";

            if (!insp_req_no_st.equals("") && !INSP_RESULT_NO_st.equals("")) {
//                dbQuery_GET_BL(CUD_FLAG, vPLANT_CD, insp_req_no_st, INSP_RESULT_NO_st, in_dt_st, SL_CD_FOR_GOOD_st, SL_CD_FOR_DEFECT_st, pCommandSend);
            } else {
                TGSClass.AlertMessage(getApplicationContext(), "검사의뢰번호 혹은 검사결과번호가 없습니다.");
                return;
            }

            MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(M11_DTL_Activity.this);
            mAlert.setTitle("구매입고등록\n(검사완료품 입고등록)")
                    .setMessage(result_msg)
                    .setCancelable(false) // Dialog 밖이나 뒤로가기 막기위한 소스 true : 풀기, false : 막기
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (result_msg.contains("입고저장")) {
                                // 저장 후 결과 값 돌려주기
                                Intent resultIntent = new Intent();
                                // 결과처리 후 부른 Activity에 보낼 값
                                resultIntent.putExtra("M_STS", "ELSE");
                                // 부른 Activity에게 결과 값 반환
                                setResult(RESULT_OK, resultIntent);
                                // 현재 Activity 종료
                                finish();
                            } else {
                                return;
                            }
                        }
                    })
                    .create().show();
        }
    }

    private boolean dbSave_HDR() {
        Thread workThd_save_HDR = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_HDR_SET ";
                sql += "@TRNS_TYPE = 'PR'";
                sql += ",@MOV_TYPE = 'R01'";
//                sql += ",@DOCUMENT_DT = '" + in_dt.getEditText().getText().toString() + "'";
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
        workThd_save_HDR.start();   //스레드 시작
        try {
            workThd_save_HDR.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

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
        Thread workThd_save_DTL = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_WMS_I_GOODS_MOVEMENT_DTL_SET_CALCUATE_ANDROID ";
                sql += "@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "'";    //채번
                // += ",@DOCUMENT_YEAR =";                                  //프로시저 안에서 적용
                sql += ",@TRNS_TYPE = 'PR'";                                  //변경유형
                sql += ",@MOV_TYPE = 'R01'";                                   //이동유형
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";                  //공장코드
//                sql += ",@DOCUMENT_DT = '" + in_dt.getEditText().getText().toString() + "'";            //이동일자(t)

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
                sql += ",@TRNS_LOT_SUB_NO = " + LOT_SUB_NO;              /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_PLANT_CD = '" + vPLANT_CD + "'";                 /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_SL_CD = '" + SL_CD + "'";                        /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_ITEM_CD = '" + ITEM_CD + "'";                    /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOC_CD = '" + LOCATION + "'";   //이동할 적치장

                sql += ",@BAD_ON_HAND_QTY = " + BAD_ON_HAND_QTY;         //불량 수량
                sql += ",@MOVE_QTY = " + Double.parseDouble(QTY);          //이동 수량
                sql += ",@DEBIT_CREDIT_FLAG = 'D'";                  //이동 수량

                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";
                sql += ",@EXTRA_FIELD1 = 'ANDROID'";
                sql += ",@EXTRA_FIELD2 = 'M11_DTL_Activity'";

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
        workThd_save_DTL.start();   //스레드 시작
        try {
            workThd_save_DTL.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }
}
