package com.example.gmax.P10;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gmax.BaseActivity;
import com.example.gmax.DBAccess;
import com.example.gmax.R;
import com.example.gmax.ScanData;
import com.example.gmax.TGSClass;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class P14_QUERY_Activity extends BaseActivity {

    private final long INTERVAL_TIME = 500;  //간격
    private long inputEnterPressedTime = 0;  //엔터키 입력 시간.

    //== JSON 선언 ==//
    private String sJson = "", sJsonCombo = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(TextView) ==//
    private TextView lbl_view_title;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(EditText) ==//
    private EditText txt_Scan_lbl_prodt_order_no;

    //== View 선언(Button) ==//
    private Button btn_menu, btn_save_go;

    public LinearLayout app_view3;
    public TextView slbl_count;
    public String sJOB_CD;
    public String sJson_check;
    public String WC_CD = "";

    public String Msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p14_query);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID                         = getIntent().getStringExtra("MENU_ID");
        vMenuNm                         = getIntent().getStringExtra("MENU_NM");
        vMenuRemark                     = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand                   = getIntent().getStringExtra("START_COMMAND");

        //== ID 값 바인딩 ==//
        lbl_view_title                  = (TextView) findViewById(R.id.lbl_view_title);
        img_barcode                     = (ImageView) findViewById(R.id.img_barcode);
        txt_Scan_lbl_prodt_order_no     = (EditText) findViewById(R.id.txt_Scan_lbl_prodt_order_no);

        btn_menu                        = (Button) findViewById(R.id.btn_menu);
        btn_save_go                     = (Button) findViewById(R.id.btn_save_go);
    }

    private void initializeListener() {

        img_barcode.setOnClickListener(qrClickListener);

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //정영진
                Intent intent = TGSClass.ChangeView(getPackageName(), P10_Activity.class);
                startActivity(intent);
            }
        });

        //태그 및 바코드 스캔 이벤트
        txt_Scan_lbl_prodt_order_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //event.getAction() == KeyEvent.ACTION_DOWN) &&
                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    int Scan_Text_Len = txt_Scan_lbl_prodt_order_no.getText().toString().length();
                    if (!txt_Scan_lbl_prodt_order_no.getText().toString().equals("") && (Scan_Text_Len == 12 || Scan_Text_Len == 11)) {
                        String prodt_order_no_st = txt_Scan_lbl_prodt_order_no.getText().toString();
                        String prodt_order_no = prodt_order_no_st.substring(0, 8);
                        String opr_no = prodt_order_no_st.substring(9, 12);

                        dbQuery_XUSP_MES_APP_PH50_CHECK(prodt_order_no, opr_no);    //이전공정 체크

                        if (!Msg.equals("")) {
                            MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_QUERY_Activity.this);
                            mAlert.setTitle("이전공정 체크")
                                    .setMessage(Msg)
                                    .setPositiveButton("확인", null)
                                    .create().show();

                            return false;
                        }

                        dbQuery_chk_outsourcing(prodt_order_no, opr_no);            //외주공정 체크

                        if (!Msg.equals("")) {
                            MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_QUERY_Activity.this);
                            mAlert.setTitle("외주공정 체크")
                                    .setMessage(Msg)
                                    .setPositiveButton("확인", null)
                                    .create().show();

                            return false;
                        }

                        Intent intent = TGSClass.ChangeView(getPackageName(), P14_SAVE_Activity.class);
                        intent.putExtra("prodt_order_no", prodt_order_no);  // SL_CD 객체를 파라메터로 다음페이지로 넘김.
                        intent.putExtra("opr_no", opr_no);  // SL_CD 객체를 파라메터로 다음페이지로 넘김.
                        startActivity(intent);

                        //return SetSCanData(sJobCode);
                        return true;
                    } else {
                        TGSClass.AlertMessage(getApplicationContext(), "[제조오더번호-공순]을 정확히 입력하여 주시기 바랍니다");
                        return false;
                    }
                }
                return false;
            }

        });

        txt_Scan_lbl_prodt_order_no.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("x5", s.toString());
                if (!s.equals("")) {
                    //do your work here
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {

                if (txt_Scan_lbl_prodt_order_no.getText().toString().length() >= 17) {
                    if (!setScanData(vStartCommand)) return;
                }
            }
        });

        btn_save_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                EditText txt_Scan_lbl_prodt_order_no = findViewById(R.id.txt_Scan_lbl_prodt_order_no);
                String prodt_order_no_st = txt_Scan_lbl_prodt_order_no.getText().toString();
                String prodt_order_no = prodt_order_no_st.substring(0,8);
                String opr_no = prodt_order_no_st.substring(9,12);

                Intent intent = TGSClass.ChangeView(getPackageName(), P14_SAVE_Activity.class);
                intent.putExtra("prodt_order_no",prodt_order_no);  // SL_CD 객체를 파라메터로 다음페이지로 넘김.
                intent.putExtra("opr_no",opr_no);  // SL_CD 객체를 파라메터로 다음페이지로 넘김.
                startActivity(intent);

                String txt_Scan_lbl_prodt_order_no_st  = txt_Scan_lbl_prodt_order_no.getText().toString();
                */

                String prodt_order_no_st = txt_Scan_lbl_prodt_order_no.getText().toString();
                String prodt_order_no = prodt_order_no_st.substring(0, 8);
                String opr_no = prodt_order_no_st.substring(9, 12);

                dbQuery_XUSP_MES_APP_PH50_CHECK(prodt_order_no, opr_no);    //이전공정 체크
                if (!Msg.equals("")) {
                    MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_QUERY_Activity.this);
                    mAlert.setTitle("이전공정 체크")
                            .setMessage(Msg)
                            .setPositiveButton("확인", null)
                            .create().show();

                    return;
                }

                dbQuery_chk_outsourcing(prodt_order_no, opr_no);            //외주공정 체크.
                if (!Msg.equals("")) {
                    MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_QUERY_Activity.this);
                    mAlert.setTitle("공정별 실적등록")
                            .setMessage(Msg)
                            .setPositiveButton("확인", null)
                            .create().show();

                    return;
                }

                Intent intent = TGSClass.ChangeView(getPackageName(), P14_SAVE_Activity.class);
                intent.putExtra("prodt_order_no", prodt_order_no);  // SL_CD 객체를 파라메터로 다음페이지로 넘김.
                intent.putExtra("opr_no", opr_no);  // SL_CD 객체를 파라메터로 다음페이지로 넘김.
                startActivity(intent);
            }
        });
    }

    private void initializeData() {

        lbl_view_title.setText(vMenuNm);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                TGSClass.AlertMessage(getApplicationContext(), "취소!");
            } else {
                //qrcode 결과가 있으면
                TGSClass.AlertMessage(getApplicationContext(), "스캔완료");

                txt_Scan_lbl_prodt_order_no.setText(result.getContents());
                setScanData(vStartCommand);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //스캔 데이터 값 처리.
    private boolean setScanData(String pJobCD) {
        String vScanText = txt_Scan_lbl_prodt_order_no.getText().toString();

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - inputEnterPressedTime;

        if (0 <= intervalTime && INTERVAL_TIME >= intervalTime) {
            /* FINISH_INTERVAL_TIME 밀리 초 안에 엔터키 이벤트가 발생하면, 뒤에 생긴 이벤트 무시.*/
            return false;
        } else {
            inputEnterPressedTime = tempTime;
        }

        //스캔 데이터 생성 클래스
        ScanData scan = new ScanData(vScanText);

        //String vRFID = scan.get_RFID();
        //String vVISS = scan.get_VISS();

        txt_Scan_lbl_prodt_order_no.setText(null);

        //ChangeView(vRFID,vVISS,pJobCD,"", sMenuRemark);

        return true;
    }

    private void dbQuery_XUSP_MES_APP_PH50_CHECK(final String prodt_order_no, final String opr_no) {
        Thread wkThd_dbQuery_XUSP_MES_APP_PH50_CHECK = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_MES_APP_PH50_CHECK ";
                sql += "    @PLANT_CD ='" + vPLANT_CD + "'";
                sql += "   , @PRODT_ORDER_NO ='" + prodt_order_no + "'";
                sql += "   , @OPR_NO ='" + opr_no + "'";
                sql += "   , @QTY  = 1";
                sql += "   , @RTNMSG  = ''";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);

                check_last_router();
            }
        };
        wkThd_dbQuery_XUSP_MES_APP_PH50_CHECK.start();   //스레드 시작
        try {
            wkThd_dbQuery_XUSP_MES_APP_PH50_CHECK.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
        }
    }

    private boolean check_last_router() {
        if (!sJson.equals("[]")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                if (ja.length() > 0) {
                    //for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(0);

                    String RTNMSG = jObject.getString("RTNMSG"); //제조오더

                    //if(RTNMSG.equals("전 공정 실적이 등록 되어있지 않습니다.")) {

                    Msg = RTNMSG;

                    return true;
                }
                return true;
            } catch (JSONException ex) {
                //Msg = ex.getMessage();
                return false;
            } catch (Exception e1) {
                //Msg = e1.getMessage();
                return false;
            }
        } else {
            Msg = "전 공정 실적이 등록 되어있지 않습니다.";
        }
        return true;
    }

    private void dbQuery_chk_outsourcing(final String prodt_order_no, final String opr_no) {
        Thread wkThd_dbQuery_chk_outsourcing = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_MES_PRODT_ORDER_DETAIL_INFO_GET_ANDROID ";
                sql += "  @PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@PRODT_ORDER_NO = '" + prodt_order_no + "'";
                sql += " ,@OPR_NO = '" + opr_no + "'";
                sql += " ,@INSIDE_FLG = 'N'";  //외주공정만 조회

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_check = dba.SendHttpMessage("GetSQLData", pParms);

                check_outsourcing(prodt_order_no, opr_no); //외주공정 체크
            }
        };
        wkThd_dbQuery_chk_outsourcing.start();   //스레드 시작
        try {
            wkThd_dbQuery_chk_outsourcing.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
        }
    }
    /*-------------------------------------------------------------------*/


    /*-------------------------------------------------------------------*/
    private boolean check_outsourcing(final String prodt_order_no, final String opr_no) {
        try {
            JSONArray ja = new JSONArray(sJson_check);

            if (ja.length() == 1) {
                return true;
            } else if (ja.length() > 1) {
                Msg = "작업장 또는 공정 정보가 없습니다.";

                return true;
            } else if (ja.length() == 0) {
                Msg = prodt_order_no + " 제조오더의 " + opr_no + " 공정은 \n 외주 공정이 아닙니다.";

                return true;
            }

        } catch (JSONException ex) {
            Msg = "DB 오류";
        } catch (Exception e1) {
            Msg = "DB 오류";
        }
        return true;
    }
    /*-------------------------------------------------------------------*/
}
