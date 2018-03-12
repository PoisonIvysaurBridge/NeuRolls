package com.werelit.neurolls.neurolls.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.werelit.neurolls.neurolls.data.MediaContract.FilmEntry;
import com.werelit.neurolls.neurolls.data.MediaContract.BookEntry;
import com.werelit.neurolls.neurolls.data.MediaContract.GameEntry;

/**
 * {@link ContentProvider} for NeuRolls app.
 */
public class MediaProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = MediaProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the films table */
    private static final int FILMS = 100;

    /** URI matcher code for the content URI for a single film in the films table */
    private static final int FILM_ID = 101;

    /** URI matcher code for the content URI for the books table */
    private static final int BOOKS = 200;

    /** URI matcher code for the content URI for a single book in the books table */
    private static final int BOOK_ID = 201;

    /** URI matcher code for the content URI for the games table */
    private static final int GAMES = 300;

    /** URI matcher code for the content URI for a single game in the games table */
    private static final int GAME_ID = 301;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.werelit.neurolls.neurolls/films" will map to the
        // integer code {@link #FILMS}. This URI is used to provide access to MULTIPLE rows
        // of the films table.
        sUriMatcher.addURI(MediaContract.CONTENT_AUTHORITY, MediaContract.PATH_FILMS, FILMS);

        // The content URI of the form "content://com.werelit.neurolls.neurolls/films/#" will map to the
        // integer code {@link #FILM_ID}. This URI is used to provide access to ONE single row
        // of the films table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.werelit.neurolls.neurolls/films/3" matches, but
        // "content://com.werelit.neurolls.neurolls/films" (without a number at the end) doesn't match.
        sUriMatcher.addURI(MediaContract.CONTENT_AUTHORITY, MediaContract.PATH_FILMS + "/#", FILM_ID);

        sUriMatcher.addURI(MediaContract.CONTENT_AUTHORITY, MediaContract.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(MediaContract.CONTENT_AUTHORITY, MediaContract.PATH_BOOKS + "/#", BOOK_ID);

        sUriMatcher.addURI(MediaContract.CONTENT_AUTHORITY, MediaContract.PATH_GAMES, GAMES);
        sUriMatcher.addURI(MediaContract.CONTENT_AUTHORITY, MediaContract.PATH_GAMES + "/#", GAME_ID);
    }

    /** Database helper object */
    private NeurollsDbHelper mDbHelper;

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a MediaDbHelper object to gain access to the medias database.
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        mDbHelper = new NeurollsDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FILMS:
                // For the FILMS code, query the films table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the films table.
                cursor = database.query(FilmEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FILM_ID:
                // For the FILM_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.werelit.neurolls.neurolls/films/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = FilmEntry.COLUMN_FILM_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the films table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(FilmEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOKS:
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BookEntry.COLUMN_BOOK_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case GAMES:
                cursor = database.query(GameEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case GAME_ID:
                selection = GameEntry.COLUMN_GAME_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(GameEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }
}