package com.PDA.gmax.I60;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.PDA.gmax.GetComboData;
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


public class I62_HDR_Activity extends BaseActivity {

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(EditText) ==//
    private EditText issue_date, txtDocumentText;

    //== View 선언(Spinner) ==//
    private Spinner storage_location, cost_center, work_center;

    //== View 선언(CheckBox) ==//
    private CheckBox chk_see;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    public Button btn_popup_remove, btn_popup_add, btn_save;

    //== 날짜관련 변수 선언 ==//
    private Calendar cal;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int I62_DTL_REQUEST_CODE = 0;

    Dialog myDialog_popup;
    Dialog myDialog_edit_qty;
    public String sJson_popup ="";
    public String sJsonCombo ="";

    public String chkYN ="N";
    public String setCHK ="N";

    private int popup_count;

    public String TempStorage_location = "";
    public String TempCost_center = "";
    public String TempWork_center = "";

    /*팝업 그리드 바인딩 관련 작업*/
    private ListAdapter adapter;
    private ListAdapter_Edit_Qty adapter_edit_qty;

    public ArrayList<I62_Popup_ListItem> listViewItemList = new ArrayList<I62_Popup_ListItem>(); //리스트뷰
    public ArrayList<I62_Popup_ListItem> listViewItemList_edit_qty = new ArrayList<I62_Popup_ListItem>(); //리스트뷰

    private ArrayList<I62_Popup_ListItem> filteredItemList = listViewItemList; //리스트뷰 임시저장소
    private ArrayList<I62_Popup_ListItem> filteredItemList_edit_qty = listViewItemList_edit_qty; //리스트뷰 임시저장소

