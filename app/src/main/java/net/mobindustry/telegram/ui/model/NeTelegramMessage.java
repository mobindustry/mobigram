package net.mobindustry.telegram.ui.model;

import java.util.Date;
import java.util.GregorianCalendar;

public class NeTelegramMessage {

    private int type;
    private String message;
    private Date date;

    public NeTelegramMessage(int type, String message) {
        this.type = type;
        this.message = message;
        date.setTime(GregorianCalendar.getInstance().getTimeInMillis());
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
