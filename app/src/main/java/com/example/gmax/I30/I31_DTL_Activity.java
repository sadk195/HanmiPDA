package com.example.gmax.I30;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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


public class I31_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private I31_HDR vGetHDRItem;

    //== View 선언(TextView) ==//
    private TextView item_cd, tracking_no, out_qty_query, resvd_issued;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(EditText) ==//
    private EditText out_date, out_qty_insert;

    //== View 선언(Button) ==//
    private Button btn_add, btn_save;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i31_dtl);

        this.initializeView();

        this.initializeCalendar();

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
        vStartCommand       = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        vGetHDRItem         = (I31_HDR)getIntent().getSerializableExtra("HDR");

        //== ID 값 바인딩 ==//
        item_cd             = (TextView) findViewById(R.id.item_cd);
        tracking_no         = (TextView) findViewById(R.id.tracking_no);
        out_qty_query       = (TextView) findViewById(R.id.out_qty_query);
        resvd_issued        = (TextView) findViewById(R.id.resvd_issued);

        listview            = (ListView) findViewById(R.id.listOrder);

        out_date            = (EditText) findViewById(R.id.out_date);
        out_qty_insert      = (EditText) findViewById(R.id.out_qty_insert);

        btn_save            = (Button) findViewById(R.id.btn_save);
        btn_add             = (Button) findViewById(R.id.btn_add);
    }

    private void initializeCalendar() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {
        out_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, out_date, cal);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbSave("ADD");
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbSave("EXIT");
            }
        });
    }

    private void initializeData() {
        out_date.setText(df.format(cal.getTime()));

        start();
    }

    private void start() {
        dbQuery();

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            I31_DTL_ListViewAdapter listViewAdapter = new I31_DTL_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I31_DTL item = new I31_DTL();

                item.ITEM_CD            = jObject.getString("ITEM_CD");             //
                item.ITEM_NM            = jObject.getString("ITEM_NM");             //
                item.GOOD_QTY           = jObject.getString("GOOD_QTY");            //
                item.SL_CD              = jObject.getString("SL_CD");               //
                item.SL_NM              = jObject.getString("SL_NM");               //
                item.TRACKING_NO        = jObject.getString("TRACKING_NO");         //
                item.GOOD_ON_HAND_QTY   = jObject.getString("GOOD_ON_HAND_QTY");    //

                listViewAdapter.addDTLItem(item);
            }
            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {

                }
            });
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private void dbQuery() {
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_I_ONHAND_STOCK_GET ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@ITEM_CD = '" + vGetHDRItem.getITEM_CD() + "' ";

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
        workThd_dbQuery.start();   //스레드 시작
        try {
            workThd_dbQuery.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private boolean BL_DATASET_SELECT(final String CUD_FLAG, final String vPlant_CD, final String PRODT_ORDER_NO, final String OPR_NO, final String ITEM_CD, final String SEQ_NO, final String OUT_QTY, final String REPORT_DT, final String vUnit_CD) {
        Thread workThd_BL_DATASET_SELECT = new Thread() {
            public void run() {
                String out_date_data = out_date.getText().toString();

                String cud_flag         = CUD_FLAG;
                String plant_cd         = vPlant_CD;
                String prodt_order_no   = PRODT_ORDER_NO;
                String opr_no           = OPR_NO;
                String item_cd          = ITEM_CD;
                String seq_no           = SEQ_NO;
                String out_qty          = OUT_QTY;
                String report_dt        = REPORT_DT;
                String unit_cd          = vUnit_CD;

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("cud_flag");
                parm.setValue(cud_flag);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("plant_cd");
                parm2.setValue(plant_cd);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("prodt_order_no");
                parm3.setValue(prodt_order_no);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("opr_no");
                parm4.setValue(opr_no);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("item_cd");
                parm5.setValue(item_cd);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("seq_no");
                parm6.setValue(seq_no);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("out_qty");
                parm7.setValue(out_qty);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("report_dt");
                parm8.setValue(report_dt);
                parm8.setType(String.class);

                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("unit_cd");
                parm9.setValue(unit_cd);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("user_id");
                parm10.setValue(vUSER_ID);
                parm10.setType(String.class);

                pParms.add(parm);
                pParms.add(parm2);
                pParms.add(parm3);
                pParms.add(parm4);
                pParms.add(parm5);
                pParms.add(parm6);
                pParms.add(parm7);
                pParms.add(parm8);
                pParms.add(parm9);
                pParms.add(parm10);

                result_msg = dba.SendHttpMessage("BL_SetPartListOut_ANDROID", pParms);
            }
        };
        workThd_BL_DATASET_SELECT.start();   //스레드 시작
        try {
            workThd_BL_DATASET_SELECT.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }

    //== 저장 ==//
    private void dbSave(String val) {
        try {
            String out_qty_query_st = out_qty_query.getText().toString();
            double out_qty_query_num = Double.valueOf(out_qty_query_st);

            String out_qty_insert_st = out_qty_insert.getText().toString();
            double out_qty_insert_num = Double.valueOf(out_qty_insert_st);

            String out_date_data = out_date.getText().toString();

            if (out_qty_query_num < out_qty_insert_num) {   //입력한 출고량이 출고가능 수량보다 많을 경우 리턴
                TGSClass.AlertMessage(getApplicationContext(), "입력한 출고량이 출고가능수량 보다 많습니다.");
                return;
            } else {
                String CUD_FLAG         = "U";
                String PRODT_ORDER_NO   = vGetHDRItem.getPRODT_ORDER_NO();
                String OPR_NO           = vGetHDRItem.getOPR_NO();
                String ITEM_CD          = vGetHDRItem.getITEM_CD();
                String SEQ_NO           = vGetHDRItem.getSEQ_NO();
                String OUT_QTY          = out_qty_insert_st;
                String REPORT_DT        = out_date_data;

                BL_DATASET_SELECT(CUD_FLAG, vPLANT_CD, PRODT_ORDER_NO, OPR_NO, ITEM_CD, SEQ_NO, OUT_QTY, REPORT_DT, vUNIT_CD);

                // 저장 후 결과 값 돌려주기
                Intent resultIntent = new Intent();
                // 결과처리 후 부른 Activity에 보낼 값
                resultIntent.putExtra("SIGN", val);
                // 부른 Activity에게 결과 값 반환
                setResult(RESULT_OK, resultIntent);
                // 현재 Activity 종료
                finish();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}