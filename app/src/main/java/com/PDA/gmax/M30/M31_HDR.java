package com.PDA.gmax.M30;

import java.io.Serializable;

public class M31_HDR implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String DLV_NO;
    public  String SER_NO;
    public  String ITEM_CD;
    public  String ITEM_NM;
    public  String DLV_QTY;
    public  String CONFIRM_DLV_QTY;
    public  String INSPECT_FLG;
    public  String PUR_TYPE;
    public  String SPEC;

    public String getDLV_NO() { return DLV_NO;}
    public void setDLV_NO(String dlv_no) { DLV_NO = dlv_no; }

    public String getSER_NO() { return SER_NO;}
    public void setSER_NO(String ser_no) { SER_NO = ser_no; }

    public String getITEM_CD() { return ITEM_CD; }
    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd; }

    public String getITEM_NM() { return ITEM_NM; }
    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm; }

    public String getDLV_QTY() { return DLV_QTY; }
    public void setDLV_QTY(String dlv_qty) { DLV_QTY = dlv_qty; }

    public String getCONFIRM_DLV_QTY() { return CONFIRM_DLV_QTY; }
    public void setCONFIRM_DLV_QTY(String confirm_dlv_qty) { CONFIRM_DLV_QTY = confirm_dlv_qty; }

    public String getINSPECT_FLG() { return INSPECT_FLG; }
    public void setINSPECT_FLG(String inspect_flg) { INSPECT_FLG = inspect_flg; }

    public String getPUR_TYPE() { return PUR_TYPE; }
    public void setPUR_TYPE(String pur_type) { PUR_TYPE = pur_type; }

    public String getSPEC() { return SPEC; }
    public void setSPEC(String spec) { SPEC = spec; }



}
