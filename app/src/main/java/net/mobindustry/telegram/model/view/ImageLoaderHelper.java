package net.mobindustry.telegram.model.view;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import net.mobindustry.telegram.model.holder.DataHolder;
import net.mobindustry.telegram.model.holder.PhotoDownloadHolder;

import org.drinkless.td.libcore.telegram.TdApi;

import java.io.IOException;
import java.io.InputStream;

public class ImageLoaderHelper {

    private static class CustomImageDownloader extends BaseImageDownloader {

        public CustomImageDownloader(Context context) {
            super(context);
        }

        @Override
        public InputStream getStream(String imageUri, Object extra) throws IOException {
            return super.getStream(imageUri, extra);
        }
    }

    private static ImageLoader imageLoader = initImageLoader();

    private static ImageLoader initImageLoader() {
        ImageLoader imageLoader = ImageLoader.getInstance();

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(DataHolder.getContext())
                .diskCacheSize(50 * 1024 * 1024)
                .defaultDisplayImageOptions(defaultOptions)
                .imageDownloader(new CustomImageDownloader(DataHolder.getContext())) // setup custom loader
                .build();

        imageLoader.init(config);
        return imageLoader;
    }

    public static void displayImage(final String url, final ImageView imageView) {
        imageLoader.displayImage(url, imageView);
    }

}
