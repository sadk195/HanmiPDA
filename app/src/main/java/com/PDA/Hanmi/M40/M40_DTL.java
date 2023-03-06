package com.PDA.Hanmi.M40;

import java.io.Serializable;

public class M40_DTL implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

     /*
     * CODE
     * AREA_DENSITY	          //순번
     * LOT_NO	              //품번
     * ROLL_NO                //품명
     * WIDTH		           //규격
     * LENGTH                 //수량
     * QR_VALUE_ALL            //확인수량
     * STATUS                  //검사여부
     * */

    public String CODE;
    public String AREA_DENSITY;
    public String LOT_NO;
    public String ROLL_NO;
    public String WIDTH;
    public String LENGTH;
    public String QR_VALUE_ALL;
    public String STATUS;
    public boolean CHK = false;

    public String getCODE(){return CODE;}

    public void setCODE(String code ) { CODE = code; }

    public String getAREA_DENSITY(){return AREA_DENSITY;}

    public void setAREA_DENSITY(String area_density ) { AREA_DENSITY = area_density; }

    public String getLOT_NO(){return LOT_NO;}

    public void setLOT_NO(String lot_no) { LOT_NO = lot_no; }

    public String getROLL_NO(){return ROLL_NO;}

    public void setROLL_NO(String roll_no) { ROLL_NO = roll_no; }

    public String getWIDTH(){return WIDTH;}

    public void setWIDTH(String width) { WIDTH = width; }

    public String getLENGTH(){return LENGTH;}

    public void setLENGTH(String length) { LENGTH = length;}

    public String getQR_VALUE_ALL(){return QR_VALUE_ALL;}

    public void setQR_VALUE_ALL(String qr_value_all) { QR_VALUE_ALL = qr_value_all; }

    public String getSTATUS(){return STATUS;}

    public void setSTATUS(String status) { STATUS = status; }

    public boolean getCHK() { return CHK;}

    public void setCHK(boolean chk) { CHK = chk; }







}



