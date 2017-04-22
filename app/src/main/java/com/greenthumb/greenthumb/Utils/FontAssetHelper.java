package com.greenthumb.greenthumb.Utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by raakeshpremkumar on 4/21/17.
 */

public class FontAssetHelper {

    public static Typeface typeface_icon_fonts=null,typeface_firamonobold=null,typeface_firasansbold=null;

    public static Typeface getTypeFaceCustomIcon_fonts(Context context){
        if(typeface_icon_fonts!=null){
            return typeface_icon_fonts;
        }
        typeface_icon_fonts=Typeface.createFromAsset(context.getAssets(),"iconsset.ttf");
        return typeface_icon_fonts;
    }

    public static Typeface getTypeface_FiraMonoBold(Context context){
        if(typeface_firamonobold!=null){
            return typeface_firamonobold;
        }
        typeface_firamonobold=Typeface.createFromAsset(context.getAssets(),"FiraMonoBold.ttf");
        return typeface_firamonobold;
    }

    public static Typeface getTypeface_FiraSansBold(Context context){
        if(typeface_firasansbold!=null){
            return typeface_firasansbold;
        }
        typeface_firasansbold=Typeface.createFromAsset(context.getAssets(),"FiraSansBold.ttf");
        return typeface_firasansbold;
    }
}
