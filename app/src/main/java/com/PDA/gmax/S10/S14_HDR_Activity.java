package com.PDA.gmax.S10;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.DBAccess;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;


public class S14_HDR_Activity extends BaseActivity {
    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(EditText) ==//
    private EditText dn_req_no;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== 키보드를 컨트롤 하기 위한 변수 정의 ==//
    private InputMethodManager imm;

    //== 전역변수 정의 ==//
    private static final float FONT_SIZE = 18;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int S14_DTL_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s14_hdr);

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

        //== 키보드를 컨트롤 하기 위한 변수에 값 넣기 ==//
        imm             = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        //== ID값 바인딩 ==//
        img_barcode     = (ImageView) findViewById(R.id.img_barcode);
        dn_req_no       = (EditText) findViewById(R.id.dn_req_no);
        listview        = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeListener() {

        img_barcode.setOnClickListener(qrClickListener);

        dn_req_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (dn_req_no.getText().toString().equals("")) {
                        TGSClass.AlertMessage(getApplicationContext(), "조회조건 [출하요청번호]는 필수입력사항입니다.");
                        return false;
                    } else {
                        start();

                        // imm.hideSoftInputFromWindow(dn_req_no.getWindowToken(), 0);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void initializeData() {
        if (!getRequeryData.s_14_hdr_dn_req_no.equals("")) {
            dn_req_no.setText(getRequeryData.s_14_hdr_dn_req_no);
            start();
        }
    }

    private void start() {
        String dn_req_no_st = dn_req_no.getText().toString();
        //TGSClass.AlertMessage(getApplicationContext(), BP_CD.getOnItemSelectedListener().toString());
        dbQuery(dn_req_no_st);

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();

            S14_HDR_ListViewAdapter ListViewAdapter = new S14_HDR_ListViewAdapter();

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                S14_HDR item = new S14_HDR();

                item.DN_REQ_NO              = jObject.getString("DN_REQ_NO");           //출하요청번호
                item.PLANT_CD               = jObject.getString("PLANT_CD");            //공장코드
                item.SL_CD                  = jObject.getString("SL_CD");               //창고코드
                item.SL_NM                  = jObject.getString("SL_NM");               //창고명
                item.ITEM_CD                = jObject.getString("ITEM_CD");             //품번
                item.ITEM_NM                = jObject.getString("ITEM_NM");             //품명
                item.REQ_QTY                = jObject.getString("REQ_QTY");             //요청수량
                item.GOOD_ON_HAND_QTY       = jObject.getString("GOOD_ON_HAND_QTY");    //양품수량
                item.LOCATION               = jObject.getString("LOCATION");            //적치장
                item.TRACKING_NO            = jObject.getString("TRACKING_NO");         //적치장
                item.LOCATION_QTY           = jObject.getString("LOCATION_QTY");        //적치장

                item.REQ_QTY2               = jObject.getString("REQ_QTY2");            // 출하요청량
                item.DN_GI_QTY              = jObject.getString("DN_GI_QTY");           // 출하량

                ListViewAdapter.addShipmentHdrItem(item);
            }

            listview.setAdapter(ListViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    getRequeryData grd = new getRequeryData();
                    grd.S14_HDR(dn_req_no.getText().toString());

                    S14_HDR vItem = (S14_HDR) parent.getItemAtPosition(position);

                    Intent intent = TGSClass.ChangeView(getPackageName(), S14_DTL_Activity.class);
                    intent.putExtra("HDR", vItem);

                    startActivityForResult(intent, S14_DTL_REQUEST_CODE);
                }
            });

            TGSClass.AlertMessage(getApplicationContext(), ja.length() + " 건 조회되었습니다.");
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private void dbQuery(final String DN_REQ_NO) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_TPC_SHIPMENT_QUERY_LOCATION_HEADER ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@DN_REQ_NO = '" + DN_REQ_NO + "'";

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case S14_DTL_REQUEST_CODE:
                    String sign = data.getStringExtra("SIGN");
                    if (sign.equals("EXIT")) {
                        Toast.makeText(getApplicationContext(), "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (sign.equals("ADD")) {
                        Toast.makeText(getApplicationContext(), "추가 되었습니다.", Toast.LENGTH_SHORT).show();
                        try {
                            sleep(500);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();;
                        }
                        start();
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case S14_DTL_REQUEST_CODE:
                    // Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}


