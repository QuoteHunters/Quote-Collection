package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dto.ThemeDTO;
import com.quotehunters.quotecollection.model.service.ThemeService;

import java.util.List;

public class ThemeController {
    private final ThemeService themeService = new ThemeService();

    public int insertTheme(String themeName) {
        return themeService.insertTheme(themeName);
    }

    public boolean existsTheme(String themeName) {
        return themeService.existsTheme(themeName);
    }

    public boolean existsTheme(int id, String themeName) {
        return themeService.existsTheme(id, themeName);
    }

    public List<ThemeDTO> selectThemes() {
        return themeService.selectThemes();
    }

    public int updateTheme(int id, String themeName) {
        return themeService.updateTheme(id, themeName);
    }

    public int deleteTheme(int id) {
        return themeService.deleteTheme(id);
    }
}
