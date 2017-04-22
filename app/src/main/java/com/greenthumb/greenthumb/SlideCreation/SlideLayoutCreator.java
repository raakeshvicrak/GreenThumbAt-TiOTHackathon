package com.greenthumb.greenthumb.SlideCreation;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greenthumb.greenthumb.CustomViews.LayoutMaskingView;
import com.greenthumb.greenthumb.CustomViews.LayoutSkeletonView;
import com.greenthumb.greenthumb.DB.DataBaseManager;
import com.greenthumb.greenthumb.DB.DataBaseQuery;
import com.greenthumb.greenthumb.DB.Model.TableLayoutPointsPojo;
import com.greenthumb.greenthumb.LayoutDisplay.LayoutDisplay;
import com.greenthumb.greenthumb.LayoutMetrics.LayoutMetricsPojo;
import com.greenthumb.greenthumb.PolygonsUtil.Point;
import com.greenthumb.greenthumb.PolygonsUtil.Polygon;
import com.greenthumb.greenthumb.R;
import com.greenthumb.greenthumb.Utils.GreenThumbUtils;

import java.util.ArrayList;

/**
 * Created by raakeshpremkumar on 4/22/17.
 */

public class SlideLayoutCreator<T> extends AppCompatActivity {

    LinearLayout slide_creation_linearlayout,layout_controller_mainlayout,layout_navigator_mainlayout,layout_controller_imagecontroller,layout_controller_textcontroller;
    FrameLayout parent_frameLayout,slide_creation_framelayout;
    LayoutMetricsPojo metrics_object;
    DataBaseManager dataBaseManager;
    ArrayList<T> table_layoutpoints_pojots_list=new ArrayList<T>();
    private String[] differentLayoutPaths;
    private ArrayList<LayoutSkeletonView> layoutSkeletonViewList=new ArrayList<LayoutSkeletonView>();
    private ArrayList<LayoutMaskingView> layoutMaskingViewList=new ArrayList<LayoutMaskingView>();
    private ArrayList<Polygon> myPolygonList=new ArrayList<Polygon>();
    private byte previousActiveGrid=-1,touchCount=0;
    private byte current_controllerparent=-1,current_controllerchild=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_creation_layout);

        slide_creation_linearlayout = (LinearLayout) findViewById(R.id.slide_creation_linearlayout);
        slide_creation_framelayout = (FrameLayout)findViewById(R.id.slide_creation_framelayout);

        String points_sno_selected=getIntent().getStringExtra("points_sno"); //NO I18N

        if(points_sno_selected!=null && !points_sno_selected.equals("")){ //NO I18N
            init(points_sno_selected);
        }
        else{
            selection_error();
        }
    }


    /*
     * Code to make the Activity Immersive - Full Screen.
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if (hasFocus) {
                if(!new GreenThumbUtils().isTablet(SlideLayoutCreator.this)){
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
                else{
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        }
    }



    private void init(String points_sno_selected) {
        displaymetrics_calculator();
        table_layoutpoints_pojots_list=DataBaseManager.getInstance(SlideLayoutCreator.this).retrieveTablerows(DataBaseQuery.TABLE_LAYOUT_POINTS,DataBaseQuery.S_NO,new String[]{points_sno_selected});
        if(table_layoutpoints_pojots_list.size()>0){
            TableLayoutPointsPojo table_layoutPoints_pojo=(TableLayoutPointsPojo)table_layoutpoints_pojots_list.get(0);
            drawSlideLayout(table_layoutPoints_pojo);
        }
    }

    /*
    * Function to calculate the display metrics of the specific device.
    */
    private void displaymetrics_calculator(){
        LayoutMetricsPojo metrics_object=new LayoutMetricsPojo();
        metrics_object.display_metrics_calculator(SlideLayoutCreator.this);
        this.metrics_object=metrics_object;
    }

    /*
    * Code for creating the skeleton layout selected by the user.
    */
    private void drawSlideLayout(TableLayoutPointsPojo table_layoutPoints_pojo){
        parent_frameLayout=new FrameLayout(SlideLayoutCreator.this);
        FrameLayout.LayoutParams parent_framelayoutparams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        parent_framelayoutparams.setMargins((metrics_object.getOriginalwidth() - metrics_object.getWidth()) / 2, 0, 0, 0);
        parent_frameLayout.setLayoutParams(parent_framelayoutparams);

        String layoutPoints=table_layoutPoints_pojo.getLayout_points();

        if(layoutPoints.contains("#")){ //NO I18N
            differentLayoutPaths=layoutPoints.split("#"); //NO I18N
        }
        else{
            differentLayoutPaths=new String[1];
            differentLayoutPaths[0]=layoutPoints;
        }

        formPolygons(differentLayoutPaths,true);

        for (int i=1;i<differentLayoutPaths.length;i++){
            /*
             * The layout for Masking each grid.
             */
            LayoutMaskingView layoutMaskingView=new LayoutMaskingView(SlideLayoutCreator.this,differentLayoutPaths[i], metrics_object, true, metrics_object.getWidth(), metrics_object.getHeight());

            /*
             * persisting in the array list to perform re-arrangements of view on touch.
             */
            layoutMaskingViewList.add(layoutMaskingView);
                /*
                 * The View on which the layout of each grid is drew.
                 */
            LayoutSkeletonView layoutSkeletonView=new LayoutSkeletonView(SlideLayoutCreator.this);
            layoutSkeletonView.setLayoutPoints(differentLayoutPaths[i], metrics_object, true, metrics_object.getWidth(), metrics_object.getHeight());
            layoutSkeletonView.invalidate();
            //layoutSkeletonView.setBackgroundColor(Color.parseColor(SlideZUtil.whiteColor));
            layoutSkeletonView.setTag(""+i);

                /*
                 * Persisting it into an arraylist for future use
                 */
            layoutSkeletonViewList.add(layoutSkeletonView);
            layoutSkeletonView.setOnTouchListener(new OnTouchExtender(layoutSkeletonView.getTag().toString()));


            //layoutimageview.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));

            layoutMaskingView.addView(layoutSkeletonView);


            parent_frameLayout.addView(layoutMaskingView);
        }

        slide_creation_linearlayout.addView(parent_frameLayout);
    }

    /*
     * Forming polygons to validate which grid the user has interacted with.
     */
    private void formPolygons(String[] differentLayoutPathstemp, boolean isMainLayout){

        double height_ratio=metrics_object.getHeight()/768.0f;
        double width_ratio=metrics_object.getWidth()/1024.0f;

        for (int i=1;i<differentLayoutPathstemp.length;i++){
            String pointstemp[]=differentLayoutPathstemp[i].split(";"); //NO I18N
            Polygon.Builder builder=null;
            for (int j=0;j<pointstemp.length;j++){
                String individualpointstemp[]=pointstemp[j].split(","); //NO I18N

                if(individualpointstemp.length==2){
                    if(!individualpointstemp[0].equals("1")){ //NO I18N
                        if(isMainLayout==false){
                            individualpointstemp[0]=String.valueOf(Float.parseFloat(individualpointstemp[0])*width_ratio);
                        }
                    }
                    if(!individualpointstemp[1].equals("1")){ //NO I18N
                        individualpointstemp[1]=String.valueOf(Float.parseFloat(individualpointstemp[1])*height_ratio);
                    }
                    if(isMainLayout==true){
                        individualpointstemp[0]=String.valueOf((Float.parseFloat(individualpointstemp[0])*width_ratio)+((metrics_object.getOriginalwidth()-metrics_object.getWidth())/2.0));
                    }
                }

                if(builder==null){
                    builder= Polygon.builder();
                }
                builder.addVertex(new Point(Float.parseFloat(individualpointstemp[0]), Float.parseFloat(individualpointstemp[1])));
                if(j==pointstemp.length-1){
                    myPolygonList.add(builder.build());
                }
            }
        }
    }

    private void selection_error(){
        Toast.makeText(SlideLayoutCreator.this,"Error!",Toast.LENGTH_SHORT).show(); // NO I18N
        Intent intent=new Intent(SlideLayoutCreator.this, LayoutDisplay.class);
        startActivity(intent);
    }

    /*
     * On Touch listener for handling touch and switching between views.
     */
    private class OnTouchExtender implements View.OnTouchListener {

        private String tag=""; //NO I18N

        public OnTouchExtender(String tag){
            this.tag=tag;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_DOWN){
                Point touch_point=new Point(event.getX(),event.getY());
                for (int i=0;i<myPolygonList.size();i++){
                    if(myPolygonList.get(i).contains(touch_point)){

                        /*
                         * when the same grid is touched more than once consecutively.
                         */
                        if(previousActiveGrid==i){
                            touchCount++;
                            if(touchCount>1){
                                //slide_creation_controller_manager();
                            }
                            break;
                        }

                        touchCount=0;
                        touchCount++;
                        parent_frameLayout.bringChildToFront(layoutMaskingViewList.get(i));
                        layoutSkeletonViewList.get(i).setPaintProperties(12);
                        if(previousActiveGrid!=-1){
                            layoutSkeletonViewList.get(previousActiveGrid).setPaintProperties(3);
                        }
                        previousActiveGrid=(byte)i;
                        break;
                    }
                }
            }
            return false;
        }
    }

}

