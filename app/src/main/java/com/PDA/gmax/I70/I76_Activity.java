package com.PDA.gmax.I70;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;

public class I76_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(TextView) ==//
    private TextView lbl_view_title;

    //== View 선언(EditText) ==//
    private EditText inventory_save_no, lbl_inventory_count_date, sl_cd, lbl_status;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    private Button btn_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i76);

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
        vStartCommand               = getIntent().getStringExtra("START_COMMAND");

        //== ID 값 바인딩 ==//
        //findViewById = @+id/### 의 ###을 읽어와 데이터 바인딩
        lbl_view_title              = (TextView) findViewById(R.id.lbl_view_title);

        inventory_save_no           = (EditText) findViewById(R.id.inventory_save_no);
        lbl_inventory_count_date    = (EditText) findViewById(R.id.lbl_inventory_count_date);
        sl_cd                       = (EditText) findViewById(R.id.sl_cd);
        lbl_status                  = (EditText) findViewById(R.id.lbl_status);

        btn_end                     = (Button) findViewById(R.id.btn_end);       // 닫기 버튼
    }

    private void initializeListener() {
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sMenuName = "메뉴 > 재고실사관리 > 재고실사현황";

                Intent intent = TGSClass.ChangeView(getPackageName(), I75_Activity.class);

                intent.putExtra("MENU_NM", sMenuName);
                intent.putExtra("MENU_REMARK", vMenuRemark);
                intent.putExtra("START_COMMAND", vStartCommand);
                startActivity(intent);
            }
        });
    }

    private void initializeData() {

        lbl_view_title.setText(vMenuNm);

    }
}
