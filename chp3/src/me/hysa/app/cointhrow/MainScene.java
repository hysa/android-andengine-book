/*
 * MainScene.java
 *
 * Copyright 2014 hysa, All Rights Reserved.
 */

package me.hysa.app.cointhrow;

import org.andengine.entity.sprite.AnimatedSprite;

import android.view.KeyEvent;

/**
 * @author hysa
 *
 */
public class MainScene extends KeyListenScene {
    private AnimatedSprite mCoin;

    public MainScene(MultiSceneActivity baseActivity) {
        super(baseActivity);
        init();
    }

    public void init() {
        attachChild(getBaseActivity().getResourceUtil().getSprite("main_bg.png"));

        setNewCoin();
    }


    /**
     * コインを初期化する
     */
    private void setNewCoin() {
        // 古いコインが存在する場合は消去
        if (mCoin != null) {
            detachChild(mCoin);
        }

        // コイン画像の読み込みと座標設定
        mCoin = getBaseActivity().getResourceUtil().getAnimatedSprite("coin_100.png", 1, 3);
        placeToCenterX(mCoin, 600);
//        mCoin.animate(50); // 1コマ50ミリ秒

        attachChild(mCoin);


    }

    @Override
    public void prepareSoundAndMusic() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        return false;
    }
}

