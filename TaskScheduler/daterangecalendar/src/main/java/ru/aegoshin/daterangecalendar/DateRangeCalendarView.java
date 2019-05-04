package ru.aegoshin.daterangecalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import ru.aegoshin.daterangecalendar.listeners.DateSelectListener;
import ru.aegoshin.daterangecalendar.listeners.DialogCompleteListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by psinetron on 29/11/2018.
 * http://slybeaver.ru
 */
public class DateRangeCalendarView extends FrameLayout implements DateSelectListener {

    private DateRangeCalendarData slyCalendarData;

    private DateRangeCalendarDialog.Callback callback = null;

    private DialogCompleteListener completeListener = null;

    private AttributeSet attrs = null;
    private int defStyleAttr = 0;


    public DateRangeCalendarView(Context context) {
        super(context);
        init(null, 0);
    }

    public DateRangeCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
    }

    public DateRangeCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        this.defStyleAttr = defStyleAttr;

    }

    public void setCallback(@Nullable DateRangeCalendarDialog.Callback callback) {
        this.callback = callback;
    }

    public void setCompleteListener(@Nullable DialogCompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    public void setSlyCalendarData(DateRangeCalendarData slyCalendarData) {
        this.slyCalendarData = slyCalendarData;
        init(attrs, defStyleAttr);
        showCalendar();
    }

    private void init(@Nullable AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.slycalendar_frame, this);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DateRangeCalendarView, defStyle, 0);

        if (slyCalendarData.getBackgroundColor() == null) {
            slyCalendarData.setBackgroundColor(typedArray.getColor(R.styleable.DateRangeCalendarView_backgroundColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defBackgroundColor)));
        }
        if (slyCalendarData.getHeaderColor() == null) {
            slyCalendarData.setHeaderColor(typedArray.getColor(R.styleable.DateRangeCalendarView_headerColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defHeaderColor)));
        }
        if (slyCalendarData.getHeaderTextColor() == null) {
            slyCalendarData.setHeaderTextColor(typedArray.getColor(R.styleable.DateRangeCalendarView_headerTextColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defHeaderTextColor)));
        }
        if (slyCalendarData.getTextColor() == null) {
            slyCalendarData.setTextColor(typedArray.getColor(R.styleable.DateRangeCalendarView_textColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defTextColor)));
        }
        if (slyCalendarData.getSelectedColor() == null) {
            slyCalendarData.setSelectedColor(typedArray.getColor(R.styleable.DateRangeCalendarView_selectedColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defSelectedColor)));
        }
        if (slyCalendarData.getSelectedTextColor() == null) {
            slyCalendarData.setSelectedTextColor(typedArray.getColor(R.styleable.DateRangeCalendarView_selectedTextColor, ContextCompat.getColor(getContext(), R.color.slycalendar_defSelectedTextColor)));
        }

        typedArray.recycle();

        final ViewPager vpager = findViewById(R.id.content);
        vpager.setAdapter(new MonthPagerAdapter(slyCalendarData, this));
        vpager.setCurrentItem(vpager.getAdapter().getCount() / 2);

        showCalendar();
    }

    private void showCalendar() {

        paintCalendar();

        findViewById(R.id.txtCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onCancelled();
                }
                if (completeListener != null) {
                    completeListener.complete();
                }
            }
        });

        findViewById(R.id.txtSave).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    Calendar start = null;
                    Calendar end = null;
                    if (slyCalendarData.getSelectedStartDate() != null) {
                        start = Calendar.getInstance();
                        start.setTime(slyCalendarData.getSelectedStartDate());
                    }
                    if (slyCalendarData.getSelectedEndDate() != null) {
                        end = Calendar.getInstance();
                        end.setTime(slyCalendarData.getSelectedEndDate());
                    }
                    callback.onDataSelected(start, end);
                }
                if (completeListener != null) {
                    completeListener.complete();
                }
            }
        });


        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = null;
        if (slyCalendarData.getSelectedStartDate() != null) {
            calendarStart.setTime(slyCalendarData.getSelectedStartDate());
        } else {
            calendarStart.setTime(slyCalendarData.getShowDate());
        }

        if (slyCalendarData.getSelectedEndDate() != null) {
            calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(slyCalendarData.getSelectedEndDate());
        }

        ((TextView) findViewById(R.id.txtYear)).setText(String.valueOf(calendarStart.get(Calendar.YEAR)));


        if (calendarEnd == null) {
            ((TextView) findViewById(R.id.txtSelectedPeriod)).setText(
                    new SimpleDateFormat("EE, dd MMMM", Locale.getDefault()).format(calendarStart.getTime())
            );
        } else {
            if (calendarStart.get(Calendar.MONTH) == calendarEnd.get(Calendar.MONTH)) {
                ((TextView) findViewById(R.id.txtSelectedPeriod)).setText(
                        getContext().getString(R.string.slycalendar_dates_period, new SimpleDateFormat("EE, dd", Locale.getDefault()).format(calendarStart.getTime()), new SimpleDateFormat("EE, dd MMM", Locale.getDefault()).format(calendarEnd.getTime()))
                );
            } else {
                ((TextView) findViewById(R.id.txtSelectedPeriod)).setText(
                        getContext().getString(R.string.slycalendar_dates_period, new SimpleDateFormat("EE, dd MMM", Locale.getDefault()).format(calendarStart.getTime()), new SimpleDateFormat("EE, dd MMM", Locale.getDefault()).format(calendarEnd.getTime()))
                );
            }
        }


        findViewById(R.id.btnMonthPrev).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager vpager = findViewById(R.id.content);
                vpager.setCurrentItem(vpager.getCurrentItem()-1);
            }
        });

        findViewById(R.id.btnMonthNext).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPager vpager = findViewById(R.id.content);
                vpager.setCurrentItem(vpager.getCurrentItem()+1);
            }
        });

        ViewPager vpager = findViewById(R.id.content);
        vpager.getAdapter().notifyDataSetChanged();
        vpager.invalidate();

    }

    @Override
    public void dateSelect(Date selectedDate) {
        if (slyCalendarData.getSelectedStartDate() == null || slyCalendarData.isSingle()) {
            slyCalendarData.setSelectedStartDate(selectedDate);
            showCalendar();
            return;
        }
        if (slyCalendarData.getSelectedEndDate() == null) {
            if (selectedDate.getTime() < slyCalendarData.getSelectedStartDate().getTime()) {
                slyCalendarData.setSelectedEndDate(slyCalendarData.getSelectedStartDate());
                slyCalendarData.setSelectedStartDate(selectedDate);
                showCalendar();
                return;
            } else if (selectedDate.getTime() == slyCalendarData.getSelectedStartDate().getTime()) {
                slyCalendarData.setSelectedEndDate(null);
                slyCalendarData.setSelectedStartDate(selectedDate);
                showCalendar();
                return;
            } else if (selectedDate.getTime() > slyCalendarData.getSelectedStartDate().getTime()) {
                slyCalendarData.setSelectedEndDate(selectedDate);
                showCalendar();
                return;
            }
        }
        if (slyCalendarData.getSelectedEndDate() != null) {
            slyCalendarData.setSelectedEndDate(null);
            slyCalendarData.setSelectedStartDate(selectedDate);
            showCalendar();
        }
    }

    @Override
    public void dateLongSelect(Date selectedDate) {
        slyCalendarData.setSelectedEndDate(null);
        slyCalendarData.setSelectedStartDate(selectedDate);
        showCalendar();
    }

    private void paintCalendar() {
        findViewById(R.id.mainFrame).setBackgroundColor(slyCalendarData.getBackgroundColor());
        findViewById(R.id.headerView).setBackgroundColor(slyCalendarData.getHeaderColor());
        ((TextView) findViewById(R.id.txtYear)).setTextColor(slyCalendarData.getHeaderTextColor());
        ((TextView) findViewById(R.id.txtSelectedPeriod)).setTextColor(slyCalendarData.getHeaderTextColor());
    }
}
