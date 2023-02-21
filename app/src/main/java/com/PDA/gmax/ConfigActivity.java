package com.PDA.gmax;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.PDA.gmax.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class ConfigActivity extends AppCompatActivity {

    //== Intent에서 받을 값 선언 ==//
    private String vMenuID, vMenuNm, vMenuRemark, vStartCommand;

    //== View 선언(TextView) ==//
    private TextView now_ver, new_ver, db_nm;

    //== View 선언(Button) ==//
    private Button btn_apk_download;

    //== Activity 관련 변수 선언 ==//
    private final int POPUP_REQUEST_CODE = 1, PROGRESSBAR_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        this.initializeView();

        this.initializeListener();

        this.initializeData();
    }

    private void initializeView() {
        //== Intent 값 바인딩 ==//
        vMenuID                 = getIntent().getStringExtra("MENU_ID");
        vMenuNm                 = getIntent().getStringExtra("MENU_NM");
        vMenuRemark             = getIntent().getStringExtra("MENU_REMARK");
        vStartCommand           = getIntent().getStringExtra("START_COMMAND");

        //== ID 값 바인딩 ==//
        now_ver                 = (TextView) findViewById(R.id.now_ver);
        new_ver                 = (TextView) findViewById(R.id.new_ver);
        db_nm                   = (TextView) findViewById(R.id.db_nm);

        btn_apk_download        = (Button) findViewById(R.id.btn_apk_download);
    }

    private void initializeListener() {
        View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_apk_download:
                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(ConfigActivity.this);
                        mAlert.setMessage("업데이트를 진행하시겠습니까?")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ((MainActivity)MainActivity.mContext).update_user_application(MainActivity.mContext);
                                    }
                                })
                                .create().show();
                        break;
                }
            }
        };
        btn_apk_download.setOnClickListener(btnListener);
    }

    private void initializeData() {

        //== MainActivity에서 사용하는 appChk()메서드 불러와서 사용하기 ==//
        ((MainActivity)MainActivity.mContext).chkApp();

        /* MainActivity에 있는 메서드를 사용하여 기존 소스 주석처리 - 김종필
        // 현재 설치된 APP 의 버전 정보를 가져옵니다.
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pInfo.versionName;
        int  versionNo = pInfo.versionCode;

        // 현재 설치된 APP 의 버전 정보와 다운로드 받을 경로 정보를 데이터 베이스에서 받아옵니다.
        if(!GetVersionFromServer())
        {
            Intent error_intent = TGSClass.ChangeView(getPackageName(),ErrorPopupActivity.class);
            error_intent.putExtra("MSG", sErrorMessage);
            startActivity(error_intent);
            return;
        }
         */

        //String versionNo_st = String.valueOf(versionNo);
        //String sVersion_st  = String.valueOf(sVersion);
        //== MainActivity에서 가져온 필드 입력하기 ==//
        String versionNo_st = String.valueOf(((MainActivity)MainActivity.mContext).intVersionNo);
        String sVersion_st  = String.valueOf(((MainActivity)MainActivity.mContext).intVersion);

        now_ver.setText(versionNo_st);
        new_ver.setText(sVersion_st);

        SELECT_DB_NAME();
    }

    //== WebServiceEM을 통하여 DB 이름 가져오기 ==//
    private void SELECT_DB_NAME() {
        Thread wkThd_SELECT_DB_NAME = new Thread() {
            public void run() {
                String rtn_db_nm = "";
                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();
                rtn_db_nm = dba.SendHttpMessage("SELECT_DB_NAME", pParms);

                db_nm.setText(rtn_db_nm);
            }
        };
        wkThd_SELECT_DB_NAME.start();   //스레드 시작
        try {
            wkThd_SELECT_DB_NAME.join(); //workingThread가 종료될때까지 Main 쓰레드를 정지함
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /* MainActivity에 있는 메서드를 사용하여 기존 소스 주석처리 - 김종필
    public Boolean GetVersionFromServer() {
        Boolean vReturn = false;
        Thread workingThread = new Thread() {
            public void run() {

                String sql = " exec XUSP_AND_APP_VERSION_GET @FILENAME='" + FILE_NAME + "'";
                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonVersion = dba.SendHttpMessage("GetSQLData", pParms);
            }

        };
        workingThread.start();   //스레드 시작


        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.
            try {

                Boolean vJsonType = TGSClass.isJsonData((sJsonVersion));

                if(!vJsonType) {
                    sErrorMessage = sJsonVersion;
                    return  false;
                }

                JSONArray ja = new JSONArray(sJsonVersion);


                if(ja.length() > 0) {
                    JSONObject jObject = ja.getJSONObject(0);

                    String Version = String.valueOf(jObject.getString("VERSION_CD"));
                    sVersion = Integer.parseInt(Version);
                    DOWNLOAD_URL =  String.valueOf(jObject.getString("UPDATE_URL"));
                }
                vReturn = true;
            }
            catch (JSONException ex) {
                TGSClass.AlterMessage(getApplicationContext(),ex.getMessage());
            }
            catch (Exception ex) {
                TGSClass.AlterMessage(getApplicationContext(),ex.getMessage());
            }

        }
        catch (InterruptedException ex) {
            TGSClass.AlterMessage(getApplicationContext(),ex.getMessage());
        }

        return vReturn;
    }
     */
}
