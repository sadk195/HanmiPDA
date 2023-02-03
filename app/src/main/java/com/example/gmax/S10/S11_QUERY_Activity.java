package com.example.gmax.S10;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

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

public class S11_QUERY_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonConfigSet = "", sJsonCombo = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(EditText) ==//
    private EditText insrt_dt_fr, insrt_dt_to;

    //== View 선언(Spinner) ==//
    private Spinner cmbPlantCD,cmbShipToParty,cmbSalesGrp,cmbSL_CD;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    private Button btn_open,btn_hide;

    //== View 선언(DrawerLayout) ==//
    private DrawerLayout DrawerView;

    //== Spinner 관련 변수 선언 ==//
    private String str_PlantCD = "",str_ShipToParty = "";
    private String str_SalesGrp = "",str_SL_CD= "";

    //== 날짜 관련 변수 선언 ==//
    private Calendar cal1, cal2;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int S11_DTL_REQUEST_CODE = 0;

    //==포장실적 구분 변수 선언==//
    private boolean PACKAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s11_query);

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
        insrt_dt_fr      = (EditText) findViewById(R.id.insrt_dt_fr);
        insrt_dt_to      = (EditText) findViewById(R.id.insrt_dt_to);
        cmbPlantCD       = (Spinner) findViewById(R.id.cmbPlantCD);
        cmbShipToParty   = (Spinner) findViewById(R.id.cmbShipToParty);
        cmbSalesGrp      = (Spinner) findViewById(R.id.cmbSalesGrp);
        cmbSL_CD         = (Spinner) findViewById(R.id.cmbSL_CD);
        listview         = (ListView) findViewById(R.id.listOrder);
        btn_hide         = (Button) findViewById(R.id.btn_hide);
        btn_open         = (Button) findViewById(R.id.btn_open);
        DrawerView       = (DrawerLayout) findViewById(R.id.drawer);
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
                //start();
            }
        };

        insrt_dt_fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, insrt_dt_fr, cal1);
            }
        });

        insrt_dt_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, insrt_dt_to, cal2);
            }
        });
        insrt_dt_fr.setText(df.format(cal1.getTime()));
        insrt_dt_to.setText(df.format(cal2.getTime()));

        insrt_dt_fr.addTextChangedListener(textWatcher);
        insrt_dt_to.addTextChangedListener(textWatcher);

        btn_open.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerView.openDrawer(Gravity.LEFT);
            }
        });
        btn_hide.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerView.closeDrawer(Gravity.LEFT);
            }
        });
    }

    private void initializeData() {
        //공정 리스트 콤보 값을 초기화 한다.
        dbQuery_getcmbPlantCD();
        dbQuery_getcmbShipToParty();
        dbQuery_getcmbSalesGrp();



        start();
    }

    private void start() {
        //TGSClass.AlertMessage(getApplicationContext(), BP_CD.getOnItemSelectedListener().toString());
        dbQuery();
        if (!sJson.equals("")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                //final ArrayList<String> items = new ArrayList<String>();

                S11_QUERY_ListViewAdapter ListViewAdapter = new S11_QUERY_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    S11_QUERY item = new S11_QUERY();

                    item.DN_REQ_NO              = jObject.getString("DN_REQ_NO");     //출하요청번호
                    item.INSRT_DT               = jObject.getString("INSRT_DT");      //출하요청일
                    item.SHIP_TO_PARTY          = jObject.getString("SHIP_TO_PARTY"); //고객사
                    item.BP_NM                  = jObject.getString("BP_NM");         //고객사명
                    item.SALES_GRP              = jObject.getString("SALES_GRP");     //영업그룹
                    item.SALES_GRP_NM           = jObject.getString("SALES_GRP_NM");  //영업그룹명
                    item.PROMISE_DT             = jObject.getString("PROMISE_DT");    //출고예정일
                    item.ITEM_CNT               = jObject.getString("ITEM_CNT");      //품목건수
                    item.DLVY_DT                = jObject.getString("DLVY_DT");       //납기일
                    ListViewAdapter.addShipmentHDRItem(item);
                }

                listview.setAdapter(ListViewAdapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {

                        S11_QUERY vItem = (S11_QUERY) parent.getItemAtPosition(position);

                        //Toast.makeText(P_OEM11_Activity.this ,vSelectItem,Toast.LENGTH_LONG).show();

                        Intent intent = TGSClass.ChangeView(getPackageName(), S11_DTL_Activity.class);

                        intent.putExtra("QUERY", vItem);
                        intent.putExtra("PACKAGE", PACKAGE);

                        intent.putExtra("DN_REQ_NO", vItem.getDN_REQ_NO());


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

    private void dbQuery() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {
                String sql = "";
                sql += "EXEC DBO.XUSP_MES_S2001MA1_GET_LIST ";
                sql += "@FLAG ='H',";
                sql += "@DN_REQ_NO ='',";
                sql += "@INSRT_DT_FR ='" + insrt_dt_fr.getText().toString()+ "',";
                sql += "@INSRT_DT_TO ='" + insrt_dt_to.getText().toString() + "',";
                sql += "@PLANT_CD ='" + str_PlantCD + "',";
                sql += "@SHIP_TO_PARTY ='" + str_ShipToParty + "',";
                sql += "@SALES_GRP ='" + str_SalesGrp + "',";
                sql += "@SL_CD ='" + str_SL_CD + "',";
                sql += "@USER_ID ='" + vUSER_ID + "'";
                sql += ";";

                dataSaveLog("출하요청 현황 검색","Shipment");
                dataSaveLog(sql,"Shipment");

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

    private void dbQuery_getcmbPlantCD() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery_getComboData = new Thread() {
            public void run() {
                String sql = "SELECT PLANT_CD,PLANT_NM FROM B_PLANT ";
                //sql += " @FLAG= 'cmbBizPartner_HDR' ";
                //sql += " ,@PLANT_CD= '' ";

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
            itemBase.setMINOR_CD("");
            itemBase.setMINOR_NM("");

            lstItem.add(itemBase);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jObject = ja.getJSONObject(i);

                //final String vJOB_CD = jObject.getString("JOB_CD");
                final String vMINOR_CD  = jObject.getString("PLANT_CD");
                final String vMINOR_NM  = jObject.getString("PLANT_NM");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                lstItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, lstItem);

            cmbPlantCD.setAdapter(adapter);
            //로딩시 기본값 세팅
            cmbPlantCD.setSelection(adapter.getPosition(itemBase));
            //콤보박스 클릭 이벤트 정의
            cmbPlantCD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    str_PlantCD = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
                    dbQuery_getcmbSL_CD();
                    //start();
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

    private void dbQuery_getcmbShipToParty() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery_getComboData = new Thread() {
            public void run() {
                String sql = " exec XUSP_TPC_SHIPMENT_GET_COMBODATA";
                sql += " @FLAG = 'cmbBizPartner_HDR'";
                sql += " ,@PLANT_CD = ''";


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

            cmbShipToParty.setAdapter(adapter);
            //로딩시 기본값 세팅
            cmbShipToParty.setSelection(adapter.getPosition(itemBase));
            //콤보박스 클릭 이벤트 정의
            cmbShipToParty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    str_ShipToParty = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();

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

    private void dbQuery_getcmbSalesGrp() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery_getComboData = new Thread() {
            public void run() {
                String sql = " SELECT '' AS CODE,'' AS NAME FROM B_SALES_GRP UNION ALL " +
                        "SELECT SALES_GRP AS CODE,SALES_GRP_NM AS NAME FROM B_SALES_GRP";
                /*sql += " @FLAG = 'cmbSL'";
                sql += " ,@PLANT_CD = '"+str_PlantCD+"'";*/


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

            cmbSalesGrp.setAdapter(adapter);
            //로딩시 기본값 세팅
            cmbSalesGrp.setSelection(adapter.getPosition(itemBase));
            //콤보박스 클릭 이벤트 정의
            cmbSalesGrp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    str_SalesGrp = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();

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

    private void dbQuery_getcmbSL_CD() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery_getComboData = new Thread() {
            public void run() {
                String sql = " exec XUSP_TPC_SHIPMENT_GET_COMBODATA";
                sql += " @FLAG = 'cmbSL'";
                sql += " ,@PLANT_CD = '"+str_PlantCD+"'";

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

            cmbSL_CD.setAdapter(adapter);
            //로딩시 기본값 세팅
            cmbSL_CD.setSelection(adapter.getPosition(itemBase));
            //콤보박스 클릭 이벤트 정의
            cmbSL_CD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    str_SL_CD = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();

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

/*        if (resultCode == RESULT_OK) {
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
        }*/
    }
}