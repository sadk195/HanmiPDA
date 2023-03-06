package com.PDA.Hanmi.S10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.PDA.Hanmi.BaseActivity;
import com.PDA.Hanmi.TGSClass;
import com.PDA.Hanmi.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class S10_Activity extends BaseActivity {

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== Button 선언 ==//
//    private Button btn_shipment_request_status, btn_shipment_registration, btn_shipment_status;
//    private Button btn_shipment_location_move, btn_shipment_location_move_query;
//    private Button btn_shipment_registration_location, btn_shipment_location_goods_status;
//    private Button btn_menu;
    MaterialToolbar materialToolbar;
    MaterialButton btn_shipment_request_status, btn_shipment_registration,btn_shipment_package
            , btn_shipment_status;
    MaterialButton btn_shipment_location_move, btn_shipment_location_move_query;
    MaterialButton btn_shipment_registration_location, btn_shipment_location_goods_status;
//    ExtendedFloatingActionButton btn_menu;

    //== Grant 관련 변수 선언 ==//
    private String ADMIN_CHK = "N";     //ADMIN
    private String S11 = "N", W_OUT = "N", S12 = "N", S13 = "N", S14 = "N", S15 = "N", S16 = "N", S17 = "N";

    //== ActivityForResult 관련 변수 ==//
    private final int S11_REQUEST_CODE = 1, S12_REQUEST_CODE = 2, S13_REQUEST_CODE = 3;
    private final int S14_REQUEST_CODE = 4, S15_REQUEST_CODE = 5, S16_REQUEST_CODE = 6, S17_REQUEST_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s10);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID                             = getIntent().getStringExtra("MENU_ID");
        vMenuNm                             = getIntent().getStringExtra("MENU_NM");
        vMenuRemark                         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand                       = getIntent().getStringExtra("START_COMMAND");      //다음 페이지에 가지고 넘어갈 코드

        //== ID 값 바인딩 ==//
        materialToolbar                     = findViewById(R.id.materialToolbar);

        btn_shipment_request_status         = findViewById(R.id.btn_shipment_request_status);           // 1. 출하요청현황
        btn_shipment_registration           = findViewById(R.id.btn_shipment_registration);             // 2. 출하등록
        btn_shipment_package                = findViewById(R.id.btn_shipment_package);                  // 2_1. 포장등록
        btn_shipment_status                 = findViewById(R.id.btn_shipment_status);                   // 3. 출하현황
        btn_shipment_location_move          = findViewById(R.id.btn_shipment_location_move);            // 4. 출하대기장 이동 등록(적치장 이동)
        btn_shipment_location_move_query    = findViewById(R.id.btn_shipment_location_move_query);      // 5. 출하대기장 이동현황조회
        btn_shipment_registration_location  = findViewById(R.id.btn_shipment_registration_location);    // 6. 적치장재고 출하등록(출하등록)
        btn_shipment_location_goods_status  = findViewById(R.id.btn_shipment_location_goods_status);    // 7. 출하 대기장 재고 현황 조회
