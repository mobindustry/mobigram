package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.utils.FolderCustomGallery;
import net.mobindustry.telegram.utils.ImageLoaderHelper;

import java.io.Serializable;
import java.util.ArrayList;

public class GalleryAdapter extends ArrayAdapter<FolderCustomGallery> {
    private LayoutInflater inflater;

    public GalleryAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
        }


        ImageView firstPhoto = (ImageView) convertView.findViewById(R.id.imagePhoto);
        TextView nameFolder = (TextView) convertView.findViewById(R.id.nameFolder);
        TextView photosFolder = (TextView) convertView.findViewById(R.id.photosQuantity);

        FolderCustomGallery galleryFolder = getItem(position);

        if (galleryFolder != null) {

            Log.e("LOg","LINK PHOTO "+galleryFolder.getFirstPhoto());
            //ImageLoader.getInstance().displayImage("file://" + galleryFolder.getFirstPhoto(),firstPhoto);
            ImageLoaderHelper.displayImageList("file://" + galleryFolder.getFirstPhoto(),firstPhoto);
            nameFolder.setText(galleryFolder.getName());
            photosFolder.setText(galleryFolder.getPhotosQuantity());
        }

        return convertView;
    }
}

