package com.PDA.Hanmi.S10;

import java.io.Serializable;

public class S11_DTL implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

     /*
     * SHIP_TO_PARTY_NM
     * MOV_TYPE_NM
     * SO_TYPE_NM
     * PLANT_NM
     * DN_NO
     * */

    public String ITEM_CD;
    public String ITEM_NM;
    public String SPEC;
    public String REQ_QTY;
    public String GI_QTY;
    public String GOOD_ON_HAND_QTY;
    public String SL_CD;
    public String SL_NM;
    public String DN_REQ_SEQ;

    public String getITEM_CD() { return ITEM_CD;}

    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd; }

    public String getITEM_NM() { return ITEM_NM;}

    public void setITEM_NM(String item_nm) { ITEM_NM = item_nm; }

    public String getSPEC() { return SPEC; }

    public void setSPEC(String spec) { SPEC = spec; }

    public String getREQ_QTY() { return REQ_QTY; }

    public void setREQ_QTY(String req_qty) { REQ_QTY = req_qty; }

    public String getGI_QTY() { return GI_QTY; }

    public void setGI_QTY(String gi_qty) { GI_QTY = gi_qty; }
    //visible = false

    public String getGOOD_ON_HAND_QTY() { return GOOD_ON_HAND_QTY; }

    public void setGOOD_ON_HAND_QTY(String good_on_hand_qty) { GOOD_ON_HAND_QTY = good_on_hand_qty; }

    public String getSL_CD() { return SL_CD; }

    public void setSL_CD(String sl_cd) { SL_CD = sl_cd; }

    public String getSL_NM() { return SL_NM; }

    public void setSL_NM(String sl_nm) { SL_NM = sl_nm; }

    public String getDN_REQ_SEQ() { return DN_REQ_SEQ; }

    public void setDN_REQ_SEQ(String dn_req_seq) { DN_REQ_SEQ = dn_req_seq; }


}
