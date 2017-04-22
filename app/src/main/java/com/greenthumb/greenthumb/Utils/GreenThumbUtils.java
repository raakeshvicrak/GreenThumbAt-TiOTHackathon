package com.greenthumb.greenthumb.Utils;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by raakeshpremkumar on 4/22/17.
 */

public class GreenThumbUtils {
    public static String whiteColor="#FFFFFF"; //No I18N

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
}
