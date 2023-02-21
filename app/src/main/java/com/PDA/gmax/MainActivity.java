package com.PDA.gmax;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.PDA.gmax.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //== JSON 선언 ==//
    private String strJson, strJson_auto_login, strJsonVersion;

    //== SESSION 선언 ==//
    protected MySession global;

    //== view 선언(ImageView) ==//
    private ImageView img_user, img_logo;

    //== view 선언(EditText) ==//
//    private EditText txtID, txtPWD;
    private TextInputLayout txtID, txtPWD;

    //== view 선언(TextView) ==//
    private TextView lbl_device;

    //== view 선언(Button) ==//
//    private Button btnLogin, btnEnd;
    private MaterialButton btnLogin;
//    private MaterialButton btnEnd;

    //== Check 변수 선언 ==//
    private boolean permissionChk, updateChk, chkLogIn = false;

    //== Tag ==//
    private static final String TAG = "MyTag";

    //== 상수 선언 ==//
    private final long FINISH_INTERVAL_TIME = 2000;
    private final int REQUEST_CODE_WRITE_STORAGE_PERMISSIONS = 1001;
    private final int DOWNLOAD_ACTIVITY_REQUEST_CODE = 1;
    private long backPressedTime = 0;
    public int intVersionNo, intVersion = 1;

    //== String 선언 ==//
    private final String FILE_NAME = "GMAX.apk";

    //== 변수 선언 ==//
    private String strClientIP, strClientHostNm, strErrorMsg;
    private String strPlantCD, strUSER_ID, strPWD;
    private String DOWNLOAD_URL;
    public String strVersionNm;

    //== 선언 ==//
    private int cntUserTouch = 0, CntLogoCnt = 0;
    private boolean blnUser = false, blnLogo = false;

    // 카메라와 외부 저장소 권한 정의
    public String[] REQUIRED_PERMISSIONS = { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    //== Exit ==//
    BottomSheetExitDialog bottomSheetDialog;

    //-- 다른 액티비티에서 현재 액티비티의 메소드를 호출할 수 있도록 하기 위해 정의 --//
    public static Context mContext;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        /** BottomSheetDialog를 사용하여 INTERVAL_TIME을 기다리지 않아도 동작이 되도록 수정.
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        //2초 안에 2번 클릭 시 프로그램 종료
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            //super.finish();
            //-- 앱 종료 --//
            finishAffinity();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, "한번 더 누르면 프로그램이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        */

        bottomSheetDialog.show(getSupportFragmentManager(), "bottomSheet");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomSheetDialog = new BottomSheetExitDialog("EXIT");

        this.initializeView();

        this.initializeListener();

        this.initializeData();

        //-- 권한 체크 --//
        chkPermission();

        //-- ERP프로그램 "MES 자동로그인정보관리"에 단말기 코드가 호스트명으로 존재하면 자동로그인 --//
        if (permissionChk && updateChk && (strErrorMsg == null)) {
            dbQuery_auto_login(strClientHostNm);
        }
    }

    ////////////////////////////// 기본 메소드 정의 ////////////////////////////////////////////////////////

    private void initializeView() {
        //== SESSION 정의 ==//
        global = (MySession) getApplication();

        //== ID 값 바인딩 ==//
        img_user = (ImageView) findViewById(R.id.img_user);
        img_logo = (ImageView) findViewById(R.id.img_logo);

//        txtID = (EditText) findViewById(R.id.txt_id);
//        txtPWD = (EditText) findViewById(R.id.txt_pwd);

        txtID = (TextInputLayout) findViewById(R.id.txt_id);
        txtPWD = (TextInputLayout) findViewById(R.id.txt_pwd);

//        btnLogin = (Button) findViewById(R.id.btn_Login);
//        btnEnd = (Button) findViewById(R.id.btn_End);
        btnLogin = (MaterialButton) findViewById(R.id.btn_Login);
//        btnEnd = (MaterialButton) findViewById(R.id.btn_End);

        lbl_device = (TextView) findViewById(R.id.lbl_device_name);

        //-- 다른 액티비티에서 현재 액티비티의 메소드를 사용할 수 있도록 하기 위한 정의 --//
        mContext    = this;

    }

    private void initializeListener() {

        //-- 클릭 이벤트 --//
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_End:
                        //finishAffinity();
                        break;
                    case R.id.lbl_device_name:
                        //-- 단말기 고유코드를 클릭 시 단말기 등록 요청 프로세스 작동 --//

                        break;
                    case R.id.btn_Login:
                        //Log.d("click", "1"); //디버그화면에서 d로 확인하기

                        if (strPlantCD.equals("") || strPlantCD == null) {
//                            TGSClass.AlertMessage(getApplicationContext(), "등록되지 않은 기기입니다. 기기등록을 요청 하시기 바랍니다.", 1000);
                            TGSClass.showToast(MainActivity.this, "WARNING", "", "등록되지 않은 기기입니다. 기기등록을 요청 하시기 바랍니다.");
                            return;
                        }
//                        if (logIn_chk(txtID.getText().toString(), txtPWD.getText().toString()) == true) {
                        if (logIn_chk(txtID.getEditText().getText().toString(), txtPWD.getEditText().getText().toString()) == true) {
                            //-- 등록된 장비일 시 메뉴화면으로 이동 --//
                            login(strUSER_ID, strPWD);
                        }
                    default:
                        break;
                }
            }
        };
