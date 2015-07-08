package net.mobindustry.telegram.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiClient;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.DownloadFileHandler;
import net.mobindustry.telegram.model.holder.DataHolder;

import org.drinkless.td.libcore.telegram.TdApi;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {

    public static SimpleDateFormat getDateFormat(String type) {
        return new SimpleDateFormat(type);
    }

    public static String getInitials(String firstName, String lastName) {
        if (firstName.isEmpty()) {
            return ":)";
        }
        if (lastName.isEmpty()) {
            char[] iconText = new char[2];
            firstName.getChars(0, 1, iconText, 0);
            firstName.getChars(1, 2, iconText, 1);
            return ("" + iconText[0] + iconText[1]).toUpperCase();
        } else {
            char[] iconText = new char[2];
            firstName.getChars(0, 1, iconText, 0);
            lastName.getChars(0, 1, iconText, 1);
            return ("" + iconText[0] + iconText[1]).toUpperCase();
        }
    }

    public static ShapeDrawable getShapeDrawable(int size, int color) {
        ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        circle.setIntrinsicHeight(size);
        circle.setIntrinsicWidth(size);
        circle.getPaint().setColor(color);
        return circle;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void fileCheckerAndLoader(final TdApi.File file, final ImageView view) {
        if (file instanceof TdApi.FileEmpty) {
            final TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;

            new ApiClient<>(new TdApi.DownloadFile(fileEmpty.id), new DownloadFileHandler(), new ApiClient.OnApiResultHandler() {
                @Override
                public void onApiResult(BaseHandler output) {
                    if (output.getHandlerId() == DownloadFileHandler.HANDLER_ID) {
                        ImageLoaderHelper.displayImage(String.valueOf(fileEmpty.id), view);
                    }
                }
            }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
        if (file instanceof TdApi.FileLocal) {
            TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
            ImageLoaderHelper.displayImage(Const.IMAGE_LOADER_PATH_PREFIX + fileLocal.path, view);
        }
    }

    public static void fileLoader(final int id, final ImageView view) {
        new ApiClient<>(new TdApi.DownloadFile(id), new DownloadFileHandler(), new ApiClient.OnApiResultHandler() {
            @Override
            public void onApiResult(BaseHandler output) {
                if (output.getHandlerId() == DownloadFileHandler.HANDLER_ID) {
                    ImageLoaderHelper.displayImage(String.valueOf(id), view);
                }
            }
        }).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static void hideKeyboard(EditText e) {
        InputMethodManager imm = (InputMethodManager) e.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(e.getWindowToken(), 0);
    }

    public static int compare(long lhs, long rhs) {
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }

    public static float getDensity(Resources res) {
        DisplayMetrics metrics = res.getDisplayMetrics();
        return metrics.density;
    }

    public static String getUserStatusString(TdApi.UserStatus status) {
        Context context = DataHolder.getContext();
        Locale.setDefault(Locale.US);
        String lastSeenString = "";

        switch (status.getConstructor()) {
            case TdApi.UserStatusOnline.CONSTRUCTOR:
                lastSeenString = context.getString(R.string.online);
                break;
            case TdApi.UserStatusOffline.CONSTRUCTOR:
                int ago = ((TdApi.UserStatusOffline) status).wasOnline;
                lastSeenString = context.getString(R.string.last_seen) + " " + DateUtils.getRelativeTimeSpanString((long) ago * 1000);
                break;
            case TdApi.UserStatusRecently.CONSTRUCTOR:
                lastSeenString = context.getString(R.string.ls_recently);
                break;
            case TdApi.UserStatusLastWeek.CONSTRUCTOR:
                lastSeenString = context.getString(R.string.ls_week_ago);
                break;
            case TdApi.UserStatusLastMonth.CONSTRUCTOR:
                lastSeenString = context.getString(R.string.ls_month_ago);
                break;
            default:
                break;
        }
        return lastSeenString;
    }
}



