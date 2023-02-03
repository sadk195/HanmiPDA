package com.example.gmax;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

public class BottomSheetExitDialog extends BottomSheetDialogFragment {

    // 초기 변수 설정
    private View view;
    // Activity 변수
    FragmentActivity fragmentActivity;
    // 인터페이스 변수
    /**
    private BottomSheetListener mListener;
    */

    String TAG;

    public BottomSheetExitDialog(String pTAG) {
        TAG = pTAG;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (this.TAG.toUpperCase().equals("EXIT")) {
            view = inflater.inflate(R.layout.bottom_sheet_exit_dialog_layout, container, false);

            // Toolbar Navigation
            MaterialToolbar materialToolbar;

            materialToolbar = view.findViewById(R.id.materialToolbar);
            materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getContentDescription().equals(materialToolbar.getNavigationContentDescription())) {
                        dismiss();
                    }
                }
            });

            // 바텀시트 숨기기 버튼
            MaterialButton btn_hide_bt_sheet;

            btn_hide_bt_sheet = view.findViewById(R.id.btn_hide_bt_sheet);
            btn_hide_bt_sheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentActivity.finishAffinity();
                }
            });

            // BottomSheetDialog 뒤로가기 막기(끌어내리기도 막아진다)
            // BottomSheetDialog 외부 화면(회색) 터치 시 종료 여부 boolean(false : 무반응, true : 종료)
            // setCanceledOnTouchOutside(false)의 경우 취소(뒤로가기)는 가능하지만 외부화면 터치를 막지만 setCanceledOnTouchOutside 는 취소(뒤로가기)가 작동하는 모든 동작을 막는다.
            setCancelable(false);
        }

        return view;
    }
    /**
    // 부모 엑티비티와 연결하기위한 인터페이스
    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }
    */
}
