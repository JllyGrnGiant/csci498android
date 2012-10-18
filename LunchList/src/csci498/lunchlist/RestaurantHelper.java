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
	private static final int    SCHEMA_VERSION = 3;
	
	private static final String CREATE_TABLE = "CREATE TABLE restaurants " +
		"(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, " +
		"type TEXT, notes TEXT, feed TEXT, lat REAL, lon REAL);";
	
	// Incomplete SQL - Must append string for ORDER BY argument in getAll()
	private static final String SELECT_ALL = "SELECT _id, name, address, type, notes, feed, lat, lon " +
		"FROM restaurants ORDER BY ";
	
	private static final String SELECT_BY_ID = "SELECT _id, name, address, type, notes, feed, lat, lon " +
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
		if (oldVersion < 2) {
			db.execSQL("ALTER TABLE restaurants ADD COLUMN feed TEXT");
		}
		
		if (oldVersion < 3) {
			db.execSQL("ALTER TABLE restaurants ADD COLUMN lat REAL");
			db.execSQL("ALTER TABLE restaurants ADD COLUMN lon REAL");
		}
	}
	
	public void insert(String name, String address, RestaurantType type, String notes, String feed) {
		ContentValues cv = new ContentValues();
		
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type.name());
		cv.put("notes", notes);
		cv.put("feed", feed);
		
		getWritableDatabase().insert("restaurants", "name", cv);
	}
	
	public void update(String id, String name, String address, RestaurantType type, String notes, String feed) {
		ContentValues cv = new ContentValues();
		String[] args = { id };
		
		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type.name());
		cv.put("notes", notes);
		cv.put("feed", feed);
		
		getWritableDatabase().update("restaurants", cv, "_ID = ?", args);
	}
	
	public void updateLocation(String id, double lat, double lon) {
		ContentValues cv = new ContentValues();
		String[] args = { id };
		
		cv.put("lat", lat);
		cv.put("lon", lon);
		
		getWritableDatabase().update("restaurants", cv, "_ID=?", args);
	}
	
	public Cursor getById(String id) {
    	String[] args = { id };
    	return getReadableDatabase().rawQuery(SELECT_BY_ID, args);
    }
	
	public Cursor getAll(String orderBy) {
		// Append orderBy to SELECT_ALL to complete the query
		return getReadableDatabase().rawQuery(SELECT_ALL + orderBy, null);
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
	
	public String getFeed(Cursor c) {
		return c.getString(5);
	}
	
	public double getLatitude(Cursor c) {
		return c.getDouble(6);
	}
	
	public double getLongitude(Cursor c) {
		return c.getDouble(7);
	}
}