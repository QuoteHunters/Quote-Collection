package com.quotehunters.quotecollection.model.dto;

public class ThemeDTO {
    private int theme_id;
    private String theme_name;

    public ThemeDTO() {}

    public ThemeDTO(int theme_id, String theme_name) {
        this.theme_id = theme_id;
        this.theme_name = theme_name;
    }

    public int getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(int theme_id) {
        this.theme_id = theme_id;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }

    @Override
    public String toString() {
        return "ThemeDTO{" +
                "theme_id=" + theme_id +
                ", theme_name='" + theme_name + '\'' +
                '}';
    }
}
