package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.utils.FolderCustomGallery;

import java.io.Serializable;

public class GalleryAdapter extends ArrayAdapter<FolderCustomGallery> implements Serializable {
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


        ImageView firstPhoto = (ImageView) convertView.findViewById(R.id.firstPhotoGalleryFragment);
        TextView nameFolder = (TextView) convertView.findViewById(R.id.nameFolder);
        TextView photosFolder = (TextView) convertView.findViewById(R.id.photosQuantity);


        FolderCustomGallery galleryFolder = getItem(position);
        if (galleryFolder != null) {
            //Log.e("LOG", "CATEGORY SIZE " + );
            firstPhoto.setImageURI(galleryFolder.getUriFirstPhoto());
            nameFolder.setText(galleryFolder.getName());
            photosFolder.setText(galleryFolder.getPhotosQuantity());
        }
        return convertView;
    }
}
