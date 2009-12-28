package com.bramosystems.oss.player.uibinder.client;

import com.bramosystems.oss.player.core.client.Plugin;
import com.google.gwt.uibinder.client.UiConstructor;

/**
 * 
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class MatrixSupport extends BinderPlayer {

    @UiConstructor
    public MatrixSupport(String mediaURL, boolean autoplay, String height, String width) {
        super(mediaURL, autoplay, height, width);
    }

    @Override
    protected Plugin getPlugin() {
        return Plugin.MatrixSupport;
    }
}
