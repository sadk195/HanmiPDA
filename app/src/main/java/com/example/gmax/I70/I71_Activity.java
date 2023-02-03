package com.example.gmax.I70;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmax.BaseActivity;
import com.example.gmax.DBAccess;
import com.example.gmax.M10.M11_DTL_Activity;
import com.example.gmax.R;
import com.example.gmax.ScanData;
import com.example.gmax.TGSClass;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class I71_Activity extends BaseActivity {

    private final long INTERVAL_TIME = 500;  //간격
    private long inputEnterPressedTime = 0;  //엔터키 입력 시간.

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(TextView) ==//
    private TextView lbl_view_title;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(EditText) ==//
    private EditText inventory_count_date, sl_cd;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    private Button btn_inventory_info, btn_selectd_query, btn_inventory_save, btn_new_inventory_hdr;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int I72_REQUEST_CODE = 2, I73_REQUEST_CODE = 3, I79_REQUEST_CODE = 9;

    public String INV_NO = "", CNT = "", SL_CD = "", SL_NM = "", WC_CD = "", WC_NM = "", STOCKYARD = "", CHK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i71);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID = getIntent().getStringExtra("MENU_ID");
        vMenuNm = getIntent().getStringExtra("MENU_NM");
        vMenuRemark = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand = getIntent().getStringExtra("START_COMMAND");

        //== ID 값 바인딩 ==//
        //findViewById = @+id/### 의 ###을 읽어와 데이터 바인딩
        lbl_view_title = (TextView) findViewById(R.id.lbl_view_title);
        inventory_count_date = (EditText) findViewById(R.id.inventory_count_date);
        img_barcode = (ImageView) findViewById(R.id.img_barcode);
        sl_cd = (EditText) findViewById(R.id.sl_cd);

        btn_inventory_info = (Button) findViewById(R.id.btn_inventory_info);       // 조회 버튼
        btn_selectd_query = (Button) findViewById(R.id.btn_selectd_query);        // 선택내역조회 버튼
        btn_inventory_save = (Button) findViewById(R.id.btn_inventory_save);       // 실사등록 버튼
        btn_new_inventory_hdr = (Button) findViewById(R.id.btn_new_inventory_hdr);    // 실사 헤더 신규생성 버튼

        listview = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeCalendar() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {

        btn_inventory_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSCanData(vStartCommand);
            }
        });

        btn_inventory_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vINVENTORY_COUNT_DATE = inventory_count_date.getText().toString();

                String sMenuName = "재고실사관리 > 재고실사 내역등록";

                Intent intent = TGSClass.ChangeView(getPackageName(), I79_Activity.class);

                intent.putExtra("MENU_NM", sMenuName);
                intent.putExtra("MENU_REMARK", vMenuRemark);
                intent.putExtra("START_COMMAND", vStartCommand);
                intent.putExtra("INVENTORY_COUNT_DATE", vINVENTORY_COUNT_DATE);
                startActivityForResult(intent, I79_REQUEST_CODE);
            }
        });

        btn_selectd_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vINVENTORY_COUNT_DATE = inventory_count_date.getText().toString();
                String vSL_CD = sl_cd.getText().toString();

                String sMenuName = "메뉴 > 재고실사관리 > 재고실사 내역조회";
                Intent intent = TGSClass.ChangeView(getPackageName(), I73_Activity.class);

                intent.putExtra("MENU_NM", sMenuName);
                intent.putExtra("MENU_REMARK", vMenuRemark);
                intent.putExtra("START_COMMAND", vStartCommand);
                intent.putExtra("INVENTORY_COUNT_DATE", vINVENTORY_COUNT_DATE);
                intent.putExtra("SL_CD", vSL_CD);

                startActivityForResult(intent, I73_REQUEST_CODE);
            }
        });

        btn_new_inventory_hdr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ScanData scan = new ScanData(vScanText);

                //String vSL_CD = scan.getm_SL_CD();
                //SL_CD = scan.getm_SL_CD();
                String VSl_cd = sl_cd.getText().toString();
                String VDate = inventory_count_date.getText().toString();

                inventory_info_query(VDate, VSl_cd);

            }
        });

        inventory_count_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, inventory_count_date, cal);
            }
        });

        sl_cd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return setSCanData(vStartCommand);
                }
                return false;
            }

        });
    }

    private void initializeData() {
        lbl_view_title.setText(vMenuNm);

        inventory_count_date.setText(df.format(cal.getTime()));

        String VSl_cd = sl_cd.getText().toString();
        String VDate = inventory_count_date.getText().toString();

        inventory_info_query(VDate, VSl_cd);
    }

    //스캔 데이터 값 처리.
    private boolean setSCanData(String pJobCD) {
        String vScanText = sl_cd.getText().toString();

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - inputEnterPressedTime;

        if (0 <= intervalTime && INTERVAL_TIME >= intervalTime) {
            /* FINISH_INTERVAL_TIME 밀리 초 안에 엔터키 이벤트가 발생하면, 뒤에 생긴 이벤트 무시.*/
            return false;
        } else {
            inputEnterPressedTime = tempTime;
        }

        //스캔 데이터 생성 클래스
        ScanData scan = new ScanData(vScanText);

        String vSL_CD = scan.getm_SL_CD();
        SL_CD = scan.getm_SL_CD();
        String VDate = inventory_count_date.getText().toString();

        inventory_info_query(VDate, vSL_CD);

        //ChangeView(vSL_CD, vMenuRemark);

        return true;
    }

    private void ChangeView(String vSL_CD, String pMenuRemark) {
        Intent intent = TGSClass.ChangeView(getPackageName(), M11_DTL_Activity.class);
        intent.putExtra("SL_CD", vSL_CD);
        intent.putExtra("MENU_REMARK", pMenuRemark);
        intent.putExtra("START_COMMAND", vStartCommand);
        startActivity(intent);
    }

    private void inventory_info_query(final String pDate, final String pSL_CD) {
        Thread wkThd_inventory_info_query = new Thread() {
            public void run() {

                String sql = " exec XUSP_I71_GET_LIST ";
                sql += " @FLAG = 'H'";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@DATE = '" + pDate + "'";
                sql += " ,@SL_CD = '" + pSL_CD + "'";
                sql += " ,@INV_NO = ''";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        wkThd_inventory_info_query.start();   //스레드 시작
        try {
            wkThd_inventory_info_query.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }

        start();
    }

    private void start() {
        if (!sJson.equals("[]")) {

            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                final ArrayList<I71_ARRAYLIST> lstItem = new ArrayList<>();

                I71_ListViewAdapter listViewAdapter = new I71_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    //CHK = jObject.getString("CHK");  //체크 이벤트
                    CNT = jObject.getString("CNT") + "건";
                    INV_NO = jObject.getString("INV_NO");
                    SL_CD = jObject.getString("SL_CD");
                    SL_NM = jObject.getString("SL_NM");
                    WC_CD = jObject.getString("WC_CD");
                    WC_NM = jObject.getString("WC_NM");

                    //listViewAdapter.addItem(CHK,CNT, INV_NO, SL_CD, SL_NM, WC_CD, WC_NM);
                    listViewAdapter.addItem(CNT, INV_NO, SL_CD, SL_NM, WC_CD, WC_NM);
                }

                listview.setAdapter(listViewAdapter);
            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage());
            }
        } else {
            MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(I71_Activity.this);
            mAlert.setTitle("재고실사등록")
                    .setMessage("재고실사등록 번호가 없습니다. 신규 번호를 생성하시겠습니까 ?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (save_Inv_no_hdr() == true) {
                                Toast.makeText(getApplicationContext(), "신규 생성이 완료 되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton("아니오", null)
                    .create().show();

        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                I71_ListViewAdapter listViewAdapter = (I71_ListViewAdapter) parent.getAdapter();
                I71_ARRAYLIST item = (I71_ARRAYLIST) listViewAdapter.getItem(position);

                @SuppressLint("WrongViewCast") CheckedTextView chk = (CheckedTextView) findViewById(R.id.chk);

                if (chk.isChecked() == true) {
                    chk.setChecked(false);
                    item.CHK = "";
                } else {
                    chk.setChecked(true);
                    item.CHK = "Y";
                }

                */
                String sMenuName = "메뉴 > 재고실사관리 > 재고실사내역등록";
                Intent intent = TGSClass.ChangeView(getPackageName(), I72_Activity.class);

                intent.putExtra("MENU_NM", sMenuName);
                intent.putExtra("MENU_REMARK", vMenuRemark);
                intent.putExtra("START_COMMAND", vStartCommand);
                intent.putExtra("INV_NO", INV_NO);
                intent.putExtra("SL_CD", SL_CD);
                intent.putExtra("SL_NM", SL_NM);
                intent.putExtra("WC_CD", WC_CD);

                startActivityForResult(intent, I72_REQUEST_CODE);
            }
        });
    }

    private boolean save_Inv_no_hdr() {
        Thread wkThd_save_Inv_no_hdr = new Thread() {
            public void run() {

                String vINV_DT = inventory_count_date.getText().toString();
                String vSL_CD = SL_CD;
                //tring vSL_CD = sl_cd.getText().toString();

                String sql = " exec XUSP_I71_S_SET_LIST_HDR ";
                sql += " @USER_ID = '" + vUSER_ID + "'";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@SL_CD = '" + vSL_CD + "'";
                sql += " ,@INV_DT = '" + vINV_DT + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        wkThd_save_Inv_no_hdr.start();   //스레드 시작
        try {
            wkThd_save_Inv_no_hdr.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
            return false;
        }
        return true;

    }
}