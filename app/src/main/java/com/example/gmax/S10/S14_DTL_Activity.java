package com.example.gmax.S10;

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


public class S14_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private S14_HDR vGetHDRItem;

    //== View 선언(TextView) ==//
    private TextView dn_req_no, sl_cd, sl_nm, item_cd, item_nm, tracking_no, good_on_hand_qty, req_qty, txt_Scan_move_location, grid_chk_qty_sum;

    //== View 선언(TextView_INVISIBLE) ==//
    private TextView remain_req_qty, req_qty2;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(EditText) ==//
    private EditText move_date;

    //== View 선언(Button) ==//
    private Button btn_add, btn_save;

    //== 날짜 관련 변수 선언 ==//
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s14_dtl);

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
        vStartCommand       = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== HDR 값 바인딩 ==//
        vGetHDRItem         = (S14_HDR)getIntent().getSerializableExtra("HDR");

        //== ID 값 바인딩 ==//
        dn_req_no           = (TextView) findViewById(R.id.dn_req_no);
        sl_cd               = (TextView) findViewById(R.id.sl_cd);
        sl_nm               = (TextView) findViewById(R.id.sl_nm);
        item_cd             = (TextView) findViewById(R.id.item_cd);
        item_nm             = (TextView) findViewById(R.id.item_nm);
        tracking_no         = (TextView) findViewById(R.id.tracking_no);
        good_on_hand_qty    = (TextView) findViewById(R.id.good_on_hand_qty);
        req_qty             = (TextView) findViewById(R.id.req_qty);

        remain_req_qty      = (TextView) findViewById(R.id.remain_req_qty);

        listview            = (ListView) findViewById(R.id.listOrder);

        move_date           = (EditText) findViewById(R.id.move_date);
        grid_chk_qty_sum    = (TextView) findViewById(R.id.grid_chk_qty_sum);

        btn_save            = (Button) findViewById(R.id.btn_save);
        btn_add             = (Button) findViewById(R.id.btn_add);
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

        //== 대기장 이동/종료 버튼 ==//
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 기존 소스는 DBSave() 메서드로 옮김
                dbSave("EXIT");
            }
        });

        //== 대기장 이동/추가 버튼 ==//
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 기존 소스는 DBSave() 메서드로 옮김
                dbSave("ADD");
            }
        });
    }

    private void initializeData() {
        dn_req_no.setText(vGetHDRItem.getDN_REQ_NO());                                      //출하요청번호
        sl_cd.setText(vGetHDRItem.getSL_CD());                                              //창고코드
        sl_nm.setText(vGetHDRItem.getSL_NM());                                              //창고명
        item_cd.setText(vGetHDRItem.getITEM_CD());                                          //품번
        item_nm.setText(vGetHDRItem.getITEM_NM());                                          //품명
        good_on_hand_qty.setText(vGetHDRItem.getGOOD_ON_HAND_QTY());                        //양품 수량
        tracking_no.setText(vGetHDRItem.getTRACKING_NO());                                  //양품 수량
        req_qty.setText(vGetHDRItem.getREQ_QTY().replace(".0", ""));    //요청량

        move_date.setText(df.format(cal.getTime()));
        grid_chk_qty_sum.setText("0");

        start();
    }

    public void start() {
        dbQuery(vGetHDRItem.getITEM_CD(), vGetHDRItem.getSL_CD(), vGetHDRItem.getTRACKING_NO());

        try {
            JSONArray ja = new JSONArray(sJson);

            // 빈 데이터 리스트 생성.
            //final ArrayList<String> items = new ArrayList<String>();
            S14_DTL_ListViewAdapter listViewAdapter = new S14_DTL_ListViewAdapter(); //

            //S14_DTL item = (S14_DTL) listViewAdapter.getItem(0);
            // 캐치 에러뜸 (고쳐야댐) 2021.01.20
            // 행이 1개일 때 요청량 바인딩 하는거 해야됨

            remain_req_qty.setText("0");

            if (ja.length() == 1) {
                JSONObject jObject = ja.getJSONObject(0);

                String CHK = "N";
                String CHK_QTY = "";

                /*
                if (req_qty.getText().toString().replace(".0", "").equals("0"))        //요청량이 0이면 체크 안함
                {
                    CHK = "N";
                } else {
                    CHK = "Y";
                }
                 */

                CHK = req_qty.getText().toString().replace(".0", "").equals("0") ? "N" : "Y"; // 요청량이 0이면 체크 안 함

                if (CHK.equals("Y")) {
                    int good_on_hand_qty_int = Integer.parseInt(jObject.getString("GOOD_ON_HAND_QTY").replace(".0", ""));
                    int req_qty_int = Integer.parseInt(req_qty.getText().toString());
                    int remain_req_qty_int = Integer.parseInt(remain_req_qty.getText().toString());

                    if (good_on_hand_qty_int >= req_qty_int) {
                        grid_chk_qty_sum.setText(String.valueOf(req_qty_int));
                        CHK_QTY = String.valueOf(req_qty_int);
                        remain_req_qty.setText("0");
                    } else {
                        grid_chk_qty_sum.setText(String.valueOf(good_on_hand_qty_int));
                        CHK_QTY = String.valueOf(good_on_hand_qty_int);

                        remain_req_qty_int = req_qty_int - good_on_hand_qty_int;
                        remain_req_qty.setText(String.valueOf(remain_req_qty_int));
                    }
                } else {
                    remain_req_qty.setText(req_qty.getText().toString());
                }

                S14_DTL item = new S14_DTL();

                item.CHK                    = CHK;
                item.TRACKING_NO            = jObject.getString("TRACKING_NO");         //Tracking 번호
                item.LOT_NO                 = jObject.getString("LOT_NO");              //Lot번호
                item.GOOD_ON_HAND_QTY       = jObject.getString("GOOD_ON_HAND_QTY");    //양품수량 = HDR의 QTY
                item.BAD_ON_HAND_QTY        = jObject.getString("BAD_ON_HAND_QTY");     //불량수량

                item.SL_CD                  = jObject.getString("SL_CD");               //창고코드
                item.ITEM_CD                = jObject.getString("ITEM_CD");             //품목코드
                item.LOT_SUB_NO             = jObject.getString("LOT_SUB_NO");          //LOT_SUB_NO
                item.BASIC_UNIT             = jObject.getString("BASIC_UNIT");          //재고단위
                item.LOCATION               = jObject.getString("LOCATION");            //재고단위
                item.CHK_QTY                = CHK_QTY;

                listViewAdapter.addDtlItem(item);
            } else {
                remain_req_qty.setText(req_qty.getText().toString());

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    S14_DTL item = new S14_DTL();

                    item.CHK                    = jObject.getString("CHK");                 //체크 이벤트
                    item.TRACKING_NO            = jObject.getString("TRACKING_NO");         //Tracking 번호
                    item.LOT_NO                 = jObject.getString("LOT_NO");              //Lot번호
                    item.GOOD_ON_HAND_QTY       = jObject.getString("GOOD_ON_HAND_QTY");    //양품수량
                    item.BAD_ON_HAND_QTY        = jObject.getString("BAD_ON_HAND_QTY");     //불량수량

                    item.SL_CD                  = jObject.getString("SL_CD");               //창고코드
                    item.ITEM_CD                = jObject.getString("ITEM_CD");             //품목코드
                    item.LOT_SUB_NO             = jObject.getString("LOT_SUB_NO");          //LOT_SUB_NO
                    item.BASIC_UNIT             = jObject.getString("BASIC_UNIT");          //재고단위
                    item.LOCATION               = jObject.getString("LOCATION");            //적치장코드
                    item.CHK_QTY                = String.valueOf(0);                                // 적치장이동량

                    listViewAdapter.addDtlItem(item);
                }
            }
            listview.setAdapter(listViewAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {

                    S14_DTL_ListViewAdapter listViewAdapter = (S14_DTL_ListViewAdapter) parent.getAdapter();
                    S14_DTL item = (S14_DTL) listViewAdapter.getItem(position);

                    CheckedTextView chk = (CheckedTextView) v.findViewById(R.id.chk);
                    TextView chk_qty = (TextView) v.findViewById(R.id.chk_qty);

                    int grid_chk_qty_sum_int = Integer.parseInt(grid_chk_qty_sum.getText().toString());                        // 총 이동수량
                    int req_qty_int = Integer.parseInt(req_qty.getText().toString().replace(".0", ""));      // 요청량
                    int good_int = Integer.parseInt(item.GOOD_ON_HAND_QTY.replace(".0", ""));                // 그리드의 양품수량
                    int remain_req_qty_int = Integer.parseInt(remain_req_qty.getText().toString().replace(".0", ""));    // 연산하기 위한 수량

                    if (chk.isChecked() == true) {       //체크를 풀려고 했을 때
                        remain_req_qty_int = remain_req_qty_int + Integer.parseInt(chk_qty.getText().toString());
                        remain_req_qty.setText(String.valueOf(remain_req_qty_int));

                        grid_chk_qty_sum_int = grid_chk_qty_sum_int - Integer.parseInt(chk_qty.getText().toString());
                        grid_chk_qty_sum.setText(String.valueOf(grid_chk_qty_sum_int));

                        chk_qty.setText("0");
                        item.setCHK_QTY(chk_qty.getText().toString());

                        chk.setChecked(false);
                        item.CHK = "";
                    } else {        //체크 할려고 했을 때
                        if (remain_req_qty_int == 0) {
                            TGSClass.AlertMessage(getApplicationContext(), "남은 요청량이 없습니다");
                            return;
                        } else {
                            if (good_int >= req_qty_int - grid_chk_qty_sum_int) {    //선택한 양품 수량이 선택할 수 있는 남은 요청량보다 크거나 같을 경우
                                grid_chk_qty_sum.setText(String.valueOf(req_qty_int));

                                chk_qty.setText(String.valueOf(req_qty_int - grid_chk_qty_sum_int));
                                item.setCHK_QTY(chk_qty.getText().toString());

                                remain_req_qty.setText("0");
                            } else {                           // 선택한 양품 수량이 요청량보다 작은 경우
                                grid_chk_qty_sum_int = grid_chk_qty_sum_int + good_int;
                                grid_chk_qty_sum.setText(String.valueOf(grid_chk_qty_sum_int));

                                chk_qty.setText(String.valueOf(good_int));
                                item.setCHK_QTY(chk_qty.getText().toString());

                                remain_req_qty_int = remain_req_qty_int - good_int;
                                remain_req_qty.setText(String.valueOf(remain_req_qty_int));
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

    //== 조회 쿼리 ==//
    private void dbQuery(final String item_cd_data, final String sl_cd_data, final String tracking_no_data) {
        Thread wkThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DTL_GET_LIST_S14 ";
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
        wkThd_dbQuery.start();   //스레드 시작
        try {
            wkThd_dbQuery.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    //== 출하대기장 이동 등록 ==//
    private void dbSave(String val) {
        if (dbSave_HDR() == true) {
            try {
                JSONArray ja = new JSONArray(sJson);
                int idx = 0;
                JSONObject jObject = ja.getJSONObject(idx);

                String RTN_ITEM_DOCUMENT_NO = jObject.getString("RTN_ITEM_DOCUMENT_NO");

                S14_DTL_ListViewAdapter listViewAdapter2 = (S14_DTL_ListViewAdapter) listview.getAdapter();

                for (int idx2 = 0; idx2 < listViewAdapter2.getCount(); idx2++) {
                    S14_DTL item = (S14_DTL) listViewAdapter2.getItem(idx2);

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
                resultIntent.putExtra("SIGN", val);
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

    //== HDR 저장 ==//
    public boolean dbSave_HDR() {
        Thread wkThd_dbSave_HDR = new Thread() {
            public void run() {

                String move_date_st = move_date.getText().toString();

                String vDN_REQ_NO_st = vGetHDRItem.getDN_REQ_NO();
                // 출하에는 제조오더번호를 안쓰고 출하요청번호로 사용함

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_HDR_SET ";
                sql += "@TRNS_TYPE = 'AN'";
                sql += ",@MOV_TYPE = 'S14'";
                sql += ",@DOCUMENT_DT = '" + move_date_st + "'";
                sql += ",@PLANT_CD = '" + vPLANT_CD + "'";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@RECORD_NO = '" + vDN_REQ_NO_st + "'"; //2020.01.26 정영진 컬럼 추가후 바인딩
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

    //== DTL 저장 ==//
    private boolean dbSave_DTL(final String ITEM_DOCUMENT_NO, final String SL_CD, final String ITEM_CD, final String TRACKING_NO, final String LOT_NO,
                               final String LOT_SUB_NO, final String QTY, final String BASIC_UNIT, final String LOCATION, final String BAD_ON_HAND_QTY,
                               final String CHK_QTY) {
        Thread wkThd_dbSave_DTL = new Thread() {
            public void run() {

                String move_date_data = move_date.getText().toString();

                String vDN_REQ_NO_st = vGetHDRItem.getDN_REQ_NO();

                String sql = " EXEC XUSP_WMS_LOCATION_I_GOODS_MOVEMENT_DTL_SET ";
                sql += "@ITEM_DOCUMENT_NO = '" + ITEM_DOCUMENT_NO + "'";        //채번
                // += ",@DOCUMENT_YEAR =";                                      //프로시저 안에서 적용
                sql += ",@TRNS_TYPE = 'MV'";                                    //변경유형
                sql += ",@MOV_TYPE = 'S14'";                                    //아동유형
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
                sql += ",@TRNS_LOC_CD = '출하대기장'";                           //이동할 적치장
                sql += ",@RECORD_NO = '" + vDN_REQ_NO_st + "'";                 //출하요청번호(비고)

                sql += ",@BAD_ON_HAND_QTY = " + BAD_ON_HAND_QTY;                //불량 수량
                sql += ",@MOVE_QTY = " + CHK_QTY;                               //이동 수량   //현재 이동 수량을 막아놨기 때문에 혹시 풀 경우 move_date_data로 다시 선언 하면 됨
                sql += ",@EXTRA_FIELD1 = 'ANDROID'";
                sql += ",@EXTRA_FIELD2 = 'S14_DTL_Activity'";

                sql += ",@DOCUMENT_TEXT = ''";
                sql += ",@USER_ID = '" + vUSER_ID + "'";
                sql += ",@MSG_CD = ''";
                sql += ",@MSG_TEXT = ''";

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
            TGSClass.AlertMessage(getApplicationContext(), "catch : ex");
        }
        return true;
    }
}