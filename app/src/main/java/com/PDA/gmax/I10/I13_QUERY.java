package com.PDA.gmax.I10;

import java.io.Serializable;

public class I13_QUERY implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public String ITEM_CD;
    public String OPT;
    public String GOOD_ON_HAND_QTY;
    public String BAD_ON_HAND_QTY;

    public String getITEM_CD() {
        return ITEM_CD;
    }

    public void setITEM_CD(String item_cd) {
        ITEM_CD = item_cd;
    }

    public String getOPT() {
        return OPT;
    }

    public void setOPT(String opt) {
        OPT = opt;
    }

    public String getGOOD_ON_HAND_QTY() {
        return GOOD_ON_HAND_QTY;
    }

    public void setGOOD_ON_HAND_QTY(String good_on_hand_qty) {
        GOOD_ON_HAND_QTY = good_on_hand_qty;
    }

    public String getBAD_ON_HAND_QTY() {
        return BAD_ON_HAND_QTY;
    }

    public void setBAD_ON_HAND_QTY(String bad_on_hand_qty) {
        BAD_ON_HAND_QTY = bad_on_hand_qty;
    }

}
