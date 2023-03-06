package com.PDA.Hanmi.S10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.PDA.Hanmi.BaseActivity;
import com.PDA.Hanmi.DBAccess;
import com.PDA.Hanmi.TGSClass;
import com.PDA.Hanmi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class S11_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonConfigSet = "", sJsonCombo = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private String dn_req_no = "";
    private S11_QUERY vGetQUERYItem;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(LinearLayout) ==//
    private LinearLayout box_view1;

    //== View 선언(Button) ==//
    private Button btn_hide;

    //== View 선언(Textview) ==//
    private TextView spec,sl_nm;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int S11_DTL_REQUEST_CODE = 0;

    //==포장실적 구분 변수 선언==//
    private boolean PACKAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s11_dtl);


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
        PACKAGE         = getIntent().getBooleanExtra("PACKAGE",false); //false => 출하관리,true => 포장실적

        vGetQUERYItem = (S11_QUERY)getIntent().getSerializableExtra("QUERY");

        dn_req_no = getIntent().getStringExtra("DN_REQ_NO");
        //== ID값 바인딩 ==//
        listview        = (ListView) findViewById(R.id.listOrder);
        box_view1       = (LinearLayout) findViewById(R.id.box_view1);
        btn_hide        = (Button) findViewById(R.id.btn_hide);
        spec            = (TextView) findViewById(R.id.spec);
        sl_nm        = (TextView) findViewById(R.id.sl_nm);

    }

    private void initializeListener() {
        btn_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                box_view1.setVisibility(View.GONE);
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

                S11_DTL_ListViewAdapter ListViewAdapter = new S11_DTL_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    S11_DTL item = new S11_DTL();

                    item.ITEM_CD             = jObject.getString("ITEM_CD");           //출하요청번호
                    item.ITEM_NM             = jObject.getString("ITEM_NM");           //출하요청일
                    item.SPEC                = jObject.getString("SPEC");              //고객사
                    item.REQ_QTY             = jObject.getString("REQ_QTY");           //고객사명
                    item.GI_QTY              = jObject.getString("GI_QTY");            //영업그룹
                    item.GOOD_ON_HAND_QTY    = jObject.getString("GOOD_ON_HAND_QTY");  //영업그룹명
                    item.SL_CD               = jObject.getString("SL_CD");             //출고예정일
                    item.SL_NM               = jObject.getString("SL_NM");             //품목건수
                    item.DN_REQ_SEQ          = jObject.getString("DN_REQ_SEQ");        //납기일
                    ListViewAdapter.addShipmentHDRItem(item);
                }

                listview.setAdapter(ListViewAdapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {

                        S11_DTL vItem = (S11_DTL) parent.getItemAtPosition(position);
                        spec.setText(vItem.getSPEC());
                        sl_nm.setText(vItem.getSL_NM());
                        box_view1.setVisibility(View.VISIBLE);

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
                sql += "@FLAG ='D',";
                sql += "@DN_REQ_NO ='"+dn_req_no+"',";
                sql += "@INSRT_DT_FR ='',";
                sql += "@INSRT_DT_TO ='',";
                sql += "@PLANT_CD ='',";
                sql += "@SHIP_TO_PARTY ='',";
                sql += "@SALES_GRP ='',";
                sql += "@SL_CD ='',";
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