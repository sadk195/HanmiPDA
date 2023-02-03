package com.example.gmax.M20;

import java.io.Serializable;

public class M22_QUERY implements Serializable {   //Serializable 객체를 파라미터로 다른 ACTIVITY에 전달할때 필요함.

    public  String INSPECT_REQ_NO;
    public  String INSP_QTY;
    public  String G_QTY;
    public  String B_QTY;
    public  String PRODT_ORDER_NO;
    public  String LOCATION;

    public String getINSPECT_REQ_NO() { return INSPECT_REQ_NO; }
    public void setINSPECT_REQ_NO(String inspect_req_no) { INSPECT_REQ_NO = inspect_req_no; }

    public String getINSP_QTY() { return INSP_QTY; }
    public void setINSP_QTY(String insp_qty) { INSP_QTY = insp_qty; }

    public String getG_QTY() { return G_QTY; }
    public void setG_QTY(String g_qty) { G_QTY = g_qty; }

    public String getB_QTY() { return B_QTY; }
    public void setB_QTY(String b_qty) { B_QTY = b_qty; }

    public String getPRODT_ORDER_NO() { return PRODT_ORDER_NO; }
    public void setPRODT_ORDER_NO(String prodt_order_no) { PRODT_ORDER_NO = prodt_order_no; }

    public String getLOCATION() { return LOCATION; }
    public void setLOCATION(String location) { LOCATION = location; }

}
