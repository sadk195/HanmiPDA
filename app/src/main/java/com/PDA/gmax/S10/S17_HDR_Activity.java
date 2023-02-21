package com.PDA.gmax.S10;

import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.DBAccess;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class S17_HDR_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJson_shipment_status ="";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 변수 선언(EditText) ==//
    private EditText sl_cd;

    //== View 변수 선언(ToggleButton) ==//
    private ToggleButton tg_group;

    //== View 정의(ListView) ==//
    private ListView listview;

    //== 키보드를 컨트롤 하기 위한 변수 정의 ==//
    private InputMethodManager imm;

    //== 전역변수 선언 ==//
    private String sJOB_CD;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int S17_DTL_REQUEST_CODE = 0;

    //== Dialog 선언 ==//
    private Dialog myDialog_shipment_status;

    //== Dialog View 선언(TextView) ==//
    private TextView app_title;

    //== Dialog View 선언(ListView) ==//
    private ListView listOrder_shipment_status;

    //== Dialog View 선언(Button) ==//
    private Button btn_cancle;

    public String popup_flag = "";
    public String popup_tittle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s17_hdr);

        //== Dialog 생성자 ==//
        myDialog_shipment_status = new Dialog(this);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent에서 값 가져오기 ==//
        vMenuID             = getIntent().getStringExtra("MENU_ID");
        vMenuNm             = getIntent().getStringExtra("MENU_NM");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== ID값 바인딩 ==//
        img_barcode         = (ImageView) findViewById(R.id.img_barcode);
        sl_cd               = (EditText) findViewById(R.id.sl_cd);

        tg_group            = (ToggleButton) findViewById(R.id.tg_group);

        listview            = (ListView) findViewById(R.id.listOrder);

        // 키보드를 컨트롤 하기 위해 정의
        imm                 = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        //== Dialog ID값 바인딩 ==//
        app_title                   = (TextView) myDialog_shipment_status.findViewById(R.id.app_title);
        listOrder_shipment_status   = (ListView) myDialog_shipment_status.findViewById(R.id.listOrder_shipment_status);
        btn_cancle                  = (Button) myDialog_shipment_status.findViewById(R.id.btn_cancle);
    }

    private void initializeListener() {

        img_barcode.setOnClickListener(qrClickListener);

        sl_cd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (sl_cd.getText().toString().equals("")) {
                        TGSClass.AlertMessage(getApplicationContext(), "조회조건 [창고코드]는 필수입력사항입니다.");
                        return false;
                    } else {
                        start();
                        return true;
                    }
                }
                return false;
            }
        });

        //토글버튼 이벤트 정의 (소형 / 미니 선택)
        tg_group.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //선택 한 설정 값을 저장 한다.
                String vRemark;

                if(isChecked == false) {      //현재상태가 출고현황일때
                    vRemark = tg_group.getTextOn().toString();
                    //글로벌 = ()
                    popup_flag = "not_yet_shipped_status"; //미출고현황으로 팝업조회
                    popup_tittle = "미출하 요청 현황 팝업";
                    tg_group.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorRed));
                } else {              //현재상태가 미출고현황일때
                    vRemark = tg_group.getTextOff().toString();
                    //글로벌 = ()
                    popup_flag = "shipment_status"; //출고현황으로 팝업조회
                    popup_tittle = "출하 요청 현황 팝업";
                    tg_group.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorBlue));
                }
            }
        });
    }

    private void initializeData() {

        tg_group.setChecked(true);
        popup_flag = "shipment_status";
        popup_tittle = "출하 요청 현황 팝업";
    }

    private void start() {
        String sl_cd_st = sl_cd.getText().toString();
        //TGSClass.AlertMessage(getApplicationContext(), BP_CD.getOnItemSelectedListener().toString());
        dbQuery(sl_cd_st);

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();

            S17_HDR_ListViewAdapter ListViewAdapter = new S17_HDR_ListViewAdapter();

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                S17_HDR item = new S17_HDR();

                item.ITEM_CD                = jObject.getString("ITEM_CD");                 //품번
                item.ITEM_NM                = jObject.getString("ITEM_NM");                 //품명
                item.WMS_QTY                = jObject.getString("WMS_QTY");                 //창고코드
                item.GOOD_ON_HAND_QTY       = jObject.getString("GOOD_ON_HAND_QTY");        //창고명
                item.REQ_QTY                = jObject.getString("REQ_QTY");                 //창고

                ListViewAdapter.addHDRItem(item);
            }

            listview.setAdapter(ListViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {

                    S17_HDR vItem = (S17_HDR) parent.getItemAtPosition(position);
                    String item_cd = vItem.getITEM_CD();

                    myDialog_shipment_status.setContentView(R.layout.activity_s17_shipment_status_popup);
                    shipment_status_start(popup_flag, item_cd);
                    app_title.setText(popup_tittle);

                    btn_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog_shipment_status.dismiss();
                        }
                    });
                    myDialog_shipment_status.show();
                }
            });
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

    private void dbQuery(final String SL_CD) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_TPC_LOCATION_QUERY_S17_HDR ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@SL_CD = '" + SL_CD + "'";

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
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
        }
    }

    private void shipment_status_start(final String popup_flag, final String item_cd) {
        shipment_status_dbQuery(popup_flag, item_cd);

        try {
            JSONArray ja = new JSONArray(sJson_shipment_status);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            S17_Shipment_Status_ListViewAdapter listViewAdapter = new S17_Shipment_Status_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                String SHIP_TO_PARTY_NM         = jObject.getString("SHIP_TO_PARTY_NM");        //적치장
                String DN_REQ_NO                = jObject.getString("DN_REQ_NO");               //품목코드
                String PROMISE_DT               = jObject.getString("PROMISE_DT");              //품목명
                String WMS_GOOD_ON_HAND_QTY     = jObject.getString("WMS_GOOD_ON_HAND_QTY");    //TRAKING_NO

                listViewAdapter.add_Item(SHIP_TO_PARTY_NM, DN_REQ_NO, PROMISE_DT, WMS_GOOD_ON_HAND_QTY);
            }

            listOrder_shipment_status.setAdapter(listViewAdapter);

        } catch (JSONException ex) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
        } catch (Exception e1) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : e1");
        }
    }

    private void shipment_status_dbQuery(final String popup_flag, final String item_cd) {
        Thread wkThd_shipment_status_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_TPC_POPUP_S17_SHIPMENT_STATUS ";
                sql += " @POPUP_FALG = '" + popup_flag + "' ";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@ITEM_CD = '" + item_cd + "' ";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_shipment_status = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        wkThd_shipment_status_dbQuery.start();   //스레드 시작
        try {
            wkThd_shipment_status_dbQuery.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
        }
    }
}