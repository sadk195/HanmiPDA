package com.PDA.Hanmi.M40;

import java.io.Serializable;

public class M41_QUERY implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

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
    public String NO;

    public String getCODE(){return CODE;}

    public void setCODE(String code ) { CODE = code; }

    public String getNO(){return NO;}

    public void setNO(String no ) { NO = no; }

}



