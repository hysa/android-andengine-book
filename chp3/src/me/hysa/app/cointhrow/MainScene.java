/*
 * MainScene.java
 *
 * Copyright 2014 hysa, All Rights Reserved.
 */

package me.hysa.app.cointhrow;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;
import android.view.KeyEvent;

/**
 * @author hysa
 *
 */
public class MainScene extends KeyListenScene implements IOnSceneTouchListener {
    private AnimatedSprite mCoin;

    // ドラッグ開始位置
    private float[] mTouchStartPoint;
    // フリック中か否か
    private boolean mIsDragging;
    // 画面のタッチ可否
    private boolean mIsTouchEnabled;
    // コインが飛んでいるか否か
    private boolean mIsCoinFlying;
    // コインが飛び出す角度
    private double mFlyAngle;
    // コインのx座標移動速度
    private float mFlyXVelocity;
    // コインのy座標移動速度
    private float mInitialCoinSpeed;

    public MainScene(MultiSceneActivity baseActivity) {
        super(baseActivity);
        init();
    }

    public void init() {
        attachChild(getBaseActivity().getResourceUtil().getSprite("main_bg.png"));

        mTouchStartPoint = new float[2];
        mIsTouchEnabled = true;
        mIsDragging = false;
        mIsCoinFlying = false;
        // コインのy座標移動の初速を決定
        mInitialCoinSpeed = 30f;
        setOnSceneTouchListener(this);

        // アップデートハンドラーを登録
        registerUpdateHandler(mUpdateHandler);

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

    /**
     * タッチイベントが発生したら呼ばれる
     * @see org.andengine.entity.scene.IOnSceneTouchListener#onSceneTouchEvent(org.andengine.entity.scene.Scene, org.andengine.input.touch.TouchEvent)
     */
    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        float x = pSceneTouchEvent.getX();
        float y = pSceneTouchEvent.getY();

        // 指が触れた瞬間のイベント
        // タッチの座標がコイン上であるかチェック
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN
                && mIsTouchEnabled
                && (x > mCoin.getX() && x < mCoin.getX() + mCoin.getWidth())
                && (y > mCoin.getY() && y < mCoin.getY() + mCoin.getHeight())) {
            // フラグ
            mIsTouchEnabled = false;
            mIsDragging = true;

            // 開始点を記録
            mTouchStartPoint[0] = x;
            mTouchStartPoint[1] = y;

        // 指が触れた時、何らかの原因でタッチ処理が中断した場合のイベント
        } else if ((pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP
                    || pSceneTouchEvent.getAction() == TouchEvent.ACTION_CANCEL)
                    && mIsDragging
                ) {

            // 終点を記録
            float[] touchEndPoint = new float[2];
            touchEndPoint[0] = x;
            touchEndPoint[1] = y;

            // フリックの距離が短すぎるときにはフリックと判定しない
            if (touchEndPoint[0] - mTouchStartPoint[0] < 50
                    && touchEndPoint[0] - mTouchStartPoint[0] > -50
                    && touchEndPoint[1] - mTouchStartPoint[1] < 50
                    && touchEndPoint[1] - mTouchStartPoint[1] > -50) {
                mIsTouchEnabled = true;
                mIsDragging = false;
                return true;
            }

            // フリックの角度を求める
            mFlyAngle = getAngleByTwoPosition(mTouchStartPoint, touchEndPoint);
            // 下から上へのフリックを0°に調整
            mFlyAngle -= 180;
            // フリックの角度が前向きでない時はフリックを無効に
            if (mFlyAngle < -80 || mFlyAngle > 80) {
                mIsTouchEnabled = true;
                mIsDragging = false;
                return true;
            }

            // コインのx座標移動速度を調整
            mFlyXVelocity = (float) (mFlyAngle / 10.0f);
            // フラグをON
            mIsCoinFlying = true;
            // アニメーション開始
            mCoin.animate(50);
        }
        return true;

    }

    // アップデートハンドラ。1秒間に60会呼び出される
    private TimerHandler mUpdateHandler = new TimerHandler(1f / 60f, true, new ITimerCallback() {

        @Override
        public void onTimePassed(TimerHandler pTimerHandler) {
            // コインが飛んでいるなら実行
            if (mIsCoinFlying) {
                // コインのy座標移動速度が3以上の時
                if (mInitialCoinSpeed > 3.0f) {
                    // 速度を徐々に落とす
                    mInitialCoinSpeed *= 0.96f;
                }
                // コインを徐々に小さく
                mCoin.setScale(mCoin.getScaleX() * 0.97f);
                // x座標の移動
                mCoin.setX(mCoin.getX() + mFlyXVelocity);
                // xの移動量を徐々に大きく
                mFlyXVelocity *= 1.03f;
                // y座標の移動
                mCoin.setY(mCoin.getY() - mInitialCoinSpeed);
            }
        }
    });


    /**
     * ２点間の角度を求める公式
     *
     * @param start 開始
     * @param end 終了
     * @return 角度
     */
    private double getAngleByTwoPosition(float[] start, float[] end) {

        double result = 0;

        float xDistance = end[0] - start[0];
        float yDistance = end[1] - start[1];

        result = Math.atan2((double) yDistance, (double) xDistance) * 180 / Math.PI;
        result += 270;
        return result;
    }
}

