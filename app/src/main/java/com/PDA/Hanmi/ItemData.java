package com.PDA.Hanmi;


import androidx.annotation.NonNull;

public class ItemData {

    private String CODE;
    private String NAME;
    private String REMARK;
    private String WC_CD;
    private String WC_NM;

    public String getCODE() { return CODE; }
    public void setCODE(String CODE) { this.CODE = CODE; }

    public String getNAME() { return NAME; }
    public void setNAME(String NAME) { this.NAME = NAME; }

    public String getREMARK() { return REMARK; }
    public void setREMARK(String REMARK) { this.REMARK = REMARK; }

    public String getWC_CD() { return WC_CD; }
    public void setWC_CD(String WC_CD) { this.WC_CD = WC_CD; }

    public String getWC_NM() { return WC_NM; }
    public void setWC_NM(String WC_NM) { this.WC_NM = WC_NM; }

    @NonNull
    @Override
    public String toString() {
//        return super.toString();
        return NAME;
    }
}
