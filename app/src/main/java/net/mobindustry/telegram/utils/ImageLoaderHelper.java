package net.mobindustry.telegram.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.holder.DataHolder;
import net.mobindustry.telegram.model.holder.DownloadFileHolder;

import java.io.IOException;
import java.io.InputStream;

public class ImageLoaderHelper {

    private static class CustomImageDownloader extends BaseImageDownloader {

        public CustomImageDownloader(Context context) {
            super(context);
        }

        @Override
        public InputStream getStream(String imageUri, Object extra) throws IOException {

            if (!imageUri.contains("/")) {
                String path = DownloadFileHolder.getUpdatedFilePath(Integer.parseInt(imageUri));
                while (path == null) {
                    path = DownloadFileHolder.getUpdatedFilePath(Integer.parseInt(imageUri));
                    if (path != null) {
                        break;
                    }
                }
                return super.getStream(Const.IMAGE_LOADER_PATH_PREFIX + path, extra);
            } else {
                return super.getStream(imageUri, extra);
            }
        }
    }

    private static ImageLoader imageLoader = initImageLoader();
    private static  DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    private static  DisplayImageOptions defaultOptionsFadeIn = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .displayer(new FadeInBitmapDisplayer(500))
            .build();


    private static ImageLoader initImageLoader() {
        ImageLoader imageLoader = ImageLoader.getInstance();


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(DataHolder.getContext())
                .diskCacheSize(50 * 1024 * 1024)
                .defaultDisplayImageOptions(defaultOptionsFadeIn)
                .imageDownloader(new CustomImageDownloader(DataHolder.getContext())) // setup custom loader
                .build();

        imageLoader.init(config);
        return imageLoader;
    }

    public static void displayImage(final String url, final ImageView imageView) {
        imageLoader.displayImage(url, imageView, defaultOptionsFadeIn);
    }

    public static void displayImageFadeIn(final String url, final ImageView imageView) {
        imageLoader.displayImage(url, imageView, defaultOptionsFadeIn);
    }
    public static void displayImageDefault(final String url, final ImageView imageView) {
        imageLoader.displayImage(url, imageView,defaultOptions);
    }

    public static void displayImageList(final String url, final ImageView imageView) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.image_placeholder)
                .build();

        imageLoader.displayImage(url, imageView, options);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


}