//        btn_menu                            = findViewById(R.id.btn_menu);                            // 메뉴
    }

    private void initializeListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_shipment_request_status:          // 1. 출하요청현황

                        if (start_grant("S11")) {
                        Intent intent = TGSClass.ChangeView(getPackageName(), S11_QUERY_Activity.class);
                            intent.putExtra("MENU_ID", "S11");
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                        startActivityForResult(intent, S11_REQUEST_CODE);
                        }
                        break;
                    case R.id.btn_shipment_registration:            // 2. 출하등록
                        if (start_grant("S12")) {
                            Intent intent = TGSClass.ChangeView(getPackageName(), S12_HDR_Activity.class);
                            intent.putExtra("MENU_ID", "S12");
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            intent.putExtra("PACKAGE", false);
                            startActivityForResult(intent, S12_REQUEST_CODE);
                        }
                        break;
                    case R.id.btn_shipment_package:            // 2_1. 포장등록
                        if (start_grant("S12")) {
                            Intent intent = TGSClass.ChangeView(getPackageName(), S12_HDR_Activity.class);
                            intent.putExtra("MENU_ID", "S12");
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            intent.putExtra("PACKAGE", true);

                            startActivityForResult(intent, S12_REQUEST_CODE);
                        }
                        break;

                    case R.id.btn_shipment_status:                  // 3. 출하현황

                        if (start_grant("S13")) {
                            Intent intent = TGSClass.ChangeView(getPackageName(), S13_QUERY_Activity.class);
                            intent.putExtra("MENU_ID", "S13");
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, S13_REQUEST_CODE);
                        }

                        break;
                    case R.id.btn_shipment_location_move:           // 4. 출하대기장이동등록(적치장이동)
                        if (start_grant("S14")) {
                            getRequeryData grd = new getRequeryData();
                            grd.S14_HDR("");

                            Intent intent = TGSClass.ChangeView(getPackageName(), S14_HDR_Activity.class);
                            intent.putExtra("MENU_ID", "S14");
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, S14_REQUEST_CODE);
                        }
                        break;
                    case R.id.btn_shipment_location_move_query:     // 5. 출하대기장이동현황조회
                        if (start_grant("S15")) {
                            Intent intent = TGSClass.ChangeView(getPackageName(), S15_QUERY_Activity.class);
                            intent.putExtra("MENU_ID", "S15");
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, S15_REQUEST_CODE);
                        }
                        break;
                    case R.id.btn_shipment_registration_location:   // 6. 적치장재고출하등록(출하등록)
                        if (start_grant("S16")) {
                            Intent intent = TGSClass.ChangeView(getPackageName(), S16_HDR_Activity.class);
                            intent.putExtra("MENU_ID", "S16");
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, S16_REQUEST_CODE);
                        }
                        break;
                    case R.id.btn_shipment_location_goods_status:   // 7. 출하대기장재고현황조회
                        if (start_grant("S17")) {
                            Intent intent = TGSClass.ChangeView(getPackageName(), S17_HDR_Activity.class);
                            intent.putExtra("MENU_ID", "S17");
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, S17_REQUEST_CODE);
                        }
                        break;
                    /*
                    case R.id.btn_menu:                             // 메뉴버튼
                        //== 결과 값 돌려주기 ==//
                        Intent resultIntent = new Intent();
                        //== 부른 Activity에게 결과 값 반환 ==//
                        setResult(RESULT_CANCELED, resultIntent);
                        // 현재 Activity 종료
                        finish();
                        break;
                    */
                    default:
                        break;
                }
            }
        };
        btn_shipment_request_status.setOnClickListener(clickListener);          // 1. 출하요청현황
        btn_shipment_registration.setOnClickListener(clickListener);            // 2. 출하등록
        btn_shipment_package.setOnClickListener(clickListener);            // 2. 출하등록

        btn_shipment_status.setOnClickListener(clickListener);                  // 3. 출하현황
        btn_shipment_location_move.setOnClickListener(clickListener);           // 4. 출하대기장 이동 등록(적치장 이동)
        btn_shipment_location_move_query.setOnClickListener(clickListener);     // 5. 출하대기장 이동현황조회
        btn_shipment_registration_location.setOnClickListener(clickListener);   // 6. 적치장재고 출하등록(출하등록)
        btn_shipment_location_goods_status.setOnClickListener(clickListener);   // 7. 출하 대기장 재고 현황 조회
//        btn_menu.setOnClickListener(clickListener);                             // 메뉴

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

    //== 권한 세팅 ==//
    public boolean start_grant(final String MenuID) {
        try {
            JSONArray ja = new JSONArray(sJson_grant);

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                final String LEVEL_CD = jObject.getString("LEVEL_CD");

                ADMIN_CHK = LEVEL_CD.equals("ADMIN") || ADMIN_CHK.equals("Y") ? "Y" : "N";
                W_OUT = LEVEL_CD.equals("W_OUT") || W_OUT.equals("Y") ? "Y" : "N";
                S11 = LEVEL_CD.equals("S11") || S11.equals("Y") ? "Y" : "N";
                S12 = LEVEL_CD.equals("S12") || S12.equals("Y") ? "Y" : "N";
                S13 = LEVEL_CD.equals("S13") || S13.equals("Y") ? "Y" : "N";
                S14 = LEVEL_CD.equals("S14") || S14.equals("Y") ? "Y" : "N";
                S15 = LEVEL_CD.equals("S15") || S15.equals("Y") ? "Y" : "N";
                S16 = LEVEL_CD.equals("S16") || S16.equals("Y") ? "Y" : "N";
                S17 = LEVEL_CD.equals("S17") || S17.equals("Y") ? "Y" : "N";
            }

            if (ADMIN_CHK.equals("N") && W_OUT.equals("N")) {
                if (MenuID.equals("S11") && S11.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "출하요청현황 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("S12") && S12.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "출하등록 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("S13") && S13.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "출하현황 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("S14") && S14.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "출하대기장 이동등록(적치장 이동) 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("S15") && S15.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "출하대기장 이동현황조회 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("S16") && S16.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "적치장재고 출하등록(출하등록) 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("S17") && S17.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "출하대기장 재고현황조회  메뉴에 대한 접속 권한이 없습니다.");
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
                case S11_REQUEST_CODE:
                    break;
                case S12_REQUEST_CODE:
                    break;
                case S13_REQUEST_CODE:
                    break;
                case S14_REQUEST_CODE:
                    break;
                case S15_REQUEST_CODE:
                    break;
                case S16_REQUEST_CODE:
                    break;
                case S17_REQUEST_CODE:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case S11_REQUEST_CODE:
                    break;
                case S12_REQUEST_CODE:
                    break;
                case S13_REQUEST_CODE:
                    break;
                case S14_REQUEST_CODE:
                    break;
                case S15_REQUEST_CODE:
                    break;
                case S16_REQUEST_CODE:
                    break;
                case S17_REQUEST_CODE:
                    break;
            }
        }
    }
}
