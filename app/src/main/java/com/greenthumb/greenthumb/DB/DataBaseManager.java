/* $Id$ */
package com.greenthumb.greenthumb.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.greenthumb.greenthumb.DB.Model.TableLayoutPointsPojo;
import com.greenthumb.greenthumb.DB.Model.TableLayoutSetsPojo;

import java.util.ArrayList;

/**
 * Created by raakeshpremkumar on 4/22/17.
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private String createTables[]={DataBaseQuery.CREATE_TABLE_LAYOUT_POINTS,DataBaseQuery.CREATE_TABLE_LAYOUT_SETS};
    private static DataBaseManager databaseManagerInstance=null;
    private static String dataBaseName="GREENTHUMB"; //NO I18N
    private static int dataBaseVersion=1;
    private static Context context=null;
    private static SQLiteDatabase sqliteDatabase=null;

    public DataBaseManager(Context context){
        super(context,DataBaseManager.dataBaseName,null,DataBaseManager.dataBaseVersion);
    }

    public DataBaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(String sqlStatement:createTables){
            db.execSQL(sqlStatement);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public synchronized static SQLiteDatabase getDatabase(){
        if(sqliteDatabase==null){
            sqliteDatabase=DataBaseManager.getInstance(DataBaseManager.context).getWritableDatabase();
        }
        return DataBaseManager.sqliteDatabase;
    }

    public synchronized static void setContext(Context context){
        DataBaseManager.context=context;
    }

    public static DataBaseManager getInstance(Context context){
        if(databaseManagerInstance==null && DataBaseManager.context!=null){
            databaseManagerInstance=new DataBaseManager(DataBaseManager.context);
        }
        return databaseManagerInstance;
    }

    public synchronized void insert_layout_points(ContentValues contentvalues){
        Cursor cursor=getDatabase().rawQuery("SELECT * FROM " + DataBaseQuery.TABLE_LAYOUT_POINTS + " WHERE " + DataBaseQuery.S_NO + " =? ", new String[]{String.valueOf(contentvalues.get(DataBaseQuery.S_NO))}); //NO I18N
        if(cursor.getCount()>0){
            long update_result=getDatabase().update(DataBaseQuery.TABLE_LAYOUT_POINTS,contentvalues,DataBaseQuery.S_NO+"=?",new String[]{String.valueOf(contentvalues.get(DataBaseQuery.S_NO))});
        }
        else{
            long insert_result=getDatabase().insert(DataBaseQuery.TABLE_LAYOUT_POINTS,null,contentvalues);
        }
    }

    public synchronized void insert_layout_sets(ContentValues contentvalues){

        Cursor cursor=getDatabase().rawQuery("SELECT * FROM " + DataBaseQuery.TABLE_LAYOUT_SETS + " WHERE " + DataBaseQuery.LAYOUT_SET+ " =? ", new String[]{String.valueOf(contentvalues.get(DataBaseQuery.LAYOUT_SET))}); //NO I18N
        if(cursor.getCount()>0){
            long update_result=getDatabase().update(DataBaseQuery.TABLE_LAYOUT_SETS,contentvalues,DataBaseQuery.LAYOUT_SET+"=?",new String[]{String.valueOf(contentvalues.get(DataBaseQuery.LAYOUT_SET))});
        }
        else{
            long insert_result=getDatabase().insert(DataBaseQuery.TABLE_LAYOUT_SETS,null,contentvalues);
        }
    }

    public synchronized <T> ArrayList<T> retrieveTablerows(String tableName, String where, String[] args){
        ArrayList<T> table_rows=new ArrayList<T>();
        Cursor cursor=null;

        try{
            if(where==null){
                cursor=getDatabase().rawQuery("SELECT * FROM " + tableName, null); //NO I18N
            }
            else{
                cursor=getDatabase().rawQuery("SELECT * FROM "+tableName+" WHERE "+where+"=?",args); //NO I18N
            }
            table_rows=retrieve_rows(tableName,cursor);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return table_rows;
    }

    private synchronized <T> ArrayList<T> retrieve_rows(String tableName, Cursor cursor){
        ArrayList<T> rows=new ArrayList<T>();
        if(tableName.equals(DataBaseQuery.TABLE_LAYOUT_SETS)){
            if(cursor.moveToFirst()){
                do{
                    TableLayoutSetsPojo pojo_object=new TableLayoutSetsPojo();
                    pojo_object.setLayoutset(cursor.getInt(0));
                    pojo_object.setLayoutpointssno(cursor.getString(1));
                    rows.add((T)pojo_object);
                }while(cursor.moveToNext());
            }
        }
        else if(tableName.equals(DataBaseQuery.TABLE_LAYOUT_POINTS)){
            if(cursor.moveToFirst()){
                do{
                    TableLayoutPointsPojo pojo_object=new TableLayoutPointsPojo();
                    pojo_object.setS_no(cursor.getInt(0));
                    pojo_object.setLayout_points(cursor.getString(1));
                    rows.add((T)pojo_object);
                }while(cursor.moveToNext());
            }
        }
        return rows;
    }
}
