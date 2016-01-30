package cs.nizam.daregame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nizamcs on 30/1/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "actionDB";

    // Table name
    private static final String TABLE_ACTION = "action";

    // Columns
    private static final String KEY_ID = "id";
    private static final String KEY_ACTION = "todo";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACTIONS_TABLE = "CREATE TABLE " + TABLE_ACTION + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ACTION + " TEXT" + ")";
        db.execSQL(CREATE_ACTIONS_TABLE);
        addAction(db,"Song");
        addAction(db,"Duet");
        addAction(db,"Dialogue");
        addAction(db,"Classical song");
        addAction(db,"Ramp walk");
        addAction(db,"Stay 1 min without laughing");
        addAction(db,"kooooi...");
        addAction(db,"Do whatever you like");
        addAction(db,"Love proposal");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTION);

        // Create tables again
        onCreate(db);
    }

    /**
     * For bulk insert.
     * @param db
     * @param action
     */
    private void addAction(SQLiteDatabase db, String action) {
        ContentValues values = new ContentValues();
        values.put(KEY_ACTION, action);

        // Inserting Row
        db.insert(TABLE_ACTION, null, values);
    }

    void addAction(String action) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTION, action);

        // Inserting Row
        db.insert(TABLE_ACTION, null, values);
        db.close(); // Closing database connection
    }

    String getAction(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ACTION, new String[] { KEY_ID,
                        KEY_ACTION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            return cursor.getString(1);
        } else return "";
    }

    public List<String> getAllActions() {
        List<String> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ACTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding action to list
                contactList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }
    // Getting actions Count
    public int getActionsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ACTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
