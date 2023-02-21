package com.PDA.gmax.I60;

public class I62_Popup_ListItem {
    public String CHK;
    private String item_cd;
    private String item_nm;
    private String tracking_no;
    private String lot_no;
    private String lot_sub_no;
    private String good_on_hand_qty;
    private String location;
    private String basic_unit;
    private String sl_cd;
    private String sl_nm;
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getChk() { return CHK; }

    public void setChk(String chk) { this.CHK = chk; }

    public String getItem_cd() {
        return item_cd;
    }

    public void setItem_cd(String item_cd) { this.item_cd = item_cd; }

    public String getItem_nm() {
        return item_nm;
    }

    public void setItem_nm(String item_nm) {
        this.item_nm = item_nm;
    }

    public String getTracking_no() { return tracking_no; }

    public void setTracking_no(String tracking_no) {
        this.tracking_no = tracking_no;
    }

    public String getLot_no() {
        return lot_no;
    }

    public void setLot_no(String lot_no) {
        this.lot_no = lot_no;
    }

    public String getLot_sub_no() {
        return lot_sub_no;
    }

    public void setLot_sub_no(String lot_sub_no) {
        this.lot_sub_no = lot_sub_no;
    }

    public String getGood_on_hand_qty() {
        return good_on_hand_qty;
    }

    public void setGood_on_hand_qty(String good_on_hand_qty) { this.good_on_hand_qty = good_on_hand_qty; }

    public String getBasic_unit() {
        return basic_unit;
    }

    public void setBasic_unit(String basic_unit) { this.basic_unit = basic_unit; }

    public String getSl_cd() {
        return sl_cd;
    }

    public void setSl_cd(String sl_cd) { this.sl_cd = sl_cd; }

    public String getSl_nm() {
        return sl_nm;
    }

    public void setSl_nm(String sl_nm) { this.sl_nm = sl_nm; }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) { this.location = location; }
}