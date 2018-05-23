package com.codez.collar.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteFullException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.base.BaseApp;

/**
 * Created by codez on 2018/4/19.
 * Description:
 */

public class DataBaseHelper extends SQLiteOpenHelper{
    private static final String TAG = "DataBaseHelper";
    private static String mDBName = "";
    private static int mDBVersion = -1;
    private static DataBaseHelper instance = null;

    public synchronized static DataBaseHelper getInstance(String name, int ver) {
        if (name == null || TextUtils.isEmpty(name) || ver <= 0) {
            Log.w(TAG, "db name " + name + ", ver " + ver + ", invalid!");
            return null;
        }
        Log.i(TAG, "database name is : " + name);
        if (instance == null) {
            mDBName = name;
            mDBVersion = ver;
            instance = new DataBaseHelper();
            return instance;
        } else {
            if (name.equals(mDBName)) {
                //没有切换用户，返回原有的实例
                Log.i(TAG, "DataBaseHelper mDBName is not change:" + mDBName);
                mDBName = name;
                mDBVersion = ver;
                return instance;
            } else {
                //用户切换，返回新的实例
                Log.i(TAG, "DataBaseHelper mDBName is changed, oldDBName:" + mDBName + ",new newDBName:" + name);
                mDBName = name;
                mDBVersion = ver;
                instance = new DataBaseHelper();
                return instance;
            }
        }
    }

    public static DataBaseHelper getDataBaseHelper() {
        Log.w(TAG, "DataBaseHelper is null!!!");
        if (AccessTokenKeeper.getInstance() == null) {
            //未初始化完成
            new Exception("AccessTokenKeeper.getInstance() 未初始化完成").printStackTrace();
            return null;
        }
        String uid = AccessTokenKeeper.getInstance().getUid();
        DataBaseHelper helper = DataBaseHelper.getInstance(uid, DBConstants.DB_VERSION);
        if (helper == null) {
            new Exception("we get database hepler is null").printStackTrace();
        }
        return helper;
    }
    public static void destroy(){
        if (instance != null){
            instance.close();
        }
        instance = null;
    }
    /**
     * 创建一个保存简单信息的表，该表只有两个列，1：key;2:value
     */
    private static final String CREATE_CONFIG_TABLE = "CREATE TABLE IF NOT EXISTS "
            + DBConstants.TABLE_CONFIG
            + " ("
            + DBConstants.CONFIG_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DBConstants.CONFIG_COLUMN_KEY + " TEXT UNIQUE DEFAULT '' ,"
            + DBConstants.CONFIG_COLUMN_VALUE + " TEXT DEFAULT '' "
            + ");";
    /**
     * 创建一个存储点赞状态的表，该表只有两个列，1：status_id;2:remark
     */
    private static final String CREATE_LIKE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + DBConstants.TABLE_LIKE
            + " ("
            + DBConstants.LIKE_COLUMN_STATUS_ID + " TEXT PRIMARY KEY,"
            + DBConstants.LIKE_COLUMN_REMARK + " TEXT DEFAULT '' "
            + ");";
    /**
     * 创建一个存储微博动态的表
     */
    private static final String CREATE_STATUS_TABLE = "CREATE TABLE IF NOT EXISTS "
            + DBConstants.TABLE_STATUS
            + " ("
            + DBConstants.STATUS_COLUMN_ID + " TEXT PRIMARY KEY,"
            + DBConstants.STATUS_COLUMN_CONTENT + " TEXT DEFAULT '' ,"
            + DBConstants.STATUS_COLUMN_TYPE + " TEXT DEFAULT '' ,"
            + DBConstants.STATUS_COLUMN_REMARK + " TEXT DEFAULT '' "
            + ");";
    /**
     * 创建一个存储用户信息的表
     */
    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "
            + DBConstants.TABLE_USER
            + " ("
            + DBConstants.USER_COLUMN_ID + " TEXT PRIMARY KEY,"
            + DBConstants.USER_COLUMN_CONTENT + " TEXT DEFAULT '' ,"
            + DBConstants.USER_COLUMN_TYPE + " TEXT DEFAULT '' ,"
            + DBConstants.USER_COLUMN_REMARK + " TEXT DEFAULT '' "
            + ");";
    /**
     * 创建一个存储评论信息的表
     */
    private static final String CREATE_COMMENT_TABLE = "CREATE TABLE IF NOT EXISTS "
            + DBConstants.TABLE_COMMENT
            + " ("
            + DBConstants.COMMENT_COLUMN_ID + " TEXT PRIMARY KEY,"
            + DBConstants.COMMENT_COLUMN_CONTENT + " TEXT DEFAULT '' ,"
            + DBConstants.COMMENT_COLUMN_TYPE + " TEXT DEFAULT '' ,"
            + DBConstants.COMMENT_COLUMN_REMARK + " TEXT DEFAULT '' "
            + ");";
    /**
     * 创建一个存储私信信息的表
     */
    private static final String CREATE_MESSAGE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + DBConstants.TABLE_MESSAGE
            + " ("
            + DBConstants.MESSAGE_COLUMN_ID + " TEXT PRIMARY KEY,"
            + DBConstants.MESSAGE_COLUMN_CONTENT + " TEXT DEFAULT '' ,"
            + DBConstants.MESSAGE_COLUMN_TYPE + " TEXT DEFAULT '' ,"
            + DBConstants.MESSAGE_COLUMN_REMARK + " TEXT DEFAULT '' "
            + ");";

    public DataBaseHelper() {
        super(BaseApp.sContext, mDBName, null, mDBVersion);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONFIG_TABLE);
        db.execSQL(CREATE_LIKE_TABLE);
        db.execSQL(CREATE_STATUS_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_COMMENT_TABLE);
        db.execSQL(CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion > newVersion) {
            Log.w(TAG, "error database upgrade, new ver:" + newVersion + ", old ver:" + oldVersion);
            return;
        }
        onCreate(db);
    }
    public boolean isExistsBySQL(String sql, String[] selectionArgs) {
        SQLiteDatabase dataBase = null;
        Cursor cursor = null;
        try {
            dataBase = getReadableDatabase();
            cursor = dataBase.rawQuery(sql, selectionArgs);
            if (cursor.moveToFirst()) {
                boolean isExist = cursor.getInt(0) > 0;
                cursor.close();
                return isExist;
            } else {
                cursor.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return false;
    }
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase dataBase;
        try {
            dataBase = getWritableDatabase();
            return dataBase.update(table, values, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public long insert(String table, ContentValues content){
        SQLiteDatabase dataBase = null;
        try {
            dataBase = getWritableDatabase();
            return dataBase.insertWithOnConflict(table, null, content,SQLiteDatabase.CONFLICT_IGNORE);
        }catch(SQLiteFullException e)
        {
            Log.w(TAG, "insert message encounter exception,in SQLiteFullException branch:"+e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e) {
            Log.w(TAG, "insert message encounter exception,in Exception branch:"+e.getMessage());

            e.printStackTrace();
        }
        return -1;
    }
    public  boolean isExistsByField(String table, String field, String value) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ").append(table).append(" WHERE ")
                .append(field).append(" =?");
        try {
            return isExistsBySQL(sql.toString(), new String[] { value });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public <T> T queryForObject(RowMapper<T> rowMapper, String sql, String[] args) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        T object = null;
        try {
            database = getReadableDatabase();
            cursor = database.rawQuery(sql, args);
            if (cursor.moveToFirst()) {
                object = rowMapper.mapRow(cursor, cursor.getCount());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return object;
    }
    public interface RowMapper<T> {
        T mapRow(Cursor cursor, int index);
    }
}
