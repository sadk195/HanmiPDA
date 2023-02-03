package com.example.gmax.I20;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class I27_HDR_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJsonHDR = "", sJsonList = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(EditText) ==//
    private EditText txt_Scan;

    //== View 선언(TextView) ==//
    private TextView view_title;

    //== 전역 변수 선언 ==//
    private String str_item_cd;

    //== Dialog ==//
    private DBQueryHDR queryHdr;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int I27_DTL_REQUEST_CODE = 0;

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
        vStartCommand   = getIntent().getStringExtra("START_COMMAND");

        //== ID 값 바인딩 ==//
        view_title      = (TextView) findViewById(R.id.view_title);

        img_barcode     = (ImageView) findViewById(R.id.img_barcode);
        txt_Scan        = (EditText) findViewById(R.id.txt_Scan);
    }

    private void initializeListener() {
        //== 품목 이벤트 ==//
        txt_Scan.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && i == KeyEvent.KEYCODE_ENTER) {
                    String item_cd = txt_Scan.getText().toString();
                    if (item_cd.equals("")) {
                        TGSClass.AlertMessage(getApplicationContext(), "품번을 입력해주세요.");
                        return false;
                    } else {
                        str_item_cd = TGSClass.transSemicolon(item_cd);
                        start();
                    }
                    return true;
                }
                return false;
            }
        });

        //== 바코드 이벤트 ==//
        img_barcode.setOnClickListener(qrClickListener);
    }

    private void initializeData() {
        //== 초기값 ==//

        //** HEADER LIST VIEW
        //== LayoutInflater 생성 ==//
//                                LayoutInflater layoutInflater = LayoutInflater.from(this);
//                                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //== LayoutInflater로부터 Header View 생성 ==//
//                                View header = layoutInflater.inflate(R.layout.header_i13_view, null);
        final View head = getLayoutInflater().inflate(R.layout.header_i13_view, null, false);
//        listview.addHeaderView(head);

        //** HEADER LIST VIEW
        view_title.setText(vMenuNm);
    }

    //== start ==//
    private void start() {
        dbQuery_HDR();
    }

    //== 조회 ==//
    private void dbQuery_HDR() {
        progressStart(this);
        queryHdr = new DBQueryHDR();
        queryHdr.start();
    }

    //== 조회 ==//
    public class DBQueryHDR extends Thread {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        @Override
        public void run() {
            try {
                String sql = " EXEC XUSP_APK_I27_GET_HDR ";
                sql += "  @PLANT_CD = 'H1' ";
                sql += "  ,@ITEM_CD = '" + str_item_cd + "' ";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                sJsonHDR = dba.SendHttpMessage("GetSQLData", pParms);
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
                    queryHdr.join();

                    txt_Scan.setText(null);

                    if (sJsonHDR.isEmpty() || sJsonHDR.equals("[]")) {
                        TGSClass.AlertMessage(getApplicationContext(), "존재하지 않는 품번입니다.", 1000);
                        progressEnd();
                        return;
                    } else {
                        try {
                            JSONArray ja = new JSONArray(sJsonHDR);

                            JSONObject jObject = ja.getJSONObject(0);

                            I27_HDR item = new I27_HDR();

                            item.setITEM_CD(jObject.getString("ITEM_CD"));
                            item.setITEM_NM(jObject.getString("ITEM_NM"));
                            item.setSPEC(jObject.getString("SPEC"));
                            item.setLOCATION(jObject.getString("LOCATION"));
                            item.setLOCATION_NM(jObject.getString("LOCATION_NM"));
                            item.setTRACKING_NO(jObject.getString("TRACKING_NO"));
                            item.setSUM_GOOD_ON_HAND_QTY(jObject.getInt("SUM_GOOD_ON_HAND_QTY"));

                            progressEnd();

                            Intent intent = TGSClass.ChangeView(getPackageName(), I27_DTL_Activity.class);
                            intent.putExtra("HDR", item);
                            startActivityForResult(intent, I27_DTL_REQUEST_CODE);
                        } catch (JSONException ex) {
                            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
                        } catch (Exception e1) {
                            TGSClass.AlertMessage(getApplicationContext(), e1.getMessage());
                        }
                    }

                    retry = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String SIGN = data.getStringExtra("SIGN");
            switch (requestCode) {
                case I27_DTL_REQUEST_CODE:
                    //Log.d("OK", "I27_DTL");
                    if (SIGN.equals("EXIT")) {
                        finish();
                    } else if (SIGN.equals("ADD")) {

                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case I27_DTL_REQUEST_CODE:
                    Log.d("CANCELED", "I27_DTL_취소");
                    break;
                default:
                    break;
            }
        }
    }
}
