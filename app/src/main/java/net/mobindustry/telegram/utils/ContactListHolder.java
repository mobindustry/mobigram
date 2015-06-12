package net.mobindustry.telegram.utils;

import org.drinkless.td.libcore.telegram.TdApi;

public class ContactListHolder {

    private static ContactListHolder instance;

    private TdApi.Contacts contacts;

    public static synchronized ContactListHolder getInstance() {
        if (instance == null) {
            instance = new ContactListHolder();
        }
        return instance;
    }

    private ContactListHolder() {
    }

    public TdApi.Contacts getContacts() {
        return contacts;
    }

    public void setContacts(TdApi.Contacts contacts) {
        this.contacts = contacts;
    }
}
