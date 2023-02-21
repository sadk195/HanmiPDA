package com.PDA.gmax.M40;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.DBAccess;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class M41_QUERY_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "",lJson = "", sJsonConfigSet = "", sJsonCombo = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(EditText) ==//
    private EditText edt_length,edt_width;
    private String tx_QR_Code;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    private Button btn_dtl,btn_open,btn_hide,btn_query,btn_end;

    //== View 선언(DrawerLayout) ==//
    private DrawerLayout DrawerView;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int M41_QUERY_REQUEST_CODE = 0;

    //== ListView Adapter 선언 ==//
    M41_QUERY_ListViewAdapter ListViewAdapter; //데이터를 완전히 초기화 하는것이 아니라 수정처리 하기때문에 전역 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m41_query);

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
        listview         = (ListView) findViewById(R.id.listOrder);
        btn_end    = (Button) findViewById(R.id.btn_end);

        //== Adapter 선언 ==//
        ListViewAdapter = new M41_QUERY_ListViewAdapter();
    }

    private void initializeListener() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

            }
        });

        btn_end.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

              finish();
            }
        });
    }

    private void initializeData(){
        start();
    }

    //액티비티 시작,데이터 조회
    private void start() {
        dbQuery();
        System.out.println("json:"+sJson);
        if (!sJson.equals("")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                //final ArrayList<String> items = new ArrayList<String>();

                //M13_DTL_ListViewAdapter ListViewAdapter = new M13_DTL_ListViewAdapter();
                ListViewAdapter.ClearItem();

                for (int idx = 0; idx < ja.length(); idx++) {

                    JSONObject jObject = ja.getJSONObject(idx);

                    M41_QUERY item = new M41_QUERY();
                    item.setCODE            (jObject.getString("CODE"));
                    item.setNO          (jObject.getString("NO"));

                    ListViewAdapter.addPkgItem(item);
                }
                listview.setAdapter(ListViewAdapter);
                ListViewAdapter.notifyDataSetChanged();

                TGSClass.AlertMessage(getApplicationContext(), ja.length() + " 건 조회되었습니다.");

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

                String sql = "EXEC DBO.XUSP_BLANKET_M41_GET_ANDROID";
                sql += " @FLAG ='M41_QUERY',";//원단 넓이
                sql += " @WIDTH ='',";//원단 넓이
                sql += " @LENGTH =''";//원단 길이

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
                case M41_QUERY_REQUEST_CODE:
                    start();
                default:
                    break;
            }
        }
    }


}