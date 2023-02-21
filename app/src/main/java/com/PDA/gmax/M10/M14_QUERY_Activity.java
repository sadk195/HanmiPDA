package com.PDA.gmax.M10;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;

import com.PDA.gmax.GetComboData;
import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.DBAccess;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class M14_QUERY_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonConfigSet = "", sJsonCombo = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand,tx_dlv_no = "";

    //== View 선언(EditText) ==//
    private EditText edtdlv_no,con_dt_fr, con_dt_to,edtItemCD;

    //== View 선언(Spinner) ==//
    private Spinner cmbBP_CD;

    private RadioGroup PostGiFlag;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    private Button btn_open,btn_hide,btn_query;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(DrawerLayout) ==//
    private DrawerLayout DrawerView;

    //== Spinner 관련 변수 선언 ==//
    private String str_BP_CD = "";

    //== 날짜 관련 변수 선언 ==//
    private Calendar cal1, cal2;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int m14_DTL_REQUEST_CODE = 0;

    //==포장실적 구분 변수 선언==//
    private boolean PACKAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m14_query);

        this.initializeView();

        this.initializeCalendar();

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

        //== ID값 바인딩 ==//
        edtdlv_no          = (EditText) findViewById(R.id.edtdlv_no);
        con_dt_fr       = (EditText) findViewById(R.id.con_dt_fr);
        con_dt_to       = (EditText) findViewById(R.id.con_dt_to);
        edtItemCD        = (EditText) findViewById(R.id.edtItemCD);

        cmbBP_CD        = (Spinner) findViewById(R.id.cmbBP_CD);

        listview         = (ListView) findViewById(R.id.listOrder);
        btn_hide         = (Button) findViewById(R.id.btn_hide);
        btn_open         = (Button) findViewById(R.id.btn_open);
        btn_query        = (Button) findViewById(R.id.btn_query);
        DrawerView       = (DrawerLayout) findViewById(R.id.drawer);
        img_barcode     = (ImageView) findViewById(R.id.img_barcode);

    }

    private void initializeCalendar() {
        cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal1.setTime(new Date());
        cal1.add(Calendar.MONTH, -1);

        cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal2.setTime(new Date());
    }

    private void initializeListener() {

        con_dt_fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, con_dt_fr, cal1);
            }
        });

        con_dt_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, con_dt_to, cal2);
            }
        });

        btn_open.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerView.openDrawer(Gravity.LEFT);
            }
        });
        btn_hide.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerView.closeDrawer(Gravity.LEFT);
            }
        });

        btn_query.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        edtdlv_no.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {

                    //바코드 입력시 텍스트 리프레시를 위해서 설정

                    String temp = edtdlv_no.getText().toString().replaceFirst(tx_dlv_no,"");
                    //temp = "MD221104-001";
                    edtdlv_no.setText(temp);
                    tx_dlv_no=edtdlv_no.getText().toString();

                    start();

                    return true;
                }
                return false;
            }
        });

        //== 바코드 이벤트 ==//
        img_barcode.setOnClickListener(qrClickListener);
    }

    private void initializeData() {
        //공정 리스트 콤보 값을 초기화 한다.
        dbQuery_getcmbBP_CD();

        con_dt_fr.setText(df.format(cal1.getTime()));
        con_dt_to.setText(df.format(cal2.getTime()));

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

                M14_QUERY_ListViewAdapter ListViewAdapter = new M14_QUERY_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    M14_QUERY item = new M14_QUERY();

                    item.ITEM_CD            = jObject.getString("ITEM_CD");    //품번
                    item.ITEM_NM            = jObject.getString("ITEM_NM");    //품명
                    item.SPEC               = jObject.getString("SPEC");       //규격
                    item.DN_NO              = jObject.getString("DLV_NO");      //거래명세서
                    item.SEQ_NO             = jObject.getString("SER_NO");     //순번
                    item.PO_NO              = jObject.getString("PO_NO");      //입하번호

                    ListViewAdapter.addShipmentHDRItem(item);
                }

                listview.setAdapter(ListViewAdapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {

                        M14_QUERY vItem = (M14_QUERY) parent.getItemAtPosition(position);

                        //Toast.makeText(P_OEM11_Activity.this ,vSelectItem,Toast.LENGTH_LONG).show();

                        //Intent intent = TGSClass.ChangeView(getPackageName(), M14_DTL_Activity.class);
//
                        //intent.putExtra("QUERY", vItem);
                        //intent.putExtra("PACKAGE", PACKAGE);
//
                        //intent.putExtra("DN_REQ_NO", vItem.getDN_REQ_NO());
//
//
                        //startActivityForResult(intent, 0);
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
               sql+= "EXEC DBO.XUSP_TPC_M1004MA1_GET_LIST ";
               sql+= "@FLAG ='GRID1',";
               sql+= "@PLANT_CD ='" + vPLANT_CD + "',";
               sql+= "@BP_CD ='" + str_BP_CD + "',";
               sql+= "@DLV_NO ='" + edtdlv_no.getText().toString() + "',";
               sql+= "@ITEM_CD ='" + edtItemCD.getText().toString() + "',";
               sql+= "@DATE_FR ='" + con_dt_fr.getText().toString() + "',";
               sql+= "@DATE_TO ='" + con_dt_to.getText().toString() + "'";
               sql+= ";";

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

    private void dbQuery_getcmbBP_CD() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery_getComboData = new Thread() {
            public void run() {
                String sql = " exec XUSP_TPC_SHIPMENT_GET_COMBODATA";
                sql += " @FLAG = 'cmbBizPartner_HDR'";
                sql += " ,@PLANT_CD = ''";


                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonCombo = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        wkThd_dbQuery_getComboData.start();   //스레드 시작
        try {
            wkThd_dbQuery_getComboData.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }

        try {
            JSONArray ja = new JSONArray(sJsonCombo);

            final ArrayList<GetComboData> lstItem = new ArrayList<>();

            /*기본값 세팅*/
            GetComboData itemBase = new GetComboData();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jObject = ja.getJSONObject(i);

                //final String vJOB_CD = jObject.getString("JOB_CD");
                final String vMINOR_CD  = jObject.getString("CODE");
                final String vMINOR_NM  = jObject.getString("NAME");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                lstItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, lstItem);

            cmbBP_CD.setAdapter(adapter);
            //로딩시 기본값 세팅
            cmbBP_CD.setSelection(adapter.getPosition(itemBase));
            //콤보박스 클릭 이벤트 정의
            cmbBP_CD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    str_BP_CD = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
                    start();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (JSONException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        } catch (Exception ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
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