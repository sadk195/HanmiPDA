package com.example.gmax.I50;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.gmax.BaseActivity;
import com.example.gmax.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class I50_Activity extends BaseActivity {

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(Button) ==//
//    private Button btn_inventory_disposal, btn_inventory_disposal_status;
//    private Button btn_menu;
    MaterialToolbar materialToolbar;
    MaterialButton btn_inventory_disposal, btn_inventory_disposal_status;
//    ExtendedFloatingActionButton btn_menu;

    //== Grant 관련 변수 ==//
    private String ADMIN_CHK = "N", W_OUT = "N", I51 = "N", I52 = "N";     //Grant

    //== ActivityForResult 관련 변수 ==//
    private final int I51_HDR_REQUEST_CODE = 1, I52_HDR_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i50);

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
        vStartCommand                       = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== ID값 바인딩 ==//
        materialToolbar                     = findViewById(R.id.materialToolbar);

        btn_inventory_disposal              = findViewById(R.id.btn_inventory_disposal);           // 1. 재고폐기출고
        btn_inventory_disposal_status       = findViewById(R.id.btn_inventory_disposal_status);    // 2. 재고폐기현황
//        btn_menu                            = findViewById(R.id.btn_menu);                         // 메뉴
    }

    private void initializeListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_inventory_disposal:
                        /**
                        if (start_grant("I51")) {
                            String sMenuName = "";

                            Intent intent = TGSClass.ChangeView(getPackageName(), I51_HDR_Activity.class);
                            intent.putExtra("MENU_ID", "I51");
                            intent.putExtra("MENU_NM", sMenuName);
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, I51_HDR_REQUEST_CODE);
                        }
                         */
                        break;
                    case R.id.btn_inventory_disposal_status:
                        /**
                        if (start_grant("I52")) {
                            String sMenuName = "";

                            Intent intent = TGSClass.ChangeView(getPackageName(), I52_HDR_Activity.class);
                            intent.putExtra("MENU_ID", "I52");
                            intent.putExtra("MENU_NM", sMenuName);
                            intent.putExtra("MENU_REMARK", vMenuRemark);
                            intent.putExtra("START_COMMAND", vStartCommand);
                            startActivityForResult(intent, I52_HDR_REQUEST_CODE);
                        }
                         */
                        break;
                    /*
                    case R.id.btn_menu:
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
        btn_inventory_disposal.setOnClickListener(clickListener);           // 1. 재고폐기출고
        btn_inventory_disposal_status.setOnClickListener(clickListener);    // 2. 재고폐기현황
//        btn_menu.setOnClickListener(clickListener);                         // 메뉴

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case I51_HDR_REQUEST_CODE:
                    //Log.d("OK", "I51_HDR");
                    break;
                case I52_HDR_REQUEST_CODE:
                    //Log.d("OK", "I52_HDR");
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case I51_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I51_HDR");
                    break;
                case I52_HDR_REQUEST_CODE:
                    //Log.d("CANCELED", "I52_HDR");
                    break;
                default:
                    break;
            }
        }
    }
}
