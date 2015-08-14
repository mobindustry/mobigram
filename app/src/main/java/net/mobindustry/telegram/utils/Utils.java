package net.mobindustry.telegram.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.core.ApiHelper;
import net.mobindustry.telegram.core.handlers.BaseHandler;
import net.mobindustry.telegram.core.handlers.DownloadFileHandler;
import net.mobindustry.telegram.core.handlers.OkHandler;
import net.mobindustry.telegram.core.service.SendGif;
import net.mobindustry.telegram.model.holder.DataHolder;
import net.mobindustry.telegram.model.holder.DownloadFileHolder;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.ui.adapters.FolderAdapter;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    public static void drawBackgroundForCheckedPhoto(TextView numberPhotos, FrameLayout buttonSend, Activity activity){
        if (Utils.isTablet(activity)) {
            if (ListFoldersHolder.getCheckQuantity() > 0 && ListFoldersHolder.getListForSending() != null && ListFoldersHolder.getListForSending().size() > 0) {
                buttonSend.setEnabled(true);
                numberPhotos.setVisibility(View.VISIBLE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) numberPhotos.getLayoutParams();
                params.leftMargin = 50;
                numberPhotos.setLayoutParams(params);
                verifySetBackground(numberPhotos, Utils.getShapeDrawable(35, activity.getResources().getColor(R.color.message_notify)));
                numberPhotos.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            } else {
                buttonSend.setEnabled(false);
                numberPhotos.setVisibility(View.GONE);
            }
        } else {
            if (ListFoldersHolder.getCheckQuantity() > 0 && ListFoldersHolder.getListForSending() != null && ListFoldersHolder.getListForSending().size() > 0) {
                buttonSend.setEnabled(true);
                numberPhotos.setVisibility(View.VISIBLE);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) numberPhotos.getLayoutParams();
                if (Utils.getSmallestScreenSize(activity) <= 480) {
                    params.leftMargin = 10;
                } else {
                    params.leftMargin = 60;
                }
                numberPhotos.setLayoutParams(params);
                verifySetBackground(numberPhotos, Utils.getShapeDrawable(60, activity.getResources().getColor(R.color.message_notify)));
                numberPhotos.setText(String.valueOf(ListFoldersHolder.getCheckQuantity()));
            } else {
                buttonSend.setEnabled(false);
                numberPhotos.setVisibility(View.GONE);
            }
        }
    }

    public static void adjustGridViewPort(GridView gridList){
        gridList.setNumColumns(2);
        gridList.setHorizontalSpacing(15);
    }

    public static void adjustGridViewLand(GridView gridList) {
        gridList.setNumColumns(3);
        gridList.setHorizontalSpacing(15);
    }

    public static void sendMessageFromGallery(Activity activity){
        if (ListFoldersHolder.getListForSending() != null && ListFoldersHolder.getListForSending().size() != 0) {
            for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                if (ListFoldersHolder.getListForSending().get(i) instanceof ImagesObject) {
                    if (((ImagesObject) ListFoldersHolder.getListForSending().get(i)).getPath().contains("http")) {
                        String linkImage = ((ImagesObject) ListFoldersHolder.getListForSending().get(i)).getPath();
                        if (ListFoldersHolder.getListImages() == null) {
                            ListFoldersHolder.setListImages(new ArrayList<String>());
                        }
                        ListFoldersHolder.getListImages().add(linkImage);
                    } else {
                        ApiHelper.sendPhotoMessage(ListFoldersHolder.getChatID(),
                                ((ImagesObject) ListFoldersHolder.getListForSending().get(i)).getPath());
                    }
                }
                if (ListFoldersHolder.getListForSending().get(i) instanceof GiphyObject) {
                    if (ListFoldersHolder.getListGif() == null) {
                        ListFoldersHolder.setListGif(new ArrayList<String>());
                    }
                    String link = ((GiphyObject) ListFoldersHolder.getListForSending().get(i)).getPath();
                    ListFoldersHolder.getListGif().add(link);
                }
            }
            activity.startService(new Intent(activity, SendGif.class));
            activity.finish();
        }
    }

    public static void changeButtonsWhenRotate(LinearLayout layoutButtons, FolderAdapter adapter,Activity activity,GridView gridList){
        if (Utils.isTablet(activity)) {
            adjustGridViewPort(gridList);
            adapter.clear();
            adapter.addAll(ListFoldersHolder.getList());
        } else {
            if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !Utils.isTablet(activity)) {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0, 2.5f);
                layoutButtons.setLayoutParams(param);
                adjustGridViewLand(gridList);
                adapter.clear();
                adapter.addAll(ListFoldersHolder.getList());
            } else {
                LinearLayout.LayoutParams paramButtons = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0, 1.6f);
                layoutButtons.setLayoutParams(paramButtons);
                adjustGridViewPort(gridList);
                adapter.clear();
                adapter.addAll(ListFoldersHolder.getList());
            }
        }
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
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

    public static String formatFileSize(long size) {
        String hrSize = null;

        double b = size;
        double mByte = 1024.0;
        double k = size / mByte;
        double m = ((size / mByte) / mByte);
        double g = (((size / mByte) / mByte) / mByte);
        double t = ((((size / mByte) / mByte) / mByte) / mByte);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" B");
        }
        return hrSize;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static void gifFileCheckerAndLoader(final TdApi.File file, Activity activity, final ImageView view) {
        if (file instanceof TdApi.FileLocal) {
            TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
            Glide.with(DataHolder.getContext()).load(fileLocal.path).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(view);
        } else if (file instanceof TdApi.FileEmpty) {
            final TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;
            if (DownloadFileHolder.getUpdatedFilePath(fileEmpty.id) != null) {
                Glide.with(DataHolder.getContext()).load(DownloadFileHolder.getUpdatedFilePath(fileEmpty.id)).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(view);
            } else {
                ApiHelper.downloadFile(fileEmpty.id);
                findLoop(fileEmpty.id, view, activity, false);
            }
        }
    }

    public static void photoFileCheckerAndLoader(final TdApi.File file, final ImageView view, final Activity activity) {
        if (file instanceof TdApi.FileLocal) {
            TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
            ImageLoaderHelper.displayImageWithoutFadeIn(Const.IMAGE_LOADER_PATH_PREFIX + fileLocal.path, view);
        } else if (file instanceof TdApi.FileEmpty) {
            final TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;
            if (DownloadFileHolder.getUpdatedFilePath(fileEmpty.id) != null) {
                findLoop(fileEmpty.id, view, activity, false);
            } else {
                ApiHelper.downloadFile(fileEmpty.id);
                findLoop(fileEmpty.id, view, activity, false);
            }
        }
    }

    public static void photoFileLoader(final int id, final ImageView view, final Activity activity) {
        ApiHelper.downloadFile(id);
        findLoop(id, view, activity, false);
    }

    public static void setIcon(TdApi.File file, int chatId, String firstName, String lastName, final ImageView iconImage, final TextView icon, final Activity activity) {
        if (file != null) {
            if (file.getConstructor() == TdApi.FileLocal.CONSTRUCTOR) {
                iconImage.setVisibility(View.VISIBLE);
                TdApi.FileLocal fileLocal = (TdApi.FileLocal) file;
                ImageLoaderHelper.displayImageWithoutFadeIn(Const.IMAGE_LOADER_PATH_PREFIX + fileLocal.path, iconImage);
            }
            if (file.getConstructor() == TdApi.FileEmpty.CONSTRUCTOR) {
                final TdApi.FileEmpty fileEmpty = (TdApi.FileEmpty) file;
                if (fileEmpty.id != 0) {
                    if (DownloadFileHolder.getUpdatedFilePath(fileEmpty.id) != null) {
                        file = DownloadFileHolder.getUpdatedFile(fileEmpty.id);
                        ImageLoaderHelper.displayImageWithoutFadeIn(Const.IMAGE_LOADER_PATH_PREFIX + ((TdApi.FileLocal) file).path, iconImage);
                    } else {
                        ApiHelper.downloadFile(fileEmpty.id);
                        findLoop(fileEmpty.id, iconImage, activity, false);
                    }
                } else {
                    iconImage.setImageDrawable(null);
                    icon.setVisibility(View.VISIBLE);
                    if (chatId < 0) {
                        verifySetBackground(icon, Utils.getShapeDrawable(R.dimen.toolbar_icon_size, chatId));
                    } else {
                        verifySetBackground(icon, Utils.getShapeDrawable(R.dimen.toolbar_icon_size, -chatId));
                    }
                    icon.setText(Utils.getInitials(firstName, lastName));
                }
            }
        }
    }

    public static void verifySetBackground(View view, Drawable drawable) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    public static int getSmallestScreenSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (width > height) {
            return height;
        } else {
            return width;
        }
    }

    public static void findLoop(final int id, final ImageView iconImage, final Activity activity, final boolean gif) {
        Runnable runnable = new Runnable() {
            public void run() {
                String path;
                for (int i = 0; i < 50; i++) {
                    path = DownloadFileHolder.getUpdatedFilePath(id);
                    if (path != null) {
                        final String finalPath = path;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (gif) {
                                    Glide.with(DataHolder.getContext()).load(finalPath).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iconImage);
                                } else {
                                    iconImage.setVisibility(View.VISIBLE);
                                    ImageLoaderHelper.displayImageWithoutFadeIn(Const.IMAGE_LOADER_PATH_PREFIX + finalPath, iconImage);
                                }
                            }
                        });
                        break;
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();
    }

    public static File processImage(File tmpFile, ExifInterface originalExif) {
        int pictureOrientation = -1;
        try {
            ExifInterface exif;
            if (originalExif != null) {
                exif = originalExif;
            } else {
                exif = new ExifInterface(tmpFile.getAbsolutePath());
            }

            pictureOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            Log.e("Tag", "ExifInterface exception", e);
        }
        rotateFileImage(pictureOrientation, tmpFile.getAbsolutePath());
        return tmpFile;
    }

    private static void rotateFileImage(int orientationFlag, String filePathFrom) {
        int angle;
        switch (orientationFlag) {
            case ExifInterface.ORIENTATION_ROTATE_180:
                angle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                angle = 90;

                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                angle = 270;
                break;
            default:
                return;
        }

        BitmapFactory.Options optionBmp = new BitmapFactory.Options();
        optionBmp.inJustDecodeBounds = false;
        optionBmp.inPreferredConfig = Bitmap.Config.ARGB_8888;
        optionBmp.inDither = false;
        optionBmp.inScaled = false;

        Bitmap originalBitmap = BitmapFactory.decodeFile(filePathFrom, optionBmp);

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePathFrom);
            Bitmap bitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
            originalBitmap.recycle();
            originalBitmap = null;

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            bitmap.recycle();
            bitmap = null;
        } catch (Exception e) {
            Log.e("Tag", "rotateFileImage error " + e);
        }
    }

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) DataHolder.getContext().getSystemService(DataHolder.getContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }
}