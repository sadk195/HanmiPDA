package com.PDA.gmax;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.PDA.gmax.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuFragment extends Fragment {

    MenuActivity menuActivity;
    Context con;

    //== JSON 선언 ==//
    private String sJson;

    View view;

    //== View 선언(Layout) ==//
    private LinearLayout list;
    private LinearLayout leftMenu, rightMenu;

    CircularProgressIndicator progress_cir;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        menuActivity = (MenuActivity) getActivity();
        con = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);
        // Inflate the layout for this fragment

        this.initializeView();

        setMenu();
//        setMenu2();

        return view;
    }

    private void initializeView() {
        leftMenu = view.findViewById(R.id.layout_left);
        rightMenu = view.findViewById(R.id.layout_right);
    }

    protected boolean setMenu() {
        if (!menuActivity.sJson.equals("[]")) {
            try {
                //배경색
                Drawable bgColor1 = con.getDrawable(R.drawable.bg_prg_menu1);
                Drawable bgColor2 = con.getDrawable(R.drawable.bg_prg_menu2);
                Drawable bgApplyColor = bgColor1;

                JSONArray ja = new JSONArray(menuActivity.sJson);

                int vMenuCnt = ja.length(); // 메뉴 숫자.

                //메뉴가 홀수인 경우 +1하여 짝수처럼하고 우측 아래 공간 빈공간 처리
                float WeightSums = vMenuCnt % 2 == 1 ? (vMenuCnt / 2 +1) *3: (vMenuCnt / 2) * 3;
                leftMenu.setWeightSum(WeightSums);
                rightMenu.setWeightSum(WeightSums);

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    final String vMenuID        = jObject.getString("MENU_ID");
                    final String vMenuName      = jObject.getString("MENU_NM");
                    final String vMenuRemark    = jObject.getString("REMARK");
                    final String vStartCommand  = jObject.getString("START_COMMAND");
                    final int vRowNum           = jObject.getInt("ROW_NUM");

                    //개별 메뉴를 만들 컨트롤
                    //double vWeight = (double) (vAllHeight / (vMenuCnt / 2 ));  //메뉴각하나당 크기 비율 전체 12

                    LinearLayout.LayoutParams menuBox_params
                            = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 3);
                    menuBox_params.setMargins(5, 5, 5, 5);

                    LinearLayout menuBox = new LinearLayout(con);
                    menuBox.setGravity(Gravity.CENTER);
                    menuBox.setLayoutParams(menuBox_params);

                    //메뉴의 텍스트 정보를 보여주는 컨트롤.
                    RelativeLayout.LayoutParams textView_params
                            = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                    params1.addRule(RelativeLayout.ALIGN_LEFT);
//                    params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);

                    TextView menuText = new TextView(con);
                    menuText.setText(vMenuName);
                    menuText.setTextSize(25);
                    menuText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    menuText.setLayoutParams(textView_params);

                    menuBox.addView(menuText);  //메뉴텍스트 뷰를 menuBox 레이아웃에 추가.

                    int id = getResources().getIdentifier(con.getPackageName() + ":drawable/" + vMenuID.toLowerCase(), null, null);

                    if (id != 0) {
                        ImageView imageView = new ImageView(con);

                        //메뉴의 이미지 정보를 보여주는 컨트롤.
                        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        params2.setMargins(20, 20, 20, 20);
                        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

                        params2.height = 400 / (vMenuCnt / 2);
                        params2.width = 400 / (vMenuCnt / 2);

                        //MENU ID 와 일치하는 이미지
                        imageView.setImageResource(id);
                        imageView.setLayoutParams(params2);

                        menuBox.addView(imageView);
                    }

                    menuBox.setOnClickListener(new View.OnClickListener() {    //동적 생성 된 컨트롤의 클릭 이벤트.
                        @Override
                        public void onClick(View v) {

                            //TGSClass.AlertMessage(getApplicationContext(),vMenuID);
                            String vActivityName = vMenuID + "." + vMenuID + "_Activity";

                            if (menuActivity.start_grant(vMenuID, vMenuName) == true) { //== 권한 적용 ==//
                                //ACTIVITY 존재여부 확인.
                                boolean bCheck = TGSClass.isIntentAvailable(con.getApplicationContext(), menuActivity.getPackageName(), vActivityName);

                                if (bCheck) {
                                    Intent intent = TGSClass.ChangeView(con.getPackageName(), vActivityName);

                                    intent.putExtra("MENU_ID", vMenuID);
                                    intent.putExtra("MENU_NM", vMenuName);
                                    intent.putExtra("MENU_REMARK", vMenuRemark);
                                    intent.putExtra("START_COMMAND", vStartCommand);
                                    startActivityForResult(intent, vRowNum);
                                } else {
                                    TGSClass.AlertMessage(con.getApplicationContext(), vMenuName + "가 존재하지 않습니다.");
                                }
                            }
                        }
                    });

                    if (idx % 2 == 0) {

                        menuBox.setBackground(bgApplyColor);
                        leftMenu.addView(menuBox);

                        bgApplyColor = bgApplyColor == bgColor1 ? bgColor2 : bgColor1;   //메뉴 스타일 반전.

                    } else {
                        menuBox.setBackground(bgApplyColor);
                        rightMenu.addView(menuBox);

                        //bgApplyColor = bgColor1;
                    }
                }
            } catch (JSONException exJson) {
                TGSClass.AlertMessage(con.getApplicationContext(), exJson.getMessage());
//                finishAffinity();
            } catch (Exception ex) {
                TGSClass.AlertMessage(con.getApplicationContext(), ex.getMessage());
//                finishAffinity();
            }
        }
        return true;
    }

    protected boolean setMenu2() {
        if (!menuActivity.sJson.equals("[]")) {
            try {
                // 배경색
                Drawable bgColor1 = con.getDrawable(R.drawable.bg_prg_menu1);
                Drawable bgColor2 = con.getDrawable(R.drawable.bg_prg_menu2);
                Drawable bgApplyColor = bgColor1;

                JSONArray ja = new JSONArray(menuActivity.sJson);

                int vMenuCnt = ja.length(); //메뉴 숫자.

                leftMenu.setWeightSum((vMenuCnt / 2));
                rightMenu.setWeightSum((vMenuCnt / 2));

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObj = ja.getJSONObject(idx);

                    final String vMenuID = jObj.getString("MENU_ID");
                    final String vMenuName = jObj.getString("MENU_NM");
                    final String vMenuRemark = jObj.getString("REMARK");
                    final String vStartCommand = jObj.getString("START_COMMAND");
                    final int vRowNum = jObj.getInt("ROW_NUM");

                    LinearLayout.LayoutParams title_params
                            = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    LinearLayout.LayoutParams secondary_params
                            = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    secondary_params.setMargins(0, 8, 0, 0);

                    ViewGroup.LayoutParams vg_secondary_params
                            = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ViewGroup.MarginLayoutParams a
                            = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    a.setMargins(0, 8, 0, 0);

                    LinearLayout.LayoutParams supporting_params
                            = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    supporting_params.setMargins(0, 16, 0, 0);
                    ViewGroup.MarginLayoutParams b
                            = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    b.setMargins(0, 16, 0, 0);

                    TextView menu_title = new TextView(con);
                    menu_title.setText(vMenuName);
                    menu_title.setTextAppearance(R.style.TextAppearance_MaterialComponents_Headline6);
                    menu_title.setLayoutParams(title_params);

                    TextView secondary_text = new TextView(con);
                    secondary_text.setText(String.valueOf(vRowNum));
                    secondary_text.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body2);
                    secondary_text.setLayoutParams(a);

                    TextView supporting_text = new TextView(con);
                    supporting_text.setText("Supporting_Text");
                    supporting_text.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body2);
                    supporting_text.setLayoutParams(b);

                    //카드 View LayoutParams
                    LinearLayout.LayoutParams cardView_params
                            = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                    cardView_params.setMargins(8, 8, 8, 8);

                    //카드 View 안쪽1
                    LinearLayout.LayoutParams outer_layout_params
                            = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout linearLayout1 = new LinearLayout(con);
                    linearLayout1.setOrientation(LinearLayout.VERTICAL);
                    linearLayout1.setLayoutParams(outer_layout_params);

                    // 카드 View 안쪽1 - 1
                    LinearLayout.LayoutParams inner_layout_params
                            = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout linearLayout2 = new LinearLayout(con);
                    linearLayout2.setOrientation(LinearLayout.VERTICAL);
                    linearLayout2.setPadding(16, 16, 16, 16);
                    linearLayout2.setLayoutParams(inner_layout_params);

                    // 개별 메뉴 만들 컨트롤
                    MaterialCardView cardView = new MaterialCardView(con);
                    cardView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                    cardView.setLayoutParams(cardView_params);
                    cardView.setClickable(true);
                    cardView.setStrokeColor(getResources().getColor(R.color.GJ_secondaryColor));
                    cardView.setStrokeWidth(1);
                    cardView.setElevation(0);

                    linearLayout2.addView(menu_title);
                    linearLayout2.addView(secondary_text);
                    linearLayout2.addView(supporting_text);

                    linearLayout1.addView(linearLayout2);

                    cardView.addView(linearLayout1);

                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String vActivityName = vMenuID + "." + vMenuID + "_Activity";

                            if (menuActivity.start_grant(vMenuID, vMenuName) == true) {
                                // Activity 존재여부 확인
                                boolean bChk = TGSClass.isIntentAvailable(con.getApplicationContext(), menuActivity.getPackageName(), vActivityName);

                                if (bChk) {
                                    Intent intent = TGSClass.ChangeView(con.getPackageName(), vActivityName);

                                    intent.putExtra("MENU_ID", vMenuID);
                                    intent.putExtra("MENU_NM", vMenuName);
                                    intent.putExtra("MENU_REMARK", vMenuRemark);
                                    intent.putExtra("START_COMMAND", vStartCommand);
                                    startActivityForResult(intent, vRowNum);
                                } else {
                                    TGSClass.AlertMessage(con.getApplicationContext(), vMenuName + "가 존재하지 않습니다.");
                                }
                            }
                        }
                    });

                    if (idx % 2 == 0) {
                        /**
                        cardView.setBackground(bgApplyColor);
                         */
                        leftMenu.addView(cardView);

                        /**
                        bgApplyColor = bgApplyColor == bgColor1 ? bgColor2 : bgColor1;
                        */
                    } else {
                        /**
                        cardView.setBackground(bgApplyColor);
                        */
                        rightMenu.addView(cardView);
                    }
                }

            } catch (JSONException exJson) {
                TGSClass.AlertMessage(menuActivity, exJson.getMessage().toString());
            } catch (Exception ex) {
                TGSClass.AlertMessage(menuActivity, ex.getMessage().toString());
            }
        }
        return true;

    }

}