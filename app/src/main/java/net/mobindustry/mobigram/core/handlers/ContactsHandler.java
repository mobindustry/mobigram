package net.mobindustry.mobigram.core.handlers;

import net.mobindustry.mobigram.utils.Const;

import org.drinkless.td.libcore.telegram.TdApi;

public class ContactsHandler extends BaseHandler<TdApi.Contacts> {
    public static final int HANDLER_ID = Const.CONTACTS_HANDLER_ID;

    @Override
    public TdApi.Contacts resultHandler(TdApi.TLObject object) {
        if (object.getConstructor() == TdApi.Contacts.CONSTRUCTOR) {
            return (TdApi.Contacts) object;
        }
        return null;
    }

    @Override
    public int getHandlerId() {
        return HANDLER_ID;
    }
}
