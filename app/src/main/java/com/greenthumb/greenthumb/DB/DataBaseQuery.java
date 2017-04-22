/* $Id$ */
package com.greenthumb.greenthumb.DB;

/**
 * Created by raakeshpremkumar on 4/22/17.
 */
public class DataBaseQuery {

    public static final String TABLE_LAYOUT_POINTS ="GREENTHUMBLAYOUTS";

    public static final String S_NO="SNO";
    public static final String LAYOUT_POINTS="LAYOUTPOINTS";

    public static final String CREATE_TABLE_LAYOUT_POINTS="CREATE TABLE "+ TABLE_LAYOUT_POINTS +"("+ S_NO +" INTEGER PRIMARY KEY,"+ LAYOUT_POINTS +" TEXT)";

    public static final String TABLE_LAYOUT_SETS="GREENTHUMBLAYOUTSETS";

    public static final String LAYOUT_SET="LAYOUTSET";
    public static final String LAYOUT_POINT_SNO="LAYOUTPOINTSNO";

    public static final String CREATE_TABLE_LAYOUT_SETS="CREATE TABLE "+ TABLE_LAYOUT_SETS +"("+ LAYOUT_SET +" INTEGER PRIMARY KEY,"+ LAYOUT_POINT_SNO +" TEXT)";
}
