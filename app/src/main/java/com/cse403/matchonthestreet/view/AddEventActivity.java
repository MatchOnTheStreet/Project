package com.cse403.matchonthestreet.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cse403.matchonthestreet.R;
import com.cse403.matchonthestreet.backend.DBManager;
import com.cse403.matchonthestreet.controller.MOTSApp;
import com.cse403.matchonthestreet.controller.ViewController;
import com.cse403.matchonthestreet.models.Account;
import com.cse403.matchonthestreet.models.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by larioj on 2/7/16.
 *
 * This view is displayed when the user wants to add an event. It has
 * elements that allow the user to enter the event information.
 */
public class AddEventActivity extends NavActivity implements OnClickListener {

    private EditText fromDateET;
    private DatePickerDialog fromDatePD;
    private EditText fromTimeET;
    private TimePickerDialog fromTimePD;
    
    private EditText durationET;
    private NumberPicker durationPicker;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private Calendar calendar;

    private SeekBar seekBar;

    private Location location;

    /*
     * Intializes the various items in the view.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // get location from the intent
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("latitude", 0.0);
        double lon = intent.getDoubleExtra("longitude", 0.0);

        location = new Location("userCreatedLocation");
        location.setLatitude(lat);
        location.setLongitude(lon);


        // We will need to format the date later.
        calendar  = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yy", Locale.US);

        // setup the date box, and register the handler.
        fromDateET = (EditText) findViewById(R.id.event_from_date);
        fromDateET.setInputType(InputType.TYPE_NULL);
        fromDateET.setOnClickListener(this);
        fromDatePD = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                fromDateET.setText(dateFormatter.format(date.getTime()));
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));


        // setup the time box, and register the handler for when the user attempts to
        // enter time.
        fromTimeET = (EditText) findViewById(R.id.event_from_time);
        fromTimeET.setInputType(InputType.TYPE_NULL);
        fromTimeET.setOnClickListener(this);
        fromTimePD = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String pad = "";
                if (minute < 10) pad = "0";
                String time = hourOfDay + ":" + pad + minute;
                fromTimeET.setText(time);
            }
        },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);

        durationET = (EditText) findViewById(R.id.durationET);

       // durationPicker = (NumberPicker) findViewById(R.id.durationPicker);
        final String[] timeValues = {"1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5", "5.5", "6", "6.5", "7", "7.5", "8", "8.5", "9"};
//        durationPicker.setDisplayedValues(timeValues);
//        durationPicker.setMinValue(0);
//        durationPicker.setMaxValue(10);
//        durationPicker.setOnClickListener(this);
//        durationPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                durationET.setText(timeValues[newVal]);
//            }
//        });


        // seekbar for picking the duration.
        seekBar = (SeekBar) findViewById(R.id.duration_seek_bar);
        seekBar.setMax(timeValues.length - 1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                durationET.setText(timeValues[progress]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    /*
     * handles the click events on the date and time elements.
     */
    @Override
    public void onClick(View v) {
        if (v == fromDateET) {
            fromDatePD.show();
        } else if (v == fromTimeET) {
            fromTimePD.show();
        }
    }

    /*
     * Handles the creation of an activity.
     */
    public void createEvent(View view) {
        Log.d("AddEventActivity", "createEvent running");
        //TODO: Check that all the fields were filled out

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        EditText titleET = (EditText)findViewById(R.id.event_title);
        EditText eventDescET = (EditText)findViewById(R.id.event_description);

        if (fromDateET.getText().toString().matches("") || fromTimeET.getText().toString().matches("") ||
                durationET.getText().toString().matches("") || titleET.getText().toString().matches("")
                || eventDescET.getText().toString().matches("")) {
            Log.d("AddEventActivity", "incomplete fields");
            Toast incompleteToast = Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT);
            incompleteToast.show();
            return;
        }

        String timerange = fromDateET.getText().toString() + " " + fromTimeET.getText().toString();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm", Locale.US);
        Date date;
        try {
            date = format.parse(timerange);

        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
            Log.d("AddEventActivity", "Date parse failed");
        }
        Log.d("AddEventActivity", "Date toString is: " + date.toString());



        String title = titleET.getText().toString();


        String description = eventDescET.getText().toString();


        float duration = Float.parseFloat(durationET.getText().toString()) * 60;

        Event event = new Event(title, location, date, (int)duration, description);

        // Adds this newly added event to ViewController
        // So that it appears in the list view as well
        ViewController viewController = ((MOTSApp)getApplicationContext()).getViewController();
        viewController.addEventToSet(event);

        Intent resultIntent = new Intent(); 

        Log.d("AddEventActivity", "Date toString is: " + event.getTime().getTime());

        Account me = ((MOTSApp) getApplication()).getMyAccount();
        event.addAttendee(me);

        ArrayList<Event> list = new ArrayList<>();
        list.add(event);

        AsyncTask<Event, Event, Event> task = new AsyncTask<Event, Event, Event>() {
            @Override
            protected Event doInBackground(Event[] params) {
                try {
                    Event e = params[0];
                    if (e != null) {
                        DBManager.addEventWithAttendance(e);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        Event[] events = new Event[1];
        events[0] = event;

        task.execute(events);


        resultIntent.putParcelableArrayListExtra("eventList", list);

        setResult(RESULT_OK, resultIntent);

        finish();

    }

}