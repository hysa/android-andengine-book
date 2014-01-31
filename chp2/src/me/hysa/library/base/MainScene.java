/*
 * MainScene.java
 *
 * Copyright 2014 hysa, All Rights Reserved.
 */

package me.hysa.library.base;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * @author hysa
 *
 */
public class MainScene extends Scene {

    // Sceneを管理するActivityのインスタンスを保持
    // アプリの場合のContextと同じように利用できる
    private BaseGameActivity baseActivity;
    private ResourceUtil resourceUtil;

    public MainScene(BaseGameActivity baseActivity) {
        this.baseActivity = baseActivity;
        init();
    }

    public void init() {
        resourceUtil = ResourceUtil.getInstance(baseActivity);
        attachChild(resourceUtil.getSprite("main_bg.png"));
    }
}