    public ArrayList<String>find2 = new ArrayList<>();
    public ArrayList<String>find2_edit_qty = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i62_hdr);

        this.initializeView();

        this.initializeCanlendar();

        this.initializeListener();

        this.initializeData();

        myDialog_popup = new Dialog(this);
        myDialog_edit_qty = new Dialog(this);

        adapter = new ListAdapter();
        listview.setAdapter(adapter);
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent 값 바인딩 ==//
        vMenuID             = getIntent().getStringExtra("MENU_ID");
        vMenuNm             = getIntent().getStringExtra("MENU_NM");
        vMenuRemark         = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand       = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== ID 값 바인딩 ==//
        issue_date          = (EditText) findViewById(R.id.issue_date);
        storage_location    = (Spinner) findViewById(R.id.storage_location);
        cost_center         = (Spinner) findViewById(R.id.cost_center);
        work_center         = (Spinner) findViewById(R.id.work_center);
        txtDocumentText     = (EditText) findViewById(R.id.txtDocumentText);

        chk_see             = (CheckBox) findViewById(R.id.chk_see);
        btn_popup_remove    = (Button) findViewById(R.id.btn_popup_remove);
        btn_popup_add       = (Button) findViewById(R.id.btn_popup_add);
        btn_save            = findViewById(R.id.btn_save);

        listview            = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeCanlendar() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {

        //== 날짜 이벤트 ==//
        issue_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, issue_date, cal);
            }
        });

        //== 체크박스 이벤트 ==//
        chk_see.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    btn_popup_remove.setVisibility(View.VISIBLE);
                    chkYN = "Y";
                } else {
                    btn_popup_remove.setVisibility(View.INVISIBLE);
                    chkYN = "N";
                }

                adapter.notifyDataSetChanged();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                ListAdapter listViewAdapter = (ListAdapter) parent.getAdapter();
                I62_Popup_ListItem item = (I62_Popup_ListItem) listViewAdapter.getItem(position);

                CheckedTextView chk = (CheckedTextView) v.findViewById(R.id.chk);

                if (chk.isChecked() == true) {
                    chk.setChecked(false);
                    item.CHK = "";
                } else {
                    chk.setChecked(true);
                    item.CHK = "Y";
                }
            }
        });

        //== 저장 버튼 이벤트 ==//
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (issue_date.getText().toString().length() != 10) {
                    TGSClass.AlertMessage(getApplicationContext(), "출고일자를 정확히 입력(선택)해주시기 바랍니다");
                } else if (TempStorage_location.equals("") || TempStorage_location.equals("") || TempStorage_location.equals("")) {
                    TGSClass.AlertMessage(getApplicationContext(), "창고, Cost Center, 작업장은\n필수입력사항입니다.");
                } else {
                    ListAdapter listViewAdapter = (ListAdapter) listview.getAdapter();

                    String sl_cd = "";
                    String sl_nm = "";
                    String item_cd = "";
                    String item_nm = "";
                    String basic_unit = "";
                    String lot_no = "";
                    String lot_sub_no = "";
                    String tracking_no = "";
                    String qty = "";
                    String idx_st = "";

                    for (int idx = 0; idx < listViewAdapter.getCount(); idx++) {
                        I62_Popup_ListItem item = (I62_Popup_ListItem) listViewAdapter.getItem(idx);

                        sl_cd           = sl_cd         + item.getSl_cd()               + ",";
                        sl_nm           = sl_nm         + item.getSl_nm()               + ",";
                        item_cd         = item_cd       + item.getItem_cd()             + ",";
                        item_nm         = item_nm       + item.getItem_nm()             + ",";
                        basic_unit      = basic_unit    + item.getBasic_unit()          + ",";
                        lot_no          = lot_no        + item.getLot_no()              + ",";
                        lot_sub_no      = lot_sub_no    + item.getLot_sub_no()          + ",";
                        tracking_no     = tracking_no   + item.getTracking_no()         + ",";
                        qty             = qty           + item.getGood_on_hand_qty()    + ".0,";
                        idx_st          = idx_st        + String.valueOf(idx)           + ",";
                    }

                    if (BL_DATASET_SELECT(sl_nm, sl_cd, item_cd, item_nm, basic_unit, lot_no, lot_sub_no, tracking_no, qty, idx_st) == true) {
                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(I62_HDR_Activity.this);
                        mAlert.setTitle("개발용 자재반입(양품)")
                                .setMessage(result_msg)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (result_msg.contains("출고번호")) {
                                            Intent intent = TGSClass.ChangeView(getPackageName(), I60_Activity.class);
                                            startActivity(intent);
                                        } else {
                                            return;
                                        }
                                    }
                                })
                                .create().show();
                    }
                }
            }
        });
    }

    private void initializeData() {    //초기화
        issue_date.setText(df.format(cal.getTime()));

        dbQuery_get_storage_location();
        dbQuery_get_cost_center();
        dbQuery_get_work_center();
    }

    private void dbQuery_get_storage_location() {   //창고 스피너
        Thread workThd_dbQuery_get_storage_location = new Thread() {
            public void run() {
                String sql = " exec XUSP_I62_HDR_SPINNER_INFO_GET_ANDROID ";
                sql += " @FLAG = 'STORAGE_LOCATION' ";
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
        workThd_dbQuery_get_storage_location.start();   //스레드 시작
        try {
            workThd_dbQuery_get_storage_location.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

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
                final String vCHOICE = jObject.getString("CHOICE");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);
                item.setCHOICE(vCHOICE);

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
                    TempStorage_location = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
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

    private void dbQuery_get_cost_center() {   //창고 스피너
        Thread workThd_dbQuery_get_cost_center = new Thread() {
            public void run() {
                String sql = " exec XUSP_I62_HDR_SPINNER_INFO_GET_ANDROID ";
                sql += " @FLAG = 'COST_CENTER' ";
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
        workThd_dbQuery_get_cost_center.start();   //스레드 시작
        try {
            workThd_dbQuery_get_cost_center.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

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
                final String vCHOICE = jObject.getString("CHOICE");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);
                item.setCHOICE(vCHOICE);

                listItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, listItem);
            cost_center.setAdapter(adapter);

            //로딩시 기본값 세팅
            cost_center.setSelection(adapter.getPosition(itemBase));
            //cmbBizPartner.setSelection();

            //콤보박스 클릭 이벤트 정의
            cost_center.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TempCost_center = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
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

    private void dbQuery_get_work_center() {     //창고 스피너
        Thread workThd_dbQuery_get_work_center = new Thread() {
            public void run() {
                String sql = " exec XUSP_I62_HDR_SPINNER_INFO_GET_ANDROID";
                sql += " @FLAG = 'WORK_CENTER' ";
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
        workThd_dbQuery_get_work_center.start();   //스레드 시작
        try {
            workThd_dbQuery_get_work_center.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

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
                final String vCHOICE = jObject.getString("CHOICE");

                GetComboData item = new GetComboData();
                item.setMINOR_CD(vMINOR_CD);
                item.setMINOR_NM(vMINOR_NM);
                item.setCHOICE(vCHOICE);

                listItem.add(item);
            }

            ArrayAdapter<GetComboData> adapter = new ArrayAdapter<GetComboData>(this, android.R.layout.simple_dropdown_item_1line, listItem);
            work_center.setAdapter(adapter);

            //로딩시 기본값 세팅
            work_center.setSelection(adapter.getPosition(itemBase));
            //cmbBizPartner.setSelection();

            //콤보박스 클릭 이벤트 정의
            work_center.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TempWork_center = ((GetComboData) parent.getSelectedItem()).getMINOR_CD();
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

    /*팝업 관련 함수*/
    protected void showPopup(View v) {
        Button btn_close;
        Button btn_add;
        Button btn_query;

        myDialog_popup.setContentView(R.layout.activity_i62_popup);

        btn_close = (Button) myDialog_popup.findViewById(R.id.btn_cancle);    //종료버튼
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog_popup.dismiss();
            }
        });

        btn_add = (Button) myDialog_popup.findViewById(R.id.btn_add);         //추가버튼
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int CheckCount = 0;
                myDialog_edit_qty.setContentView(R.layout.activity_i62_popup_edit_qty);

                ListView listOrder_edit_qty = (ListView) myDialog_edit_qty.findViewById(R.id.listOrder_edit_qty);
                adapter_edit_qty = new ListAdapter_Edit_Qty();
                listOrder_edit_qty.setAdapter(adapter_edit_qty);

                ListView listview_popup = (ListView) myDialog_popup.findViewById(R.id.listOrder_popup);
                I62_POPUP_ListViewAdapter listViewAdapter_popup = (I62_POPUP_ListViewAdapter) listview_popup.getAdapter();

                if (sJson_popup.equals("[]") || sJson_popup.equals("")) {
                    TGSClass.AlertMessage(getApplicationContext(), "선택된 품목이 없습니다. 품목을 선택해주십시오.");
                    return;
                }

                for (int idx2 = 0; idx2 < listViewAdapter_popup.getCount(); idx2++) {
                    I62_POPUP item = (I62_POPUP) listViewAdapter_popup.getItem(idx2);

                    if (item.getCHK() == "Y") {
                        CheckCount++;
                    }
                }

                if (CheckCount < 1) {
                    TGSClass.AlertMessage(getApplicationContext(), "선택된 품목이 없습니다. 품목을 선택해주십시오.");
                    return;
                }

                for (int idx2 = 0; idx2 < listViewAdapter_popup.getCount(); idx2++) {
                    I62_POPUP item = (I62_POPUP) listViewAdapter_popup.getItem(idx2);

                    if (item.getCHK() == "Y") {
                        String item_cd = item.getITEM_CD();
                        String item_nm = item.getITEM_NM();
                        String tracking_no = item.getTRACKING_NO();
                        String lot_no = item.getLOT_NO();
                        String lot_sub_no = item.getLOT_SUB_NO();
                        String good_on_hand_qty = item.getGOOD_ON_HAND_QTY();
                        String location = item.getLOCATION();
                        String basic_unit = item.getBASIC_UNIT();
                        String sl_cd = item.getSL_CD();
                        String sl_nm = item.getSL_NM();

                        After_Popup_Add_Click(item_cd, item_nm, tracking_no, lot_no, lot_sub_no, good_on_hand_qty, location, basic_unit, sl_cd, sl_nm);
                    }
                }

                Button btn_add_edit_qty = myDialog_edit_qty.findViewById(R.id.btn_add_edit_qty);

                btn_add_edit_qty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListView listview_edit_qty = (ListView) myDialog_edit_qty.findViewById(R.id.listOrder_edit_qty);
                        ListAdapter_Edit_Qty listViewAdapter_edit_qty = (ListAdapter_Edit_Qty) listview_edit_qty.getAdapter();

                        for (int idx2 = 0; idx2 < listViewAdapter_edit_qty.getCount(); idx2++) {

                            I62_Popup_ListItem item = (I62_Popup_ListItem) listViewAdapter_edit_qty.getItem(idx2);

                            String item_cd = item.getItem_cd();
                            String item_nm = item.getItem_nm();
                            String tracking_no = item.getTracking_no();
                            String lot_no = item.getLot_no();
                            String lot_sub_no = item.getLot_sub_no();
                            String good_on_hand_qty = item.getGood_on_hand_qty();
                            String location = item.getLocation();
                            String basic_unit = item.getBasic_unit();
                            String sl_cd = item.getSl_cd();
                            String sl_nm = item.getSl_nm();

                            After_Popup_Add_Edit_Qty_Click(item_cd, item_nm, tracking_no, lot_no, lot_sub_no, good_on_hand_qty, location, basic_unit, sl_cd, sl_nm);
                        }

                        for (int idx2 = 0; idx2 < listViewAdapter_edit_qty.getCount(); idx2++) {
                            if (listViewItemList_edit_qty.size() < 1) {
                            } else {
                                listViewItemList_edit_qty.remove(idx2);
                                idx2--;
                            }
                        }
                        myDialog_edit_qty.dismiss();
                    }
                });

                Button btn_cancle_edit_qty = myDialog_edit_qty.findViewById(R.id.btn_cancle_edit_qty);

                btn_cancle_edit_qty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ListView listview_edit_qty = (ListView) myDialog_edit_qty.findViewById(R.id.listOrder_edit_qty);
                        ListAdapter_Edit_Qty listViewAdapter_edit_qty = (ListAdapter_Edit_Qty) listview_edit_qty.getAdapter();

                        for (int idx2 = 0; idx2 < listViewAdapter_edit_qty.getCount(); idx2++) {
                            if (listViewItemList_edit_qty.size() < 1) {
                            } else {
                                listViewItemList_edit_qty.remove(idx2);
                                idx2--;
                            }
                        }

                        myDialog_edit_qty.dismiss();
                    }
                });

                myDialog_edit_qty.show();
            }
        });

        btn_query = (Button) myDialog_popup.findViewById(R.id.btn_query);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView Scan_location = myDialog_popup.findViewById(R.id.Scan_location);
                TextView Scan_item_cd = myDialog_popup.findViewById(R.id.Scan_item_cd);

                popup_Start(Scan_location.getText().toString(), Scan_item_cd.getText().toString());
            }
        });

        myDialog_popup.show();
    }

    private void popup_Start(final String location, final String item_cd) {
        popup_dbQuery(location, item_cd);

        try {
            JSONArray ja = new JSONArray(sJson_popup);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            final ListView listview = myDialog_popup.findViewById(R.id.listOrder_popup);
            I62_POPUP_ListViewAdapter listViewAdapter = new I62_POPUP_ListViewAdapter(); //

            for (int idx = 0; idx < ja.length(); idx++) {
                JSONObject jObject = ja.getJSONObject(idx);

                String CHK = jObject.getString("CHK");  //적치장
                String LOCATION = jObject.getString("LOCATION");  //적치장
                String ITEM_CD = jObject.getString("ITEM_CD");  //품목코드
                String ITEM_NM = jObject.getString("ITEM_NM");  //품목명
                String TRACKING_NO = jObject.getString("TRACKING_NO");  //TRAKING_NO
                String LOT_NO = jObject.getString("LOT_NO");  //LOT_NO
                String LOT_SUB_NO = jObject.getString("LOT_SUB_NO");  //LOT 순번
                String GOOD_ON_HAND_QTY = jObject.getString("GOOD_ON_HAND_QTY");  //양품수량
                String BASIC_UNIT = jObject.getString("BASIC_UNIT");  //양품수량
                String SL_CD = jObject.getString("SL_CD");  //양품수량
                String SL_NM = jObject.getString("SL_NM");  //양품수량

                listViewAdapter.add_Loading_Place_Item(CHK, LOCATION, ITEM_CD, ITEM_NM, TRACKING_NO, LOT_NO, LOT_SUB_NO, GOOD_ON_HAND_QTY
                        , BASIC_UNIT, SL_CD, SL_NM);
            }

            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {

                    I62_POPUP_ListViewAdapter listViewAdapter = (I62_POPUP_ListViewAdapter) parent.getAdapter();
                    I62_POPUP item = (I62_POPUP) listViewAdapter.getItem(position);
                    CheckedTextView chk = (CheckedTextView) v.findViewById(R.id.chk);

                    //CheckedTextView chk = (CheckedTextView) myDialog.findViewById(R.id.chk);

                    if (chk.isChecked() == true) {
                        chk.setChecked(false);
                        item.CHK = "";
                    } else {
                        chk.setChecked(true);
                        item.CHK = "Y";
                    }
                }
            });
        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());
        }
    }

    private void popup_dbQuery(final String location, final String item_cd) {
        Thread workThd_popup_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_I62_POPUP_INFO_GET_ANDROID ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@LOCATION = '" + location + "' ";
                sql += " ,@ITEM_CD = '" + item_cd + "' ";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_popup = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workThd_popup_dbQuery.start();   //스레드 시작
        try {
            workThd_popup_dbQuery.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    public class ListAdapter_Edit_Qty extends BaseAdapter {
        @Override
        public int getCount() {
            return filteredItemList_edit_qty.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredItemList_edit_qty.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final int pos = filteredItemList_edit_qty.get(position).getNum();
            final Context context = parent.getContext();
            final I62_ViewHolder_Edit_Qty holder;

            // "listview_item" Layout을 inflate하여 convertView 참조 획득.
            if (convertView == null) {
                holder = new I62_ViewHolder_Edit_Qty();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_view_i62_popup_edit_qty, parent, false);

                holder.item_cd              = (TextView) convertView.findViewById(R.id.item_cd);
                holder.item_nm              = (TextView) convertView.findViewById(R.id.item_nm);
                holder.tracking_no          = (TextView) convertView.findViewById(R.id.tracking_no);
                holder.lot_no               = (TextView) convertView.findViewById(R.id.lot_no);
                holder.lot_sub_no           = (TextView) convertView.findViewById(R.id.lot_sub_no);
                holder.good_on_hand_qty     = (EditText) convertView.findViewById(R.id.good_on_hand_qty);
                holder.location             = (TextView) convertView.findViewById(R.id.location);
                holder.basic_unit           = (TextView) convertView.findViewById(R.id.basic_unit);
                holder.sl_cd                = (TextView) convertView.findViewById(R.id.sl_cd);
                holder.sl_nm                = (TextView) convertView.findViewById(R.id.sl_nm);

                convertView.setTag(holder);
            } else {
                holder = (I62_ViewHolder_Edit_Qty) convertView.getTag();
            }

            holder.ref = position;

            // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
            final I62_Popup_ListItem listViewItem = filteredItemList_edit_qty.get(position);

            holder.item_cd.setText(listViewItem.getItem_cd());
            holder.item_nm.setText(listViewItem.getItem_nm());
            holder.tracking_no.setText(listViewItem.getTracking_no());
            holder.lot_no.setText(listViewItem.getLot_no());
            holder.lot_sub_no.setText(listViewItem.getLot_sub_no());
            holder.good_on_hand_qty.setText(listViewItem.getGood_on_hand_qty().replace(".0", ""));
            holder.location.setText(listViewItem.getLocation());
            holder.basic_unit.setText(listViewItem.getBasic_unit());
            holder.sl_cd.setText(listViewItem.getSl_cd());
            holder.sl_nm.setText(listViewItem.getSl_nm());

            holder.good_on_hand_qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    filteredItemList_edit_qty.get(holder.ref).setGood_on_hand_qty(s.toString());
                    find2_edit_qty.add(holder.good_on_hand_qty.getText().toString());
                }
            });

            return convertView;
        }

        public void addItem(String item_cd, String item_nm, String tracking_no, String lot_no, String lot_sub_no,
                            String good_on_hand_qty, String location, String basic_unit, String sl_cd, String sl_nm, int num) {
            I62_Popup_ListItem item = new I62_Popup_ListItem();

            item.setItem_cd(item_cd);
            item.setItem_nm(item_nm);
            item.setTracking_no(tracking_no);
            item.setLot_no(lot_no);
            item.setLot_sub_no(lot_sub_no);
            item.setGood_on_hand_qty(good_on_hand_qty);
            item.setLocation(location);
            item.setBasic_unit(basic_unit);
            item.setSl_cd(sl_cd);
            item.setSl_nm(sl_nm);
            item.setNum(num);

            listViewItemList_edit_qty.add(item);
        }
    }

    public class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return filteredItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final int pos = filteredItemList.get(position).getNum();
            final Context context = parent.getContext();
            final I62_ViewHolder holder;

            // "listview_item" Layout을 inflate하여 convertView 참조 획득.
            if (convertView == null) {
                holder = new I62_ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_view_i62_hdr, parent, false);

                holder.chk                  = (CheckedTextView) convertView.findViewById(R.id.chk);
                holder.item_cd              = (TextView) convertView.findViewById(R.id.item_cd);
                holder.item_nm              = (TextView) convertView.findViewById(R.id.item_nm);
                holder.tracking_no          = (TextView) convertView.findViewById(R.id.tracking_no);
                holder.lot_no               = (TextView) convertView.findViewById(R.id.lot_no);
                holder.lot_sub_no           = (TextView) convertView.findViewById(R.id.lot_sub_no);
                holder.good_on_hand_qty     = (TextView) convertView.findViewById(R.id.good_on_hand_qty);
                holder.location             = (TextView) convertView.findViewById(R.id.location);
                holder.basic_unit           = (TextView) convertView.findViewById(R.id.basic_unit);
                holder.sl_cd                = (TextView) convertView.findViewById(R.id.sl_cd);
                holder.sl_nm                = (TextView) convertView.findViewById(R.id.sl_nm);

                convertView.setTag(holder);
            } else {
                holder = (I62_ViewHolder) convertView.getTag();
            }

            holder.ref = position;

            if (chkYN.equals("Y")) {
                holder.chk.setVisibility(View.VISIBLE);
            } else {
                holder.chk.setVisibility(View.INVISIBLE);
            }

            if (setCHK.equals("Click")) {
                holder.chk.setChecked(false);
            }

            // Data Set(filteredItemList)에서 position에 위치한 데이터 참조 획득
            final I62_Popup_ListItem listViewItem = filteredItemList.get(position);

            holder.item_cd.setText(listViewItem.getItem_cd());
            holder.item_nm.setText(listViewItem.getItem_nm());
            holder.tracking_no.setText(listViewItem.getTracking_no());
            holder.lot_no.setText(listViewItem.getLot_no());
            holder.lot_sub_no.setText(listViewItem.getLot_sub_no());
            holder.good_on_hand_qty.setText(listViewItem.getGood_on_hand_qty().replace(".0", ""));
            holder.location.setText(listViewItem.getLocation());
            holder.basic_unit.setText(listViewItem.getBasic_unit());
            holder.sl_cd.setText(listViewItem.getSl_cd());
            holder.sl_nm.setText(listViewItem.getSl_nm());

            return convertView;
        }

        public void addItem(String item_cd, String item_nm, String tracking_no, String lot_no, String lot_sub_no,
                            String good_on_hand_qty, String location, String basic_unit, String sl_cd, String sl_nm, int num) {
            I62_Popup_ListItem item = new I62_Popup_ListItem();

            item.setItem_cd(item_cd);
            item.setItem_nm(item_nm);
            item.setTracking_no(tracking_no);
            item.setLot_no(lot_no);
            item.setLot_sub_no(lot_sub_no);
            item.setGood_on_hand_qty(good_on_hand_qty);
            item.setLocation(location);
            item.setBasic_unit(basic_unit);
            item.setSl_cd(sl_cd);
            item.setSl_nm(sl_nm);
            item.setNum(num);

            listViewItemList.add(item);
        }

        public void delItem(int position) {
            if (listViewItemList.size() < 1) {

            } else {
                listViewItemList.remove(position);
            }
        }
    }

    public void After_Popup_Add_Edit_Qty_Click(final String item_cd, final String item_nm, final String tracking_no, final String lot_no, final String lot_sub_no
            , final String good_on_hand_qty, final String location, final String basic_unit, final String sl_cd, final String sl_nm) {
        adapter.addItem(item_cd, item_nm, tracking_no, lot_no, lot_sub_no, good_on_hand_qty, location, basic_unit, sl_cd, sl_nm, popup_count);
        adapter.notifyDataSetChanged();
        popup_count++;
    }

    public void After_Popup_Add_Click(final String item_cd, final String item_nm, final String tracking_no, final String lot_no, final String lot_sub_no
            , final String good_on_hand_qty, final String location, final String basic_unit, final String sl_cd, final String sl_nm) {
        adapter_edit_qty.addItem(item_cd, item_nm, tracking_no, lot_no, lot_sub_no, good_on_hand_qty, location, basic_unit, sl_cd, sl_nm, popup_count);
        adapter_edit_qty.notifyDataSetChanged();
        popup_count++;
    }

    public void HDR_GridRemove(View v) {    //팝업에서 바인딩해서 가져온 HDR 그리드 데이터 삭제 로직
        if (popup_count == 0) {
            TGSClass.AlertMessage(getApplicationContext(), "삭제할 자료를 없습니다.");
            return;
        } else {
            ListView listview = (ListView) findViewById(R.id.listOrder);
            ListAdapter listViewAdapter = (ListAdapter) listview.getAdapter();
            CheckedTextView chkTV = (CheckedTextView) findViewById(R.id.chk);

            for (int idx2 = 0; idx2 < listViewAdapter.getCount(); idx2++) {
                I62_Popup_ListItem item = (I62_Popup_ListItem) listViewAdapter.getItem(idx2);

                if (item.getChk() == "Y") {
                    adapter.delItem(idx2);
                    popup_count--;  // additem 할때마다 popup_count가 1씩 증가함, 다 지웠을 땐 0이 되고 0일땐 삭제할 자료가 없으므로 조회 기능을 return 시킴
                    idx2--; //그리드에 액션을 취한뒤 rowcount를 가진다.

                     /*
                     delitem을 시도하였을 때 0,1,2번째 행을 지울거라고 한다면 보통 모든 행을 읽고 몇번째, 몇번째 행을 지울건지 컴퓨터가 읽고 지운다고 생각하지만
                     지금 delitem은 한 행마다 액션을 취하기 때문에 0번째를 지웠을때 지우고 나서 1번째 행을 읽기때문에 지우기 전의 2번째 행을 지워버리게 됨 그렇기에 지울때마다 idx2를 1씩 내림
                     2020.02.21 정영진 주임
                     */
                }
            }

            setCHK = "Click";
            adapter.notifyDataSetChanged();
        }
    }

    public boolean BL_DATASET_SELECT(final String sl_nm, final String sl_cd, final String item_cd, final String item_nm, final String basic_unit,
                                     final String lot_no, final String lot_sub_no, final String tracking_no, final String qty, final String idx_st) {
        Thread workThd_BL_DATASET_SELECT = new Thread() {
            public void run() {

                //EditText issue_date = findViewById(R.id.issue_date); // 출고일, 회계전표일 병합
                // TempStorage_location                             // 선택한 창고코드
                // TempCost_center                                  // 선택한 Cost Center
                // TempWork_center                                  // 선택한 Work Center
                //EditText txtDocumentText = findViewById(R.id.txtDocumentText);  // 비고

                String item_document_no_param = "";                                       // 출고번호 -> 자동생성으로 변경(2021-02-23 정영진 주임) BL에서 자동생성하도록 빈값 바인딩
                String mov_type_param = "I94";                                    // 수불유형 -> 개발용 자재반입(양품)
                String document_dt_param = issue_date.getText().toString();          // 출고일
                String pos_dt_param = issue_date.getText().toString();          // 회계전표일
                String document_text_param = txtDocumentText.getText().toString();     // remark
                String cost_cd_param = TempCost_center;                          // Cost Center
                String plant_param = vPLANT_CD;                                // 공장코드
                String work_cd_param = TempWork_center;                          // Work Center
                String unit_cd = vUNIT_CD;                                 // 단말기코드

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                /* HDR */
                PropertyInfo parm = new PropertyInfo();
                parm.setName("item_document_no_param");
                parm.setValue(item_document_no_param);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("mov_type_param");
                parm2.setValue(mov_type_param);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("document_dt_param");
                parm3.setValue(document_dt_param);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("pos_dt_param");
                parm4.setValue(pos_dt_param);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("document_text_param");
                parm5.setValue(document_text_param);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("cost_cd_param");
                parm6.setValue(cost_cd_param);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("plant_param");
                parm7.setValue(plant_param);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("work_cd_param");
                parm8.setValue(work_cd_param);
                parm8.setType(String.class);

                /* DTL의 DataTable을 생성하기 위하여 필요한 파라미터들*/
                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("sl_nm");
                parm9.setValue(sl_nm);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("sl_cd");
                parm10.setValue(sl_cd);
                parm10.setType(String.class);

                PropertyInfo parm11 = new PropertyInfo();
                parm11.setName("item_cd");
                parm11.setValue(item_cd);
                parm11.setType(String.class);

                PropertyInfo parm12 = new PropertyInfo();
                parm12.setName("item_nm");
                parm12.setValue(item_nm);
                parm12.setType(String.class);

                PropertyInfo parm13 = new PropertyInfo();
                parm13.setName("basic_unit");
                parm13.setValue(basic_unit);
                parm13.setType(String.class);

                PropertyInfo parm14 = new PropertyInfo();
                parm14.setName("lot_no");
                parm14.setValue(lot_no);
                parm14.setType(String.class);

                PropertyInfo parm15 = new PropertyInfo();
                parm15.setName("lot_sub_no");
                parm15.setValue(lot_sub_no);
                parm15.setType(String.class);

                PropertyInfo parm16 = new PropertyInfo();
                parm16.setName("tracking_no");
                parm16.setValue(tracking_no);
                parm16.setType(String.class);

                PropertyInfo parm17 = new PropertyInfo();
                parm17.setName("qty");
                parm17.setValue(qty);
                parm17.setType(String.class);

                PropertyInfo parm18 = new PropertyInfo();
                parm18.setName("idx_st");
                parm18.setValue(idx_st);
                parm18.setType(String.class);

                PropertyInfo parm19 = new PropertyInfo();
                parm19.setName("unit_cd");
                parm19.setValue(unit_cd);
                parm19.setType(String.class);

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
                pParms.add(parm15);
                pParms.add(parm16);
                pParms.add(parm17);
                pParms.add(parm18);
                pParms.add(parm19);

                result_msg = dba.SendHttpMessage("BL_SetPartListETCOut_ANDROID", pParms);
            }
        };
        workThd_BL_DATASET_SELECT.start();   //스레드 시작
        try {
            workThd_BL_DATASET_SELECT.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }
}