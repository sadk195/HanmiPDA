package com.PDA.gmax;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.PDA.gmax.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Menu2Fragment extends Fragment {

    private MenuActivity menuActivity;
    private Context con;
    private RecyclerView rv_menu;
    private ArrayList<JSONObject> menuList;
    public MenuAdapter menuAdapter;
    private LinearLayoutManager llm;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        menuActivity = (MenuActivity) getActivity();
        con = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu2, container, false);

        rv_menu = v.findViewById(R.id.rv_main);
        llm = new LinearLayoutManager(con);

        if (!menuActivity.sJson.equals("[]")) {
            try {
                JSONArray ja = new JSONArray(menuActivity.sJson);

                menuList = new ArrayList<>();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObj = ja.getJSONObject(idx);

                    menuList.add(jObj);
                }

                menuAdapter = new MenuAdapter(menuList);
                rv_menu.setLayoutManager(llm);
                rv_menu.setAdapter(menuAdapter);
            } catch (JSONException exJson) {
                TGSClass.AlertMessage(con, "JSON ERROR");
            }
        }

        menuAdapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, JSONObject menuName) {
                itentMenu(position, menuName);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    private void itentMenu(int position, JSONObject menuName) {
        try {
            final String vMenuID = menuName.getString("MENU_ID");
            final String vMenuName = menuName.getString("MENU_NM");
            final String vMenuRemark = menuName.getString("REMARK");
            final String vStartCommand = menuName.getString("START_COMMAND");
            final int vRowNum = menuName.getInt("ROW_NUM");

            String vAcitivtyName = vMenuID + "." + vMenuID + "_Activity";

            if (menuActivity.start_grant(vMenuID, vMenuName) == true) {
                //ACTIVITY 존재여부 확인.
                boolean bChk = TGSClass.isIntentAvailable(con, con.getPackageName(), vAcitivtyName);

                if (bChk) {
                    Intent intent = TGSClass.ChangeView(con.getPackageName(), vAcitivtyName);

                    intent.putExtra("MENU_ID", vMenuID);
                    intent.putExtra("MENU_NM", vMenuName);
                    intent.putExtra("MENU_REMARK", vMenuRemark);
                    intent.putExtra("START_COMMAND", vStartCommand);
                    startActivityForResult(intent, vRowNum);
                } else {
                    TGSClass.AlertMessage(con, vMenuName + "가 존재하지 않습니다.");
                }
            }
        } catch (JSONException exJson) {
            return;
        }
    }
}