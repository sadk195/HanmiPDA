package com.example.gmax;

public class ScanData {

    private  String m_SCANTEXT ="";
    private  String m_RFID="";
    private  String m_VISS="";
    private  String m_SL_CD="";
    private  String m_INSP_REQ_No="";


    public ScanData(String pScanText)
    {
        m_SCANTEXT = pScanText;
        Start();
    }

    public  void Start()
    {
        //1. 문자열 나누기 (VISS 코드와 RFID 태그 값이 동시 스캔 될 가능 성 있음( RFID 스캔 태크 설정 앱에서 시작,종료 문자를 * 로 설정.
        // 1.1 문자열에서 * 의 인덱스를 찾는다. (단, * 의 인덱스가 0이면 무시)

        if(m_SCANTEXT.length() == 0){
            return;
        }

        //스캔 된 텍스트의 개행 문자를 #으로 변경.
        m_SCANTEXT = m_SCANTEXT.replaceAll("\\r\\n|\\r|\\n","#");

        //# 문자 이후 데이터는 모두 버림, 즉, 한줄로 들어온 텍스트만 정상적인 값으로 판단.
            if(m_SCANTEXT.indexOf("#") >= 0) {
            m_SCANTEXT = m_SCANTEXT.substring(0, m_SCANTEXT.indexOf("#"));
        }



            //스캔된 텍스트에서 [-] 를 찾을 시, [*] index 기준으로 문자열을 나눈다.
            String val1 = m_SCANTEXT;
            //String chk = val1.substring(0,1);

            if (val1.substring(0,1).equals("Q") || val1.substring(0,1).equals("q") || val1.substring(0,1).equals("M") || val1.substring(0,1).equals("m"))
            {
                m_INSP_REQ_No = val1;
            }
            else
            {
                m_SL_CD = val1;
            }
    }

    public String getm_SL_CD()
    {
        return  m_SL_CD;
    }

    public String getm_INSP_REQ_No()
    {
        return  m_INSP_REQ_No;
    }


}