//        btnEnd.setOnClickListener(clickListener);
        lbl_device.setOnClickListener(clickListener);
        btnLogin.setOnClickListener(clickListener);
    }

    private void initializeData() {
        strClientIP = TGSClass.getLocalIPAddress();
        strClientHostNm = TGSClass.getMacAddress();

        //테스트용 MAC
        //strClientHostNm ="02000044556";
        //System.out.println("strClientHostNm:"+strClientHostNm);
        lbl_device.setText(strClientHostNm);

        //== SESSION에 값 저장 ==//
        global.setUserIPString(strClientIP);
        global.setUnitCDString(strClientHostNm);

        //== 체크 ==//
        permissionChk = false;
        updateChk = false;
    }

    public void start() {
        strPlantCD = dbQuery(strClientHostNm); //등록된 단말기라면 공장 코드값을 불러옴.

        if (strPlantCD.equals("")) {
            //등록되지 않은 단말기라면 단말기 등록 요청 페이지로 이동
            Intent register_intent = TGSClass.ChangeView(getPackageName(), RegisterActivity.class);
            startActivity(register_intent);
            finish();
        }
    }

    ////////////////////////////// 로그인 관련 메소드 정의 ////////////////////////////////////////////////////////

    protected String dbQuery(final String pDevice) {
        String vPLANT_CD = "";
        String vUNIT_CD = "";
        String vUNIT_NM = "";
        String vUNIT_TYPE = "";
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_dbQuery = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_AND_APK_DEVICE_CHECK @UNIT_CD = '" + pDevice + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParams = new ArrayList<>();

                PropertyInfo param = new PropertyInfo();
                param.setName("pSQL_Command");
                param.setValue(sql);
                param.setType(String.class);

                pParams.add(param);

                strJson = dba.SendHttpMessage("GetSQLData", pParams);
            }
        };

        workThd_dbQuery.start();

        try {
            workThd_dbQuery.join();

            try {
                JSONArray ja = new JSONArray(strJson);

                if (ja.length() > 0) {
                    JSONObject jObj = ja.getJSONObject(0);

                    vPLANT_CD = jObj.getString("PLANT_CD");
                    vUNIT_CD = jObj.getString("UNIT_CD");
                    vUNIT_NM = jObj.getString("UNIT_NM");
                    vUNIT_TYPE = jObj.getString("UNIT_TYPE");
                }
            } catch (JSONException exJson) {
//                TGSClass.AlertMessage(getApplicationContext(), exJson.getMessage());
                TGSClass.showToast(MainActivity.this, "ERROR", "Json Error", exJson.getMessage());
            } catch (Exception ex) {
//                TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
                TGSClass.showToast(MainActivity.this, "ERROR", "Exception Error", ex.getMessage());
            }
        } catch (InterruptedException exInterrup) {
//            TGSClass.AlertMessage(getApplicationContext(), exInterrup.getMessage());
            TGSClass.showToast(MainActivity.this, "ERROR", "Interrup Error", exInterrup.getMessage());
        }

        return vPLANT_CD;
    }

    /**
     * 로그인 메소드
     * @param pID ID
     * @param pPWD Password
     */
    private void login(final String pID, final String pPWD) {

        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workThd_logIn = new Thread() {
            public void run() {

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                global.setLoginIDString(pID);
                global.setPlantCDString(strPlantCD);

                ArrayList<PropertyInfo> pParams = new ArrayList<>();

                PropertyInfo paramID = new PropertyInfo();
                paramID.setName("pUSER_ID");
                paramID.setValue(pID);
                paramID.setType(String.class);
                pParams.add(paramID);

                PropertyInfo paramPWD = new PropertyInfo();
                paramPWD.setName("pPass");
                paramPWD.setValue(pPWD);
                paramPWD.setType(String.class);
                pParams.add(paramPWD);

                PropertyInfo paramIP = new PropertyInfo();
                paramIP.setName("pClientIP");
                paramIP.setValue(strClientIP);
                paramIP.setType(String.class);
                pParams.add(paramIP);


                PropertyInfo paramHostName = new PropertyInfo();
                paramHostName.setName("pClientHostName");
                paramHostName.setValue(strClientHostNm);
                paramHostName.setType(String.class);
                pParams.add(paramHostName);

                String vResponse = dba.SendHttpMessage("Login", pParams);

                Bundle bun = new Bundle();
                bun.putString("HTML_DATA", vResponse);
                bun.putString("TYPE", "LOGIN");
                bun.putString("ID", pID);

                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        };
        workThd_logIn.start();
        try {
            workThd_logIn.join();
        } catch (InterruptedException exInterrup) {

        }
    }

    /**
     * MES에 등록된 사용자 자동 로그인(dbQuery_auto_login)
     * @param pHostName Host명
     */
    public void dbQuery_auto_login(final String pHostName) {
        Thread workThd_dbQuery_auto_login = new Thread() {
          public void run() {
              String sql = " EXEC XUSP_AUTO_LOGIN_CHECK_YN_ANDROID ";
              sql += " @UNIT_CD = '" + pHostName + "'";

              DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

              ArrayList<PropertyInfo> pParams = new ArrayList<>();

              PropertyInfo param = new PropertyInfo();
              param.setName("pSQL_Command");
              param.setValue(sql);
              param.setType(String.class);
              pParams.add(param);

              strJson_auto_login = dba.SendHttpMessage("GetSQLData", pParams);
          }
        };
        workThd_dbQuery_auto_login.start();

        try {
            workThd_dbQuery_auto_login.join();
            try {

                if (TGSClass.isJsonData(strJson_auto_login)) {
                    JSONArray ja = new JSONArray(strJson_auto_login);

                    if (!strJson_auto_login.equals("[]")) {
                        //== SESSION에 값 추가 ==//
                        for (int idx = 0; idx < ja.length(); idx++) {
                            JSONObject jObject = ja.getJSONObject(idx);

                            String login_id = jObject.getString("USER_ID");
                            global.setLoginIDString(login_id);
                            //global.setLoginIDString(strClientHostNm);
                            global.setUnitCDString(strClientHostNm);
                            global.setPlantCDString(strPlantCD);


                            Intent intent = TGSClass.ChangeView(getPackageName(), MenuActivity.class);
                            intent.putExtra("nowVer", intVersionNo);
                            intent.putExtra("newVer", intVersion);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (InterruptedException exInterrup) {

        }
    }

    /**
     * 로그인 체크 메소드
     * @param pID ID
     * @param pPWD Password
     * @return 로그인 정보 여부 True or False
     */
    private boolean logIn_chk(final String pID, final String pPWD) {
        chkLogIn = true;
        Thread workThd_logIn_chk = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_PDA_LOGIN_CHK ";
                sql += " @USER_ID = '" + pID + "',";
                sql += " @PW = '" + pPWD + "'";
                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParams = new ArrayList<>();

                PropertyInfo param = new PropertyInfo();
                param.setName("pSQL_Command");
                param.setValue(sql);
                param.setType(String.class);
                pParams.add(param);

                strJson = dba.SendHttpMessage("GetSQLData", pParams);
            }
        };
        workThd_logIn_chk.start();
        try {
            workThd_logIn_chk.join();

            try {
                boolean jSonType = TGSClass.isJsonData(strJson);

                if (jSonType) {
                    if (!strJson.equals("[]")) {
                        try {
                            JSONArray ja = new JSONArray(strJson);

                            if (ja.length() > 0) {
                                JSONObject jObj = ja.getJSONObject(0);

                                strUSER_ID = jObj.getString("USER_ID");
                                strPWD = jObj.getString("PW");
                            }
                        } catch (JSONException exJson) {
//                            TGSClass.AlertMessage(getApplicationContext(), exJson.getMessage());
                            TGSClass.showToast(MainActivity.this, "ERROR", "Json Error", exJson.getMessage());
                        }
                    } else {
                        //Toast.makeText(getApplicationContext(), "로그인 정보를 다시 입력 해주시기 바랍니다.", Toast.LENGTH_LONG).show();
                        chkLogIn = false;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (InterruptedException exInterrup) {
//            TGSClass.AlertMessage(getApplicationContext(), exInterrup.getMessage());
            TGSClass.showToast(MainActivity.this, "ERROR", "Interrup Error", exInterrup.getMessage());
            return false;
        }

        if (!chkLogIn) {
//            Toast.makeText(getApplicationContext(), "로그인 정보가 바르지 않습니다. 다시 입력 부탁드립니다.", Toast.LENGTH_LONG).show();
            TGSClass.showToast(MainActivity.this, "WARNING", "", "로그인 정보가 바르지 않습니다.\n다시 입력 부탁드립니다.");
            return false;
        }
        return true;
    }

    /**
     * 로그인 관련 핸들러
     */
    Handler handler = new Handler() {
        //스레드에서 값을 받아와서 화면에 뿌림 //
        public void handleMessage(Message msg) {

            Bundle bun = msg.getData();

            String vType = bun.getString("TYPE");
            String vResponse = bun.getString("HTML_DATA");
            String vID = bun.getString("ID");

            switch (vType) {
                case "LOGIN":
                    if (vResponse.equals("OK")) {
                        TGSClass.showToast(MainActivity.this, "SUCCESS", "", "인증완료");
//                        TGSClass.AlertMessage(MainActivity.this, "인증완료");

                        //== SESSION에 값 추가 ==//
                        global.setLoginIDString(vID);
                        global.setUnitCDString(strClientHostNm);
                        global.setPlantCDString(strPlantCD);

                        Intent intent = TGSClass.ChangeView(getPackageName(), MenuActivity.class);
                        intent.putExtra("nowVer", intVersionNo);
                        intent.putExtra("newVer", intVersion);
                        startActivity(intent);

                        finish();
                    } else if (vResponse.equals("FAIL")) {
//                        TGSClass.AlertMessage(getApplicationContext(), "*계정 및 패스워드를 확인해주세요*");
                        TGSClass.showToast(MainActivity.this, "WARNING", "", "*계정 및 패스워드를 확인해주세요*");
                    } else {
//                        TGSClass.AlertMessage(getApplicationContext(), vResponse);
                        TGSClass.showToast(MainActivity.this, "ERROR", "Else", vResponse);
                    }
                    break;
            }
        }
    };



    ////////////////////////////// 권한, 체크 관련 메소드 정의 ////////////////////////////////////////////////////////

    /**
     * 앱 체크
     */
    protected boolean chkApp() {

        /* 현재 설치된 APP 의 버전 정보를 가져온다. */
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException ex) {
            //ex.printStackTrace();
            Log.w(TAG, "something error!!", ex);
        }

        strVersionNm = pInfo.versionName;
        intVersionNo = pInfo.versionCode;

        System.out.println("version:"+intVersionNo);

        /* 현재 설치된 APP 의 버전 정보와 다운로드 받을 경로 정보를 데이터베이스에서 받아옵니다. */
        if (!getVersionFromServer()) {
            Intent error_intent = TGSClass.ChangeView(getPackageName(), ErrorPopupActivity.class);
            error_intent.putExtra("MSG", strErrorMsg);
            startActivity(error_intent);
            finish();
            return false;
        }
        return true;
    }

    /**
     * 버전체크(chkVersion)
     */
    public void chkVersion() {
        /* 현재 설치된 APP 보다 새 버전의 APP 정보가 존재하면 */
        if (intVersionNo < intVersion) {
            //-- 업데이트 파일이 존재한다는 팝업창 띄우기 --//
            // 앱 여러번 실행 시
            String vTitle = "버전 업데이트 현재버전 : " + intVersionNo + ", 새버전 : " + intVersion;
            String vMsg = "앱 업데이트를 해야합니다.\n업데이트를 진행하겠습니까? 취소 하면 프로그램이 종료됩니다.";

            MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(this);
            mAlert.setTitle(vTitle) //메세지 타이틀
                    .setMessage(vMsg) //메세지 내용
                    .setCancelable(false) // Dialog 밖이나 뒤로가기를 막기 위한 소스 true : 해제, false : 설정
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //APP을 업데이트 시키기 위한 메소드
                            update_user_application(MainActivity.mContext);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            TGSClass.AlertMessage(MainActivity.this, "프로그램을 종료합니다.");
                            TGSClass.showToast(MainActivity.this, "INFO", "", "프로그램을 종료합니다.");
                            finishAffinity();
                        }
                    })
                    .show();
        } else {
            //-- 업데이트 할 필요가 없으면 앱 실행 --//
            updateChk = true;
            start();
        }
    }

    /**
     *  권한 체크 메소드
     */
    private void chkPermission() {
        //-- 카메라 권한 --//
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        //-- 외부저장소 권한 --//
        int writeExteralStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //-- 카메라, 외부저장소 권한 체크 --//
        if (cameraPermission == PackageManager.PERMISSION_GRANTED
                && writeExteralStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //-- 권한을 다 가지고 있따면 동작 --//
            permissionChk = true;
            //-- 앱 체크 --//
            if (chkApp()) {
                //-- 버전 체크 --//
                chkVersion();
            }
        } else {
            //-- 권한 요청 --//
            setPermission();
        }
    }

    /**
     * APK 버전과 config 값 가져오기
     * @return True or False
     */
    public boolean getVersionFromServer() {
        boolean vReturn = false;
        Thread workThd_getVersionFromServer = new Thread() {
            public void run() {
                String sql = " EXEC XUSP_AND_APP_VERSION_GET @FILENAME = '" + FILE_NAME + "'";

                System.out.println("sqls:"+sql);

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParams = new ArrayList<>();

                PropertyInfo param = new PropertyInfo();
                param.setName("pSQL_Command");
                param.setValue(sql);
                param.setType(String.class);

                pParams.add(param);

                strJsonVersion = dba.SendHttpMessage("GetSQLData", pParams);
                System.out.println("strJsonVersion:"+strJsonVersion);

            }
        };

        workThd_getVersionFromServer.start(); //스레드 시작

        try {
            workThd_getVersionFromServer.join(); //workThd 가 종료될때까지 Main 쓰레드를 정지함.
            try {
                boolean vJsonType = TGSClass.isJsonData(strJsonVersion);

                if (!vJsonType) {
                    strErrorMsg = strJsonVersion;
                    return false;
                }

                JSONArray ja = new JSONArray(strJsonVersion);

                if (ja.length() > 0) {
                    JSONObject jObj = ja.getJSONObject(0);

                    DOWNLOAD_URL = String.valueOf(jObj.getString("UPDATE_URL"));
                    String version = String.valueOf(jObj.getString("VERSION_CD"));
                    intVersion = Integer.parseInt(version);
                }

                vReturn = true;
            } catch (JSONException exJson) {
//                TGSClass.AlertMessage(getApplicationContext(), exJson.getMessage());
                TGSClass.showToast(MainActivity.this, "ERROR", "Json Error", exJson.getMessage());
            } catch (Exception ex) {
//                TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
                TGSClass.showToast(MainActivity.this, "ERROR", "Exception Error", ex.getMessage());
            }
        } catch (InterruptedException exInterrup) {
//            TGSClass.AlertMessage(getApplicationContext(), exInterrup.getMessage());
            TGSClass.showToast(MainActivity.this, "ERROR", "Interrup Error", exInterrup.getMessage());
        }

        return vReturn;
    }
    dialog_Loading LoadingDialog;
    public void update_user_application(Context context) {
        try{

        }
      catch (Exception e){
          Toast.makeText(getApplicationContext(), "업데이트 실패! 관리자에 문의하세요", Toast.LENGTH_LONG).show();

      }
//로딩창 띄우기
        LoadingDialog = new dialog_Loading(context);
        LoadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LoadingDialog.show();

        //다운로드 쓰레드 실행
        DonwloadThread downThread = new DonwloadThread();
        downThread.setRunning(true);
        downThread.start();

        //-- 1. 파일 다운로드 --//
        //downloadAPK(DOWNLOAD_URL);
        //-- 2. 다운로드 설치 관리자로 설치 --//
        //updateAPK();
        //-- 3. 앱 종료 --//
        //finishAffinity();
    }

    /**
     * 파일다운로드(downloadAPK)
     * 다운로드 로딩창과 응답없음 방지를 위해 다운로드 쓰레드 적용
     */
    public class DonwloadThread extends Thread {
        volatile boolean running = false;
        void setRunning(boolean b) {
            running = b;
        }
        @Override
        public void run() {
            System.out.println("running1:"+running);
            while (running) {
                try {
                    sleep(500);
                    System.out.println("running2:"+running);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    //다운로드 페이지 접속
                    URL url = new URL(DOWNLOAD_URL);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    //c.setDoOutput(true);
                    c.connect();

                    //다운로드 받을 폴더 설정.
                    String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GMAX";
                    File file = new File(PATH);
                    file.mkdir();
                    String filePath = file.getPath();

                    //다운로드 받을 파일명 설정
                    File outputFile = new File(filePath, FILE_NAME);
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    System.out.println("running3:"+running);

                    //파일 다운로드 진행
                    InputStream is = c.getInputStream();
                    System.out.println("running4:"+running);

                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);
                    }
                    System.out.println("running5:"+running);

                    fos.close();
                    is.close(); //till here, it works fine - .apk is download to my sdcard in download file
                    running = false;
                } catch (MalformedURLException exURL) {
                    Toast.makeText(getApplicationContext(), exURL.toString(), Toast.LENGTH_LONG);
                } catch (IOException exIO) {
                    String aaa = exIO.toString();

                    Toast.makeText(getApplicationContext(), exIO.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }

            }
            downhandler.sendMessage(handler.obtainMessage());
        }
    }

    //다운로드 이후 핸들러로 메시지 받아서 이후 업데이트 진행
    Handler downhandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            //다운로드 쓰레드 종료
            if (LoadingDialog.isShowing()) {
                LoadingDialog.dismiss();
            }
            //-- 2. 다운로드 설치 관리자로 설치 --//
            updateAPK();
            //-- 3. 앱 종료 --//
            finishAffinity();
        }
    };


    /**
     * 파일다운로드(downloadAPK)
     * @param pApk_url /APK URL
     */
   /*private void downloadAPK(final String pApk_url) {
//        TGSClass.AlertMessage(this, pApk_url + "파일 다운로드 시작", 2000);
        Thread downThd = new Thread() {
            public void run() {
                try {
                    //다운로드 페이지 접속
                    URL url = new URL(pApk_url);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    //c.setDoOutput(true);
                    c.connect();

                    //다운로드 받을 폴더 설정.
                    String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GMAX";
                    File file = new File(PATH);
                    file.mkdir();
                    String filePath = file.getPath();

                    //다운로드 받을 파일명 설정
                    File outputFile = new File(filePath, FILE_NAME);
                    FileOutputStream fos = new FileOutputStream(outputFile);

                    //파일 다운로드 진행
                    InputStream is = c.getInputStream();

                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);
                    }
                    fos.close();
                    is.close(); //till here, it works fine - .apk is download to my sdcard in download file

                } catch (MalformedURLException exURL) {
                    Toast.makeText(getApplicationContext(), exURL.toString(), Toast.LENGTH_LONG);
                } catch (IOException exIO) {
                    String aaa = exIO.toString();

                    Toast.makeText(getApplicationContext(), exIO.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        downThd.start(); //스레드 시작
        try {
            downThd.join();
        } catch (InterruptedException exInterrupt) {

        }
    }*/

    /**
     * 다운받은 APK를 설치(updateAPK)
     */
    public void updateAPK() {
        try {
            //File file = new File(Environment.getExternalStorageDirectory() + FILE_NAME);

            File directory = Environment.getExternalStoragePublicDirectory("GMAX");
            File file = new File(directory, FILE_NAME);

            Uri fileUri = Uri.fromFile(file); //for Build.VERSION.SDK_INT <= 24

            if (Build.VERSION.SDK_INT >= 24) {
                fileUri = FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID+".fileprovider" , file);
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
                    .setDataAndType(fileUri, "application/vnd.android.package-archive")
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //don't forget add this line
            startActivity(intent);

        } catch (Exception ex) {
//            TGSClass.AlertMessage(this, ex.toString());
            TGSClass.showToast(MainActivity.this, "ERROR", "Exception Error", ex.getMessage());
        }
    }

    /**
     * 권한 요청(setPermission)
     */
    public void setPermission() {
        //-- 권한 요청을 허용한 적이 없다면 권한 요청(2가지 경우) --//
        //-- 1. 사용자가 권한 거부를 한 적이 있는 경우 --//
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
            //-- 요청을 진행하기 전에 사용자에게 권한이 필요한 이유를 설명 --//
            String vMsg = "APP을 정상적으로 사용하기 위해서 해당 권한이 필요합니다.";

            MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(this);
            mAlert.setTitle("APP 권한 요청")
                    .setMessage(vMsg)
                    .setCancelable(false) // Dialog 밖이나 뒤로가기를 막기위한 소스 True : 해제, False : 설정
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); //닫기
                            ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, REQUEST_CODE_WRITE_STORAGE_PERMISSIONS);
                        }
                    })
                    .show();
        } else {
            //-- 2. 사용자가 권한을 거부한 적이 없는 경우 --//
            //-- 권한 요청을 바로 실시 --//
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_WRITE_STORAGE_PERMISSIONS);
        }
    }

    /**
     * 권한 요청 결과 메소드
     * @param requestCode 요청 코드
     * @param permissions 권한
     * @param grantResults 요청한 권한
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_STORAGE_PERMISSIONS && grantResults.length == REQUIRED_PERMISSIONS.length) {
            //-- 요청 코드가 REQUEST_CODE_WRITE_STORAGE_PERMISSIONS 이고, 요청한 권한 개수만큼 수신되었다면 --//
            boolean chk_result = true;

            //-- 모든 권한을 허용했는지 체크 --//
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    chk_result = false;
                    break;
                }
            }

            if (chk_result) {
                //-- 모든 권한을 허용했다면 앱체크와 버전체크 실행 --//
                permissionChk = false;
                //== 앱 체크 ==//
                chkApp();
                //== 버전 체크 ==//
                chkVersion();
            } else {
                //-- 거부한 권한이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱 종료 --//
                //-- 2가지 경우로 나뉨 --//
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    //-- 1.1 사용자가 거부를 선택한 경우에는 APP을 다시 실행하고 허용을 선택하면 앱을 사용할 수 있다. --//
                    MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(this);
                    mAlert.setMessage("필수 권한이 거부되었습니다. 앱을 다시 실행하여 권한을 허용해주세요.")
                            .setCancelable(false) // Dialog 밖이나 뒤로가기를 막기위한 소스 true : 해제, false : 설정
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .create().show();
                } else {
                    //-- 2.1 사용자가 "다시 묻지 않음"을 선택한 경우에는 설정(앱 정보)에서 권한을 허용해야 앱을 사용할 수 있다. --//
                    MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(this);
                    mAlert.setMessage("필수 권한이 거부되었습니다. 설정(앱 정보)에서 권한을 허용해주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                            , Uri.fromParts("package", getPackageName(), null));
                                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .create().show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case DOWNLOAD_ACTIVITY_REQUEST_CODE:
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case DOWNLOAD_ACTIVITY_REQUEST_CODE:
//                    TGSClass.AlertMessage(this, "이미 등록된 장비입니다.\n로그인을 진행해 주시길 바랍니다.");
                    TGSClass.showToast(MainActivity.this, "WARNING", "", "이미 등록된 장비입니다.\n로그인을 진행해 주시길 바랍니다.");
                    break;
                default:
                    break;
            }
        }
    }
}