package com.example.gmax.I20;

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

public class I20_Activity extends BaseActivity {

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(Button) ==//
//    private Button btn_stockyard, btn_warehouse, btn_tracking, btn_workplace;
//    private Button btn_query, btn_move, btn_item_qty_move, btn_menu;
    MaterialToolbar materialToolbar;
    MaterialButton btn_stockyard, btn_warehouse, btn_tracking, btn_workplace;
    MaterialButton btn_query, btn_move, btn_item_qty_move;
//    ExtendedFloatingActionButton btn_menu;

    //== Grant 관련 변수 ==//
    private String ADMIN_CHK = "N", W_MOVE = "N", I21 = "N", I22 = "N", I23 = "N", I24 = "N", I25 = "N", I26 = "N", I27 = "N";     //Grant

    //== Request Code 상수 선언 ==//
    private final int I21_HDR_REQUEST_CODE = 1, I22_HDR_REQUEST_CODE = 2, I23_HDR_REQUEST_CODE = 3;
    private final int I24_HDR_REQUEST_CODE = 4, I25_QUERY_REQUEST_CODE = 5, I26_HDR_REQUEST_CODE = 6, I27_HDR_REQUEST_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i20);

        this.initializeView();

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

        //== ID값 바인딩 ==//
        materialToolbar     = findViewById(R.id.materialToolbar);

        btn_stockyard       = findViewById(R.id.btn_stockyard);         // 1. 적치장이동
        btn_warehouse       = findViewById(R.id.btn_warehouse);         // 2. 창고이동
        btn_tracking        = findViewById(R.id.btn_tracking);          // 3. TRACKING이동
        btn_workplace       = findViewById(R.id.btn_workplace);         // 4. 사업장이동
        btn_query           = findViewById(R.id.btn_query);             // 5. 재고이동현황조회
        btn_move            = findViewById(R.id.btn_move);              // 6. 출고이동처리
        btn_item_qty_move   = findViewById(R.id.btn_item_qty_move);     // 7. 품목재고이동(함안)
//        btn_menu            = findViewById(R.id.btn_menu);              // 메뉴버튼
    }

    private void initializeListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_stockyard) {    // 1. 적치장이동
                    if (start_grant("I21")) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I21_HDR_Activity.class);
                        intent.putExtra("MENU_ID", "I21");
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I21_HDR_REQUEST_CODE);
                    }
                } else if (v == btn_warehouse) {    // 2. 창고이동
                    if (start_grant("I22")) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I22_HDR_Activity.class);
                        intent.putExtra("MENU_ID", "I22");
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I22_HDR_REQUEST_CODE);
                    }
                } else if (v == btn_tracking) {     // 3. TRACKING이동
                    /**
                    if (start_grant("I23")) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I23_HDR_Activity.class);
                        intent.putExtra("MENU_ID", "I23");
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I23_HDR_REQUEST_CODE);
                    }
                     */
                } else if (v == btn_workplace) {    // 4. 사업장이동
                    /**
                    if (start_grant("I24")) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I24_HDR_Activity.class);
                        intent.putExtra("MENU_ID", "I24");
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I24_HDR_REQUEST_CODE);
                    }
                     */
                } else if (v == btn_query) {        // 5. 재고이동현황조회
                    if (start_grant("I25")) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I25_QUERY_Activity.class);
                        intent.putExtra("MENU_ID", "I25");
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I25_QUERY_REQUEST_CODE);
                    }
                } else if (v == btn_move) {         // 6. 출고이동처리(창원)
                    if (start_grant("I26")) {
                        String sMenuName = "출고이동요청서 조회";
                        Intent intent = TGSClass.ChangeView(getPackageName(), I26_HDR_Activity.class);
                        intent.putExtra("MENU_ID", "I26");
                        intent.putExtra("MENU_NM", sMenuName);
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I26_HDR_REQUEST_CODE);
                    }
                } else if (v == btn_item_qty_move) {        // 7. 품목재고이동(함안)
                    if (start_grant("I27")) {
                        String sMenuName = "재고이동 > 품목재고이동\n\n품번 입력";
                        Intent intent = TGSClass.ChangeView(getPackageName(), I27_HDR_Activity.class);
                        intent.putExtra("MENU_ID", "I27");
                        intent.putExtra("MENU_NM", sMenuName);
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I27_HDR_REQUEST_CODE);
                    }
                }
                /*
                if (v == btn_menu) {           // 메뉴
                    // 저장 후 결과 값 돌려주기
                    Intent resultIntent = new Intent();
                    // 부른 Activity에게 결과 값 반환
                    setResult(RESULT_CANCELED, resultIntent);
                    // 현재 Activity 종료
                    finish();
                }
                */
            }
        };
        btn_stockyard.setOnClickListener(clickListener);        // 1. 적치장이동
        btn_warehouse.setOnClickListener(clickListener);        // 2. 창고이동
        btn_tracking.setOnClickListener(clickListener);         // 3. TRACKING이동
        btn_workplace.setOnClickListener(clickListener);        // 4. 사업장이동
        btn_query.setOnClickListener(clickListener);            // 5. 재고이동현황조회
        btn_move.setOnClickListener(clickListener);             // 6. 출고이동처리
        btn_item_qty_move.setOnClickListener(clickListener);    // 7. 품목재고이동(함안)
