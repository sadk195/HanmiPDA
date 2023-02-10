package com.example.gmax.S10;

import java.io.Serializable;

public class S12_CUSTOM implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

     /*
     * SHIP_TO_PARTY_NM
     * MOV_TYPE_NM
     * SO_TYPE_NM
     * PLANT_NM
     * DN_NO
     * */

    public String LOT_NO;
    public boolean CHK = false;


    public String getLOT_NO() { return LOT_NO;}

    public void setLOT_NO(String lot_no) { LOT_NO = lot_no; }

    public boolean getCHK() { return CHK;}

    public void setCHK(boolean chk) { CHK = chk; }


}
