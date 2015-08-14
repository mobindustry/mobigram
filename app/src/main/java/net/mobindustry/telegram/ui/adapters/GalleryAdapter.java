package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.model.FolderCustomGallery;
import net.mobindustry.telegram.utils.ImageLoaderHelper;

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
            if (galleryFolder.getFirstThumb().equals("")) {
                ImageLoaderHelper.displayImageList(Const.IMAGE_LOADER_PATH_PREFIX + galleryFolder.getFirstPhoto(), firstPhoto);
            } else {
                ImageLoaderHelper.displayImageList(Const.IMAGE_LOADER_PATH_PREFIX + galleryFolder.getFirstThumb(), firstPhoto);
            }
            nameFolder.setText(galleryFolder.getName());
            if (Integer.valueOf(galleryFolder.getPhotosQuantity()) > 1000)
                photosFolder.setText("> 1k");
            else {
                photosFolder.setText(galleryFolder.getPhotosQuantity());
            }
        }
        return convertView;
    }
}

