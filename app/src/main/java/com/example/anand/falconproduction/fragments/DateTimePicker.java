package com.example.anand.falconproduction.fragments;

import java.util.Calendar;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;

import com.example.anand.falconproduction.R;

public class DateTimePicker extends RelativeLayout implements View.OnClickListener {

  // DatePicker reference
  private DatePicker              datePicker;
  // TimePicker reference
  private TimePicker              timePicker;
  // ViewSwitcher reference
  private ViewSwitcher    viewSwitcher;
  // Calendar reference
  private Calendar                mCalendar;

  // Constructor start
  public DateTimePicker(Context context) {
    this(context, null);
  }

  public DateTimePicker(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DateTimePicker(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    Log.d("dateTimePicker", "DateTimePicker called");
    // Get LayoutInflater instance
    final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    inflater.inflate(R.layout.datetimepicker, this, true);

    final LinearLayout datePickerView = (LinearLayout) inflater.inflate(R.layout.datepicker, null);
    final LinearLayout timePickerView = (LinearLayout) inflater.inflate(R.layout.timepicker, null);

    // Grab a Calendar instance
    mCalendar = Calendar.getInstance();

    viewSwitcher = (ViewSwitcher) this.findViewById(R.id.DateTimePickerVS);

    // Init date picker
    datePicker = (DatePicker) datePickerView.findViewById(R.id.DatePicker);
    datePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {
      @Override
      public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d("dTimePick", "Date changed triggred " + dayOfMonth);
        mCalendar.set(year, monthOfYear, dayOfMonth, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE));
      }
    });


    // Init time picker
    timePicker = (TimePicker) timePickerView.findViewById(R.id.TimePicker);
    timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
      @Override
      public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        Log.d("dTimePick", "Time changed triggred " + hourOfDay);
        mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
      }
    });

    // Handle button clicks
    (findViewById(R.id.SwitchToTime)).setOnClickListener(this); // shows the time picker
    (findViewById(R.id.SwitchToDate)).setOnClickListener(this); // shows the date picker

    // Populate ViewSwitcher
    viewSwitcher.addView(datePickerView, 0);
    viewSwitcher.addView(timePickerView, 1);
  }
  // Constructor end

  // Handle button clicks
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.SwitchToDate:
        v.setEnabled(false);
        findViewById(R.id.SwitchToTime).setEnabled(true);
        viewSwitcher.showPrevious();
        break;

      case R.id.SwitchToTime:
        v.setEnabled(false);
        findViewById(R.id.SwitchToDate).setEnabled(true);
        viewSwitcher.showNext();
        break;
    }
  }

  // Convenience wrapper for internal Calendar instance
  public int get(final int field) {
    return mCalendar.get(field);
  }

  public Calendar get() {
    mCalendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
    return mCalendar;
  }

  // Reset DatePicker, TimePicker and internal Calendar instance
  public void reset() {
    final Calendar c = Calendar.getInstance();
    updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    updateTime(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
  }

  // Convenience wrapper for internal DatePicker instance
  public void updateDate(int year, int monthOfYear, int dayOfMonth) {
    datePicker.updateDate(year, monthOfYear, dayOfMonth);
  }

  // Convenience wrapper for internal TimePicker instance
  public void updateTime(int currentHour, int currentMinute) {
    timePicker.setCurrentHour(currentHour);
    timePicker.setCurrentMinute(currentMinute);
  }
}

