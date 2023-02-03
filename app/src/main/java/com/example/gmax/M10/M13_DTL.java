package com.example.gmax.M10;

import java.io.Serializable;

public class M13_DTL implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

     /*
     * CHECKFLAG
     * SER_NO                   //순번
     * ITEM_CD                  //품번
     * ITEM_NM                  //품명
     * SPEC                      //규격
     * DLV_QTY                  //수량
     * CONFIRM_DLV_QTY           //확인수량
     * INSPECT_FLG               //검사여부
     * SL_NM
     * LOT_NO                    //LOT NO
     * PUR_TYPE
     * PUR_TYPE_CD
     * PLANT_CD                   //공장
     * DLV_NO                     //거래명세서
     * SL_CD                      //창고코드
     * PO_NO                      //발주번호
     * PO_SEQ_NO                  //발주순번
     * PROCUR_TYPE
     * BP_CD
     * MVMT_QTY                    //입하수량
     * PRODT_ORDER_NO
     * OPR_NO
     * TRACKING_NO
     * */

    //public String CHECKFLAG;
    private String SER_NO;
    private String ITEM_CD;
    private String ITEM_NM;
    private String SPEC;
    private String DLV_QTY;
    private String CONFIRM_DLV_QTY;
    private String INSPECT_FLG ;
    private String SL_NM ;
    private String LOT_NO;
    private String SUB_SEQ_NO;

    private String PUR_TYPE ;
    private String PUR_TYPE_CD;
    private String PLANT_CD;
    private String DLV_NO;
    private String SL_CD;
    private String PO_NO;
    private String PO_SEQ_NO;
    private String PROCUR_TYPE;
    private String BP_CD ;
    //public String MVMT_QTY;
    private String PRODT_ORDER_NO;
    private String OPR_NO;
    private String TRACKING_NO;
    private boolean CHK;
    private String CHK_QTY;
    private int IDX;
    private String END_CUST_NM;



    public String getITEM_CD(){return ITEM_CD;}

    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd;}

    public String getITEM_NM(){return ITEM_NM;}

    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm;}

    public String getDLV_QTY(){return DLV_QTY;}

    public void setDLV_QTY(String dlv_qty) { DLV_QTY = dlv_qty;}

    public String getCHK_QTY(){return CHK_QTY;}

    public void setCHK_QTY(String chk_qty) { CHK_QTY = chk_qty;}

    public boolean getCHK() {return CHK;}

    public void setCHK(boolean chk) { CHK = chk;}

    public int getIDX() {return IDX;}

    public void setIDX(int idx) { IDX = idx;}

    //visible = false

   /* public String getCHECKFLAG(){return CHECKFLAG;}

    public void setCHECKFLAG(String checkflag) { CHECKFLAG = checkflag;}*/

    public String getSER_NO(){return SER_NO;}

    public void setSER_NO(String ser_no) { SER_NO = ser_no;}

    public String getSPEC(){return SPEC;}

    public void setSPEC(String spec) { SPEC = spec;}

    public String getCONFIRM_DLV_QTY(){return CONFIRM_DLV_QTY;}

    public void setCONFIRM_DLV_QTY(String confirm_dlv_qty) { CONFIRM_DLV_QTY = confirm_dlv_qty;}

    public String getINSPECT_FLG(){return INSPECT_FLG;}

    public void setINSPECT_FLG(String inspect_flg) { INSPECT_FLG = inspect_flg;}

    public String getSL_NM(){return SL_NM ;}

    public void setSL_NM(String sl_nm) { SL_NM  = SL_NM;}

    public String getLOT_NO(){return LOT_NO;}

    public void setLOT_NO(String lot_no) { LOT_NO = lot_no;}

    public String getSUB_SEQ_NO(){return SUB_SEQ_NO;}

    public void setSUB_SEQ_NO(String sub_seq_no) { SUB_SEQ_NO = sub_seq_no;}

    public String getPUR_TYPE (){return PUR_TYPE;}

    public void setPUR_TYPE (String pur_type) { PUR_TYPE  = pur_type;}

    public String getPUR_TYPE_CD(){return PUR_TYPE_CD;}

    public void setPUR_TYPE_CD(String pur_type_cd) { PUR_TYPE_CD = pur_type_cd;}

    public String getPLANT_CD(){return PLANT_CD;}

    public void setPLANT_CD(String plant_cd) { PLANT_CD = plant_cd;}

    public String getDLV_NO(){return DLV_NO;}

    public void setDLV_NO(String dlv_no) { DLV_NO = dlv_no;}

    public String getSL_CD(){return SL_CD;}

    public void setSL_CD(String sl_cd) { SL_CD = sl_cd;}

    public String getPO_NO(){return PO_NO;}

    public void setPO_NO(String po_no) { PO_NO = po_no;}

    public String getPO_SEQ_NO(){return PO_SEQ_NO;}

    public void setPO_SEQ_NO (String po_seq_no) { PO_SEQ_NO = po_seq_no;}

    public String getPROCUR_TYPE(){return PROCUR_TYPE;}

    public void setPROCUR_TYPE(String procur_type) { PROCUR_TYPE = procur_type;}

    public String getBP_CD (){return BP_CD ;}

    public void setBP_CD (String bp_cd) { BP_CD  = bp_cd;}

  /*  public String getMVMT_QTY(){return MVMT_QTY;}

    public void setMVMT_QTY(String mvmt_qty) { MVMT_QTY = mvmt_qty;}*/

    public String getPRODT_ORDER_NO(){return PRODT_ORDER_NO;}

    public void setPRODT_ORDER_NO(String prodt_order_no) { PRODT_ORDER_NO = prodt_order_no;}

    public String getOPR_NO() {return OPR_NO;}

    public void setOPR_NO(String opr_no) { OPR_NO = opr_no;}

    public String getTRACKING_NO() {return TRACKING_NO;}

    public void setTRACKING_NO(String tracking_no) { TRACKING_NO = tracking_no;}

    public String getEND_CUST_NM(){return END_CUST_NM;}

    public void setEND_CUST_NM(String end_cust_nm) { END_CUST_NM = end_cust_nm;}




}
