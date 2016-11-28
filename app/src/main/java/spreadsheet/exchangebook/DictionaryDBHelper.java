package spreadsheet.exchangebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mixail on 28.11.16.
 */

class DictionaryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ExchangeBase.db";
    private static final String DATABASE_ID = "id";
    private static final String DATABASE_EXCHANGE = "exchange";
    private static final String DATABASE_FROM_CENTS = "from_cents";
    private static final String DATABASE_FROM_CURRENCY = "from_currency";
    private static final String DATABASE_TO_CENTS = "to_cents";
    private static final String DATABASE_TO_CURRENCY = "to_currency";
    private static final String DATABASE_CREATED = "created";
    private static final String DATABASE_HASH = "hash_signature";
    private Context context;

    public DictionaryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table  " + DATABASE_EXCHANGE + "(" +
                        DATABASE_ID + " integer primary key, " +
                        DATABASE_FROM_CENTS + " integer, " +
                        DATABASE_FROM_CURRENCY + " text, " +
                        DATABASE_TO_CENTS + " integer, " +
                        DATABASE_TO_CURRENCY + " text," +
                        DATABASE_CREATED + " text," +
                        DATABASE_HASH + " text" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_EXCHANGE);
        onCreate(sqLiteDatabase);
    }

    public long insertExcengeRecord(int from_cents, String from_currency, int to_cents, String to_currency, String created, String hash_value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DATABASE_FROM_CENTS, from_cents);
        contentValues.put(DATABASE_FROM_CURRENCY, from_currency);
        contentValues.put(DATABASE_TO_CENTS, to_cents);
        contentValues.put(DATABASE_TO_CURRENCY, to_currency);
        contentValues.put(DATABASE_CREATED, created);
        contentValues.put(DATABASE_HASH, hash_value);
        return db.insert(DATABASE_EXCHANGE, null, contentValues);
    }
}
