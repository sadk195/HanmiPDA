package com.example.gmax;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;

import org.ksoap2.serialization.PropertyInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import www.sanju.motiontoast.MotionToast;

public class BaseActivity extends AppCompatActivity {

    //== JSON 선언 ==//
    protected static String sJson_grant = "", sJson_menu_grant = "";

    //== SESSION 선언 ==//
    protected static MySession global;

    //== SESSION에서 받을 변수 선언 ==//
    protected static String vUSER_ID, vPLANT_CD, vUNIT_CD;

    //== Progress Dialog 정의 ==//
    protected static ProgressDialog mProgressDialog;
    protected static AsyncTask<Integer, String, Integer> mDlg;

    //== QR 스캔 관련 변수 ==//
    protected IntentIntegrator qrScan;

    //== 날짜관련 변수 선언 ==//
    protected DateFormat df;
    protected SimpleDateFormat simpleDF;

    //== 숫자 포맷 ==//
    protected DecimalFormat decimalForm;

    //== 저장 시 BL 후 메시지 ==//
    protected String result_msg = "";

    //== Toast 변수 ==//
    int position = MotionToast.GRAVITY_BOTTOM;
    long duration = MotionToast.SHORT_DURATION;

    protected BaseActivity() { /* compiled code */ }

    protected void init() {
        //== SESSION 정의 ==//
        global = (MySession) getApplication();

        //== SESSION 값 바인딩 ==//
        vUSER_ID = global.getLoginIDString();
        vPLANT_CD = global.getPlantCDString();
        //테스트용 공장코드 삽입
        //vPLANT_CD = "4000";
        vPLANT_CD = global.getPlantCDString();

        vUNIT_CD = global.getUnitCDString();

        //== QR코드 ==//
        qrScan = new IntentIntegrator(this);

        //== DateFormat init ==//
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        //== DecimalFormat init ==//
        decimalForm = new DecimalFormat("###,###");
    }

