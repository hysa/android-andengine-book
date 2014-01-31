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

    public MainScene(BaseGameActivity baseActivity) {
        this.baseActivity = baseActivity;
        init();
    }

    public void init() {
        // 画像リソースが格納されている場所を指定
        // BitmapTextureAtlasTextureRegionFactoryはここでしか使用しない
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        // オブジェクトのサイズを指定。480、800が収まる2のべき乗。
        BitmapTextureAtlas bta = new BitmapTextureAtlas(
                baseActivity.getTextureManager(), 512, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        // 範囲をメモリ上に読み込み
        baseActivity.getTextureManager().loadTexture(bta);
        // メモリ上に読み込んだ範囲に画像を読み込み。座標は0,0。
        ITextureRegion btr = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
                bta, baseActivity, "main_bg.png", 0, 0);

        // Spriteをインスタンス化。座標は0, 0。
        Sprite bg = new Sprite(0, 0, btr, baseActivity.getVertexBufferObjectManager());
        // Spriteをアルファ値取り扱いを設定
        bg.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        // 画面に配置
        attachChild(bg);
    }
}
