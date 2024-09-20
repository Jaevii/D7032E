package ltu;

import java.util.Date;

public class TestCalendar implements ICalendar {

    private Date date2016 = new Date(1451606400000L); // 2016-01-01 in milliseconds

    public Date getDate() {
        return this.date2016;
    }

    public void setDate(Date date) {
        this.date2016 = date;
    }
}