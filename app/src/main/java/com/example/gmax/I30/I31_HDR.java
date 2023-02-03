package com.example.gmax.I30;

import java.io.Serializable;

public class I31_HDR implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String PRODT_ORDER_NO;
    public  String OPR_NO;
    public  String ITEM_CD;
    public  String ITEM_NM;
    public  String TRACKING_NO;
    public  String REQ_QTY;
    public  String BASE_UNIT;
    public  String SL_CD;
    public  String SL_NM;
    public  String ISSUED_QTY;
    public  String REMAIN_QTY;
    public  String REQ_NO;
    public  String ISSUE_MTHD;
    public  String RESV_STATUS;
    public  String OUT_QTY;
    public  String SEQ_NO;

    public String getPRODT_ORDER_NO() { return PRODT_ORDER_NO;}
    public void setPRODT_ORDER_NO(String prodt_order_no) { PRODT_ORDER_NO = prodt_order_no; }

    public String getOPR_NO() { return OPR_NO;}
    public void setOPR_NO(String opr_no) { OPR_NO = opr_no; }

    public String getITEM_CD() { return ITEM_CD; }
    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd; }

    public String getITEM_NM() { return ITEM_NM; }
    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm; }

    public String getREQ_QTY() { return REQ_QTY; }
    public void setREQ_QTY(String req_qty) { REQ_QTY = req_qty; }

    public String getBASE_UNIT() { return BASE_UNIT; }
    public void setBASE_UNIT(String base_unit) { BASE_UNIT = base_unit; }

    public String getTRACKING_NO() { return TRACKING_NO; }
    public void setTRACKING_NO(String tracking_no) { TRACKING_NO = tracking_no; }

    public String getSL_CD() { return SL_CD; }
    public void setSL_CD(String sl_cd) { SL_CD = sl_cd; }

    public String getSL_NM() { return SL_NM; }
    public void setSL_NM(String sl_nm) { SL_NM = sl_nm; }

    public String getISSUED_QTY() { return ISSUED_QTY; }
    public void setISSUED_QTY(String issued_qty) { ISSUED_QTY = issued_qty; }

    public String getREMAIN_QTY() { return REMAIN_QTY; }
    public void setREMAIN_QTY(String remain_qty) { REMAIN_QTY = remain_qty; }

    public String getREQ_NO() { return REQ_NO; }
    public void setREQ_NO(String req_no) { REQ_NO = req_no; }

    public String getISSUE_MTHD() { return ISSUE_MTHD; }
    public void setISSUE_MTHD(String issue_mthd) { ISSUE_MTHD = issue_mthd; }

    public String getRESV_STATUS() { return RESV_STATUS; }
    public void setRESV_STATUS(String resv_status) { RESV_STATUS = resv_status; }

    public String getOUT_QTY() { return OUT_QTY; }
    public void setOUT_QTY(String out_qty) { OUT_QTY = out_qty; }

    public String getSEQ_NO() { return SEQ_NO; }
    public void setSEQ_NO(String seq_no) { SEQ_NO = seq_no; }


}
