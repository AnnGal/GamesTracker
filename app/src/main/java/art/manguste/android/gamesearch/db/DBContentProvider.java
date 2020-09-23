package art.manguste.android.gamesearch.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import art.manguste.android.gamesearch.core.Game;

public class DBContentProvider extends ContentProvider {

    public static final String AUTHORITY = "art.manguste.android.gamesearch.provider";
    public static final Uri URI_GAME = Uri.parse(
            "content://" + AUTHORITY + "/" + FavoriteGame.TABLE_NAME);
    private static final int CODE_GAME_DIR = 1;
    private static final int CODE_GAME_ITEM = 2;

    /** The URI matcher. */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, FavoriteGame.TABLE_NAME, CODE_GAME_DIR);
        MATCHER.addURI(AUTHORITY, FavoriteGame.TABLE_NAME + "/*", CODE_GAME_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_GAME_DIR || code == CODE_GAME_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }

            FavoriteGameDao gameDao = GameDatabase.getInstance(context).favoriteGameDao();
            final Cursor cursor;
            if (code == CODE_GAME_DIR) {
                cursor = gameDao.selectAll();
            } else {
                cursor = gameDao.selectById(ContentUris.parseId(uri));
            }

            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_GAME_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }

                final long id = GameDatabase.getInstance(context).favoriteGameDao()
                        .insert(FavoriteGame.formFavoriteGame(values));

                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);

            case CODE_GAME_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }



}
