package com.PDA.Hanmi.S10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.PDA.Hanmi.BaseActivity;
import com.PDA.Hanmi.DBAccess;
import com.PDA.Hanmi.R;
import com.PDA.Hanmi.String_ListViewAdapter;
import com.PDA.Hanmi.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class S12_LOT_SEARCH_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonConfigSet = "", sJsonCombo = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private String tx_item_cd="",selected="";

    //== View 선언(EditText) ==//
    private EditText item_cd;

    //== View 선언(ListView) ==//
    private ListView listview;

    // private TextView lbl_count_scan;
    private TextView lbl_count;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int M13_DTL_REQUEST_CODE = 0;

    //== M13_DTL 관련 변수 ==//
    private ArrayList<String> Lot_info;

    private String_ListViewAdapter ListViewAdapter;

    //== View 선언(Button) ==//
    private Button btn_search,btn_OK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s12_lotsearch);

        this.initializeView();

        this.initializeListener();

        //this.initializeData();

    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        //this.init();

       //== Intent에서 값 가져오기 ==//
        vMenuID         = getIntent().getStringExtra("MENU_ID");
        vMenuNm         = getIntent().getStringExtra("MENU_NM");
        vMenuRemark     = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand   = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드


        //== ID값 바인딩 ==//

        listview        = (ListView) findViewById(R.id.listOrder);
        item_cd         = (EditText) findViewById(R.id.item_cd);

        btn_search      = (Button) findViewById(R.id.btn_search);
        btn_OK          = (Button) findViewById(R.id.btn_OK);

        lbl_count       = (TextView) findViewById(R.id.lbl_count);
        //lbl_count_scan  = (TextView) findViewById(R.id.lbl_count_scan);


    }


    private void initializeListener() {
        /*item_cd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {

                    //바코드 입력시 텍스트 리프레시를 위해서 설정

                    String temp = item_cd.getText().toString().replaceFirst(tx_item_cd, "");
                    //temp = "M1000221103001001001";
                    item_cd.setText(temp);
                    tx_item_cd = item_cd.getText().toString();

                    start();
                    return true;
                }
                return false;
            }
        });*/

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                selected = (String) parent.getItemAtPosition(position);
                ListViewAdapter.notifyDataSetChanged();
                //parent.getItemAtPosition(position)
                //parent.setBackgroundColor(Color.parseColor("#00D8FF"));
            }
        });

        btn_OK.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                // 저장 후 결과 값 돌려주기
                Intent resultIntent = new Intent();
                // 결과처리 후 부른 Activity에 보낼 값
                //resultIntent.putExtra("LOT", Lot_info);
                resultIntent.putExtra("result",selected );

                // 부른 Activity에게 결과 값 반환
                setResult(RESULT_OK, resultIntent);
                // 현재 Activity 종료
                finish();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                tx_item_cd = item_cd.getText().toString();

                start();
            }
        });
    }

    private void start() {
        dbQuery();

        if (!sJson.equals("")) {
            System.out.println("SJSON:"+sJson);
            try {
                JSONArray ja = new JSONArray(sJson);

                ListViewAdapter = new String_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {

                    JSONObject jObject = ja.getJSONObject(idx);

                    ListViewAdapter.addListItem(jObject.getString("LOT_NO"));
                }
                listview.setAdapter(ListViewAdapter);
                ListViewAdapter.notifyDataSetChanged();

            } catch (JSONException ex) {

                //TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                //TGSClass.AlertMessage(this, e1.getMessage());
            }
        }

    }

    //리스트 데이터 조회
    private void dbQuery() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {

                String sql = "EXEC DBO.XUSP_MES_S2002PA2_GET_LOT_CUSTOM ";
                sql += "@FLAG ='I',"; //
                sql += "@ITEM_CD ='"+tx_item_cd+"',"; //
                sql += "@LOT_NO ='',"; //
                sql += "@PLANT_CD ='',"; //carton 번호 조회시 빈값으로 조회
                sql += "@USER_ID =''";
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

}