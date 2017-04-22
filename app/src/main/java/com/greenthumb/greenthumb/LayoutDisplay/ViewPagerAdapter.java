package com.greenthumb.greenthumb.LayoutDisplay;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.greenthumb.greenthumb.CustomViews.LayoutSkeletonView;
import com.greenthumb.greenthumb.DB.Model.TableLayoutPointsPojo;
import com.greenthumb.greenthumb.LayoutMetrics.LayoutMetricsPojo;

import java.util.ArrayList;

/**
 * Created by raakeshpremkumar on 4/22/17.
 */

public class ViewPagerAdapter<T> extends PagerAdapter {

    Context context;
    ArrayList<String> points_for_setarray=new ArrayList<String>();
    ArrayList<T> layout_points_rows=new ArrayList<T>();
    int layout_width_individual=0,layout_height_individual=0;
    private LayoutMetricsPojo metrics_object;

    public ViewPagerAdapter(Context context,ArrayList<String> points_for_setarray,ArrayList<T> layout_points_rows,LayoutMetricsPojo metrics_object, int layout_width_individual, int layout_height_individual){
        this.context=context;
        this.points_for_setarray=points_for_setarray;
        this.layout_width_individual=layout_width_individual;
        this.layout_height_individual=layout_height_individual;
        this.layout_points_rows=layout_points_rows;
        this.metrics_object=metrics_object;
    }

    @Override
    public int getCount() {
        return points_for_setarray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        TableLayoutPointsPojo points_pojo_object=null;
        int points_sno=Integer.parseInt(points_for_setarray.get(position));
        for (int i=0;i<layout_points_rows.size();i++){
            points_pojo_object=(TableLayoutPointsPojo)layout_points_rows.get(i);
            if(points_pojo_object.getS_no()==points_sno){
                break;
            }
        }

        if(points_pojo_object!=null){
            LayoutSkeletonView layoutSkeletonView=new LayoutSkeletonView(context);
            layoutSkeletonView.setLayoutPoints(points_pojo_object.getLayout_points(), metrics_object, false,layout_width_individual,layout_height_individual);
            layoutSkeletonView.invalidate();
            container.addView(layoutSkeletonView);
            layoutSkeletonView.setOnClickListener(new ViewpagerOnClickListener(Integer.parseInt(points_for_setarray.get(position)), points_pojo_object));
            return layoutSkeletonView;
        }
        else{
            return null;
        }

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    private class ViewpagerOnClickListener implements View.OnClickListener{

        int points_sno=0;
        TableLayoutPointsPojo points_pojo_object;

        public ViewpagerOnClickListener(int points_sno,TableLayoutPointsPojo points_pojo_object){
            this.points_sno=points_sno;
            this.points_pojo_object=points_pojo_object;
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(context,""+points_sno+"  "+points_pojo_object.getS_no()+" "+points_pojo_object.getLayout_points(),Toast.LENGTH_LONG).show(); //NO I18N
            LayoutSelectionCallback layoutSelectionCallback=new LayoutSelectionCallback();
            layoutSelectionCallback.registerCallBack((LayoutDisplay) context);
            layoutSelectionCallback.slideLayoutSelectedTrigger(points_pojo_object.getS_no());
        }
    }

}

