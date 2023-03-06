package com.PDA.Hanmi.S10;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.PDA.Hanmi.BaseActivity;
import com.PDA.Hanmi.DBAccess;
import com.PDA.Hanmi.R;
import com.PDA.Hanmi.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class S12_LOT_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonConfigSet = "", sJsonCombo = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private String tx_req_no,tx_lot_no="";

    //== View 선언(EditText) ==//
    private EditText item_cd, item_nm,lot_no;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int S12_DTL_REQUEST_CODE = 0;

    //== global변수 선언 ==//
    private S12_LOT selected_Lot;

    //== View 선언(Button) ==//
    private Button btn_lot,btn_end;

    private S12_LOT_ListViewAdapter ListViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s12_lot);

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
        tx_req_no       = getIntent().getStringExtra("REQ_NO");

        //== ID값 바인딩 ==//
        item_cd         = (EditText) findViewById(R.id.item_cd);
        item_nm         = (EditText) findViewById(R.id.item_nm);
        listview        = (ListView) findViewById(R.id.listLot);
        lot_no          = (EditText) findViewById(R.id.lot_no);

        item_cd.setText(getIntent().getStringExtra("ITEM_CD"));
        item_nm.setText(getIntent().getStringExtra("ITEM_NM"));

        btn_lot     = (Button) findViewById(R.id.btn_lot);
        btn_end     = (Button) findViewById(R.id.btn_end);

        S12_LOT_ListViewAdapter ListViewAdapter = new S12_LOT_ListViewAdapter();

    }


    private void initializeListener() {
        btn_lot.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(selected_Lot== null){
                    return;
                }
                dbDelete(selected_Lot);
            }
        });
        btn_end.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                finish();
            }
        });
        lot_no.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {

                    String temp=lot_no.getText().toString().replaceFirst(tx_lot_no,"");
                    lot_no.setText(temp);
                    tx_lot_no=lot_no.getText().toString();


                    for(S12_LOT list : (ArrayList<S12_LOT>) ListViewAdapter.getItems()){
                        if(list.LOT_NO.equals(tx_lot_no)){
                            dbDelete(list);
                            break;
                        }
                    }

                    lot_no.requestFocus();
                    start();
                    return true;
                }
                return false;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                S12_LOT vItem = (S12_LOT) parent.getItemAtPosition(position);
                selected_Lot = vItem;

            }
        });

    }

    private void initializeData() {
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

                ListViewAdapter = new S12_LOT_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    S12_LOT item = new S12_LOT();

                    item.ITEM_CD   = jObject.getString("ITEM_CD");               //품목 코드
                    //String DN_NO = jObject.getString("DN_NO");  //출하요청번호
                    item.ITEM_NM    = jObject.getString("ITEM_NM");              //품명

                    item.DN_REQ_NO  = jObject.getString("DN_REQ_NO");            //출하요청번호
                    item.PACKING_NO = jObject.getString("PACKING_NO");           //포장번호
                    item.LOT_NO     = jObject.getString("LOT_NO");               //로트 번호
                    item.SCAN_QTY   = jObject.getString("SCAN_QTY");             //스캔 개수
                    item.CONT_NO    = jObject.getString("CONT_NO");
                    ListViewAdapter.addShipmentHDRItem(item);
                }

                listview.setAdapter(ListViewAdapter);


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
                String sql = " EXEC XUSP_MES_S2002PA2_GET_LOT_ANDROID ";
                sql += "  @PACKING_NO = '" + tx_req_no + "'";
                sql += " ,@DN_REQ_NO = '" + tx_req_no + "'";
                //sql += " ,@CONT_NO = '" + tx_cont_no + "'";
                //sql += " ,@DN_REQ_SEQ = '" + tx_req_seq + "'";
                sql += " ,@ITEM_CD = '" + item_cd.getText().toString() + "'";
                sql += " ,@USER_ID = '" + vPLANT_CD + "'";

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

    //LOT번호 스캔하여 데이터 저장
    private void dbDelete(S12_LOT delLot) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC DBO.XUSP_MES_S2002PA2_DEL_LOT ";
                sql += " @CUD_CHAR = 'D',";
                sql += " @PACKING_NO = '" + delLot.PACKING_NO + "',";
                sql += " @DN_REQ_NO = '" + delLot.DN_REQ_NO + "',";
                sql += " @CONT_NO = '" + delLot.CONT_NO + "',";
                sql += " @LOT_NO = '" + delLot.LOT_NO + "',";
                sql += " @USER_ID = '" + vUSER_ID + "'";
                sql += ";";

                System.out.println("sql:" + sql);
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
            start();

            if (!sJson.equals("")) {
                TGSClass.AlertMessage(getApplicationContext(), "요청하신 LOT내역이 삭제되었습니다.");
            }
        }catch (InterruptedException ex) {

        }
    }
}