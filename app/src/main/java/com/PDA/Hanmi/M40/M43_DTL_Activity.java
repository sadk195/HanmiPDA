package com.PDA.Hanmi.M40;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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


public class M43_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "",lJson = "", sJsonConfigSet = "", sJsonCombo = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(EditText) ==//
    private EditText edt_length,edt_width;
    private String tx_QR_Code;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(TextView) ==//
    private TextView lbl_count_scan,lbl_count;

    //== View 선언(Button) ==//
    private Button btn_dtl,btn_end;

    //== View 선언(CheckBox) ==//
    private CheckBox chk_custom;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int M43_QUERY_REQUEST_CODE = 0;

    //== ListView Adapter 선언 ==//
    M43_DTL_ListViewAdapter ListViewAdapter; //데이터를 완전히 초기화 하는것이 아니라 수정처리 하기때문에 전역 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m43_dtl);

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


        //== ID값 바인딩 ==//
        edt_length       = (EditText) findViewById(R.id.length);
        edt_width       = (EditText) findViewById(R.id.width);

        listview    = (ListView) findViewById(R.id.listOrder);

        btn_end    = (Button) findViewById(R.id.btn_end);
        btn_dtl    = (Button) findViewById(R.id.btn_dtl);
        chk_custom  = (CheckBox) findViewById(R.id.custom);

        lbl_count       = (TextView) findViewById(R.id.lbl_count);
        lbl_count_scan  = (TextView) findViewById(R.id.lbl_count_scan);

        //== Adapter 선언 ==//
        ListViewAdapter = new M43_DTL_ListViewAdapter();
    }

    private void initializeListener() {

        btn_dtl.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                for (M40_DTL dtl : ListViewAdapter.getListviewArray()) {
                    if (dtl.getCHK()) {
                        try {
                            dbSave(dtl);
                            String err = "";
                            String err_name = "";
                            if (!sJson.equals("") && sJson.contains("ERR")) {
                                JSONArray ja = new JSONArray(sJson);
                                JSONObject jObject = ja.getJSONObject(0);
                                err = jObject.getString("ERR");
                                err_name = jObject.getString("ERR_NANE");

                            }

                            if (!err.equals("TRUE")) {
                                TGSClass.AlertMessage(getApplicationContext(), err_name,5000);
                            }
                            else if(err.equals("")){
                                TGSClass.AlertMessage(getApplicationContext(), " 오류가 발생하였습니다 다시 스캔하여주십시오",5000);
                                return;
                            }
                            else {
                                TGSClass.AlertMessage(getApplicationContext(), err_name);
                            }
                        }
                        catch (JSONException e) {

                        }
                    }
                    start();
                }
            }

        });

        btn_end.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
               finish();
            }

        });

        edt_length.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    start();
                    return true;
                }
                return false;
            }
        });
        edt_width.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    start();
                    return true;
                }
                return false;
            }
        });

    }

    private void initializeData(){
        start();
    }

    //액티비티 시작,데이터 조회
    private void start() {
        dbQuery();

        ListViewAdapter.setTextView(lbl_count_scan);
        if (!sJson.equals("")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                //final ArrayList<String> items = new ArrayList<String>();

                //M13_DTL_ListViewAdapter ListViewAdapter = new M13_DTL_ListViewAdapter();
                ListViewAdapter.ClearItem();

                for (int idx = 0; idx < ja.length(); idx++) {

                    JSONObject jObject = ja.getJSONObject(idx);

                    M40_DTL item = new M40_DTL();
                    item.setCODE            (jObject.getString("CODE"));
                    item.setAREA_DENSITY    (jObject.getString("AREA_DENSITY"));
                    item.setLOT_NO          (jObject.getString("LOT_NO"));
                    item.setROLL_NO         (jObject.getString("ROLL_NO"));
                    item.setWIDTH           (jObject.getString("WIDTH"));
                    item.setLENGTH          (jObject.getString("LENGTH"));
                    item.setQR_VALUE_ALL    (jObject.getString("QR_VALUE_ALL"));
                    item.setSTATUS          (jObject.getString("STATUS"));

                    ListViewAdapter.addPkgItem(item);
                }
                listview.setAdapter(ListViewAdapter);
                ListViewAdapter.notifyDataSetChanged();

                TGSClass.AlertMessage(getApplicationContext(), ja.length() + " 건 조회되었습니다.");

                //전체 카운트는 1회만 설정, 스캔카운트는 어댑터로 넘겨서 실시간 처리
                lbl_count_scan.setText(String.valueOf(ListViewAdapter.getChkCount()));
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

                String width = edt_width.getText().toString().equals("")?"0":edt_width.getText().toString();
                String length = edt_length.getText().toString().equals("")?"0":edt_length.getText().toString();

                String sql = "SELECT * FROM ZZ_M_BLANKET";
                sql += " WHERE STATUS ='R'";//현재 공장 코드 번호
                sql += " AND WIDTH like '%"+width+"%'";//원단 넓이
                sql += " AND LENGTH like '%"+length+"%'";//원단 길이


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


    //리스트 데이터 조회
    private void dbSave(M40_DTL dtl) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {

                String sql = "EXEC DBO.XUSP_BLANKET_M41_SET_ANDROID ";
                sql += " @CUD_FLAG     ='U',";//현재 공장 코드 번호
                sql += " @CODE	       ='" + dtl.CODE + "',";
                sql += " @AREA_DENSITY ='" + dtl.AREA_DENSITY + "',";
                sql += " @LOT_NO	   ='" + dtl.LOT_NO + "',";
                sql += " @ROLL_NO      ='" + dtl.ROLL_NO + "',";
                sql += " @WIDTH		   ='" + dtl.WIDTH + "',";
                sql += " @LENGTH       ='" + dtl.LENGTH + "',";
                sql += " @QR_VALUE_ALL ='" + dtl.QR_VALUE_ALL + "',";
                sql += " @STATUS       ='O',";
                sql += " @USER_ID      = '" + vUSER_ID + "'";

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case M43_QUERY_REQUEST_CODE:
                    start();
                default:
                    break;
            }
        }
    }


}