    //== 권한 가져오기 ==//
    protected static void getMenuGrant() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_getMajorMenuGrant = new Thread() {
            public void run() {
                String sql = " exec USP_MENU_LOAD";
                sql += " @FLAG = '0'";                      //== 0 : 사용하는메뉴만, 1 : 전체 : 1(기본값 0)
                sql += ", @USER_ID = '" + vUSER_ID + "'";   //== USER_ID
                sql += ", @PAR_ID = 'APK'";                 //== APK
                sql += ", @LANG_CD = 'KO'";                 //== KO

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_menu_grant = dba.SendHttpMessage("GetSQLData", pParms);
            }

        };
        wkThd_getMajorMenuGrant.start();   //스레드 시작
        try {
            wkThd_getMajorMenuGrant.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    //== 권한 가져오기 ==//
    protected static void getGrant(final String USER_ID) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread wkThd_getGrant = new Thread() {
            public void run() {
                String sql = " exec XUSP_APK_USER_GRANT_CHK ";
                sql += " @USER_ID = '" + USER_ID + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_grant = dba.SendHttpMessage("GetSQLData", pParms);
            }

        };
        wkThd_getGrant.start();   //스레드 시작
        try {
            wkThd_getGrant.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    //== 로딩 Dialog 시작 ==//
    protected static void progressStart(Context context, String title, String msg) {
        if (msg == null) msg = "잠시만 기다려주세요..";
        mProgressDialog = ProgressDialog.show(
                context, title, msg
        );
    }

    protected static void progressStart(Context context, String title) {
        progressStart(context, title, null);
    }

    protected static void progressStart(Context context) {
        progressStart(context, null);
    }

    //== 로딩 Dialog 끝 ==//
    protected static void progressEnd() {
        mProgressDialog.dismiss();
    }

    //== 로딩화면2 만들기
    protected void loadingStart(final Context context) {
        // 로딩
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog = new ProgressDialog(context);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setMessage("잠시만 기다려 주세요.");
                        mProgressDialog.show();
                    }
                }
                , 0);
    }

    //== 로딩화면2 닫기
    protected void loadingEnd() {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                    }
                }
                , 0);
    }

    //== 날짜 팝업 ==//
    protected void openPopupDate(final View v, final EditText et, final Calendar c) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                et.setText(df.format(c.getTime()));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    //== 날짜 팝업 - MaterialDatePicker(TextInputLayout) ==//

    /**
     * MaterialDatePicker(TextInputLayout) - No Title
     * @param pView
     * @param pTextInputLayout
     * @param pCalendar
     */
    protected void openDate(final View pView, final TextInputLayout pTextInputLayout, final Calendar pCalendar) {
        openDate(pView, pTextInputLayout, pCalendar, "날짜 선택");
    }

    /**
     * MaterialDatePicker(TextInputLayout) - Title
     * @param pView
     * @param pTextInputLayout
     * @param pCalendar
     * @param pTitle
     */
    protected void openDate(final View pView, final TextInputLayout pTextInputLayout, final Calendar pCalendar, final String pTitle) {
        MaterialDatePicker.Builder<Long> mDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(pTitle)
                .setSelection(pCalendar.getTimeInMillis())
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT);

        // 날짜 셋팅
        MaterialDatePicker materialDatePicker = mDatePicker.build();
        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

        // 확인 버튼
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Date date = new Date();

                date.setTime(selection);

                pCalendar.setTime(date);

                String strDate = simpleDF.format(date);

                pTextInputLayout.getEditText().setText(strDate);
            }
        });
    }

    /**
     * MaterialDatePicker(TextView) - No Title
     * @param pTextView
     * @param pCalendar
     */
    /*
    protected void openDate(final TextView pTextView, final Calendar pCalendar) {
        openDate(pTextView, pCalendar, "날짜 선택");
    }
    */

    /**
     * MaterialDatePicker(TextView) - Title
     * @param pTextView
     * @param pCalendar
     * @param pTitle
     */
    protected void openDate(final TextView pTextView, final Calendar pCalendar, final String pTitle) {
        MaterialDatePicker.Builder<Long> mDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(pTitle)
                .setSelection(pCalendar.getTimeInMillis())
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT);

        // 날짜 셋팅
        MaterialDatePicker materialDatePicker = mDatePicker.build();
        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

        // 확인 버튼
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Date date = new Date();

                date.setTime(selection);

                pCalendar.setTime(date);

                String strDate = simpleDF.format(date);

                pTextView.setText(strDate);
            }
        });
    }

    /**
     * MaterialDatePicker - No Title
     * @param pView
     * @param pCalendar
     */
    protected void openDate(final View pView, final Calendar pCalendar) {
        openDate(pView, pCalendar, "날짜 선택");
    }

    /**
     * MaterialDatePicker - Title
     * @param pView
     * @param pCalendar
     * @param pTitle
     */
    protected void openDate(final View pView, final Calendar pCalendar, final String pTitle) {
        MaterialDatePicker.Builder<Long> mDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(pTitle)
                .setSelection(pCalendar.getTimeInMillis());

        // 날짜 셋팅
        MaterialDatePicker materialDatePicker = mDatePicker.build();
        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

        // 확인 버튼
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Date date = new Date();

                date.setTime(selection);

                pCalendar.setTime(date);

                String strDate = simpleDF.format(date);

                if (pView instanceof TextView) {
                    TextView tv = (TextView) pView;

                    tv.setText(strDate);
                } else if (pView instanceof TextInputLayout) {
                    TextInputLayout til = (TextInputLayout) pView;
                    til.getEditText().setText(strDate);
                }
            }
        });
    }

    //== 날짜 팝업 - MaterialDatePicker : Range ==//
    /**
     * MaterialDateRangePicker - No Title
     * @param pView
     * @param pCal1
     * @param pCal2
     */
    protected void openFragRangeDate(final View pView, final Calendar pCal1, final Calendar pCal2) {
        openFragRangeDate(pView, pCal1, pCal2, "기간 선택");
    }

    /**
     * MaterialDateRangePicker - Title
     * @param pView
     * @param pCal1
     * @param pCal2
     * @param pTitle
     */
    protected void openFragRangeDate(final View pView, final Calendar pCal1, final Calendar pCal2, final String pTitle) {
        MaterialDatePicker.Builder<Pair<Long, Long>> mDateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(pTitle)
                .setSelection(Pair.create(pCal1.getTimeInMillis(), pCal2.getTimeInMillis()));

        // 날짜 셋팅
        MaterialDatePicker materialDatePicker = mDateRangePicker.build();
        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Date dt1 = new Date();
                Date dt2 = new Date();

                dt1.setTime(selection.first);
                dt2.setTime(selection.second);

                pCal1.setTime(dt1);
                pCal2.setTime(dt2);

                String strDate1 = simpleDF.format(dt1);
                String strDate2 = simpleDF.format(dt2);


                if (pView instanceof TextView) {
                    TextView tv = (TextView) pView;

                    tv.setText(strDate1 + " ~ " + strDate2);
                } else if (pView instanceof EditText) {
                    EditText et = (EditText) pView;

                    et.setText(strDate1 + " ~ " + strDate2);
                } else if (pView instanceof TextInputLayout) {
                    TextInputLayout til = (TextInputLayout) pView;

                    til.getEditText().setText(strDate1 + " ~ " + strDate2);
                }
            }
        });
    }

    public View.OnClickListener qrClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            qrScan.setPrompt("VISS 바코드를 스캔하세요");
            qrScan.setOrientationLocked(true);
            qrScan.initiateScan();
        }
    };

    public void showKeyboard(View v) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // 키보드 호출
        manager.showSoftInput(v.getRootView(), InputMethodManager.SHOW_IMPLICIT);

        // 포커스 지정
        v.requestFocus();
    }

    public void hideKeyboard(View v) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // 키보드 숨김
        manager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    public static void dataSaveLog(String _log, String _fileName) {
        /* SD CARD 하위에 LOG 폴더를 생성하기 위해 미리 dirPath에 생성 할 폴더명을 추가 */
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LOG/";
        System.out.println("dir:"+dirPath);
        File file = new File(dirPath);

        //현재날짜 시간 가져오기
        long now = System.currentTimeMillis();

        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String getTime = sdf.format(date);


        // 일치하는 폴더가 없으면 생성
        if (!file.exists())
            file.mkdirs();

        // txt 파일 생성
        File savefile = new File(dirPath + "LOG_" + _fileName + ".txt");
        try {
            BufferedWriter bfw = new BufferedWriter(new FileWriter(savefile, true));
           //BufferedWriter bfw = new BufferedWriter(new FileWriter(dirPath + "LOG_" + _fileName + ".txt", true));
            bfw.write(getTime+" : ");
            bfw.write(_log);
            bfw.write("\n");
            bfw.flush();
            bfw.close();
        } catch (IOException e){
            /* Exception */
        }
    }
    /**
     * 텍스트뷰 생성
     private void createTextView(LinearLayout layout, String txt, int i) {
     // 1. 텍스트뷰 객체생성
     TextView tv = new TextView(getApplicationContext());
     // 2. 텍스트뷰에 들어갈 문자설정
     tv.setText(txt);
     // 3. 텍스트뷰 글자크기, 색상 설정
     tv.setTextSize(FONT_SIZE); // 텍스트 크기
     tv.setTextColor(getResources().getColor(R.color.design_default_color_primary));
     //tv.setTextColor(ContextCompat.getColor(this, R.color.design_default_color_primary));
     // 4. 텍스트뷰 글자타입 설정
     //tv.setTypeface(null, Typeface.BOLD);
     // 5. 텍스트뷰 ID 설정
     tv.setId(i);
     // 6. 레이아웃 설정
     LinearLayout.LayoutParams param
     = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
     param.rightMargin = 15;
     // 7. 설정한 레이아웃 텍스트뷰에 적용
     tv.setLayoutParams(param);
     // 8. 텍스트뷰 백그라운드색상 설정
     //tv.setBackgroundColor(Color.rgb(184,236,184));
     // 9. 생성 및 설정된 텍스트뷰 레이아웃에 적용
     layout.addView(tv);
     }
     */
}
