package com.example.gmax.I70;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gmax.DBAccess;
import com.example.gmax.MySession;
import com.example.gmax.R;
import com.example.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class I77_Activity extends AppCompatActivity {

    private final long INTERVAL_TIME = 500;  //간격
    private long inputEnterPressedTime = 0;  //엔터키 입력 시간.
    public String sJson = "";
    public ImageView img_barcode;
    public EditText txt_Scan;
    public String sJobCode;
    public String sMenuRemark;
    public MySession global;
    public String Plant_CD;
    public String INVENTORY_NO, INVENTORY_COUNT_DATE, SL_CD, SL_NM, LOCATION, WC_CD, ITEM_NM, QTY, ITEM_CD, TRACKING_NO, LOT_NO;
    public EditText lbl_inventory_table, txt_stockyard, txt_sl_cd, txt_item_cd, txt_item_nm, txt_inventory_qty,txt_tracking_no,txt_lot_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i77);




        String vMenuName = getIntent().getStringExtra("MENU_NM");
        sMenuRemark = getIntent().getStringExtra("MENU_REMARK");
        sJobCode = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드
        INVENTORY_NO = getIntent().getStringExtra("INVENTORY_NO");
        LOCATION = getIntent().getStringExtra("LOCATION");
        SL_CD = getIntent().getStringExtra("SL_CD");
        SL_NM = getIntent().getStringExtra("SL_NM");
        ITEM_CD = getIntent().getStringExtra("ITEM_CD");
        ITEM_NM = getIntent().getStringExtra("ITEM_NM");
        QTY = getIntent().getStringExtra("QTY");
        TRACKING_NO = getIntent().getStringExtra("TRACKING_NO");
        LOT_NO = getIntent().getStringExtra("LOT_NO");
        INVENTORY_COUNT_DATE = getIntent().getStringExtra("INVENTORY_COUNT_DATE");
        TextView lbl_view_title = findViewById(R.id.lbl_view_title);
        lbl_view_title.setText(vMenuName);
        final EditText lbl_inventory_table = (EditText) findViewById(R.id.lbl_inventory_table);
        lbl_inventory_table.setText(INVENTORY_NO);
        EditText txt_stockyard = (EditText) findViewById(R.id.txt_stockyard);
        txt_stockyard.setText(LOCATION);
        final TextView txt_sl_cd = findViewById(R.id.txt_sl_cd);
        txt_sl_cd.setText(SL_NM);
        EditText txt_item_cd = (EditText) findViewById(R.id.txt_item_cd);
        txt_item_cd.setText(ITEM_CD);
        EditText txt_item_nm = (EditText) findViewById(R.id.txt_item_nm);
        txt_item_nm.setText(ITEM_NM);
        EditText txt_inventory_qty = (EditText) findViewById(R.id.txt_inventory_qty);
        txt_inventory_qty.setText(QTY);
        EditText txt_tracking_no = (EditText) findViewById(R.id.txt_tracking_no);
        txt_tracking_no.setText(TRACKING_NO);
        EditText txt_lot_no = (EditText) findViewById(R.id.txt_lot_no);
        txt_lot_no.setText(LOT_NO);

        txt_inventory_qty.requestFocus();

        txt_inventory_qty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    Button btn_save = (Button) findViewById(R.id.btn_save);
                    btn_save.requestFocus();
                    return true;
                }

                return false;
            }
        });


        //재고실사 내역등록 버튼 클릭 이벤트
        Button btn_list = findViewById(R.id.btn_save);   //findViewById = @+id/### 의 ###을 읽어와 데이터 바인딩
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText lbl_inventory_table = (EditText) findViewById(R.id.lbl_inventory_table);
                EditText txt_stockyard = (EditText) findViewById(R.id.txt_stockyard);
                EditText txt_item_cd = (EditText) findViewById(R.id.txt_item_cd);
                EditText txt_inventory_qty = (EditText) findViewById(R.id.txt_inventory_qty);
                EditText txt_tracking_no = (EditText) findViewById(R.id.txt_tracking_no);
                EditText txt_lot_no = (EditText) findViewById(R.id.txt_lot_no);
                EditText txt_item_nm = (EditText) findViewById(R.id.txt_item_nm);

                INVENTORY_NO = lbl_inventory_table.getText().toString();
                LOCATION = txt_stockyard.getText().toString();
                ITEM_CD = txt_item_cd.getText().toString();
                QTY = txt_inventory_qty.getText().toString();
                TRACKING_NO = txt_tracking_no.getText().toString();
                LOT_NO = txt_lot_no.getText().toString();

                String vSL_CD = SL_CD;
                String vINVENTORY_COUNT_DATE = INVENTORY_COUNT_DATE;


                if(inventory_info_save(INVENTORY_NO,vSL_CD,ITEM_CD,QTY,vINVENTORY_COUNT_DATE) == true)
                {
                    lbl_inventory_table.getText().clear();
                    txt_stockyard.getText().clear();
                    txt_item_cd.getText().clear();
                    txt_inventory_qty.getText().clear();
                    txt_tracking_no.getText().clear();
                    txt_lot_no.getText().clear();
                    txt_item_nm.getText().clear();

                    lbl_inventory_table.requestFocus();
                }
            }
        });

        lbl_inventory_table.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    String vINVENTORY_TALBE_NO = lbl_inventory_table.getText().toString();

                    Start(vINVENTORY_TALBE_NO);
                    return true;
                }

                return false;
            }
        });

    }


    public boolean inventory_info_save(final String pINVENTORY_NO, final String vSL_CD, final String pITEM_CD, final String pINVENTORY_QTY, final String vINVENTORY_COUNT_DATE) {
        Thread workingThread = new Thread() {
            public void run() {



                global = (MySession)getApplication(); //전역 클래스

                String vParmID = getIntent().getStringExtra("pID");              //등록된 장비명이 로그인 아이디로 판단.
                String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");
                String vDevice = getIntent().getStringExtra("pDEVICE");

                if(vParmID == null) vParmID = global.getLoginIDString();
                if(vPlant_CD == null) vPlant_CD = global.getPlantCDString();
                if(vDevice == null) vDevice = global.getUnitCDString();

                String sql = " exec XUSP_I77_SET_LIST_DTL ";
                sql += " @FLAG = 'U'";
                sql += " ,@PLANT_CD = '" + vPlant_CD + "'";
                sql += " ,@TAG_NO = '" + pINVENTORY_NO + "'";
                sql += " ,@SL_CD = '" + vSL_CD + "'";
                sql += " ,@REAL_DT = '" + vINVENTORY_COUNT_DATE + "'";
                sql += " ,@REAL_QTY = " + pINVENTORY_QTY + "";
                sql += " ,@ITEM_CD = '" + pITEM_CD + "'";
                sql += " ,@USER_ID = '" + vParmID + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);
                CallableStatement cs = (CallableStatement) new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                try {
                    cs.setString(1, "RTM_ITEM_DOCUMENT_NO");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    cs.registerOutParameter(1, java.sql.Types.VARCHAR);
                } catch (SQLException e) {
                    e.printStackTrace();
                }



            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
            return false;
        }
        return true;
    }

    public  void Start(final String pINVENTORY_TALBE_NO) {

        Inventory_table_detail_nfo(pINVENTORY_TALBE_NO);

        EditText lbl_inventory_table = (EditText) findViewById(R.id.lbl_inventory_table);
        EditText txt_stockyard = (EditText) findViewById(R.id.txt_stockyard);
        EditText txt_item_cd = (EditText) findViewById(R.id.txt_item_cd);
        EditText txt_sl_cd = (EditText) findViewById(R.id.txt_sl_cd);
        EditText txt_inventory_qty = (EditText) findViewById(R.id.txt_inventory_qty);
        EditText txt_tracking_no = (EditText) findViewById(R.id.txt_tracking_no);
        EditText txt_lot_no = (EditText) findViewById(R.id.txt_lot_no);
        EditText txt_item_nm = (EditText) findViewById(R.id.txt_item_nm);

        if (!sJson.equals("")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    lbl_inventory_table.setText(jObject.getString("TAG_NO"));
                    txt_stockyard.setText(jObject.getString("LOCATION"));
                    txt_sl_cd.setText(jObject.getString("SL_NM"));
                    txt_item_cd.setText(jObject.getString("ITEM_CD"));
                    txt_item_nm.setText(jObject.getString("ITEM_NM"));
                    txt_inventory_qty.setText(jObject.getString("QTY"));
                    txt_tracking_no.setText(jObject.getString("TRACKING_NO"));
                    txt_lot_no.setText(jObject.getString("LOT_NO"));


                }
                txt_inventory_qty.requestFocus();
            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage());
            }
        }
    }

    public void Inventory_table_detail_nfo(final String pINVENTORY_TALBE_NO) {
        Thread workingThread = new Thread() {
            public void run() {
                String pSL_CD = "";
                String pREAL_DT = "";
                String pIVT_REGISTRATION = "";

                global = (MySession)getApplication(); //전역 클래스

                String vParmID = getIntent().getStringExtra("pID");              //등록된 장비명이 로그인 아이디로 판단.
                String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");
                String vDevice = getIntent().getStringExtra("pDEVICE");

                if(vParmID == null) vParmID = global.getLoginIDString();
                if(vPlant_CD == null) vPlant_CD = global.getPlantCDString();
                if(vDevice == null) vDevice = global.getUnitCDString();

                String sql = " exec XUSP_I74_GET_TAG_INFO ";
                sql += "@FLAG = 'S'";
                sql += " ,@PLANT_CD = '" + vPlant_CD + "'";
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
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }
    }


}
