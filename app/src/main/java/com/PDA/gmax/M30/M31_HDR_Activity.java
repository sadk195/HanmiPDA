package com.PDA.gmax.M30;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.PDA.gmax.BaseActivity;
import com.PDA.gmax.DBAccess;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class M31_HDR_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJson_po_type_cd = "", sJson_uniscm = "", sJson_update_issue_mthd = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(TextView) ==//
    private TextView app_title;
    private TextView item_cd, item_nm, spec, dlv_qty, dlv_no_hide;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode;

    //== View 선언(EditText) ==//
    private EditText dlv_no, move_date;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    private Button btn_query, btn_save;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    public String sPO_TYPE_CD ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m31_hdr);

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
        app_title           = (TextView) findViewById(R.id.app_title);

        img_barcode         = (ImageView) findViewById(R.id.img_barcode);
        dlv_no              = (EditText) findViewById(R.id.dlv_no);

        item_cd             = (TextView) findViewById(R.id.item_cd);
        item_nm             = (TextView) findViewById(R.id.item_nm);
        spec                = (TextView) findViewById(R.id.spec);
        dlv_qty             = (TextView) findViewById(R.id.dlv_qty);
        dlv_no_hide         = (TextView) findViewById(R.id.dlv_no_hide);

        move_date           = (EditText) findViewById(R.id.move_date);

        btn_query           = (Button) findViewById(R.id.btn_query);
        btn_save            = (Button) findViewById(R.id.btn_save);

        listview            = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeCalendar() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {

        move_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, move_date, cal);
            }
        });

        img_barcode.setOnClickListener(qrClickListener);

        dlv_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String dlv_no_data = dlv_no.getText().toString();

                    if (dlv_no_data.equals("")) {
                        TGSClass.AlertMessage(getApplicationContext(), "조회조건 [거래명세서]는 필수입력사항입니다.");
                    } else {
                        start();
                    }
                    return true;
                }
                return false;
            }
        });

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dlv_no_data = dlv_no.getText().toString();

                if (dlv_no_data.equals("")) {
                    TGSClass.AlertMessage(getApplicationContext(), "조회조건 [거래명세서]는 필수입력사항입니다.");
                } else {
                    start();
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String dlv_no_data = dlv_no_hide.getText().toString();

                if (import_customs_chk(dlv_no_data) == false) {
                    return;
                } else {
                    if (CHK_DLV_QTY() == true) {
                        dbQuery_update_ISSUE_MTHD(dlv_no_data);

                        try {
                            JSONArray ja = new JSONArray(sJson);

                            String CUD_FLAG = "C";
                            String MVMT_DT = move_date.getText().toString();

                            for (int idx = 0; idx < ja.length(); idx++) {
                                JSONObject jObject = ja.getJSONObject(idx);

                                String PO_NO            = jObject.getString("PO_NO");
                                String PO_SEQ_NO        = jObject.getString("PO_SEQ_NO");      // => INT로 받아야함
                                String INSPECT_FLG      = jObject.getString("INSPECT_FLG");
                                String LOT_NO           = jObject.getString("LOT_NO");
                                String PUR_TYPE_CD      = jObject.getString("PUR_TYPE_CD");
                                String PRODT_ORDER_NO   = jObject.getString("PRODT_ORDER_NO");
                                String OPR_NO           = jObject.getString("OPR_NO");
                                String DLV_NO           = jObject.getString("DLV_NO");

                                String SER_NO           = jObject.getString("SER_NO");
                                String MVMT_QTY         = jObject.getString("DLV_QTY");

                                dbQuery_GET_BL(CUD_FLAG, MVMT_DT, PO_NO, PO_SEQ_NO, SER_NO, MVMT_QTY, LOT_NO, PUR_TYPE_CD, PRODT_ORDER_NO, OPR_NO, DLV_NO);
                            }
                        } catch (JSONException ex) {
                            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
                        } catch (Exception e1) {
                            TGSClass.AlertMessage(getApplicationContext(), "catch : e1");
                        }
                    } else {
                        return;
                    }
                }
            }
        });
    }

    private void initializeData() {

        move_date.setText(df.format(cal.getTime()));
    }

    private void start() {
        String dlv_no_data = dlv_no.getText().toString();

        dbQuery_DLV_INFO(dlv_no_data);

        if (sJson.equals("[]")) {
            TGSClass.AlertMessage(getApplicationContext(), "거래명세서 정보가 없습니다.");
            return;
        } else {
            SET_TEXT_DLV_INFO();
        }
    }

    private void dbQuery_DLV_INFO(final String dlv_no) {
        Thread wkThd_dbQuery_DLV_INFO = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_APK_M31_GRID_GET_LIST ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@DLV_NO = '" + dlv_no + "' ";
                sql += " ,@SER_NO = 0";

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
        wkThd_dbQuery_DLV_INFO.start();   //스레드 시작
        try {
            wkThd_dbQuery_DLV_INFO.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private void SET_TEXT_DLV_INFO() {
        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            M31_HDR_ListViewAdapter listViewAdapter = new M31_HDR_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                String DLV_NO           = jObject.getString("DLV_NO");                    //거래명세서 번호
                String SER_NO           = jObject.getString("SER_NO");                    //순번
                String ITEM_CD          = jObject.getString("ITEM_CD");                  //품번
                String ITEM_NM          = jObject.getString("ITEM_NM");                  //품명
                String DLV_QTY          = jObject.getString("DLV_QTY");                  //수량
                String CONFIRM_DLV_QTY  = jObject.getString("CONFIRM_DLV_QTY");  //확인수량
                String INSPECT_FLG      = jObject.getString("INSPECT_FLG");          //검사여부
                String PUR_TYPE         = jObject.getString("PUR_TYPE");                //발주유형

                String SPEC             = jObject.getString("SPEC");                        //규격

                listViewAdapter.add_Item(DLV_NO, SER_NO, ITEM_CD, ITEM_NM, DLV_QTY, CONFIRM_DLV_QTY, INSPECT_FLG, PUR_TYPE, SPEC);
            }

            JSONObject jObject_hdr = ja.getJSONObject(0);

            dlv_no_hide.setText(jObject_hdr.getString("DLV_NO"));               //거래명세서번호
            item_cd.setText(jObject_hdr.getString("ITEM_CD"));                  //품번
            item_nm.setText(jObject_hdr.getString("ITEM_NM"));                  //품명
            spec.setText(jObject_hdr.getString("SPEC"));                        //규격
            dlv_qty.setText(jObject_hdr.getString("DLV_QTY"));                  //수량

            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    M31_HDR vItem = (M31_HDR) parent.getItemAtPosition(position);

                    item_cd.setText(vItem.getITEM_CD());                  //품번
                    item_nm.setText(vItem.getITEM_NM());                  //품명
                    spec.setText(vItem.getSPEC());                        //규격
                    dlv_qty.setText(vItem.getDLV_QTY());                  //수량
                }
            });
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    //입하등록시 구매품이 수입건 일때 체크하는 로직
    public boolean import_customs_chk(final String dlv_no_data) {
        if (dlv_no_data.equals("")) {
            TGSClass.AlertMessage(getApplicationContext(), "입하대상을 검색하세요.");
            return false;
        } else {
            dbQuery_IMPORT_CUSTOMS_CHK_PO_TYPE_CD(dlv_no_data);

            if (sJson_po_type_cd.equals("[]")) {
                TGSClass.AlertMessage(getApplicationContext(), dlv_no_data + " 거래명세서 번호에 해당하는 발주타입(PO_TYPE_CD)가 없습니다.");
                return false;
            } else {
                SELECT_PO_TYPE_CD();

                if (sPO_TYPE_CD.equals("PO")) {
                    dbQuery_IMPORT_CUSTOMS_CHK_UNISCM(dlv_no_data);

                    if (sJson_uniscm.equals("[]")) {
                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(M31_HDR_Activity.this);
                        mAlert.setTitle("입하등록")
                                .setMessage("통관등록이 되어 있지 않습니다.")
                                .setPositiveButton("확인", null)
                                .create().show();

                        return false;
                    } else {
                        try {
                            JSONArray ja = new JSONArray(sJson_uniscm);

                            for (int idx = 0; idx < ja.length(); idx++) {
                                JSONObject jObject = ja.getJSONObject(idx);

                                String DLV_NO   = jObject.getString("CC_NO");
                                String SER_NO   = jObject.getString("ID_NO");
                                String ITEM_CD  = jObject.getString("ID_DT");

                                if (DLV_NO == null || SER_NO == null || ITEM_CD == null) {
                                    MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(M31_HDR_Activity.this);
                                    mAlert.setTitle("입하등록")
                                            .setMessage("통관등록이 되어 있지 않습니다.")
                                            .setPositiveButton("확인", null)
                                            .create().show();

                                    return false;
                                }
                            }
                        } catch (JSONException ex) {
                            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
                            return false;
                        } catch (Exception e1) {
                            TGSClass.AlertMessage(getApplicationContext(), "catch : e1");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void dbQuery_IMPORT_CUSTOMS_CHK_PO_TYPE_CD(final String dlv_no) {
        Thread wkThd_dbQuery_IMPORT_CUSTOMS_CHK_PO_TYPE_CD = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_APK_M31_IMPORT_CUSTOMS_CHK ";
                sql += " @FLAG = 'PO_TYPE_CD' ";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@DLV_NO = '" + dlv_no + "' ";
                sql += " ,@SER_NO = 0";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_po_type_cd = dba.SendHttpMessage("GetSQLData", pParms);
            }


        };
        wkThd_dbQuery_IMPORT_CUSTOMS_CHK_PO_TYPE_CD.start();   //스레드 시작
        try {
            wkThd_dbQuery_IMPORT_CUSTOMS_CHK_PO_TYPE_CD.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private void SELECT_PO_TYPE_CD() {
        try {
            JSONArray ja = new JSONArray(sJson_po_type_cd);

            JSONObject jObject = ja.getJSONObject(0);

            sPO_TYPE_CD = jObject.getString("PO_TYPE_CD");

        } catch (JSONException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(getApplicationContext(), e1.getMessage());
        }
    }

    private void dbQuery_IMPORT_CUSTOMS_CHK_UNISCM(final String dlv_no) {
        Thread wkThd_dbQuery_IMPORT_CUSTOMS_CHK_UNISCM = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_APK_M31_IMPORT_CUSTOMS_CHK ";
                sql += " @FLAG = 'UNISCM' ";
                sql += " ,@PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@DLV_NO = '" + dlv_no + "' ";
                sql += " ,@SER_NO = 0";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_uniscm = dba.SendHttpMessage("GetSQLData", pParms);
            }


        };
        wkThd_dbQuery_IMPORT_CUSTOMS_CHK_UNISCM.start();   //스레드 시작
        try {
            wkThd_dbQuery_IMPORT_CUSTOMS_CHK_UNISCM.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private void dbQuery_update_ISSUE_MTHD(final String dlv_no) {
        Thread wkThd_dbQuery_udate_ISSUE_MTHD = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_Udate_ISSUE_MTHD_LIST ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@DLV_NO = '" + dlv_no + "' ";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_update_issue_mthd = dba.SendHttpMessage("GetSQLData", pParms);
            }


        };
        wkThd_dbQuery_udate_ISSUE_MTHD.start();   //스레드 시작
        try {
            wkThd_dbQuery_udate_ISSUE_MTHD.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    private boolean CHK_DLV_QTY() {
        try {
            JSONArray ja = new JSONArray(sJson);

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                String ITEM_CD = jObject.getString("ITEM_CD");                  //품번
                String ITEM_NM = jObject.getString("ITEM_NM");                  //품명
                String DLV_QTY = jObject.getString("DLV_QTY");                  //수량
                String CONFIRM_DLV_QTY = jObject.getString("CONFIRM_DLV_QTY");  //확인수량

                if (!DLV_QTY.equals(CONFIRM_DLV_QTY)) {
                    MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(M31_HDR_Activity.this);
                    mAlert.setTitle("입하등록 수량체크")
                            .setMessage(ITEM_CD + "(" + ITEM_NM + ")" + " 품목의 입고 수량" + "(" + DLV_QTY + ")" + "과 입고확인수량" + "(" + CONFIRM_DLV_QTY + ")" + " 정보가 일치하지 않습니다.")
                            .setPositiveButton("확인", null)
                            .create().show();

                    return false;
                }
            }

        } catch (JSONException ex) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
            return false;
        } catch (Exception e1) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : e1");
            return false;
        }
        return true;
    }

    private boolean dbQuery_GET_BL(final String cud_flag, final String mvmt_dt, final String po_no, final String po_seq_no, final String ser_no, final String mvmt_qty,
                                  final String lot_no, final String pur_type_cd, final String prodt_order_no, final String opr_no, final String dlv_no) {
        Thread wkThd_dbQuery_GET_BL = new Thread() {
            public void run() {

                String cud_flag_parm            = cud_flag;
                String mvmt_dt_parm             = mvmt_dt;
                String po_no_parm               = po_no;
                String po_seq_no_parm           = po_seq_no;
                String ser_no_parm              = ser_no;
                String mvmt_qty_parm            = mvmt_qty;
                String lot_no_parm              = lot_no;
                String pur_type_cd_parm         = pur_type_cd;
                String prodt_order_no_parm      = prodt_order_no;
                String opr_no_parm              = opr_no;
                String dlv_no_parm              = dlv_no;
                String unit_cd_parm             = vUNIT_CD;
                String plant_cd_parm            = vPLANT_CD;
                String user_id_parm             = vUSER_ID;

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("cud_flag");
                parm.setValue(cud_flag_parm);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("mvmt_dt_parm");
                parm2.setValue(mvmt_dt_parm);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("po_no_parm");
                parm3.setValue(po_no_parm);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("po_seq_no_parm");
                parm4.setValue(po_seq_no_parm);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("ser_no_parm");
                parm5.setValue(ser_no_parm);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("mvmt_qty_parm");
                parm6.setValue(mvmt_qty_parm);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("lot_no_parm");
                parm7.setValue(lot_no_parm);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("pur_type_cd_parm");
                parm8.setValue(pur_type_cd_parm);
                parm8.setType(Integer.class);

                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("prodt_order_no_parm");
                parm9.setValue(prodt_order_no_parm);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("opr_no_parm");
                parm10.setValue(opr_no_parm);
                parm10.setType(String.class);

                PropertyInfo parm11 = new PropertyInfo();
                parm11.setName("dlv_no_parm");
                parm11.setValue(dlv_no_parm);
                parm11.setType(String.class);

                PropertyInfo parm12 = new PropertyInfo();
                parm12.setName("unit_cd");
                parm12.setValue(unit_cd_parm);
                parm12.setType(String.class);

                PropertyInfo parm13 = new PropertyInfo();
                parm13.setName("plant_cd");
                parm13.setValue(plant_cd_parm);
                parm13.setType(String.class);

                PropertyInfo parm14 = new PropertyInfo();
                parm14.setName("user_id");
                parm14.setValue(user_id_parm);
                parm14.setType(String.class);

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
                pParms.add(parm13);
                pParms.add(parm14);

                result_msg = dba.SendHttpMessage("BL_RCPT_REGISTATION_ANDROID", pParms);  //BL 등록해야됨
            }
        };
        wkThd_dbQuery_GET_BL.start();   //스레드 시작
        try {
            wkThd_dbQuery_GET_BL.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }
}