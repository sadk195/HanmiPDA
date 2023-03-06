package com.PDA.Hanmi.I70;

import java.io.Serializable;

public class I71_ARRAYLIST implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    //public  String CHK;
    public  String CNT;            //건수
    public  String INV_NO;                 //등록번호
    public  String SL_CD;                 //창고코드
    public  String SL_NM;                 //창고명
    public  String WC_CD;           //등록자코드
    public  String WC_NM;           //등록자명

    /*
    public String getCHK() { return CHK;}
    public void setCHK(String chk) { CHK = chk; }

    */

    public String getCNT() {
        return CNT;
    }
    public void setCNT(String cnt) {
        CNT = cnt;
    }

    public String getINV_NO() {
        return INV_NO;
    }
    public void setINV_NO(String item_cd) {
        INV_NO = item_cd;
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

    public String getWC_CD() {
        return WC_CD;
    }
    public void setWC_CD(String wc_cd) {
        WC_CD = wc_cd;
    }

    public String getWC_NM() {
        return WC_NM;
    }
    public void setWC_NM(String wc_nm) {
        WC_NM = wc_nm;
    }


}
