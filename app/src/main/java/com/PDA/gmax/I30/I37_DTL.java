package com.PDA.gmax.I30;

import java.io.Serializable;

public class I37_DTL implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String ITEM_CD;
    public  String ITEM_NM;
    public  String GOOD_QTY;
    public  String SL_CD;
    public  String SL_NM;
    public  String TRACKING_NO;
    //public  String GOOD_ON_HAND_QTY;
    public  String WMS_QTY;


    public String getITEM_CD() { return ITEM_CD; }
    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd; }

    public String getITEM_NM() { return ITEM_NM; }
    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm; }

    public String getGOOD_QTY() { return GOOD_QTY; }
    public void setGOOD_QTY(String good_qty) { GOOD_QTY = good_qty; }

    public String getSL_CD() { return SL_CD; }
    public void setSL_CD(String sl_cd) { SL_CD = sl_cd; }

    public String getSL_NM() { return SL_NM; }
    public void setSL_NM(String sl_nm) { SL_NM = sl_nm; }

    public String getTRACKING_NO() { return TRACKING_NO; }
    public void setTRACKING_NO(String tracking_no) { TRACKING_NO = tracking_no; }

    /*
    public String getGOOD_ON_HAND_QTY() { return GOOD_ON_HAND_QTY; }
    public void setGOOD_ON_HAND_QTY(String good_on_hand_qty) { GOOD_ON_HAND_QTY = good_on_hand_qty; }
     */

    public String getWMS_QTY() { return WMS_QTY; }
    public void setWMS_QTY(String wms_qty) { WMS_QTY = wms_qty; }


}
