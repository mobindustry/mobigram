package net.mobindustry.telegram.ui.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.LevelListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.utils.ImageLoaderHelper;

import java.io.File;

public class FolderAdapter extends ArrayAdapter<File> {
    private LayoutInflater inflater;
    private ImageView photo;
    private View.OnClickListener clickListener;
    public static final int LEVEL_SEND = 0;
    public static final int LEVEL_ATTACH = 1;
    private static final long SCALE_UP_DURATION = 80;
    private static final long SCALE_DOWN_DURATION = 80;
    private AnimatorSet currentAnimation;
    private ViewHolderFolder viewHolder;


    public FolderAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                LevelListDrawable drawable = (LevelListDrawable) imageView.getDrawable();
                if (drawable.getLevel() == LEVEL_ATTACH) {
                    animateLevel(LEVEL_SEND,imageView);
                } else {
                    animateLevel(LEVEL_ATTACH,imageView);
                }
            }
        };

    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolderFolder();
            convertView = inflater.inflate(R.layout.grid_folder_item, parent, false);
            viewHolder.view = (ImageView)convertView.findViewById(R.id.photoFolder);
            viewHolder.view.setOnClickListener(clickListener);
        }
        photo = (ImageView) convertView.findViewById(R.id.imagePhoto);

        File galleryFolder = getItem(position);

        //TODO

        if (galleryFolder != null) {
            ImageLoaderHelper.displayImageList("file://" + galleryFolder.getAbsolutePath(), photo);
        }
        return convertView;
    }

    private void animateLevel(final int level, final ImageView view) {
        LevelListDrawable drawable = (LevelListDrawable) view.getDrawable();
        if (drawable.getLevel() == level) {
            return;
        }
        if (currentAnimation != null) {
            currentAnimation.cancel();
        }

        AnimatorSet scaleDown = new AnimatorSet()
                .setDuration(SCALE_DOWN_DURATION);
        scaleDown.playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 0.1f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0.1f));
        scaleDown.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setImageLevel(level);
            }
        });
        AnimatorSet scaleUp = new AnimatorSet()
                .setDuration(SCALE_UP_DURATION);
        scaleUp.playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, 0.1f, 1f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.1f, 1f));
        currentAnimation = new AnimatorSet();
        currentAnimation.playSequentially(scaleDown, scaleUp);
        currentAnimation.start();
    }

    public class ViewHolderFolder {
        ImageView photo;
        ImageView view;
    }
}

