package com.PDA.Hanmi.I30;

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

public class I30_Activity extends BaseActivity {

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(Button) ==//
//    private Button btn_prod_release_registration, btn_out_of_plan, btn_reservation_status, btn_prod_release_registration_location;
//    private Button btn_prod_shipment_status, btn_prod_location_move, btn_prod_location_move_query, btn_prod_location_goods_status;
//    private Button btn_menu;
    MaterialToolbar materialToolbar;
    MaterialButton btn_prod_release_registration, btn_out_of_plan, btn_reservation_status, btn_prod_release_registration_location;
    MaterialButton btn_prod_shipment_status, btn_prod_location_move, btn_prod_location_move_query, btn_prod_location_goods_status;
//    ExtendedFloatingActionButton btn_menu;

    //== Grant 관련 변수 ==//
    private String ADMIN_CHK = "N", W_MOVE = "N", I31 = "N", I32 = "N", I33 = "N";
    private String I34 = "N", I35 = "N", I36 = "N", I37 = "N", I38 = "N";     //Grant

    //== ActivityForResult 관련 변수 ==//
    private final int I31_HDR_REQUEST_CODE = 1, I32_HDR_REQUEST_CODE = 2, I33_QUERY_REQUEST_CODE = 3, I34_QUERY_REQUEST_CODE = 4;
    private final int I35_HDR_REQUEST_CODE = 5, I36_QUERY_REQUEST_CODE = 6, I37_HDR_REQUEST_CODE = 7, I38_HDR_REQUEST_CODE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i30);

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
        materialToolbar                         = findViewById(R.id.materialToolbar);

        btn_prod_release_registration           = findViewById(R.id.btn_prod_release_registration);             // 1. 생산출고 등록
        btn_out_of_plan                         = findViewById(R.id.btn_out_of_plan);                           // 2. 계획 외 출고양산용)
        btn_reservation_status                  = findViewById(R.id.btn_reservation_status);                    // 3. 자재예약현황
        btn_prod_shipment_status                = findViewById(R.id.btn_prod_shipment_status);                  // 4. 생산출고 현황조회
        btn_prod_location_move                  = findViewById(R.id.btn_prod_location_move);                    // 5. 생산출고 대기장이동(적치장이동)
        btn_prod_location_move_query            = findViewById(R.id.btn_prod_location_move_query);              // 6. 생산출고 대기장 이동 현황 조회
        btn_prod_release_registration_location  = findViewById(R.id.btn_prod_release_registration_location);    // 7. 적치장 재고 생산출고등록(생산출고등록)
        btn_prod_location_goods_status          = findViewById(R.id.btn_prod_location_goods_status);            // 8. 생산출고 대기장 재고 이동 현황 조회
