package scu.dynamiclayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;

/**
 * Created by Anthony Harrell on 4/7/2016.
 */
public class FLDatabaseHelper{

    // If schema is changed need to increment the data base version

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DynamicLayoutDB.db";

    // Table one name and columns
    public static final String TABLE_NAME_1 = "AppsTable";
    public static final String COLUMN_APP_ID = "_Id";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ICON = "icon";

    // Table two name and columns
    private SQLiteOpenHelper _openHelper;
    public static final String TABLE_NAME_2 = "LayoutTable";
    public static final String COLUMN_PAGE_ID = "_Id";
    public static final String COLUMN_LAYOUT_NAME = "layoutName";
    public static final String COLUMN_UP_LINK = "upLink";
    public static final String COLUMN_RIGHT_LINK = "rightLink";
    public static final String COLUMN_DOWN_LINK = "downLink";
    public static final String COLUMN_LEFT_LINK = "leftLink";
    public static final String COLUMN_INNER_LINK_1 = "innerLink1";
    public static final String COLUMN_INNER_LINK_2 = "innerLink2";

    public FLDatabaseHelper(Context context){
        _openHelper = new FLDatabaseOpenHelper(context);
    }

    class FLDatabaseOpenHelper extends SQLiteOpenHelper {
        public FLDatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table if not exists AppsTable"
                    + "(_Id integer primary key autoincrement, label text not null unique"
                    + ", name text, icon blob)");
            db.execSQL("create table if not exists LayoutTable"
                    + "(_Id integer primary key autoincrement"
                    + ", layoutName text, upLink integer, rightLink integer, downLink integer"
                    + ", leftLink integer, innerLink1 integer, innerLink2 integer)");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS AppsTable");
            db.execSQL("DROP TABLE IF EXISTS LayoutTable");
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public boolean insertAppsTable(String table, String label, String name, byte[] iconBytes){
        //if(getLabelData(table, label).compareTo(label)==0){
        //    return false;
        //}
        //else {
            SQLiteDatabase db = _openHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("label", label);
            values.put("name", name);
            values.put("icon", iconBytes);
            db.insert(table, null, values);
            return true;
        //}
    }

    public boolean insertLayoutTable(String table, String layoutName, int lu, int lr,
                                     int ld, int ll, int in1, int in2){
        SQLiteDatabase db = _openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("layoutName", layoutName);
        values.put("upLink", lu);
        values.put("rightLink", lr);
        values.put("downLink", ld);
        values.put("leftLink", ll);
        values.put("innerLink1", in1);
        values.put("innerLink2", in2);
        db.insert(table, null, values);
        return true;
    }

    // Get table names in DB
    public String getTableName1(){return TABLE_NAME_1;}

    public String getTableName2(){return TABLE_NAME_2;}

    // Get Column names.... THINK ON HOW YOU WANT TO DO THIS

    public Cursor getTableData(String table) {
        SQLiteDatabase db = _openHelper.getReadableDatabase();
        //Cursor res = db.rawQuery("SELECT * FROM " + table + " WHERE _Id ="+ id,null);
        Cursor cur = db.rawQuery("SELECT * FROM " + table, null);
        return cur;
    }

    public String getLabelData(String table, String label){
        String curLabel = "";
        SQLiteDatabase db = _openHelper.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + table + " WHERE "+ COLUMN_LABEL +" = '" + label + "'", null);
        if(cur.moveToFirst()){
            curLabel = cur.getString(cur.getColumnIndex(COLUMN_LABEL));
            return curLabel;
        }
        return curLabel;
    }

    public int getCustomRowNumber(String table, String column, String search) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + table, null);
        int numRows = cur.getCount();
        cur.moveToFirst();
        int pos = cur.getPosition();
        while (pos < numRows){
            String s = cur.getString(cur.getColumnIndex(column));
            if(s.compareTo(search) == 0){
                return cur.getPosition();
            }
            else {
                cur.moveToNext();
                pos = cur.getPosition();
            }
        }
        return pos;
    }

    public int getRows(String table){
        SQLiteDatabase db = _openHelper.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table);
        return numRows;
    }

    // Think about arrays or hash to pass in values...
    public boolean updateAppsTable(Integer id, String table, String label,
                                   String name, byte[] iconBytes){
        SQLiteDatabase db = _openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("label", label);
        values.put("name", name);
        values.put("icon", iconBytes);
        db.update(table, values, COLUMN_PAGE_ID + " = ?", new String[]{Integer.toString(id)});
        return true;
    }
    // Think about arrays or hash to pass in values...
    public boolean updateLayoutTable(Integer id, String table, String layoutName, int lu, int lr,
                                     int ld, int ll, int in1, int in2){
        SQLiteDatabase db = _openHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("layoutName", layoutName);
        values.put("upLink", lu);
        values.put("rightLink", lr);
        values.put("downLink", ld);
        values.put("leftLink", ll);
        values.put("innerLink1", in1);
        values.put("innerLink2", in2);
        db.update(table, values,COLUMN_PAGE_ID + " = ?", new String[] {Integer.toString(id)});
        return true;
    }

    public Integer deleteAppsRow (Integer id) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();
        return db.delete("AppsTable", COLUMN_APP_ID +
                " = ?", new String[] {Integer.toString(id)});
    }

    public Integer deleteLayoutRow (Integer id) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();
        return db.delete("LayoutTable", COLUMN_PAGE_ID
                + " = ?", new String[] {Integer.toString(id)});
    }

    public Integer deleteAllRows(String table){
        SQLiteDatabase db = _openHelper.getWritableDatabase();
        return db.delete(table, null, null);
    }

    //Helper functions to save Bitmaps in DB
    @Nullable
    public static Bitmap drawableToBitmap(Drawable d){
        BitmapDrawable bd = (BitmapDrawable)d;
        if(bd.getBitmap() != null){
            return bd.getBitmap();
        }
        else
            return null;
    }

    @Nullable
    public static Drawable bitmapToDrawable(Bitmap b){
        Drawable d = new BitmapDrawable(Resources.getSystem(),b);
        if(d != null){
            return d;
        }
        else
            return null;
    }

    @Nullable
    public static byte[] bitmapToBytesArray(Bitmap b) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        b.compress(Bitmap.CompressFormat.PNG, 100, bos);

        // Get the bytes of the serialized object
        if(bos.toByteArray() != null){
            return bos.toByteArray();
        }
        else
            return null;
    }

    public static Bitmap byteArrayToBitmap(byte[] bArray){
        return BitmapFactory.decodeByteArray(bArray, 0, bArray.length);
    }
}
