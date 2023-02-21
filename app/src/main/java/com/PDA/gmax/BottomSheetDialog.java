package com.PDA.gmax;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    // 초기 변수 설정
    private Context con;
    private FragmentActivity fragmentActivity;
    private View view;
    private String TAG;
    private SimpleDateFormat simpleDF;
    // 인터페이스 변수

    public BottomSheetDialog(String pTag) {
        this.TAG = pTag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        con = context;
        fragmentActivity = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (TAG.toUpperCase().equals("M11_DTL")) {
            view = inflater.inflate(R.layout.bottom_sheet_dialog_layout, container, false);

            simpleDF = new SimpleDateFormat("yyyy-MM-dd");

            Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal1.setTime(new Date());

            TextInputLayout in_dt = view.findViewById(R.id.in_dt);
            in_dt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDate(v, cal1);
                }
            });

            MaterialButton btn_m11_save = view.findViewById(R.id.btn_m11_save);
            btn_m11_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TGSClass.showToast(getActivity(), "INFO", "", "버튼");
                }
            });
        }

        return view;
    }

    /**
     * MaterialDatePicker - No Title
     * @param pView
     * @param pCalendar
     */
    protected void openDate(final View pView, final Calendar pCalendar) {
        openDate(pView, pCalendar, "날짜 선택");
    }

    /**
     * MaterialDatePicker - Title
     * @param pView
     * @param pCalendar
     * @param pTitle
     */
    protected void openDate(final View pView, final Calendar pCalendar, final String pTitle) {
        MaterialDatePicker.Builder<Long> mDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(pTitle)
                .setSelection(pCalendar.getTimeInMillis());

        // 날짜 셋팅
        MaterialDatePicker materialDatePicker = mDatePicker.build();
        materialDatePicker.show(fragmentActivity.getSupportFragmentManager(), "DATE_PICKER");

        // 확인 버튼
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Date date = new Date();

                date.setTime(selection);

                pCalendar.setTime(date);

                String strDate = simpleDF.format(date);

                if (pView instanceof TextView) {
                    TextView tv = (TextView) pView;

                    tv.setText(strDate);
                } else if (pView instanceof TextInputLayout) {
                    TextInputLayout til = (TextInputLayout) pView;
                    til.getEditText().setText(strDate);
                }
            }
        });
    }

    /**
    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }
    */
}
