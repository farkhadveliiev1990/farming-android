package com.dmy.farming.view.emojicon;

public class EaseEmojicon {
    public EaseEmojicon() {
    }

    /**
     * constructor
     *
     * @param icon-      resource id of the icon
     * @param emojiText- text of emoji icon
     */
    public EaseEmojicon(int icon, String emojiText) {
        this.icon = icon;
        this.emojiText = emojiText;
    }


    /**
     * identity code
     */
    private String identityCode;

    /**
     * static icon resource id
     */
    private int icon;

    /**
     * dynamic icon resource id
     */
    private int bigIcon;

    /**
     * text of emoji, could be null for big icon
     */
    private String emojiText;

    /**
     * name of emoji icon
     */
    private String name;

    /**
     * path of icon
     */
    private String iconPath;

    /**
     * path of big icon
     */
    private String bigIconPath;

    /**
     * 是否被锁而不允许使用
     */
    private boolean isLock;


    /**
     * get the resource id of the icon
     *
     * @return
     */
    public int getIcon() {
        return icon;
    }


    /**
     * set the resource id of the icon
     *
     * @param icon
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }


    /**
     * get the resource id of the big icon
     *
     * @return
     */
    public int getBigIcon() {
        return bigIcon;
    }


    /**
     * set the resource id of the big icon
     *
     * @return
     */
    public void setBigIcon(int dynamicIcon) {
        this.bigIcon = dynamicIcon;
    }


    /**
     * get text of emoji icon
     *
     * @return
     */
    public String getEmojiText() {
        return emojiText;
    }


    /**
     * set text of emoji icon
     *
     * @param emojiText
     */
    public void setEmojiText(String emojiText) {
        this.emojiText = emojiText;
    }

    /**
     * get name of emoji icon
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * set name of emoji icon
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get icon path
     *
     * @return
     */
    public String getIconPath() {
        return iconPath;
    }


    /**
     * set icon path
     *
     * @param iconPath
     */
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }


    /**
     * get path of big icon
     *
     * @return
     */
    public String getBigIconPath() {
        return bigIconPath;
    }


    /**
     * set path of big icon
     *
     * @param bigIconPath
     */
    public void setBigIconPath(String bigIconPath) {
        this.bigIconPath = bigIconPath;
    }

    /**
     * get identity code
     *
     * @return
     */
    public String getIdentityCode() {
        return identityCode;
    }

    /**
     * set identity code
     *
     */
    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public static String newEmojiText(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        } else {
            return new String(Character.toChars(codePoint));
        }
    }

}
