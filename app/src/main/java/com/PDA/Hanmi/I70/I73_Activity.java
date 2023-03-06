package com.PDA.Hanmi.I70;

import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.PDA.Hanmi.MySession;
import com.PDA.Hanmi.DBAccess;
import com.PDA.Hanmi.R;
import com.PDA.Hanmi.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class I73_Activity extends AppCompatActivity {

    private final long INTERVAL_TIME = 500;  //간격
    private long inputEnterPressedTime = 0;  //엔터키 입력 시간.
    public String sJson = "";
    public ImageView img_barcode;
    public EditText txt_Scan;
    public String sJobCode;
    public String sMenuRemark;
    public MySession global;
    public String Plant_CD;
    public Button btn_del;
    public String SL_NM,LOCATION,ITEM_CD,ITEM_NM,TRACKING_NO, CHK, vINVENTORY_COUNT_DATE,SL_CD, PARMID, INV_NO, INV_SEQ;
    public int QTY;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i73);

        String vMenuName = getIntent().getStringExtra("MENU_NM");
        sMenuRemark = getIntent().getStringExtra("MENU_REMARK");
        sJobCode = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드
        vINVENTORY_COUNT_DATE = getIntent().getStringExtra("INVENTORY_COUNT_DATE");
        String vSL_CD = getIntent().getStringExtra("SL_CD");
        PARMID = getIntent().getStringExtra("PARMID");

        TextView lbl_inventory_count_date = (TextView) findViewById(R.id.lbl_inventory_count_date);
        lbl_inventory_count_date.setText(vINVENTORY_COUNT_DATE);
        TextView sl_cd = (TextView) findViewById(R.id.sl_cd);
        sl_cd.setText(vSL_CD);

        String vINV_NO = "";

        inventory_info_query(vINVENTORY_COUNT_DATE, vSL_CD, vINV_NO,PARMID);

        TextView lbl_view_title = findViewById(R.id.lbl_view_title);
        lbl_view_title.setText(vMenuName);

        btn_del = findViewById(R.id.btn_del);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListView listview = (ListView) findViewById(R.id.listOrder);
                I73_ListViewAdapter listViewAdapter = (I73_ListViewAdapter) listview.getAdapter();

                for (int idx2 = 0; idx2 < listViewAdapter.getCount(); idx2++)
                {
                    I73_ARRAYLIST item = (I73_ARRAYLIST) listViewAdapter.getItem(idx2);

                    if(item.getCHK() == "Y")
                    {
                        String item_cd = item.getITEM_CD();
                        String tracking_no = item.getTRACKING_NO();
                        String LOCATION = item.getLOCATION();
                        String SL_CD = item.getSL_CD();
                        String INV_NO = item.getINV_NO();
                        String INV_SEQ = item.getINV_SEQ();

                        if(DB_DEL_DTL(item_cd, tracking_no, LOCATION, SL_CD, vINVENTORY_COUNT_DATE, INV_NO, INV_SEQ) == true)
                        {
                            Toast.makeText(getApplicationContext(),"해당 건이 정상적으로 삭제되었습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    public boolean DB_DEL_DTL(final String item_cd, final String tracking_no, final String LOCATION, final String SL_CD, final String vINVENTORY_COUNT_DATE, final String INV_NO, final String INV_SEQ)
    {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession)getApplication(); //전역 클래스

                String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");
                String vUser_ID = getIntent().getStringExtra("pUSER_ID");

                if(vPlant_CD == null)
                    vPlant_CD = global.getPlantCDString();

                if(vUser_ID == null)
                    vUser_ID = global.getLoginIDString();

                String sql = " EXEC XUSP_I73_SET_LIST ";
                sql += "@PLANT_CD = '" + vPlant_CD + "'";
                sql += ",@ITEM_CD = '" + item_cd + "'";
                sql += ",@TRACKING_NO = '" + tracking_no + "'";
                sql += ",@LOCATION = '" + LOCATION + "'";
                sql += ",@SL_CD = '" + SL_CD + "'";
                sql += ",@INVENTORY_COUNT_DATE = '" + vINVENTORY_COUNT_DATE + "'";
                sql += ",@INV_NO = '" + INV_NO + "'";
                sql += ",@INV_SEQ = '" + INV_SEQ + "'";

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

        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void inventory_info_query(final String pINVENTORY_COUNT_DATE, final String pSL_CD, final  String pINV_NO, final String pPARMID) {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession)getApplication(); //전역 클래스
                String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");

                if(vPlant_CD == null) vPlant_CD = global.getPlantCDString();


                String sql = " exec XUSP_I73_GET_LIST ";
                sql += " @PLANT_CD = '" + vPlant_CD + "'";
                sql += " ,@DATE = '" + pINVENTORY_COUNT_DATE + "'";
                sql += " ,@SL_CD = '" + pSL_CD + "'";
                sql += " ,@INV_NO = '" + pINV_NO + "'";
                sql += " ,@PARMID = '" + pPARMID + "'";


                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);


                //TGSClass.AlertMessage(getApplicationContext(), pParms.toString());
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }

        Start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public  void Start() {



        if (!sJson.equals("[]")) {

            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                final ArrayList<I73_ARRAYLIST> lstItem = new ArrayList<>();

                ListView listview = findViewById(R.id.listOrder);
                I73_ListViewAdapter listViewAdapter = new I73_ListViewAdapter();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    CHK = jObject.getString("CHK");  //체크 이벤트
                    SL_NM =  jObject.getString("SL_CD");
                    SL_NM = jObject.getString("SL_NM");
                    LOCATION = jObject.getString("LOCATION");
                    ITEM_CD = jObject.getString("ITEM_CD");
                    ITEM_NM = jObject.getString("ITEM_NM");
                    QTY = Integer.parseInt(jObject.getString("QTY"));
                    TRACKING_NO = jObject.getString("TRACKING_NO");
                    INV_NO = jObject.getString("INV_NO");
                    INV_SEQ = jObject.getString("INV_SEQ");

                    DecimalFormat myFormatter = new DecimalFormat("###,###");
                    String formattedStringPrice = myFormatter.format(QTY);

                    listViewAdapter.addItem(CHK,SL_CD,SL_NM,LOCATION, ITEM_CD,ITEM_NM, formattedStringPrice, TRACKING_NO, INV_NO, INV_SEQ);
                }
                listview.setAdapter(listViewAdapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {

                        I73_ListViewAdapter listViewAdapter = (I73_ListViewAdapter) parent.getAdapter();
                        I73_ARRAYLIST item = (I73_ARRAYLIST) listViewAdapter.getItem(position);

                        CheckedTextView chk = (CheckedTextView) v.findViewById(R.id.CHK_TEXT);

                        if (chk.isChecked() == true) {
                            chk.setChecked(false);
                            item.CHK = "";
                        } else {
                            chk.setChecked(true);
                            item.CHK = "Y";
                        }
                    }
                }) ;

            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage());
            } catch (Exception e1) {
                TGSClass.AlertMessage(this, e1.getMessage());
            }
        }
    }
}
