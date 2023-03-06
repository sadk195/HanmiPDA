package com.PDA.Hanmi.S10;

import java.io.Serializable;

public class S17_Shipment_Status implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String SHIP_TO_PARTY_NM;
    public  String DN_REQ_NO;
    public  String PROMISE_DT;
    public  String WMS_GOOD_ON_HAND_QTY;

    public String getSHIP_TO_PARTY_NM() { return SHIP_TO_PARTY_NM;}
    public void setSHIP_TO_PARTY_NM(String ship_to_party_nm) { SHIP_TO_PARTY_NM = ship_to_party_nm; }

    public String getDN_REQ_NO() { return DN_REQ_NO;}
    public void setDN_REQ_NO(String dn_req_no) { DN_REQ_NO = dn_req_no; }

    public String getPROMISE_DT() { return PROMISE_DT; }
    public void setPROMISE_DT(String promise_dt) { PROMISE_DT = promise_dt; }

    public String getWMS_GOOD_ON_HAND_QTY() { return WMS_GOOD_ON_HAND_QTY; }
    public void setWMS_GOOD_ON_HAND_QTY(String wms_good_on_hand_qty) { WMS_GOOD_ON_HAND_QTY = wms_good_on_hand_qty; }

}
