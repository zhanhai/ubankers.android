package cn.com.ubankers.www.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String USER_DB = "UserDB";
    public static final String USER_NAME = "user_name";
    public static final String USER_USER_ID = "user_id";
    public static final String USER_ROLE = "user_role";
    public static final String USER_CATALOG = "user_catalog";
    public static final String USER_ID = "_id";
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL("CREATE TABLE IF NOT EXISTS "+ USER_DB+"("
				+USER_ID +" INTEGER PRIMARY KEY,"
				+USER_USER_ID +" VARCHAR,"
				+USER_NAME +" VARCHAR,"
				+USER_ROLE +" VARCHAR,"
				+USER_CATALOG +" VARCHAR)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		if (arg1!= arg2) {
		    arg0.execSQL("CREATE TABLE IF NOT EXISTS "+USER_DB+"("
			    + USER_ID + " INTEGER PRIMARY KEY," 
			    + USER_USER_ID + " VARCHAR,"
		    	+ USER_NAME + " VARCHAR," 
			    + "USER_ROLE" +" VARCHAR,"
			    + USER_CATALOG +" VARCHAR)");
		}
	}
	
 
}
