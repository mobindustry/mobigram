package net.mobindustry.telegram.utils.emoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.TextView;



//invalidates itself when the emojies are loaded
public class EmojiTextView extends TextView {
    Emoji emoji = new Emoji(getContext(), new DpCalculator(1f));
    private Subscription s;

    public EmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        s = emoji.pageLoaded().subscribe(new Action1<Bitmap>() {
//            @Override
//            public void call(Bitmap bitmap) {
//                invalidate();
//            }
//        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        s.unsubscribe();
    }
}
