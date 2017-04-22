package com.greenthumb.greenthumb.LayoutDisplay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.greenthumb.greenthumb.DB.DataBaseManager;
import com.greenthumb.greenthumb.DB.DataBaseQuery;
import com.greenthumb.greenthumb.DB.Model.TableLayoutSetsPojo;
import com.greenthumb.greenthumb.LayoutMetrics.LayoutMetricsPojo;
import com.greenthumb.greenthumb.R;
import com.greenthumb.greenthumb.SlideCreation.SlideLayoutCreator;
import com.greenthumb.greenthumb.Utils.GreenThumbUtils;

import java.util.ArrayList;

/**
 * Created by raakeshpremkumar on 4/22/17.
 */

public class LayoutDisplay<T> extends AppCompatActivity implements LayoutSelectionCallback.SlideCreationPageCallback {

    LayoutMetricsPojo metrics_object;
    private int layout_width_individual=0,layout_height_individual=0,viewpager_totalno=0, difference=0;
    RelativeLayout layoutselectormain;
    ArrayList<T> layout_sets_rows,layout_points_rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_display);

        layoutselectormain=(RelativeLayout)findViewById(R.id.layoutselectormain);

        init();
    }

    /*
     * Code to make the Activity Immersive - Full Screen.
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if (hasFocus) {
                if(!new GreenThumbUtils().isTablet(LayoutDisplay.this)){
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
                else{
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        }
    }

    private void init(){
        displaymetrics_calculator();
        layout_Selector_widthCalculator();
        retrieve_layoutSets_DB();
        retrieve_layoutPoints_DB();
        layout_selector_formation(viewpager_totalno);
    }

    /*
    * Function to calculate the display metrics of the specific device.
    */
    private void displaymetrics_calculator(){
        LayoutMetricsPojo metrics_object=new LayoutMetricsPojo();
        metrics_object.display_metrics_calculator(LayoutDisplay.this);
        this.metrics_object=metrics_object;
    }

    private void layout_Selector_widthCalculator(){
        float width_individual= (float) (metrics_object.getWidth()/5.0f);
        float height_individual=(float) (metrics_object.getHeight()/5.0f);
        layout_width_individual=(int)width_individual;
        layout_height_individual=(int)height_individual;
        viewpager_totalno=4;
        difference =(metrics_object.getOriginalwidth()-layout_width_individual*4)/5;
    }

    private <T> void retrieve_layoutSets_DB(){
        layout_sets_rows=DataBaseManager.getInstance(LayoutDisplay.this).retrieveTablerows(DataBaseQuery.TABLE_LAYOUT_SETS,null,null);
    }

    private <T> void retrieve_layoutPoints_DB(){
        layout_points_rows= DataBaseManager.getInstance(LayoutDisplay.this).retrieveTablerows(DataBaseQuery.TABLE_LAYOUT_POINTS,null,null);
    }

    private void layout_selector_formation(int viewpager_totalno){
        String[] points_for_setarray;


        /*
         * The layout params for each layout is defined below.
         */

        LinearLayout.LayoutParams parentlayout_params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(layout_width_individual,layout_height_individual);

        LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.setMargins(difference,0,0,0);

        LinearLayout.LayoutParams dots_layoutparams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,40);


        ScrollView scrollview=new ScrollView(LayoutDisplay.this);
        scrollview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout scrollview_linearlayout=new LinearLayout(LayoutDisplay.this);
        scrollview_linearlayout.setLayoutParams(parentlayout_params);
        scrollview_linearlayout.setOrientation(LinearLayout.VERTICAL);

        for(int i=0;i<layout_sets_rows.size();i=i+viewpager_totalno){

            LinearLayout layout_selector_linearlayout=new LinearLayout(LayoutDisplay.this);
            layout_selector_linearlayout.setLayoutParams(parentlayout_params);
            layout_selector_linearlayout.setOrientation(LinearLayout.HORIZONTAL);
            layout_selector_linearlayout.setPadding(0, difference, 0, 0);

            for(int j=i;j<i+4 && j<layout_sets_rows.size();j++){
                TableLayoutSetsPojo sets_pojo_object= (TableLayoutSetsPojo) layout_sets_rows.get(j);
                String points_for_set=sets_pojo_object.getLayoutpointssno();
                if(points_for_set.contains(",")){
                    points_for_setarray=points_for_set.split(","); //NO I18N
                }
                else{
                    points_for_setarray=new String[1];
                    points_for_setarray[0]=points_for_set;
                }

                LinearLayout viewpager_linearlayout=new LinearLayout(LayoutDisplay.this);
                viewpager_linearlayout.setLayoutParams(layoutParams1);
                viewpager_linearlayout.setOrientation(LinearLayout.VERTICAL);

                /*
                 * Code for creating viewpager and the dots layout indicating which item of viewpager the user is currently viewing.
                 */

                ViewPager layout_viewpager=new ViewPager(LayoutDisplay.this);
                layout_viewpager.setLayoutParams(layoutParams);

                LinearLayout dots_layout=new LinearLayout(LayoutDisplay.this);
                dots_layout.setOrientation(LinearLayout.HORIZONTAL);
                dots_layout.setGravity(Gravity.CENTER);

                /*
                 * code for creating the dots and adding them to linear layout for dots.
                 */

                TextView[] dots = new TextView[points_for_setarray.length];

                for (int k = 0; k < points_for_setarray.length; k++) {
                    dots[k] = new TextView(this);
                    dots[k].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    dots[k].setText(Html.fromHtml("&#8226;") + " ");//No I18N
                    dots[k].setTextSize(25);
                    dots[k].setTextColor(Color.parseColor("#BDBDBD"));//No I18N

                    dots_layout.addView(dots[k]);
                }
                dots[0].setTextColor(Color.parseColor("#fe3d32"));//No I18N


                ArrayList<String> points_for_setlisttemp=new ArrayList<String>();
                for (int m=0;m<points_for_setarray.length;m++){
                    points_for_setlisttemp.add(points_for_setarray[m]);
                }

                ViewPagerAdapter viewpageradapter=new ViewPagerAdapter(LayoutDisplay.this,points_for_setlisttemp,layout_points_rows,metrics_object, layout_width_individual, layout_height_individual);

                layout_viewpager.setCurrentItem(0);
                layout_viewpager.setAdapter(viewpageradapter);
                layout_viewpager.addOnPageChangeListener(new CustomOnPageChangeListener(points_for_setarray,dots));

                viewpager_linearlayout.addView(layout_viewpager);
                viewpager_linearlayout.addView(dots_layout);

                layout_selector_linearlayout.addView(viewpager_linearlayout);
            }

            scrollview_linearlayout.addView(layout_selector_linearlayout);
        }

        scrollview.addView(scrollview_linearlayout);
        layoutselectormain.addView(scrollview);

    }

    @Override
    public void slideLayoutSelected(int points_sno) {
        Intent intent=new Intent(LayoutDisplay.this, SlideLayoutCreator.class);
        intent.putExtra("points_sno",String.valueOf(points_sno)); //NO I18N
        startActivity(intent);
    }

    class CustomOnPageChangeListener implements ViewPager.OnPageChangeListener {

        String[] points_for_setarray;
        TextView[] dots;

        public CustomOnPageChangeListener(String[] points_for_setarray,TextView[] dots){
            this.points_for_setarray=points_for_setarray;
            this.dots=dots;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            for (int i = 0; i < points_for_setarray.length; i++) {
                dots[i].setTextColor(Color.parseColor("#BDBDBD"));//No I18N
            }
            dots[position].setTextColor(Color.parseColor("#fe3d32"));//No I18N
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
