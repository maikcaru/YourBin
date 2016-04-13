package maikcaru.yourbin;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BinScheduler extends NavigationDrawerParent {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin_scheduler);
        createToolbar();

        prefs = getSharedPreferences("maikcaru.yourbin", Context.MODE_PRIVATE);

        //Add spinners and spinner values to array
        int[][] IDs = new int[2][2];
        IDs[0][0] = R.id.day_of_week_spinner;
        IDs[0][1] = R.array.days_array;
        IDs[1][0] = R.id.frequency_spinner;
        IDs[1][1] = R.array.frequency_array;

        //Loop twice to add both spinners using IDs array.
        //J is incremented in the loop in order to access the other variable.
        for (int[] ID : IDs) {
            int j = 0;
            Spinner spinner = (Spinner) findViewById(ID[j]);
            j++;
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    ID[j], android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

        final Spinner daySpinner = (Spinner) findViewById(IDs[0][0]);
        daySpinner.setSelection(prefs.getInt("dayOfWeek", -1));
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putInt("dayOfWeek", position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final Spinner frequencySpinner = (Spinner) findViewById(IDs[1][0]);
        frequencySpinner.setSelection(prefs.getInt("frequency", -1));
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putInt("frequency", position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Switch repeatSwitch = (Switch) findViewById(R.id.repeatSwitch);
        repeatSwitch.setChecked(prefs.getBoolean("repeat", false));
        repeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("repeat", isChecked).apply();
            }
        });


        TextView tvReminderTime = (TextView) findViewById(R.id.textReminderTime);
        String time = String.format("%02d", prefs.getInt("hour", 22)) + ":" + String.format("%02d", prefs.getInt("minute", 00));
        time = get12HourTime(time);
        tvReminderTime.setText(time);
        tvReminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getFragmentManager(), "TimePicker");
            }
        });


    }

    public String get12HourTime(String time) {

        if (!DateFormat.is24HourFormat(this)) {
            try {
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                // Log.e("24 SDF", _24HourSDF.toLocalizedPattern());
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                // Log.e("12 SDF", _12HourSDF.toLocalizedPattern());
                Date _24HourDt = _24HourSDF.parse(time);
                // Log.e("24 DT", _24HourDt.toString());
                time = _12HourSDF.format(_24HourDt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return time;
    }

    public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = prefs.getInt("hour", 22);
            int minute = prefs.getInt("minute", 00);
            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //Do something with the user chosen time
            //Get reference of host activity (XML Layout File) TextView widget

            TextView tvReminderTime = (TextView) getActivity().findViewById(R.id.textReminderTime);
            String time = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
            time = get12HourTime(time);

            prefs.edit().putInt("hour", hourOfDay).apply();
            prefs.edit().putInt("minute", minute).apply();
            prefs.edit().putString("reminderTimeString", time).apply();
            tvReminderTime.setText(time);
        }

    }


}