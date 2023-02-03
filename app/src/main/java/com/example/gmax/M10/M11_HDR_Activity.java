package com.example.gmax.M10;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.gmax.BaseActivity;
import com.example.gmax.DBAccess;
import com.example.gmax.R;
import com.example.gmax.ScanData;
import com.example.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class M11_HDR_Activity extends BaseActivity {

    //== 상수 선언 ==//
    private final long INTERVAL_TIME = 500;  //간격
    private long inputEnterPressedTime = 0;  //엔터키 입력 시간

    //== JSON 선언 ==//
    private String sJson;

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== 화면 타이틀 ==//
    private TextView lbl_view_title;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(EditText) ==//
    private EditText txt_Scan;

    //== 변수 선언 ==//
    private String Q_STS = "", M_STS = "";

    //== ActivityForResult 관련 변수 선언 ==//
    private final int M11_DTL_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.initializeView();

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

        //== ID 값 바인딩 ==//
        lbl_view_title  = (TextView) findViewById(R.id.view_title);
        txt_Scan        = (EditText) findViewById(R.id.txt_Scan);
        img_barcode     = (ImageView) findViewById(R.id.img_barcode);
    }

    private void initializeListener() {
        txt_Scan.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String txt_Scan_st = txt_Scan.getText().toString();
                    if (txt_Scan_st.equals("")) {
                        TGSClass.AlertMessage(getApplicationContext(), "검사요청번호를 입력하여 주시기 바랍니다.");
                        return false;
                    } else {
                        if (setScanData(vStartCommand)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
                return false;
            }
        });

        txt_Scan.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("x5", s.toString());
                if (!s.equals("")) {
                    //do your work here
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (txt_Scan.getText().toString().length() >= 20) {
                    if (!setScanData(vStartCommand)) return;
                }
            }
        });

        img_barcode.setOnClickListener(qrClickListener);
    }

    private void initializeData() {
        lbl_view_title.setText(vMenuNm);
    }

    private void changeView(String pINSP_REQ_NO) {
        Intent intent = TGSClass.ChangeView(getPackageName(), M11_DTL_Activity.class);
        intent.putExtra("INSP_REQ_NO", pINSP_REQ_NO);
        intent.putExtra("MENU_REMARK", vMenuRemark);
        intent.putExtra("START_COMMAND", vStartCommand);
        startActivityForResult(intent, M11_DTL_REQUEST_CODE);
    }

    //스캔 데이터 값 처리.
    private boolean setScanData(String pJobCD) {
        String vScanText = txt_Scan.getText().toString();

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

        String vINSP_REQ_NO = scan.getm_INSP_REQ_No();

        dbQuery_get_INSP_REQ_NO_INFO(vINSP_REQ_NO);       //검사요청번호

        if (sJson.equals("[]")) {
            TGSClass.AlertMessage(getApplicationContext(), "검색 된 검사요청번호 결과가 없습니다.");
            return false;
        }

        if (Q_STS.equals("N")) {
            TGSClass.AlertMessage(getApplicationContext(), "검사결과 등록 후 입고처리해주세요.");
            return false;
        } else if (Q_STS.equals("D")) {
            TGSClass.AlertMessage(getApplicationContext(), "검사결과 확정처리 후 입고처리해주세요.");
            sJson = "[]";
            return false;
        }

        if (M_STS.equals("IT")) {
            TGSClass.AlertMessage(getApplicationContext(), "입고완료된 자료가 선택되었습니다.");
            return false;
        }

        txt_Scan.setText(null);

        changeView(vINSP_REQ_NO);

        return true;
    }

    private void dbQuery_get_INSP_REQ_NO_INFO(final String pINSP_REQ_NO) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workQueryInfoThread = new Thread() {
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
        workQueryInfoThread.start();   //스레드 시작
        try {
            workQueryInfoThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

            start();
        } catch (InterruptedException ex) {

        }

        //Start();
    }

    private void start() {
        if (!sJson.equals("[]")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                JSONObject jObject = ja.getJSONObject(0);

                Q_STS = jObject.getString("Q_STS");
                M_STS = jObject.getString("M_STS");

            } catch (JSONException ex) {
                TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
            } catch (Exception e1) {
                TGSClass.AlertMessage(getApplicationContext(), "catch : e1");
            }
        }
    }

    /*
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

                txt_Scan.setText(result.getContents());
                setScanData(vJobCd);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String M_STS = data.getStringExtra("M_STS");
            switch (requestCode) {
                case M11_DTL_REQUEST_CODE:
                    //Log.d("OK", "M11_DTL");
                    //Log.w("M_STS", M_STS);
                    //finish();
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case M11_DTL_REQUEST_CODE:
                    //Log.d("CANCELED", "M11_DTL");
                    break;
                default:
                    break;
            }
        }
    }
}
