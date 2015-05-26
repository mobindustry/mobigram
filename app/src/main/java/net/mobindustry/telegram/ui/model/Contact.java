package net.mobindustry.telegram.ui.model;

public class Contact {

    String firstName;
    String lastName;
    String lastMessage;

    public Contact(String firstName, String lastName, String lastMessage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastMessage = lastMessage;
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
}
