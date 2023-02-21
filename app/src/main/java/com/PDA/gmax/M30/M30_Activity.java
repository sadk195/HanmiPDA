package com.PDA.gmax.M30;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class M30_Activity extends BaseActivity {

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(Button) ==//
//    private Button btn_arrival_registration, btn_arrival_query, btn_menu;
    MaterialToolbar materialToolbar;
    MaterialButton btn_arrival_registration, btn_arrival_query;
//    MaterialButton btn_menu;

    //== Grant 관련 변수 ==//
    private String ADMIN_CHK = "N", W_IN = "N", M31 = "N", M32 = "N";     //Grant

    //== ActivityForResult 관련 변수 ==//
    private final int M31_HDR_REQUEST_CODE = 1, M32_QUERY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m30);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID                     = getIntent().getStringExtra("MENU_ID");
        vMenuNm                     = getIntent().getStringExtra("MENU_NM");
        vMenuRemark                 = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand               = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== ID값 바인딩 ==//
        materialToolbar             = findViewById(R.id.materialToolbar);

        btn_arrival_registration    = findViewById(R.id.btn_arrival_registration);     // 1. 입하등록
        btn_arrival_query           = findViewById(R.id.btn_arrival_query);            // 2. 입하현황조회
//        btn_menu                    = findViewById(R.id.btn_menu);                     // 메뉴
    }

    private void initializeListener() {
        //== Click 이벤트 만들기 ==//
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btn_arrival_registration) {
                    String sMenuName = "메뉴 > 입하관리 > 입하등록";

                    Intent intent = TGSClass.ChangeView(getPackageName(), M31_HDR_Activity.class);
                    intent.putExtra("MENU_ID", "M31");
                    intent.putExtra("MENU_NM", sMenuName);
                    intent.putExtra("MENU_REMARK", vMenuRemark);
                    intent.putExtra("JOB_CD", vStartCommand);
                    startActivityForResult(intent, M31_HDR_REQUEST_CODE);
                } else if (v == btn_arrival_query) {
                    /**
                    String sMenuName = "메뉴 > 입하관리 > 입하현황조회";
                    Intent intent = TGSClass.ChangeView(getPackageName(), M32_HDR_Activity.class);
                    intent.putExtra("MENU_ID", "M32");
                    intent.putExtra("MENU_NM", sMenuName);
                    intent.putExtra("MENU_REMARK", vMenuRemark);
                    intent.putExtra("JOB_CD", vStartCommand);
                    startActivityForResult(intent, M32_QUERY_REQUEST_CODE);
                    */
                }
                /*
                if (v == btn_menu) {
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

        //== 이벤트 부여 ==//
        btn_arrival_registration.setOnClickListener(clickListener);     // 1. 입하등록
        btn_arrival_query.setOnClickListener(clickListener);            // 2. 입하현황조회
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case M31_HDR_REQUEST_CODE:
                    //Log.d("OK", "M31_HDR");
                    break;
                case M32_QUERY_REQUEST_CODE:
                    //Log.d("OK", "M32_QUERY");
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case M31_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "M31_HDR");
                    break;
                case M32_QUERY_REQUEST_CODE:
                    //Log.d("CANCELED", "M32_QUERY");
                    break;
                default:
                    break;
            }
        }
    }
}
