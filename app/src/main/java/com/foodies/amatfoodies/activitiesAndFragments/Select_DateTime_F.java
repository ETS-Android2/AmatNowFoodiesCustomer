package com.foodies.amatfoodies.activitiesAndFragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.foodies.amatfoodies.constants.Fragment_Callback;
import com.foodies.amatfoodies.R;
import com.foodies.amatfoodies.utils.relateToFragment_OnBack.RootFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Select_DateTime_F extends RootFragment implements View.OnClickListener {

    View view;
    Context context;

    TextView date_txt,time_txt;

    public Select_DateTime_F() {

    }

    Fragment_Callback fragment_callback;
    public Select_DateTime_F(Fragment_Callback fragment_callback) {
        this.fragment_callback=fragment_callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_select_date_time, container, false);
        context=getContext();

        view.findViewById(R.id.Goback).setOnClickListener(this::onClick);

        date_txt=view.findViewById(R.id.date_txt);
        time_txt=view.findViewById(R.id.time_txt);


        view.findViewById(R.id.btn_edit_date).setOnClickListener(this::onClick);
        view.findViewById(R.id.btn_edit_time).setOnClickListener(this::onClick);
        view.findViewById(R.id.continue_btn).setOnClickListener(this::onClick);




        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+45);

        SimpleDateFormat timeformat= new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        time_txt.setText(timeformat.format(calendar.getTime()));

        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        date_txt.setText(format.format(calendar.getTime()));

        return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Goback:
                getActivity().onBackPressed();
                break;

            case R.id.btn_edit_date:
                Opendate_picker();
                break;

            case R.id.btn_edit_time:
                opentimepicker();
                break;

            case R.id.continue_btn:
                if(fragment_callback!=null) {
                    Bundle bundle=new Bundle();
                    bundle.putString("datetime",date_txt.getText()+" "+time_txt.getText());
                    fragment_callback.Responce(bundle);
                    getActivity().onBackPressed();
                }
                break;
        }
    }


    public  void Opendate_picker(){
        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog mdiDialog =new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(0);
                cal.set(year, monthOfYear, dayOfMonth);
                Date chosenDate = cal.getTime();
                SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                date_txt.setText(format.format(chosenDate));

            }
        }, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
        mdiDialog.show();
    }



    public void opentimepicker(){
        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat timeformat= new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                time_txt.setText(timeformat.format(calendar.getTime()));

            }
        }, 12, 00, false).show();
    }
}
