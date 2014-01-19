package me.hysa.library.base;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends SimpleLayoutGameActivity {

    // 画面サイズ
    private int CAMERA_WIDTH = 480;
    private int CAMERA_HEIGHT = 800;

    /* (non-Javadoc)
     * @see org.andengine.ui.IGameInterface#onCreateEngineOptions()
     */
    @Override
    public EngineOptions onCreateEngineOptions() {
        // サイズを指定し描画範囲をインスタンス化
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        // ゲームのエンジンを初期化
        // 引数：タイトルバーを表示しないモード, 画面の向き, 解像度の縦横比を保ったまま最大まで拡大, 描画範囲
        EngineOptions eo = new EngineOptions(
                true,
                ScreenOrientation.PORTRAIT_FIXED,
                new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
                camera);
        return eo;
    }

    @Override
    protected void onCreateResources() {
        // TODO Auto-generated method stub

    }

    @Override
    protected Scene onCreateScene() {
        // MainSceneをインスタンス化し、エンジンにセット
        MainScene mainScene = new MainScene(this);
        return mainScene;
    }

    @Override
    protected int getLayoutID() {
        // ActivityのレイアウトのIDを返す
        return R.layout.activity_main;
    }

    @Override
    protected int getRenderSurfaceViewID() {
        // SceneがセットされるViewのIDを返す
        return R.id.renderview;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
