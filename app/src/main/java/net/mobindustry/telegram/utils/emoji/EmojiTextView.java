package net.mobindustry.telegram.utils.emoji;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

//invalidates itself when the emojies are loaded
public class EmojiTextView extends TextView {

    public EmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }
}
