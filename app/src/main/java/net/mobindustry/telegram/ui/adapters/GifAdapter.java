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
import net.mobindustry.telegram.utils.ImageLoaderHelper;

import java.io.Serializable;
import java.util.List;

public class GifAdapter extends ArrayAdapter<String> implements Serializable {

    private LayoutInflater inflater;


    public GifAdapter(Context context, List<String> objects) {
        super(context,0,objects);
        inflater=LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(R.layout.gif_item,parent,false);
        }

        ImageView image=(ImageView)convertView.findViewById(R.id.image);

        String url=getItem(position);
        ImageLoaderHelper.displayImageList(url,image);
        //Glide.with(convertView.getContext()).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(image);

        return convertView;
    }
}
