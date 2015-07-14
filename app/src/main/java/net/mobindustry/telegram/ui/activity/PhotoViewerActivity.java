package net.mobindustry.telegram.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.Utils;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoViewerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_viewer_activity);

        //TODO add backButton
        //TODO fix photo scale

        ImageView imageView = (ImageView) findViewById(R.id.photo_image_view);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);

        String path = getIntent().getStringExtra("file_path");
        if (path == null) {
            int id = getIntent().getIntExtra("file_id", 0);
            Utils.photoFileLoader(id, imageView);
        } else {
            ImageLoaderHelper.displayImage(Const.IMAGE_LOADER_PATH_PREFIX + path, imageView);
        }
        mAttacher.update();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
