package com.example.gmax.S10;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.gmax.BaseActivity;
import com.example.gmax.DBAccess;
import com.example.gmax.GetComboData;
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


public class S16_HDR_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson, sJsonCombo;

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 변수 선언(EditText) ==//
    private EditText dn_req_no, work_fr_dt, work_to_dt;

    //== View 변수 선언(Spinner) ==//
    private Spinner BP_CD;

    //== View 변수 선언(Button) ==//
    private Button btn_query;

    //== View 정의(ListView) ==//
    private ListView listview;

    //== 키보드를 컨트롤 하기 위한 변수 정의 ==//
    private InputMethodManager imm;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal1, cal2;

    //== 전역변수 선언 ==//
    private String sJOB_CD;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int S16_DTL_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s16_hdr);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent에서 값 가져오기 ==//
        vMenuID             = getIntent().getStringExtra("MENU_ID");
        vMenuNm             = getIntent().getStringExtra("MENU_NM");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== ID값 바인딩 ==//
        dn_req_no           = (EditText) findViewById(R.id.dn_req_no);
        work_fr_dt          = (EditText) findViewById(R.id.work_fr_dt);
        work_to_dt          = (EditText) findViewById(R.id.work_to_dt);
        BP_CD               = (Spinner) findViewById(R.id.BP_CD);
        btn_query           = (Button) findViewById(R.id.btn_query);

        listview            = (ListView) findViewById(R.id.listOrder);

        // 키보드를 컨트롤 하기 위해 정의
        imm                 = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
    }

    private void initializeCalendar() {
        cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal1.setTime(new Date());
        cal1.add(Calendar.MONTH, -1);

        cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal2.setTime(new Date());
    }

    private void initializeListener() {

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dn_req_no.getText().toString().equals("")) {
                    TGSClass.AlertMessage(getApplicationContext(), "조회조건 [출하요청번호]는 필수입력사항입니다.");
                } else {
                    start();
                }
            }
        });

        dn_req_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (dn_req_no.getText().toString().equals("")) {
                        TGSClass.AlertMessage(getApplicationContext(), "조회조건 [출하요청번호]는 필수입력사항입니다.");
                        return false;
                    } else {
                        start();
                        dn_req_no.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });

        work_fr_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, work_fr_dt, cal1);
            }
        });
        work_to_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, work_to_dt, cal2);
            }
        });
    }

    private void initializeData() {
        work_fr_dt.setText(df.format(cal1.getTime()));
        work_to_dt.setText(df.format(cal2.getTime()));

        //공정 리스트 콤보 값을 초기화 한다.
        dbQuery_getComboData(sJOB_CD);

        //start();
    }

    private void start() {
        //TGSClass.AlertMessage(getApplicationContext(), BP_CD.getOnItemSelectedListener().toString());
        dbQuery(work_fr_dt.getText().toString(), work_to_dt.getText().toString());

        if (!sJson.equals("[]")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                //final ArrayList<String> items = new ArrayList<String>();
                S16_HDR_ListViewAdapter ListViewAdapter = new S16_HDR_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    S16_HDR item = new S16_HDR();

                    item.SHIP_TO_PARTY_NM       = jObject.getString("SHIP_TO_PARTY_NM");    // 납품처명
                    //item.DN_NO                  = jObject.getString("DN_NO");               // 출하요청번호
                    item.DE_COUNT               = jObject.getString("DE_COUNT");            // Detail 건수
                    item.SHIP_TO_PARTY          = jObject.getString("SHIP_TO_PARTY");       // 납품처 코드
                    item.MOV_TYPE               = jObject.getString("MOV_TYPE");            // 수불유형
                    item.SO_TYPE                = jObject.getString("SO_TYPE");             // 수주형태
                    item.PLANT_CD               = jObject.getString("PLANT_CD");            // 공장코드
                    item.SL_CD                  = jObject.getString("SL_CD");               // 창고코드

                    item.START_DT               = jObject.getString("START_DT");            // 시작일자
                    item.END_DT                 = jObject.getString("END_DT");              // 종료일자

                    item.DN_REQ_NO              = jObject.getString("DN_REQ_NO");           // 출하요청번호
                    item.PROMISE_DT             = jObject.getString("PROMISE_DT");          // 출하예정일

                    item.LOCATION               = jObject.getString("LOCATION");            // 적치장
                    item.TRANS_METH             = jObject.getString("TRANS_METH");          // 운송방법

                    ListViewAdapter.add_Shipment_Hdr_Item(item);
                }

                listview.setAdapter(ListViewAdapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {

                        S16_HDR vItem = (S16_HDR) parent.getItemAtPosition(position);

                        if (!vItem.getLOCATION().equals("출하대기장")) {
                            TGSClass.AlertMessage(getApplicationContext(), "선택하신 품목의 적치장이 [출하대기장]으로 이동되지 않았습니다.");
                        } else {
                            //Toast.makeText(P_OEM11_Activity.this ,vSelectItem,Toast.LENGTH_LONG).show();

                            Intent intent = TGSClass.ChangeView(getPackageName(), S16_DTL_Activity.class);
                            intent.putExtra("HDR", vItem);

                            startActivityForResult(intent, S16_DTL_REQUEST_CODE);
                        }
                    }
                });

                TGSClass.AlertMessage(this, ja.length() + " 건 조회되었습니다.", 1000);
            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage());
            }
        } else {
            TGSClass.AlertMessage(this, "조회내역이 없습니다.");
        }
    }

    //== 조회 쿼리 ==//
    private void dbQuery(final String pStartDate, final String pEndDate) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_dbQuery = new Thread() {
            public void run() {

                String dn_req_no_st = dn_req_no.getText().toString();

                String sql = " EXEC XUSP_TPC_SHIPMENT_S16_QUERY_HEADER ";
                sql += "  @START_DT = '" + pStartDate + "'";
                sql += " ,@END_DT = '" + pEndDate + "'";
                sql += " ,@BP_CD = '" + sJOB_CD + "'";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@DN_REQ_NO = '" + dn_req_no_st + "'";

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

    private void dbQuery_getComboData(final String pJOBCODE) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_dbQuery_getComboData = new Thread() {
            public void run() {
                String sql = " exec XUSP_TPC_SHIPMENT_GET_COMBODATA ";
                sql += " @FLAG= 'cmbBizPartner_HDR' ";
                sql += " ,@PLANT_CD= '' ";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonCombo = dba.SendHttpMessage("GetSQLData", pParms);
            }

        };
        workThd_dbQuery_getComboData.start();   //스레드 시작
        try {
            workThd_dbQuery_getComboData.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }

        try {
            JSONArray ja = new JSONArray(sJsonCombo);

            final ArrayList<GetComboData> lstItem = new ArrayList<>();

            /*기본값 세팅*/
            GetComboData itemBase = new GetComboData();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jObject = ja.getJSONObject(i);

                //final String vJOB_CD = jObject.getString("JOB_CD");
                final String vMINOR_CD = jObject.getString("CODE");
                final String vMINOR_NM = jObject.getString("NAME");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                lstItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, lstItem);

            BP_CD.setAdapter(adapter);
            //로딩시 기본값 세팅
            BP_CD.setSelection(adapter.getPosition(itemBase));
            //콤보박스 클릭 이벤트 정의
            BP_CD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sJOB_CD = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
                    //Start();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (JSONException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        } catch (Exception ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case S16_DTL_REQUEST_CODE:
                    String sign = data.getStringExtra("SIGN");
                    if (sign.equals("EXIT")) {
                        //Toast.makeText(S16_HDR_Activity.this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (sign.equals("ADD")) {
                        //Toast.makeText(S16_HDR_Activity.this, "추가 되었습니다.", Toast.LENGTH_SHORT).show();
                        start();
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case S16_DTL_REQUEST_CODE:
                    // Toast.makeText(S16_HDR_Activity.this, "취소", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}