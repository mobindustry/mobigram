package net.mobindustry.telegram.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NeTelegramMessage implements Serializable {

    private int type;
    private String message;
    private Date date = new Date();
    private Calendar calendar = GregorianCalendar.getInstance();

    public NeTelegramMessage(int type, String message) {
        this.type = type;
        this.message = message;
        date.setTime(calendar.getTimeInMillis());
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}
