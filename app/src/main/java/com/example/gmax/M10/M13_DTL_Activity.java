package com.example.gmax.M10;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.gmax.BaseActivity;
import com.example.gmax.DBAccess;
import com.example.gmax.ErrorList_Popup;
import com.example.gmax.R;
import com.example.gmax.TGSClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class M13_DTL_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "",lJson = "", sJsonConfigSet = "", sJsonCombo = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private String tx_dn_no,tx_lot_no;

    //== M13_LOT와 주고 받을 변수 선언 ==//
    private M13_DTL vItem;
    private int vIDX = -1;

    //== View 선언(EditText) ==//
    private EditText lot_no,dn_no;

    private TextView end_cust_nm;

    //== View 선언(ListView) ==//
    private ListView listview;

    //== View 선언(Button) ==//
    private Button btn_save,btn_custom;

    //== View 선언(CheckBox) ==//
    private CheckBox chk_custom;

    //== View 선언(ImageView) ==//
    private ImageView img_barcode,img_lot;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int M13_DTL_REQUEST_CODE = 0;

    private ErrorList_Popup Error_Popup;

    //== ListView Adapter 선언 ==//
    M13_DTL_ListViewAdapter ListViewAdapter; //데이터를 완전히 초기화 하는것이 아니라 수정처리 하기때문에 전역 선언



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m13_dtl);

        this.initializeView();

        this.initializeListener();

        //this.initializeData();
    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent에서 값 가져오기 ==//
        vMenuID         = getIntent().getStringExtra("MENU_ID");
        vMenuNm         = getIntent().getStringExtra("MENU_NM");
        vMenuRemark     = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand   = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        //== DTL 값 바인딩 ==//
        tx_dn_no      = "";
        tx_lot_no     ="";

        //== ID값 바인딩 ==//
        dn_no       = (EditText) findViewById(R.id.dn_no);
        lot_no      = (EditText) findViewById(R.id.lot_no);
        end_cust_nm   = (TextView) findViewById(R.id.end_cust_nm);
        listview    = (ListView) findViewById(R.id.listPacking);

        btn_save    = (Button) findViewById(R.id.btn_save);
        btn_custom  = (Button) findViewById(R.id.btn_custom);
        chk_custom  = (CheckBox) findViewById(R.id.custom);

        img_barcode     = (ImageView) findViewById(R.id.img_barcode);
        img_lot     = (ImageView) findViewById(R.id.img_lot);

        //== Adapter 선언 ==//
        ListViewAdapter = new M13_DTL_ListViewAdapter();
        listview.setAdapter(ListViewAdapter);
        listview.setFocusable(false);

        //팝업창 띄우면 뒤쪽 메인액티비티 어둡게 설정
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
    }

    private void initializeListener() {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                vItem = (M13_DTL) parent.getItemAtPosition(position);
                vIDX = position;

                ListViewAdapter.notifyDataSetChanged();
                //parent.getItemAtPosition(position)
                //parent.setBackgroundColor(Color.parseColor("#00D8FF"));
            }
        });


        lot_no.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                lot_no.setText("");
            }
        });
        //== 바코드 이벤트 ==//
        img_barcode.setOnClickListener(qrClickListener);

        //== 바코드 이벤트 ==//
        img_lot.setOnClickListener(qrClickListener);

        btn_save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                //저장할 항목의 수 확인
                int saveCnt = ListViewAdapter.getCount();

                //입고할 항목이 있는지 스캔
                if(saveCnt<=0){
                    TGSClass.AlertMessage(getApplicationContext(), "입하처리할 내역이 존재하지 않습니다.");
                    return;
                }

                //모든 입고 항목이 스캔되었는지 확인처리
                for(int i = 0; i< saveCnt; i++){
                    M13_DTL checkdtl = (M13_DTL) ListViewAdapter.getItem(i);
                    if(!checkdtl.getCHK()){
                        TGSClass.AlertMessage(getApplicationContext(),"모든 항목의 입력을 완료해주세요.");
                        return;
                    }
                }
                dbSave(saveCnt);

                //finish();
            }
        });
        btn_custom.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                Intent intent = TGSClass.ChangeView(getPackageName(), M13_LOT_Activity.class);

                intent.putExtra("DN_NO", dn_no.getText().toString());

                intent.putExtra("LOT", ListViewAdapter.getLotArray());

                startActivityForResult(intent, 0);

            }
        });
        dn_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {

                    //바코드 입력시 텍스트 리프레시를 위해서 설정

                    String temp=dn_no.getText().toString().replaceFirst(tx_dn_no,"");
                    //temp="MD221125-001";
                    dn_no.setText(temp);
                    tx_dn_no=dn_no.getText().toString();

                    start();

                    lot_no.requestFocus();

                    return true;
                }
                return false;
            }
        });

        lot_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {

                    //바코드 입력시 텍스트 리프레시를 위해서 설정

                    String temp = lot_no.getText().toString().replaceFirst(tx_lot_no, "");
                    //temp = "M1000221103001001001";
                    lot_no.setText(temp);
                    tx_lot_no = lot_no.getText().toString();

                    //일반등록
                    Auto_Input();

                    lot_no.setText(tx_lot_no);
                    lot_no.setSelection(lot_no.getText().length());

                    return true;
                }
                return false;
            }
        });

    }

    private void initializeData() {

        start();

    }

    //액티비티 시작,데이터 조회
    private void start() {
        dataSaveLog("거래명세서 스캔","CKD_IN");
        dataSaveLog(dn_no.getText().toString(),"CKD_IN");

        dbQuery(dn_no.getText().toString());

        System.out.println("sjson:"+sJson);
        if (!sJson.equals("")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                //final ArrayList<String> items = new ArrayList<String>();

                //M13_DTL_ListViewAdapter ListViewAdapter = new M13_DTL_ListViewAdapter();
                ListViewAdapter.ClearItem();

                for (int idx = 0; idx < ja.length(); idx++) {

                    JSONObject jObject = ja.getJSONObject(idx);

                    M13_DTL item = new M13_DTL();
                    item.setSER_NO             (jObject.getString("SER_NO"));           //순번
                    item.setITEM_CD            (jObject.getString("ITEM_CD"));          //품번
                    item.setITEM_NM            (jObject.getString("ITEM_NM"));          //품명
                    item.setSPEC               (jObject.getString("SPEC"));             //규격
                    item.setDLV_QTY            (jObject.getString("DLV_QTY"));          //수량
                    item.setCONFIRM_DLV_QTY    (jObject.getString("CONFIRM_DLV_QTY"));   //확인수량
                    item.setINSPECT_FLG        (jObject.getString("INSPECT_FLG"));      //검사여부
                    item.setSL_NM              (jObject.getString("SL_NM"));            //입고창고
                    item.setLOT_NO             (jObject.getString("LOT_NO"));           //LOT NO
                    item.setSUB_SEQ_NO         (jObject.getString("SUB_SEQ_NO"));       //하위 순번
                    item.setPUR_TYPE           (jObject.getString("PUR_TYPE"));         //발주유형
                    item.setPUR_TYPE_CD        (jObject.getString("PUR_TYPE_CD"));      //
                    item.setPLANT_CD           (jObject.getString("PLANT_CD"));         //공장코드
                    item.setDLV_NO             (jObject.getString("DLV_NO"));           //거래명세서
                    item.setSL_CD              (jObject.getString("SL_CD"));            //창고코드
                    item.setPO_NO              (jObject.getString("PO_NO"));            //발주번호
                    item.setPO_SEQ_NO          (jObject.getString("PO_SEQ_NO"));        //발주순번
                    item.setPROCUR_TYPE        (jObject.getString("PROCUR_TYPE"));
                    item.setBP_CD              (jObject.getString("BP_CD"));
                    //item.MVMT_QTY          = jObject.getString("MVMT_QTY");         //입하수량
                    item.setPRODT_ORDER_NO     (jObject.getString("PRODT_ORDER_NO"));
                    item.setOPR_NO             (jObject.getString("OPR_NO"));
                    item.setTRACKING_NO        (jObject.getString("TRACKING_NO"));
                    item.setCHK                (jObject.getString("CHK_FLAG").equals("Y") ? true:false);//로트스캔 여부
                    item.setIDX                (idx);
                    item.setEND_CUST_NM        (jObject.getString("END_CUST_NM")); //최종고객
                    ListViewAdapter.addPkgItem(item);
                }

                //최종고객 표시
                if(ListViewAdapter.getCount()>0){
                    M13_DTL item = (M13_DTL) ListViewAdapter.getItem(0);
                    end_cust_nm.setText(item.getEND_CUST_NM());
                }


                listview.setAdapter(ListViewAdapter);
                ListViewAdapter.notifyDataSetChanged();

                TGSClass.AlertMessage(getApplicationContext(), ja.length() + " 건 조회되었습니다.");

            } catch (JSONException ex) {
                TGSClass.AlertMessage(this, ex.getMessage());
                //System.out.println("json:"+ex.getStackTrace());
                //System.out.println("json:"+ex.getMessage());

            } catch (Exception e1) {
               TGSClass.AlertMessage(this, e1.getMessage());
                //System.out.println("e1:"+e1.getStackTrace());

            }
        }
    }


    //리스트 데이터 조회
    private void dbQuery(final String pDN_NO) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {

                String sql = "EXEC DBO.XUSP_TPC_M1003MA1_GET_LIST_ANDROID ";
                sql += " @PLANT_CD ='" + vPLANT_CD+ "',";//현재 공장 코드 번호
                sql += " @DLV_NO ='" + pDN_NO + "',";
                sql += " @USER_ID    = '" + vUSER_ID + "'";

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


    private void dbSave(int saveCnt) {

        String dlv_no_data = dn_no.getText().toString();
        dbQuery_update_ISSUE_MTHD(dlv_no_data); //공장별 품목수불현황에서 자품목 자동출고로 강제 조정

        try {

            String CUD_FLAG = "C";

            //입고일자 세팅
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String MVMT_DT =  sdf.format(date);


            String Error = "";

            ArrayList<M13_DTL> SaveData = SetSaveData();

            for (M13_DTL savedtl : SaveData) {
                //BL 실행
                dbQuery_GET_BL(savedtl,CUD_FLAG, MVMT_DT);

                //에러표시 되는 항목만 메시지에 추가
                if(result_msg.contains("FAIL")){
                    result_msg = result_msg.replace("FAIL:","");
                    result_msg += " ("+savedtl.getITEM_NM()+")";
                    result_msg += "\n\r";
                    Error+=result_msg;
                }
                else{
                    //dbResultSave(savedtl);
                }
            }

            //에러메시지 표시
            if(!Error.equals("")){
                Error_Popup = new ErrorList_Popup(this,"구매입고 등록 오류",Error);
                Error_Popup.show();

                start();
            }
            else{
                TGSClass.AlertMessage(getApplicationContext(),"입하저장 완료되었습니다.",5000);

                finish();
            }


        } catch (Exception e1) {
            System.out.println(e1.getMessage());
            TGSClass.AlertMessage(getApplicationContext(), "catch :"+e1);
        }


    }


    private  ArrayList<M13_DTL> SetSaveData(){

        M13_DTL temp_dtl = new M13_DTL();
        temp_dtl.setITEM_CD("");
        ArrayList<M13_DTL> result = new ArrayList<>();
        try {

            for (int i = 0; i < ListViewAdapter.getCount(); i++) {
                M13_DTL savedtl = (M13_DTL) ListViewAdapter.getItem(i);

                if(i==0){
                    temp_dtl = savedtl;
                    continue;
                }

                //이전항목과 현재항목이 같으면 qty더하기
                if (temp_dtl.getITEM_CD().equals(savedtl.getITEM_CD())) {

                    double temp = Double.parseDouble(
                            temp_dtl.getDLV_QTY()) + Double.parseDouble(savedtl.getDLV_QTY()
                    );

                    temp_dtl.setDLV_QTY(String.valueOf(temp));
                } else {
                    //이전항목과 현재항목이 다르면 배열에 저장
                    result.add(temp_dtl);
                    temp_dtl = savedtl;
                }

                //마지막 항목일 경우 배열에 저장
                if (i + 1 == ListViewAdapter.getCount()) {
                    result.add(temp_dtl);
                }
            }

        }catch (Exception e){
            TGSClass.AlertMessage(getApplicationContext(), "catch :"+e.getMessage(),3000);
        }
        return result;
    }
    //LOT번호 스캔하여 데이터 저장
    private void dbLotSave(M13_DTL dtl) {
                ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
                Thread wkThd_dbQuery = new Thread() {
                    public void run() {

                        dataSaveLog("로트 스캔 저장","CKD_IN");
                        dataSaveLog("로트번호// "+dtl.getLOT_NO(),"CKD_IN");
                        dataSaveLog("거래명세서 번호// "+dtl.getDLV_NO(),"CKD_IN");

                        String sql = " EXEC DBO.XUSP_TPC_M1003MA1_SET_LOT_ANDROID ";
                        sql += " @DN_NO	     = '"+dtl.getDLV_NO()+"',";
                        sql += " @SER_NO     = '" + dtl.getSER_NO() + "',";
                        sql += " @SUB_SEQ_NO = '" + dtl.getSUB_SEQ_NO() + "',";
                        sql += " @LOT_NO     = '" + dtl.getLOT_NO() + "',";
                        sql += " @CHK_FLAG   = '" + (dtl.getCHK() ? "Y" : "F") + "',";
                        sql += " @LOT_QTY    = '" + dtl.getDLV_QTY() + "',";
                        sql += " @USER_ID    = '" + vUSER_ID + "'";
                        sql += ";";
                        dataSaveLog("저장 SQL// "+sql,"CKD_IN");

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

    private void dbQuery_update_ISSUE_MTHD(final String dlv_no) {
        Thread wkThd_dbQuery_udate_ISSUE_MTHD = new Thread() {
            public void run() {

                //공장별 품목정보의 출고방법 수정(M->A) --하위자품목 자동출고로 강제 조정
                String sql = " EXEC XUSP_Udate_ISSUE_MTHD_LIST ";
                sql += " @PLANT_CD = '" + vPLANT_CD + "' ";
                sql += " ,@DLV_NO = '" + dlv_no + "' ";
                System.out.println("sql:"+sql);

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);
                System.out.println("sJson:"+sJson);

            }


        };
        wkThd_dbQuery_udate_ISSUE_MTHD.start();   //스레드 시작
        try {
            wkThd_dbQuery_udate_ISSUE_MTHD.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    //입고 BL 실행
    private boolean dbQuery_GET_BL(M13_DTL savedtl,String cud_flag_parm,String mvmt_dt_parm) {
        Thread wkThd_dbQuery_GET_BL = new Thread() {
            public void run() {

                String po_no_parm               = savedtl.getPO_NO();
                String po_seq_no_parm           = savedtl.getPO_SEQ_NO();      // => INT로 받아야함
                String lot_no_parm              = savedtl.getLOT_NO();
                String pur_type_cd_parm         = savedtl.getPUR_TYPE_CD();
                String prodt_order_no_parm      = savedtl.getPRODT_ORDER_NO();
                String opr_no_parm              = savedtl.getOPR_NO();
                String dlv_no_parm              = savedtl.getDLV_NO();
                String ser_no_parm              = savedtl.getSER_NO();
                String mvmt_qty_parm            = savedtl.getDLV_QTY();

                String unit_cd_parm             = vUNIT_CD;
                String plant_cd_parm            = vPLANT_CD;
                String user_id_parm             = vUSER_ID;
                plant_cd_parm ="1000";

                System.out.println("po_no_parm:"+po_no_parm);
                //System.out.println("po_seq_no_parm:"+po_seq_no_parm);
                System.out.println("lot_no_parm:"+lot_no_parm);
                //System.out.println("pur_type_cd_parm:"+pur_type_cd_parm);
                //System.out.println("prodt_order_no_parm:"+prodt_order_no_parm);
                //System.out.println("opr_no_parm:"+opr_no_parm);
                //System.out.println("dlv_no_parm:"+dlv_no_parm);
                //System.out.println("ser_no_parm:"+ser_no_parm);
                System.out.println("mvmt_qty_parm:"+mvmt_qty_parm);

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("cud_flag");
                parm.setValue(cud_flag_parm);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("mvmt_dt");
                parm2.setValue(mvmt_dt_parm);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("po_no");
                parm3.setValue(po_no_parm);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("po_seq_no");
                parm4.setValue(po_seq_no_parm);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("ser_no");
                parm5.setValue(ser_no_parm);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("mvmt_qty");
                parm6.setValue(mvmt_qty_parm);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("lot_no");
                parm7.setValue(lot_no_parm);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("pur_type_cd");
                parm8.setValue(pur_type_cd_parm);
                parm8.setType(Integer.class);

                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("prodt_order_no");
                parm9.setValue(prodt_order_no_parm);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("opr_no");
                parm10.setValue(opr_no_parm);
                parm10.setType(String.class);

                PropertyInfo parm11 = new PropertyInfo();
                parm11.setName("dlv_no");
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

    //LOT번호 스캔하여 데이터 저장
    private void dbResultSave(M13_DTL dtl) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_dbQuery = new Thread() {
            public void run() {

                dataSaveLog("로트 스캔 저장","CKD_IN");
                dataSaveLog("로트번호// "+dtl.getLOT_NO(),"CKD_IN");
                dataSaveLog("거래명세서 번호// "+dtl.getDLV_NO(),"CKD_IN");

                String sql = " EXEC DBO.XUSP_TPC_M1003MA1_SET_RESULT_ANDROID ";
                sql += " @DN_NO	     = '"+dtl.getDLV_NO()+"',";
                sql += " @SER_NO     = '" + dtl.getSER_NO() + "',";
                sql += " @SUB_SEQ_NO = '" + dtl.getSUB_SEQ_NO() + "',";
                sql += " @USER_ID    = '" + vUSER_ID + "'";
                sql += ";";

                dataSaveLog("결과 저장 SQL// "+sql,"CKD_IN");

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


    //수기등록 페이지로 이동하여 처리
    private void Custom_Input(){

        Intent intent = TGSClass.ChangeView(getPackageName(), M13_LOT_Activity.class);

        vItem = (M13_DTL) ListViewAdapter.getItemFromLotno(tx_lot_no);

        if(vItem == null){
            TGSClass.AlertMessage(getApplicationContext(), "일치하는 항목이 없습니다.");

            return;
        }
        intent.putExtra("DN_NO", dn_no.getText().toString());
        intent.putExtra("ITEM_CD", vItem.getITEM_CD() );
        intent.putExtra("ITEM_NM", vItem.getITEM_NM());
        intent.putExtra("LOT_NO", vItem.getLOT_NO());

        intent.putExtra("LOT", ListViewAdapter.getLotArray());

        startActivityForResult(intent, 0);
    }

    //일반등록으로 스캔한 데이터만 입력처리
    private void Auto_Input(){

        for(M13_DTL dtl : ListViewAdapter.getLotArray()){

            if(dtl.getLOT_NO().equals(tx_lot_no)){

                dtl.setCHK(true);
                ListViewAdapter.updatePkgItem(dtl);

                listview.setAdapter(ListViewAdapter);
                ListViewAdapter.notifyDataSetChanged();

                dbLotSave(dtl);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case M13_DTL_REQUEST_CODE:
                    ArrayList<M13_DTL> lot = (ArrayList<M13_DTL>)data.getSerializableExtra("LOT");

                    try{
                        for(M13_DTL dtl : lot){

                            dbLotSave(dtl);
                        }
                        ListViewAdapter.updateLotItem(lot);
                        ListViewAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e){

                    }

                default:
                    break;
            }
        }
    }


}