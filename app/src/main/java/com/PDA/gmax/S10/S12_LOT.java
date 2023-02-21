package com.PDA.gmax.S10;

import java.io.Serializable;

public class S12_LOT implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

     /*
     * SHIP_TO_PARTY_NM
     * MOV_TYPE_NM
     * SO_TYPE_NM
     * PLANT_NM
     * DN_NO
     * */

    public String ITEM_CD;
    public String ITEM_NM;
    public String PACKING_NO;
    public String DN_REQ_NO;
    public String LOT_NO;
    public String SCAN_QTY;
    public String CONT_NO;
    //품목별 로트 내역이 저장
    //public class LOT{
    //    public String LOT_NO;
    //    public String QTY;
    //}
    //public ArrayList<LOT> LOT_INFO;

    public String getITEM_CD() { return ITEM_CD;}

    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd; }

    public String getITEM_NM() { return ITEM_NM;}

    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm; }

    public String getPACKING_NO() { return PACKING_NO;}

    public void setPACKING_NO(String packing_no) { PACKING_NO = packing_no; }

    public String getLOT_NO() { return LOT_NO;}

    public void setLOT_NO(String lot_no) { LOT_NO = lot_no; }

    public String getSCAN_QTY() { return SCAN_QTY;}

    public void setSCAN_QTY(String scan_qty) { SCAN_QTY = scan_qty; }

    public String getCONT_NO() { return CONT_NO;}

    public void setCONT_NO(String cont_no) { CONT_NO = cont_no; }
    //public ArrayList<LOT> getLOT_INFO() { return LOT_INFO; }

    //public void setLOT_INFO(ArrayList<LOT> lot_info) { LOT_INFO = lot_info; }

    //public void addLOT_INFO(String lot_no,String qty) {
    //    if(LOT_INFO == null){
    //        LOT_INFO = new ArrayList<LOT>();
    //    }
//
    //    LOT add = new LOT();
    //    add.LOT_NO = lot_no;
    //    add.QTY = qty;
//
    //    LOT_INFO.add(add);
    //}


}
