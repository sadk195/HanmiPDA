package com.example.gmax;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ErrorList_Popup extends Dialog {
    private TextView txt_contents,txt_name;
    private Button shutdownClick;

    public ErrorList_Popup(@NonNull Context context,String name, String contents) {
        super(context);
        setContentView(R.layout.popup_errorlist);

        txt_name = findViewById(R.id.txt_contents);
        txt_name.setText(name);

        txt_contents = findViewById(R.id.txt_contents);
        txt_contents.setText(contents);
        shutdownClick = findViewById(R.id.btn_shutdown);
        shutdownClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}