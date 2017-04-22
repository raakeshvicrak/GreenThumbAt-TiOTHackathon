package com.greenthumb.greenthumb;

import android.app.Application;

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
}
