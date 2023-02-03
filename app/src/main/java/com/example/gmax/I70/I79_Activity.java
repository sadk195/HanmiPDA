package com.example.gmax.I70;

import android.content.DialogInterface;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.gmax.BaseActivity;
import com.example.gmax.DBAccess;
import com.example.gmax.MySession;
import com.example.gmax.R;
import com.example.gmax.TGSClass;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class I79_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand, SL_NM;

    //== View 선언(EditText) ==//
    private EditText txt_Scan, txt_tracking_no, txt_lot_no, txt_stockyard, txt_item_cd, txt_inventory_qty;

    //== View 선언(TextView) ==//
    private TextView txt_inventory_stock_qty, txt_sl_cd,app_title, txt_item_nm;

    //== View 선언(CheckBox) ==//
    private CheckBox chk_option;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    public Button btn_list, btn_save;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int I79_DTL_REQUEST_CODE = 0;
    private final long INTERVAL_TIME = 500;  //간격
    private long inputEnterPressedTime = 0;  //엔터키 입력 시간.

    public String sJobCode;
    public String sMenuRemark;
    public String Plant_CD;
    public String INV_NO, SL_CD , WC_CD, ITEM_NM, STOCKYARD, ITEM_CD, INVENTORY_QTY, TRACKING_NO, LOT_NO, INVENTORY_COUNT_DATE, MAJOR_SL_CD, UPPER_INV_NO, formattedStringPrice;
    private  String m_item_cd="";
    private  String m_tracking_no="";
    public int GOOD_ON_HAND_QTY;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i79);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID                     = getIntent().getStringExtra("MENU_ID");
        vMenuNm                     = getIntent().getStringExtra("MENU_NM");
        vMenuRemark                 = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand               = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        SL_NM                       = getIntent().getStringExtra("SL_NM");
        UPPER_INV_NO                = getIntent().getStringExtra("INV_NO");
        INVENTORY_COUNT_DATE        = getIntent().getStringExtra("INVENTORY_COUNT_DATE");

        //== ID 값 바인딩 ==//
        txt_tracking_no             = (EditText) findViewById(R.id.txt_tracking_no);
        txt_lot_no                  = (EditText) findViewById(R.id.txt_lot_no);
        txt_inventory_stock_qty     = (TextView) findViewById(R.id.txt_inventory_stock_qty);
        txt_sl_cd                   = (TextView) findViewById(R.id.txt_sl_cd);
        txt_item_nm                 = (TextView) findViewById(R.id.txt_item_nm);

        txt_stockyard               = (EditText) findViewById(R.id.txt_stockyard);
        txt_item_cd                 = (EditText) findViewById(R.id.txt_item_cd);
        txt_inventory_qty           = (EditText) findViewById(R.id.txt_inventory_qty);

        app_title                   = (TextView) findViewById(R.id.app_title);

        chk_option                  = (CheckBox) findViewById(R.id.chk_option);

        btn_list                    = (Button) findViewById(R.id.btn_save);
        btn_save                    = (Button) findViewById(R.id.btn_save);

        listview                    = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeListener() {

        //== 이벤트 ==//
        txt_item_cd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    String vItem_nm = txt_item_cd.getText().toString();
                    String vTracking_no;

                    if(scandata(vItem_nm)==true);
                    {
                        vItem_nm = m_item_cd;
                        vTracking_no = m_tracking_no;
                    }

                    item_info_query(vItem_nm,vPLANT_CD,vTracking_no);
                    txt_item_cd.setText(ITEM_CD);
                    txt_item_nm.setText(ITEM_NM);
                    txt_sl_cd.setText(SL_NM);
                    txt_tracking_no.setText(TRACKING_NO);
                    txt_lot_no.setText(LOT_NO);
                    txt_inventory_stock_qty.setText(formattedStringPrice);

                    if (vPLANT_CD.equals("C1")) {
                        txt_stockyard.requestFocus();
                    }
                    else {
                        txt_inventory_qty.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });

        txt_stockyard.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    txt_inventory_qty.requestFocus();
                    return true;
                }
                return false;
            }

        });

        txt_inventory_qty.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    btn_save.requestFocus();
                    return true;
                }

                return false;
            }
        });

        /*
        txt_tracking_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    EditText txt_lot_no = (EditText) findViewById(R.id.txt_lot_no);
                    txt_lot_no.requestFocus();
                    return true;
                }

                return false;
            }
        });

         */

        /*
        txt_lot_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction()== KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    Button btn_save = (Button) findViewById(R.id.btn_save);
                    btn_save.requestFocus();
                    return true;
                }

                return false;
            }
        });
        */

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                STOCKYARD = txt_stockyard.getText().toString();
                ITEM_CD = txt_item_cd.getText().toString();
                INVENTORY_QTY = txt_inventory_qty.getText().toString();
                TRACKING_NO = txt_tracking_no.getText().toString();
                LOT_NO = txt_lot_no.getText().toString();

                if(inventory_info_chk(INVENTORY_COUNT_DATE,STOCKYARD,ITEM_CD,TRACKING_NO,LOT_NO) == false)
                {
                    MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(I79_Activity.this);
                    mAlert.setTitle("재고실사등록")
                            .setMessage("동일한 품번의 재고실사 등록 내역이 존재합니다. 등록을 진행 하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialog, int which) {
                                    if(inventory_info_save(INVENTORY_COUNT_DATE,MAJOR_SL_CD,STOCKYARD,ITEM_CD,INVENTORY_QTY,TRACKING_NO,LOT_NO) == true) {
                                        /*
                                        MySoundPlayer.initSounds(getApplicationContext());
                                        MySoundPlayer.play(MySoundPlayer.bee);
                                         */
                                        txt_stockyard.getText().clear();
                                        txt_item_cd.getText().clear();
                                        txt_inventory_qty.getText().clear();
                                        txt_tracking_no.getText().clear();
                                        txt_lot_no.getText().clear();
                                        txt_item_nm.setText("");
                                        txt_sl_cd.setText("");
                                        txt_inventory_stock_qty.setText("");

                                        txt_item_cd.requestFocus();
                                    }
                                    Toast.makeText(getApplicationContext(),"실사등록이 완료되었습니다.", Toast.LENGTH_LONG).show();
                            } })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    txt_stockyard.getText().clear();
                                    txt_item_cd.getText().clear();
                                    txt_inventory_qty.getText().clear();
                                    txt_tracking_no.getText().clear();
                                    txt_lot_no.getText().clear();
                                    txt_item_nm.setText("");
                                    txt_sl_cd.setText("");
                                    txt_inventory_stock_qty.setText("");

                                    txt_item_cd.requestFocus();
                                }
                            })
                            .create().show();
                }
                else
                {
                    if(inventory_info_save(INVENTORY_COUNT_DATE,MAJOR_SL_CD,STOCKYARD,ITEM_CD,INVENTORY_QTY,TRACKING_NO,LOT_NO) == true)
                    {
                        //val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                        //toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 300)
                        /*
                        MySoundPlayer.initSounds(getApplicationContext());
                        MySoundPlayer.play(MySoundPlayer.bee);
                        */
                        txt_stockyard.getText().clear();
                        txt_item_cd.getText().clear();
                        txt_inventory_qty.getText().clear();
                        txt_tracking_no.getText().clear();
                        txt_lot_no.getText().clear();
                        txt_item_nm.setText("");
                        txt_sl_cd.setText("");
                        txt_inventory_stock_qty.setText("");

                        txt_item_cd.requestFocus();
                    }
                    Toast.makeText(getApplicationContext(),"실사등록이 완료되었습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initializeData() {
        app_title.setText(vMenuNm);

        txt_sl_cd.setText(SL_NM);
    }

    public  boolean scandata(String pITEM_CD)
    {
        //1. 문자열 나누기 (VISS 코드와 RFID 태그 값이 동시 스캔 될 가능 성 있음( RFID 스캔 태크 설정 앱에서 시작,종료 문자를 * 로 설정.
        // 1.1 문자열에서 * 의 인덱스를 찾는다. (단, * 의 인덱스가 0이면 무시)

        if(pITEM_CD.length() == 0){
            return false;
        }

        //스캔 된 텍스트의 개행 문자를 #으로 변경.
        String vITEM_CD = pITEM_CD.replaceAll("\\r\\n|\\r|\\n",";");

        //# 문자 이후 데이터는 모두 버림, 즉, 한줄로 들어온 텍스트만 정상적인 값으로 판단.
        if(pITEM_CD.indexOf(";") >= 0) {
            vITEM_CD = pITEM_CD.substring(0, pITEM_CD.indexOf(";"));
        }

        int nIDX = pITEM_CD.indexOf(";",0); //2번째 칸부터(인덱스가 1)인 거 부터 <*> 검색한다.

        String val1 = "";
        String val2 = "";

        if (nIDX == -1 ) {
            val1 = pITEM_CD;
            val2 = "";
        }
        else{
            val1 = pITEM_CD.substring(0,nIDX);
            val2 = pITEM_CD.substring(nIDX + 1,pITEM_CD.length());
        }


        m_item_cd = val1;
        m_tracking_no = val2;


        return true;
    }

    public void item_info_query(final String pITEM_CD, final String pPLANT_CD, final String pTRACKING_NO) {
        Thread workingThread = new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {

                String sql = " XUSP_I79_ITEM_INFO_GET_LIST ";
                sql += " @PLANT_CD = '" + pPLANT_CD + "'";
                sql += " ,@ITEM_CD = '" + pITEM_CD + "'";
                sql += " ,@TRACKING_NO = '" + pTRACKING_NO + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);

                ITEM_NM_INFO();
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ITEM_NM_INFO() {
        if (!sJson.equals("")) {

            try {
                JSONArray ja = new JSONArray(sJson);

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    ITEM_CD = jObject.getString("ITEM_CD");
                    ITEM_NM = jObject.getString("ITEM_NM");
                    MAJOR_SL_CD = jObject.getString("MAJOR_SL_CD");
                    SL_NM = jObject.getString("SL_NM");
                    TRACKING_NO = jObject.getString("TRACKING_NO");
                    LOT_NO = jObject.getString("LOT_NO");
                    GOOD_ON_HAND_QTY = Integer.parseInt(jObject.getString("GOOD_ON_HAND_QTY"));

                    DecimalFormat myFormatter = new DecimalFormat("###,###");
                    formattedStringPrice = myFormatter.format(GOOD_ON_HAND_QTY);

                }
            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage());
            }
        }
    }

    public boolean inventory_info_save(final String pINVENTORY_COUNT_DATE, final String pMAJOR_SL_CD ,final String pSTOCKYARD, final String pITEM_CD, final String pINVENTORY_QTY, final String pTRACKING_NO, final String pLOT_NO) {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession)getApplication(); //전역 클래스

                String vParmID = getIntent().getStringExtra("pID");              //등록된 장비명이 로그인 아이디로 판단.
                String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");
                String vDevice = getIntent().getStringExtra("pDEVICE");

                if(vParmID == null) vParmID = global.getLoginIDString();
                if(vPlant_CD == null) vPlant_CD = global.getPlantCDString();
                if(vDevice == null) vDevice = global.getUnitCDString();

                String sql = " exec XUSP_I79_SET_LIST_DTL ";
                sql += "@UPPER_INV_NO = '" + UPPER_INV_NO + "'";
                sql += " ,@PLANT_CD = '" + vPlant_CD + "'";
                sql += " ,@ITEM_CD = '" + pITEM_CD + "'";
                sql += " ,@TRACKING_NO = '" + pTRACKING_NO + "'";
                sql += " ,@LOT_NO = '" + pLOT_NO + "'";
                sql += " ,@INV_QTY = " + pINVENTORY_QTY + "";
                sql += " ,@LOCATION = '" + pSTOCKYARD + "'";
                sql += " ,@USER_ID = '" + vParmID + "'";
                //sql += " ,@MAJOR_SL_CD = '" + pMAJOR_SL_CD + "'";

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
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean inventory_info_chk(final String pINVENTORY_COUNT_DATE, final String pSTOCKYARD, final String pITEM_CD, final String pTRACKING_NO, final String pLOT_NO) {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession)getApplication(); //전역 클래스

                String sql = " exec XUSP_I79_SET_LIST_CHK ";
                sql += "@INVENTORY_COUNT_DATE = '" + pINVENTORY_COUNT_DATE + "'";
                sql += " ,@ITEM_CD = '" + pITEM_CD + "'";
                sql += " ,@TRACKING_NO = '" + pTRACKING_NO + "'";
                sql += " ,@LOT_NO = '" + pLOT_NO + "'";
                sql += " ,@LOCATION = '" + pSTOCKYARD + "'";

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
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
            return false;
        }
        if (!sJson.equals("[]"))
        {
            return false;
        }
        return true;
    }

}