//        btn_menu                                = findViewById(R.id.btn_menu);                                  // 메뉴
    }

    private void initializeListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_prod_release_registration) {            // 1. 생산출고등록 버튼
                    if(start_grant("I31") == true) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I31_HDR_Activity.class);
                        intent.putExtra("MENU_ID", "I31");
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I31_HDR_REQUEST_CODE);
                    }
                } else if (v == btn_out_of_plan) {                          // 2. 계획외출고(양산)
                    /**
                    if(start_grant("I32") == true) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I32_HDR_Activity.class);
                        intent.putExtra("MENU_ID", "I32");
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I32_HDR_REQUEST_CODE);
                    }
                     */
                } else if (v == btn_reservation_status) {                   // 3. 자재예약현황
                    /**
                    if(start_grant("I33") == true) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I33_HDR_Activity.class);
                        intent.putExtra("MENU_ID", "I33");
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I33_HDR_REQUEST_CODE);
                    }
                     */
                } else if (v == btn_prod_shipment_status) {                 // 4. 생산출고 현황조회
                    if(start_grant("I34") == true) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I34_QUERY_Activity.class);
                        intent.putExtra("MENU_ID", "I34");
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I34_QUERY_REQUEST_CODE);
                    }
                } else if (v == btn_prod_location_move) {                   // 5. 생산출고 대기장이동(적치장이동)
                    if(start_grant("I35") == true) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I35_HDR_Activity.class);
                        intent.putExtra("MENU_ID", "I35");
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I35_HDR_REQUEST_CODE);
                    }
                } else if (v == btn_prod_location_move_query) {             // 6. 생산출고 대기장 이동 현황 조회
                    if(start_grant("I36") == true) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I36_QUERY_Activity.class);
                        intent.putExtra("MENU_ID", "I36");
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I36_QUERY_REQUEST_CODE);
                    }
                } else if (v == btn_prod_release_registration_location) {   // 7. 적치장 재고 생산출고등록(생산출고등록)
                    if(start_grant("I37") == true) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I37_HDR_Activity.class);
                        intent.putExtra("MENU_ID", "I37");
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I37_HDR_REQUEST_CODE);
                    }
                } else if (v == btn_prod_location_goods_status) {           // 8. 생산출고 대기장 재고 이동 현황 조회
                    if(start_grant("I38") == true) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), I38_HDR_Activity.class);
                        intent.putExtra("MENU_ID", "I38");
                        intent.putExtra("MENU_REMARK", vMenuRemark);
                        intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, I38_HDR_REQUEST_CODE);
                    }
                }
                /*
                if (v == btn_menu) {        // 메뉴 버튼
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
        btn_prod_release_registration.setOnClickListener(clickListener);            // 1. 생산출고 등록
        btn_out_of_plan.setOnClickListener(clickListener);                          // 2. 계획 외 출고양산용)
        btn_reservation_status.setOnClickListener(clickListener);                   // 3. 자재예약현황
        btn_prod_shipment_status.setOnClickListener(clickListener);                 // 4. 생산출고 현황조회
        btn_prod_location_move.setOnClickListener(clickListener);                   // 5. 생산출고 대기장이동(적치장이동)
        btn_prod_location_move_query.setOnClickListener(clickListener);             // 6. 생산출고 대기장 이동 현황 조회
        btn_prod_release_registration_location.setOnClickListener(clickListener);   // 7. 적치장 재고 생산출고등록(생산출고등록)
        btn_prod_location_goods_status.setOnClickListener(clickListener);           // 8. 생산출고 대기장 재고 이동 현황 조회
//        btn_menu.setOnClickListener(clickListener);

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
                I31 = LEVEL_CD.equals("I31") || I31.equals("Y") ? "Y" : "N";
                I32 = LEVEL_CD.equals("I32") || I32.equals("Y") ? "Y" : "N";
                I33 = LEVEL_CD.equals("I33") || I33.equals("Y") ? "Y" : "N";
                I34 = LEVEL_CD.equals("I34") || I34.equals("Y") ? "Y" : "N";
                I35 = LEVEL_CD.equals("I35") || I35.equals("Y") ? "Y" : "N";
                I36 = LEVEL_CD.equals("I36") || I36.equals("Y") ? "Y" : "N";
                I37 = LEVEL_CD.equals("I37") || I37.equals("Y") ? "Y" : "N";
                I38 = LEVEL_CD.equals("I38") || I38.equals("Y") ? "Y" : "N";
            }

            if (ADMIN_CHK.equals("N") && W_MOVE.equals("N")) {
                if (MenuID.equals("I31") && I31.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "생산출고 등록 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I34") && I34.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "생산 출고현황 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I35") && I35.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "생산출고 대기장 이동(적치장이동) 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I36") && I36.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "생산출고대기장 이동내역조회 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I37") && I37.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "적치장 재고 생산출고등록(생산출고등록) 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I38") && I38.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "생산출고대기장 재고현황조회 메뉴에 대한 접속 권한이 없습니다.");
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
                case I31_HDR_REQUEST_CODE:
                    //Log.d("OK", "I31_HDR");
                    break;
                case I32_HDR_REQUEST_CODE:
                    //Log.d("OK", "I32_HDR");
                    break;
                case I33_QUERY_REQUEST_CODE:
                    //Log.d("OK", "I33_QUERY");
                    break;
                case I34_QUERY_REQUEST_CODE:
                    //Log.d("OK", "I34_QUERY");
                    break;
                case I35_HDR_REQUEST_CODE:
                    //Log.d("OK", "I35_HDR");
                    break;
                case I36_QUERY_REQUEST_CODE:
                    //Log.d("OK", "I36_QUERY");
                    break;
                case I37_HDR_REQUEST_CODE:
                    //Log.d("OK", "I37_HDR");
                    break;
                case I38_HDR_REQUEST_CODE:
                    //Log.d("OK", "I38_HDR");
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case I31_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I31_HDR");
                    break;
                case I32_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I32_HDR");
                    break;
                case I33_QUERY_REQUEST_CODE:
                    //Log.d("CANCELED", "I33_QUERY");
                    break;
                case I34_QUERY_REQUEST_CODE:
                    //Log.d("CANCELED", "I34_QUERY");
                    break;
                case I35_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I35_HDR");
                    break;
                case I36_QUERY_REQUEST_CODE:
                    //Log.d("CANCELED", "I36_QUERY");
                    break;
                case I37_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I37_HDR");
                    break;
                case I38_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I38_HDR");
                    break;
                default:
                    break;
            }
        }
    }
}
