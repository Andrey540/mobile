package ru.aegoshin.daterangecalendar;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.aegoshin.daterangecalendar.listeners.DialogCompleteListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by psinetron on 03/12/2018.
 * http://slybeaver.ru
 */
public class DateRangeCalendarDialog extends DialogFragment implements DialogCompleteListener {


    private DateRangeCalendarData slyCalendarData = new DateRangeCalendarData();
    private Callback callback = null;

    public DateRangeCalendarDialog setStartDate(@Nullable Date startDate) {
        slyCalendarData.setSelectedStartDate(startDate);
        return this;
    }

    public DateRangeCalendarDialog setEndDate(@Nullable Date endDate) {
        slyCalendarData.setSelectedEndDate(endDate);
        return this;
    }

    public DateRangeCalendarDialog setSingle(boolean single) {
        slyCalendarData.setSingle(single);
        return this;
    }

    public DateRangeCalendarDialog setFirstMonday(boolean firsMonday) {
        slyCalendarData.setFirstMonday(firsMonday);
        return this;
    }

    public DateRangeCalendarDialog setCallback(@Nullable Callback callback) {
        this.callback = callback;
        return this;
    }

    public DateRangeCalendarDialog setTimeTheme(@Nullable Integer themeResource) {
        slyCalendarData.setTimeTheme(themeResource);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SlyCalendarDialogStyle);
    }

    @Nullable
    public Date getCalendarFirstDate() {
        return slyCalendarData.getSelectedStartDate();
    }

    @Nullable
    public Date getCalendarSecondDate() {
        return slyCalendarData.getSelectedEndDate();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DateRangeCalendarView calendarView = (DateRangeCalendarView) getActivity().getLayoutInflater().inflate(R.layout.slycalendar_main, container);
        calendarView.setSlyCalendarData(slyCalendarData);
        calendarView.setCallback(callback);
        calendarView.setCompleteListener(this);
        return calendarView;
    }

    @Override
    public void complete() {
        this.dismiss();
    }


    public interface Callback {
        void onCancelled();

        void onDataSelected(Calendar firstDate, Calendar secondDate);
    }


    public DateRangeCalendarDialog setBackgroundColor(Integer backgroundColor) {
        slyCalendarData.setBackgroundColor(backgroundColor);
        return this;
    }

    public DateRangeCalendarDialog setHeaderColor(Integer headerColor) {
        slyCalendarData.setHeaderColor(headerColor);
        return this;
    }

    public DateRangeCalendarDialog setHeaderTextColor(Integer headerTextColor) {
        slyCalendarData.setHeaderTextColor(headerTextColor);
        return this;
    }

    public DateRangeCalendarDialog setTextColor(Integer textColor) {
        slyCalendarData.setTextColor(textColor);
        return this;
    }

    public DateRangeCalendarDialog setSelectedColor(Integer selectedColor) {
        slyCalendarData.setSelectedColor(selectedColor);
        return this;
    }

    public DateRangeCalendarDialog setSelectedTextColor(Integer selectedTextColor) {
        slyCalendarData.setSelectedTextColor(selectedTextColor);
        return this;
    }


}
