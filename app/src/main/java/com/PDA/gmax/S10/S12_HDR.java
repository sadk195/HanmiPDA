package com.PDA.gmax.S10;

import java.io.Serializable;

public class S12_HDR implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

     /*
     * SHIP_TO_PARTY_NM
     * MOV_TYPE_NM
     * SO_TYPE_NM
     * PLANT_NM
     * DN_NO
     * */

    public String SHIP_TO_PARTY_NM;
    public String DN_NO;
    public String DE_COUNT;
    public String SHIP_TO_PARTY;
    public String MOV_TYPE;
    public String SO_TYPE;
    public String PLANT_CD;
    public String SL_CD;
    public String TRANS_METH;
    public String START_DT;
    public String END_DT;
    public String DN_REQ_NO;
    public String PROMISE_DT;

    public String getSHIP_TO_PARTY_NM() { return SHIP_TO_PARTY_NM;}

    public void setSHIP_TO_PARTY_NM(String ship_to_party_nm) { SHIP_TO_PARTY_NM = ship_to_party_nm; }

    public String getDN_NO() { return DN_NO; }

    public void setDN_NO(String dn_no) { DN_NO = dn_no; }

    public String getDE_COUNT() { return DE_COUNT; }

    public void setDE_COUNT(String de_count) { DE_COUNT = de_count; }


        //visible = false
    public String getSHIP_TO_PARTY() { return SHIP_TO_PARTY; }

    public void setSHIP_TO_PARTY(String ship_to_party) { SHIP_TO_PARTY = ship_to_party; }

    public String getMOV_TYPE() { return MOV_TYPE; }

    public void setMOV_TYPE(String mov_type) { MOV_TYPE = mov_type; }

    public String getSO_TYPE() { return SO_TYPE; }

    public void setSO_TYPE(String so_type) { SO_TYPE = so_type; }

    public String getPLANT_CD() { return PLANT_CD; }

    public void setPLANT_CD(String plant_cd) { PLANT_CD = plant_cd; }

    public String getSL_CD() { return SL_CD; }

    public void setSL_CD(String sl_cd) { SL_CD = sl_cd; }

    public String getTRANS_METH() { return TRANS_METH; }

    public void setTRANS_METH(String trans_meth) { TRANS_METH = trans_meth; }


    public String getSTART_DT() { return START_DT; }

    public void setSTART_DT(String start_dt) { START_DT = start_dt; }

    public String getEND_DT() { return END_DT; }

    public void setEND_DT(String end_dt) { END_DT = end_dt; }

    public String getDN_REQ_NO() { return DN_REQ_NO; }

    public void setDN_REQ_NO(String dn_req_no) { DN_REQ_NO = dn_req_no; }

    public String getPROMISE_DT() { return PROMISE_DT; }

    public void setPROMISE_DT(String promise_dt) { PROMISE_DT = promise_dt; }

}
