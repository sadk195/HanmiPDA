package com.PDA.gmax.I30;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.DBAccess;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class I38_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private I38_HDR vGetHDRItem;

    //== View 선언(TextView) ==//
    private TextView storage_location, location, item_cd, item_nm, wms_qty_sum, req_qty;

    //== View 선언(ListView) ==//
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i38_dtl);

        this.initializeView();

        this.initializeListener();

        this.initializeData();

        start();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID         = getIntent().getStringExtra("MENU_ID");
        vMenuNm         = getIntent().getStringExtra("MENU_NM");
        vMenuRemark     = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand   = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== HDR 값 바인딩 ==//
        vGetHDRItem         = (I38_HDR)getIntent().getSerializableExtra("HDR");

        //== ID 값 바인딩 ==//

        storage_location    = (TextView) findViewById(R.id.storage_location);
        location            = (TextView) findViewById(R.id.location);
        item_cd             = (TextView) findViewById(R.id.item_cd);
        item_nm             = (TextView) findViewById(R.id.item_nm);
        wms_qty_sum         = (TextView) findViewById(R.id.wms_qty_sum);
        req_qty             = (TextView) findViewById(R.id.req_qty);

        listview            = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeListener() {

    }

    private void initializeData() {
        storage_location.setText(vGetHDRItem.getSL_CD());
        location.setText(vGetHDRItem.getLOCATION());
        item_cd.setText(vGetHDRItem.getITEM_CD());
        item_nm.setText(vGetHDRItem.getITEM_NM());
        wms_qty_sum.setText(vGetHDRItem.getWMS_GOOD_ON_HAND_QTY());
        req_qty.setText(vGetHDRItem.getREQ_QTY());  //잔량
    }

    private void start() {
        dbQuery(vGetHDRItem.getITEM_CD(), vGetHDRItem.getSL_CD(), vGetHDRItem.getTRACKING_NO(), vGetHDRItem.getLOCATION());

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            I38_DTL_ListViewAdapter listViewAdapter = new I38_DTL_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I38_DTL item = new I38_DTL();

                item.TRACKING_NO        = jObject.getString("TRACKING_NO");              //Tracking 번호
                item.LOT_NO             = jObject.getString("LOT_NO");                        //Lot번호
                item.GOOD_ON_HAND_QTY   = jObject.getString("GOOD_ON_HAND_QTY");    //양품수량
                item.BAD_ON_HAND_QTY    = jObject.getString("BAD_ON_HAND_QTY");      //불량수량

                item.SL_CD              = jObject.getString("SL_CD");              //창고코드
                item.ITEM_CD            = jObject.getString("ITEM_CD");          //품목코드
                item.LOT_SUB_NO         = jObject.getString("LOT_SUB_NO");    //LOT_SUB_NO
                item.BASIC_UNIT         = jObject.getString("BASIC_UNIT");    //재고단위
                item.LOCATION           = jObject.getString("LOCATION");        //적치장

                listViewAdapter.addDTLItem(item);
            }
            listview.setAdapter(listViewAdapter);

        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, "catch : ex");
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, "catch : e1");
        }
    }

    private void dbQuery(final String item_cd_data, final String sl_cd_data, final String tracking_no_data, final String location_cd) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DTL_GET_LIST_I38 ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@ITEM_CD = '" + item_cd_data + "' ";
                sql += " ,@SL_CD = '" + sl_cd_data + "' ";
                sql += " ,@TRACKING_NO = '" + tracking_no_data + "' ";

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