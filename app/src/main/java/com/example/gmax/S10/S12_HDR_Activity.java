package com.example.gmax.S10;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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


public class S12_HDR_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonConfigSet = "", sJsonCombo = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(EditText) ==//
    private EditText work_fr_dt, work_to_dt;

    //== View 선언(Spinner) ==//
    private Spinner BP_CD;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== Spinner 관련 변수 선언 ==//
    private String str_BP_CD="";

    //== 날짜 관련 변수 선언 ==//
    private Calendar cal1, cal2;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int S12_DTL_REQUEST_CODE = 0;

    //==포장실적 구분 변수 선언==//
    private boolean PACKAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s12_hdr);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent에서 값 가져오기 ==//
        vMenuID         = getIntent().getStringExtra("MENU_ID");
        vMenuNm         = getIntent().getStringExtra("MENU_NM");
        vMenuRemark     = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand   = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드
        PACKAGE         = getIntent().getBooleanExtra("PACKAGE",false); //false => 출하관리,true => 포장실적

        //== ID값 바인딩 ==//
        work_fr_dt      = (EditText) findViewById(R.id.work_fr_dt);
        work_to_dt      = (EditText) findViewById(R.id.work_to_dt);
        BP_CD           = (Spinner) findViewById(R.id.BP_CD);
        listview        = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeCalendar() {
        cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal1.setTime(new Date());
        cal1.add(Calendar.MONTH, -1);

        cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal2.setTime(new Date());
    }

    private void initializeListener() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int cnt, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int cnt) {
                if (s.length() > 0) { //do your work here }
                    start();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                start();
            }
        };

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
        work_fr_dt.addTextChangedListener(textWatcher);
        work_to_dt.addTextChangedListener(textWatcher);

        BP_CD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initializeData() {
        //공정 리스트 콤보 값을 초기화 한다.
        dbQuery_getComboData();

        work_fr_dt.setText(df.format(cal1.getTime()));
        work_to_dt.setText(df.format(cal2.getTime()));

        start();
    }

    private void start() {
        //TGSClass.AlertMessage(getApplicationContext(), BP_CD.getOnItemSelectedListener().toString());


        dbQuery(work_fr_dt.getText().toString(), work_to_dt.getText().toString());

        if (!sJson.equals("")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                //final ArrayList<String> items = new ArrayList<String>();

                S12_HDR_ListViewAdapter ListViewAdapter = new S12_HDR_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    S12_HDR item = new S12_HDR();

                    item.SHIP_TO_PARTY_NM   = jObject.getString("SHIP_TO_PARTY_NM");    //납품처명
                    //String DN_NO = jObject.getString("DN_NO");  //출하요청번호
                    item.DE_COUNT           = jObject.getString("DE_COUNT");            //Detail 건수

                    item.SHIP_TO_PARTY      = jObject.getString("SHIP_TO_PARTY");       //납품처코드
                    item.MOV_TYPE           = jObject.getString("MOV_TYPE");            //수주형태
                    item.SO_TYPE            = jObject.getString("SO_TYPE");             //Detail 건수
                    item.PLANT_CD           = jObject.getString("PLANT_CD");            //공장코드
                    item.SL_CD              = jObject.getString("SL_CD");               //창고코드
                    item.TRANS_METH         = jObject.getString("TRANS_METH");          //운송방법

                    item.START_DT           = jObject.getString("START_DT");            //시작일자
                    item.END_DT             = jObject.getString("END_DT");              //종료일자

                    //String SL_CD = jObject.getString("SL_CD");  //납품처
                    //String SL_NM = jObject.getString("SL_NM");  //납품처
                    item.DN_REQ_NO          = jObject.getString("DN_REQ_NO");           //출하요청번호
                    item.PROMISE_DT         = jObject.getString("PROMISE_DT");          //출하예정일

                    ListViewAdapter.addShipmentHDRItem(item);
                }

                listview.setAdapter(ListViewAdapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {

                        S12_HDR vItem = (S12_HDR) parent.getItemAtPosition(position);

                        //Toast.makeText(P_OEM11_Activity.this ,vSelectItem,Toast.LENGTH_LONG).show();

                        Intent intent = TGSClass.ChangeView(getPackageName(), S12_DTL_Activity.class);
                        intent.putExtra("HDR", vItem);
                        intent.putExtra("PACKAGE", PACKAGE);

                        startActivityForResult(intent, 0);
                    }
                });
                TGSClass.AlertMessage(getApplicationContext(), ja.length() + " 건 조회되었습니다.");
                /*
                String vCountText = String.valueOf(ja.length()) + " 건 / " + selectStartDate.getText().toString();
                slbl_count.setText(vCountText);
                 */
            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage());
            }
        }
    }

    private void dbQuery(final String pStartDate, final String pEndDate) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_TPC_SHIPMENT_QUERY_HEADER ";
                sql += "  @START_DT = '" + pStartDate + "'";
                sql += " ,@END_DT = '" + pEndDate + "'";
                sql += " ,@BP_CD = '" + str_BP_CD + "'";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "'";

                System.out.println("sql:"+sql);
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
        wkThd_dbQuery.start();   //스레드 시작
        try {
            wkThd_dbQuery.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private void dbSave_setConfig(final String pUnitCD, final String pConfigID, final String pConfigValue, final String pRemark) {
        Thread workingThread3 = new Thread() {
            public void run() {
                String sql = "DECLARE  @RTN_MSG NVARCHAR(200)=''";
                sql += "EXEC XUSP_AND_P_UNIT_CONFIG  ";
                sql += "  @FLAG = 'SET'";
                sql += " ,@PLANT_CD = '" + global.getPlantCDString() + "'";
                sql += " ,@CONFIG_ID = '" + pConfigID + "'";
                sql += " ,@CONFIG_VALUE = '" + pConfigValue + "'";
                sql += " ,@UNIT_CD = '" + pUnitCD + "'";
                sql += " ,@REMARK = '" + pRemark + "'";
                sql += " ,@RTN_MSG =  @RTN_MSG  OUTPUT";
                sql += " SELECT  @RTN_MSG AS RTN_MSG ";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonConfigSet = dba.SendHttpMessage("SetSQLSave", pParms);
                String vMSG = "";
                String vStatus = "";

                try {
                    JSONArray ja = new JSONArray(sJsonConfigSet);

                    if (ja.length() > 0) {
                        JSONObject jObject = ja.getJSONObject(0);

                        vMSG = !jObject.isNull("RTN_MSG") ? jObject.getString("RTN_MSG") : "";
                        vStatus = !jObject.isNull("STATUS") ? jObject.getString("STATUS") : "";
                    }
                    if (!vStatus.equals("OK")) {
                        TGSClass.AlertMessage(S12_HDR_Activity.this, vMSG);
                        return;
                    }
                } catch (JSONException ex) {
                    TGSClass.AlertMessage(S12_HDR_Activity.this, ex.toString());
                    return;
                }
            }
        };
        workingThread3.start();   //스레드 시작
        try {
            workingThread3.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private void dbQuery_getComboData() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery_getComboData = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_TPC_SHIPMENT_GET_COMBODATA ";
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
        wkThd_dbQuery_getComboData.start();   //스레드 시작
        try {
            wkThd_dbQuery_getComboData.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

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
                final String vMINOR_CD  = jObject.getString("CODE");
                final String vMINOR_NM  = jObject.getString("NAME");

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
                    str_BP_CD = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();

                    start();
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
                case S12_DTL_REQUEST_CODE:
                    String sign = data.getStringExtra("SIGN");
                    if (sign.equals("EXIT")) {
                        Toast.makeText(getApplicationContext(), "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (sign.equals("ADD")) {
                        Toast.makeText(getApplicationContext(), "추가 되었습니다.", Toast.LENGTH_SHORT).show();
                        start();
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case S12_DTL_REQUEST_CODE:
                    // Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}