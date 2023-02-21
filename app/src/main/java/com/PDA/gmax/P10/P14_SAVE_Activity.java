package com.PDA.gmax.P10;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.PDA.gmax.MySession;
import com.PDA.gmax.DBAccess;
import com.PDA.gmax.R;
import com.PDA.gmax.TGSClass;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapPrimitive;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class P14_SAVE_Activity extends AppCompatActivity {


    Button btn_save;
    Button btn_start;
    Button btn_cancle;
    Button btn_scan;
    Button btn_reset;

    public String sJson = "";
    public String sJsonConfig = "";
    public String sJsonConfigSet = "";
    public String sJsonCombo = "";
    public String result_msg = "";

    public String sJson_StartWork = "";
    public String sJson_check_outsourcing = "";
    public String sJson_check = "";
    public String sJson_CloseWork = "";


    public String sResultNo = "";

    public EditText selectDate;
    private int mYear, mMonth, mDay;
    private MySession global;
    public String sJOB_CD;
    public String sFACILTY_NM;
    public String sWORKER_NM;

    public TextView lbl_ProdtOrderNo;
    public TextView lbl_opr_no;
    public TextView lbl_item_cd;
    public TextView lbl_item_name;
    public TextView lbl_ORD_QTY;
    public TextView lbl_REMAIN_QTY;
    public TextView lbl_TRACKING_NO;
    public TextView lbl_FACILITY_CD;
    //public TextView lbl_FACILITY_NM;
    public TextView cmb_FACILITY_NM;  //
    public TextView lbl_WORKER_CD;
    public TextView cmb_WORKER_NM;  //
    public TextView lbl_ROUT_ORDER;
    public TextView lbl_PRODT_ORDER_UNIT;
    public TextView lbl_BASE_UNIT;
    public TextView lbl_SPEC;
    public TextView lbl_RCPT_ORDER_QTY;
    public TextView lbl_RCPT_BASE_QTY;
    public TextView lbl_PROD_QTY_IN_ORDER_UNIT;
    public TextView lbl_GOOD_QTY;
    public TextView lbl_BAD_QTY;
    public TextView lbl_SL_CD;
    public TextView lbl_SL_NM;
    public TextView lbl_WC_CD;
    public TextView lbl_WC_NM;
    public TextView lbl_WC_MGR;
    public TextView METHOD;
    public TextView state_message;

    public EditText start_day;
    public EditText start_time;
    public EditText end_day;
    public EditText end_time;
    public TextView lbl_WORK_QTY;
    SoapPrimitive resultString;

    public String Sql;
    public String method_chk;
    public String prodt_order_no_remark;
    public String prodt_order_no_query;
    public String opr_no_remark;
    public String opr_no_query;

    Calendar myCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    /*public class MainActivity extends AppCompatActivity {*/

    //Calendar myCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            update_start_dt();

        }
    };


    DatePickerDialog.OnDateSetListener myDatePicker1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            update_end_dt();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p14_save);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_scan = (Button) findViewById(R.id.btn_scan);

        lbl_ProdtOrderNo = findViewById(R.id.lbl_ProdtOrderNo);
        lbl_opr_no = findViewById(R.id.lbl_opr_no);
        lbl_item_cd = findViewById(R.id.lbl_item_cd);
        lbl_item_name = findViewById(R.id.lbl_item_name);
        lbl_ORD_QTY = findViewById(R.id.lbl_ORD_QTY);
        lbl_REMAIN_QTY = findViewById(R.id.lbl_REMAIN_QTY);
        lbl_TRACKING_NO = findViewById(R.id.lbl_TRACKING_NO);
        lbl_FACILITY_CD = findViewById(R.id.lbl_FACILITY_CD);
        cmb_FACILITY_NM = findViewById(R.id.cmb_FACILITY_NM);
        lbl_WORKER_CD = findViewById(R.id.lbl_WORKER_CD);
        cmb_WORKER_NM = findViewById(R.id.cmb_WORKER_NM);
        start_day = findViewById(R.id.start_day);
        start_time = findViewById(R.id.start_time);
        end_day = findViewById(R.id.end_day);
        end_time = findViewById(R.id.end_time);
        lbl_WORK_QTY = findViewById(R.id.lbl_WORK_QTY);
        lbl_ROUT_ORDER = findViewById(R.id.lbl_ROUT_ORDER);
        lbl_PRODT_ORDER_UNIT = findViewById(R.id.lbl_PRODT_ORDER_UNIT);
        lbl_BASE_UNIT = findViewById(R.id.lbl_BASE_UNIT);
        lbl_SPEC = findViewById(R.id.lbl_SPEC);
        lbl_RCPT_ORDER_QTY = findViewById(R.id.lbl_RCPT_ORDER_QTY);
        lbl_RCPT_BASE_QTY = findViewById(R.id.lbl_RCPT_BASE_QTY);
        lbl_PROD_QTY_IN_ORDER_UNIT = findViewById(R.id.lbl_PROD_QTY_IN_ORDER_UNIT);
        lbl_GOOD_QTY = findViewById(R.id.lbl_GOOD_QTY);
        lbl_BAD_QTY = findViewById(R.id.lbl_BAD_QTY);
        lbl_SL_CD = findViewById(R.id.lbl_SL_CD);
        lbl_SL_NM = findViewById(R.id.lbl_SL_NM);
        lbl_WC_CD = findViewById(R.id.lbl_WC_CD);
        lbl_WC_NM = findViewById(R.id.lbl_WC_NM);
        lbl_WC_MGR = findViewById(R.id.lbl_WC_MGR);
        METHOD = findViewById(R.id.METHOD);
        state_message = findViewById(R.id.state_message);

        sJOB_CD = getIntent().getStringExtra("JOB_CD");
        global = (MySession) getApplication();

        Intent intent = new Intent(this.getIntent());

        String PRODT_ORDER_NO = intent.getStringExtra("prodt_order_no");
        String OPR_NO = intent.getStringExtra("opr_no");

        btn_start.setVisibility(View.VISIBLE);
        btn_cancle.setVisibility(View.VISIBLE);
        btn_save.setVisibility(View.VISIBLE);

        DBQuery_GET_PRODT_INFO(PRODT_ORDER_NO, OPR_NO);
        DBQuery_PRODT_START(PRODT_ORDER_NO, OPR_NO);
        DBQuery_GetWC_CD(PRODT_ORDER_NO, OPR_NO);

        final EditText start_day = (EditText) findViewById(R.id.start_day);
        SimpleDateFormat current_dt = new SimpleDateFormat("yyyy-MM-dd");
        start_day.setText(current_dt.format(new Date()));
        start_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(P14_SAVE_Activity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText end_day = (EditText) findViewById(R.id.end_day);
        end_day.setText(current_dt.format(new Date()));
        end_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(P14_SAVE_Activity.this, myDatePicker1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final EditText start_time = (EditText) findViewById(R.id.start_time);
        SimpleDateFormat current_start_time = new SimpleDateFormat("08:00");
        long now = System.currentTimeMillis();
        start_time.setText(current_start_time.format(new Time(now)));
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(P14_SAVE_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        // EditText에 출력할 형식 지정
                        start_time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        SimpleDateFormat current_end_time = new SimpleDateFormat("20:00");
        final EditText end_time = (EditText) findViewById(R.id.end_time);
        end_time.setText(current_end_time.format(new Time(now)));
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(P14_SAVE_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // EditText에 출력할 형식 지정
                        end_time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        /*
        // EMK 에서는 작업지시서를 스캔해서 생산실적 등록을 하지만, 작업지시서를 분실했을 때를 대비하여 미리 만들어 놓은 작업지시서 화면입니다.
        // 해당 작업지시서 선택 화면은 업체에서 요청했을 경우 배포하는 것으로 결정났으므로 2020-12-11 현재는 주석처리하였습니다.
        lbl_ProdtOrderNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = TGSClass.ChangeView(getPackageName(), P11_PROD_ORDER_Activity.class);
                startActivity(intent);
            }
        });

         */


        btn_start.setOnClickListener(new View.OnClickListener() {        //실적등록버튼
            @Override
            public void onClick(View v) {

                if (state_message.getText().toString().equals("* 공정 실적 등록(실적버튼) *")) {
                    TGSClass.AlertMessage(getApplicationContext(), "작업장별 실적등록을 관리하므로 실적 버튼만을 사용합니다.");
                } else if (state_message.getText().toString().equals("* 작업 중 *")) {
                    TGSClass.AlertMessage(getApplicationContext(), "작업 중입니다.");
                } else {
                    String lbl_FACILITY_CD_st = lbl_FACILITY_CD.getText().toString();
                    String lbl_WORKER_CD_st = lbl_WORKER_CD.getText().toString();
                    String lbl_REMAIN_QTY_st = lbl_REMAIN_QTY.getText().toString();

                    if (lbl_REMAIN_QTY_st.equals("0.0")) {
                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                        mAlert.setTitle("공정별 실적등록")
                                .setMessage("실적등록이 완료된 작업입니다.")
                                .setPositiveButton("확인", null)
                                .create().show();

                        return;
                    } else if (lbl_FACILITY_CD_st.equals("")) {
                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                        mAlert.setTitle("공정별 실적등록")
                                .setMessage("설비정보가 없습니다.")
                                .setPositiveButton("확인", null)
                                .create().show();

                        return;
                    } else if (lbl_WORKER_CD_st.equals("")) {
                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                        mAlert.setTitle("공정별 실적등록")
                                .setMessage("작업자 정보가 없습니다.")
                                .setPositiveButton("확인", null)
                                .create().show();

                        return;
                    } else {
                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                        mAlert.setTitle("공정별 실적등록")
                                .setMessage("작업 시작하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            if (DBQuery_StartWork() == true) {
                                                DBQuery_InitData();

                                                DBQuery_PRODT_START(prodt_order_no_query, opr_no_query);

                                                MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                                                mAlert.setTitle("공정별 실적등록")
                                                        .setMessage("작업 중입니다. 설비 변경 불가능")
                                                        .setPositiveButton("확인", null)
                                                        .create().show();
                                            }
                                        } catch (Exception e1) {
                                            return;
                                        }
                                    }
                                })
                                .setNegativeButton("아니오", null)
                                .create().show();
                    }
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {        //실적등록버튼
            @Override
            public void onClick(View v) {

                if (state_message.getText().toString().equals("* 작업 대기 중 *")) {
                    TGSClass.AlertMessage(getApplicationContext(), "작업 대기중입니다.");
                    return;
                } else {
                    String lbl_ORD_QTY_st = lbl_ORD_QTY.getText().toString();
                    String lbl_GOOD_QTY_st = lbl_GOOD_QTY.getText().toString();


                    /* 지시량 양품수량 체크*/
                    if (lbl_ORD_QTY_st.equals(lbl_GOOD_QTY_st)) {
                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                        mAlert.setTitle("공정별 실적등록")
                                .setMessage("지시량이 생산량을 초과 할 수 없습니다.")
                                .setPositiveButton("확인", null)
                                .create().show();

                        return;
                    }

                    String lbl_WORKER_CD_st = lbl_WORKER_CD.getText().toString();

                    /* 지시량 양품수량 체크*/
                    if (lbl_WORKER_CD_st.equals("")) {
                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                        mAlert.setTitle("공정별 실적등록")
                                .setMessage("작업자가 선택되지 않았습니다.")
                                .setPositiveButton("확인", null)
                                .create().show();

                        return;
                    }

                    String start_day_st = start_day.getText().toString();
                    String start_time_st = start_time.getText().toString();
                    String end_day_st = end_day.getText().toString();
                    String end_time_st = end_time.getText().toString();

                    int start_time_int = Integer.parseInt(start_time_st.replace(":", ""));
                    int end_time_int = Integer.parseInt(end_time_st.replace(":", ""));

                    if (start_time_int >= end_time_int) {
                        int start_day_int = Integer.parseInt(start_day_st.replace("-", ""));
                        int end_day_int = Integer.parseInt(end_day_st.replace("-", ""));
                        if (start_day_int >= end_day_int) {
                            MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                            mAlert.setTitle("공정별 실적등록")
                                    .setMessage("작업 종료시간이 시작시간 보다 빠를 순 없습니다.")
                                    .setPositiveButton("확인", null)
                                    .create().show();

                            return;
                        }
                    }

                    if (method_chk.equals("N")) {
                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                        mAlert.setTitle("생산실적등록")
                                .setMessage("실적 수량이 0입니다. 작업종료 하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            final int vQTY = 0;

                                            EditText end_day2 = findViewById(R.id.end_day);
                                            EditText end_time2 = findViewById(R.id.end_time);

                                            String end_day_st = end_day2.getText().toString();
                                            String end_time_st = end_time2.getText().toString();

                                            String end_datetime = end_day_st + " " + end_time_st;

                                            DBQuery_CloseWork(vQTY, "CL", end_datetime);
                                        } catch (Exception e1) {
                                            return;
                                        }
                                        DBQuery_PRODT_START(prodt_order_no_query, opr_no_query);
                                        DBQuery_GET_PRODT_INFO(prodt_order_no_query, opr_no_query);
                                        DBQuery_InitData();
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                                        mAlert.setTitle("생산실적등록")
                                                .setMessage("실적 수량이 1입니다. 작업종료 하시겠습니까?")
                                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        DBQuery_XUSP_MES_APP_PH50_CHECK();

                                                        global = (MySession) getApplication(); //전역 클래스

                                                        String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");

                                                        if (vPlant_CD == null)
                                                            vPlant_CD = global.getPlantCDString();

                                                        String lbl_ProdtOrderNo_st = lbl_ProdtOrderNo.getText().toString();
                                                        String lbl_opr_no_st = lbl_opr_no.getText().toString();

                                                        EditText end_day2 = findViewById(R.id.end_day);
                                                        EditText end_time2 = findViewById(R.id.end_time);
                                                        String end_day_st = end_day2.getText().toString();
                                                        String end_time_st = end_time2.getText().toString() + ":00";

                                                        String end_datetime = end_day_st + " " + end_time_st;

                                                        DBQuery_GET_BL("C", vPlant_CD, "G", "1", lbl_ProdtOrderNo_st, lbl_opr_no_st, "1", "", "1", end_day_st);


                                                        DBQuery_CloseWork(1, "CL", end_datetime);
                                                        TGSClass.AlertMessage(getApplicationContext(), "완료처리 되었습니다.");
                                                        DBQuery_PRODT_START(prodt_order_no_query, opr_no_query);
                                                        DBQuery_GET_PRODT_INFO(prodt_order_no_query, opr_no_query);
                                                    }
                                                })
                                                .setNegativeButton("아니오", null)
                                                .create().show();
                                    }
                                })
                                .create().show();
                    } else if (method_chk.equals("Y")) {
                        final int vQTY = 0;

                        MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                        mAlert.setTitle("생산실적등록")
                                .setMessage("실적 수량이 0입니다. 작업종료 하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            EditText end_day2 = findViewById(R.id.end_day);
                                            EditText end_time2 = findViewById(R.id.end_time);

                                            String end_day_st = end_day2.getText().toString();
                                            String end_time_st = end_time2.getText().toString();

                                            String end_datetime = end_day_st + " " + end_time_st;

                                            DBQuery_StartWork();
                                            DBQuery_CloseWork(vQTY, "CL", end_datetime);
                                        } catch (Exception e1) {

                                        }
                                        DBQuery_InitData();
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                                            mAlert.setTitle("생산실적등록")
                                                    .setMessage("실적 수량이 1입니다. 작업종료 하시겠습니까?")
                                                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            DBQuery_XUSP_MES_APP_PH50_CHECK();

                                                            global = (MySession) getApplication(); //전역 클래스

                                                            String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");

                                                            if (vPlant_CD == null)
                                                                vPlant_CD = global.getPlantCDString();

                                                            String lbl_ProdtOrderNo_st = lbl_ProdtOrderNo.getText().toString();
                                                            String lbl_opr_no_st = lbl_opr_no.getText().toString();

                                                            EditText end_day2 = findViewById(R.id.end_day);
                                                            EditText end_time2 = findViewById(R.id.end_time);
                                                            String end_day_st = end_day2.getText().toString();
                                                            String end_time_st = end_time2.getText().toString();

                                                            String end_datetime = end_day_st + " " + end_time_st;

                                                            DBQuery_GET_BL("C", vPlant_CD, "G", "1", lbl_ProdtOrderNo_st, lbl_opr_no_st, "1", "", "1", end_datetime);

                                                            //작업장의 실적 저장방식이 시작시간, 종료시간 동시 저장 방식이면 실적 등록 할때, 시작정보를 등록한다.
                                                            DBQuery_StartWork();
                                                            DBQuery_CloseWork(1, "CL", end_datetime);
                                                            TGSClass.AlertMessage(getApplicationContext(), "완료처리 되었습니다.");
                                                            DBQuery_PRODT_START(lbl_ProdtOrderNo_st, lbl_opr_no_st);
                                                            DBQuery_GET_PRODT_INFO(lbl_ProdtOrderNo_st, lbl_opr_no_st);
                                                        }
                                                    })
                                                    .setNegativeButton("아니오", null)
                                                    .create().show();
                                        } catch (Exception e1) {

                                        }
                                        DBQuery_InitData();
                                    }
                                })
                                .create().show();
                    }
                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TGSClass.AlertMessage(getApplicationContext(), "아직 기능이 없습니다.");
                //InitData();
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TGSClass.AlertMessage(getApplicationContext(), "아직 기능이 없습니다.");
                //InitData();
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state_message.getText().toString().equals("* 작업 대기 중 *")) {
                    TGSClass.AlertMessage(getApplicationContext(), "작업이 시작되지 않았습니다.");
                    return;
                } else if (state_message.getText().toString().equals("* 공정 실적 등록(실적버튼) *")) {
                    TGSClass.AlertMessage(getApplicationContext(), "작업장별 실적등록을 관리하므로 실적 버튼만을 사용합니다.");
                    return;
                } else {
                    MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                    mAlert.setTitle("작업 취소")
                            .setMessage("작업 시작을 취소 하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DBQuery_DeleteWork();
                                    DBQuery_PRODT_START(prodt_order_no_query, opr_no_query);
                                    DBQuery_GET_PRODT_INFO(prodt_order_no_query, opr_no_query);
                                }
                            })
                            .setNegativeButton("아니오", null)
                            .create().show();
                }
            }
        });

    }

    public void DBQuery_GetWC_CD(final String prodt_order_no, final String opr_no) {
        Thread workingThread = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_MES_P4004MA1_GET_WC_CD ";
                sql += "  @PRODT_ORDER_NO = '" + prodt_order_no + "'";
                sql += " ,@OPR_NO = '" + opr_no + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);

                GetWC_CD();
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    public void GetWC_CD() {
        if (!sJson.equals("[]")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                if (ja.length() == 1) {
                    JSONObject jObject = ja.getJSONObject(0);

                    lbl_FACILITY_CD.setText(jObject.getString("WC_CD")); //제조오더
                    cmb_FACILITY_NM.setText(jObject.getString("WC_NM")); //공순
                    lbl_WORKER_CD.setText(jObject.getString("BP_CD")); //품번
                    cmb_WORKER_NM.setText(jObject.getString("BP_NM")); //품명
                }
            } catch (JSONException ex) {
                return;
            } catch (Exception e1) {
                return;
            }
        }
    }

    private void update_start_dt() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_date = (EditText) findViewById(R.id.start_day);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void update_end_dt() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText end_day = (EditText) findViewById(R.id.end_day);
        end_day.setText(sdf.format(myCalendar.getTime()));
    }

    public void InitText() {
        lbl_ProdtOrderNo.setText("");
        lbl_opr_no.setText("");
        lbl_item_cd.setText("");
        lbl_item_name.setText("");
        lbl_ORD_QTY.setText("");
        lbl_REMAIN_QTY.setText("");
        lbl_TRACKING_NO.setText("");
        lbl_FACILITY_CD.setText("");
        cmb_FACILITY_NM.setText("");
        lbl_WORKER_CD.setText("");
        cmb_WORKER_NM.setText("");
        start_day.setText("");
        start_time.setText("");
        end_day.setText("");
        end_time.setText("");
        lbl_WORK_QTY.setText("");
        lbl_ROUT_ORDER.setText("");
        lbl_PRODT_ORDER_UNIT.setText("");
        lbl_BASE_UNIT.setText("");
        lbl_SPEC.setText("");
        lbl_RCPT_ORDER_QTY.setText("");
        lbl_RCPT_BASE_QTY.setText("");
        lbl_PROD_QTY_IN_ORDER_UNIT.setText("");
        lbl_GOOD_QTY.setText("");
        lbl_BAD_QTY.setText("");
        lbl_SL_CD.setText("");
        lbl_SL_NM.setText("");
        lbl_WC_CD.setText("");
        lbl_WC_NM.setText("");
        lbl_WC_MGR.setText("");
        METHOD.setText("");
    }

    public void InitData() {
        String lbl_WC_CD_st = lbl_WC_CD.getText().toString();

        //설정 값을 초기화 한다.
        //DBQuery_GetConfig();

        if (!sJsonConfig.equals("")) {
            try {
                JSONArray ja = new JSONArray(sJsonConfig);

                if (ja.length() > 0) {
                    JSONObject jObject = ja.getJSONObject(0);
                    String vConfigValue = jObject.getString("CONFIG_VALUE");
                }
            } catch (JSONException ex) {

            } catch (Exception e1) {

            }
        }
    }

    public void DBQuery(final String pDate, final String pGroupCD) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_AND_P_RESULT_GETLIST_KO773 ";
                sql += "  @DT = '" + pDate + "'";
                sql += " ,@ITEM_GROUP_CD = '" + pGroupCD + "'";
                sql += " ,@JOB_CD= '" + sJOB_CD + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
    }

    public void DBQuery_GetConfig() {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread2 = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_AND_P_UNIT_CONFIG ";
                sql += "  @FLAG = 'GET'";
                sql += " ,@PLANT_CD = '" + global.getPlantCDString() + "'";
                sql += " ,@CONFIG_ID = 'ITEM_GROUP'";
                sql += " ,@UNIT_CD = '" + global.getUnitCDString() + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJsonConfig = dba.SendHttpMessage("GetSQLData", pParms);
            }


        };
        workingThread2.start();   //스레드 시작
        try {
            workingThread2.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.
        } catch (InterruptedException ex) {

        }
    }

    public void OpenPopupDate(View view) {
        if (view == selectDate) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String vYear = String.format("%04d", year);
                    String vMonth = String.format("%02d", monthOfYear + 1);
                    String vDay = String.format("%02d", dayOfMonth);

                    selectDate.setText(vYear + "-" + vMonth + "-" + vDay);

                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    public void DBQuery_InitData() {
        DBQuery_XUSP_MES_APP_PH50_CHECK();

        DBquery_Chk_Outsourcing();
    }

    /*----------------------------[ CheckLastRouter ]---------------------------------------*/
    public void DBQuery_XUSP_MES_APP_PH50_CHECK() {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession) getApplication(); //전역 클래스

                String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");

                if (vPlant_CD == null)
                    vPlant_CD = global.getPlantCDString();

                String lbl_ProdtOrderNo_st = lbl_ProdtOrderNo.getText().toString();
                String lbl_opr_no_st = lbl_opr_no.getText().toString();

                String sql = " EXEC XUSP_MES_APP_PH50_CHECK ";
                sql += "    @PLANT_CD ='" + vPlant_CD + "'";
                sql += "   , @PRODT_ORDER_NO ='" + lbl_ProdtOrderNo_st + "'";
                sql += "   , @OPR_NO ='" + lbl_opr_no_st + "'";
                sql += "   , @QTY  = 1";
                sql += "   , @RTNMSG  = ''";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);

                Check_Last_Router();
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
        }
    }

    public boolean Check_Last_Router() {
        if (!sJson.equals("[]")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                if (ja.length() > 0) {
                    JSONObject jObject = ja.getJSONObject(0);

                    String RTNMSG = jObject.getString("RTNMSG"); //제조오더

                    if (RTNMSG.equals("전 공정 실적이 등록 되어있지 않습니다.")) {
                        TGSClass.AlertMessage(getApplicationContext(), "전 공정 실적이 등록 되어있지 않습니다.");
                        return false;
                    }
                }
            } catch (JSONException ex) {
                return false;
            } catch (Exception e1) {
                return false;
            }
        } else {
            TGSClass.AlertMessage(getApplicationContext(), "전 공정 실적이 등록 되어있지 않습니다.");
        }
        return true;
    }
    /*-------------------------------------------------------------------*/

    /*-------------------------------------------------------------------*/
    public void DBQuery_GET_PRODT_INFO(final String pPRODT_NO, final String pOPR_NO) {
        ////////////////////////////// 웹 서비스 호출 시 쓰레드 사용 ////////////////////////////////////////////////////////
        Thread workingThread = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_APK_PRODT_ORDER_DETAIL_INFO_GET ";
                sql += "  @PRODT_ORDER_NO = '" + pPRODT_NO + "'";
                sql += " ,@OPR_NO = '" + pOPR_NO + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson = dba.SendHttpMessage("GetSQLData", pParms);

                Start();
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
        }
    }

    public void Start() {
        if (!sJson.equals("")) {
            try {
                JSONArray ja = new JSONArray(sJson);

                // 빈 데이터 리스트 생성.
                final ArrayList<P14_SAVE_INFO> lstItem = new ArrayList<>();

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    lbl_ProdtOrderNo.setText(jObject.getString("PRODT_ORDER_NO")); //제조오더
                    lbl_opr_no.setText(jObject.getString("OPR_NO")); //공순
                    lbl_ROUT_ORDER.setText(jObject.getString("ROUT_ORDER")); //공정구분
                    lbl_item_cd.setText(jObject.getString("ITEM_CD")); //품번
                    lbl_item_name.setText(jObject.getString("ITEM_NM")); //품명
                    lbl_PRODT_ORDER_UNIT.setText(jObject.getString("PRODT_ORDER_UNIT")); //단위
                    lbl_BASE_UNIT.setText(jObject.getString("BASE_UNIT")); //단위
                    lbl_SPEC.setText(jObject.getString("SPEC")); //규격
                    lbl_TRACKING_NO.setText(jObject.getString("TRACKING_NO")); //TRACKING_NO
                    lbl_ORD_QTY.setText(jObject.getString("PRODT_ORDER_QTY")); //지시량
                    lbl_RCPT_ORDER_QTY.setText(jObject.getString("RCPT_ORDER_QTY")); //오더단위 입고량
                    lbl_RCPT_BASE_QTY.setText(jObject.getString("RCPT_BASE_QTY"));  //기준단위 입고량
                    lbl_PROD_QTY_IN_ORDER_UNIT.setText(jObject.getString("PROD_QTY_IN_ORDER_UNIT"));  //생산량
                    lbl_GOOD_QTY.setText(jObject.getString("GOOD_QTY"));  //양품생산량
                    lbl_BAD_QTY.setText(jObject.getString("BAD_QTY"));  //불량생산량
                    lbl_REMAIN_QTY.setText(jObject.getString("REMAIN_QTY"));  //잔량
                    lbl_SL_CD.setText(jObject.getString("SL_CD"));  //출고창고
                    lbl_SL_NM.setText(jObject.getString("SL_NM"));  //출고창고명
                    lbl_WC_CD.setText(jObject.getString("WC_CD"));  //작업장코드
                    lbl_WC_NM.setText(jObject.getString("WC_NM"));  //작업장명
                    lbl_WC_MGR.setText(jObject.getString("WC_MGR"));  //작업장그룹
                    METHOD.setText(jObject.getString("METHOD"));  //작업장그룹

                    method_chk = jObject.getString("METHOD");
                }
            } catch (JSONException ex) {

            } catch (Exception e1) {

            }
        } else {
            TGSClass.AlertMessage(getApplicationContext(), "제조오더에 해당하는 공순이 없습니다.");
        }
    }
    /*-----------------------------------------------------------------------------------*/

    /*-----------------------------[ StartWork ]--------------------------------------*/
    public boolean DBQuery_StartWork() {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession) getApplication(); //전역 클래스

                String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");

                if (vPlant_CD == null)
                    vPlant_CD = global.getPlantCDString();

                String lbl_ProdtOrderNo_st = lbl_ProdtOrderNo.getText().toString();
                String lbl_opr_no_st = lbl_opr_no.getText().toString();
                String lbl_FACILITY_CD_st = lbl_FACILITY_CD.getText().toString();
                String lbl_WORKER_CD_st = lbl_WORKER_CD.getText().toString();
                String start_day_st = start_day.getText().toString();
                String start_time_st = start_time.getText().toString();

                String start_datetime = start_day_st + " " + start_time_st;

                String sql = " EXEC XUSP_APP_P2002MA1_SET ";
                sql += "  @CUD_FLAG = 'C'";
                sql += " ,@PLANT_CD = '" + vPlant_CD + "'";
                sql += " ,@PRODT_ORDER_NO = '" + lbl_ProdtOrderNo_st + "'";
                sql += " ,@OPR_NO = '" + lbl_opr_no_st + "'";
                sql += " ,@FACILITY_CD = '" + lbl_FACILITY_CD_st + "'";
                sql += " ,@WORKER_ID = '" + lbl_WORKER_CD_st + "'";
                sql += " ,@REAL_START_DT = '" + start_datetime + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                prodt_order_no_query = lbl_ProdtOrderNo_st;
                opr_no_query = lbl_opr_no_st;

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_StartWork = dba.SendHttpMessage("GetSQLData", pParms);
                //TGSClass.AlertMessage(getApplicationContext(), sJson_StartWork);

                StartWork();

            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            return false;
        }
        return true;
    }

    public void StartWork() {
        if (!sJson_StartWork.equals("[]")) {
            try {
                JSONArray ja = new JSONArray(sJson_StartWork);

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    sResultNo = jObject.getString("RESULT_NO");
                }
            } catch (JSONException ex) {

            } catch (Exception e1) {

            }
        } else {
            TGSClass.AlertMessage(getApplicationContext(), "작업 시작 오류");
        }
    }
    /*----------------------------------------------------------------------------------*/

    /*----------------------------[ Fn_Get_List ]---------------------------------------*/
    public void DBquery_Chk_Outsourcing() {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession) getApplication(); //전역 클래스

                String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");

                if (vPlant_CD == null)
                    vPlant_CD = global.getPlantCDString();

                String lbl_ProdtOrderNo_st = lbl_ProdtOrderNo.getText().toString();
                String lbl_opr_no_st = lbl_opr_no.getText().toString();
                String lbl_WC_CD_st = lbl_WC_CD.getText().toString();

                String sql = " EXEC XUSP_MES_PRODT_ORDER_DETAIL_INFO_GET_ANDROID ";
                sql += "  @PLANT_CD = '" + vPlant_CD + "'";
                sql += " ,@PRODT_ORDER_NO = '" + lbl_ProdtOrderNo_st + "'";
                sql += " ,@OPR_NO = '" + lbl_opr_no_st + "'";
                sql += " ,@WC_CD = '" + lbl_WC_CD_st + "'";
                sql += " ,@FACILITY_CD = ''";
                sql += " ,@WORKER_ID = ''";
                sql += " ,@INSIDE_FLG = 'N'";  //외주공정만 조회

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_check = dba.SendHttpMessage("GetSQLData", pParms);
                //TGSClass.AlertMessage(getApplicationContext(), sJson_check);

                Check_Outsourcing(); //외주공정 체크
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
        }
    }
    /*-------------------------------------------------------------------*/

    /*-------------------------------------------------------------------*/
    public boolean Check_Outsourcing() {
        if (!sJson_check.equals("[]")) {
            try {
                JSONArray ja = new JSONArray(sJson_check);

                if (ja.length() == 1) {
                    JSONObject jObject = ja.getJSONObject(0);

                    lbl_ProdtOrderNo.setText(jObject.getString("PRODT_ORDER_NO")); //제조오더
                    lbl_opr_no.setText(jObject.getString("OPR_NO")); //공순
                    lbl_item_cd.setText(jObject.getString("ITEM_CD")); //품번
                    lbl_item_name.setText(jObject.getString("ITEM_NM")); //품명
                    lbl_TRACKING_NO.setText(jObject.getString("TRACKING_NO")); //TRACKING_NO
                    lbl_ORD_QTY.setText(jObject.getString("PRODT_ORDER_QTY")); //지시량
                    lbl_GOOD_QTY.setText(jObject.getString("GOOD_QTY"));  //양품생산량
                    lbl_BAD_QTY.setText(jObject.getString("BAD_QTY"));  //불량생산량
                    lbl_REMAIN_QTY.setText(jObject.getString("REMAIN_QTY"));  //잔량
                    lbl_WC_CD.setText(jObject.getString("WC_CD"));  //작업장코드
                    lbl_WC_NM.setText(jObject.getString("WC_NM"));  //작업장명
                    lbl_WC_MGR.setText(jObject.getString("WC_MGR"));  //작업장그룹
                    METHOD.setText(jObject.getString("METHOD"));  //작업장그룹

                    method_chk = jObject.getString("METHOD");

                    prodt_order_no_remark = jObject.getString("PRODT_ORDER_NO");
                    opr_no_remark = jObject.getString("OPR_NO");
                } else if (ja.length() > 1) {
                    TGSClass.AlertMessage(getApplicationContext(), "작업장 또는 공정 정보가 없습니다.");
                    return false;
                } else {
                    String lbl_ProdtOrderNo_st = lbl_ProdtOrderNo.getText().toString();
                    String lbl_opr_no_st = lbl_opr_no.getText().toString();

                    MaterialAlertDialogBuilder mAlert = new MaterialAlertDialogBuilder(P14_SAVE_Activity.this);
                    mAlert.setTitle("공정별 실적등록")
                            .setMessage(lbl_ProdtOrderNo_st + " 제조오더의 " + lbl_opr_no_st + " 공정은 \n 외주 공정이 아닙니다.")
                            .setNegativeButton("확인", null)
                            .create().show();

                    return false;
                }

                DBQuery_PRODT_START(prodt_order_no_remark, opr_no_remark);

            } catch (JSONException ex) {

            } catch (Exception e1) {

            }
        } else {
            TGSClass.AlertMessage(getApplicationContext(), "DB 오류");
        }
        return true;
    }
    /*-------------------------------------------------------------------*/


    /*-------------------------------------------------------------------*/
    public void DBQuery_PRODT_START(final String prodt_order_no_remark, final String opr_no_remark) {
        Thread workingThread = new Thread() {
            public void run() {

                String sql = " EXEC XUSP_MES_PRODT_ORDER_DETAIL_START_INFO_GET_ANDROID ";
                sql += " @PRODT_ORDER_NO = '" + prodt_order_no_remark + "'";
                sql += " ,@OPR_NO = '" + opr_no_remark + "'";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_check = dba.SendHttpMessage("GetSQLData", pParms);
                //TGSClass.AlertMessage(getApplicationContext(), sJson_check);

                PRODT_START(); //외주공정 체크
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
        }
    }

    public void PRODT_START() {
        try {
            JSONArray ja = new JSONArray(sJson_check);

            if (method_chk.equals("N")) {
                if (ja.length() > 0) {
                    JSONObject jObject = ja.getJSONObject(0);

                    sResultNo = jObject.getString("RESULT_NO");
                    //String REAL_START_DAY = jObject.getString("REAL_START_DAY");
                    //String REAL_START_TIME = jObject.getString("REAL_START_TIME");
                    //String FACILITY_CD = jObject.getString("FACILITY_CD");
                    //String WORKER_ID = jObject.getString("WORKER_ID");

                    //start_day = findViewById(R.id.start_day);
                    //start_day.setText(jObject.getString("REAL_START_DAY"));
                    //start_time = findViewById(R.id.start_time);
                    //start_time.setText(jObject.getString("REAL_START_TIME"));

                    //btn_start.setVisibility(View.INVISIBLE); //작업 시작
                    //btn_cancle.setVisibility(View.VISIBLE);  //시작 취소
                    //btn_save.setVisibility(View.VISIBLE);    //실적 등록
                    state_message.setText("* 작업 중 *");
                    btn_start.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorGray));
                    btn_cancle.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorBlue));
                    btn_save.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorBlue));


                } else {
                    sResultNo = "";
                    btn_start.setVisibility(View.VISIBLE);
                    /*
                    btn_cancle.setVisibility(View.INVISIBLE);
                    btn_save.setVisibility(View.INVISIBLE);
                     */

                    state_message.setText("* 작업 대기 중 *");
                    btn_start.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorBlue));       //INVISIBLE
                    btn_cancle.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorGray));      //VISIBLE
                    btn_save.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorGray));        //VISIBLE

                }
            } else if (method_chk.equals("Y")) {
                SimpleDateFormat current_start_time = new SimpleDateFormat("08:00");
                long now = System.currentTimeMillis();
                start_time.setText(current_start_time.format(new Time(now)));

                SimpleDateFormat current_end_time = new SimpleDateFormat("20:00");
                final EditText end_time = (EditText) findViewById(R.id.end_time);
                end_time.setText(current_end_time.format(new Time(now)));

                /*
                btn_start.setVisibility(View.INVISIBLE);
                btn_cancle.setVisibility(View.INVISIBLE);
                btn_save.setVisibility(View.VISIBLE);
                 */
                state_message.setText("* 공정 실적 등록(실적버튼) *");

                btn_start.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorGray));       //INVISIBLE
                btn_cancle.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorGray));      //INVISIBLE
                btn_save.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorBlue));        //VISIBLE
            }

        } catch (JSONException ex) {
            TGSClass.AlertMessage(getApplicationContext(), ex.getMessage());
        } catch (Exception e1) {
            TGSClass.AlertMessage(getApplicationContext(), e1.getMessage());
        }
    }

    /*-------------------------------------------------------------------*/
    /*-------------------------------------------------------------------*/
    //실적등록 버튼
    public void DBquery_CHK_RESERVATION() {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession) getApplication(); //전역 클래스

                String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");

                if (vPlant_CD == null)
                    vPlant_CD = global.getPlantCDString();

                String lbl_ProdtOrderNo_st = lbl_ProdtOrderNo.getText().toString();
                String lbl_opr_no_st = lbl_opr_no.getText().toString();

                String sql = " EXEC XUSP_MES_APP_PH5007MA1_GET1 ";
                sql += "  @PLANT_CD = '" + vPlant_CD + "'";
                sql += " ,@PRODT_ORDER_NO = '" + lbl_ProdtOrderNo_st + "'";
                sql += " ,@OPR_NO = '" + lbl_opr_no_st + "'";
                sql += " ,@ISSUE_MTHD = ''";

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_check = dba.SendHttpMessage("GetSQLData", pParms);
                //TGSClass.AlertMessage(getApplicationContext(), sJson_check);

                CHK_RESERVATION();        //외주공정 체크
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
        }
    }

    public boolean CHK_RESERVATION() {
        if (!sJson_check.equals("[]")) {
            try {
                JSONArray ja = new JSONArray(sJson_check);

                for (int idx = 0; idx < ja.length(); idx++) {
                    JSONObject jObject = ja.getJSONObject(idx);

                    int sOUT_QTY = jObject.getInt("OUT_QTY"); //출고수량

                    if (sOUT_QTY == 0) {
                        return false;
                    }
                }
            } catch (JSONException ex) {

            } catch (Exception e1) {

            }
        } else {
            TGSClass.AlertMessage(getApplicationContext(), "DB 오류");
        }
        return true;
    }
    /*-------------------------------------------------------------------*/

    /*-----------------------------[ DBSave ]--------------------------------------*/
    /*--------------------[ DBSave_Work_SaveLotResult ]--------------------------------------*/
    /*---------------------------[ FncBL_GET_SQL ]--------------------------------------*/
    public boolean DBQuery_GET_BL(final String cud_flag, final String plant_cd, final String report_type, final String seq, final String prodt_order_no,
                                  final String opr_no, final String qty, final String b_type_cd, final String qty_in, final String report_dt) {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession) getApplication(); //전역 클래스

                String vUnit_CD = getIntent().getStringExtra("pUNIT_CD");

                if (vUnit_CD == null)
                    vUnit_CD = global.getUnitCDString();

                String cud_flag_parm = cud_flag;
                String plant_cd_parm = plant_cd;
                String report_type_parm = report_type;
                String seq_parm = seq;
                String prodt_order_no_parm = prodt_order_no;
                String opr_no_parm = opr_no;
                String qty_parm = qty;
                String b_type_cd_parm = b_type_cd;
                String qty_in_parm = qty_in;
                String report_dt_parm = report_dt;
                String unit_cd_parm = vUnit_CD;

                String wk_id = lbl_WORKER_CD.getText().toString();

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);
                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("cud_flag");
                parm.setValue(cud_flag_parm);
                parm.setType(String.class);

                PropertyInfo parm2 = new PropertyInfo();
                parm2.setName("plant_cd");
                parm2.setValue(plant_cd_parm);
                parm2.setType(String.class);

                PropertyInfo parm3 = new PropertyInfo();
                parm3.setName("report_type");
                parm3.setValue(report_type_parm);
                parm3.setType(String.class);

                PropertyInfo parm4 = new PropertyInfo();
                parm4.setName("seq_parm");
                parm4.setValue(seq_parm);
                parm4.setType(String.class);

                PropertyInfo parm5 = new PropertyInfo();
                parm5.setName("prodt_order_no");
                parm5.setValue(prodt_order_no_parm);
                parm5.setType(String.class);

                PropertyInfo parm6 = new PropertyInfo();
                parm6.setName("opr_no");
                parm6.setValue(opr_no_parm);
                parm6.setType(String.class);

                PropertyInfo parm7 = new PropertyInfo();
                parm7.setName("qty");
                parm7.setValue(qty_parm);
                parm7.setType(String.class);

                PropertyInfo parm8 = new PropertyInfo();
                parm8.setName("b_type_cd");
                parm8.setValue(b_type_cd_parm);
                parm8.setType(Integer.class);

                PropertyInfo parm9 = new PropertyInfo();
                parm9.setName("qty_in");
                parm9.setValue(qty_in_parm);
                parm9.setType(String.class);

                PropertyInfo parm10 = new PropertyInfo();
                parm10.setName("report_dt");
                parm10.setValue(report_dt_parm);
                parm10.setType(String.class);

                PropertyInfo parm11 = new PropertyInfo();
                parm11.setName("wk_id");
                parm11.setValue(wk_id);
                parm11.setType(String.class);

                PropertyInfo parm12 = new PropertyInfo();
                parm12.setName("unit_cd");
                parm12.setValue(unit_cd_parm);
                parm12.setType(String.class);

                pParms.add(parm);
                pParms.add(parm2);
                pParms.add(parm3);
                pParms.add(parm4);
                pParms.add(parm5);
                pParms.add(parm6);
                pParms.add(parm7);
                pParms.add(parm8);
                pParms.add(parm9);
                pParms.add(parm10);
                pParms.add(parm11);
                pParms.add(parm12);

                result_msg = dba.SendHttpMessage("BL_SetProductionResult_ANDROID", pParms);
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {

        }
        return true;
    }
    /*--------------------------------------------------------------------------------*/

    /*-----------------------------[ CloseWork ]--------------------------------------*/
    public boolean DBQuery_CloseWork(final int pGoodQty, final String pResultFlag, final String pEnddt) {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession) getApplication(); //전역 클래스

                String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");
                String vLoginID = getIntent().getStringExtra("pUser_ID");

                if (vPlant_CD == null)
                    vPlant_CD = global.getPlantCDString();

                if (vLoginID == null)
                    vLoginID = global.getLoginIDString();

                String lbl_ProdtOrderNo_st = lbl_ProdtOrderNo.getText().toString();
                String lbl_opr_no_st = lbl_opr_no.getText().toString();

                String sql = " EXEC XUSP_APP_P2002MA1_SET ";
                sql += "  @CUD_FLAG = 'U'";
                sql += " ,@PLANT_CD = '" + vPlant_CD + "'";
                sql += " ,@PRODT_ORDER_NO = '" + lbl_ProdtOrderNo_st + "'";
                sql += " ,@OPR_NO = '" + lbl_opr_no_st + "'";
                sql += " ,@GOOD_QTY = " + pGoodQty;
                sql += " ,@RESULT_NO = '" + sResultNo + "'";
                sql += " ,@RESULT_FLAG = '" + pResultFlag + "'";
                sql += " ,@REAL_END_DT = '" + pEnddt + "'";
                sql += " ,@UPDATE_DT = ''";
                sql += " ,@USER_ID = '" + vLoginID + "'";

                prodt_order_no_query = lbl_ProdtOrderNo_st;
                opr_no_query = lbl_opr_no_st;

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_CloseWork = dba.SendHttpMessage("GetSQLData", pParms);  //정영진
                //TGSClass.AlertMessage(getApplicationContext(), sJson_StartWork);

            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            return false;
        }
        return true;
    }

    /*-------------------------------------------------------------------*/
    public boolean DBQuery_DeleteWork() {
        Thread workingThread = new Thread() {
            public void run() {

                global = (MySession) getApplication(); //전역 클래스

                String vPlant_CD = getIntent().getStringExtra("pPLANT_CD");

                if (vPlant_CD == null)
                    vPlant_CD = global.getPlantCDString();

                String lbl_ProdtOrderNo_st = lbl_ProdtOrderNo.getText().toString();
                String lbl_opr_no_st = lbl_opr_no.getText().toString();

                String sql = " EXEC XUSP_APP_P2002MA1_SET ";
                sql += "  @CUD_FLAG = 'D'";
                sql += " ,@PLANT_CD = '" + vPlant_CD + "'";
                sql += " ,@PRODT_ORDER_NO = '" + lbl_ProdtOrderNo_st + "'";
                sql += " ,@OPR_NO = '" + lbl_opr_no_st + "'";
                sql += " ,@RESULT_NO = '" + sResultNo + "'";

                prodt_order_no_query = lbl_ProdtOrderNo_st;
                opr_no_query = lbl_opr_no_st;

                DBAccess dba = new DBAccess(TGSClass.ws_name_space, TGSClass.ws_url);

                ArrayList<PropertyInfo> pParms = new ArrayList<>();

                PropertyInfo parm = new PropertyInfo();
                parm.setName("pSQL_Command");
                parm.setValue(sql);
                parm.setType(String.class);

                pParms.add(parm);

                sJson_StartWork = dba.SendHttpMessage("GetSQLData", pParms);
                //TGSClass.AlertMessage(getApplicationContext(), sJson_StartWork);
            }
        };
        workingThread.start();   //스레드 시작
        try {
            workingThread.join();  //workingThread가 종료될때까지 Main 쓰레드를 정지함.

        } catch (InterruptedException ex) {
            return false;
        }
        return true;
    }
    /*----------------------------------------------------------------------------------*/
}


