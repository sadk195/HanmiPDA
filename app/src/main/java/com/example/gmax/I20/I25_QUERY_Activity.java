package com.example.gmax.I20;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gmax.BaseActivity;
import com.example.gmax.DBAccess;
import com.example.gmax.GetComboData;
import com.example.gmax.R;
import com.example.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class I25_QUERY_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonCombo = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(TextView) ==//
    private TextView txt_title;

    //== View 선언(EditText) ==//
    private EditText move_date_from, move_date_to, txt_Scan_location;

    //== View 선언(Spinner) ==//
    private Spinner move_type, storage_location, insert_user;

    //== View 선언(Button) ==//
    private Button btn_query, btn_menu;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal1, cal2;

    //== Spinner 관련 변수 선언 ==//
    private String move_type_query = "", sl_cd_query = "", insert_user_query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i25_query);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID             = getIntent().getStringExtra("MENU_ID");
        vMenuNm             = getIntent().getStringExtra("MENU_NM");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("START_COMMAND");

        //== ID 값 바인딩 ==//
        txt_title           = (TextView) findViewById(R.id.txt_title);

        move_date_from      = (EditText) findViewById(R.id.move_date_from);
        move_date_to        = (EditText) findViewById(R.id.move_date_to);
        txt_Scan_location   = (EditText) findViewById(R.id.txt_Scan_location);

        move_type           = (Spinner) findViewById(R.id.move_type);
        storage_location    = (Spinner) findViewById(R.id.storage_location);
        insert_user         = (Spinner) findViewById(R.id.insert_user);

        btn_query           = (Button) findViewById(R.id.btn_query);

        listview            = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeCalendar() {
        cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal1.setTime(new Date());
        cal1.add(Calendar.YEAR, -1);

        cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal2.setTime(new Date());
    }

    private void initializeListener() {
        move_date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, move_date_from, cal1);
            }
        });

        move_date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, move_date_to, cal2);
            }
        });

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (move_type_query.equals("")) {
                    TGSClass.AlertMessage(getApplicationContext(), "조회할 이동유형을 선택해주십시오.");
                } else {
                    TGSClass.AlertMessage(getApplicationContext(), move_type_query);

                    if (move_type_query.equals("LOCATION")) {
                        txt_title.setText("품번 | 품명 | 수량 | 적치장 이동");
                    } else if (move_type_query.equals("STORAGE")) {
                        txt_title.setText("품번 | 품명 | 수량 | 창고 이동");
                    } else if (move_type_query.equals("TRACKING")) {
                        txt_title.setText("품번 | 품명 | 수량 | TRACKING 이동");
                    } else if (move_type_query.equals("WORKPLACE")) {
                        txt_title.setText("품번 | 품명 | 수량 | 사업장 이동");
                    } else {
                        txt_title.setText("품번 | 품명 | 수량 | 이동");
                    }
                    start();
                }
            }
        });
    }

    private void initializeData() {    //초기화
        move_date_from.setText(df.format(cal1.getTime()));

        move_date_to.setText(df.format(cal2.getTime()));

        dbQuery_get_storage_location();
        dbQuery_get_insert_user();
        dbQuery_get_move_type();
    }

    private void dbQuery_get_storage_location() {   //창고 스피너
        Thread workThd_get_storage_location = new Thread() {
            public void run() {
                String sql = " exec XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_GET_COMBODATA ";
                sql += " @FLAG = 'storage_location' ";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonCombo = dba.SendHttpMessage("GetSQLData", pParms);

                //TGSClass.AlertMessage(getApplicationContext(), pParms.toString());
            }
        };
        workThd_get_storage_location.start();   //스레드 시작
        try {
            workThd_get_storage_location.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }

        try {
            JSONArray ja = new JSONArray(sJsonCombo);

            final ArrayList<GetComboData> listItem = new ArrayList<>();

            /*기본값 세팅*/
            GetComboData itemBase = new GetComboData();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jObject = ja.getJSONObject(i);

                final String vMINOR_CD = jObject.getString("CODE");
                final String vMINOR_NM = jObject.getString("NAME");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                listItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, listItem);
            storage_location.setAdapter(adapter);

            //로딩시 기본값 세팅
            storage_location.setSelection(adapter.getPosition(itemBase));
            //cmbBizPartner.setSelection();

            //콤보박스 클릭 이벤트 정의
            storage_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sl_cd_query = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
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

    private void dbQuery_get_insert_user() {     //작업장 스피너
        Thread workThd_get_insert_user = new Thread() {
            public void run() {
                String sql = " exec XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_GET_COMBODATA";
                sql += " @FLAG = 'insert_user'";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "'";

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
        workThd_get_insert_user.start();   //스레드 시작
        try {
            workThd_get_insert_user.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }

        try {
            JSONArray ja = new JSONArray(sJsonCombo);

            final ArrayList<GetComboData> listItem = new ArrayList<>();

            /*기본값 세팅*/
            GetComboData itemBase = new GetComboData();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jObject = ja.getJSONObject(i);

                final String vMINOR_CD = jObject.getString("CODE");
                final String vMINOR_NM = jObject.getString("NAME");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                listItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, listItem);
            insert_user.setAdapter(adapter);

            //로딩시 기본값 세팅
            insert_user.setSelection(adapter.getPosition(itemBase));
            //cmbBizPartner.setSelection();

            //콤보박스 클릭 이벤트 정의
            insert_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    insert_user_query = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
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

    private void dbQuery_get_move_type() {      //작업장 스피너
        Thread workThd_get_move_type = new Thread() {
            public void run() {
                String sql = " SELECT '' AS CODE, '' AS NAME";
                sql += " UNION ALL ";
                sql += " SELECT 'LOCATION' AS CODE, '적치장 이동' AS NAME ";
                sql += " UNION ALL ";
                sql += " SELECT 'STORAGE' AS CODE, '창고 이동' AS NAME";
                sql += " UNION ALL ";
                sql += " SELECT 'TRACKING' AS CODE, 'TRACKING 이동' AS NAME";
                sql += " UNION ALL ";
                sql += " SELECT 'WORKPLACE' AS CODE, '사업장 이동' AS NAME";

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
        workThd_get_move_type.start();   //스레드 시작
        try {
            workThd_get_move_type.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }

        try {
            JSONArray ja = new JSONArray(sJsonCombo);

            final ArrayList<GetComboData> listItem = new ArrayList<>();

            /*기본값 세팅*/
            GetComboData itemBase = new GetComboData();

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jObject = ja.getJSONObject(i);

                final String vMINOR_CD = jObject.getString("CODE");
                final String vMINOR_NM = jObject.getString("NAME");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                listItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, listItem);
            move_type.setAdapter(adapter);

            //로딩시 기본값 세팅
            move_type.setSelection(adapter.getPosition(itemBase));
            //cmbBizPartner.setSelection();

            //콤보박스 클릭 이벤트 정의
            move_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    move_type_query = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
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

    private void start() {
        dbQuery();

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            I25_QUERY_ListViewAdapter listViewAdapter = new I25_QUERY_ListViewAdapter(); //


            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                I25_QUERY item = new I25_QUERY();

                item.ITEM_CD            = jObject.getString("ITEM_CD");             //품번
                item.ITEM_NM            = jObject.getString("ITEM_NM");             //품명
                item.QTY                = jObject.getString("QTY");                 //수량
                item.UPDATE_LOCATION    = jObject.getString("UPDATE_LOCATION");     //적치장 이동

                listViewAdapter.addQueryItem(item);
            }

            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {

                }
            });

            TGSClass.AlertMessage(getApplicationContext(), ja.length() + " 건 조회되었습니다.");
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private void dbQuery() {
        Thread workThddbQuery = new Thread() {
            public void run() {
                String move_date_from_data = move_date_from.getText().toString();
                String move_date_to_data = move_date_to.getText().toString();
                String location = txt_Scan_location.getText().toString();

                String sql = " EXEC XUSP_WMS_I_GOODS_MOVEMENT_QUERY_GET_LIST ";
                sql += " @FLAG = '" + move_type_query + "'";
                sql += " ,@MOV_TYPE = ''";
                sql += " ,@MOVE_DATE_FROM = '" + move_date_from_data + "'";
                sql += " ,@MOVE_DATE_TO = '" + move_date_to_data + "'";
                sql += " ,@SL_CD = '" + sl_cd_query + "'";
                sql += " ,@LOCATION = '" + location + "'";
                sql += " ,@INSRT_USER_ID = '" + insert_user_query + "'";

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
        workThddbQuery.start();   //스레드 시작
        try {
            workThddbQuery.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }
}

