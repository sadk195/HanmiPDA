package com.PDA.Hanmi.I10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.PDA.Hanmi.BaseActivity;
import com.PDA.Hanmi.DBAccess;
import com.PDA.Hanmi.TGSClass;
import com.PDA.Hanmi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class I11_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private I11_HDR vGetHDRItem;

    //== View 선언(TextView) ==//
    private TextView storage_location, location, item_cd, item_nm, good_on_hand_qty;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    private Button btn_menu, btn_choice_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i11_dtl);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID             = getIntent().getStringExtra("MENU_ID");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("START_COMMAND");

        //== ID 값 바인딩 ==//
        storage_location    = (TextView) findViewById(R.id.storage_location);
        location            = (TextView) findViewById(R.id.location);
        item_cd             = (TextView) findViewById(R.id.item_cd);
        item_nm             = (TextView) findViewById(R.id.item_nm);
        good_on_hand_qty    = (TextView) findViewById(R.id.good_on_hand_qty);

        listview            = (ListView) findViewById(R.id.listOrder);
        btn_menu            = (Button) findViewById(R.id.btn_menu);             //재고이동 현황 조회 선택 페이지 이동
        btn_choice_query    = (Button) findViewById(R.id.btn_choice_query);     //조회할 재고 선택 페이지 이동

        //== HDR에서 넘긴 값 받기 ==//
        vGetHDRItem = (I11_HDR) getIntent().getSerializableExtra("HDR");
    }

    private void initializeListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 저장 후 결과 값 돌려주기
                Intent resultIntent = new Intent();
                switch (v.getId()) {
                    case R.id.btn_menu:
                        // 결과처리 후 부른 Activity에 보낼 값
                        resultIntent.putExtra("SIGN", "EXIT");
                        break;
                    case R.id.btn_choice_query:
                        // 결과처리 후 부른 Activity에 보낼 값
                        resultIntent.putExtra("SIGN", "BACK");
                        break;
                    default:
                        break;
                }
                // 부른 Activity에게 결과 값 반환
                setResult(RESULT_OK, resultIntent);
                // 현재 Activity 종료
                finish();
            }
        };
        btn_menu.setOnClickListener(clickListener);
        btn_choice_query.setOnClickListener(clickListener);
    }

    private void initializeData() {
        //== 데이터 바인딩 ==//
        storage_location.setText(vGetHDRItem.getSL_CD());
        location.setText(vGetHDRItem.getLOCATION());
        item_cd.setText(vGetHDRItem.getITEM_CD());
        item_nm.setText(vGetHDRItem.getITEM_NM());
        good_on_hand_qty.setText(vGetHDRItem.getGOOD_ON_HAND_QTY());

        start();
    }

    private void start() {

        dbQuery(vGetHDRItem.getPLANT_CD(), vGetHDRItem.getITEM_CD(), vGetHDRItem.getSL_CD(), vGetHDRItem.getTRACKING_NO(), vGetHDRItem.getLOCATION());

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            I11_DTL_ListViewAdapter listViewAdapter = new I11_DTL_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I11_DTL item = new I11_DTL();

                item.TRACKING_NO        = jObject.getString("TRACKING_NO");         //Tracking 번호
                item.LOT_NO             = jObject.getString("LOT_NO");              //Lot번호
                item.GOOD_ON_HAND_QTY   = jObject.getString("GOOD_ON_HAND_QTY");    //양품수량
                item.BAD_ON_HAND_QTY    = jObject.getString("BAD_ON_HAND_QTY");     //불량수량

                item.SL_CD              = jObject.getString("SL_CD");               //창고코드
                item.ITEM_CD            = jObject.getString("ITEM_CD");             //품목코드
                item.LOT_SUB_NO         = jObject.getString("LOT_SUB_NO");          //LOT_SUB_NO
                item.BASIC_UNIT         = jObject.getString("BASIC_UNIT");          //재고단위
                item.LOCATION           = jObject.getString("LOCATION");            //재고단위

                listViewAdapter.addDTLItem(item);
            }
            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {

                }
            });
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private void dbQuery(final String plant_cd_data, final String item_cd_data, final String sl_cd_data, final String tracking_no_data, final String location_cd) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DTL_GET_LIST_I11 ";
                sql += " @PLANT_CD = '" + plant_cd_data + "' ";
                sql += " ,@ITEM_CD = '" + item_cd_data + "' ";
                sql += " ,@SL_CD = '" + sl_cd_data + "' ";
                sql += " ,@TRACKING_NO = '" + tracking_no_data + "' ";
                sql += " ,@LOCATION_CD = '" + location_cd + "'";

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
}

