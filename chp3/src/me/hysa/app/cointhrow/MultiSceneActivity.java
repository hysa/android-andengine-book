/*
 * MultiSceneActivity.java
 *
 * Copyright 2014 hysa, All Rights Reserved.
 */

package me.hysa.app.cointhrow;

import java.util.ArrayList;

import org.andengine.ui.activity.SimpleLayoutGameActivity;

/**
 * @author hysa
 *
 */
public abstract class MultiSceneActivity extends SimpleLayoutGameActivity {
    // ResourceUtilのインスタンス
    private ResourceUtil mResourceUtil;
    // 起動済みのSceneの配列
    private ArrayList<KeyListenScene> mSceneArray;


    /**
     * @see org.andengine.ui.activity.SimpleLayoutGameActivity#onCreateResources()
     */
    @Override
    protected void onCreateResources() {
        mResourceUtil = ResourceUtil.getInstance(this);
        mSceneArray = new ArrayList<KeyListenScene>();
    }

    public ResourceUtil getResourceUtil() {
        return mResourceUtil;
    }

    public ArrayList<KeyListenScene> getSceneArray() {
        return mSceneArray;
    }

    // 起動済みのKeyListenSceneを格納する配列
    public abstract void appendScene(KeyListenScene scene);
    // 最初のシーンに戻るための関数
    public abstract void backToInitial();
    // シーンとシーン格納配列を更新する関数
    public abstract void refreshRUnnningScene(KeyListenScene scene);
}
