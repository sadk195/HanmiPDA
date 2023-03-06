package com.PDA.Hanmi;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.util.ArrayList;

public class DBAccess {
    public String NAMESPACE;
    public String METHOD;
    public String URL;
    public String ACTION;


    public DBAccess(String pNameSpace, String pUrl) {
        //생성자
        this.NAMESPACE = pNameSpace;
        this.URL = pUrl;
    }

    private boolean run = false;
    public String SendHttpMessage(String pMethod, ArrayList<PropertyInfo> pParams) {
        if(run){
            return "";
        }

        run = true;

        this.METHOD = pMethod;
        this.ACTION = NAMESPACE + pMethod;

        SoapObject request = new SoapObject(NAMESPACE, METHOD); //set up request

        for(int i = 0; i < pParams.size(); i++) {
            PropertyInfo pi = pParams.get(i);
            request.addProperty(pi);
        }

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // put all required data into a soap
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request); // prepare request
        envelope.bodyOut = request;
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 3000);
        Object response= null;
        try
        {
            androidHttpTransport.call(ACTION, envelope);
            response = envelope.getResponse();
        }

        catch (SocketException ex)
        {
            response= "-1" + ex.toString();
        }

        catch (Exception exception)
        {
            response= "-2" + exception.toString();
            System.out.println("exception:"+exception.getMessage());

        }

        run = false;
        return  response.toString();
    }
}
