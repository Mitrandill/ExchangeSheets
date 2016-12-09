package spreadsheet.exchangebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private final static String HEX = "0123456789ABCDEF";
    private Context context;

    public DictionaryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    /**
     * Returns the SHA1 hash for the provided String
     * @param text
     * @return the SHA1 hash or null if an error occurs
     */
    public static String SHA1(String text) {

        try {

            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("UTF-8"),
                    0, text.length());
            byte[] sha1hash = md.digest();

            return toHex(sha1hash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String toHex(byte[] buf) {

        if (buf == null) return "";

        int l = buf.length;
        StringBuffer result = new StringBuffer(2 * l);

        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }

        return result.toString();

    }

    private static void appendHex(StringBuffer sb, byte b) {

        sb.append(HEX.charAt((b >> 4) & 0x0f))
                .append(HEX.charAt(b & 0x0f));

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

    public ExchangeOperation getDataByPosition(int position){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(
                "select * from " +
                        DATABASE_EXCHANGE +
                        " order by " +
                        DATABASE_CREATED +
                        " limit 1 offset ?", new String[]{Integer.toString(position)}
        );
        if (res.moveToFirst()) {
            ExchangeOperation result;

            result = new ExchangeOperation(
                    res.getInt(res.getColumnIndex(DATABASE_ID)),
                    res.getInt(res.getColumnIndex(DATABASE_FROM_CENTS)),
                    res.getString(res.getColumnIndex(DATABASE_FROM_CURRENCY)),
                    res.getInt(res.getColumnIndex(DATABASE_FROM_CENTS)),
                    res.getString(res.getColumnIndex(DATABASE_FROM_CURRENCY)),
                    res.getString(res.getColumnIndex(DATABASE_CREATED))
            );

            res.close();
            return result;
        }
        res.close();
        return null;
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

    public long insertExchangeRecordWithHash(int from_cents, String from_currency, int to_cents, String to_currency) {
        String created = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date());


        String hash_value = SHA1(this.lastHash() + created + ":" + Integer.toString(from_cents) + ">" + from_currency + "<" + Integer.toString(to_cents) + ":" + to_currency);
        return this.insertExcengeRecord(from_cents, from_currency, to_cents, to_currency, created, hash_value);
    }

    public String lastHash() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DATABASE_EXCHANGE + " order by " + DATABASE_CREATED + " desc limit 1", new String[]{});
        if (res.moveToFirst()) {
            String result = res.getString(res.getColumnIndex(DATABASE_HASH));
            res.close();
            return result;
        }
        res.close();
        return "";
    }

    public int numberOfOperationsRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, DATABASE_EXCHANGE);
        return numRows;
    }
}
