package com.example.buiderdream.programmercommunity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/15.
 * @author 李秉龙
 */

public class TopicTheme implements Serializable{
    private String themeName;
    private String themeURL;
    private boolean isSelected;

    public TopicTheme(String themeName, String themeURL) {
        this.themeName = themeName;
        this.themeURL = themeURL;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeURL() {
        return themeURL;
    }

    public void setThemeURL(String themeURL) {
        this.themeURL = themeURL;
    }
}