//        btn_menu.setOnClickListener(clickListener);             // 메뉴버튼

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
                W_MOVE = LEVEL_CD.equals("W_MOVE") || W_MOVE.equals("Y") ? "Y" : "N";
                I21 = LEVEL_CD.equals("I21") || I21.equals("Y") ? "Y" : "N";
                I22 = LEVEL_CD.equals("I22") || I22.equals("Y") ? "Y" : "N";
                I23 = LEVEL_CD.equals("I23") || I23.equals("Y") ? "Y" : "N";
                I24 = LEVEL_CD.equals("I24") || I24.equals("Y") ? "Y" : "N";
                I25 = LEVEL_CD.equals("I25") || I25.equals("Y") ? "Y" : "N";
                I26 = LEVEL_CD.equals("I26") || I26.equals("Y") ? "Y" : "N";
                I27 = LEVEL_CD.equals("I27") || I27.equals("Y") ? "Y" : "N";
            }

            if (ADMIN_CHK.equals("N") && W_MOVE.equals("N")) {
                if (MenuID.equals("I21") && I21.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "적치장이동 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I22") && I22.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "창고이동 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I23") && I23.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "Tracking이동 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I24") && I24.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "사업장이동 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I25") && I25.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "재고이동현황조회 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I26") && I26.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "출고이동처리 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I27") && I27.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "품목재고이동 메뉴에 대한 접속 권한이 없습니다.");
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
                case I21_HDR_REQUEST_CODE:
                    //Log.d("OK", "I21_HDR");
                    break;
                case I22_HDR_REQUEST_CODE:
                    //Log.d("OK", "I22_HDR");
                    break;
                case I23_HDR_REQUEST_CODE:
                    //Log.d("OK", "I23_HDR");
                    break;
                case I24_HDR_REQUEST_CODE:
                    //Log.d("OK", "I24_HDR");
                    break;
                case I25_QUERY_REQUEST_CODE:
                    //Log.d("OK", "I25_QUERY");
                    break;
                case I26_HDR_REQUEST_CODE:
                    //Log.d("OK", "I26_HDR");
                    break;
                case I27_HDR_REQUEST_CODE:
                    //Log.d("OK", "I27_HDR");
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case I21_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I21_HDR");
                    break;
                case I22_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I22_HDR");
                    break;
                case I23_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I23_HDR");
                    break;
                case I24_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I24_HDR");
                    break;
                case I25_QUERY_REQUEST_CODE:
                    //Log.d("CANCELED", "I25_QUERY");
                    break;
                case I26_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I26_HDR");
                    break;
                case I27_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I27_HDR");
                    break;
                default:
                    break;
            }
        }
    }
}
