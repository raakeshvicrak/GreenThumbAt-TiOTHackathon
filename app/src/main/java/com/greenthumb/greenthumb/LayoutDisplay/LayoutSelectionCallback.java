package com.greenthumb.greenthumb.LayoutDisplay;

/**
 * Created by raakeshpremkumar on 4/22/17.
 */

public class LayoutSelectionCallback {

    interface SlideCreationPageCallback{
        void slideLayoutSelected(int points_sno);
    }

    SlideCreationPageCallback slideCreationPageCallback;

    public void registerCallBack(SlideCreationPageCallback slideCreationPageCallback){
        this.slideCreationPageCallback=slideCreationPageCallback;
    }

    public void slideLayoutSelectedTrigger(int points_sno){
        slideCreationPageCallback.slideLayoutSelected(points_sno);
    }
}
