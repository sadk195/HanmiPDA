package com.PDA.Hanmi.S10;

import java.io.Serializable;

public class S13_QUERY implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

     /*
     DN_NO
     MOV_TYPE
     MOV_TYPE_NM
     SHIP_TO_PARTY
     BP_NM
     PROMISE_DT
     DLVY_DT
     ACTUAL_GI_DT
     GOODS_MV_NO
     SALES_GRP
     SALES_GRP_NM
     TRANS_METH
     TRANS_METH_NM
     SO_NO
     DN_REQ_NO
     *

     * */
     public String DN_NO;
     public String MOV_TYPE;
     public String MOV_TYPE_NM;
     public String SHIP_TO_PARTY;
     public String BP_NM;
     public String PROMISE_DT;
     public String DLVY_DT;
     public String ACTUAL_GI_DT;
     public String GOODS_MV_NO;
     public String SALES_GRP;
     public String SALES_GRP_NM;
     public String TRANS_METH;
     public String TRANS_METH_NM;
     public String SO_NO;
     public String DN_REQ_NO;


     public String getDN_NO(){return DN_NO;}

     public void setDN_NO(String dn_no ) {DN_NO =dn_no; }

     public String getMOV_TYPE(){return MOV_TYPE;}

     public void setMOV_TYPE(String mov_type) {MOV_TYPE = mov_type; }

     public String getMOV_TYPE_NM(){return MOV_TYPE_NM;}

     public void setMOV_TYPE_NM(String mov_type_nm) {MOV_TYPE_NM = mov_type_nm; }

     public String getSHIP_TO_PARTY(){return SHIP_TO_PARTY;}

     public void setSHIP_TO_PARTY(String ship_to_party ) {SHIP_TO_PARTY = ship_to_party; }

     public String getBP_NM(){return BP_NM;}

     public void setBP_NM(String bp_nm ) {BP_NM = bp_nm; }

     public String getPROMISE_DT(){return PROMISE_DT;}

     public void setPROMISE_DT(String promise_dt ) {PROMISE_DT = promise_dt; }

     public String getDLVY_DT(){return DLVY_DT;}

     public void setDLVY_DT(String dlvy_dt ) {DLVY_DT = dlvy_dt; }

     public String getACTUAL_GI_DT(){return ACTUAL_GI_DT;}

     public void setACTUAL_GI_DT(String actual_gi_dt ) {ACTUAL_GI_DT = actual_gi_dt;}

     public String getGOODS_MV_NO(){return GOODS_MV_NO;}

     public void setGOODS_MV_NO(String goods_mv_no ) {GOODS_MV_NO = goods_mv_no; }

     public String getSALES_GRP(){return SALES_GRP;}

     public void setSALES_GRP(String sales_grp ) {SALES_GRP = sales_grp; }
     public String getSALES_GRP_NM(){return SALES_GRP_NM;}

     public void setSALES_GRP_NM(String sales_grp_nm ) {SALES_GRP_NM = sales_grp_nm; }
     public String getTRANS_METH(){return TRANS_METH;}

     public void setTRANS_METH(String trans_meth ) {TRANS_METH = trans_meth; }

     public String getTRANS_METH_NM(){return TRANS_METH_NM;}

     public void setTRANS_METH_NM(String trans_meth_nm ) {TRANS_METH_NM = trans_meth_nm;}

     public String getSO_NO(){return SO_NO;}

     public void setSO_NO(String so_no ) {SO_NO = so_no;}

     public String getDN_REQ_NO(){return DN_REQ_NO;}

     public void setDN_REQ_NO(String dn_req_no ) {DN_REQ_NO = dn_req_no; }

}
