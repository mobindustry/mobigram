package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.utils.FileWithIndicator;
import net.mobindustry.telegram.utils.ImageLoaderHelper;

public class FolderAdapter extends ArrayAdapter<FileWithIndicator>  {
    //TODO create a simple animation for button check
    private LayoutInflater inflater;
    private ImageView photo;
    private View.OnClickListener clickListener;


    public FolderAdapter(Context context, final LoadPhotos loadPhotos) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileWithIndicator galleryFolder = (FileWithIndicator) v.getTag();
                if (galleryFolder.isCheck()) {
                    galleryFolder.setCheck(false);
                    ListFoldersHolder.setCheckQuantity(ListFoldersHolder.getCheckQuantity() - 1);
                    loadPhotos.load();
                } else {
                    if (ListFoldersHolder.getCheckQuantity()<10){
                        galleryFolder.setCheck(true);
                        ListFoldersHolder.setCheckQuantity(ListFoldersHolder.getCheckQuantity() + 1);
                        loadPhotos.load();
                    }
                }
                notifyDataSetChanged();

            }
        };

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FileWithIndicator galleryFolder = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_folder_item, parent, false);

        }
        ImageView image = (ImageView) convertView.findViewById(R.id.photoFolder);
        image.setOnClickListener(clickListener);
        image.setTag(galleryFolder);

        photo = (ImageView) convertView.findViewById(R.id.imagePhoto);
        if (galleryFolder.isCheck()) {
            image.setImageResource(R.drawable.ic_attach_check);
        } else {
            image.setImageResource(R.drawable.ic_circle);
        }

        if (galleryFolder != null) {
            if (!galleryFolder.getThumbPhoto().equals("")){
                ImageLoaderHelper.displayImageList("file://" + galleryFolder.getThumbPhoto(), photo);
            } else {
                ImageLoaderHelper.displayImageList("file://" + galleryFolder.getFile().getAbsolutePath(), photo);
            }

        }
        return convertView;
    }
    public interface LoadPhotos {
        void load();
    }

}

