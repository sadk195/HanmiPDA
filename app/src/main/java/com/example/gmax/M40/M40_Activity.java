package com.example.gmax.M40;

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

public class M40_Activity extends BaseActivity {

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(Button) ==//
//    private Button btn_save, btn_query, btn_menu;
    MaterialToolbar materialToolbar;
    MaterialButton btn_m41, btn_m42, btn_m43,btn_m44;
//    ExtendedFloatingActionButton btn_menu;

    //== Grant 관련 변수 ==//
    private String ADMIN_CHK = "N", W_IN = "N", M41 = "N", M42 = "N", M43 = "N",M44 = "N";     //Grant

    //== ActivityForResult 관련 변수 ==//
    private final int M41_DTL_REQUEST_CODE = 1, M42_DTL_REQUEST_CODE = 2
            , M43_QUERY_REQUEST_CODE = 3,M44_QUERY_REQUEST_CODE=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m40);

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

        //== ID값 바인딩 ==//
        materialToolbar = findViewById(R.id.materialToolbar);

        btn_m41        = findViewById(R.id.btn_Fabric_Receiving);           // 1. 리즐링 원단 입고
        btn_m42        = findViewById(R.id.btn_Fabric_Input);               // 2. 리즐링 원단 투입
        btn_m43        = findViewById(R.id.btn_Fabric_Inventory_status);    // 3. 원단 재고 현황
        btn_m44        = findViewById(R.id.btn_Fabric_Success);             // 4. 리즐링 생산 완료

//        btn_menu        = findViewById(R.id.btn_menu);                             // 메뉴
    }

    private void initializeListener() {
        //== Click 이벤트 만들기 ==//
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_m41) {     // 리즐링 원단 입고
                    if (start_grant("M41")) {
                        String sMenuName = "메뉴 > 리즐링 원단 관리 > 리즐링 원단 입고";

                        Intent intent = TGSClass.ChangeView(getPackageName(), M41_DTL_Activity.class);
                        intent.putExtra("MENU_ID", "M41");
                        intent.putExtra("MENU_NM", sMenuName);
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, M41_DTL_REQUEST_CODE);
                    }
                } else if (v == btn_m42) {    // 리즐링 원단 투입
                    if (start_grant("M42")) {
                        String sMenuName = "메뉴 > 리즐링 원단 관리 > 리즐링 원단 투입";

                        Intent intent = TGSClass.ChangeView(getPackageName(), M42_DTL_Activity.class);
                        intent.putExtra("MENU_ID", "M42");
                        intent.putExtra("MENU_NM", sMenuName);
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, M42_DTL_REQUEST_CODE);
                    }
                }
                else if (v == btn_m43) {    // 원단 재고 현황
                    if (start_grant("M43")) {
                        String sMenuName = "메뉴 > 리즐링 원단 관리 > 원단 재고 현황";

                        Intent intent = TGSClass.ChangeView(getPackageName(), M43_QUERY_Activity.class);
                        intent.putExtra("MENU_ID", "M43");
                        intent.putExtra("MENU_NM", sMenuName);
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, M43_QUERY_REQUEST_CODE);
                    }
                }
                else if (v == btn_m44) {    // 원단 재고 현황
                    if (start_grant("M44")) {
                        String sMenuName = "메뉴 > 리즐링 원단 관리 > 리즐링 생산 완료";

                        Intent intent = TGSClass.ChangeView(getPackageName(), M44_DTL_Activity.class);
                        intent.putExtra("MENU_ID", "M44");
                        intent.putExtra("MENU_NM", sMenuName);
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, M44_QUERY_REQUEST_CODE);
                    }
                }

            }
        };
        btn_m41.setOnClickListener(clickListener);
        btn_m42.setOnClickListener(clickListener);
        btn_m43.setOnClickListener(clickListener);
        btn_m44.setOnClickListener(clickListener);

//        btn_menu.setOnClickListener(clickListener);

        //== 이벤트 부여 ==//
        //== AppBar Navigation Icon Click Listener ==//
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContentDescription().equals(materialToolbar.getNavigationContentDescription())) {
                    // 저장 후 결과 값 돌려주기
                    Intent resultIntent = new Intent();
                    // 부른 Activity에게 결과 값 반환
                    setResult(RESULT_CANCELED, resultIntent);
                    // 현재 Activity 종료
                    finish();
                }
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
                W_IN = LEVEL_CD.equals("W_IN") || W_IN.equals("Y") ? "Y" : "N";
                M41 = LEVEL_CD.equals("M41") || M41.equals("Y") ? "Y" : "N";
                M42 = LEVEL_CD.equals("M42") || M42.equals("Y") ? "Y" : "N";
                M43 = LEVEL_CD.equals("M43") || M43.equals("Y") ? "Y" : "N";
                M44 = LEVEL_CD.equals("M44") || M44.equals("Y") ? "Y" : "N";

            }

            if (ADMIN_CHK.equals("N") && W_IN.equals("N")) {
                if (MenuID.equals("M41") && M41.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "리즐링 원단 입고 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("M42") && M42.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "리즐링 원단 재고 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("M43") && M43.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "원단 재고 현황 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("M44") && M44.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "리즐링 생산 완료 메뉴에 대한 접속 권한이 없습니다.");
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
                case M41_DTL_REQUEST_CODE:
                    //Log.d("OK", "M11_HDR");
                    break;
                case M42_DTL_REQUEST_CODE:
                    //Log.d("OK", "M12_QUERY");
                    break;
                case M43_QUERY_REQUEST_CODE:
                    //Log.d("OK", "M12_QUERY");
                    break;
                case M44_QUERY_REQUEST_CODE:
                    //Log.d("OK", "M12_QUERY");
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case M41_DTL_REQUEST_CODE:
                    //Log.d("CANCELED", "M11_HDR");
                    break;
                case M42_DTL_REQUEST_CODE:
                    //Log.d("CANCELED", "M12_QUERY");
                    break;
                case M43_QUERY_REQUEST_CODE:
                    //Log.d("CANCELED", "M12_QUERY");
                    break;
                case M44_QUERY_REQUEST_CODE:
                    //Log.d("CANCELED", "M12_QUERY");
                    break;

                default:
                    break;
            }
        }
    }
}
