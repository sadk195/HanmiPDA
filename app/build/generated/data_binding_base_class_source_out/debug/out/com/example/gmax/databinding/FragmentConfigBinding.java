// Generated by view binder compiler. Do not edit!
package com.example.gmax.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.gmax.R;
import com.google.android.material.button.MaterialButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentConfigBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final LinearLayout appView1;

  @NonNull
  public final LinearLayout appView2;

  @NonNull
  public final LinearLayout appView3;

  @NonNull
  public final LinearLayout boxView;

  @NonNull
  public final MaterialButton btnApkDownload;

  @NonNull
  public final TextView dbNm;

  @NonNull
  public final TextView newVer;

  @NonNull
  public final TextView nowVer;

  private FragmentConfigBinding(@NonNull CoordinatorLayout rootView, @NonNull LinearLayout appView1,
      @NonNull LinearLayout appView2, @NonNull LinearLayout appView3, @NonNull LinearLayout boxView,
      @NonNull MaterialButton btnApkDownload, @NonNull TextView dbNm, @NonNull TextView newVer,
      @NonNull TextView nowVer) {
    this.rootView = rootView;
    this.appView1 = appView1;
    this.appView2 = appView2;
    this.appView3 = appView3;
    this.boxView = boxView;
    this.btnApkDownload = btnApkDownload;
    this.dbNm = dbNm;
    this.newVer = newVer;
    this.nowVer = nowVer;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentConfigBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentConfigBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_config, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentConfigBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.app_view1;
      LinearLayout appView1 = ViewBindings.findChildViewById(rootView, id);
      if (appView1 == null) {
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

      id = R.id.box_view;
      LinearLayout boxView = ViewBindings.findChildViewById(rootView, id);
      if (boxView == null) {
        break missingId;
      }

      id = R.id.btn_apk_download;
      MaterialButton btnApkDownload = ViewBindings.findChildViewById(rootView, id);
      if (btnApkDownload == null) {
        break missingId;
      }

      id = R.id.db_nm;
      TextView dbNm = ViewBindings.findChildViewById(rootView, id);
      if (dbNm == null) {
        break missingId;
      }

      id = R.id.new_ver;
      TextView newVer = ViewBindings.findChildViewById(rootView, id);
      if (newVer == null) {
        break missingId;
      }

      id = R.id.now_ver;
      TextView nowVer = ViewBindings.findChildViewById(rootView, id);
      if (nowVer == null) {
        break missingId;
      }

      return new FragmentConfigBinding((CoordinatorLayout) rootView, appView1, appView2, appView3,
          boxView, btnApkDownload, dbNm, newVer, nowVer);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}