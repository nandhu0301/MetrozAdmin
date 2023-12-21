package com.smiligenceUAT1.metrozadmin.bean;

/**
 * Created by anupamchugh on 22/12/17.
 */

public class MenuModel {

    public String menuName;
    public boolean hasChildren, isGroup;

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren) {

        this.menuName = menuName;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }
}
