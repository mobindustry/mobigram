package net.mobindustry.telegram.ui.model;

import android.graphics.Color;

import java.util.Random;

public class Contact {

    private String firstName;
    private String lastName;
    private String lastMessage;
    private int color;

    private Random rand = new Random();

    public Contact(String firstName, String lastName, String lastMessage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastMessage = lastMessage;
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
}
