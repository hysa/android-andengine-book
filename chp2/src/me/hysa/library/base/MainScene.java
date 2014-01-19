/*
 * MainScene.java
 *
 * Copyright 2014 hysa, All Rights Reserved.
 */

package me.hysa.library.base;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * @author hysa
 *
 */
public class MainScene extends Scene {

    // Sceneを管理するActivityのインスタンスを保持
    // アプリの場合のContextと同じように利用できる
    private BaseGameActivity baseActivity;

    public MainScene(BaseGameActivity baseActivity) {
        this.baseActivity = baseActivity;
        init();
    }

    public void init() {

    }

}
