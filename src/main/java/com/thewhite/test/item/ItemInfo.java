package com.thewhite.test.item;

import com.thewhite.test.util.Util;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class ItemInfo {

    protected final String name;
    protected final String basename;
    protected final String extension;
    @Setter
    protected String resultName;

    public ItemInfo(String name) {
        this.name = Util.getNameWithoutUUID(name);
        this.basename = Util.getBasename(this.name);
        this.extension = Util.getExtension(this.name);
    }
}
