package com.PDA.Hanmi.I30;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.PDA.Hanmi.BaseActivity;
import com.PDA.Hanmi.DBAccess;
import com.PDA.Hanmi.R;
import com.PDA.Hanmi.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class I34_QUERY_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(TextView) ==//
    private TextView txt_title, item_cd, item_nm, tracking_no, prodt_qty, remain_qty;

    //== View 선언(EditText) ==//
    private EditText prodtorder_no, fr_dt, to_dt;

    //== View 선언(Button) ==//
    private Button btn_query, btn_menu;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== 날짜 관련 변수 선언 ==//
    private Calendar cal1, cal2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i34_query);

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

        //== ID 값 바인딩 ==//
        txt_title           = (TextView) findViewById(R.id.txt_title);

        prodtorder_no       = (EditText) findViewById(R.id.prodtorder_no);
        fr_dt               = (EditText) findViewById(R.id.fr_dt);
        to_dt               = (EditText) findViewById(R.id.to_dt);

        item_cd             = (TextView) findViewById(R.id.item_cd);
        item_nm             = (TextView) findViewById(R.id.item_nm);
        tracking_no         = (TextView) findViewById(R.id.tracking_no);
        prodt_qty           = (TextView) findViewById(R.id.prodt_qty);
        remain_qty          = (TextView) findViewById(R.id.remain_qty);

        btn_query           = (Button) findViewById(R.id.btn_query);

        listview            = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeCalendar() {
        cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal1.setTime(new Date());
        cal1.add(Calendar.DAY_OF_MONTH, -7);

        cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal2.setTime(new Date());
    }

    private void initializeListener() {

        //== 제조오더번호(EditText) 이벤트 ==//
        prodtorder_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String prodtorder_no_st = prodtorder_no.getText().toString();

                    dbQuery_PRODT_ORDER_INFO(prodtorder_no_st);
                    select_PRODT_ORDER_INFO();

                    start(prodtorder_no_st);
                    return true;
                }
                return false;
            }
        });

        //== 날짜 이벤트 ==//
        fr_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, fr_dt, cal1);
            }
        });
        to_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, to_dt, cal2);
            }
        });

        //== 버튼 이벤트 ==//
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prodtorder_no_st = "";

                dbQuery_PRODT_ORDER_INFO(prodtorder_no_st);
                select_PRODT_ORDER_INFO();

                start(prodtorder_no_st);     //조회 버튼을 눌렀을 때는 입력한 수불발생일 기간동안의 모든 제조오더 데이터가 나오도록 설계 되어있음
            }
        });
    }

    private void initializeData() {
        fr_dt.setText(df.format(cal1.getTime()));
        to_dt.setText(df.format(cal2.getTime()));
    }

    private void start(String prodtorder_no_st) {
        dbQuery(prodtorder_no_st);

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            I34_QUERY_ListViewAdapter listViewAdapter = new I34_QUERY_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I34_QUERY item = new I34_QUERY();

                item.PRODT_ORDER_NO     = jObject.getString("PRODT_ORDER_NO");      //제조오더번호
                item.DOCUMENT_DT        = jObject.getString("DOCUMENT_DT");         //수불 발생일
                item.ITEM_CD            = jObject.getString("ITEM_CD");             //품번
                item.ITEM_NM            = jObject.getString("ITEM_NM");             //품명
                item.QTY                = jObject.getString("QTY");                 //출고수량
                item.TRACKING_NO        = jObject.getString("TRACKING_NO");         //Tracking 번호
                item.SL_NM              = jObject.getString("SL_NM");               //공장명
                item.WC_NM              = jObject.getString("WC_NM");               //작업장명

                listViewAdapter.addQueryItem(item);
            }

            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {

                }
            });

            TGSClass.AlertMessage(getApplicationContext(), ja.length() + " 건 조회되었습니다.");
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private void dbQuery(final String prodtorder_no_st) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String fr_dt_st = fr_dt.getText().toString();
                String to_dt_st = to_dt.getText().toString();

                String sql = " EXEC XUSP_MES_PRODT_ORDER_RESERVATION_INFO_GET ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@PRODT_ORDER_NO = '" + prodtorder_no_st + "'";
                sql += " ,@DOCUMENT_DT_FROM = '" + fr_dt_st + "'";
                sql += " ,@DOCUMENT_DT_TO = '" + to_dt_st + "'";
                sql += " ,@ITEM_CD = ''";

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

    private void dbQuery_PRODT_ORDER_INFO(final String prodt_order_no) {
        Thread workThd_dbQuery_PRODT_ORDER_INFO = new Thread() {
            public void run() {
                String fr_dt_st = fr_dt.getText().toString();
                String to_dt_st = to_dt.getText().toString();

                String sql = " EXEC XUSP_MES_PRODT_ORDER_INFO_GET3 ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@PRODT_ORDER_NO = '" + prodt_order_no + "' ";
                sql += " ,@DATE_FR = '" + fr_dt_st + "' ";
                sql += " ,@DATE_TO = '" + to_dt_st + "' ";

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
        workThd_dbQuery_PRODT_ORDER_INFO.start();   //스레드 시작
        try {
            workThd_dbQuery_PRODT_ORDER_INFO.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private void select_PRODT_ORDER_INFO() {
        try {
            final boolean jSonType = TGSClass.isJsonData(sJson);
            if (!jSonType) {
                return;
            }

            JSONArray ja = new JSONArray(sJson);

            JSONObject jObject = ja.getJSONObject(0);

            item_cd.setText(jObject.getString("ITEM_CD"));              // 품번
            item_nm.setText(jObject.getString("ITEM_NM"));              // 품명
            tracking_no.setText(jObject.getString("TRACKING_NO"));      // Tracking
            prodt_qty.setText(jObject.getString("PRODT_ORDER_QTY"));    // 지시량
            remain_qty.setText(jObject.getString("REMAIN_QTY"));        // 잔량

        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }
}