package com.PDA.Hanmi.M10;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.PDA.Hanmi.BaseActivity;
import com.PDA.Hanmi.DBAccess;
import com.PDA.Hanmi.R;
import com.PDA.Hanmi.TGSClass;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class M12_QUERY_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(TextView) ==//
    private TextView lbl_view_title;

    //== View 선언(EditText) ==//
//    private EditText from_dt, to_dt, inspect_req_no, sl_cd, item_cd, location;

    //== View 선언(ImageView) ==//
//    private ImageView img_barcode_inspect_req_no, img_barcode_sl_cd, img_barcode_item_cd, img_barcode_location;

    //== View 선언(Button) ==//
//    private Button btn_query;

    //== View 선언(ListView) ==//
//    private ListView listview;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal1, cal2;

    //== Material Design ==//
    TextInputLayout pd_dt, inspect_req_no, sl_cd, item_cd, location;
    TextInputEditText date_picker_actions;
    LinearLayout layout_hide_search;
    MaterialButtonToggleGroup toggle_btn;
    MaterialButton btn_query;
    RecyclerView listView;
    M12_QUERY_ViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m12_query);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuNm                     = getIntent().getStringExtra("MENU_NM");
        vMenuRemark                 = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand               = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== ID 값 바인딩 ==//
        lbl_view_title              = (TextView) findViewById(R.id.lbl_view_title);
        /**
        from_dt                     = (EditText) findViewById(R.id.from_dt);
        to_dt                       = (EditText) findViewById(R.id.to_dt);
         */
        inspect_req_no              = findViewById(R.id.inspect_req_no);
        sl_cd                       = findViewById(R.id.sl_cd);
        item_cd                     = findViewById(R.id.item_cd);
        location                    = findViewById(R.id.location);

        /**
        img_barcode_inspect_req_no  = (ImageView) findViewById(R.id.img_barcode_inspect_req_no);
        img_barcode_sl_cd           = (ImageView) findViewById(R.id.img_barcode_sl_cd);
        img_barcode_item_cd         = (ImageView) findViewById(R.id.img_barcode_item_cd);
        img_barcode_location        = (ImageView) findViewById(R.id.img_barcode_location);
        */

        btn_query                   = findViewById(R.id.btn_query);   //생산입고등록

        /**
        listview                    = (ListView) findViewById(R.id.listOrder);
        */

        pd_dt                       = findViewById(R.id.pd_dt);
        date_picker_actions         = findViewById(R.id.date_picker_actions);

        layout_hide_search          = findViewById(R.id.layout_hide_search);
        toggle_btn                  = findViewById(R.id.toggle_btn);

        listView                    = findViewById(R.id.listOrder);
    }

    private void initializeCalendar() {
        simpleDF = new SimpleDateFormat("yyyy-MM-dd");

        cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal1.setTime(new Date());
        cal1.add(Calendar.MONTH, -1);

        cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal2.setTime(new Date());
    }

    private void initializeListener() {
        //== 날짜 이벤트 ==//
        /**
        from_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, from_dt, cal1);
            }
        });
        to_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, to_dt, cal2);
            }
        });
         */

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                start2();
                start();
            }
        });

        date_picker_actions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragRangeDate(v, cal1, cal2);
            }
        });

        toggle_btn.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                switch (checkedId) {
                    case R.id.btn_hide:
                        layout_hide_search.setVisibility(View.GONE);
                        break;
                    case R.id.btn_show:
                        layout_hide_search.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        /**
        img_barcode_inspect_req_no.setOnClickListener(qrClickListener);
        img_barcode_sl_cd.setOnClickListener(qrClickListener);
        img_barcode_item_cd.setOnClickListener(qrClickListener);
        img_barcode_location.setOnClickListener(qrClickListener);
        */
    }

    private void initializeData() {
        lbl_view_title.setText(vMenuNm);

        /**
        from_dt.setText(df.format(cal1.getTime()));
        to_dt.setText(df.format(cal2.getTime()));
        */

        pd_dt.getEditText().setText(simpleDF.format(cal1.getTime()) + " ~ " + simpleDF.format(cal2.getTime()));
    }

    private void start2() {
        toggle_btn.check(R.id.btn_hide);
        String msg = "기간 : " + simpleDF.format(cal1.getTime()) + " and " + simpleDF.format(cal2.getTime()) + ",";
        msg += "요청번호 : " + inspect_req_no.getEditText().getText().toString() + ",";
        msg += "창고 : " + sl_cd.getEditText().getText().toString() + ",";
        msg += "품목 : " + item_cd.getEditText().getText().toString() + ",";
        msg += "적치장 : " + location.getEditText().getText().toString() + "";
        TGSClass.showToast(M12_QUERY_Activity.this, "SUCCESS", "파라미터", "" + msg);
    }

    //== start ==//
    private void start() {
        dbQuery();

        if (!sJson.equals("[]")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                M12_QUERY_ListViewAdapter listViewAdapter = new M12_QUERY_ListViewAdapter(); //

                ArrayList<M12_QUERY> item2 = new ArrayList<>();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    item2.add(new M12_QUERY(jObject.getString("INSPECT_REQ_NO"), jObject.getString("MVMT_RCPT_DT"), jObject.getString("INSPECT_GOOD_QTY"), jObject.getString("INSPECT_BAD_QTY"), jObject.getString("BP_NM")));

                    /**

                    M12_QUERY item = new M12_QUERY();

                    item.INSPECT_REQ_NO     = jObject.getString("INSPECT_REQ_NO");      // 검사요청번호
                    item.MVMT_RCPT_DT       = jObject.getString("MVMT_RCPT_DT");        // 일자
                    item.INSPECT_GOOD_QTY   = jObject.getString("INSPECT_GOOD_QTY");    // 검사수량
                    item.INSPECT_BAD_QTY    = jObject.getString("INSPECT_BAD_QTY");     // 검사불량수량
                    item.BP_NM              = jObject.getString("BP_NM");               // 공급처

                    listViewAdapter.addQueryItem(item);
                    */
                }

                adapter = new M12_QUERY_ViewAdapter(this, item2);

                listView.setLayoutManager(new LinearLayoutManager(this));
                listView.setAdapter(adapter);

                adapter.setOnItemClickListener(new M12_QUERY_ViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        TGSClass.showToast(M12_QUERY_Activity.this, "INFO", "파라미터", String.valueOf(position));
                    }
                });

                /**
                listview.setAdapter(listViewAdapter);
                */

                TGSClass.AlertMessage(getApplicationContext(), ja.length() + " 건 조회 되었습니다.");
            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage());
            }
        } else {
            TGSClass.AlertMessage(getApplicationContext(), "조회할 항목이 없습니다.");
        }
    }

    //== 조회 ==//
    private void dbQuery() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThread_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_APK_QM12_GET_LIST ";
//                sql += "  @PLANT_CD = '" + vPLANT_CD + "'";
                sql += "  @PLANT_CD = 'PS01'";
//                sql += "  ,@START_DT = '" + simpleDF.format(cal1.getTime()) + "'";
//                sql += "  ,@END_DT = '" + simpleDF.format(cal2.getTime()) + "'";
                sql += "  ,@START_DT = '2003-10-04'";
                sql += "  ,@END_DT = '" + simpleDF.format(cal2.getTime()) + "'";
                sql += "  ,@INSP_REQ_NO = '" + inspect_req_no.getEditText().getText().toString() + "'";
                sql += "  ,@SL_CD = '" + sl_cd.getEditText().getText().toString() + "'";
                sql += "  ,@ITEM_CD = '" + item_cd.getEditText().getText().toString() + "'";
                sql += "  ,@LOCATION = '" + location.getEditText().getText().toString() + "'";

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
        workThread_dbQuery.start();   //스레드 시작
        try {
            workThread_dbQuery.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.
        } catch (InterruptedException ex) {

        }
    }
}
