package net.mobindustry.telegram.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.model.Gif.GiphyInfo;
import net.mobindustry.telegram.model.holder.ListFoldersHolder;
import net.mobindustry.telegram.utils.FileWithIndicator;
import net.mobindustry.telegram.utils.GiphyObject;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.MediaGallery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GifAdapter extends ArrayAdapter<GiphyObject> implements Serializable {

    private LayoutInflater inflater;
    private View.OnClickListener clickListener;


    public GifAdapter(Context context, List<GiphyObject> objects) {
        super(context, 0, objects);
        inflater = LayoutInflater.from(context);

        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiphyObject giphyObject = (GiphyObject) v.getTag();
                if (giphyObject.isCheck()) {
                    giphyObject.setCheck(false);
                    for (int i = 0; i < ListFoldersHolder.getListForSending().size(); i++) {
                        if (ListFoldersHolder.getListForSending().get(i) instanceof GiphyObject) {
                            if (((GiphyObject) ListFoldersHolder.getListForSending().get(i)).getPath().equals(giphyObject.getPath())) {
                                ListFoldersHolder.getListForSending().remove(ListFoldersHolder.getListForSending().get(i));
                            }
                        }
                    }
                    ListFoldersHolder.setCheckQuantity(ListFoldersHolder.getCheckQuantity() + 1);
                } else {
                    if (ListFoldersHolder.getCheckQuantity() < 10) {
                        giphyObject.setCheck(true);
                        if (ListFoldersHolder.getListForSending()==null){
                            ListFoldersHolder.setListForSending(new ArrayList<MediaGallery>());
                        }
                        ListFoldersHolder.getListForSending().add(giphyObject);
                        ListFoldersHolder.setCheckQuantity(ListFoldersHolder.getCheckQuantity() + 1);
                    }
                }
                notifyDataSetChanged();

            }
        };
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GiphyObject giphyObject = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gif_item, parent, false);
        }
        ImageView check = (ImageView) convertView.findViewById(R.id.check);

        ImageView image = (ImageView) convertView.findViewById(R.id.imageGif);
        image.setOnClickListener(clickListener);
        image.setTag(giphyObject);

        if (giphyObject.isCheck()) {
            check.setImageResource(R.drawable.ic_attach_check);
        } else {
            check.setImageResource(R.drawable.ic_circle);
        }

        ImageLoaderHelper.displayImageList(giphyObject.getPath(), image);
        //todo
        //Glide.with(convertView.getContext()).load(giphyObject.getPath()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new ImageView(getContext()));
        //Glide.getPhotoCacheDir(getContext());
        return convertView;
    }
}
