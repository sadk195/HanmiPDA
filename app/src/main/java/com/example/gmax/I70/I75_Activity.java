package com.example.gmax.I70;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gmax.BaseActivity;
import com.example.gmax.R;
import com.example.gmax.TGSClass;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class I75_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(TextView) ==//
    private TextView lbl_view_title;

    //== View 선언(EditText) ==//
    private EditText inventory_count_date, sl_cd;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(Button) ==//
    private Button btn_combo_sl_cd, btn_inventory_info, btn_selectd_query;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i75);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID                 = getIntent().getStringExtra("MENU_ID");
        vMenuNm                 = getIntent().getStringExtra("MENU_NM");
        vMenuRemark             = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand           = getIntent().getStringExtra("START_COMMAND");

        //== ID 값 바인딩 ==//
        //findViewById = @+id/### 의 ###을 읽어와 데이터 바인딩
        lbl_view_title          = (TextView) findViewById(R.id.lbl_view_title);
        inventory_count_date    = (EditText) findViewById(R.id.inventory_count_date);
        img_barcode             = (ImageView) findViewById(R.id.img_barcode);
        sl_cd                   = (EditText) findViewById(R.id.sl_cd);

        btn_combo_sl_cd         = (Button) findViewById(R.id.btn_combo_sl_cd);       //
        btn_inventory_info      = (Button) findViewById(R.id.btn_inventory_info);       // 조회 버튼
        btn_selectd_query       = (Button) findViewById(R.id.btn_selectd_query);        // 선택내역조회 버튼
    }

    private void initializeCalendar() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {

        inventory_count_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, inventory_count_date, cal);
            }
        });

        //선택내역조회 버튼 클릭 이벤트
        btn_selectd_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sMenuName = "메뉴 > 재고실사관리 > 재고실사 내역조회";

                Intent intent = TGSClass.ChangeView(getPackageName(), I76_Activity.class);

                intent.putExtra("MENU_ID", "I76");
                intent.putExtra("MENU_NM", sMenuName);
                intent.putExtra("MENU_REMARK", vMenuRemark);
                intent.putExtra("START_COMMAND", vStartCommand);
                startActivity(intent);
            }
        });
    }

    private void initializeData() {
        lbl_view_title.setText(vMenuNm);

        inventory_count_date.setText(df.format(cal.getTime()));
    }
}
