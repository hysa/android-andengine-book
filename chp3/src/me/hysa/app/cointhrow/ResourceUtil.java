/*
 * ResourceUtil.java
 *
 * Copyright 2014 hysa, All Rights Reserved.
 */

package me.hysa.app.cointhrow;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * リソースを管理するユーティリティクラス
 *
 * @author hysa
 *
 */
public class ResourceUtil {
    /** Asset path */
    public static final String ASSET_PATH = "gfx/";

    // Singleton
    private static ResourceUtil instance;
    // Context
    private static BaseGameActivity gameActivity;
    // TextureRegionの無駄な生成を防ぎ、再利用するための一時的な格納場所
    private static HashMap<String, ITextureRegion> textureRegionPool;
    // TiledTextureRegionの無駄な生成を防ぎ、再利用するための一時的な格納場所
    private static HashMap<String, TiledTextureRegion> tiledTextureRegionPool;

    private ResourceUtil() {}

    /**
     * 初期化関数
     * @param gameActivity
     * @return 唯一のインスタンス
     */
    public static ResourceUtil getInstance(BaseGameActivity gameActivity) {
        if (instance == null) {
            instance = new ResourceUtil();
            ResourceUtil.gameActivity = gameActivity;
            BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(ASSET_PATH);

            textureRegionPool = new HashMap<String, ITextureRegion>();
            tiledTextureRegionPool = new HashMap<String, TiledTextureRegion>();
        }
        return instance;
    }

    /**
     * ファイル名を与えてSpriteを得る
     *
     */
    public Sprite getSprite(String fileName) {
        Sprite sprite = null;
        // 同名のファイルからITextureRegionが生成済みであれば再利用
        if (textureRegionPool.containsKey(fileName)) {
            sprite = new Sprite(0, 0, textureRegionPool.get(fileName),
                            gameActivity.getVertexBufferObjectManager());

        } else {
            Bitmap bm = createBitmap(fileName);

            // Bitmapのサイズをもとに2のべき乗の値を取得、BitmapTextureAtlasの生成
            BitmapTextureAtlas bta = new BitmapTextureAtlas(
                    gameActivity.getTextureManager(),
                    getTwoPowerSize(bm.getWidth()), getTwoPowerSize(bm.getHeight()),
                    TextureOptions.BILINEAR_PREMULTIPLYALPHA);
            gameActivity.getEngine().getTextureManager().loadTexture(bta);
            ITextureRegion btr = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta, gameActivity, fileName, 0, 0);

            // 再生成を防ぐため、プールの登録
            textureRegionPool.put(fileName, btr);

            sprite = new Sprite(0, 0, btr, gameActivity.getVertexBufferObjectManager());
        }

        sprite.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        return sprite;
    }


    /**
     * パラパラアニメのようなSpriteを生成
     * 画像は一枚にまとめ、マス数とともに引数とする
     */
    public AnimatedSprite getAnimatedSprite(String fileName, int column, int row) {
        AnimatedSprite sprite = null;

        // 同名のファイルからITextureRegionが生成済みであれば再利用
        if (tiledTextureRegionPool.containsKey(fileName)) {
            sprite = new AnimatedSprite(0, 0, tiledTextureRegionPool.get(fileName),
                            gameActivity.getVertexBufferObjectManager());

        } else {
            Bitmap bm = createBitmap(fileName);

            // Bitmapのサイズをもとに2のべき乗の値を取得、BitmapTextureAtlasの生成
            BitmapTextureAtlas bta = new BitmapTextureAtlas(
                    gameActivity.getTextureManager(),
                    getTwoPowerSize(bm.getWidth()), getTwoPowerSize(bm.getHeight()),
                    TextureOptions.BILINEAR_PREMULTIPLYALPHA);
            gameActivity.getEngine().getTextureManager().loadTexture(bta);

            // TiledTextureRegion（タイル上のTextureRegion）を生成
            // マス数を与え、同じサイズのTextureRegionを用意
            TiledTextureRegion ttr = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
                    bta, gameActivity, fileName, 0, 0, column, row);

            tiledTextureRegionPool.put(fileName, ttr);

            sprite = new AnimatedSprite(0, 0, ttr, gameActivity.getVertexBufferObjectManager());
        }

        sprite.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); // アルファ値を反映させる
        return sprite;

    }

    /**
     * タップすると画像が切り替わるボタン機能を持つSpriteを生成。
     */
    public ButtonSprite getButtonSprite(String normal, String pressed) {
        ButtonSprite sprite = null;
        if (textureRegionPool.containsKey(normal) && textureRegionPool.containsKey(pressed)) {
            sprite = new ButtonSprite(
                    0, 0,
                    textureRegionPool.get(normal), textureRegionPool.get(pressed),
                    gameActivity.getVertexBufferObjectManager());
        } else {
            Bitmap bm = createBitmap(normal);
            // ボタン生成のためのTextureRegionの生成
            // TiledTextureRegionでなく、BuildableBitmapTextureAtlasを利用する
            BuildableBitmapTextureAtlas bta = new BuildableBitmapTextureAtlas(
                    gameActivity.getTextureManager(), getTwoPowerSize(bm.getWidth() * 2), getTwoPowerSize(bm.getHeight()));
            ITextureRegion trNormal = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta, gameActivity, normal);
            ITextureRegion trPressed = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta, gameActivity, pressed);

            try {
                bta.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
                bta.load();
            } catch (TextureAtlasBuilderException e) {
                Debug.e(e);
            }

            textureRegionPool.put(normal, trNormal);
            textureRegionPool.put(pressed, trPressed);

            sprite = new ButtonSprite(
                    0, 0, trNormal, trPressed, gameActivity.getVertexBufferObjectManager());

        }

        sprite.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        return sprite;
    }

    /**
     * ファイル名からBitmapを作成する
     * @param fileName
     * @return
     */
    private Bitmap createBitmap(String fileName) {
        // サイズを自動的に取得するためにBitmapとして読み込み
        InputStream is = null;
        try {
            is = gameActivity.getResources().getAssets().open(ASSET_PATH + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bm = BitmapFactory.decodeStream(is);
        return bm;
    }

    /**
     * プールを開放、シングルトンを削除するための関数
     */
    public void resetAllTexture() {
        // Activity.finish()だけだとシングルトンなクラスがnullにならないため、明示的にnullを代入
        instance = null;
        textureRegionPool.clear();
        tiledTextureRegionPool.clear();
    }

    /**
     * 2のべき乗を求める
     *
     * @param length
     * @return べき乗の値
     */
    private int getTwoPowerSize(float length) {
        int value = (int) (length + 1);
        int pow2value = 64;
        while (pow2value < value) {
            pow2value *= 2;
        }
        return pow2value;
    }
}
