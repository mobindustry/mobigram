package net.mobindustry.telegram.core.handlers;

import net.mobindustry.telegram.utils.Const;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;

public abstract class BaseHandler<T> implements Client.ResultHandler {
    public static final int HANDLER_ID = Const.BASE_HANDLER_ID;
    protected T response;
    protected TdApi.Error error;
    protected boolean hasErrors;
    protected boolean hasAnswer;

    public BaseHandler() {
        hasAnswer = false;
    }

    public T getResponse() {
        return response;
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    public boolean hasAnswer() {
        return hasAnswer;
    }

    public TdApi.Error getError() {
        return error;
    }

    @Override
    public void onResult(TdApi.TLObject object) {
        if (object  instanceof TdApi.Error) {
            hasErrors = true;
            error = (TdApi.Error) object;
            hasAnswer = true;
            return;
        }
        hasErrors = false;
        response = resultHandler(object);
        hasAnswer = true;
    }

    public abstract T resultHandler(TdApi.TLObject object);

    public int getHandlerId() {
        return HANDLER_ID;
    }
}
