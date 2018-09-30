package com.example.brandonmain.listviewexample1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.DatePicker;
import android.widget.TextView;

/**We create our Class DatePicker extending of DialogFragment to display a selectable date calendar */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    DataSource dataSource;
    public String formattedDate;/**This type String object will store the date value converted to string*/

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /** the current date as the default date in the picker*/
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        /** Create a new instance of DatePickerDialog and return it*/
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    /**We set the date with onDateSet*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        /**We change from date format to String format in the yyyy-MM-dd standard form*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = sdf.format(c.getTime());
        /**We set the formattedDate(date in String form) to the EditDate so we have our date to insert in the item*/
        ( (TextView) getActivity().findViewById(R.id.editDate)).setText(formattedDate);
    }

}