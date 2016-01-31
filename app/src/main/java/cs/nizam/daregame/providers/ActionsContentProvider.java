package cs.nizam.daregame.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;

import cs.nizam.daregame.DatabaseHandler;

/**
 * Created by nizamcs on 31/1/16.
 */
public class ActionsContentProvider extends ContentProvider {

    public static final String AUTHORITY = "cs.nizam.daregame.providers.ActionsContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/"+ DatabaseHandler.TABLE_ACTION);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.jwei512."+DatabaseHandler.TABLE_ACTION;

    private static final int NOTES = 1;

    private static final int NOTES_ID = 2;

    private DatabaseHandler dbHelper;
    private static final UriMatcher sUriMatcher;

    private static final HashMap<String, String> notesProjectionMap;

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHandler(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DatabaseHandler.TABLE_ACTION);
        qb.setProjectionMap(notesProjectionMap);

        switch (sUriMatcher.match(uri)) {
            case NOTES:
                break;
            case NOTES_ID:
                selection = selection + "_id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case NOTES:
                return CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (sUriMatcher.match(uri) != NOTES) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(DatabaseHandler.TABLE_ACTION, DatabaseHandler.KEY_ACTION, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case NOTES:
                break;
            case NOTES_ID:
                selection = selection + "_id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        int count = db.delete(DatabaseHandler.TABLE_ACTION, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case NOTES:
                count = db.update(DatabaseHandler.TABLE_ACTION, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, DatabaseHandler.TABLE_ACTION, NOTES);
        sUriMatcher.addURI(AUTHORITY, DatabaseHandler.TABLE_ACTION + "/#", NOTES_ID);

        notesProjectionMap = new HashMap<>();
        notesProjectionMap.put(DatabaseHandler.KEY_ID, DatabaseHandler.KEY_ID);
        notesProjectionMap.put(DatabaseHandler.KEY_ACTION, DatabaseHandler.KEY_ACTION);
    }
}
