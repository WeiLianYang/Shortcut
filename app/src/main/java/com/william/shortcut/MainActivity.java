package com.william.shortcut;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean exit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createShortcut();
        handleShortcutEvent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleShortcutEvent();
    }

    private void handleShortcutEvent() {
        String targetAction = getIntent().getStringExtra("targetAction");
        if (!TextUtils.isEmpty(targetAction)) {
            TargetActivity.startTargetPage(this, targetAction);
        }
    }

    /**
     * 动态创建桌面图标长按菜单列表
     */
    public void createShortcut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            try {
                boolean isShortcutCreated = PreferencesUtil.readBoolean("isShortcutCreated", false);
                if (!isShortcutCreated) {// 如果有创建过就不再重复创建，后期看是否考虑从接口拉取
                    ShortcutManager mShortcutManager = getSystemService(ShortcutManager.class);
                    List<ShortcutInfo> infoList = new ArrayList<>();

                    infoList.add(createShortcutInfo(ShortcutUtil.ID1, "Action-1", 0,
                            R.mipmap.ic_launcher_round, "Action-1"));
                    infoList.add(createShortcutInfo(ShortcutUtil.ID2, "Action-2", 1,
                            R.mipmap.ic_launcher_round, "Action-2"));
                    infoList.add(createShortcutInfo(ShortcutUtil.ID3, "Action-3", 2,
                            R.mipmap.ic_launcher_round, "Action-3"));
                    infoList.add(createShortcutInfo(ShortcutUtil.ID4, "Action-4", 3,
                            R.mipmap.ic_launcher_round, "Action-4"));

                    if (mShortcutManager != null)
                        mShortcutManager.setDynamicShortcuts(infoList);

                    PreferencesUtil.writeBoolean("isShortcutCreated", true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ShortcutInfo createShortcutInfo(String id, String label, int rank, int iconResId, String targetAction) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            return new ShortcutInfo.Builder(this, id)
                    .setShortLabel("桌面" + label)// 设置桌面图标名称，可以拖动到桌面上的图标标题
                    .setLongLabel(label)// APP长按显示的标题
                    .setRank(rank)// 顺序显示，这个是相对的，它是始终按照离应用图标最近的顺序显示，如果图标在桌面最上和最下，显示的顺序是相反的
                    .setIcon(Icon.createWithResource(this, iconResId))// 快捷方式的图标
                    .setIntent(createIntent(targetAction))// 跳转的意图，官网推荐用setIntents()
//                    .setIntents(new Intent[]{})
                    .build();
        }
        return null;
    }

    private Intent createIntent(String targetAction) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.putExtra("targetAction", targetAction);
        return intent;
    }

    @Override
    public void onBackPressed() {
        if (!exit) {
            exit = true;
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            getWindow().getDecorView().postDelayed(() -> {
                if (!isFinishing()) {
                    exit = false;
                }
            }, 1500);
        } else {
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }

}
