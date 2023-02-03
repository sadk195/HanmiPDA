package com.example.gmax.P10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.gmax.BaseActivity;
import com.example.gmax.R;
import com.example.gmax.TGSClass;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class P10_Activity extends BaseActivity {

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(버튼) ==//
//    private Button btn_c1_traveler_register, btn_c1_traveler_query, btn_h1_traveler_register, btn_h1_outorder_register;
//    private Button btn_menu;
    MaterialToolbar materialToolbar;
    MaterialButton btn_c1_traveler_register, btn_c1_traveler_query, btn_h1_traveler_register, btn_h1_outorder_register;
//    ExtendedFloatingActionButton btn_menu;

    //== Grant 관련 변수 ==//
    private String ADMIN_CHK = "N", P14 = "N";     //Grant

    //== Request Code 상수 선언 ==//
    private final int P14_HDR_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p10);

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
        vStartCommand                   = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== ID값 바인딩 ==//
        materialToolbar                 = findViewById(R.id.materialToolbar);

        btn_c1_traveler_register        = findViewById(R.id.btn_c1_traveler_register);         // 1. TRAVELER실적등록
        btn_c1_traveler_query           = findViewById(R.id.btn_c1_traveler_query);            // 2. TRAVELER실적조회
        btn_h1_traveler_register        = findViewById(R.id.btn_h1_traveler_register);         // 3. (함안)TRAVELER실적등록
        btn_h1_outorder_register        = findViewById(R.id.btn_h1_outorder_register);         // 4. (함안)공정외주실적등록
//        btn_menu                        = findViewById(R.id.btn_menu);                         // 메뉴버튼
    }

    private void initializeListener() {
        //== Click 이벤트 만들기 ==//
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_c1_traveler_register:     // 1. TRAVELER실적등록
                    case R.id.btn_c1_traveler_query:        // 2. TRAVELER실적조회
                    case R.id.btn_h1_traveler_register:     // 3. (함안)TRAVELER실적등록
                        break;
                    case R.id.btn_h1_outorder_register:     // 4. (함안)공정외주실적등록
                        if (vPLANT_CD.equals("C1")) {
                            TGSClass.AlertMessage(getApplicationContext(), "(" + vPLANT_CD + ")" + "창원의 공장코드를 가진 직원은 접속하실 수 없습니다.");
                        } else if (vPLANT_CD.equals("H1")) {    // 함안 공장코드만 접속 가능
                            if (start_grant("P14")) {
                                Intent intent = TGSClass.ChangeView(getPackageName(), P14_QUERY_Activity.class);
                                intent.putExtra("MENU_ID", "P14");
                                intent.putExtra("MENU_REMARK", vMenuRemark);
                                intent.putExtra("START_COMMAND", vStartCommand);
                                startActivityForResult(intent, P14_HDR_REQUEST_CODE);
                            }
                        }
                        break;
                    /*
                    case R.id.btn_menu:     // 메뉴
                        // 저장 후 결과 값 돌려주기
                        Intent resultIntent = new Intent();
                        // 부른 Activity에게 결과 값 반환
                        setResult(RESULT_CANCELED, resultIntent);
                        // 현재 Activity 종료
                        break;
                    */
                    default:
                        break;
                }
            }
        };
        //== 이벤트 부여 ==//
        btn_c1_traveler_register.setOnClickListener(clickListener);     // 1. TRAVELER실적등록
        btn_c1_traveler_query.setOnClickListener(clickListener);        // 2. TRAVELER실적조회
        btn_h1_traveler_register.setOnClickListener(clickListener);     // 3. (함안)TRAVELER실적등록
        btn_h1_outorder_register.setOnClickListener(clickListener);     // 4. (함안)공정외주실적등록
//        btn_menu.setOnClickListener(clickListener);                     // 메뉴버튼

        //== 이벤트 부여 ==//
        //== AppBar Navigation Icon Click Listener ==//
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //== 결과 값 돌려주기 ==//
                Intent resultIntent = new Intent();
                //== 부른 Activity에게 결과 값 반환 ==//
                setResult(RESULT_CANCELED, resultIntent);
                // 현재 Activity 종료
                finish();
            }
        });
    }

    private void initializeData() {
        getGrant(vUSER_ID);
    }

    private boolean start_grant(final String MenuID) {
        try {
            JSONArray ja = new JSONArray(sJson_grant);

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                final String LEVEL_CD = jObject.getString("LEVEL_CD");

                ADMIN_CHK = LEVEL_CD.equals("ADMIN") || ADMIN_CHK.equals("Y") ? "Y" : "N";
                P14 = LEVEL_CD.equals("P14") || P14.equals("Y") ? "Y" : "N";
            }

            if (ADMIN_CHK.equals("N")) {
                if (MenuID.equals("P14") && P14.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "(함안) 공정외주실적등록 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                }
            }
            return true;
        } catch (JSONException exJson) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : exJson");
            return false;
        } catch (Exception ex) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case P14_HDR_REQUEST_CODE:
                    //Log.d("OK", "P14_HDR");
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case P14_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "P14_HDR");
                    break;
                default:
                    break;
            }
        }
    }
}