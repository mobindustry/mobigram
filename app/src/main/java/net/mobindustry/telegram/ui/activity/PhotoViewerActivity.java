package net.mobindustry.telegram.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import net.mobindustry.telegram.R;
import net.mobindustry.telegram.utils.Const;
import net.mobindustry.telegram.utils.ImageLoaderHelper;
import net.mobindustry.telegram.utils.Utils;

public class PhotoViewerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_viewer_activity);

        ImageView imageView = (ImageView) findViewById(R.id.photo_image_view);

        String path = getIntent().getStringExtra("file_path");
        if (path == null) {
            int id = getIntent().getIntExtra("file_id", 0);
            Utils.fileLoader(id, imageView);
        } else {
            ImageLoaderHelper.displayImage(Const.IMAGE_LOADER_PATH_PREFIX + path, imageView);
        }

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.transparent_frame_layout);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
