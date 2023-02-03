package com.example.gmax;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    public EditText lbl_max_address;
    public Spinner cmb_type;
    public Button btn_save;
    public TextView txt_name;

    public String sJson;

    public MySession global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //단말기 등록 요청 페이지.
        //등록 요청 후 프로그램 종료 됨.

        //== 글로벌 변수 ==//
        global          = (MySession)getApplication();

        //== ID값 바인딩 ==//
        lbl_max_address = findViewById(R.id.lbl_max_address);

        cmb_type        = findViewById(R.id.cmb_type);
        btn_save        = findViewById(R.id.btn_save);
        txt_name        = findViewById(R.id.txt_name);

        GatheringCombo();

        String sClientHostName = TGSClass.getMacAddress();
        lbl_max_address.setText(sClientHostName);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_name.getText().toString().equals("")) {
                    TGSClass.AlertMessage(RegisterActivity.this, "등록이름을 입력하세요.");
                    return;
                }

                DBSave_RequestRegister();
            }
        });

    }

    /* 뒤로가기 버튼 빠르게 2번 클릭 시 프로그램 종료
     *  - 시스템 함수 onBackPressed() 함수 오버라이드
     * */

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        //Log.d("tempTime", String.valueOf(tempTime));


        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            //super.finish();
            finishAffinity();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, "한번 더 누르면 프로그램이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void GatheringCombo() {
        final ArrayList<ItemData> lstItem = new ArrayList<>();
        ItemData itemPDA = new ItemData();
        itemPDA.setCODE("PDA");
        itemPDA.setNAME("PDA");
        itemPDA.setREMARK("PDA");
        lstItem.add(itemPDA);

        ItemData itemPhone = new ItemData();
        itemPhone.setCODE("PHONE");
        itemPhone.setNAME("PHONE");
        itemPhone.setREMARK("PHONE");
        lstItem.add(itemPhone);

        ArrayAdapter<ItemData> adapter = new ArrayAdapter<ItemData>(this, android.R.layout.simple_spinner_dropdown_item, lstItem);
        cmb_type.setAdapter(adapter);

        //로딩시 기본값 세팅
        cmb_type.setSelection(adapter.getPosition(itemPDA));
    }

    public void DBSave_RequestRegister() {
        Thread wTh = new Thread() {
            public void run() {

                String vType = ((ItemData) cmb_type.getSelectedItem()).getCODE();

                String vUnitCD = global.getUnitCDString();
                String vUserID = global.getLoginIDString();

                String sql = "DECLARE  @RTN_MSG NVARCHAR(200)=''";
                sql += "EXEC XUSP_AND_REGISTER_REQUEST_SET  ";
                sql += "  @UNIT_CD='" + lbl_max_address.getText().toString() + "'";
                sql += " ,@UNIT_NM='" + txt_name.getText().toString() + "'";
                sql += " ,@UNIT_TYPE='" + vType + "'";
                sql += " , @RTN_MSG =  @RTN_MSG  OUTPUT";
                sql += " SELECT  @RTN_MSG AS RTN_MSG ";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("SetSQLSave", pParms);
                String vMSG = "";
                String vStatus = "";

                try {
                    JSONArray ja = new JSONArray(sJson);

                    if (ja.length() > 0) {

                        JSONObject jObject = ja.getJSONObject(0);

                        vMSG = !jObject.isNull("RTN_MSG") ? jObject.getString("RTN_MSG") : "";
                        vStatus = !jObject.isNull("STATUS") ? jObject.getString("STATUS") : "";
                    }

                    Bundle bun = new Bundle();
                    if (!vStatus.equals("OK")) {

                        bun.putString("RTN_MSG", vMSG);
                        bun.putString("STATUS", vStatus);
                    } else {
                        bun.putString("RTN_MSG", "등록 요청 완료되었습니다.");
                        bun.putString("STATUS", vStatus);
                    }
                    Message msg = handler1.obtainMessage();
                    msg.setData(bun);
                    handler1.sendMessage(msg);


                } catch (JSONException ex) {

                }

            }

        };

        wTh.start();   //스레드 시작
        try {
            wTh.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    Handler handler1 = new Handler() {
        //스레드에서 값을 받아와서 화면에 뿌림.
        public void handleMessage(Message msg) {

            Bundle bun = msg.getData();

            String vMsg = bun.getString("RTN_MSG");

            TGSClass.AlertMessage(RegisterActivity.this, vMsg);

            finishAffinity();

        }

    };
}