package com.PDA.gmax.I60;

import java.io.Serializable;

public class I62_POPUP implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String CHK;
    public  String LOCATION;
    public  String ITEM_CD;
    public  String ITEM_NM;
    public  String TRACKING_NO;
    public  String LOT_NO;
    public  String LOT_SUB_NO;
    public  String GOOD_ON_HAND_QTY;
    public  String BASIC_UNIT;
    public  String SL_CD;
    public  String SL_NM;

    public String getCHK() { return CHK;}
    public void setCHK(String chk) { CHK = chk; }

    public String getLOCATION() { return LOCATION;}
    public void setLOCATION(String location) { LOCATION = location; }

    public String getITEM_CD() { return ITEM_CD; }
    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd; }

    public String getITEM_NM() { return ITEM_NM; }
    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm; }

    public String getTRACKING_NO() { return TRACKING_NO; }
    public void setTRACKING_NO(String tracking_no) { TRACKING_NO = tracking_no; }

    public String getLOT_NO() { return LOT_NO; }
    public void setLOT_NO(String lot_no) { LOT_NO = lot_no; }

    public String getLOT_SUB_NO() { return LOT_SUB_NO; }
    public void setLOT_SUB_NO(String lot_sub_no) { LOT_SUB_NO = lot_sub_no; }

    public String getGOOD_ON_HAND_QTY() { return GOOD_ON_HAND_QTY; }
    public void setGOOD_ON_HAND_QTY(String good_on_hand_qty) { GOOD_ON_HAND_QTY = good_on_hand_qty; }

    public String getBASIC_UNIT() { return BASIC_UNIT; }
    public void setBASIC_UNIT(String basic_unit) { BASIC_UNIT = basic_unit; }

    public String getSL_CD() { return SL_CD; }
    public void setSL_CD(String sl_cd) { SL_CD = sl_cd; }

    public String getSL_NM() { return SL_NM; }
    public void setSL_NM(String sl_nm) { SL_NM = sl_nm; }
}
