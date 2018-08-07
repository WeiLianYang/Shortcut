# Shortcut
[我的博客地址](https://blog.csdn.net/java_android_man/article/details/81047888)
<br>

### GIF效果图：
<br><br>
![image](https://github.com/LuckyYangChen/Shortcut/blob/master/1.gif)
![image](https://github.com/LuckyYangChen/Shortcut/blob/master/2.gif)

## 一、背景介绍

  3D Touch是一种立体触控技术，被苹果称为新一代多点触控技术，是在Apple Watch上采用的Force Touch，屏幕可感应不同的感压力度触控。3D Touch，苹果iPhone 6s的新功能，看起来类似 PC 上的右键。有Peek Pop 两种新手势。

随着IOS在iphone 6s 上加入了3d touch后，果粉手机很多应用都有了快捷访问的功能。

3D touch是需要硬件支持的，但是谷歌开放了系统，没有牢牢把控住硬件。就算谷歌实现了3Dtouch，其他安卓厂商估计也没这么快更新硬件。

但是我们也没有必要暗自菲薄不是，开放有开放的好处，封闭有封闭的缺点。

## 二、安卓实现：
好了，废话不多说。这篇文章的主题是介绍怎么在安卓上实现类似IOS 3d touch的功能。在安卓系统上，这个功能被称为

应用程序快捷方式（shortcut），显示的入口是长按应用图标，也可以拖动快捷方式到桌面形成一个独立的入口。

首先，看看谷歌关于shortcut的介绍:

如果有梯子点这:
[谷歌关于shortcut介绍](https://developer.android.com/guide/topics/ui/shortcuts)

### 应用程序快捷方式

如果您的应用定位到Android 7.1（API级别25）或更高级别，则可以定义 应用中特定操作的快捷方式。
<br>这些快捷方式可以显示在支持的启动器中。快捷方式可让您的用户快速启动应用内的常见或推荐任务。<br>

每个快捷方式都引用一个或多个 意图，当用户选择快捷方式时，每个意图都会在应用中启动特定操作。<br>

#### 您可以表示为快捷方式的操作示例包括：

* 将用户导航到地图应用中的特定位置<br>
* 在通信应用中向朋友发送消息<br>
* 在媒体应用中播放电视节目的下一集<br>
* 游戏在应用中加载求最后一个保存点<br>

#### 您可以为您的应用发布以下类型的快捷方式：

* 静态快捷方式在打包到APK或应用程序包中的资源文件中定义<br>
* 只有在运行时，您的应用才能发布，更新和删除动态快捷方式<br>
* 如果用户授予权限，则固定快捷方式可以在运行时固定到受支持的启动器。<br>

#### 注意：用户还可以通过将应用程序的静态和动态快捷方式复制到启动器上来创建固定快捷方式

您可以一次为应用程序发布最多五个快捷方式（静态快捷方式和动态快捷方式组合）。

但是，某些启动器应用程序不会显示您为应用创建的所有静态和动态快捷方式。<br>
用户可以创建的应用固定快捷方式的数量没有限制。即使您的应用无法移除固定的快捷方式，它仍然可以禁用它们。<br>

#### 注意：虽然其他应用无法访问快捷方式中的数据，但启动器本身可以访问此数据。<br>因此，这些数据应隐藏敏感的用户信息。

#### 通过介绍我们知道：

* 有动态和静态两种注册快捷方式；<br>
* 需要注意的是要保护用户的敏感信息，比如订单之类的信息；<br>
* 可以动态发布，更新和删除；<br>
* 可以固定它们到桌面，作为一种快捷方式的入口。<br>

## 关键代码
```
public void createShortcut() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            try {
                boolean isShortcutCreated = PreferencesUtil.readBoolean("isShortcutCreated", false);
                if (!isShortcutCreated) {// 如果有创建过就不再重复创建，可以考虑从接口拉取
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

```
