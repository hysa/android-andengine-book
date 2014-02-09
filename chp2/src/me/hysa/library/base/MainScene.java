/*
 * MainScene.java
 *
 * Copyright 2014 hysa, All Rights Reserved.
 */

package me.hysa.library.base;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;

/**
 * @author hysa
 *
 */
public class MainScene extends KeyListenScene {

    public MainScene(MultiSceneActivity baseActivity) {
        super(baseActivity);
        init();
    }

    public void init() {
        attachChild(getBaseActivity().getResourceUtil().getSprite("main_bg.png"));
    }

    @Override
    public void prepareSoundAndMusic() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        return false;
    }
}

