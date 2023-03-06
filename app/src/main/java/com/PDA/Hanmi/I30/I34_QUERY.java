package com.PDA.Hanmi.I30;

import java.io.Serializable;

public class I34_QUERY implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String PRODT_ORDER_NO;
    public  String DOCUMENT_DT;
    public  String ITEM_CD;
    public  String ITEM_NM;
    public  String QTY;
    public  String TRACKING_NO;
    public  String SL_NM;
    public  String WC_NM;

    public String getPRODT_ORDER_NO() { return PRODT_ORDER_NO; }
    public void setPRODT_ORDER_NO(String prodt_order_no) { PRODT_ORDER_NO = prodt_order_no; }

    public String getDOCUMENT_DT() { return DOCUMENT_DT; }
    public void setDOCUMENT_DT(String document_dt) { DOCUMENT_DT = document_dt; }

    public String getITEM_CD() { return ITEM_CD; }
    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd; }

    public String getITEM_NM() { return ITEM_NM; }
    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm; }

    public String getQTY() { return QTY; }
    public void setQTY(String qty) { QTY = qty; }

    public String getTRACKING_NO() { return TRACKING_NO; }
    public void setTRACKING_NO(String tracking_no) { TRACKING_NO = tracking_no; }

    public String getSL_NM() { return SL_NM; }
    public void setSL_NM(String sl_nm) { SL_NM = sl_nm; }

    public String getWC_NM() { return WC_NM; }
    public void setWC_NM(String wc_nm) { WC_NM = wc_nm; }


}
