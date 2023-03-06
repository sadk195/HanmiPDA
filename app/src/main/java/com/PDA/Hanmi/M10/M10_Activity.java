package com.PDA.Hanmi.M10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.PDA.Hanmi.BaseActivity;
import com.PDA.Hanmi.R;
import com.PDA.Hanmi.TGSClass;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class M10_Activity extends BaseActivity {

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(Button) ==//
//    private Button btn_save, btn_query, btn_menu;
    MaterialToolbar materialToolbar;
    MaterialButton btn_m11, btn_m12, btn_m13, btn_m14;
//    ExtendedFloatingActionButton btn_menu;

    //== Grant 관련 변수 ==//
    private String ADMIN_CHK = "N", W_IN = "N", M11 = "N", M12 = "N", M13 = "N", M14 = "N";     //Grant

    //== ActivityForResult 관련 변수 ==//
    private final int M11_HDR_REQUEST_CODE = 1, M12_QUERY_REQUEST_CODE = 2
            , M13_QUERY_REQUEST_CODE = 3, M14_QUERY_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m10);

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

        btn_m11        = findViewById(R.id.btn_Purchase_Warehousing_Out_save);    // 1. 검사품입고등록
        btn_m12       = findViewById(R.id.btn_Purchase_Warehousing_Out_query);   // 2. 검사품입고현황조회
        btn_m13        = findViewById(R.id.btn_Purchase_Warehousing_save);    // 3. 구매입고등록
        btn_m14       = findViewById(R.id.btn_Purchase_Warehousing_query);   // 4. 구매입고현황조회

//        btn_menu        = findViewById(R.id.btn_menu);                             // 메뉴
    }

    private void initializeListener() {
        //== Click 이벤트 만들기 ==//
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_m11) {     // 검사품입고등록
                    if (start_grant("M11")) {
                        String sMenuName = "메뉴 > 구매입고관리 > 검사품입고등록\n\n검사요청번호 입력";

                        Intent intent = TGSClass.ChangeView(getPackageName(), M11_DTL_Activity.class);
                        intent.putExtra("MENU_ID", "M11");
                        intent.putExtra("MENU_NM", sMenuName);
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, M11_HDR_REQUEST_CODE);
                    }
                } else if (v == btn_m12) {    // 검사품입고현황조회
                    if (start_grant("M12")) {
                        String sMenuName = "메뉴 > 구매입고관리 > 검사품입고현황조회";

                        Intent intent = TGSClass.ChangeView(getPackageName(), M12_QUERY_Activity.class);
                        intent.putExtra("MENU_ID", "M12");
                        intent.putExtra("MENU_NM", sMenuName);
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, M12_QUERY_REQUEST_CODE);
                    }
                }
                else if (v == btn_m13) {    // 구매입고현황조회
                    if (start_grant("M13")) {
                        String sMenuName = "메뉴 > 구매입고관리 > 구매입고현황조회";

                        Intent intent = TGSClass.ChangeView(getPackageName(), M13_DTL_Activity.class);
                        intent.putExtra("MENU_ID", "M13");
                        intent.putExtra("MENU_NM", sMenuName);
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, M13_QUERY_REQUEST_CODE);
                    }
                }
                else if (v == btn_m14) {    // 구매입고현황조회
                    if (start_grant("M14")) {
                        String sMenuName = "메뉴 > 구매입고관리 > 구매입고현황조회";

                        Intent intent = TGSClass.ChangeView(getPackageName(), M14_QUERY_Activity.class);
                        intent.putExtra("MENU_ID", "M14");
                        intent.putExtra("MENU_NM", sMenuName);
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, M14_QUERY_REQUEST_CODE);
                    }
                }
            }
        };
        btn_m11.setOnClickListener(clickListener);
        btn_m12.setOnClickListener(clickListener);
        btn_m13.setOnClickListener(clickListener);
        btn_m14.setOnClickListener(clickListener);

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
                M11 = LEVEL_CD.equals("M11") || M11.equals("Y") ? "Y" : "N";
                M12 = LEVEL_CD.equals("M12") || M12.equals("Y") ? "Y" : "N";
                M13 = LEVEL_CD.equals("M13") || M13.equals("Y") ? "Y" : "N";
                M14 = LEVEL_CD.equals("M14") || M14.equals("Y") ? "Y" : "N";

            }

            if (ADMIN_CHK.equals("N") && W_IN.equals("N")) {
                if (MenuID.equals("M11") && M11.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "검사품입고등록 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("M12") && M12.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "검사품입고조회 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                }else if (MenuID.equals("M13") && M13.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "구매입고등록 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                }else if (MenuID.equals("M14") && M14.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "구매입고조회 메뉴에 대한 접속 권한이 없습니다.");
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
                case M11_HDR_REQUEST_CODE:
                    //Log.d("OK", "M11_HDR");
                    break;
                case M12_QUERY_REQUEST_CODE:
                    //Log.d("OK", "M12_QUERY");
                    break;
                case M13_QUERY_REQUEST_CODE:
                    //Log.d("OK", "M12_QUERY");
                    break;
                case M14_QUERY_REQUEST_CODE:
                    //Log.d("OK", "M12_QUERY");
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case M11_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "M11_HDR");
                    break;
                case M12_QUERY_REQUEST_CODE:
                    //Log.d("CANCELED", "M12_QUERY");
                    break;
                case M13_QUERY_REQUEST_CODE:
                    //Log.d("CANCELED", "M12_QUERY");
                    break;
                case M14_QUERY_REQUEST_CODE:
                    //Log.d("CANCELED", "M12_QUERY");
                    break;
                default:
                    break;
            }
        }
    }
}
