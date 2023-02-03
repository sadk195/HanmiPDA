package com.example.gmax.I70;

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

public class I72_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(TextView) ==//
    private TextView lbl_view_title;

    //== View 선언(ImageView) ==//
    private ImageView img_stockyard_barcode, img_item_cd_barcode, img_tracking_no_barcode, img_lot_no_barcode;

    //== View 선언(EditText) ==//
    private EditText txt_stockyard, txt_sl_cd, txt_item_cd, txt_item_nm, txt_inventory_qty, txt_tracking_no, txt_lot_no;

    //== View 선언(Button) ==//
    private Button btn_save;

    public String INV_NO, SL_CD, SL_NM , WC_CD, ITEM_NM, STOCKYARD, ITEM_CD, INVENTORY_QTY, TRACKING_NO, LOT_NO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i72);

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

        img_stockyard_barcode       = (ImageView) findViewById(R.id.img_stockyard_barcode);
        img_item_cd_barcode         = (ImageView) findViewById(R.id.img_item_cd_barcode);
        img_tracking_no_barcode     = (ImageView) findViewById(R.id.img_tracking_no_barcode);
        img_lot_no_barcode          = (ImageView) findViewById(R.id.img_lot_no_barcode);

        txt_stockyard               = (EditText) findViewById(R.id.txt_stockyard);
        txt_sl_cd                   = (EditText) findViewById(R.id.txt_sl_cd);
        txt_item_cd                 = (EditText) findViewById(R.id.txt_item_cd);
        txt_item_nm                 = (EditText) findViewById(R.id.txt_item_nm);
        txt_inventory_qty           = (EditText) findViewById(R.id.txt_inventory_qty);
        txt_tracking_no             = (EditText) findViewById(R.id.txt_tracking_no);
        txt_lot_no                  = (EditText) findViewById(R.id.txt_lot_no);

        btn_save                    = (Button) findViewById(R.id.btn_save);       // 등록 버튼
    }

    private void initializeListener() {

        txt_stockyard.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    //EditText txt_stockyard = (EditText) findViewById(R.id.txt_stockyard);
                    txt_item_cd.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txt_item_cd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String vitem_nm = txt_item_cd.getText().toString();
                    item_info_query(vitem_nm);
                    txt_item_nm.setText(ITEM_NM);

                    txt_inventory_qty.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txt_inventory_qty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    txt_tracking_no.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txt_tracking_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    txt_lot_no.requestFocus();
                    return true;
                }
                return false;
            }
        });

        txt_lot_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    btn_save.requestFocus();
                    return true;
                }
                return false;
            }
        });

        //재고실사 내역등록 버튼 클릭 이벤트
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                STOCKYARD = txt_stockyard.getText().toString();
                ITEM_CD = txt_item_cd.getText().toString();
                INVENTORY_QTY = txt_inventory_qty.getText().toString();
                TRACKING_NO = txt_tracking_no.getText().toString();
                LOT_NO = txt_lot_no.getText().toString();

                if (inventory_info_save(INV_NO, STOCKYARD, ITEM_CD, INVENTORY_QTY, TRACKING_NO, LOT_NO) == true) {
                    txt_stockyard.getText().clear();
                    txt_item_cd.getText().clear();
                    txt_inventory_qty.getText().clear();
                    txt_tracking_no.getText().clear();
                    txt_lot_no.getText().clear();
                    txt_item_nm.getText().clear();

                    //Button btn_list = findViewById(R.id.btn_save);

                    //EditText txt_stockyard = (EditText) findViewById(R.id.txt_stockyard);
                    txt_stockyard.requestFocus();
                }
            }
        });
    }

    private void initializeData() {
        INV_NO = getIntent().getStringExtra("INV_NO");
        SL_CD = getIntent().getStringExtra("SL_CD");
        SL_NM = getIntent().getStringExtra("SL_NM");
        WC_CD = getIntent().getStringExtra("WC_CD");

        lbl_view_title.setText(vMenuNm);
        txt_sl_cd.setText(SL_NM);
    }

    private void item_info_query(final String pITEM_CD) {
        Thread wkThd_item_info_query = new Thread() {
            public void run() {
                String sql = " SELECT A.ITEM_NM ";
                sql += " FROM B_ITEM AS A";
                sql += " LEFT OUTER JOIN B_ITEM_BY_PLANT AS B (NOLOCK) ON B.ITEM_CD = A.ITEM_CD";
                sql += " WHERE B.PLANT_CD = '" + vPLANT_CD + "'";
                sql += " AND A.ITEM_CD LIKE + '" + pITEM_CD + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);

                ITEM_NM_INFO();
            }
        };
        wkThd_item_info_query.start();   //스레드 시작
        try {
            wkThd_item_info_query.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }
    }

    private void ITEM_NM_INFO() {
        if (!sJson.equals("")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    ITEM_NM = jObject.getString("ITEM_NM");
                }
            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage());
            }
        }
    }

    private boolean inventory_info_save(final String pINV_NO, final String pSTOCKYARD, final String pITEM_CD, final String pINVENTORY_QTY, final String pTRACKING_NO, final String pLOT_NO) {
        Thread wkThd_inventory_info_save = new Thread() {
            public void run() {

                String sql = " exec XUSP_I72_SET_LIST_DTL ";
                sql += "@INV_NO = '" + pINV_NO + "'";
                sql += " ,@ITEM_CD = '" + pITEM_CD + "'";
                sql += " ,@TRACKING_NO = '" + pTRACKING_NO + "'";
                sql += " ,@LOT_NO = '" + pLOT_NO + "'";
                sql += " ,@INV_QTY = " + pINVENTORY_QTY + "";
                sql += " ,@LOCATION = '" + pSTOCKYARD + "'";
                sql += " ,@USER_ID = '" + vUSER_ID + "'";

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
        wkThd_inventory_info_save.start();   //스레드 시작
        try {
            wkThd_inventory_info_save.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
            return false;
        }
        return true;
    }
}
