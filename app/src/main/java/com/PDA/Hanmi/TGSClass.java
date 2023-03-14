package com.PDA.Hanmi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class TGSClass {

    //웹서비스 네임스페이스 정의

    //public static String ws_name_space = "http://tgs.com/";
    public static String ws_name_space = "http://localhost/";

    //    public static String ws_url = "http://106.245.142.2:8078/MES_ANDROID/webService.asmx"; //-- TGS DB(unierp5_GMAX)
    public static String ws_url = "http://106.245.142.3/HanmiTest/webService.asmx"; //-- TGS DB(unierp5_HANMI)
    //public static String ws_url = "http://106.245.142.3/WMS_TEST/webService.asmx"; //-- TGS DB(unierp5_GMAX)


    public TGSClass() {

    }

    /**
     * 변경할 Intent 반환
     * @param pPackageName /패키지 명칭(네임스페이스)
     * @param pClassName /클래스 네임(뷰 클래스 이름)
     * @return Intent 객체(보여질 화면)
     */
    public static Intent ChangeView(String pPackageName, String pClassName) {

        String vComponentName = pPackageName + "." + pClassName;
        //Intent intent = new Intent(getApplicationContext(), SubActivity.class);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pPackageName, vComponentName));

        return intent;
    }

    /**
     * 변경할 Intent 반환(menu)
     * @param pPackageName /패키지 명칭(네임스페이스)
     * @param pClassName /클래스 네임(뷰 클래스 이름)
     * @return Intent 객체(보여질 화면)
     */
    public static Intent ChangeView(String pPackageName, Class pClassName) {

        String vComponentName = pClassName.getName();
        //Intent intent = new Intent(getApplicationContext(), SubActivity.class);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pPackageName, vComponentName));

        return intent;
    }

    /**
     * 현재 화면을 변경합니다.
     * @param pContext /컨텍스트 명칭(getApplicationContext())
     * @param pPackageName /패키지 명칭(getPackageName())
     * @param pClassName /클래스 네임(Activity Name)
     * @return boolean (뷰 액티비티 존재 유무)
     */
    public static boolean ChangeView(Context pContext, String pPackageName, String pClassName) {

        String vComponentName = pPackageName + "." + pClassName;
        //Intent intent = new Intent(getApplicationContext(), SubActivity.class);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pPackageName, vComponentName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //startActivity를 외부 클래스(TGS Class)에서 실행하기 위한 설정값

        boolean isActivity = true;

        if (isActivity) {
            //Activity 리스트 중에서 일치하는 Activity Class 가 있으면 화면 변경
            pContext.startActivity(intent);
        } else {
            //Activity 리스트 중에서 일치하는 Activity Class 가 없으면 에러 메세지 출력

        }

        return isActivity;
    }

    /**
     * Dialog 메세지 출력
     * @param pContext /컨텍스트 명칭
     * @param pTitle /메세지 타이틀
     * @param pMessage /메세지 내용
     */
    public static void MessageBox(Context pContext, String pTitle, String pMessage) {
        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(pContext);
        mAlert.setTitle(pTitle)
                .setMessage(pMessage)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //닫기
                    }
                })
                .show();
        /*
        AlertDialog.Builder alert = new AlertDialog.Builder(pContext);

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //닫기
            }
        });
        alert.setTitle(pTitle).setMessage(pMessage).show();
         */
    }

    public static void AlertMessage(Context pContext, String pMessage) {
        AlertMessage(pContext, pMessage, 500);
    }

    public static void AlertMessage(Context pContext, String pMessage, int pTime) {
        final Toast toast = Toast.makeText(pContext, pMessage, Toast.LENGTH_SHORT);
        showMyToast(toast, pTime);
    }

    /**
     * 하단 메세지를 출력
     * @param pToast /컨텍스트 명(Toast)
     * @param pDelay /메세지 보여줄 시간 - 밀리 세컨드(delay)
     */
    public static void showMyToast(final Toast pToast, final int pDelay) {
        pToast.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pToast.cancel();
            }
        }, pDelay);
    }

    /**
     * Motion Toast 생성
     * @param pActivity / 액티비티
     * @param pTag / Toast 구분(INFO, SUCCESS, WARNING, ERROR)
     * @param pTitle / 타이틀
     * @param pMsg / 메세지
     */
    public static void showToast(Activity pActivity, String pTag, String pTitle, String pMsg) {

        //== Toast 변수 ==//
        int position = MotionToast.GRAVITY_BOTTOM;
        long duration = MotionToast.SHORT_DURATION;

        Typeface typeface = ResourcesCompat.getFont(pActivity, R.font.helvetica_regular);

        switch (pTag.toUpperCase()) {
            case "INFO":
                MotionToast.Companion.createColorToast(pActivity, pTitle, pMsg, MotionToastStyle.INFO, position, duration, typeface);
                break;
            case "SUCCESS":
                MotionToast.Companion.createColorToast(pActivity, pTitle, pMsg, MotionToastStyle.SUCCESS, position, duration, typeface);
                break;
            case "WARNING":
                MotionToast.Companion.createColorToast(pActivity, pTitle, pMsg, MotionToastStyle.WARNING, position, duration, typeface);
                break;
            case "ERROR":
                MotionToast.Companion.createColorToast(pActivity, pTitle, pMsg, MotionToastStyle.ERROR, position, duration, typeface);
                break;
            case "DELETE":
                MotionToast.Companion.createColorToast(pActivity, pTitle, pMsg, MotionToastStyle.DELETE, position, duration, typeface);
                break;
        }
    }

    /**
     * IP 주소 받아오는 메서드
     * @return IP 반환
     */
    public static String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface inf = en.nextElement();
                for (Enumeration<InetAddress> listIP = inf.getInetAddresses(); listIP.hasMoreElements(); ) {
                    InetAddress inetAddress = listIP.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof InetAddress) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException exSocket) {
            exSocket.printStackTrace();
        }
        return null;
    }

    /**
     * 문자열이 JSON 데이터 형식인지 확인
     * @param pJsonString /확인할 JSON 문자열
     * @return True or False
     */
    public static boolean isJsonData(String pJsonString) {
        try {
            JSONArray ja = new JSONArray(pJsonString);
            return true;
        } catch (JSONException exJson) {
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 문자열이 숫자인지 문자인지 판별
     * @param pValue /문자열
     * @return /숫자인지 문자인지
     */
    public static boolean isNumeric(String pValue) {
        Pattern pattern = Pattern.compile("^(-?0|-?[1-9]\\d*)(\\.\\d+)?(E\\d+)?$");
        return pValue != null && pattern.matcher(pValue).matches();
    }

    /**
     * Intent 존재 여부 확인
     * @param pContext /컨텍스트 명칭(getApplicationContext())
     * @param pPackageName /패키지 명칭(getPackageName())
     * @param pClassName /클래스 네임(Activity Name)
     * @return
     */
    public static boolean isIntentAvailable(Context pContext, String pPackageName, String pClassName) {

        String vComponentName = pPackageName + "." + pClassName;

        final PackageManager packageManager = pContext.getPackageManager();

        final Intent intent = new Intent();
        intent.setComponent(new ComponentName(pPackageName, vComponentName));
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;
    }

    /**
     * Mac 주소 가져오는 메서드
     * @return 해당 기기의 Mac 주소 반환
     */
    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface inf : all) {
                if (!inf.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = inf.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }

        return "";
    }

    public static String transSemicolon(String txt) {
        String transStr = "";
        if (txt.contains(";")) {
            int semicolon_idx = txt.indexOf(";");
            transStr = txt.substring(0, semicolon_idx);
        } else {
            transStr = txt;
        }
        return transStr;
    }

    public static String[] transHyphen(String txt) {
        String[] transStr = null;
        if (txt.contains("-")) {
            transStr = txt.split("-");
        }

        return transStr;
    }

    public static String transHyphen(String txt, int idx) {
        String[] transStr = null;
        transStr = transHyphen(txt);

        return transStr[idx];
    }
}
