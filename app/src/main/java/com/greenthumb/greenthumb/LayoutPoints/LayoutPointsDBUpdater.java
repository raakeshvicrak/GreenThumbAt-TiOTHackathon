package com.greenthumb.greenthumb.LayoutPoints;

import android.content.ContentValues;
import android.content.Context;

import com.greenthumb.greenthumb.DB.DataBaseManager;
import com.greenthumb.greenthumb.DB.DataBaseQuery;

/**
 * Created by raakeshpremkumar on 4/21/17.
 */

public class LayoutPointsDBUpdater {

    private Context context;
    private DataBaseManager dataBaseManager;

    public LayoutPointsDBUpdater(Context context){
        this.context = context;
        insert_DB_layoutpoints();
    }

    private void insert_DB_layoutpoints(){

        ContentValues contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"1");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS,"1,1;1023,1;1023,767;1,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);

        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"121");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;1023,1;1023,767;1,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);

        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"2");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;256,1;256,767;1,767#256,1;1023,1;1023,767;256,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);




        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.LAYOUT_SET,"1");
        contentValues.put(DataBaseQuery.LAYOUT_POINT_SNO,"121");
        DataBaseManager.getInstance(context).insert_layout_sets(contentValues);

        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.LAYOUT_SET,"5");
        contentValues.put(DataBaseQuery.LAYOUT_POINT_SNO,"22,23,24");
        DataBaseManager.getInstance(context).insert_layout_sets(contentValues);


    }
}
