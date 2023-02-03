// Generated by view binder compiler. Do not edit!
package com.example.gmax.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gmax.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityI26HdrBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextView appTitle;

  @NonNull
  public final LinearLayout appView1;

  @NonNull
  public final LinearLayout appView11;

  @NonNull
  public final LinearLayout appView2;

  @NonNull
  public final LinearLayout appView3;

  @NonNull
  public final LinearLayout appView4;

  @NonNull
  public final LinearLayout appView5;

  @NonNull
  public final LinearLayout boxView1;

  @NonNull
  public final LinearLayout boxView2;

  @NonNull
  public final Button btnQuery;

  @NonNull
  public final Button btnSave;

  @NonNull
  public final ImageView imgBarcode;

  @NonNull
  public final TextView issuedSlCd;

  @NonNull
  public final RelativeLayout layoutBody;

  @NonNull
  public final RelativeLayout layoutBtn;

  @NonNull
  public final RelativeLayout layoutMenuTitle;

  @NonNull
  public final RelativeLayout layoutQuery;

  @NonNull
  public final LinearLayout lblInformation;

  @NonNull
  public final ListView listOrder;

  @NonNull
  public final EditText moveReqNo;

  @NonNull
  public final TextView moveType;

  @NonNull
  public final EditText reqDt;

  @NonNull
  public final EditText reqLocation;

  @NonNull
  public final TextView reqPersonName;

  @NonNull
  public final TextView reqSlCd;

  @NonNull
  public final TextView transType;

  private ActivityI26HdrBinding(@NonNull RelativeLayout rootView, @NonNull TextView appTitle,
      @NonNull LinearLayout appView1, @NonNull LinearLayout appView11,
      @NonNull LinearLayout appView2, @NonNull LinearLayout appView3,
      @NonNull LinearLayout appView4, @NonNull LinearLayout appView5,
      @NonNull LinearLayout boxView1, @NonNull LinearLayout boxView2, @NonNull Button btnQuery,
      @NonNull Button btnSave, @NonNull ImageView imgBarcode, @NonNull TextView issuedSlCd,
      @NonNull RelativeLayout layoutBody, @NonNull RelativeLayout layoutBtn,
      @NonNull RelativeLayout layoutMenuTitle, @NonNull RelativeLayout layoutQuery,
      @NonNull LinearLayout lblInformation, @NonNull ListView listOrder,
      @NonNull EditText moveReqNo, @NonNull TextView moveType, @NonNull EditText reqDt,
      @NonNull EditText reqLocation, @NonNull TextView reqPersonName, @NonNull TextView reqSlCd,
      @NonNull TextView transType) {
    this.rootView = rootView;
    this.appTitle = appTitle;
    this.appView1 = appView1;
    this.appView11 = appView11;
    this.appView2 = appView2;
    this.appView3 = appView3;
    this.appView4 = appView4;
    this.appView5 = appView5;
    this.boxView1 = boxView1;
    this.boxView2 = boxView2;
    this.btnQuery = btnQuery;
    this.btnSave = btnSave;
    this.imgBarcode = imgBarcode;
    this.issuedSlCd = issuedSlCd;
    this.layoutBody = layoutBody;
    this.layoutBtn = layoutBtn;
    this.layoutMenuTitle = layoutMenuTitle;
    this.layoutQuery = layoutQuery;
    this.lblInformation = lblInformation;
    this.listOrder = listOrder;
    this.moveReqNo = moveReqNo;
    this.moveType = moveType;
    this.reqDt = reqDt;
    this.reqLocation = reqLocation;
    this.reqPersonName = reqPersonName;
    this.reqSlCd = reqSlCd;
    this.transType = transType;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityI26HdrBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityI26HdrBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_i26_hdr, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityI26HdrBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.app_title;
      TextView appTitle = ViewBindings.findChildViewById(rootView, id);
      if (appTitle == null) {
        break missingId;
      }

      id = R.id.app_view1;
      LinearLayout appView1 = ViewBindings.findChildViewById(rootView, id);
      if (appView1 == null) {
        break missingId;
      }

      id = R.id.app_view1_1;
      LinearLayout appView11 = ViewBindings.findChildViewById(rootView, id);
      if (appView11 == null) {
        break missingId;
      }

      id = R.id.app_view2;
      LinearLayout appView2 = ViewBindings.findChildViewById(rootView, id);
      if (appView2 == null) {
        break missingId;
      }

      id = R.id.app_view3;
      LinearLayout appView3 = ViewBindings.findChildViewById(rootView, id);
      if (appView3 == null) {
        break missingId;
      }

      id = R.id.app_view4;
      LinearLayout appView4 = ViewBindings.findChildViewById(rootView, id);
      if (appView4 == null) {
        break missingId;
      }

      id = R.id.app_view5;
      LinearLayout appView5 = ViewBindings.findChildViewById(rootView, id);
      if (appView5 == null) {
        break missingId;
      }

      id = R.id.box_view1;
      LinearLayout boxView1 = ViewBindings.findChildViewById(rootView, id);
      if (boxView1 == null) {
        break missingId;
      }

      id = R.id.box_view2;
      LinearLayout boxView2 = ViewBindings.findChildViewById(rootView, id);
      if (boxView2 == null) {
        break missingId;
      }

      id = R.id.btn_query;
      Button btnQuery = ViewBindings.findChildViewById(rootView, id);
      if (btnQuery == null) {
        break missingId;
      }

      id = R.id.btn_save;
      Button btnSave = ViewBindings.findChildViewById(rootView, id);
      if (btnSave == null) {
        break missingId;
      }

      id = R.id.img_barcode;
      ImageView imgBarcode = ViewBindings.findChildViewById(rootView, id);
      if (imgBarcode == null) {
        break missingId;
      }

      id = R.id.issued_sl_cd;
      TextView issuedSlCd = ViewBindings.findChildViewById(rootView, id);
      if (issuedSlCd == null) {
        break missingId;
      }

      id = R.id.layout_body;
      RelativeLayout layoutBody = ViewBindings.findChildViewById(rootView, id);
      if (layoutBody == null) {
        break missingId;
      }

      id = R.id.layout_btn;
      RelativeLayout layoutBtn = ViewBindings.findChildViewById(rootView, id);
      if (layoutBtn == null) {
        break missingId;
      }

      id = R.id.layout_menu_title;
      RelativeLayout layoutMenuTitle = ViewBindings.findChildViewById(rootView, id);
      if (layoutMenuTitle == null) {
        break missingId;
      }

      id = R.id.layout_query;
      RelativeLayout layoutQuery = ViewBindings.findChildViewById(rootView, id);
      if (layoutQuery == null) {
        break missingId;
      }

      id = R.id.lbl_information;
      LinearLayout lblInformation = ViewBindings.findChildViewById(rootView, id);
      if (lblInformation == null) {
        break missingId;
      }

      id = R.id.listOrder;
      ListView listOrder = ViewBindings.findChildViewById(rootView, id);
      if (listOrder == null) {
        break missingId;
      }

      id = R.id.move_req_no;
      EditText moveReqNo = ViewBindings.findChildViewById(rootView, id);
      if (moveReqNo == null) {
        break missingId;
      }

      id = R.id.move_type;
      TextView moveType = ViewBindings.findChildViewById(rootView, id);
      if (moveType == null) {
        break missingId;
      }

      id = R.id.req_dt;
      EditText reqDt = ViewBindings.findChildViewById(rootView, id);
      if (reqDt == null) {
        break missingId;
      }

      id = R.id.req_location;
      EditText reqLocation = ViewBindings.findChildViewById(rootView, id);
      if (reqLocation == null) {
        break missingId;
      }

      id = R.id.req_person_name;
      TextView reqPersonName = ViewBindings.findChildViewById(rootView, id);
      if (reqPersonName == null) {
        break missingId;
      }

      id = R.id.req_sl_cd;
      TextView reqSlCd = ViewBindings.findChildViewById(rootView, id);
      if (reqSlCd == null) {
        break missingId;
      }

      id = R.id.trans_type;
      TextView transType = ViewBindings.findChildViewById(rootView, id);
      if (transType == null) {
        break missingId;
      }

      return new ActivityI26HdrBinding((RelativeLayout) rootView, appTitle, appView1, appView11,
          appView2, appView3, appView4, appView5, boxView1, boxView2, btnQuery, btnSave, imgBarcode,
          issuedSlCd, layoutBody, layoutBtn, layoutMenuTitle, layoutQuery, lblInformation,
          listOrder, moveReqNo, moveType, reqDt, reqLocation, reqPersonName, reqSlCd, transType);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}