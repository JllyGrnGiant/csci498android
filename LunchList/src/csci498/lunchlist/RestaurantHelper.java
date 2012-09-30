package csci498.lunchlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * RestaurantHelper provides an interface for LunchList to use a
 * persistable database for restaurant information storage
 */
public class RestaurantHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME  = "lunchlist.db";
	private static final int    SCHEMA_VERSION = 1;
	
	private static final String CREATE_TABLE = "CREATE TABLE restaurants " +
		"(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, type TEXT, notes TEXT);";
	private static final String SELECT_ALL   = "SELECT _id, name, address, type, notes " +
		"FROM restaurants ORDER BY name";
	private static final String SELECT_BY_ID = "SELECT _id, name, address, type, notes " +
			"FROM restaurants WHERE _ID = ?";
	
	public RestaurantHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// no-op since this function will not be called until another db schema exists
	}
	
	public void insert(String name, String address, RestaurantType type, String notes) {
		ContentValues cv = new ContentValues();
		
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type.name());
		cv.put("notes", notes);
		
		getWritableDatabase().insert("restaurants", "name", cv);
	}
	
	public void update(String id, String name, String address, RestaurantType type, String notes) {
		ContentValues cv = new ContentValues();
		String[] args = { id };
		
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type.name());
		cv.put("notes", notes);
		
		getWritableDatabase().update("restaurants", cv, "_ID = ?", args);
	}
	
	public Cursor getById(String id) {
    	String[] args = { id };
    	
    	return getReadableDatabase().rawQuery(SELECT_BY_ID, args);
    }
	
	public Cursor getAll() {
		return getReadableDatabase().rawQuery(SELECT_ALL, null);
	}
	
	public String getName(Cursor c) {
		return c.getString(1);
	}
	
	public String getAddress(Cursor c) {
		return c.getString(2);
	}
	
	public RestaurantType getType(Cursor c) {
		return RestaurantType.valueOf(c.getString(3));
	}
	
	public String getNotes(Cursor c) {
		return c.getString(4);
	}
}