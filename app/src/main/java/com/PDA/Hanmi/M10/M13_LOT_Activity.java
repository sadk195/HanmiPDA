package com.PDA.Hanmi.M10;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.PDA.Hanmi.BaseActivity;
import com.PDA.Hanmi.R;
import com.PDA.Hanmi.TGSClass;

import java.util.ArrayList;


public class M13_LOT_Activity extends BaseActivity {

    //== JSON 선언 ==//
    private String sJson = "", sJsonConfigSet = "", sJsonCombo = "";

    //== Intent에서 받을 변수 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;
    private String tx_lot_no="",Item_CD,Item_NM;

    //== View 선언(EditText) ==//
    private EditText lot_qty,lot_no,item_nm;

    //== View 선언(ListView) ==//
    private ListView listview;

    private TextView lbl_count_scan;
    private TextView lbl_count;

    //== ActivityForResult 관련 변수 선언 ==//
    private final int M13_DTL_REQUEST_CODE = 0;

    //== M13_DTL 관련 변수 ==//
    private ArrayList<M13_DTL> Lot_info;
    private int selected_idx,scan_qty,lot_idx;
    private M13_LOT_ListViewAdapter ListViewAdapter;
    private M13_LOT m13_lot;

    //== View 선언(Button) ==//
    private Button btn_lot,btn_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m13_lot);

        this.initializeView();

        this.initializeListener();

        this.initializeData();


    }

    private void initializeView() {
        //== BaseActivity 에서 SESSION 값 셋팅 ==//
        this.init();

        //== Intent에서 값 가져오기 ==//
        vMenuID         = getIntent().getStringExtra("MENU_ID");
        vMenuNm         = getIntent().getStringExtra("MENU_NM");
        vMenuRemark     = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand   = getIntent().getStringExtra("START_COMMAND"); //다음 페이지에 가지고 넘어갈 코드

        Lot_info        = (ArrayList<M13_DTL>)getIntent().getSerializableExtra("LOT");

        //== ID값 바인딩 ==//

        listview        = (ListView) findViewById(R.id.listLot);
        lot_no          = (EditText) findViewById(R.id.lot_no);
        lot_qty         = (EditText) findViewById(R.id.lot_qty);
        item_nm         = (EditText) findViewById(R.id.item_nm);

        btn_lot         = (Button) findViewById(R.id.btn_lot);
        btn_end         = (Button) findViewById(R.id.btn_end);

        lbl_count       = (TextView) findViewById(R.id.lbl_count);
        lbl_count_scan  = (TextView) findViewById(R.id.lbl_count_scan);

        //== EDIT_TEXT 값 지정 ==//
        item_nm.setText(Item_NM);
        lot_no.setText(tx_lot_no);

    }


    private void initializeListener() {
        lot_no.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {

                    //바코드 입력시 텍스트 리프레시를 위해서 설정


                    String temp = lot_no.getText().toString().replaceFirst(tx_lot_no, "");
                    //temp = "M1000221103001001001";
                    lot_no.setText(temp);
                    tx_lot_no = lot_no.getText().toString();

                    for(M13_DTL dtl : Lot_info){
                        if(dtl.getLOT_NO().equals(tx_lot_no)){
                            dataSaveLog("수기등록 로드스캔","CKD_IN");

                            Item_CD = dtl.getITEM_CD();
                            item_nm.setText(dtl.getITEM_NM());
                            dataSaveLog("품명// "+dtl.getITEM_CD(),"CKD_IN");
                            dataSaveLog("거래명세서 번호// "+dtl.getITEM_NM(),"CKD_IN");
                        }
                    }

                    start();
                    return true;
                }
                return false;
            }
        });

        btn_end.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                if(ListViewAdapter == null){

                    Intent resultIntent = new Intent();
                    setResult(RESULT_CANCELED, resultIntent);

                    finish();
                    return;//리턴처리 하지 않으면 UI쓰레드와의 속도차이로 뒤로 프로세스 흐름
                }

                setLotChk();

                for(M13_DTL dtl : ListViewAdapter.getLotArray()){
                    Lot_info.set(dtl.getIDX(),dtl);
                }

                // 저장 후 결과 값 돌려주기
                Intent resultIntent = new Intent();
                // 결과처리 후 부른 Activity에 보낼 값
                resultIntent.putExtra("LOT", Lot_info);
                //resultIntent.putExtra("LOT_IDX", 2);

                // 부른 Activity에게 결과 값 반환
                setResult(RESULT_OK, resultIntent);
                // 현재 Activity 종료
                finish();
            }
        });
    }

    private void initializeData() {


    }

    private void start() {
        //TGSClass.AlertMessage(getApplicationContext(), BP_CD.getOnItemSelectedListener().toString());

        try {
            int cnt = 0;
            ListViewAdapter = new M13_LOT_ListViewAdapter();
            ListViewAdapter.setTextView(lbl_count_scan);


            for (M13_DTL LOT : Lot_info) {
                if(LOT.getITEM_CD().equals(Item_CD)){
                    ListViewAdapter.addShipmentHDRItem(LOT);
                }

            }


            listview.setAdapter(ListViewAdapter);
            ListViewAdapter.notifyDataSetChanged();

            //전체 카운트는 1회만 설정, 스캔카운트는 어댑터로 넘겨서 실시간 처리
            lbl_count_scan.setText(String.valueOf(ListViewAdapter.getChkCount()));
            lbl_count.setText(String.valueOf(ListViewAdapter.getCount()));

        } catch (Exception e1) {
            TGSClass.AlertMessage(this, e1.getMessage());

        }
    }

    public  void setLotChk(){

        //BOX수가 입력되어 있는지 체크
        if(lot_qty.getText().equals("")){
            TGSClass.AlertMessage(this, "BOX수를 입력해주세요");
            return;
        }

        int qty = Integer.parseInt(String.valueOf(lot_qty.getText()));

        //남은수보다 BOX수가 많을경우 리턴
        if(ListViewAdapter.getUnChkCount() < qty){
            TGSClass.AlertMessage(this, "남은 수량과 BOX수량이 다릅니다");
            return;
        }

        //수량 입력시 체크되지 않은 항목을 입력한 수량만큼 체크 표시
        ArrayList<M13_DTL> arrayList = ListViewAdapter.getLotArray();

        int cnt = 0;
        dataSaveLog("수기등록 로드체크수// "+qty,"CKD_IN");

        for(int i = 0; i < arrayList.size() && cnt < qty; i++){
            M13_DTL dtl = arrayList.get(i);

            if(!dtl.getCHK()){
                dtl.setCHK(true);
                ListViewAdapter.updatePkgItem(i,dtl);
                cnt++;
            }
            else{

            }

        }
        dataSaveLog("수기등록 로드체크 완료수// "+cnt,"CKD_IN");

        ListViewAdapter.notifyDataSetChanged();
    }


}