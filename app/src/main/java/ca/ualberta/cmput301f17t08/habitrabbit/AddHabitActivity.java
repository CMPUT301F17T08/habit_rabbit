package ca.ualberta.cmput301f17t08.habitrabbit;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AddHabitActivity extends AppCompatActivity {
    private AddHabitActivity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_habit);

        final EditText habitTitle = findViewById(R.id.habit_title_field);
        final EditText habitReason = findViewById(R.id.habit_reason_field);
        final EditText dateSelector = findViewById(R.id.habit_date_selector);
        Button addHabitButton = findViewById(R.id.add_habit_button);

        // Date Picker (Source: https://goo.gl/nmN56M)
        final SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy");
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Edmonton"));

        // set the habit start date to the current date
        dateSelector.setText(format.format(calendar.getTime()));

        // date picker
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // update the editText label
                dateSelector.setText(format.format(calendar.getTime()));
            }
        };
        // when the date field is clicked on the add habit page
        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePicker = new DatePickerDialog(AddHabitActivity.this, R.style.date_picker, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                // ensure that a previous date can't be selected
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePicker.show();
            }
        });

        // submit button
        addHabitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String title = habitTitle.getText().toString();
                String reason = habitReason.getText().toString();
                Date d = calendar.getTime();

                // input error checking
                if (title.length() > 20){
                    habitTitle.setError("Please keep title under 20 characters");
                }
                else if (title.length() == 0){
                    habitTitle.setError("Please enter a title");
                }

                if (reason.length() > 30){
                    habitReason.setError("Please keep reason under 30 characters");
                }
                else if (reason.length() == 0){
                    habitReason.setError("Please enter a reason");
                }

                // TODO create a new habit object here and associate that with the user
            }
        });



    }

}
