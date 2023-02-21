package com.PDA.gmax;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.PDA.gmax.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class ConfigFragment extends Fragment {

    MenuActivity menuActivity;

    View view;

    //== View 선언(TextView) ==//
    private TextView now_ver, new_ver, db_nm;

    //== View 선언(Button) ==//
    private MaterialButton btn_apk_download;

    public ConfigFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        menuActivity = (MenuActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_config, container, false);
        // Inflate the layout for this fragment

        this.initializeView();

        this.initializeListener();

        this.initializeData();

        return view;
    }

    private void initializeView() {
        //== ID 값 바인딩 ==//
        now_ver                 = view.findViewById(R.id.now_ver);
        new_ver                 = view.findViewById(R.id.new_ver);
        db_nm                   = view.findViewById(R.id.db_nm);

        btn_apk_download        = view.findViewById(R.id.btn_apk_download);
    }

    private void initializeListener() {
        View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_apk_download:
                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(getActivity());
                        mAlert.setMessage("업데이트를 진행하시겠습니까?")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ((MainActivity)MainActivity.mContext).update_user_application(getContext());
                                    }
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();
                        break;
                }
            }
        };
        btn_apk_download.setOnClickListener(btnListener);
    }

    private void initializeData() {
        now_ver.setText(String.valueOf(menuActivity.nowVer));
        new_ver.setText(String.valueOf(menuActivity.newVer));

        SELECT_DB_NAME();
    }

    //== WebServiceEM을 통하여 DB 이름 가져오기 ==//
    private void SELECT_DB_NAME() {
        Thread wkThd_SELECT_DB_NAME = new Thread() {
            public void run() {
                String rtn_db_nm = "";
                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();
                rtn_db_nm = dba.SendHttpMessage("SELECT_DB_NAME", pParms);

                db_nm.setText(rtn_db_nm);
            }
        };
        wkThd_SELECT_DB_NAME.start();   //스레드 시작
        try {
            wkThd_SELECT_DB_NAME.join(); //workingThread가 종료될때까지 Main 쓰레드를 정지함
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}