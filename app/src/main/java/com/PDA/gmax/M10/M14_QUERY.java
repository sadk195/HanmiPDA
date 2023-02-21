package com.PDA.gmax.M10;

import java.io.Serializable;

public class M14_QUERY implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

     /*
     * ITEM_CD
     * ITEM_NM
     * SPEC
     * DN_NO
     * SEQ_NO
     * PO_NO

     * */
    public String ITEM_CD;
    public String ITEM_NM;
    public String SPEC;
    public String DN_NO;
    public String SEQ_NO;
    public String PO_NO;

    public String getITEM_CD() {return ITEM_CD;}

    public void setITEM_CD(String item_cd) { ITEM_CD = item_cd;}

    public String getITEM_NM() {return ITEM_NM;}

    public void setITEM_NM(String item_nm ) { ITEM_NM = item_nm;}

    public String getSPEC() {return SPEC;}

    public void setSPEC(String spec ) { SPEC = spec; }

    public String getDN_NO() {return DN_NO;}

    public void setDN_NO(String dn_no) { DN_NO = dn_no; }

    public String getSEQ_NO() {return SEQ_NO;}

    public void setSEQ_NO(String seq_no ) { SEQ_NO = seq_no; }

    public String getPO_NO() {return PO_NO;}

    public void setPO_NO(String po_no ) { PO_NO = po_no; }




}
