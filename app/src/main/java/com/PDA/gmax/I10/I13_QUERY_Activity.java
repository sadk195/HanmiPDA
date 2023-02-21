package com.PDA.gmax.I10;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.DBAccess;
import com.PDA.gmax.TGSClass;
import com.PDA.gmax.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class I13_QUERY_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJsonHDR = "", sJsonList = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(타이틀) ==//
    private TextView lbl_view_title, txt_sub_title;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode_item_cd;

    //== View 선언(EditText) ==//
    private EditText item_cd;

    //== View 선언(TextView) ==//
    private TextView txt_item_cd, txt_item_nm, txt_standard, txt_issued_sl_cd, txt_issued_sl_nm, txt_location, txt_location_nm;

    //== View 선언(RadioGroup) ==//
    private RadioGroup rdo_grp;

    //== View 선언(RadioButton) ==//
    private RadioButton rdo_sl_cd, rdo_tracking, rdo_position, chkRdoId;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== 변수 선언 ==//
    private String rdoOpt;

    //== Dialog ==//
    private DBQueryList queryList;

    //== Header 관련 변수 ==//
    private TextView OPT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i13_query);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID                 = getIntent().getStringExtra("MENU_ID");
        vMenuNm                 = getIntent().getStringExtra("MENU_NM");
        vMenuRemark             = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand           = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== ID 값 바인딩 ==//
        lbl_view_title          = (TextView) findViewById(R.id.lbl_view_title);
        txt_sub_title           = (TextView) findViewById(R.id.txt_sub_title);

        img_barcode_item_cd     = (ImageView) findViewById(R.id.img_barcode_item_cd);
        item_cd                 = (EditText) findViewById(R.id.item_cd);

        txt_item_cd             = (TextView) findViewById(R.id.txt_item_cd);
        txt_item_nm             = (TextView) findViewById(R.id.txt_item_nm);
        txt_standard            = (TextView) findViewById(R.id.txt_standard);
        txt_issued_sl_cd        = (TextView) findViewById(R.id.txt_issued_sl_cd);
        txt_issued_sl_nm        = (TextView) findViewById(R.id.txt_issued_sl_nm);
        txt_location            = (TextView) findViewById(R.id.txt_location);
        txt_location_nm         = (TextView) findViewById(R.id.txt_location_nm);

        rdo_grp                 = (RadioGroup) findViewById(R.id.rdo_grp);
        rdo_sl_cd               = (RadioButton) findViewById(R.id.rdo_sl_cd);
        rdo_tracking            = (RadioButton) findViewById(R.id.rdo_tracking);
        rdo_position            = (RadioButton) findViewById(R.id.rdo_position);

        listview                = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeListener() {
        //== 품목 이벤트 ==//
        item_cd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && i == KeyEvent.KEYCODE_ENTER) {
                    String str_item_cd = item_cd.getText().toString();
                    if (str_item_cd.equals("")) {
                        TGSClass.AlertMessage(getApplicationContext(), "품번을 입력해주세요.");
                        return false;
                    } else {
                        String str_item = TGSClass.transSemicolon(str_item_cd);
                        start(str_item);
                    }
                    return true;
                }
                return false;
            }
        });

        //== 라디오 그룹 이벤트 ==//
        rdo_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int chkId) {
                //== 라디오 버튼별 값 세팅 ==//
                String sub_title = "";
                switch (chkId) {
                    case R.id.rdo_sl_cd:
                        rdoOpt = "SL_CD";
                        OPT.setText("창고");
                        sub_title = " 창고    |    양품수량    |    불량품수량 ";
                        break;
                    case R.id.rdo_tracking:
                        rdoOpt = "TRACKING";
                        OPT.setText("TRACKING");
                        sub_title = " TRACKING   |    양품수량    |    불량품수량 ";
                        break;
                    case R.id.rdo_position:
                        rdoOpt = "POSITION";
                        OPT.setText("위치");
                        sub_title = " 위치    |    양품수량    |    불량품수량 ";
                    default:
                        break;
                }
                txt_sub_title.setText(sub_title);

                //== 품목 값 체크 ==//
                if (sJsonHDR.equals("[]") || sJsonHDR.isEmpty()) {
                    TGSClass.AlertMessage(getApplicationContext(), "선택 된 품목이 없습니다.");
                    return;
                } else {
                    dbQuery_LIST();
                }
            }
        });

        //== 바코드 이벤트 ==//
        img_barcode_item_cd.setOnClickListener(qrClickListener);
    }

    private void initializeData() {
        //== 초기값 ==//

        //** HEADER LIST VIEW
        //== LayoutInflater 생성 ==//
//                                LayoutInflater layoutInflater = LayoutInflater.from(this);
//                                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //== LayoutInflater로부터 Header View 생성 ==//
//                                View header = layoutInflater.inflate(R.layout.header_i13_view, null);
        final View head = getLayoutInflater().inflate(R.layout.header_i13_view, null, false);
        OPT = (TextView) head.findViewById(R.id.OPT);
//        listview.addHeaderView(head);
        OPT.setText("창고");
        //** HEADER LIST VIEW

        rdoOpt = "SL_CD";
    }

    //== start ==//
    private void start(String str_item_cd) {
        dbQuery_HDR(str_item_cd);

        if (sJsonHDR.isEmpty() || sJsonHDR.equals("[]")) {
            item_cd.setText(null);
            TGSClass.AlertMessage(getApplicationContext(), "존재하지 않는 품번입니다.", 1000);
            return;
        } else {
            try {
                JSONArray ja = new JSONArray(sJsonHDR);

                JSONObject jObject = ja.getJSONObject(0);

                item_cd.setText(null);

                txt_item_cd.setText(jObject.getString("ITEM_CD"));
                txt_item_nm.setText(jObject.getString("ITEM_NM"));
                txt_standard.setText(jObject.getString("SPEC"));
                txt_issued_sl_cd.setText(jObject.getString("ISSUED_SL_CD"));
                txt_issued_sl_nm.setText(jObject.getString("ISSUED_SL_NM"));
                txt_location.setText(jObject.getString("LOCATION"));
                txt_location_nm.setText(jObject.getString("LOCATION_NM"));
            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage());
            }

            dbQuery_LIST();
        }
    }

    //== 조회 ==//
    private void dbQuery_HDR(final String str_item_cd) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_dbQuery_HDR = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_APK_I13_QUERY_GET_HDR ";
                sql += "  @PLANT_CD = 'H1' ";
                sql += "  ,@ITEM_CD = '" + str_item_cd + "' ";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                sJsonHDR = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThd_dbQuery_HDR.start();   //스레드 시작
        try {
            workThd_dbQuery_HDR.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.
        } catch (InterruptedException ex) {

        }
    }

    //== 조회 ==//
    private void dbQuery_LIST() {
        progressStart(this);
        queryList = new DBQueryList();
        queryList.start();
    }

    public class DBQueryList extends Thread {
        @Override
        public void run() {
            boolean jSonType = TGSClass.isJsonData(sJsonHDR);

            if (!jSonType) return;

            try {
                JSONArray ja = new JSONArray(sJsonHDR);
                JSONObject jObject = ja.getJSONObject(0);

                String sql = " EXEC XUSP_APK_I13_QUERY_GET_LIST ";
                sql += " @PLANT_CD = 'H1' ";
                sql += ", @ITEM_CD = '" + jObject.getString("ITEM_CD") + "'";
                sql += ", @OPT = '" + rdoOpt + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);
                pParms.add(parm);

                sJsonList = dba.SendHttpMessage("GetSQLData", pParms);
                handler.sendMessage(handler.obtainMessage());
            } catch (JSONException ex) {
                TGSClass.AlertMessage(I13_QUERY_Activity.this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(I13_QUERY_Activity.this, e1.getMessage());
            }
        }
    }

    /*
    public class BackgroundThread extends Thread {
        volatile boolean running = false;
        int cnt;

        void setRunning(boolean bln) {
            running = bln;
            cnt = 10;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    sleep(1000);
                    if (cnt-- == 0) {
                        running = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handler.sendMessage(handler.obtainMessage());
        }
    }
     */

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            progressEnd();

            boolean retry = true;
            while (retry) {
                try {
                    queryList.join();

                    I13_QUERY_ListViewAdapter listViewAdapter = new I13_QUERY_ListViewAdapter();

                    try {
                        boolean jSonType = TGSClass.isJsonData(sJsonList);

                        if (jSonType) {
                            if (sJsonList.equals("[]") || sJsonList.isEmpty()) {
                                TGSClass.AlertMessage(getApplicationContext(), "조회할 항목이 없습니다.");
                            } else {
                                JSONArray ja = new JSONArray(sJsonList);

                                for (int idx = 0; idx < ja.length(); idx++) {
                                    JSONObject jObject = ja.getJSONObject(idx);

                                    I13_QUERY item = new I13_QUERY();

                                    item.ITEM_CD            = jObject.getString("ITEM_CD");
                                    item.OPT                = jObject.getString("OPT");
                                    item.GOOD_ON_HAND_QTY   = decimalForm.format(jObject.getInt("GOOD_ON_HAND_QTY"));
                                    item.BAD_ON_HAND_QTY    = decimalForm.format(jObject.getInt("BAD_ON_HAND_QTY"));

                                    listViewAdapter.addQueryItem(item);
                                }

                                listview.setAdapter(listViewAdapter);

                                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
//                                        TGSClass.AlertMessage(getApplicationContext(), position + "번째 item");
                                    }
                                });

                                TGSClass.AlertMessage(I13_QUERY_Activity.this, ja.length() + " 건 조회되었습니다.");
                            }
                        }
                    } catch (JSONException ex) {
                        TGSClass.AlertMessage(I13_QUERY_Activity.this, ex.getMessage());
                    } catch (Exception e1) {
                        TGSClass.AlertMessage(I13_QUERY_Activity.this, e1.getMessage());
                    }
                    retry = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
