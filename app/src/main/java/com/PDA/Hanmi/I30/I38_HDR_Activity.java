package com.PDA.Hanmi.I30;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

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


public class I38_HDR_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(EditText) ==//
    private EditText txt_Scan_prodt_order_no, txt_Scan_item_cd;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode_prodt_order_no, img_barcode_item_cd;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    public Button btn_query;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int I38_DTL_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i38_hdr);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID         = getIntent().getStringExtra("MENU_ID");
        vMenuNm         = getIntent().getStringExtra("MENU_NM");
        vMenuRemark     = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand   = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== ID 값 바인딩 ==//
        txt_Scan_prodt_order_no     = (EditText) findViewById(R.id.txt_Scan_prodt_order_no);
        txt_Scan_item_cd            = (EditText) findViewById(R.id.txt_Scan_item_cd);
        img_barcode_prodt_order_no  = (ImageView) findViewById(R.id.img_barcode_prodt_order_no);
        img_barcode_item_cd         = (ImageView) findViewById(R.id.img_barcode_item_cd);

        btn_query           = (Button) findViewById(R.id.btn_query);

        listview            = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeListener() {
        //== 조회 버튼 이벤트 ==//
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if(txt_Scan_prodt_order_no.getText().toString().equals(""))       //제조오더, 출고창고 조회조건 필수 입력사항
                {
                    TGSClass.AlertMessage(getApplicationContext(), "제조오더 또는 출고창고의 조회조건은 \n필수 입력 사항입니다!");
                    return;
                }
                else
                {
                 */
                start();
            }
        });

        //== 제조오더번호 이벤트 ==//
        txt_Scan_prodt_order_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    txt_Scan_item_cd.requestFocus();
                    return true;
                }
                return false;
            }
        });

        //== 품목 이벤트 ==//
        txt_Scan_item_cd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    start();
                    return true;
                }
                return false;
            }
        });

        //== 바코드 이벤트 ==//
        img_barcode_prodt_order_no.setOnClickListener(qrClickListener);
        img_barcode_item_cd.setOnClickListener(qrClickListener);
    }

    private void initializeData() {
        //Start(); //처음 조회할 시 데이터가 많아서 선태사항이 느린 경우를 대비해 조회조건을 사용자가 원할히 선택한 다음 조회할 수 있도록 조정 (조회 버튼 별도 사용)
    }

    private void start() {
        String txt_Scan_prodt_order_no_st   = txt_Scan_prodt_order_no.getText().toString();
        String txt_Scan_item_cd_st          = txt_Scan_item_cd.getText().toString();

        dbQuery(txt_Scan_prodt_order_no_st, txt_Scan_item_cd_st);

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            I38_HDR_ListViewAdapter listViewAdapter = new I38_HDR_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I38_HDR item = new I38_HDR();

                item.ITEM_CD                = jObject.getString("ITEM_CD");                      //품번
                item.ITEM_NM                = jObject.getString("ITEM_NM");                      //품명
                item.REQ_QTY                = jObject.getString("REQ_QTY");                      //요청량(필요량)
                item.ISSUED_QTY             = jObject.getString("ISSUED_QTY");                //출고량
                item.LOCATION               = jObject.getString("LOCATION");                    //적치장

                item.SL_CD                  = jObject.getString("SL_CD");                          //창고 (intent)
                item.OUT_QTY                = jObject.getString("QTY");                              //현재고
                item.TRACKING_NO            = jObject.getString("TRACKING_NO");              //
                item.PRODT_ORDER_NO         = jObject.getString("PRODT_ORDER_NO");        //
                item.REMAIN_QTY             = jObject.getString("REMAIN_QTY");                //잔량
                item.WMS_GOOD_ON_HAND_QTY   = jObject.getString("WMS_GOOD_ON_HAND_QTY");                //잔량

                listViewAdapter.addHDRItem(item);
            }

            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    I38_HDR vItem = (I38_HDR) parent.getItemAtPosition(position);

                    Intent intent = TGSClass.ChangeView(getPackageName(), I38_DTL_Activity.class);

                    intent.putExtra("HDR", vItem);  // 선택한 리스트를 파라메터로 다음페이지에 넘김.

                    startActivityForResult(intent, I38_DTL_REQUEST_CODE);
                }
            });
            TGSClass.AlertMessage(getApplicationContext(), ja.length() + "건 조회되었습니다.");
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private void dbQuery(final String prodt_order_no, final String item_cd) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_MES_APK_PRODT_OUT_LOCATION_MOVE_I38_QUERY_ANDROID ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "'";
                sql += " ,@PRODT_ORDER_NO = '" + prodt_order_no + "'";
                sql += " ,@ITEM_CD = '" + item_cd + "'";

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case I38_DTL_REQUEST_CODE:
                    String sign = data.getStringExtra("SIGN");
                    if (sign.equals("EXIT")) {
                        TGSClass.AlertMessage(this, "저장 되었습니다.");
                        finish();
                    } else if (sign.equals("ADD")) {
                        TGSClass.AlertMessage(this, "추가 되었습니다.");
                        start();
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case I38_DTL_REQUEST_CODE:
                    // Toast.makeText(I35_HDR_Activity.this, "취소", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}