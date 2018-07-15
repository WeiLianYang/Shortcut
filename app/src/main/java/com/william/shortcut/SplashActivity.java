package com.william.shortcut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 * 作者：William 时间：2018/7/14
 * 类说明：闪屏页
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String targetAction = getIntent().getStringExtra("targetAction");
        new Handler().postDelayed(() -> {
            // 真实的业务场景中，这里需要判断目标页面是否需要登录
            // 这里简单模拟1秒钟后跳转到目标页面，统一在MainActivity做跳转
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("targetAction", targetAction);
            startActivity(intent);
            finish();// 避免回退时又能重新看到闪屏页
        }, 1000);
    }
}
