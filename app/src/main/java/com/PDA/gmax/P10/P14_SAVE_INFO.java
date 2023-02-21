package com.PDA.gmax.P10;

import java.io.Serializable;

public class P14_SAVE_INFO implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String PRODT_ORDER_NO;          //제조오더
    public  String OPR_NO;                  //공순
    public  String ROUT_ORDER;              //공정구분
    public  String ITEM_CD;                 //픔번
    public  String ITEM_NM;                 //품명
    public  String PRODT_ORDER_UNIT;        //단위
    public  String BASE_UNIT;               //단위
    public  String SPEC;                    //규격
    public  String TRACKING_NO;             //TRACKING_NO
    public  String PRODT_ORDER_QTY;         //지시량
    public  String RCPT_ORDER_QTY;          //오더단위입고량
    public  String RCPT_BASE_QTY;           //기준단위입고량
    public  String PROD_QTY_IN_ORDER_UNIT;  //생산량
    public  String GOOD_QTY;                //양품생산량
    public  String BAD_QTY;                 //불량 생산량
    public  String REMAIN_QTY;              //잔량
    public  String SL_CD;                   //출고창고
    public  String SL_NM;                   //출고 창고 명
    public  String WC_CD;                   //작업장 코드
    public  String WC_NM;                   //작업장 명
    public  String WC_MGR;                  //작업장 그룹


    public String getPRODT_ORDER_NO() {
        return PRODT_ORDER_NO;
    }

    public void setPRODT_ORDER_NO(String prodt_order_no) {
        PRODT_ORDER_NO = prodt_order_no;
    }

    public String getOPR_NO() {
        return OPR_NO;
    }

    public void setOPR_NO(String opr_no) {
        OPR_NO = opr_no;
    }

    public String getROUT_ORDER() {
        return ROUT_ORDER;
    }

    public void setROUT_ORDER(String rout_order) {
        ROUT_ORDER = rout_order;
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

    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm; }

    public String getPRODT_ORDER_UNIT() {
        return PRODT_ORDER_UNIT;
    }

    public void setPRODT_ORDER_UNIT(String prodt_order_unit) { PRODT_ORDER_UNIT = prodt_order_unit; }

    public String getSPEC() {
        return SPEC;
    }

    public void setSPEC(String spec) {
        BASE_UNIT = spec;
    }

    public String getTRACKING_NO() {
        return TRACKING_NO;
    }

    public void setTRACKING_NO(String tracking_no) {
        TRACKING_NO = tracking_no;
    }

    public String getPRODT_ORDER_QTY() {
        return PRODT_ORDER_QTY;
    }

    public void setPRODT_ORDER_QTY(String prodt_order_qty) {
        BASE_UNIT = prodt_order_qty;
    }

    public String getRCPT_ORDER_QTY() {
        return RCPT_ORDER_QTY;
    }

    public void setRCPT_ORDER_QTY(String rcpt_order_qty) {
        RCPT_ORDER_QTY = rcpt_order_qty;
    }


    public String getRCPT_BASE_QTY() {
        return RCPT_BASE_QTY;
    }

    public void setRCPT_BASE_QTY(String rcpt_base_qty) { RCPT_BASE_QTY = rcpt_base_qty; }


    public String getGOOD_QTY() { return GOOD_QTY; }

    public void setGOOD_QTY(String good_qty) { GOOD_QTY = good_qty; }

    public String getBAD_QTY() { return BAD_QTY; }

    public void setBAD_QTY(String BAD_QTY) { PROD_QTY_IN_ORDER_UNIT = BAD_QTY; }

    public String getREMAIN_QTY() { return REMAIN_QTY; }

    public void setREMAIN_QTY(String remain_qty) { REMAIN_QTY = remain_qty; }

    public String getSL_CD() { return SL_CD; }

    public void setSL_CD(String sl_cd) { SL_CD = sl_cd; }

    public String getSL_NM() { return SL_NM; }

    public void setSL_NM(String sl_nm) { SL_NM = sl_nm; }

    public String getWC_NM() { return WC_NM; }

    public void setWC_NM(String WC_NM) { WC_CD = WC_NM; }

    public String getWC_MGR() { return WC_MGR; }

    public void setWC_MGR(String WC_MGR) { WC_CD = WC_MGR; }

}
