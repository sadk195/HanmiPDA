package com.PDA.Hanmi.S10;

import java.io.Serializable;

public class S12_PKG implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

     /*
     * SHIP_TO_PARTY_NM
     * MOV_TYPE_NM
     * SO_TYPE_NM
     * PLANT_NM
     * DN_NO
     * */

    public String ITEM_CD;
    public String ITEM_NM;
    public String REQ_STOCK;
    public String REQ_QTY;
    public String PACKING_CNT;
    public String QTY;
    public String CARTON_NO;

    public String getITEM_CD() { return ITEM_CD;}

    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd; }

    public String getITEM_NM() { return ITEM_NM; }

    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm; }

    public String getREQ_STOCK() { return REQ_STOCK; }

    public void setREQ_STOCK(String req_stock) { REQ_STOCK = req_stock; }

    public String getREQ_QTY() { return REQ_QTY; }

    public void setREQ_QTY(String req_qty) { REQ_QTY = req_qty; }

    public String getPACKING_CNT() { return PACKING_CNT; }

    public void setPACKING_CNT(String packing_cnt) { PACKING_CNT = packing_cnt; }

    public String getQTY() { return QTY; }

    public void setQTY(String qty) { QTY = qty; }

    public String getCARTON_NO() { return CARTON_NO; }

    public void setCARTON_NO(String qty) { CARTON_NO = qty; }




    //visible = false

}
