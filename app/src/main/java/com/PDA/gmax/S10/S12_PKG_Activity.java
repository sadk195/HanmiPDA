package com.PDA.gmax.S10;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.PDA.gmax.GetComboData;
import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.DBAccess;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class S12_PKG_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonConfigSet = "", sJsonCombo = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private String tx_req_no,tx_lot_no;

    //== S12_LOT와 주고 받을 변수 선언 ==//
    private S12_LOT s12_lot;
    private S12_PKG vItem;

    //== View 선언(EditText) ==//
    private EditText lot_no,req_no;

    //== View 선언(Spinner) ==//
    private Spinner carton_no;
    private int selected_no = 0;
    private boolean first = true;
    private String cudFlag = "C";

    //== View 선언(ListView) ==//
    private ListView listview;

    //== Spinner 관련 변수 선언 ==//
    private String str_carton_no = "";

    //== View 선언(Button) ==//
    private Button btn_lot,btn_end,btn_custom;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int S12_DTL_REQUEST_CODE = 0;
    private final int S12_CUSTOM_REQUEST_CODE = 1;

    //== ListView Adapter 선언 ==//
    S12_PKG_ListViewAdapter ListViewAdapter; //데이터를 완전히 초기화 하는것이 아니라 수정처리 하기때문에 전역 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s12_pkg);

        this.initializeView();

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

        //== DTL 값 바인딩 ==//
        tx_req_no     = getIntent().getStringExtra("REQ_NO");
        tx_lot_no     ="";

        //== ID값 바인딩 ==//
        lot_no      = (EditText) findViewById(R.id.lot_no);
        req_no      = (EditText) findViewById(R.id.req_no);
        carton_no   = (Spinner) findViewById(R.id.carton_no);
        listview    = (ListView) findViewById(R.id.listPacking);

        btn_custom = (Button) findViewById(R.id.btn_custom);
        btn_lot     = (Button) findViewById(R.id.btn_lot);
        btn_end     = (Button) findViewById(R.id.btn_end);

        //== LOT내역 저장용 클래스 선언 ==//
        s12_lot = new S12_LOT();

        //== Adapter 선언 ==//
        ListViewAdapter = new S12_PKG_ListViewAdapter();
    }

    private void initializeListener() {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                vItem = (S12_PKG) parent.getItemAtPosition(position);
                ListViewAdapter.notifyDataSetChanged();
                //parent.getItemAtPosition(position)
                //parent.setBackgroundColor(Color.parseColor("#00D8FF"));
            }
        });

        btn_lot.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(vItem== null){
                    TGSClass.AlertMessage(getApplicationContext(),"먼저 LOT 품목을 선택해 주세요");

                    return;
                }
                Intent intent = TGSClass.ChangeView(getPackageName(), S12_LOT_Activity.class);
                intent.putExtra("REQ_NO", req_no.getText().toString());
                intent.putExtra("ITEM_CD", vItem.ITEM_CD );
                intent.putExtra("ITEM_NM", vItem.ITEM_NM);
                startActivityForResult(intent, 0);
            }
        });
        btn_end.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                finish();
            }
        });

        carton_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        lot_no.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {

                    String temp=lot_no.getText().toString().replaceFirst(tx_lot_no,"");
                    lot_no.setText(temp);
                    tx_lot_no=lot_no.getText().toString();

                    dbSave(req_no.getText().toString(), str_carton_no,lot_no.getText().toString());

                    lot_no.requestFocus();
                    start();
                    return true;
                }
                return false;
            }
        });
        btn_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TGSClass.ChangeView(getPackageName(), S12_CUSTOM_Activity.class);
                intent.putExtra("REQ_NO", tx_req_no);
                intent.putExtra("CARTON_NO",str_carton_no);
                startActivityForResult(intent, 1);
            }
        });

    }

    private void initializeData() {
        //공정 리스트 콤보 값을 초기화 한다.
        dbQuery_getComboData(false);

        req_no.setText(tx_req_no);

        start();
    }

    //액티비티 시작,데이터 조회
    private void start() {
        //TGSClass.AlertMessage(getApplicationContext(), BP_CD.getOnItemSelectedListener().toString());
        //TGSClass.AlertMessage(getApplicationContext(), vPLANT_CD);
        dbQuery(req_no.getText().toString(), str_carton_no);

        if (!sJson.equals("")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                //final ArrayList<String> items = new ArrayList<String>();

                //S12_PKG_ListViewAdapter ListViewAdapter = new S12_PKG_ListViewAdapter();
                ListViewAdapter.ClearItem();

                for (int idx = 0; idx < ja.length(); idx++) {

                    JSONObject jObject = ja.getJSONObject(idx);

                    S12_PKG item = new S12_PKG();

                    item.ITEM_CD            = jObject.getString("ITEM_CD");
                    //품번

                    //String DN_NO = jObject.getString("DN_NO");  //출하요청번호
                    item.ITEM_NM            = jObject.getString("ITEM_NM");         //품명
                    item.REQ_QTY            = jObject.getString("REQ_QTY");         //요청수
                    //item.REQ_STOCK          = jObject.getString("REQ_STOCK");       //요청잔량
                    item.PACKING_CNT        = jObject.getString("PACKING_CNT");     //박스수
                    item.QTY                = jObject.getString("QTY");             //포장수
                    item.CARTON_NO          = jObject.getString("CONT_NO");

                    ListViewAdapter.addShipmentPKGItem(item);
                }
                listview.setAdapter(ListViewAdapter);
                ListViewAdapter.notifyDataSetChanged();

            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage(),5000);
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage(),5000);
            }
        }
    }

    //LOT번호 스캔하여 데이터 저장
    private void dbSave(final String pReqNo, final String pCartonNo, final String pLotNo) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC DBO.XUSP_MES_S2002PA2_SET_LOT ";
                sql += " @CUD_CHAR = '"+cudFlag+"',";
                sql += " @PACKING_NO = '" + pReqNo + "',";
                sql += " @DN_REQ_NO = '" + pReqNo + "',";
                sql += " @CONT_NO = '" + pCartonNo + "',";
                sql += " @LOT_NO = '" + pLotNo + "',";
                sql += " @USER_ID = '" + vUSER_ID + "'";
                sql += ";";
                System.out.println("lot save : "+sql);

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

            if (!sJson.equals("")) {
                try {
                    JSONArray ja = new JSONArray(sJson);
                    System.out.println("sjson:"+sJson);
                    for (int idx = 0; idx < ja.length(); idx++) {
                        JSONObject jObject = ja.getJSONObject(idx);

                        S12_PKG item = new S12_PKG();

                        item.ITEM_CD            = jObject.getString("ITEM_CD");         //품번
                        //String DN_NO = jObject.getString("DN_NO");  //출하요청번호
                        //item.ITEM_NM            = jObject.getString("ITEM_NM");         //품명
                        //item.REQ_QTY            = jObject.getString("REQ_QTY");         //요청수
                        //item.REQ_STOCK          = jObject.getString("REQ_STOCK");       //요청잔량
                        item.PACKING_CNT        = jObject.getString("PACKING_CNT");     //박스수
                        item.QTY                = jObject.getString("GI_QTY");             //포장수
                        item.CARTON_NO          = jObject.getString("CONT_NO");

                        //LOT스캔 처리 후 데이터 적용
                        ListViewAdapter.setShipmentItem(item.ITEM_CD,item.PACKING_CNT,item.QTY);
                    }

                    ListViewAdapter.notifyDataSetChanged();

                    //carton번호 spinner 초기화
                    dbQuery_getComboData(true);

                } catch (JSONException ex) {
                    System.out.println(ex.getMessage());
                    //TGSClass.AlertMessage(this, ex.getMessage());
                } catch (Exception e1) {
                    TGSClass.AlertMessage(this, e1.getMessage(),5000);
                }
            }
        } catch (InterruptedException ex) {

        }
    }

    //리스트 데이터 조회
    private void dbQuery(final String pReqNo, final String pCartonNO) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {

                if(selected_no == 0){
                    cudFlag = "C";
                }
                else{
                    cudFlag = "U";
                }

                String sql = "EXEC DBO.XUSP_MES_S2002PA2_GET_LIST_TEST ";
                //String sql = "EXEC DBO.XUSP_MES_S2002PA2_GET_LIST ";
                sql += "@FLAG ='"+cudFlag+"',"; //
                sql += "@DN_REQ_NO ='" + pReqNo + "',";
                sql += "@CONT_NO ='" + pCartonNO +"',"; //carton 번호 조회시 빈값으로 조회
                sql += "@USER_ID ='" + vUSER_ID + "'";
                sql += ";";

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

    //spinner데이터 조회
    private void dbQuery_getComboData(boolean Scan) {
        int CartonCount = 0;
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery_getComboData = new Thread() {
            public void run() {
                String sql = "EXEC DBO.XUSP_MES_S2002PA1_GET_LIST ";
                sql += "@FLAG ='H',"; // MES프로그램 포장실적 왼쪽 1번그리드,등록된 CARTON번호 조회 FLAG 'H'
                sql += "@DN_REQ_NO ='" + tx_req_no + "',";
                sql += "@CONT_NO ='',"; //carton 번호 조회시 빈값으로 조회
                sql += "@USER_ID ='" + vUSER_ID + "'";
                sql += ";";

                System.out.println("combo sql:"+sql);
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
            System.out.println("sJsonCombo:"+sJsonCombo);

            final ArrayList<GetComboData> lstItem = new ArrayList<>();

            /*기본값 세팅*/
            //GetComboData itemBase = new GetComboData();

            //첫번째 항목 빈값으로 처리하고 선택시 모든 데이터 표출되도록 표시
            //MINOR_NM에 SELECT 조건 저장
            GetComboData item = new GetComboData();
            item.setMINOR_CD("");
            item.setMINOR_NM("신규");
            item.setNUM(0);
            lstItem.add(item);

            for (int i = 0; i < ja.length(); i++) {

                CartonCount++;

                JSONObject jObject = ja.getJSONObject(i);

                //final String vJOB_CD = jObject.getString("JOB_CD");
                final String vMINOR_CD  = jObject.getString("CONT_NO");
                final String vMINOR_NM  = jObject.getString("CONT_NO");


                item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);
                item.setNUM(i+1);
                lstItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, lstItem);

            carton_no.setAdapter(adapter);

            //콤보박스 클릭 이벤트 정의
            carton_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    str_carton_no = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
                    selected_no = ((GetComboData) parent.getSelectedItem()).getNUM();

                    start();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            //로딩시 기본값 세팅(사용하지 않음)
            //carton_no.setSelection(adapter.getPosition(itemBase));

            //scan하면
            if(Scan){
                if(selected_no == 0) {
                    carton_no.setSelection(CartonCount);
                    return;
                }
                else{
                    carton_no.setSelection(selected_no);
                }
            }

            //최초1회만 동작
            if(first){
                //등록된 carton이 있을경우 첫번째 carton으로 설정
                //없을경우 신규로 설정
                if(CartonCount>0){
                    carton_no.setSelection(1);
                    str_carton_no="1";
                }
                else{
                    carton_no.setSelection(0);
                    str_carton_no="0";
                }
                first = false;
                return;
            }

            carton_no.setSelection(0);

        } catch (JSONException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        } catch (Exception ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == S12_DTL_REQUEST_CODE){

        }
        switch (requestCode){
            case S12_DTL_REQUEST_CODE:
                dbQuery_getComboData(false);
                start();
                break;
            case S12_CUSTOM_REQUEST_CODE:
                //str_carton_no = data.getStringExtra("CONT_NO");
                selected_no =0;
                dbQuery_getComboData(true);
                start();
                break;
        }
    }
}