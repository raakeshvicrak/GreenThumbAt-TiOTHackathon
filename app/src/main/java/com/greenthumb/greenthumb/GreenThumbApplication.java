package com.greenthumb.greenthumb;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.greenthumb.greenthumb.DB.DataBaseManager;
import com.greenthumb.greenthumb.Utils.FontAssetHelper;

/**
 * Created by raakeshpremkumar on 4/22/17.
 */

public class GreenThumbApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataBaseManager.setContext(this);
        FontAssetHelper.getTypeFaceCustomIcon_fonts(GreenThumbApplication.this);
        FontAssetHelper.getTypeface_FiraMonoBold(GreenThumbApplication.this);
        FontAssetHelper.getTypeface_FiraSansBold(GreenThumbApplication.this);
    }
    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }
}
