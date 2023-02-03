package com.example.gmax.S10;

import java.io.Serializable;

public class S14_HDR implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public String DN_REQ_NO;
    public String PLANT_CD;
    public String SL_CD;
    public String SL_NM;
    public String ITEM_CD;
    public String ITEM_NM;
    public String REQ_QTY;
    public String GOOD_ON_HAND_QTY;
    public String LOCATION;
    public String TRACKING_NO;
    public String LOCATION_QTY;
    public String REQ_QTY2;
    public String DN_GI_QTY;

    public String getDN_REQ_NO() {
        return DN_REQ_NO;
    }

    public void setDN_REQ_NO(String dn_req_no) {
        DN_REQ_NO = dn_req_no;
    }

    public String getPLANT_CD() {
        return PLANT_CD;
    }

    public void setPLANT_CD(String plant_cd) {
        PLANT_CD = plant_cd;
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
        SL_NM = sl_nm;
    }

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

    public String getREQ_QTY() {
        return REQ_QTY;
    }

    public void setREQ_QTY(String req_qty) {
        REQ_QTY = req_qty;
    }

    public String getGOOD_ON_HAND_QTY() {
        return GOOD_ON_HAND_QTY;
    }

    public void setGOOD_ON_HAND_QTY(String good_on_hand_qty) {
        GOOD_ON_HAND_QTY = good_on_hand_qty;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String location) {
        LOCATION = location;
    }

    public String getTRACKING_NO() {
        return TRACKING_NO;
    }

    public void setTRACKING_NO(String tracking_no) {
        TRACKING_NO = tracking_no;
    }

    public String getLOCATION_QTY() {
        return LOCATION_QTY;
    }

    public void setLOCATION_QTY(String location_qty) {
        LOCATION_QTY = location_qty;
    }

    public String getREQ_QTY2() {
        return REQ_QTY2;
    }

    public void setREQ_QTY2(String req_qty2) {
        REQ_QTY2 = req_qty2;
    }

    public String getDN_GI_QTY() {
        return DN_GI_QTY;
    }

    public void setDN_GI_QTY(String dn_gi_qty) {
        DN_GI_QTY = dn_gi_qty;
    }
}
