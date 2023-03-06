package com.PDA.Hanmi.I20;

import java.io.Serializable;

public class I26_HDR implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    //== HDR ==//
    public String MV_REQ_SEQ;               //== 출고요청순번
    public String ITEM_CD;                  //== 품목코드
    public String ITEM_NM;                  //== 품목코드명
    public String MV_REQ_QTY;               //== 출고이동요청수량
    public String MV_REQ_UNIT;              //== 출고이동요청단위
    public String DLVY_DT;                  //== 납기일
    public String TRACKING_NO;              //== TRACKING_NO

    public String ISSUE_SL_CD;              //== 출고창고코드
    public String TRNS_TYPE;                //== 출고유형구분
    public String MOV_TYPE;                 //== 수불유형구분
    public String MV_REQ_SL_CD;             //== 출고요청창고코드
    public String MV_REQ_PLANT_CD;          //== 출고요청공장코드

    public String LOT_NO;                   //== LOT_NO
    public String LOT_SUB_NO;               //== LOT_SUB_NO
    public String ISSUE_LOCATION;           //== 출고적치장코드
    public String ISSUE_GOOD_ONHAND_QTY;    //== 출고양품수량
    public String ISSUE_BAD_ONHAND_QTY;     //== 출고불량품수량

    public String REQ_QTY;                  //== 출고이동요청잔여수량

    public String REQ_PERSON_NM;            //== 출고요청자
    public String ISSUE_QTY;                //== 출고수량
    public String MV_REQ_ONHAND_QTY;        //== 현재고(요청창고)

    //== 이동한 정보 ==//
    public String TRNS_TRACKING_NO;         //== 이동한TRACKING NO
    public String TRNS_LOT_NO;              //== 이동한LOT NO
    public String TRNS_LOT_SUB_NO;          //== 이동한LOT SUB NO
    public String TRNS_ITEM_CD;             //== 이동한품목코드

    //납기일 넣어야됨

    public String getMV_REQ_SEQ() { return MV_REQ_SEQ; }
    public void setMV_REQ_SEQ(String mv_req_seq) { MV_REQ_SEQ = mv_req_seq; }

    public String getISSUE_SL_CD() { return ISSUE_SL_CD; }
    public void setISSUE_SL_CD(String issue_sl_cd) { ISSUE_SL_CD = issue_sl_cd; }

    public String getISSUE_LOCATION() { return ISSUE_LOCATION; }
    public void setISSUE_LOCATION(String issue_location) { ISSUE_LOCATION = issue_location; }

    public String getREQ_PERSON_NM() { return REQ_PERSON_NM; }
    public void setREQ_PERSON_NM(String req_person_nm) { REQ_PERSON_NM = req_person_nm; }

    public String getTRNS_TYPE() { return TRNS_TYPE; }
    public void setTRNS_TYPE(String trns_type) { TRNS_TYPE = trns_type; }

    public String getMOV_TYPE() { return MOV_TYPE; }
    public void setMOV_TYPE(String mov_type) { MOV_TYPE = mov_type; }

    public String getMV_REQ_SL_CD() { return MV_REQ_SL_CD; }
    public void setMV_REQ_SL_CD(String mv_req_sl_cd) { MV_REQ_SL_CD = mv_req_sl_cd; }

    public String getMV_REQ_PLANT_CD() { return MV_REQ_PLANT_CD; }
    public void setMV_REQ_PLANT_CD(String mv_req_plant_cd) { MV_REQ_PLANT_CD = mv_req_plant_cd; }


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

    public String getMV_REQ_QTY() { return MV_REQ_QTY; }
    public void setMV_REQ_QTY(String mv_req_qty) { MV_REQ_QTY = mv_req_qty; }

    public String getREQ_QTY() { return REQ_QTY; }
    public void setREQ_QTY(String req_qty) { REQ_QTY = req_qty; }

    public String getISSUE_QTY() { return ISSUE_QTY; }
    public void setISSUE_QTY(String issued_qty) { ISSUE_QTY = issued_qty; }

    public String getMV_REQ_UNIT() { return MV_REQ_UNIT; }
    public void setMV_REQ_UNIT(String mv_req_unit) { MV_REQ_UNIT = mv_req_unit; }

    public String getMV_REQ_ONHAND_QTY() { return MV_REQ_ONHAND_QTY; }
    public void setMV_REQ_ONHAND_QTY(String mv_req_onhand_qty) { MV_REQ_ONHAND_QTY = mv_req_onhand_qty; }

    public String getDLVY_DT() { return DLVY_DT; }
    public void setDLVY_DT(String dlvy_dt) { DLVY_DT = dlvy_dt; }

    public String getISSUE_GOOD_ONHAND_QTY() { return ISSUE_GOOD_ONHAND_QTY; }
    public void setISSUE_GOOD_ONHAND_QTY(String issue_good_onhand_qty) { ISSUE_GOOD_ONHAND_QTY = issue_good_onhand_qty; }

    public String getISSUE_BAD_ONHAND_QTY() { return ISSUE_BAD_ONHAND_QTY; }
    public void setISSUE_BAD_ONHAND_QTY(String issue_bad_onhand_qty) { ISSUE_BAD_ONHAND_QTY = issue_bad_onhand_qty; }

    public String getTRNS_ITEM_CD() { return TRNS_ITEM_CD; }
    public void setTRNS_ITEM_CD(String trns_item_cd) { TRNS_ITEM_CD = trns_item_cd; }

    public String getTRNS_TRACKING_NO() { return TRNS_TRACKING_NO; }
    public void setTRNS_TRACKING_NO(String trns_tracking_no) { TRNS_TRACKING_NO = trns_tracking_no; }

    public String getTRNS_LOT_NO() { return TRNS_LOT_NO; }
    public void setTRNS_LOT_NO(String trns_lot_no) { TRNS_LOT_NO = trns_lot_no; }

    public String getTRNS_LOT_SUB_NO() { return TRNS_LOT_SUB_NO; }
    public void setTRNS_LOT_SUB_NO(String trns_lot_sub_no) { TRNS_LOT_SUB_NO = trns_lot_sub_no; }

}
