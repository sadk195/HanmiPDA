package com.PDA.Hanmi;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.PDA.Hanmi.R;


public class dialog_Loading extends Dialog {

    public dialog_Loading(Context context){
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
    }


}





