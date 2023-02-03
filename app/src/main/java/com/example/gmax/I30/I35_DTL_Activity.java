package com.example.gmax.I30;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gmax.BaseActivity;
import com.example.gmax.DBAccess;
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


public class I35_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private I35_HDR vGetHDRItem;

    //== View 선언(TextView) ==//
    private TextView storage_location, location, item_cd, item_nm, qty, req_qty;
    private TextView grid_chk_qty_sum, remain_req_qty;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(EditText) ==//
    private EditText move_date, out_qty_insert;

    //== View 선언(Button) ==//
    private Button btn_add, btn_save;

    //== 날짜 관련 변수 선언 ==//
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i35_dtl);

        this.initializeView();

        this.initializeCalendar();

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

        //== HDR 값 바인딩 ==//
        vGetHDRItem         = (I35_HDR)getIntent().getSerializableExtra("HDR");

        //== ID 값 바인딩 ==//
        storage_location    = (TextView)findViewById(R.id.storage_location);
        location            = (TextView)findViewById(R.id.location);
        item_cd             = (TextView)findViewById(R.id.item_cd);
        item_nm             = (TextView)findViewById(R.id.item_nm);
        qty                 = (TextView)findViewById(R.id.qty);
        req_qty             = (TextView)findViewById(R.id.req_qty);

        grid_chk_qty_sum    = (TextView)findViewById(R.id.grid_chk_qty_sum);
        remain_req_qty      = (TextView)findViewById(R.id.remain_req_qty);
        req_qty             = (TextView)findViewById(R.id.req_qty);
        move_date           = (EditText)findViewById(R.id.move_date);

        btn_save            = (Button)findViewById(R.id.btn_save);
        btn_add             = (Button)findViewById(R.id.btn_add);

        listview            = (ListView) findViewById(R.id.listOrder);
    }

    private void initializeCalendar() {
        cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date());
    }

    private void initializeListener() {
        //== 날짜 이벤트 ==//
        move_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupDate(v, move_date, cal);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 기존 소스는 DBSave() 메서드로 옮김
                dbSave("EXIT");

                /*
                EditText move_qty = findViewById(R.id.move_qty);

                ListView listview = (ListView) findViewById(R.id.listOrder);
                I35_DTL_ListViewAdapter listViewAdapter = (I35_DTL_ListViewAdapter) listview.getAdapter();
                Double move_qty_double = Double.parseDouble(move_qty.getText().toString());

                for (int idx2 = 0; idx2 < listViewAdapter.getCount(); idx2++)
                {
                    I35_DTL item = (I35_DTL) listViewAdapter.getItem(idx2);

                    if(item.getCHK() == "Y")
                    {
                        double qty =  Double.parseDouble(item.getGOOD_ON_HAND_QTY());

                        if(qty < move_qty_double)
                        {
                            TGSClass.AlertMessage(getApplicationContext(), "현재고수량보다 이동수량이 초과되었습니다.");
                            return;
                        }
                    }
                }

                 */
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                // 기존 소스
                Intent intent = TGSClass.ChangeView(getPackageName(), I35_HDR_Activity.class);
                startActivity(intent);
                 */

                // 기존 소스는 DBSave() 메서드로 옮김
                dbSave("ADD");

            }
        });
    }

    private void initializeData() {
        storage_location.setText(vGetHDRItem.getSL_CD());
        location.setText(vGetHDRItem.getLOCATION());
        item_cd.setText(vGetHDRItem.getITEM_CD());
        item_nm.setText(vGetHDRItem.getITEM_NM());
        qty.setText(vGetHDRItem.getOUT_QTY());
        req_qty.setText(vGetHDRItem.getREQ_QTY());

        grid_chk_qty_sum.setText("0");

        move_date.setText(df.format(cal.getTime()));

        start();
    }

    private void start() {

        dbQuery(vGetHDRItem.getITEM_CD(), vGetHDRItem.getSL_CD(), vGetHDRItem.getTRACKING_NO(), vGetHDRItem.getLOCATION());

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            I35_DTL_ListViewAdapter listViewAdapter = new I35_DTL_ListViewAdapter(); //

            //I35_DTL item = (I35_DTL) listViewAdapter.getItem(0);
            // 캐치 에러뜸 (고쳐야댐) 2021.01.20
            // 행이 1개일 때 요청량 바인딩 하는거 해야됨

            remain_req_qty.setText("0");

            if (ja.length() == 1) {
                JSONObject jObject = ja.getJSONObject(0);

                String CHK = "N";
                String CHK_QTY = "";

                /*if (req_qty.getText().toString().equals("0.0"))        //요청량이 0이면 체크 안함
                {
                    CHK = "N";
                } else {
                    CHK = "Y";
                }*/
                CHK = req_qty.getText().toString().equals("0.0") ? "N" : "Y";

                if (CHK.equals("Y")) {
                    double double_good_on_hand_qty = Double.parseDouble(jObject.getString("GOOD_ON_HAND_QTY"));
                    double double_req_qty = Double.parseDouble(req_qty.getText().toString());
                    double double_remain_req_qty = Double.parseDouble(remain_req_qty.getText().toString());

                    if (double_good_on_hand_qty >= double_req_qty) {
                        grid_chk_qty_sum.setText(String.valueOf(double_req_qty));
                        CHK_QTY = String.valueOf(double_req_qty);
                        remain_req_qty.setText("0");
                    } else {
                        grid_chk_qty_sum.setText(String.valueOf(double_good_on_hand_qty));
                        CHK_QTY = String.valueOf(double_good_on_hand_qty);

                        double_remain_req_qty = double_req_qty - double_good_on_hand_qty;
                        remain_req_qty.setText(String.valueOf(double_remain_req_qty));
                    }
                } else {
                    remain_req_qty.setText(req_qty.getText().toString());
                }

                I35_DTL item = new I35_DTL();

                item.CHK                = CHK;
                item.TRACKING_NO        = jObject.getString("TRACKING_NO");         //Tracking 번호
                item.LOT_NO             = jObject.getString("LOT_NO");              //Lot번호
                item.GOOD_ON_HAND_QTY   = jObject.getString("GOOD_ON_HAND_QTY");    //양품수량 = HDR의 QTY
                item.BAD_ON_HAND_QTY    = jObject.getString("BAD_ON_HAND_QTY");     //불량수량

                item.SL_CD              = jObject.getString("SL_CD");               //창고코드
                item.ITEM_CD            = jObject.getString("ITEM_CD");             //품목코드
                item.LOT_SUB_NO         = jObject.getString("LOT_SUB_NO");          //LOT_SUB_NO
                item.BASIC_UNIT         = jObject.getString("BASIC_UNIT");          //재고단위
                item.LOCATION           = jObject.getString("LOCATION");            //재고단위
                item.CHK_QTY            = CHK_QTY;

                listViewAdapter.addDTLItem(item);
            } else {
                remain_req_qty.setText(req_qty.getText().toString());

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    I35_DTL item = new I35_DTL();

                    item.CHK                = jObject.getString("CHK");                 //체크 이벤트
                    item.TRACKING_NO        = jObject.getString("TRACKING_NO");         //Tracking 번호
                    item.LOT_NO             = jObject.getString("LOT_NO");              //Lot번호
                    item.GOOD_ON_HAND_QTY   = jObject.getString("GOOD_ON_HAND_QTY");    //양품수량
                    item.BAD_ON_HAND_QTY    = jObject.getString("BAD_ON_HAND_QTY");     //불량수량

                    item.SL_CD              = jObject.getString("SL_CD");               //창고코드
                    item.ITEM_CD            = jObject.getString("ITEM_CD");             //품목코드
                    item.LOT_SUB_NO         = jObject.getString("LOT_SUB_NO");          //LOT_SUB_NO
                    item.BASIC_UNIT         = jObject.getString("BASIC_UNIT");          //재고단위
                    item.LOCATION           = jObject.getString("LOCATION");            //재고단위
                    item.CHK_QTY            = "0";

                    listViewAdapter.addDTLItem(item);
                }
            }
            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {

                    I35_DTL_ListViewAdapter listViewAdapter = (I35_DTL_ListViewAdapter) parent.getAdapter();
                    I35_DTL item = (I35_DTL) listViewAdapter.getItem(position);

                    CheckedTextView chk = (CheckedTextView) v.findViewById(R.id.chk);
                    TextView chk_qty = (TextView) v.findViewById(R.id.chk_qty);

                    double double_grid_chk_qty_sum = Double.parseDouble(grid_chk_qty_sum.getText().toString());                        // 총 이동수량
                    double req_qty_int = Double.parseDouble(req_qty.getText().toString());      // 요청량
                    int good_int = Integer.parseInt(item.GOOD_ON_HAND_QTY.replace(".0", ""));                // 그리드의 양품수량
                    double double_remain_req_qty = Double.parseDouble(remain_req_qty.getText().toString());    // 연산하기 위한 수량

                    if (chk.isChecked() == true) {      //체크를 풀려고 했을 때

                        double_remain_req_qty = double_remain_req_qty + Double.parseDouble(chk_qty.getText().toString());
                        remain_req_qty.setText(String.valueOf(double_remain_req_qty));

                        double_grid_chk_qty_sum = double_grid_chk_qty_sum - Double.parseDouble(chk_qty.getText().toString());
                        grid_chk_qty_sum.setText(String.valueOf(double_grid_chk_qty_sum));

                        chk_qty.setText("0");
                        item.setCHK_QTY(chk_qty.getText().toString());

                        chk.setChecked(false);
                        item.CHK = "";

                    } else {        //체크 할려고 했을 때
                        if (double_remain_req_qty == 0) {
                            TGSClass.AlertMessage(getApplicationContext(), "남은 요청량이 없습니다");
                            return;
                        } else {
                            if (good_int >= req_qty_int - double_grid_chk_qty_sum)     //선택한 양품 수량이 요청량보다 크거나 같을 경우
                            {
                                grid_chk_qty_sum.setText(String.valueOf(req_qty_int));

                                chk_qty.setText(String.valueOf(req_qty_int - double_grid_chk_qty_sum));
                                item.setCHK_QTY(chk_qty.getText().toString());

                                remain_req_qty.setText("0");
                            } else                            // 선택한 양품 수량이 요청량보다 작은 경우
                            {
                                double_grid_chk_qty_sum = double_grid_chk_qty_sum + good_int;
                                grid_chk_qty_sum.setText(String.valueOf(double_grid_chk_qty_sum));

                                chk_qty.setText(String.valueOf(good_int));
                                item.setCHK_QTY(chk_qty.getText().toString());

                                double_remain_req_qty = double_remain_req_qty - good_int;
                                remain_req_qty.setText(String.valueOf(double_remain_req_qty));
                            }
                        }

                        chk.setChecked(true);
                        item.CHK = "Y";
                    }
                }
            });

        } catch (JSONException ex) {
            TGSClass.AlertMessage(this, "catch : ex");
        } catch (Exception e1) {
            TGSClass.AlertMessage(this, "catch : e1");
        }
    }

    private void dbQuery(final String item_cd_data, final String sl_cd_data, final String tracking_no_data, final String location_cd) {
        Thread workThd_dbQuery = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DTL_GET_LIST_I35 ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@ITEM_CD = '" + item_cd_data + "' ";
                sql += " ,@SL_CD = '" + sl_cd_data + "' ";
                sql += " ,@TRACKING_NO = '" + tracking_no_data + "' ";

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

    private void dbSave(String value) {
/*
        if (!req_qty.getText().equals(grid_chk_qty_sum.getText())) {
            TGSClass.AlertMessage(getApplicationContext(), "요청 량과 이동할 수량의 합계가 동일하지 않습니다.", 3000);
            return;
        }
*/

        // 기존 소스
        if (dbSave_HDR() == true) {
            try {
                JSONArray ja = new JSONArray(sJson);
                int idx = 0;
                JSONObject jObject = ja.getJSONObject(idx);

                String RTN_ITEM_DOCUMENT_NO = jObject.getString("RTN_ITEM_DOCUMENT_NO");

                I35_DTL_ListViewAdapter listViewAdapter2 = (I35_DTL_ListViewAdapter) listview.getAdapter();

                for (int idx2 = 0; idx2 < listViewAdapter2.getCount(); idx2++) {
                    I35_DTL item = (I35_DTL) listViewAdapter2.getItem(idx2);

                    if (item.getCHK() == "Y") {
                        String sl_cd            = item.getSL_CD();
                        String item_cd          = item.getITEM_CD();
                        String tracking_no      = item.getTRACKING_NO();
                        String lot_no           = item.getLOT_NO();
                        String lot_sub_no       = item.getLOT_SUB_NO();
                        String qty              = item.getGOOD_ON_HAND_QTY();
                        String basic_unit       = item.getBASIC_UNIT();
                        String location         = item.getLOCATION();
                        String bad_on_hand_qty  = item.getBAD_ON_HAND_QTY();
                        String chk_qty          = item.getCHK_QTY();

                        dbSave_DTL(RTN_ITEM_DOCUMENT_NO, sl_cd, item_cd, tracking_no, lot_no, lot_sub_no, qty, basic_unit, location, bad_on_hand_qty, chk_qty);
                    }
                }
                TGSClass.AlertMessage(getApplicationContext(), RTN_ITEM_DOCUMENT_NO + "자동입력번호로 저장되었습니다.");

                // 저장 후 결과 값 돌려주기
                Intent resultIntent = new Intent();
                // 결과처리 후 부른 Activity에 보낼 값
                resultIntent.putExtra("SIGN", value);
                // 부른 Activity에게 결과 값 반환
                setResult(RESULT_OK, resultIntent);
                // 현재 Activity 종료
                finish();
            } catch (JSONException ex) {
                TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
            } catch (Exception e1) {
                TGSClass.AlertMessage(getApplicationContext(), "catch : e1");
            }
        } else {
            TGSClass.AlertMessage(getApplicationContext(), "저장 되지 않았습니다. 데이터를 정확히 입력하였는지 확인해주시기 바랍니다!");
            return;
        }
    }

    private boolean dbSave_HDR() {
        Thread workThd_dbSave_HDR = new Thread() {
            public void run() {

                String move_date_st = move_date.getText().toString();

                String vPRODT_ORDER_NO_st = vGetHDRItem.getPRODT_ORDER_NO();

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_HDR_SET ";
                sql += "@TRNS_TYPE = 'AN'";
                sql += ",@MOV_TYPE = 'I35'";
                sql += ",@DOCUMENT_DT = '" + move_date_st + "'";
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@RECORD_NO = '" + vPRODT_ORDER_NO_st + "'"; //정영진
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";
                sql += ",@RTN_ITEM_DOCUMENT_NO = ''";

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
        workThd_dbSave_HDR.start();   //스레드 시작
        try {
            workThd_dbSave_HDR.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
        }

        if (sJson.equals("[]")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean dbSave_DTL(final String ITEM_DOCUMENT_NO, final String SL_CD, final String ITEM_CD, final String TRACKING_NO, final String LOT_NO,
                              final String LOT_SUB_NO, final String QTY, final String BASIC_UNIT, final String LOCATION, final String BAD_ON_HAND_QTY,
                              final String CHK_QTY) {
        Thread workThd_dbSave_DTL = new Thread() {
            public void run() {

                String move_date_data = move_date.getText().toString();

                String vPRODT_ORDER_NO_st = vGetHDRItem.getPRODT_ORDER_NO();

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DTL_SET ";
                sql += "@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "'";        //채번
                // += ",@DOCUMENT_YEAR =";                                      //프로시저 안에서 적용
                sql += ",@TRNS_TYPE = 'AN'";                                  //변경유형
                sql += ",@MOV_TYPE = 'I35'";                                    //아동유형
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";                      //공장코드
                sql += ",@DOCUMENT_DT = '" + move_date_data + "'";              //이동일자

                /* I_ONHAND_STOCK_DETAIL 에서 바인딩 받아야 하므로 ListView에 조회되도록 SELECT 프로시저에 DTL항목 추가하고 바인딩 한 후 가져와야됨*/
                sql += ",@SL_CD = '" + SL_CD + "'";                             //창고코드
                sql += ",@ITEM_CD = '" + ITEM_CD + "'";                         //품목코드
                sql += ",@TRACKING_NO = '" + TRACKING_NO + "'";                 //TRACKING_NO
                sql += ",@LOT_NO = '" + LOT_NO + "'";                           //LOT_NO
                sql += ",@LOT_SUB_NO = " + LOT_SUB_NO;                          //LOT_SUB_NO
                sql += ",@QTY = " + QTY;                                        //양품수량
                sql += ",@BASE_UNIT = '" + BASIC_UNIT + "'";                    //재고단위
                sql += ",@LOC_CD = '" + LOCATION + "'";                         //기존의 적치장
                sql += ",@TRNS_TRACKING_NO = '" + TRACKING_NO + "'";            /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_NO = '" + LOT_NO + "'";                      /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOT_SUB_NO = " + LOT_SUB_NO;                     /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_PLANT_CD = '" + vPLANT_CD + "'";                 /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_SL_CD = '" + SL_CD + "'";                        /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_ITEM_CD = '" + ITEM_CD + "'";                    /*적치장이동 프로그램에서는 적치장을 제외한 정보는 변하지 않음*/
                sql += ",@TRNS_LOC_CD = '출고대기장'";                   //이동할 적치장
                sql += ",@RECORD_NO = '" + vPRODT_ORDER_NO_st + "'";       //제조오더(비고)

                sql += ",@BAD_ON_HAND_QTY = " + BAD_ON_HAND_QTY;                //불량 수량
                sql += ",@MOVE_QTY = " + CHK_QTY;                        //이동 수량   //현재 이동 수량을 막아놨기 때문에 혹시 풀 경우 move_date_data로 다시 선언 하면 됨

                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";
                sql += ",@EXTRA_FIELD1 = 'ANDROID'";
                sql += ",@EXTRA_FIELD2 = 'I35_DTL_Activity'";

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
        workThd_dbSave_DTL.start();   //스레드 시작
        try {
            workThd_dbSave_DTL.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
        }
        return true;
    }
}