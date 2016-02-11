package maikcaru.yourbin;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class BinScheduler extends NavigationDrawerParent {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin_scheduler);
        createToolbar();

        Object[][] objects = new Object[2][2];

        objects[0][0] = R.id.day_of_week_spinner;
        objects[0][1] = R.array.days_array;
        objects[1][0] = R.id.frequency_spinner;
        objects[1][1] = R.array.frequency_array;


        Spinner daysSpinner = (Spinner) findViewById(R.id.day_of_week_spinner);
        ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter.createFromResource(this,
                R.array.days_array, android.R.layout.simple_spinner_item);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysSpinner.setAdapter(daysAdapter);


        Spinner freqSpinner = (Spinner) findViewById(R.id.frequency_spinner);
        ArrayAdapter<CharSequence> freqAdapter = ArrayAdapter.createFromResource(this,
                R.array.frequency_array, android.R.layout.simple_spinner_item);
        freqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        freqSpinner.setAdapter(freqAdapter);

        TextView reminderTime = (TextView) findViewById(R.id.textReminderTime);
        reminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getFragmentManager(), "TimePicker");
            }
        });


    }


    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current time as the default values for the time picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //Do something with the user chosen time
            //Get reference of host activity (XML Layout File) TextView widget
            String halfOfDay = "am";
            TextView reminderTime = (TextView) getActivity().findViewById(R.id.textReminderTime);

            if (hourOfDay > 12) {
                hourOfDay %= 12;
                halfOfDay = "pm";
            }

            reminderTime.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute) + " " + halfOfDay);
        }
    }
}