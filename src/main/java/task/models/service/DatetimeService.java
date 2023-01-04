package task.models.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class DatetimeService {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");
    private static final Calendar calendar = new GregorianCalendar();
    private static final long milllisInDay = 86400000;

    public String getCurrentDate(){
        return FORMATTER.format(new Date(calendar.getTimeInMillis()));
    }

    public String getCurrentTime(){
        return TIME_FORMATTER.format(new Date(calendar.getTimeInMillis()));
    }

    public String getDateInTwoWeeks(){
        return FORMATTER.format(new Date(calendar.getTimeInMillis() + 14 * milllisInDay));
    }
}
