package com.PDA.Hanmi.S10;

import java.io.Serializable;

public class S12_CUSTOM implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    //  ITEM_CD
	//  LOT_NO
	//  LOT_QTY
	//  GOOD_QTY

    //체크상태 확인
    public boolean CHK = false;


    public String ITEM_CD;
    public String ITEM_NM;

    public String LOT_NO;
    public String LOT_QTY;
    public String GOOD_QTY;

    public String getITEM_CD(){return  ITEM_CD;}

    public void setITEM_CD(String item_cd ) {  ITEM_CD = item_cd; }

    public String getITEM_NM(){return  ITEM_NM;}

    public void setITEM_NM(String item_nm ) {  ITEM_NM = item_nm; }

    public String getLOT_NO(){return  LOT_NO;}

    public void setLOT_NO(String lot_no ) {  LOT_NO = lot_no; }

    public String getLOT_QTY(){return  LOT_QTY;}

    public void setLOT_QTY(String lot_qty) {  LOT_QTY = lot_qty;}

    public String getGOOD_QTY(){return  GOOD_QTY;}

    public void setGOOD_QTY(String good_qty) {  GOOD_QTY = good_qty;}

    public boolean getCHK() { return CHK;}

    public void setCHK(boolean chk) { CHK = chk; }




}
