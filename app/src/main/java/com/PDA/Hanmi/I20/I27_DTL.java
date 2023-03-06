package com.PDA.Hanmi.I20;

import java.io.Serializable;

public class I27_DTL implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public String ITEM_CD;
    public String ITEM_NM;
    public String SL_CD;
    public String SL_NM;
    public String GOOD_ON_HAND_QTY;
    public String BAD_ON_HAND_QTY;
    public String TRACKING_NO;

    public String LOT_NO;
    public String LOT_SUB_NO;
    public String BASIC_UNIT;
    public String LOCATION;

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

    public String getSL_CD() {
        return SL_CD;
    }

    public void setSL_CD(String sl_cd) {
        SL_CD = sl_cd;
    }

    public String getSL_NM() {
        return SL_NM;
    }

    public void setSL_NM(String sl_nm) {
        SL_CD = sl_nm;
    }

    public String getGOOD_ON_HAND_QTY() {
        return GOOD_ON_HAND_QTY;
    }

    public void setGOOD_ON_HAND_QTY(String good_on_hand_qty) {
        GOOD_ON_HAND_QTY = good_on_hand_qty;
    }

    public String getBAD_ON_HAND_QTY() {
        return BAD_ON_HAND_QTY;
    }

    public void setBAD_ON_HAND_QTY(String bad_on_hand_qty) {
        BAD_ON_HAND_QTY = bad_on_hand_qty;
    }

    public String getTRACKING_NO() {
        return TRACKING_NO;
    }

    public void setTRACKING_NO(String tracking_no) {
        TRACKING_NO = tracking_no;
    }

    public String getLOT_NO() {
        return LOT_NO;
    }

    public void setLOT_NO(String lot_no) {
        LOT_NO = lot_no;
    }

    public String getLOT_SUB_NO() {
        return LOT_SUB_NO;
    }

    public void setLOT_SUB_NO(String lot_sub_no) {
        LOT_SUB_NO = lot_sub_no;
    }

    public String getBASIC_UNIT() {
        return BASIC_UNIT;
    }

    public void setBASIC_UNIT(String basic_unit) {
        BASIC_UNIT = basic_unit;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String location) {
        LOCATION = location;
    }

}
