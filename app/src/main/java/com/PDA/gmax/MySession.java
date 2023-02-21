package com.PDA.gmax;

import android.app.Application;

public class MySession extends Application {

    private String mLoginID; //로그인 계정
    private String mUnitCD; //단말기 코드
    private String mUserIP; //접속 IP
    private String mPlantCD; //공장 코드
    private String mUnitType = "APK";

    public String getLoginIDString() { return mLoginID; }
    public void setLoginIDString(String mLoginID) { this.mLoginID = mLoginID; }

    public String getUnitCDString() { return mUnitCD; }
    public void setUnitCDString(String mUnitCD) { this.mUnitCD = mUnitCD; }

    public String getUserIPString() { return mUserIP; }
    public void setUserIPString(String mUserIP) { this.mUserIP = mUserIP; }

    public String getPlantCDString() { return mPlantCD; }
    public void setPlantCDString(String mPlantCD) { this.mPlantCD = mPlantCD; }

    public String getUnitTypeString() { return mUnitType; }
    public void setUnitTypeString(String mUnitType) { this.mUnitType = mUnitType; }
}
