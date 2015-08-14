package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.flickr.PhotoFlickr;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.ImagesObject;
import net.mobindustry.telegram.utils.MediaGallery;

import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends ArrayAdapter<PhotoFlickr> {

    private LayoutInflater inflater;
    private View.OnClickListener clickListener;

    public ImagesAdapter(Context context, List<PhotoFlickr> list, final LoadPhotos loadPhotos) {
        super(context, 0, list);
        inflater = LayoutInflater.from(context);
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoFlickr photoFlickr = (PhotoFlickr) v.getTag();
                if (photoFlickr.isCheck()) {
                    photoFlickr.setCheck(false);
                    for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                        if (ListFoldersHolder.getListForSending().get(i) instanceof ImagesObject) {
                            if (((ImagesObject) ListFoldersHolder.getListForSending().get(i)).getPath().equals(photoFlickr.getSendLinkLarge())) {
                                ListFoldersHolder.getListForSending().remove(ListFoldersHolder.getListForSending().get(i));
                            }
                        }
                    }
                    ListFoldersHolder.setCheckQuantity(ListFoldersHolder.getCheckQuantity() - 1);
                    loadPhotos.load();
                } else {
                    if (ListFoldersHolder.getCheckQuantity() < 10) {
                        photoFlickr.setCheck(true);
                        ImagesObject imagesObject = new ImagesObject();
                        imagesObject.setPath(photoFlickr.getSendLinkLarge());
                        if (ListFoldersHolder.getListForSending() == null) {
                            ListFoldersHolder.setListForSending(new ArrayList<MediaGallery>());
                        }
                        ListFoldersHolder.getListForSending().add(imagesObject);
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
        PhotoFlickr photoFlickr = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.images_flickr_item, parent, false);
        }
        ImageView check = (ImageView) convertView.findViewById(R.id.checkFlickr);
        ImageView photo = (ImageView) convertView.findViewById(R.id.imageFlickr);
        check.setOnClickListener(clickListener);
        check.setTag(photoFlickr);
        if (photoFlickr.isCheck()) {
            check.setImageResource(R.drawable.ic_attach_check);
        } else {
            check.setImageResource(R.drawable.circle);
        }

        String link = "http://farm" + photoFlickr.getFarm()
                + ".staticflickr.com/" + photoFlickr.getServer()
                + "/" + photoFlickr.getPhotoId() + "_" + photoFlickr.getSecret() + "_m.jpg";
        String sendLinkLarge = "http://farm" + photoFlickr.getFarm()
                + ".staticflickr.com/" + photoFlickr.getServer()
                + "/" + photoFlickr.getPhotoId() + "_" + photoFlickr.getSecret() + "_b.jpg";
        Log.e("log", "LINL THUMB " + link);
        photoFlickr.setLink(link);
        photoFlickr.setSendLinkLarge(sendLinkLarge);
        ImageLoaderHelper.displayImageList(link, photo);
        return convertView;
    }

    public interface LoadPhotos {
        void load();
    }
}

