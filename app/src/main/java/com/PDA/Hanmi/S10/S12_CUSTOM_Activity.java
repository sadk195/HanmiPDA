package com.PDA.Hanmi.S10;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.PDA.Hanmi.BaseActivity;
import com.PDA.Hanmi.DBAccess;
import com.PDA.Hanmi.R;
import com.PDA.Hanmi.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class S12_CUSTOM_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonConfigSet = "", sJsonCombo = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private String tx_lot_no="",tx_carton_no="",tx_req_no;

    //== View 선언(EditText) ==//
    private EditText lot_qty,lot_no,carton_no;

    //== View 선언(ListView) ==//
    private ListView listview;

   // private TextView lbl_count_scan;
    private TextView lbl_count;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int LOT_SEARCH_REQUEST_CODE = 2;

    //== M13_DTL 관련 변수 ==//
    private ArrayList<S12_CUSTOM> Lot_info;
    private int selected_idx,scan_qty,lot_idx;
    private S12_CUSTOM_ListViewAdapter ListViewAdapter;

    //== View 선언(Button) ==//
    private Button btn_lot,btn_end,btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s12_custom);

        this.initializeView();

        this.initializeListener();

        //this.initializeData();

    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent에서 값 가져오기 ==//
        vMenuID         = getIntent().getStringExtra("MENU_ID");
        vMenuNm         = getIntent().getStringExtra("MENU_NM");
        vMenuRemark     = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand   = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        tx_carton_no    = getIntent().getStringExtra("CARTON_NO");
        tx_req_no       = getIntent().getStringExtra("REQ_NO");
        //== ID값 바인딩 ==//

        listview        = (ListView) findViewById(R.id.listLot);
        lot_no          = (EditText) findViewById(R.id.lot_no);
        lot_qty         = (EditText) findViewById(R.id.lot_qty);
        carton_no         = (EditText) findViewById(R.id.carton_no);

        btn_lot         = (Button) findViewById(R.id.btn_lot);
        btn_end         = (Button) findViewById(R.id.btn_end);
        btn_search      = (Button) findViewById(R.id.btn_search);

        lbl_count       = (TextView) findViewById(R.id.lbl_count);
        //lbl_count_scan  = (TextView) findViewById(R.id.lbl_count_scan);

        //== EDIT_TEXT 값 지정 ==//
        carton_no.setText(tx_carton_no);

    }


    private void initializeListener() {
        lot_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {

                    //바코드 입력시 텍스트 리프레시를 위해서 설정

                    String temp = lot_no.getText().toString().replaceFirst(tx_lot_no, "");
                    //temp = "M1000221103001001001";
                    lot_no.setText(temp);
                    tx_lot_no = lot_no.getText().toString();


                    start();
                    return true;
                }
                return false;
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                //데이터 담아서 팝업(액티비티) 호출
                Intent intent = new Intent(getApplicationContext(), S12_LOT_SEARCH_Activity.class);
                //intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 2);

            }
        });

        btn_end.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                if(ListViewAdapter == null){
                    Intent resultIntent = new Intent();
                    setResult(RESULT_CANCELED, resultIntent);

                    finish();
                    return;//리턴처리 하지 않으면 UI쓰레드와의 속도차이로 뒤로 프로세스 흐름
                }
                if(ListViewAdapter.getCount() == 0){

                    Intent resultIntent = new Intent();
                    setResult(RESULT_CANCELED, resultIntent);

                    finish();
                    return;//리턴처리 하지 않으면 UI쓰레드와의 속도차이로 뒤로 프로세스 흐름
                }

                if(!setLotChk()){
                    return;
                }


                for(S12_CUSTOM dtl : ListViewAdapter.getLotArray()){
                    if(dtl.CHK){
                        System.out.println("tx_carton_no:"+tx_carton_no);
                        tx_carton_no = dbSave(tx_req_no,tx_carton_no, dtl.LOT_NO);

                    }
                }
                System.out.println("tx_carton_no:"+tx_carton_no);
                if(tx_carton_no.equals("")){
                    TGSClass.AlertMessage(getApplicationContext(), "오류가 발생하였습니다.");
                    return;
                }

                // 저장 후 결과 값 돌려주기
                Intent resultIntent = new Intent();
                // 결과처리 후 부른 Activity에 보낼 값
                //resultIntent.putExtra("LOT", Lot_info);
                resultIntent.putExtra("CONT_NO", tx_carton_no);

                // 부른 Activity에게 결과 값 반환
                setResult(RESULT_OK, resultIntent);
                // 현재 Activity 종료
                finish();
            }
        });
    }

    private void start() {
        dbQuery();

        if (!sJson.equals("")) {
            System.out.println("SJSON:"+sJson);
            try {
                JSONArray ja = new JSONArray(sJson);

                ListViewAdapter = new S12_CUSTOM_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {

                    JSONObject jObject = ja.getJSONObject(idx);

                    S12_CUSTOM item = new S12_CUSTOM();

                    if(jObject.getString("ASSIGN").equals("Y")){
                        continue;
                    }

                    item.ITEM_CD            = jObject.getString("ITEM_CD");        //품번
                    item.ITEM_NM            = jObject.getString("ITEM_NM");        //품명
                    item.LOT_NO             = jObject.getString("LOT_NO");         //LOT번호
                    item.LOT_QTY            = jObject.getString("LOT_QTY");        //LOT수량
                    item.GOOD_QTY           = jObject.getString("GOOD_QTY");       //양품수량
                    ListViewAdapter.addShipmentHDRItem(item);
                }
                listview.setAdapter(ListViewAdapter);
                ListViewAdapter.notifyDataSetChanged();

                //전체 카운트는 1회만 설정, 스캔카운트는 어댑터로 넘겨서 실시간 처리
                lbl_count.setText(String.valueOf(ListViewAdapter.getCount()));
            } catch (JSONException ex) {

                TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage());
            }
        }

    }

    //리스트 데이터 조회
    private void dbQuery() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {

                String sql = "EXEC DBO.XUSP_MES_S2002PA2_GET_LOT_CUSTOM ";
                sql += "@FLAG ='L',"; //
                sql += "@ITEM_CD ='',"; //
                sql += "@LOT_NO ='"+tx_lot_no+"',"; //
                sql += "@PLANT_CD ='" + vPLANT_CD +"',"; //carton 번호 조회시 빈값으로 조회
                sql += "@USER_ID ='" + vUSER_ID + "'";
                sql += ";";


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


    //LOT번호 스캔하여 데이터 저장
    private String dbSave(final String pReqNo, final String pCartonNo, final String pLotNo) {
        String result_carton="";
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC DBO.XUSP_MES_S2002PA2_SET_LOT_CUSTOM ";
                sql += " @CUD_CHAR = 'C',";
                sql += " @PACKING_NO = '" + pReqNo + "',";
                sql += " @DN_REQ_NO = '" + pReqNo + "',";
                sql += " @CONT_NO = '" + pCartonNo + "',";
                sql += " @LOT_NO = '" + pLotNo + "',";
                sql += " @USER_ID = '" + vUSER_ID + "'";
                sql += ";";
                System.out.println("lot save : " + sql);

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

                    for (int idx = 0; idx < ja.length(); idx++) {

                        JSONObject jObject = ja.getJSONObject(idx);

                        S12_CUSTOM item = new S12_CUSTOM();

                        result_carton = jObject.getString("CONT_NO");        //품번

                    }

                } catch (JSONException ex) {
                    TGSClass.AlertMessage(this, ex.getMessage());
                } catch (Exception e1) {
                    TGSClass.AlertMessage(this, e1.getMessage());
                }
            }

        } catch (InterruptedException ex) {

        }
        return result_carton;
    }


    //로트번호 수기등록 체크
    //체크된 데이터 확인 및 입력한 수량만큼 로트 체크
    public  boolean setLotChk(){

        //BOX수가 입력되어 있는지 체크
        if(lot_qty.getText().equals("")){
            TGSClass.AlertMessage(this, "BOX수를 입력해주세요");
            return false;
        }

        int qty = Integer.parseInt(String.valueOf(lot_qty.getText()));

        //남은수보다 BOX수가 많을경우 리턴
        if(ListViewAdapter.getUnChkCount() < qty){
            TGSClass.AlertMessage(this, "남은 수량과 BOX수량이 다릅니다");
            return false;
        }

        //수량 입력시 체크되지 않은 항목을 입력한 수량만큼 체크 표시
        ArrayList<S12_CUSTOM> arrayList = ListViewAdapter.getLotArray();

        int cnt = 0;
        dataSaveLog("수기등록 로드체크수// "+qty,"CKD_IN");

        for(int i = 0; i < arrayList.size() && cnt < qty; i++){
            S12_CUSTOM dtl = arrayList.get(i);

            if(!dtl.getCHK()){
                dtl.setCHK(true);
                ListViewAdapter.updatePkgItem(i,dtl);
                cnt++;
            }
            else{

            }

        }
        dataSaveLog("수기등록 로드체크 완료수// "+cnt,"CKD_IN");

        ListViewAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case LOT_SEARCH_REQUEST_CODE:
                if(resultCode==RESULT_OK){
                    //데이터 받기
                    String result = data.getStringExtra("result");
                    lot_no.setText(result);
                    tx_lot_no = result;
                    start();
                }

                break;
        }
    }

}