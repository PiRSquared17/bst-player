/*
 *  Copyright 2010 Sikiru Braheem <sbraheem at bramosystems . com>.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.Plugin;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class PlayerWidget extends Widget {

    private BeforeUnloadCallback callback;
    private HashMap<String, String> params;
    private Plugin plugin;
    private String playerId, mediaURL;
    private ArrayList<String> mediaURLs;
    private boolean autoplay, isMultiSource;

    private PlayerWidget() {
        setElement(DOM.createDiv());
        params = new HashMap<String, String>();
    }

    public PlayerWidget(Plugin plugin, String playerId, String mediaURL,
            boolean autoplay, BeforeUnloadCallback callback) {
        this();
        this.callback = callback;
        this.plugin = plugin;
        this.playerId = playerId;
        this.autoplay = autoplay;
        this.mediaURL = mediaURL;
    }

    public PlayerWidget(Plugin plugin, String playerId, ArrayList<String> mediaURL,
            boolean autoplay, BeforeUnloadCallback callback) {
        this();
        this.callback = callback;
        this.plugin = plugin;
        this.playerId = playerId;
        this.mediaURLs = mediaURL;
        this.autoplay = autoplay;
        isMultiSource = true;
    }

    public void addParam(String name, String value) {
        params.put(name, value);
    }

    public void removeParam(String name) {
        params.remove(name);
    }

    public String getParam(String name) {
        return params.get(name);
    }

    @Override
    protected void onLoad() {
        injectWidget(false);
    }

    @Override
    protected void onUnload() {
        if (callback != null) {
            callback.onBeforeUnload();
        }
    }

    @Override
    public void setHeight(String height) {
        Element _e = getElement().getFirstChildElement();
        if (_e != null) {
            _e.setAttribute("height", height);
            _e.getStyle().setProperty("height", height);
        }
        super.setHeight(height);
    }

    @Override
    public void setWidth(String width) {
        Element _e = getElement().getFirstChildElement();
        if (_e != null) {
            _e.setAttribute("width", width);
            _e.getStyle().setProperty("width", width);
        }
        super.setWidth(width);
    }

    public void replace(Plugin plugin, String playerId, String mediaURL, boolean autoplay) {
        this.plugin = plugin;
        this.playerId = playerId;
        this.autoplay = autoplay;
        this.mediaURL = mediaURL;
        injectWidget(true);
    }

    public void replace(Plugin plugin, String playerId, ArrayList<String> mediaURL, boolean autoplay) {
        this.plugin = plugin;
        this.playerId = playerId;
        this.mediaURLs = mediaURL;
        this.autoplay = autoplay;
        isMultiSource = true;
        injectWidget(true);
    }

    private void injectWidget(boolean updateDimension) {
        Element e = DOM.createDiv();
        PlayerWidgetFactory pf = PlayerWidgetFactory.get();
        switch (plugin) {
            case Native:
                if (isMultiSource) {
                    e = pf.getNativeElement(playerId, mediaURLs, autoplay);
                } else {
                    e = pf.getNativeElement(playerId, mediaURL, autoplay);
                }
                break;
            case FlashPlayer:
                e = pf.getSWFElement(playerId, mediaURL, params);
                break;
            case QuickTimePlayer:
                e = pf.getQTElement(playerId, mediaURL, autoplay);
                break;
            case VLCPlayer:
                e = pf.getVLCElement(playerId, "", false);
                break;
            case WinMediaPlayer:
                e = pf.getWMPElement(playerId, mediaURL, autoplay, params);
                break;
        }
        if(updateDimension) {
            String curHeight = getElement().getFirstChildElement().getAttribute("height");
            String curWidth = getElement().getFirstChildElement().getAttribute("width");
            e.setAttribute("height", curHeight);
            e.getStyle().setProperty("height", curHeight);
            e.setAttribute("width", curWidth);
            e.getStyle().setProperty("width", curWidth);
        }
        getElement().setInnerHTML(e.getString());
    }
}
