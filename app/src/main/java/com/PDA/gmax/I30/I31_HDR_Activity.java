package com.PDA.gmax.I30;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.DBAccess;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class I31_HDR_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(EditText) ==//
    private EditText prodtorder_no;

    //== View 선언(TextView) ==//
    private TextView item_cd, item_nm, tracking_no, prodt_qty, remain_qty, good_qty, bad_qty;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    public Button btn_query;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int I31_DTL_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i31_hdr);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID             = getIntent().getStringExtra("MENU_ID");
        vMenuNm             = getIntent().getStringExtra("MENU_NM");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("START_COMMAND");

        //== ID 값 바인딩 ==//
        prodtorder_no       = (EditText) findViewById(R.id.prodtorder_no);

        item_cd             = (TextView) findViewById(R.id.item_cd);
        item_nm             = (TextView) findViewById(R.id.item_nm);
        tracking_no         = (TextView) findViewById(R.id.tracking_no);
        prodt_qty           = (TextView) findViewById(R.id.prodt_qty);
        remain_qty          = (TextView) findViewById(R.id.remain_qty);
        good_qty            = (TextView) findViewById(R.id.good_qty);
        bad_qty             = (TextView) findViewById(R.id.bad_qty);

        btn_query           = (Button) findViewById(R.id.btn_query);

        listview            = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeListener() {
        prodtorder_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    start();
                    return true;
                }
                return false;
            }
        });

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prodtorder_no.getText().toString().equals("")) {
                    TGSClass.AlertMessage(getApplicationContext(), "제조오더번호를 스캔해주시기 바랍니다.");
                    return;
                } else {
                    start();
                    TGSClass.AlertMessage(getApplicationContext(), "조회되었습니다.");
                }
            }
        });
    }

    private void initializeData() {

    }

    private void start() {
        String prodtorder_no_data = prodtorder_no.getText().toString();

        dbQuery_PRODT_ORDER_INFO(prodtorder_no_data);
        SELECT_PRODT_ORDER_INFO();

        if (sJson.equals("[]")) {
            TGSClass.AlertMessage(getApplicationContext(), "제조오더 정보가 없습니다.");
            return;
        } else {
            dbQuery_HDR(prodtorder_no_data);
            SELECT_HDR();
        }
    }

    private void dbQuery_PRODT_ORDER_INFO(final String prodt_order_no) {
        Thread workThd_PRODT_ORDER_INFO = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_MES_PRODT_ORDER_INFO_GET ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@PRODT_ORDER_NO = '" + prodt_order_no + "' ";

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
        workThd_PRODT_ORDER_INFO.start();   //스레드 시작
        try {
            workThd_PRODT_ORDER_INFO.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private void SELECT_PRODT_ORDER_INFO() {
        try {
            JSONArray ja = new JSONArray(sJson);

            JSONObject jObject = ja.getJSONObject(0);

            String item_cd_data             = jObject.getString("ITEM_CD");  //
            String item_nm_data             = jObject.getString("ITEM_NM");  //품명
            String tracking_no_data         = jObject.getString("TRACKING_NO");  //수량
            String prodt_order_qty_data     = jObject.getString("PRODT_ORDER_QTY");  //
            String remain_qty_data          = jObject.getString("REMAIN_QTY");  //트레킹번호
            String good_qty_data            = jObject.getString("GOOD_QTY");  //트레킹번호
            String bad_qty_data             = jObject.getString("BAD_QTY");  //트레킹번호

            item_cd.setText(item_cd_data);
            item_nm.setText(item_nm_data);
            tracking_no.setText(tracking_no_data);
            prodt_qty.setText(prodt_order_qty_data);
            remain_qty.setText(remain_qty_data);
            good_qty.setText(good_qty_data);
            bad_qty.setText(bad_qty_data);
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private void dbQuery_HDR(final String prodt_order_no) {
        Thread workThd_HDR = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_MES_APP_P1001MA1_GET1 ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@PRODT_ORDER_NO = '" + prodt_order_no + "' ";

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
        workThd_HDR.start();   //스레드 시작
        try {
            workThd_HDR.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private void SELECT_HDR() {
        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            I31_HDR_ListViewAdapter listViewAdapter = new I31_HDR_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I31_HDR item = new I31_HDR();

                item.PRODT_ORDER_NO     = jObject.getString("PRODT_ORDER_NO");  //제조오더번호
                item.OPR_NO             = jObject.getString("OPR_NO");          //공정순서
                item.ITEM_CD            = jObject.getString("ITEM_CD");         //품번
                item.ITEM_NM            = jObject.getString("ITEM_NM");         //품명
                item.TRACKING_NO        = jObject.getString("TRACKING_NO");     //Tracking 번호
                item.REQ_QTY            = jObject.getString("REQ_QTY");         //수량
                item.BASE_UNIT          = jObject.getString("BASE_UNIT");       //
                item.SL_CD              = jObject.getString("SL_CD");           //트레킹번호
                item.SL_NM              = jObject.getString("SL_NM");           //트레킹번호
                item.ISSUED_QTY         = jObject.getString("ISSUED_QTY");      //트레킹번호
                item.REMAIN_QTY         = jObject.getString("REMAIN_QTY");      //트레킹번호
                item.REQ_NO             = jObject.getString("REQ_NO");          //트레킹번호
                item.ISSUE_MTHD         = jObject.getString("ISSUE_MTHD");      //트레킹번호
                item.RESV_STATUS        = jObject.getString("RESV_STATUS");     //트레킹번호
                item.OUT_QTY            = jObject.getString("OUT_QTY");         //트레킹번호
                item.SEQ_NO             = jObject.getString("SEQ_NO");          //트레킹번호

                listViewAdapter.addHDRItem(item);
            }

            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    I31_HDR vItem = (I31_HDR) parent.getItemAtPosition(position);

                    Intent intent = TGSClass.ChangeView(getPackageName(), I31_DTL_Activity.class);
                    intent.putExtra("HDR", vItem);
                    startActivityForResult(intent, I31_DTL_REQUEST_CODE);
                }
            });
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String SIGN = data.getStringExtra("SIGN");
            switch (requestCode) {
                case I31_DTL_REQUEST_CODE:
                    //Log.d("OK", "I31_DTL");
                    if (SIGN.equals("EXIT")) {
                        finish();
                    } else if (SIGN.equals("ADD")) {

                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case I31_DTL_REQUEST_CODE:
                    //Log.d("CANCELED", "I31_DTL");
                    break;
                default:
                    break;
            }
        }
    }
}