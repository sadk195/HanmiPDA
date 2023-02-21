package com.PDA.gmax.S10;

import java.io.Serializable;

public class S11_QUERY implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

     /*
     * SHIP_TO_PARTY_NM
     * MOV_TYPE_NM
     * SO_TYPE_NM
     * PLANT_NM
     * DN_NO
     * */

    public String DN_REQ_NO;
    public String INSRT_DT;
    public String SHIP_TO_PARTY;
    public String BP_NM;
    public String SALES_GRP;
    public String SALES_GRP_NM;
    public String PROMISE_DT;
    public String ITEM_CNT;
    public String DLVY_DT;

    public String getDN_REQ_NO() { return DN_REQ_NO;}

    public void setDN_REQ_NO(String dn_req_no) { DN_REQ_NO = dn_req_no; }

    public String getINSRT_DT() { return INSRT_DT;}

    public void setINSRT_DT(String insrt_dt) { INSRT_DT = insrt_dt; }

    public String getSHIP_TO_PARTY() { return SHIP_TO_PARTY; }

    public void setSHIP_TO_PARTY(String ship_to_party) { SHIP_TO_PARTY = ship_to_party; }

    public String getBP_NM() { return BP_NM; }

    public void setBP_NM(String bp_nm) { BP_NM = bp_nm; }


        //visible = false
    public String getSALES_GRP() { return SALES_GRP; }

    public void setSALES_GRP(String sales_grp) { SALES_GRP = sales_grp; }

    public String getSALES_GRP_NM() { return SALES_GRP_NM; }

    public void setSALES_GRP_NM(String sales_grp_nm) { SALES_GRP_NM = sales_grp_nm; }

    public String getPROMISE_DT() { return PROMISE_DT; }

    public void setPROMISE_DT(String promise_dt) { PROMISE_DT = promise_dt; }

    public String getITEM_CNT() { return ITEM_CNT; }

    public void setITEM_CNT(String item_cnt) { ITEM_CNT = item_cnt; }

    public String getDLVY_DT() { return DLVY_DT; }

    public void setDLVY_DT(String dlvy_dt) { DLVY_DT = dlvy_dt; }


}
