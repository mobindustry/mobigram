package net.mobindustry.telegram.ui.model;

import android.graphics.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Contact implements Serializable {

    private String firstName;
    private String lastName;
    private String lastMessage;
    private int color;
    private List<NeTelegramMessage> list;

    private Random rand = new Random();

    public Contact(String firstName, String lastName) {
        listInit();
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastMessage = list.get(list.size()-1).getMessage();
        this.color = colorInit();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getColor() {
        return color;
    }

    private int colorInit() {
        return Color.rgb(rand.nextInt(255),
                rand.nextInt(255), rand.nextInt(255));
    }

    public List<NeTelegramMessage> getList() {
        return list;
    }

    private void listInit(){
        list = new ArrayList<>();
        for (int i = 0; i < rand.nextInt(50)+1; i++) {
            list.add(new NeTelegramMessage(rand.nextInt(4), "message" + i + " blablabla" + i + "jkfas jasfh kjha skjlhsafk"));
        }
    }

    public String getInitials() {
        char[] iconText = new char[2];
        firstName.getChars(0, 1, iconText, 0);
        lastName.getChars(0, 1, iconText, 1);
        return ("" + iconText[0] + iconText[1]).toUpperCase();
    }
}
