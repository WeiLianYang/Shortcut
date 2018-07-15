package com.william.shortcut;

import android.content.Context;
import android.content.pm.ShortcutManager;
import android.os.Build;

/**
 * 作者：William 时间：2018/7/4
 * 类说明：桌面快捷方式工具类
 */
public class ShortcutUtil {
    public static final String ID1 = "shortcut-1";
    public static final String ID2 = "shortcut-2";
    public static final String ID3 = "shortcut-3";
    public static final String ID4 = "shortcut-4";

    public static void report(Context context, String id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager mShortcutManager = context.getSystemService(ShortcutManager.class);
            if (mShortcutManager != null) {
                mShortcutManager.reportShortcutUsed(id);
            }
        }
    }

}
