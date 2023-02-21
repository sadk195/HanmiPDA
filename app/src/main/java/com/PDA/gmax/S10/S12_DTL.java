package com.PDA.gmax.S10;

import java.io.Serializable;

public class S12_DTL implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    /*
     * SHIP_TO_PARTY_NM
     * MOV_TYPE_NM
     * SO_TYPE_NM
     * PLANT_NM
     * DN_NO
     * */

    public  String CHK;
    public  String ITEM_CD;
    public  String ITEM_NM;
    public  String SO_UNIT;
    public  String GI_QTY;
    public  String SO_NO;
    public  int SO_SEQ;
    public  String GOOD_ON_HAND_QTY;
    public  String BASIC_UNIT;
    public  String TRACKING_NO;
    public  String LOT_NO;
    public  String LOT_SEQ;
    public  String DN_TYPE;
    public  String SALES_GRP;
    public  String PACKAGING_QTY;
    public  String PACKING_STATUS;

    public String getCHK() { return CHK;}

    public void setCHK(String chk) { CHK = chk; }

    public String getITEM_CD() { return ITEM_CD; }

    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd; }

    public String getITEM_NM() { return ITEM_NM; }

    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm; }

    public String getGI_QTY() { return GI_QTY; }

    public void setGI_QTY(String gi_qty) { GI_QTY = gi_qty; }


    public String getSO_NO() { return SO_NO; }

    public void setSO_NO(String so_no) { SO_NO = so_no; }

    public int getSO_SEQ() { return SO_SEQ; }

    public void setSO_SEQ(int so_seq) { SO_SEQ = so_seq; }

    public String getGOOD_ON_HAND_QTY() { return GOOD_ON_HAND_QTY; }

    public void setGOOD_ON_HAND_QTY(String good_on_hand_qty) { GOOD_ON_HAND_QTY = good_on_hand_qty; }

    public String getPACKAGING_QTY() { return PACKAGING_QTY; }

    public void setPACKAGING_QTY(String packaging_qty) { PACKAGING_QTY = packaging_qty; }

    public String getPACKING_STATUS() { return PACKING_STATUS; }

    public void setPACKING_STATUS(String packing_status) { PACKING_STATUS = packing_status; }
}
