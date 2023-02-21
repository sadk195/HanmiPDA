package com.PDA.gmax.M10;

import java.io.Serializable;

public class M12_QUERY implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public String INSPECT_REQ_NO;
    public String MVMT_RCPT_DT;
    public String INSPECT_GOOD_QTY;
    public String INSPECT_BAD_QTY;
    public String BP_NM;

    public String getINSPECT_REQ_NO() {
        return INSPECT_REQ_NO;
    }

    public void setINSPECT_REQ_NO(String inspect_req_no) {
        INSPECT_REQ_NO = inspect_req_no;
    }

    public String getMVMT_RCPT_DT() {
        return MVMT_RCPT_DT;
    }

    public void setMVMT_RCPT_DT(String mvmt_rcpt_dt) {
        MVMT_RCPT_DT = mvmt_rcpt_dt;
    }

    public String getINSPECT_GOOD_QTY() {
        return INSPECT_GOOD_QTY;
    }

    public void setINSPECT_GOOD_QTY(String inspect_good_qty) {
        INSPECT_GOOD_QTY = inspect_good_qty;
    }

    public String getINSPECT_BAD_QTY() {
        return INSPECT_BAD_QTY;
    }

    public void setINSPECT_BAD_QTY(String inspect_bad_qty) {
        INSPECT_BAD_QTY = inspect_bad_qty;
    }

    public String getBP_NM() {
        return BP_NM;
    }

    public void setBP_NM(String bp_nm) {
        BP_NM = bp_nm;
    }

    public M12_QUERY (String pINSPECT_REQ_NO, String pMVMT_RCPT_DT, String pINSPECT_GOOD_QTY, String pINSPECT_BAD_QTY, String pBP_NM) {
        this.INSPECT_REQ_NO = pINSPECT_REQ_NO;
        this.MVMT_RCPT_DT = pMVMT_RCPT_DT;
        this.INSPECT_GOOD_QTY = pINSPECT_GOOD_QTY;
        this.INSPECT_BAD_QTY = pINSPECT_BAD_QTY;
        this.BP_NM = pBP_NM;
    }

}
