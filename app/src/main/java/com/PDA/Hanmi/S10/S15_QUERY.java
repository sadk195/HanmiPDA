package com.PDA.Hanmi.S10;

import java.io.Serializable;

public class S15_QUERY implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String ITEM_CD;
    public  String ITEM_NM;
    public  String QTY;
    public  String UPDATE_LOCATION;

    public String getITEM_CD() { return ITEM_CD; }
    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd; }

    public String getITEM_NM() { return ITEM_NM; }
    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm; }

    public String getQTY() { return QTY; }
    public void setQTY(String qty) { QTY = qty; }

    public String getUPDATE_LOCATION() { return UPDATE_LOCATION; }
    public void setUPDATE_LOCATION(String update_location) { UPDATE_LOCATION = update_location; }


}
