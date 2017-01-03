package com.minehut.gameplate.tabList.entries;

public class EmptyTabEntry extends TabEntry {

    public EmptyTabEntry() {
        super(getProfile(DEFAULT_PROPERTY, DEFAULT_NAME));
        load();
    }

}
