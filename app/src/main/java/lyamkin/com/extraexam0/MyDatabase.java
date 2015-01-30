package lyamkin.com.extraexam0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Random;

public class MyDatabase {
    public static final String DB_NAME = "databasess";
    public static final Integer VERSION = 2;

    public static final String CREATE_TABLE_CHANNELS = "CREATE TABLE channel (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, status TEXT, count INTEGER NOT NULL, time INTEGER NOT NULL)";
    public static final String CREATE_TABLE_NEWS = "CREATE TABLE news (_id INTEGER PRIMARY KEY AUTOINCREMENT, channel_id INTEGER NOT NULL, title TEXT, description TEXT, " +
            "url TEXT, time INTEGER NOT NULL, FOREIGN KEY (channel_id ) REFERENCES channel (_id) ON DELETE CASCADE, UNIQUE (url) ON CONFLICT IGNORE)";

    private static MyDatabase mInstance = null;
    private Context context;
    private SQLiteDatabase db;

    private MyDatabase(Context context){
        this.context = context;
    }

    public static MyDatabase getOpenedInstance(Context context){
        if (mInstance==null)
            mInstance = new MyDatabase(context.getApplicationContext()).open();
        return mInstance;
    }

    private MyDatabase open() {
        DBHelper mDbHelper = new DBHelper(context);
        db = mDbHelper.getWritableDatabase();
        return this;
    }


    private static class DBHelper extends SQLiteOpenHelper {
        Context context;

        public DBHelper(Context context) {
            super(context, DB_NAME, null, VERSION);
            this.context = context;
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON");
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE_CHANNELS);
            sqLiteDatabase.execSQL(CREATE_TABLE_NEWS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            sqLiteDatabase.delete("channel", null, null);
            sqLiteDatabase.execSQL(CREATE_TABLE_CHANNELS);
        }
    }

    public String getUrlByChannelId(long channelId){
        Cursor c = db.query("channel", new String[] {"url"},"_id" + "=" + channelId,
                null,null,null,null);
        if (c.getCount()==0) {
            c.close();
            return null;
        }
        c.moveToFirst();
        String result = c.getString(c.getColumnIndex("url"));
        c.close();
        return result;
    }

    public long randomFunction() {
        Cursor c = db.query("channel",new String [] {"_id", "name", "status", "count", "time"},null,null,null,null,null);
        if (c.getCount()==0) {
            c.close();
            return -1;
        }
        Random random = new Random();
        int r = random.nextInt(c.getCount());
        c.moveToFirst();
        for (int i = 0; i < r; i++)
            c.moveToNext();
        long result = c.getLong(c.getColumnIndex("_id"));
        ContentValues cv = new ContentValues();
        cv.put("status", "Running");
        cv.put("count", c.getLong(c.getColumnIndex("count")) + 1);
        changeChannel(cv, result);
        c.close();
        return r;
    }

    public boolean randomFunction2(long id, int time) {
        Cursor c = db.query("channel",new String [] {"_id", "name", "status", "count", "time"},null,null,null,null,null);
        if (c.getCount()==0) {
            c.close();
            return false;
        }
        c.moveToFirst();
        for (int i = 0; i < id; i++)
            c.moveToNext();
        Random rand = new Random();
        boolean success = rand.nextBoolean();
        ContentValues cv = new ContentValues();
        long result = c.getLong(c.getColumnIndex("_id"));
        if (success) {
            cv.put("status", "Success");
        } else {
            cv.put("status", "Failed");
        }
        cv.put("time", time);
        changeChannel(cv, result);
        c.close();
        return true;
    }


    public long createChannel(ContentValues channel) {
        return db.insert("channel",null,channel);
    }


    public boolean changeChannel(ContentValues channel, long channelId) {
        return db.update("channel", channel, "_id" + "=" + channelId,null)==1;
    }

    public Cursor getNewsByChannelId(long channelId){
        return db.query("news", new String[] {"_id", "url", "description", "title"},
                "channel_id=" +channelId,null,null,null, "time" + " DESC");
    }

    public Cursor getAllChannels(){
        return db.query("channel",new String [] {"_id", "name", "status", "time", "count"},null,null,null,null,null);
    }

    public long createNews(ContentValues news) {
        return db.insert("news",null,news);
    }

    public boolean deleteChannel(long channelId){
        return db.delete("channel","_id=" + channelId, null)==1;
    }
}