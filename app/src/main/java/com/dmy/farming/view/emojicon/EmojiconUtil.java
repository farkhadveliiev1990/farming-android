package com.dmy.farming.view.emojicon;

import java.util.Map;

/**
 * Created by 陈晖 on 2018/4/2.
 */

public class EmojiconUtil {

    EaseEmojiconInfoProvider easeEmojiconInfoProvider;

    private static EmojiconUtil instance = null;

    /**
     * get instance of EaseUI
     *
     * @return
     */
    public synchronized static EmojiconUtil getInstance() {
        if (instance == null) {
            instance = new EmojiconUtil();
        }
        return instance;
    }

    public interface EaseEmojiconInfoProvider {
        /**
         * return EaseEmojicon for input emojiconIdentityCode
         *
         * @param emojiconIdentityCode
         * @return
         */
        EaseEmojicon getEmojiconInfo(String emojiconIdentityCode);

        /**
         * get Emojicon map, key is the text of emoji, value is the resource id or local path of emoji icon(can't be URL on internet)
         *
         * @return
         */
        Map<String, Object> getTextEmojiconMapping();
    }

    public EaseEmojiconInfoProvider getEaseEmojiconInfoProvider() {
        return easeEmojiconInfoProvider;
    }

    public void setEaseEmojiconInfoProvider(EaseEmojiconInfoProvider easeEmojiconInfoProvider) {
        this.easeEmojiconInfoProvider = easeEmojiconInfoProvider;
    }

}
