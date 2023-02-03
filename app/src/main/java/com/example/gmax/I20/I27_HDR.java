package com.example.gmax.I20;

import java.io.Serializable;

public class I27_HDR implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public String ITEM_CD;
    public String ITEM_NM;
    public String SPEC;
    public String LOCATION;
    public String LOCATION_NM;
    public String TRACKING_NO;
    public int SUM_GOOD_ON_HAND_QTY;

    public String getITEM_CD() {
        return ITEM_CD;
    }

    public void setITEM_CD(String item_cd) {
        ITEM_CD = item_cd;
    }

    public String getITEM_NM() {
        return ITEM_NM;
    }

    public void setITEM_NM(String item_nm) {
        ITEM_NM = item_nm;
    }

    public String getSPEC() {
        return SPEC;
    }

    public void setSPEC(String spec) {
        SPEC = spec;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String location) {
        LOCATION = location;
    }

    public String getLOCATION_NM() {
        return LOCATION_NM;
    }

    public void setLOCATION_NM(String location_nm) {
        LOCATION_NM = location_nm;
    }

    public String getTRACKING_NO() {
        return TRACKING_NO;
    }

    public void setTRACKING_NO(String tracking_no) {
        TRACKING_NO = tracking_no;
    }

    public int getSUM_GOOD_ON_HAND_QTY() {
        return SUM_GOOD_ON_HAND_QTY;
    }

    public void setSUM_GOOD_ON_HAND_QTY(int sum_good_on_hand_qty) {
        SUM_GOOD_ON_HAND_QTY = sum_good_on_hand_qty;
    }
}
