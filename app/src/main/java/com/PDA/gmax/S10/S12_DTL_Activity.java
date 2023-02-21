package com.PDA.gmax.S10;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.GetComboData;
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


public class S12_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonCombo = "", sJson_hdr = "", sJson_status = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private S12_HDR vGetHDRItem;

    //== View 선언(TextView) ==//
    private TextView dn_req_no, lbl_count, packingText;

    //== View 선언(EditText) ==//
    private EditText cmbBizPartner, DN_RQ_DT;

    //== View 선언(Spinner) ==//
    public Spinner cmbMgmtUser, cmbTrans, cmbSL;
    public int Number_cmbBizPartner, Number_UserID, Number_cmbTrans, Number_cmbSL;

    //== View 선언(CheckBox) ==//
    private CheckBox chkAR, chkVAT;

    //== View 선언(Button) ==//
    private Button btn_save, btn_packing;

    //== View 선언(LinearLayout) ==//
    private LinearLayout app_view0;

    //== 날짜 관련 변수 선언 ==//
    private Calendar cal;

    public String ComboFlag;

    //==포장실적 구분 변수 선언==//
    //PACKAGE => 포장등록 페이지인지 구분
    //Packing => 포장상태 구분
    private boolean PACKAGE, Packing = false;

    S12_DTL_ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s12_dtl);

        this.initializeView();

        this.initializeCalendar();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID = getIntent().getStringExtra("MENU_ID");
        vMenuNm = getIntent().getStringExtra("MENU_NM");
        vMenuRemark = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드
        PACKAGE = getIntent().getBooleanExtra("PACKAGE", false); //false => 출하관리,true => 포장실적

        //== HDR 값 바인딩 ==//
        vGetHDRItem = (S12_HDR) getIntent().getSerializableExtra("HDR");

        //== ID 값 바인딩 ==//
        dn_req_no = (TextView) findViewById(R.id.dn_req_no);
        lbl_count = (TextView) findViewById(R.id.lbl_count);
        packingText = (TextView) findViewById(R.id.packingText);

        DN_RQ_DT = (EditText) findViewById(R.id.DN_RQ_DT);
        cmbBizPartner = (EditText) findViewById(R.id.cmbBizPartner);
        cmbMgmtUser = (Spinner) findViewById(R.id.cmbMgmtUser);
        cmbTrans = (Spinner) findViewById(R.id.cmbTrans);
        cmbSL = (Spinner) findViewById(R.id.cmbSL);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_packing = (Button) findViewById(R.id.btn_packing);
        chkAR = (CheckBox) findViewById(R.id.chkAR);
        chkVAT = (CheckBox) findViewById(R.id.chkVAT);
        app_view0 = (LinearLayout) findViewById(R.id.app_view0);


    }

    private void initializeCalendar() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {
        DN_RQ_DT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, DN_RQ_DT, cal);
            }
        });

       /* cmbBizPartner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        cmbMgmtUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cmbTrans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btn_packing.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //포장중으로 변경
                if (PACKAGE & Packing) {
                    set_PackingViews("P");

                    dbSave_Status();
                    return;
                }
                Intent intent = TGSClass.ChangeView(getPackageName(), S12_PKG_Activity.class);
                intent.putExtra("REQ_NO", dn_req_no.getText().toString());
                startActivityForResult(intent, 0);

            }
        });


        //출고처리 버튼
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //포장완료로 변경
                if (!Packing & PACKAGE) {

                    ArrayList<S12_DTL> items = (ArrayList<S12_DTL>) listViewAdapter.getItems();

                    for (S12_DTL item : items) {
                        if (Integer.parseInt(item.getGI_QTY()) > Integer.parseInt(item.getPACKAGING_QTY())) {
                            check_packing();
                            return;
                        }
                    }
                    set_PackingViews("S");

                    dbSave_Status();
                    return;
                }

                //출고 확인 메시지 및 출고처리
                check_Out();

            }
        });
    }

    private void initializeData() {
        ComboFlag = "cmbTrans";

        // 가져온 값 할당
        dn_req_no.setText(vGetHDRItem.getDN_REQ_NO());

        DN_RQ_DT.setText(df.format(cal.getTime()));

        cmbBizPartner.setText(vGetHDRItem.getSHIP_TO_PARTY_NM());

        //공정 리스트 콤보 값을 초기화 한다.
        //dbQuery_get_cmbBizPartner();
        dbQuery_get_cmbMgmtUser();
        dbQuery_get_cmbTrans();
        dbQuery_get_cmbSL();

        //dbQuery_get_ship_to_party(vGetHDRItem.getSHIP_TO_PARTY());
        //dbQuery_get_userID(global.getLoginString());
        dbQuery_get_trans_meth(vGetHDRItem.getTRANS_METH());
        //dbQuery_get_sl_cd(vGetHDRItem.getSL_CD());


        //포장실적이 아닌 출하관리로 페이지 사용시 컨트롤 변경
        String status = "N";
        if (!PACKAGE) {
            status = "";
        }
        set_PackingViews(status);
        start();


    }

    //출하등록 BL
    private boolean dbQuery_GET_BL(final String cud_flag, final String flag, final String bp_cd, final String dn_req_no, final String dn_req_seq,
                                   final String actual_gi_dt, final String trans_meth, final String ar_flag, final String vat_flag, final String inv_mgr) {
        Thread wkThd_dbQuery_GET_BL = new Thread() {
            public void run() {
                result_msg = "";

                String cud_flag_parm = cud_flag;
                String flag_parm = flag;
                String bp_cd_parm = bp_cd;          //납품처
                String dn_req_no_parm = dn_req_no;      //출하요청번호
                String dn_req_seq_parm = dn_req_seq;     //빈값
                String actual_gi_dt_parm = actual_gi_dt;   //출고일자
                String trans_meth_parm = trans_meth;     //운송방법
                String ar_flag_parm = ar_flag;        //매출채권
                String vat_flag_parm = vat_flag;       //세금계산서
                String inv_mgr_parm = inv_mgr;        //담당자
                String unit_cd = vUNIT_CD;        //단말기코드

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("cud_flag");
                parm.setValue(cud_flag_parm);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("flag");
                parm2.setValue(flag_parm);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("bp_cd");
                parm3.setValue(bp_cd_parm);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("dn_req_no");
                parm4.setValue(dn_req_no_parm);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("dn_req_seq");
                parm5.setValue(dn_req_seq_parm);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("actual_gi_dt");
                parm6.setValue(actual_gi_dt_parm);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("trans_meth");
                parm7.setValue(trans_meth_parm);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("ar_flag");
                parm8.setValue(ar_flag_parm);
                parm8.setType(Integer.class);

                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("vat_flag");
                parm9.setValue(vat_flag_parm);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("inv_mgr");
                parm10.setValue(inv_mgr_parm);
                parm10.setType(String.class);

                PropertyInfo parm11 = new PropertyInfo();
                parm11.setName("unit_cd");
                parm11.setValue(unit_cd);
                parm11.setType(String.class);

                PropertyInfo parm12 = new PropertyInfo();
                parm12.setName("user_id");
                parm12.setValue(vUSER_ID);
                parm12.setType(String.class);

                pParms.add(parm);
                pParms.add(parm2);
                pParms.add(parm3);
                pParms.add(parm4);
                pParms.add(parm5);
                pParms.add(parm6);
                pParms.add(parm7);
                pParms.add(parm8);
                pParms.add(parm9);
                pParms.add(parm10);
                pParms.add(parm11);
                pParms.add(parm12);

                result_msg = dba.SendHttpMessage("BL_Set_Shipment_ANDROID", pParms);
            }
        };
        wkThd_dbQuery_GET_BL.start();   //스레드 시작
        try {
            wkThd_dbQuery_GET_BL.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }


    private void start() {
        //dbQuery(vGetHDRItem.getPLANT_CD(), vGetHDRItem.getDN_REQ_NO());
        dbQuery(vPLANT_CD, vGetHDRItem.getDN_REQ_NO());
        System.out.println("sjson:" + sJson);

        try {
            JSONArray ja = new JSONArray(sJson);

            ListView listview = findViewById(R.id.listOrder);
            listViewAdapter = new S12_DTL_ListViewAdapter();

            //포장실적으로 진입시 포장수량 표시되는 listview 로딩하도록 설정
            listViewAdapter.setPackaging(PACKAGE);

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                S12_DTL sItem = new S12_DTL();

                sItem.CHK = jObject.getString("CHK");
                sItem.ITEM_CD = jObject.getString("ITEM_CD");
                sItem.ITEM_NM = jObject.getString("ITEM_NM");
                sItem.GI_QTY = jObject.getString("GI_QTY");
                sItem.GOOD_ON_HAND_QTY = jObject.getString("GOOD_ON_HAND_QTY");

                sItem.SO_UNIT = jObject.getString("SO_UNIT");
                sItem.SO_NO = jObject.getString("SO_NO");
                sItem.SO_SEQ = jObject.getInt("SO_SEQ");
                sItem.BASIC_UNIT = jObject.getString("BASIC_UNIT");
                sItem.TRACKING_NO = jObject.getString("TRACKING_NO");
                sItem.LOT_NO = jObject.getString("LOT_NO");
                sItem.LOT_SEQ = jObject.getString("LOT_SEQ");
                //sItem.DN_TYPE = jObject.getString("DN_TYPE");
                sItem.SALES_GRP = jObject.getString("SALES_GRP");
                sItem.PACKAGING_QTY = jObject.getString("PACKGING_QTY");
                sItem.PACKING_STATUS = jObject.getString("PACKING_STATUS");

                if (PACKAGE) {
                    set_PackingViews(sItem.PACKING_STATUS);
                }
                listViewAdapter.add_Shipment_Dtl_Item(sItem);
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


    //출하요청 번호에 대한 상세정보 확인
    private void dbQuery(final String plant_cd, final String dn_req_no) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_TPC_SHIPMENT_QUERY_DETAIL ";
                sql += " @PLANT_CD = '" + plant_cd + "'";
                sql += " ,@DN_REQ_NO = '" + dn_req_no + "'";

                System.out.println("sql:"+sql);

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

    private void dbQuery_get_cmbMgmtUser() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery_get_cmbMgmtUser = new Thread() {
            public void run() {
                String sql = " exec XUSP_TPC_SHIPMENT_GET_COMBODATA";
                sql += " @FLAG = 'cmbMgmtUser'";
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
        wkThd_dbQuery_get_cmbMgmtUser.start();   //스레드 시작
        try {
            wkThd_dbQuery_get_cmbMgmtUser.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

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
                final String vMINOR_CD = jObject.getString("CODE");
                final String vMINOR_NM = jObject.getString("NAME");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                lstItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, lstItem);

            cmbMgmtUser.setAdapter(adapter);
            //로딩시 기본값 세팅
            cmbMgmtUser.setSelection(Number_UserID);
            //콤보박스 클릭 이벤트 정의
            cmbMgmtUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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



    private void dbQuery_get_cmbTrans() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery_get_cmbTrans = new Thread() {
            public void run() {
                String sql = " exec XUSP_TPC_SHIPMENT_GET_COMBODATA ";
                sql += " @FLAG = 'cmbTrans'";
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
        wkThd_dbQuery_get_cmbTrans.start();   //스레드 시작
        try {
            wkThd_dbQuery_get_cmbTrans.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.
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
                final String vMINOR_CD = jObject.getString("CODE");
                final String vMINOR_NM = jObject.getString("NAME");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                lstItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, lstItem);

            cmbTrans.setAdapter(adapter);
            //로딩시 기본값 세팅
            cmbTrans.setSelection(Number_cmbTrans);
            //콤보박스 클릭 이벤트 정의
            cmbTrans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

    private void dbQuery_get_cmbSL() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery_get_cmbSL = new Thread() {
            public void run() {
                String sql = " exec XUSP_TPC_SHIPMENT_GET_COMBODATA ";
                sql += " @FLAG = 'cmbSL'";
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
        wkThd_dbQuery_get_cmbSL.start();   //스레드 시작
        try {
            wkThd_dbQuery_get_cmbSL.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

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
                final String vMINOR_CD = jObject.getString("CODE");
                final String vMINOR_NM = jObject.getString("NAME");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                lstItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, lstItem);

            cmbSL.setAdapter(adapter);
            //로딩시 기본값 세팅

            //cmbSL.setSelection(adapter.getPosition(itemBase));
            cmbSL.setSelection(Number_cmbSL);
            //콤보박스 클릭 이벤트 정의
            cmbSL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

    //납품처 조회 쿼리
    private String dbQuery_get_BP_CD() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery_get_cmbMgmtUser = new Thread() {
            public void run() {
                String sql = " SELECT　DISTINCT BP_CD AS CODE FROM B_BIZ_PARTNER";
                sql += " WHERE BP_NM = '"+cmbBizPartner.getText().toString()+"'";

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
        wkThd_dbQuery_get_cmbMgmtUser.start();   //스레드 시작
        try {
            wkThd_dbQuery_get_cmbMgmtUser.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }

        try {
            JSONArray ja = new JSONArray(sJsonCombo);

            final ArrayList<GetComboData> lstItem = new ArrayList<>();

            /*기본값 세팅*/
            GetComboData itemBase = new GetComboData();

            if(ja.length()>0){
                JSONObject jObject = ja.getJSONObject(0);
                return jObject.getString("CODE");
            }
        } catch (JSONException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        } catch (Exception ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }
        return"";
    }

    private void dbQuery_get_trans_meth(final String Value) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery_get_trans_meth = new Thread() {
            public void run() {
                String sql = " exec XUSP_TPC_SHIPMENT_GET_COMBODATA_FIND_NUM ";
                sql += " @FLAG = 'cmbTrans_support' ";
                sql += ", @VALUE = '" + Value + "'";
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
        wkThd_dbQuery_get_trans_meth.start();   //스레드 시작
        try {
            wkThd_dbQuery_get_trans_meth.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

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
                final int vNum = jObject.getInt("NUM");
                final String vMINOR_CD = jObject.getString("CODE");
                final String vMINOR_NM = jObject.getString("NAME");

                GetComboData item = new GetComboData();
                item.setNUM(vNum);
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);

                Number_cmbTrans = vNum;
                lstItem.add(item);
            }
        } catch (JSONException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        } catch (Exception ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        }
        //Value = "";
    }



    //출고처리 시작
    private void Shipment_ANDROID(){
        //GetComboData Partner = (GetComboData) cmbBizPartner.getSelectedItem();  //Class에 담겨진 데이터를 가져오기
        GetComboData User = (GetComboData) cmbMgmtUser.getSelectedItem();
        GetComboData Trans = (GetComboData) cmbTrans.getSelectedItem();
        GetComboData SL = (GetComboData) cmbSL.getSelectedItem();

        String chkAR_checkvalue = "N";
        String chkVAT_checkvalue = "N";

        chkAR_checkvalue = chkAR.isChecked() == true ? "Y" : "N"; //매출채권 선택, 미선택 확인 작업
        chkVAT_checkvalue = chkVAT.isChecked() == true ? "Y" : "N";

        String cud_flag_st = "C";                                  //
        String flag_st = "SR";                                 //
        String cmbBizPartner_st = dbQuery_get_BP_CD();//Partner.getMINOR_CD();                //납품처
        String dn_req_no_st = dn_req_no.getText().toString();       //출하요청번호
        String dn_req_seq = "";                                   //
        String dn_rq_dt_st = DN_RQ_DT.getText().toString();      //출고일자
        String cmbTrans_st = Trans.getMINOR_CD();                  //운송방법
        String ar_flag_st = chkAR_checkvalue;                     //매출채권
        String vat_flag_st = chkVAT_checkvalue;                    //세금계산서
//                String cmbMgmtUser_st   = User.getMINOR_CD();                   //등록자
        String cmbMgmtUser_st = "";                   //등록자


        //출하 BL 실행
        if (dbQuery_GET_BL(cud_flag_st, flag_st, cmbBizPartner_st, dn_req_no_st, dn_req_seq, dn_rq_dt_st,
                cmbTrans_st, ar_flag_st, vat_flag_st, cmbMgmtUser_st) == true) {
            System.out.println("returnmessage:"+result_msg);

        }

        if (!result_msg.contains("출하번호")) {
            TGSClass.AlertMessage(getApplicationContext(), result_msg, 50000);
            return;
        }
        else{
            result_msg = result_msg.replace("출하번호 : ","");
        }

        if (dbSave_HDR(result_msg) == false) {
            TGSClass.AlertMessage(getApplicationContext(), sJson_hdr, 50000);
            return;
        }

        try {
            JSONArray ja_hdr = new JSONArray(sJson_hdr);
            JSONObject jObject_hdr = ja_hdr.getJSONObject(0);

            String RTN_ITEM_DOCUMENT_NO = jObject_hdr.getString("RTN_ITEM_DOCUMENT_NO");

            JSONArray ja = new JSONArray(sJson);

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                String sl_cd = jObject.getString("SL_CD");
                String item_cd = jObject.getString("ITEM_CD");
                String tracking_no = jObject.getString("TRACKING_NO");
                String lot_no = jObject.getString("LOT_NO");
                String lot_sub_no = jObject.getString("LOT_SEQ");
                String qty = jObject.getString("GOOD_ON_HAND_QTY");
                String basic_unit = jObject.getString("BASIC_UNIT");
                String location = jObject.getString("LOCATION");
                String bad_on_hand_qty = jObject.getString("BAD_ON_HAND_QTY");

                dbSave_DTL(RTN_ITEM_DOCUMENT_NO, sl_cd, item_cd, tracking_no, lot_no, lot_sub_no, qty, basic_unit, location, bad_on_hand_qty);

            }
        } catch (JSONException ex) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
        } catch (Exception e1) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : e1");
        }


        TGSClass.AlertMessage(getApplicationContext(), "출하처리가 완료되었습니다",50000);

        // 저장 후 결과 값 돌려주기
        Intent resultIntent = new Intent();
        // 결과처리 후 부른 Activity에 보낼 값
        resultIntent.putExtra("SIGN", "EXIT");
        // 부른 Activity에게 결과 값 반환
        setResult(RESULT_OK, resultIntent);
        // 현재 Activity 종료
        finish();
    }


    //포장상태 변경 메소드
    private void set_PackingViews(String result){
        System.out.println("packing:"+result);
        // p=> 포장중 / s=> 포장완료 / N => 포장기록 없음(초기상태) / default => 즉시 출고
        switch (result){
            case "P":
                btn_packing.setText("포장등록");
                btn_save.setText("포장완료");
                packingText.setText("포장중");
                //colorAccent
                packingText.setTextColor(Color.rgb(216,27,96));
                Packing =false;
                break;
            case "S":



                btn_packing.setText("재포장");
                btn_save.setText("출고처리");
                packingText.setText("포장완료");
                packingText.setTextColor(Color.BLUE);
                Packing =true;
                break;
            case "N":
                btn_packing.setText("포장실적");
                btn_save.setText("포장완료");
                packingText.setText("대기");
                //colorGold
                packingText.setTextColor(Color.rgb(244,157,9));
                Packing =false;
                break;
            default:
                btn_save.setText("출고처리");
                btn_packing.setVisibility(View.GONE);
                lbl_count.setText("     품번     |      품목명      |     요청량     |    재고수량 ");
                app_view0.setVisibility(View.GONE);
                btn_save.setText("출고처리");
                break;

        }
    }


    private boolean dbSave_HDR(String dlv_no) {
        Thread wkThd_dbSave_HDR = new Thread() {
            public void run() {
                String DN_RQ_DT_st = DN_RQ_DT.getText().toString();

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_HDR_SET ";
                sql += "@TRNS_TYPE = 'DI'";
                sql += ",@MOV_TYPE = 'I31'";
                sql += ",@DOCUMENT_DT = '" + DN_RQ_DT_st + "'";
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";
                sql += ",@RTN_ITEM_DOCUMENT_NO = ''";
                sql += ",@DLV_NO = '"+ dlv_no+"'";

                System.out.println("sql:"+sql);
                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_hdr = dba.SendHttpMessage("GetSQLData", pParms);

            }
        };
        wkThd_dbSave_HDR.start();   //스레드 시작
        try {
            wkThd_dbSave_HDR.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
        }

        if (sJson.equals("[]")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean dbSave_DTL(final String ITEM_DOCUMENT_NO, final String SL_CD, final String ITEM_CD, final String TRACKING_NO,
                              final String LOT_NO, final String LOT_SUB_NO, final String QTY, final String BASIC_UNIT,
                              final String LOCATION, final String BAD_ON_HAND_QTY) {
        Thread wkThd_dbSave_DTL = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_WMS_I_GOODS_MOVEMENT_DTL_SET_CALCUATE_ANDROID ";
                sql += "@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "'";    //채번
                // += ",@DOCUMENT_YEAR =";                                  //프로시저 안에서 적용
                sql += ",@TRNS_TYPE = 'DI'";                                  //변경유형
                sql += ",@MOV_TYPE = 'I31'";                                   //이동유형
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";                  //공장코드
                sql += ",@DOCUMENT_DT = '" + DN_RQ_DT.getText().toString() + "'";            //이동일자(t)

                /* I_ONHAND_STOCK_DETAIL 에서 바인딩 받아야 하므로 ListView에 조회되도록 SELECT 프로시저에 DTL항목 추가하고 바인딩 한 후 가져와야됨*/
                sql += ",@SL_CD = '" + SL_CD + "'";                             //창고코드
                sql += ",@ITEM_CD = '" + ITEM_CD + "'";                         //품목코드
                sql += ",@TRACKING_NO = '" + TRACKING_NO + "'";                 //TRACKING_NO
                sql += ",@LOT_NO = '" + LOT_NO + "'";                           //LOT_NO
                sql += ",@LOT_SUB_NO = " + LOT_SUB_NO;                    //LOT_SUB_NO
                sql += ",@QTY = " + Integer.parseInt(QTY);                                  //양품수량
                sql += ",@BASE_UNIT = '" + BASIC_UNIT + "'";                    //재고단위
                sql += ",@LOC_CD = '" + LOCATION + "'";                         //기존의 적치장
                sql += ",@TRNS_TRACKING_NO = '" + TRACKING_NO + "'";            /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_NO = '" + LOT_NO + "'";                      /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_SUB_NO = " + LOT_SUB_NO;              /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_PLANT_CD = '" + vPLANT_CD + "'";                 /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_SL_CD = '" + SL_CD + "'";                        /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_ITEM_CD = '" + ITEM_CD + "'";                    /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOC_CD = '" + LOCATION + "'";   //이동할 적치장

                sql += ",@BAD_ON_HAND_QTY = " + BAD_ON_HAND_QTY;         //불량 수량
                sql += ",@MOVE_QTY = " + Integer.parseInt(QTY);                  //이동 수량
                sql += ",@DEBIT_CREDIT_FLAG = 'C'";                  //이동 수량

                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";
                sql += ",@EXTRA_FIELD1 = 'ANDROID'";
                sql += ",@EXTRA_FIELD2 = 'S12_DTL_Activity'";

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
        wkThd_dbSave_DTL.start();   //스레드 시작
        try {
            wkThd_dbSave_DTL.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }
    private void dbSave_Status() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {
                String status = Packing ? "S" : "P";

                String sql = "UPDATE S_DN_REQ_HDR SET";
                sql += " PACKING_STATUS = '" + status + "'";
                sql += " where DN_REQ_NO = '" + dn_req_no.getText().toString() + "'";

                dataSaveLog("포장완료 플래그 변경","Shipment");
                dataSaveLog(sql,"Shipment");

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_status = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        wkThd_dbQuery.start();   //스레드 시작
        try {
            wkThd_dbQuery.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }


    private void check_packing() {

        new AlertDialog.Builder(this)
                .setTitle("포장확인")
                .setMessage("포장수량이 요청량보다 적습니다 포장완료 처리 하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        set_PackingViews("S");

                        dbSave_Status();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }

    private void check_Out() {
        new AlertDialog.Builder(this)
                .setTitle("출고 확인")
                .setMessage("등록한 실적을 출고 하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //출고처리
                        Shipment_ANDROID();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        start();
    }

}
