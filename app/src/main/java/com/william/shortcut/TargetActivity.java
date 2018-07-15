package com.william.shortcut;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * 作者：William 时间：2018/7/14
 * 类说明：目标测试页面
 */
public class TargetActivity extends AppCompatActivity {

    public static void startTargetPage(Context context, String targetAction) {
        Intent intent = new Intent(context, TargetActivity.class);
        intent.putExtra("targetAction", targetAction);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        String targetAction = getIntent().getStringExtra("targetAction");
        ((TextView) findViewById(R.id.tv_action)).setText(targetAction);
        String shortcutId = null;
        if (targetAction != null) {
            switch (targetAction) {
                case "Action-1":
                    shortcutId = ShortcutUtil.ID1;
                    break;
                case "Action-2":
                    shortcutId = ShortcutUtil.ID2;
                    break;
                case "Action-3":
                    shortcutId = ShortcutUtil.ID3;
                    break;
                case "Action-4":
                    shortcutId = ShortcutUtil.ID4;
                    break;
            }
            if (shortcutId != null) {
                ShortcutUtil.report(this, shortcutId);
            }
        }
    }
}
