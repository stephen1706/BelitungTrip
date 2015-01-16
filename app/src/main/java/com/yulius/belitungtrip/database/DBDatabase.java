//package com.yulius.belitungtrip.database;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DBDatabase extends SQLiteOpenHelper {
//
//    private static final String DATABASE_NAME = "yulius.db";
//    private static final int DB_VER_1 = 1;
//    private static final int DATABASE_VERSION = DB_VER_1;
//
//    public static final String TABLE_TRIP = "trip";
//
//    public static final String TRIP_ID = "trip";
//    public static final String TRIP_NAME = "name";
//    public static final String TRIP_TIME = "time";
//    private final Context mContext;
//
//    public DBDatabase(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//
//        mContext = context;
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        updateDBVer1(db);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//
//    public static void deleteDatabase(Context context) {
//        context.deleteDatabase(DATABASE_NAME);
//    }
//
//    //================================================================================
//    // Life Cycle
//    //================================================================================
//
//    private void updateDBVer1(SQLiteDatabase db){
//        db.execSQL("CREATE TABLE IF NOT EXISTS" + TABLE_TRIP + " ("
//                + TRIP_ID + " INT AUTO INCREMENT PRIMARY KEY,"
//                + TRIP_NAME + " TEXT NOT NULL,"
//                + TRIP_TIME + " INT NOT NULL," //time dalam timestamp int
//                + ")");
//    }
//
//    public Cursor getData(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.query(TABLE_TRIP, null, null, null, null, null, null);
//        return res;
//    }
//
//    public Integer deleteTrip (int id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete(TABLE_TRIP, TRIP_ID + " = ? ", new String[] { Integer.toString(id) });
//    }
//
//    public boolean updateTrip(int id, String name, String timestamp) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(TRIP_NAME, name);
//        contentValues.put(TRIP_TIME, timestamp);
//        db.update(TABLE_TRIP, contentValues, TRIP_ID + " = ? ", new String[] { Integer.toString(id) } );
//        return true;
//    }
//}
