package com.lzjs.uappoint.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.lzjs.uappoint.R;

import java.lang.reflect.Field;

/**
 * 时间选择控件
 * @author shalei
 */
public class TimePickDialogUtil extends AlertDialog implements DialogInterface.OnClickListener, OnTimeChangedListener {

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";

    private final TimePicker mTimePicker_start;
    private final OnTimeSetListener mCallBack;


    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        System.out.println("h:" + hourOfDay + " m:" + minute);
    }


    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnTimeSetListener {

        void onTimeSet(TimePicker startTimePicker, int hour, int minute);
    }

    /**
     * @param context
     *            The context the dialog is to run in.
     * @param theme
     *            the theme to apply to this dialog
     * @param callBack
     *            How the parent is notified that the date is set.
     * @param hh
     *            The initial hour of the dialog.
     * @param mm
     *            The initial minute of the dialog.
     */
    public TimePickDialogUtil(Context context, int theme, OnTimeSetListener callBack, int hh, int mm,
                               boolean isDayVisible) {
        super(context, theme);

        mCallBack = callBack;

        Context themeContext = getContext();
        setButton(BUTTON_POSITIVE, "确 定", this);
        setButton(BUTTON_NEGATIVE, "取 消", this);
        // setButton(BUTTON_POSITIVE,
        // themeContext.getText(android.R.string.date_time_done), this);
        setIcon(0);

        LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.common_time, null);
        setView(view);

        mTimePicker_start = (TimePicker) view.findViewById(R.id.timePicker);
        //是否使用24小时制
        mTimePicker_start.setIs24HourView(true);

        int h = mTimePicker_start.getCurrentHour();
        int m = mTimePicker_start.getCurrentMinute();
        System.out.println("h:" + h + "   m:"+m);

        // 如果要隐藏当前时间，则使用下面方法。
        if (!isDayVisible) {
            hidDay(mTimePicker_start);
        }
    }

    /**
     * 隐藏TimePicker中的时间显示
     *
     * @param mTimePicker
     */
    private void hidDay(TimePicker mTimePicker) {
        Field[] datePickerfFields = mTimePicker.getClass().getDeclaredFields();
        for (Field datePickerField : datePickerfFields) {
            if ("mDaySpinner".equals(datePickerField.getName())) {
                datePickerField.setAccessible(true);
                Object dayPicker = new Object();
                try {
                    dayPicker = datePickerField.get(mTimePicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                // datePicker.getCalendarView().setVisibility(View.GONE);
                ((View) dayPicker).setVisibility(View.GONE);
            }
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        // Log.d(this.getClass().getSimpleName(), String.format("which:%d",
        // which));
        // 如果是“取 消”按钮，则返回，如果是“确 定”按钮，则往下执行
        if (which == BUTTON_POSITIVE)
            tryNotifyDateSet();
    }

    /*@Override
    public void onDateChanged(TimePicker view, int hh, int mm,  int ss) {
        if (view.getId() == R.id.datePicker)
            mTimePicker_start.init(hh, mm, ss, this);
    }*/

    /**
     * 获得时间的TimePicker
     *
     * @return The calendar view.
     */
    public TimePicker getTimePickerStart() {
        return mTimePicker_start;
    }

   /* public void updateStartTime(int hh, int mm) {
        mTimePicker_start.updateTime(hh, mm);
    }*/


    private void tryNotifyDateSet() {
        if (mCallBack != null) {
            mTimePicker_start.clearFocus();
            mCallBack.onTimeSet(mTimePicker_start, mTimePicker_start.getCurrentHour(), mTimePicker_start.getCurrentMinute());
        }
    }

    @Override
    protected void onStop() {
        // tryNotifyDateSet();
        super.onStop();
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, mTimePicker_start.getCurrentHour());
        state.putInt(MINUTE, mTimePicker_start.getCurrentMinute());
        return state;
    }

}