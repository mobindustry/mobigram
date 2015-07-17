package net.mobindustry.telegram.utils;

import android.content.Context;
import android.graphics.Bitmap;
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
import java.util.concurrent.TimeUnit;

public class ImageLoaderHelper {

    private static class CustomImageDownloader extends BaseImageDownloader {

        public CustomImageDownloader(Context context) {
            super(context);
        }

        @Override
        public InputStream getStream(String imageUri, Object extra) throws IOException {

            if (!imageUri.contains("/")) {
                String path = null;
                int id = Integer.parseInt(imageUri);
                for (int i = 0; i < 50; i++) {
                    path = DownloadFileHolder.getUpdatedFilePath(id);
                    if (path != null) {
                        break;
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return super.getStream(Const.IMAGE_LOADER_PATH_PREFIX + path, extra);
            } else {
                return super.getStream(imageUri, extra);
            }
        }
    }

    private static ImageLoader imageLoader = initImageLoader();
    private static DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    private static DisplayImageOptions defaultOptionsFadeIn = new DisplayImageOptions.Builder()
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
        imageLoader.displayImage(url, imageView, defaultOptions);
    }

    public static void displayImageList(final String url, final ImageView imageView) {


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .showImageOnLoading(R.drawable.ic_netelegram_placeholder)
                .build();
        imageLoader.displayImage(url, imageView, options);
    }

}
