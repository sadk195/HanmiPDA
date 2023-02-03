package com.example.gmax.I70;

import java.io.Serializable;

public class I73_ARRAYLIST implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public String SL_NM;            //창고
    public String LOCATION;            //적치장
    public String ITEM_CD;                 //품목코드
    public String ITEM_NM;                 //품목명
    public String QTY;                 //수량
    public String TRACKING_NO;           //TRACKING_NO
    public String CHK;
    public String SL_CD;
    public String INV_NO;
    public String INV_SEQ;

    public String getCHK() { return CHK;}
    public void setCHK(String chk) { CHK = chk; }

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

    public String getLOCATION() {
        return LOCATION;
    }
    public void setLOCATION(String location) {
        LOCATION = location;
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

    public String getQTY() {
        return QTY;
    }
    public void setQTY(String qty) {
        QTY = qty;
    }

    public String getTRACKING_NO() {
        return TRACKING_NO;
    }
    public void setTRACKING_NO(String tracking_no) {
        TRACKING_NO = tracking_no;
    }

    public String getINV_NO() {
        return INV_NO;
    }
    public void setINV_NO(String inv_no) {
        INV_NO = inv_no;
    }

    public String getINV_SEQ() {
        return INV_SEQ;
    }
    public void setINV_SEQ(String inv_seq) {
        INV_SEQ = inv_seq;
    }

}
