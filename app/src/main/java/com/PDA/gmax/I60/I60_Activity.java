package com.PDA.gmax.I60;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class I60_Activity extends BaseActivity {

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(Button) ==//
//    private Button btn_material_delivery, btn_material_import_good, btn_material_import_bad, btn_material_status;
//    private Button btn_menu;
    MaterialToolbar materialToolbar;
    MaterialButton btn_material_delivery, btn_material_import_good, btn_material_import_bad, btn_material_status;
//    ExtendedFloatingActionButton btn_menu;

    //== Grant 관련 변수 ==//
    private String ADMIN_CHK = "N", W_OUT = "N", I61 = "N", I62 = "N", I63 = "N", I64 = "N";     //Grant

    //== ActivityForResult 관련 변수 ==//
    private final int I61_HDR_REQUEST_CODE = 1, I62_HDR_REQUEST_CODE = 2, I63_HDR_REQUEST_CODE = 3, I64_HDR_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i60);

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

        btn_material_delivery           = findViewById(R.id.btn_material_delivery);        // 1. 개발용자재출고
        btn_material_import_good        = findViewById(R.id.btn_material_import_good);     // 2. 개발용자재반입(양품)
        btn_material_import_bad         = findViewById(R.id.btn_material_import_bad);      // 3. 개발용자재반입(불량)
        btn_material_status             = findViewById(R.id.btn_material_status);          // 4. 개발용출고/반입현황
//        btn_menu                        = findViewById(R.id.btn_menu);                     // 메뉴
    }

    private void initializeListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_material_delivery:
                        /**
                        if(start_grant("I61") == true) {
                            Intent intent = TGSClass.ChangeView(getPackageName(), I61_HDR_Activity.class);
                            intent.putExtra("MENU_ID", "I61");
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, I61_HDR_REQUEST_CODE);
                        }
                        */
                        break;
                    case R.id.btn_material_import_good:
                        if(start_grant("I62") == true) {
                            Intent intent = TGSClass.ChangeView(getPackageName(), I62_HDR_Activity.class);
                            intent.putExtra("MENU_ID", "I62");
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, I62_HDR_REQUEST_CODE);
                        }
                        break;
                    case R.id.btn_material_import_bad:
                        /**
                        if(start_grant("I63") == true) {
                            Intent intent = TGSClass.ChangeView(getPackageName(), I63_HDR_Activity.class);
                            intent.putExtra("MENU_ID", "I63");
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, I63_HDR_REQUEST_CODE);
                        }
                        */
                        break;
                    case R.id.btn_material_status:
                        /**
                        if(start_grant("I64") == true) {
                            Intent intent = TGSClass.ChangeView(getPackageName(), I64_HDR_Activity.class);
                            intent.putExtra("MENU_ID", "I64");
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, I64_HDR_REQUEST_CODE);
                        }
                        */
                        break;
                    /*
                    case R.id.btn_menu:        // 메뉴 버튼
                        // 저장 후 결과 값 돌려주기
                        Intent resultIntent = new Intent();
                        // 부른 Activity에게 결과 값 반환
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
        btn_material_delivery.setOnClickListener(clickListener);        // 1. 개발용자재출고
        btn_material_import_good.setOnClickListener(clickListener);     // 2. 개발용자재반입(양품)
        btn_material_import_bad.setOnClickListener(clickListener);      // 3. 개발용자재반입(불량)
        btn_material_status.setOnClickListener(clickListener);          // 4. 개발용출고/반입현황
//        btn_menu.setOnClickListener(clickListener);                     // 메뉴

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
                W_OUT = LEVEL_CD.equals("W_OUT") || W_OUT.equals("Y") ? "Y" : "N";
                I61 = LEVEL_CD.equals("I61") || I61.equals("Y") ? "Y" : "N";
                I62 = LEVEL_CD.equals("I62") || I62.equals("Y") ? "Y" : "N";
                I63 = LEVEL_CD.equals("I63") || I63.equals("Y") ? "Y" : "N";
                I64 = LEVEL_CD.equals("I64") || I64.equals("Y") ? "Y" : "N";
            }

            if (ADMIN_CHK.equals("N") && W_OUT.equals("N")) {
                if (MenuID.equals("I61") && I61.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "개발용 자재출고 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I62") && I62.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "개발용 자재반입(양품) 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I63") && I63.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "개발용 자재반입(불량) 메뉴에 대한 접속 권한이 없습니다.");
                    return false;
                } else if (MenuID.equals("I64") && I64.equals("N")) {
                    TGSClass.AlertMessage(getApplicationContext(), "개발용 출고/반입 현황 메뉴에 대한 접속 권한이 없습니다.");
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
                case I61_HDR_REQUEST_CODE:
                    //Log.d("OK", "I61_HDR");
                    break;
                case I62_HDR_REQUEST_CODE:
                    //Log.d("OK", "I62_HDR");
                    break;
                case I63_HDR_REQUEST_CODE:
                    //Log.d("OK", "I63_HDR");
                    break;
                case I64_HDR_REQUEST_CODE:
                    //Log.d("OK", "I64_HDR");
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case I61_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I61_HDR");
                    break;
                case I62_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I62_HDR");
                    break;
                case I63_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I63_HDR");
                    break;
                case I64_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I64_HDR");
                    break;
                default:
                    break;
            }
        }
    }
}
