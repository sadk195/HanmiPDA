package com.PDA.Hanmi.S10;

import java.io.Serializable;

public class S17_HDR implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String ITEM_CD;
    public  String ITEM_NM;
    public  String WMS_QTY;
    public String GOOD_ON_HAND_QTY;
    public  String REQ_QTY;

    public String getITEM_CD() { return ITEM_CD; }

    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd; }

    public String getITEM_NM() { return ITEM_NM; }

    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm; }

    public String getWMS_QTY() { return WMS_QTY;}

    public void setWMS_QTY(String wms_qty) { WMS_QTY = wms_qty; }

    public String getGOOD_ON_HAND_QTY() { return GOOD_ON_HAND_QTY; }

    public void setGOOD_ON_HAND_QTY(String good_on_hand_qty) { GOOD_ON_HAND_QTY = good_on_hand_qty; }

    public String getREQ_QTY() { return REQ_QTY; }

    public void setREQ_QTY(String req_qty) { REQ_QTY = req_qty; }


}
