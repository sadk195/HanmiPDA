package com.example.gmax;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ErrorPopupActivity extends AppCompatActivity {

    public TextView lbl_error_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_popup);

        //== ID값 바인딩 ==//
        lbl_error_msg = findViewById(R.id.lbl_error_msg);

        //== 이전 Intent에서 MSG라고 받은 값을 가져온다 ==//
        String vErrorMSG = getIntent().getStringExtra("MSG");

        //== 가져온 값을 바인딩 ==//
        lbl_error_msg.setText(vErrorMSG);
    }

    public void mOnClose(View v){
        //== 액티비티(팝업) 닫기(앱 종료) ==//
        finishAffinity();
    }
}