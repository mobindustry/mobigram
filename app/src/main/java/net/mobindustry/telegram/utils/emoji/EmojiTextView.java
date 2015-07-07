package net.mobindustry.telegram.utils.emoji;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import net.mobindustry.telegram.utils.Utils;

public class EmojiTextView extends TextView {

    public EmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //TODO find a way to show smiles

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Emoji emoji = new Emoji(getContext(), new DpCalculator(Utils.getDensity(getResources())), new Emoji.PageLoaded() {
            @Override
            public void load() {
                invalidate();
            }
        });

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
