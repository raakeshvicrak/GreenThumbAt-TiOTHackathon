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
        contentValues.put(DataBaseQuery.S_NO,"3");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;768,1;768,767;1,767#768,1;1023,1;1023,767;768,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"4");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;1023,1;1023,192;1,192#1,192;1023,192;1023,767;1,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"5");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;1023,1;1023,576;1,576#1,576;1023,576;1023,767;1,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"6");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;341,1;341,767;1,767#341,1;683,1;683,767;341,767#683,1;1023,1;1023,767;683,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"7");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;1023,1;1023,256;1,256#1,256;1023,256;1023,512;1,512#1,512;1023,512;1023,767;1,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"8");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;624,1;1,767#624,1;1023,1;400,767;1,767#1023,1;1023,767;400,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"9");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;624,767;1,767#1,1;400,1;1023,767;624,767#400,1;1023,1;1023,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"10");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;1023,1;1,468#1,468;1023,1;1023,300;1,767#1,767;1023,300;1023,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"11");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;1023,1;1023,468#1,1;1023,468;1023,767;1,300#1,300;1023,767;1,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"12");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;424,1;256,767;1,767#424,1;1023,1;1023,767;256,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"13");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;600,1;768,767;1,767#600,1;1023,1;1023,767;768,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"14");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;256,1;424,767;1,767#256,1;1023,1;1023,767;424,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"15");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;768,1;600,767;1,767#768,1;1023,1;1023,767;600,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"16");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;1023,1;1023,192;1,462#1,462;500,330;600,767;1,767#500,330;1023,192;1023,767;600,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"17");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;1023,1;1023,462;1,192#1,192;524,330;424,767;1,767#524,330;1023,462;1023,767;424,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);

        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"19");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;424,1;520,438;1,576#424,1;1023,1;1023,306;520,438#1,576;1023,306;1023,767;1,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"20");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;1023,1;1023,192;1,462#1,462;312,380;408,767;1,767#312,380;612,300;740,767;408,767#612,300;1023,192;1023,767;740,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"21");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;1023,1;1023,462;1,192#1,192;408,300;286,767;1,767#408,300;712,380;616,767;286,767#712,380;1023,462;1023,767;616,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"22");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;400,1;304,386;1,306#400,1;732,1;608,466;304,386#732,1;1023,1;1023,576;608,466#1,306;1023,576;1023,767;1,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);
        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.S_NO,"23");
        contentValues.put(DataBaseQuery.LAYOUT_POINTS, "1,1;1023,1;1023,767;1,767#1,1;292,1;416,466;1,576#292,1;624,1;720,386;416,466#624,1;1023,1;1023,306;720,386#1,576;1023,306;1023,767;1,767"); //NO I18N
        DataBaseManager.getInstance(context).insert_layout_points(contentValues);


        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.LAYOUT_SET,"1");
        contentValues.put(DataBaseQuery.LAYOUT_POINT_SNO,"121");
        DataBaseManager.getInstance(context).insert_layout_sets(contentValues);

        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.LAYOUT_SET,"2");
        contentValues.put(DataBaseQuery.LAYOUT_POINT_SNO,"22,23");
        DataBaseManager.getInstance(context).insert_layout_sets(contentValues);

        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.LAYOUT_SET,"3");
        contentValues.put(DataBaseQuery.LAYOUT_POINT_SNO,"2,3");
        DataBaseManager.getInstance(context).insert_layout_sets(contentValues);

        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.LAYOUT_SET,"4");
        contentValues.put(DataBaseQuery.LAYOUT_POINT_SNO,"4,5");
        DataBaseManager.getInstance(context).insert_layout_sets(contentValues);

        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.LAYOUT_SET,"5");
        contentValues.put(DataBaseQuery.LAYOUT_POINT_SNO,"6,7");
        DataBaseManager.getInstance(context).insert_layout_sets(contentValues);

        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.LAYOUT_SET,"6");
        contentValues.put(DataBaseQuery.LAYOUT_POINT_SNO,"8,9");
        DataBaseManager.getInstance(context).insert_layout_sets(contentValues);

        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.LAYOUT_SET,"7");
        contentValues.put(DataBaseQuery.LAYOUT_POINT_SNO,"10,11");
        DataBaseManager.getInstance(context).insert_layout_sets(contentValues);

        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.LAYOUT_SET,"8");
        contentValues.put(DataBaseQuery.LAYOUT_POINT_SNO,"12,13");
        DataBaseManager.getInstance(context).insert_layout_sets(contentValues);

        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.LAYOUT_SET,"5");
        contentValues.put(DataBaseQuery.LAYOUT_POINT_SNO,"14,15");
        DataBaseManager.getInstance(context).insert_layout_sets(contentValues);

        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.LAYOUT_SET,"6");
        contentValues.put(DataBaseQuery.LAYOUT_POINT_SNO,"16,17");
        DataBaseManager.getInstance(context).insert_layout_sets(contentValues);



        contentValues=new ContentValues();
        contentValues.put(DataBaseQuery.LAYOUT_SET,"8");
        contentValues.put(DataBaseQuery.LAYOUT_POINT_SNO,"19,20,21");
        DataBaseManager.getInstance(context).insert_layout_sets(contentValues);

    }
}

