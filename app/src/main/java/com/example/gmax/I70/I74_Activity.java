package com.example.gmax.I70;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gmax.BaseActivity;
import com.example.gmax.DBAccess;
import com.example.gmax.R;
import com.example.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class I74_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(TextView) ==//
    private TextView lbl_view_title;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(EditText) ==//
    private EditText lbl_inventory_table, inventory_no, lbl_inventory_count_date, lbl_sl_cd;

    //== View 선언(Button) ==//
    private Button btn_combo_sl_cd, btn_inventory_info, btn_new_inventory_hdr;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    public String SL_CD;
    public String LOCATION;
    public String ITEM_CD;
    public String ITEM_NM;
    public String QTY;
    public String TRACKING_NO;
    public String LOT_NO;
    public String TAG_NO;
    public String SL_NM;
    public String INVENTORY_COUNT_DATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i74);

        this.initializeView();

        this.initializeCalendar();

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
        img_barcode                 = (ImageView) findViewById(R.id.img_barcode);
        lbl_inventory_table         = (EditText) findViewById(R.id.lbl_inventory_table);
        inventory_no                = (EditText) findViewById(R.id.inventory_no);
        lbl_inventory_count_date    = (EditText) findViewById(R.id.lbl_inventory_count_date);
        lbl_sl_cd                   = (EditText) findViewById(R.id.lbl_sl_cd);
        btn_combo_sl_cd             = (Button) findViewById(R.id.btn_combo_sl_cd);
        btn_inventory_info          = (Button) findViewById(R.id.btn_inventory_info);
        btn_new_inventory_hdr       = (Button) findViewById(R.id.btn_new_inventory_hdr);
    }

    private void initializeCalendar() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {

        lbl_inventory_count_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, lbl_inventory_count_date, cal);
            }
        });

        lbl_inventory_table.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String vINVENTORY_TALBE_NO = lbl_inventory_table.getText().toString();

                    start(vINVENTORY_TALBE_NO);
                    return true;
                }

                return false;
            }
        });

        //조회 버튼 클릭 이벤트
        btn_inventory_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vINVENTORY_TALBE_NO = lbl_inventory_table.getText().toString();

                start(vINVENTORY_TALBE_NO);
            }
        });

        btn_new_inventory_hdr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String INVENTORY_NO = TAG_NO;

                String sMenuName = "메뉴 > 재고실사관리 > 재고실사표 실사등록 > 실사내역등록";

                Intent intent = TGSClass.ChangeView(getPackageName(), I77_Activity.class);

                intent.putExtra("MENU_ID", "I77");
                intent.putExtra("MENU_NM", sMenuName);
                intent.putExtra("MENU_REMARK", vMenuRemark);
                intent.putExtra("START_COMMAND", vStartCommand);

                intent.putExtra("INVENTORY_NO", INVENTORY_NO);
                intent.putExtra("LOCATION", LOCATION);
                intent.putExtra("SL_CD", SL_CD);
                intent.putExtra("SL_NM", SL_NM);
                intent.putExtra("ITEM_CD", ITEM_CD);
                intent.putExtra("ITEM_NM", ITEM_NM);
                intent.putExtra("QTY", QTY);
                intent.putExtra("TRACKING_NO", TRACKING_NO);
                intent.putExtra("LOT_NO", LOT_NO);
                intent.putExtra("INVENTORY_COUNT_DATE", INVENTORY_COUNT_DATE);

                startActivity(intent);
            }
        });
    }

    private void initializeData() {
        lbl_inventory_count_date.setText(df.format(cal.getTime()));
    }

    private void inventory_table_info(final String pINVENTORY_TALBE_NO) {
        Thread wkThd_inventory_table_info = new Thread() {
            public void run() {
                String pSL_CD = "";
                String pREAL_DT = "";
                String pIVT_REGISTRATION = "";

                String sql = " exec XUSP_I74_GET_TAG_INFO ";
                sql += "@FLAG = 'S'";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@TAG_NO = '" + pINVENTORY_TALBE_NO + "'";
                sql += " ,@SL_CD = '" + pSL_CD + "'";
                sql += " ,@REAL_DT = '" + pREAL_DT + "'";
                sql += " ,@IVT_REGISTRATION = '" + pIVT_REGISTRATION + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);
                //Start();
            }
        };
        wkThd_inventory_table_info.start();   //스레드 시작
        try {
            wkThd_inventory_table_info.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }
    }

    private void start(final String pINVENTORY_TALBE_NO) {

        inventory_table_info(pINVENTORY_TALBE_NO);

        if (!sJson.equals("")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    TAG_NO = jObject.getString("TAG_NO");
                    inventory_no.setText(TAG_NO);

                    lbl_inventory_count_date.setText(jObject.getString("REAL_DT"));
                    lbl_sl_cd.setText(jObject.getString("SL_NM"));

                    SL_CD                   = jObject.getString("SL_CD");
                    SL_NM                   = jObject.getString("SL_NM");
                    LOCATION                = jObject.getString("LOCATION");
                    ITEM_CD                 = jObject.getString("ITEM_CD");
                    ITEM_NM                 = jObject.getString("ITEM_NM");
                    QTY                     = jObject.getString("QTY");
                    TRACKING_NO             = jObject.getString("TRACKING_NO");
                    LOT_NO                  = jObject.getString("LOT_NO");
                    INVENTORY_COUNT_DATE    = jObject.getString("REAL_DT");
                }
            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage());
            }
        }
    }
}
