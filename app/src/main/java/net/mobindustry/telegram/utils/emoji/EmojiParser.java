package net.mobindustry.telegram.utils.emoji;

import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

import org.drinkless.td.libcore.telegram.TdApi;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiParser {

    private final Emoji emoji;
    private final Map<String, CharSequence> cache = new HashMap<>();
    private final Pattern userReference = Pattern.compile("(?:^|[^a-zA-Z0-9_＠!@#$%&*])(?:(?:@|＠)(?!/))([a-zA-Z0-9/_]{1,15})(?:\\b(?!@|＠)|$)");

    public EmojiParser(Emoji emoji) {
        this.emoji = emoji;
    }

    public void parse(TdApi.Message msg) {
        TdApi.MessageText text = (TdApi.MessageText) msg.message;
        String key = text.text;
        CharSequence fromCache = cache.get(key);
        if (fromCache != null) {
            text.textWithSmilesAndUserRefs = fromCache;
        } else {
            CharSequence parsed = emoji.replaceEmoji(key);
            Matcher matcher = userReference.matcher(key);
            Spannable s;
            if (parsed instanceof Spannable) {
                s = (Spannable) parsed;
            } else {
                s = Spannable.Factory.getInstance().newSpannable(parsed);
            }

            while (matcher.find()) {
                s.setSpan(new ForegroundColorSpan(0xff427ab0), matcher.start(), matcher.end(), 0);
            }
            cache.put(key, s);
            text.textWithSmilesAndUserRefs = s;
        }
    }
}
