package cn.soft_x.supplies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017-01-11.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "citymine.db";

    public static final String USER_ID = "user_id";

    public static final String MSG_DETAIL_TABLE_NAME = "msg_detail";

    public static final String MSG_DETAIL_TABLE_ID = "_id";// 自增id
    public static final String MSG_DETAIL_TABLE_MSGDL = "msgDL";// 消息大类
    public static final String MSG_DETAIL_TABLE_MSGLX = "msgLX";// 消息小类
    public static final String MSG_DETAIL_TABLE_TIME = "time";// 时间
    public static final String MSG_DETAIL_TABLE_READ = "read";// 已读 1，未读 0
    public static final String MSG_DETAIL_TABLE_CONTENT = "content";// 内容
    public static final String MSG_DETAIL_TABLE_GLID = "glid";// 关联id 点击用的
    public static final String MSG_DETAIL_TABLE_COMPANYID = "companyid";//公司id

    private static final String CREATE_MSG_DETAIL_TABLE = "create table if not exists "
            + MSG_DETAIL_TABLE_NAME + "("
            + MSG_DETAIL_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_ID + " TEXT,"
            + MSG_DETAIL_TABLE_MSGDL + " INTEGER,"
            + MSG_DETAIL_TABLE_MSGLX + " INTEGER,"
            + MSG_DETAIL_TABLE_TIME + " INTEGER,"
            + MSG_DETAIL_TABLE_READ + " INTEGER,"
            + MSG_DETAIL_TABLE_CONTENT + " TEXT,"
            + MSG_DETAIL_TABLE_GLID + " TEXT,"
            + MSG_DETAIL_TABLE_COMPANYID + " TEXT)";

    public static final String MSG_TABLE_NAME = "msg";
    public static final String MSG_TABLE_ID = "_id";
    public static final String MSG_TABLE_MSGDL = "msgDL";
    public static final String MSG_TABLE_MSGLX = "msgLX";
    public static final String MSG_TABLE_MSG_TIME = "time";
    public static final String MSG_TABLE_MSG_CONTENT = "content";
    public static final String MSG_TABLE_READ = "read";
    private static String CREATE_MSG_TABLE = "create table if not exists "
            + MSG_TABLE_NAME + "("
            + MSG_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_ID + " TEXT,"
            + MSG_TABLE_MSGDL + " INTEGER,"
            + MSG_TABLE_MSGLX + " INTEGER,"
            + MSG_TABLE_MSG_TIME + " INTEGER,"
            + MSG_TABLE_MSG_CONTENT + " TEXT,"
            + MSG_TABLE_READ + " INTEGER)";

    public static final String MSG_GHS_TABLE_NAME = "msgghs";
    public static final String MSG_GHS_TABLE_ID = "_id";
    public static final String MSG_GHS_TABLE_MSGDL = "msgDL";
    public static final String MSG_GHS_TABLE_MSGLX = "msgLX";
    public static final String MSG_GHS_TABLE_MSG_TIME = "time";
    public static final String MSG_GHS_TABLE_MSG_CONTENT = "content";
    public static final String MSG_GHS_TABLE_MSG_YWTAGS = "ywtags";
    public static final String MSG_GHS_TABLE_MSG_GLID = "glid";
    public static final String MSG_GHS_TABLE_MSG_GLCOMPANYID = "glcompanyid";
    public static final String MSG_GHS_TABLE_READ = "read";
    private static String CREATE_MSG_GHS_TABLE = "create table if not exists "
            + MSG_GHS_TABLE_NAME + "("
            + MSG_GHS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_ID + " TEXT,"
            + MSG_GHS_TABLE_MSGDL + " INTEGER,"
            + MSG_GHS_TABLE_MSGLX + " INTEGER,"
            + MSG_GHS_TABLE_MSG_TIME + " INTEGER,"
            + MSG_GHS_TABLE_MSG_CONTENT + " TEXT,"
            + MSG_GHS_TABLE_MSG_YWTAGS + " TEXT,"
            + MSG_GHS_TABLE_MSG_GLID + " TEXT,"
            + MSG_GHS_TABLE_MSG_GLCOMPANYID + " TEXT,"
            + MSG_GHS_TABLE_READ + " INTEGER)";


    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MSG_DETAIL_TABLE);
        db.execSQL(CREATE_MSG_TABLE);
        db.execSQL(CREATE_MSG_GHS_TABLE);
//        ContentValues contentValues1 = new ContentValues();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion ==1 && newVersion ==2){
            db.execSQL(CREATE_MSG_GHS_TABLE);
        }
    }
}
