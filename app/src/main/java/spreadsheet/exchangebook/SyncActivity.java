package spreadsheet.exchangebook;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SyncActivity extends Activity {
    private static final String DEBUG_TAG = "HttpExample";
    private final String LAST_SYNC_VALUE = "LastSync";
    private final String PREFERENCES_NAME = "SyncSettings";
    DictionaryDBHelper db;
    private TextView textsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        textsync = (TextView) findViewById(R.id.textsync);
        Button buttonsync = (Button) findViewById(R.id.buttonsync);
        db = new DictionaryDBHelper(this);//создать экземпляр класса дб

        //final String domain = "192.168.1.3:8080";
        //final String domain = "api.dev.exchange.dmitriy.in.ua";
        final String domain = "api.exchange.dmitriy.in.ua";

        buttonsync.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
                int lastSyncValue = prefs.getInt(LAST_SYNC_VALUE, 1);
                String json = "";

                int count = db.numberOfOperationsRows("");

                for (int x = lastSyncValue; x <= count; x = x + 1) {

                    ExchangeOperation exchangeOperation = db.getDataById(x);

                    json += exchangeOperation.toJSON();

                    if (x < count) {
                        json = json + ",";
                    }
                }

                json = "{\"operations\": [" + json + "]}";

                final String data = encodeData(json);
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new RequestTask().execute("http://" + domain + "/", "deviceHash=abcdef&action=saveOperations&data=" + data);
                } else {
                    textsync.setText("нет соединения ");
                }
            }
        });
    }

    protected String encodeData(String data) {

        String encoded;

        try {
            encoded = URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encoded = "";
        }

        return Base64.encodeToString(encoded.getBytes(), Base64.DEFAULT);
    }

    class RequestTask extends AsyncTask<String, String, String> {

        String readIt(InputStream stream) throws IOException {
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(stream, "UTF-8");
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            return out.toString();
        }

        private String downloadUrl(String myurl, String payload) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(myurl);
                Log.v(DEBUG_TAG, "The request is: " + myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

                OutputStream os = conn.getOutputStream();
                os.write(payload.getBytes("UTF-8"));
                os.close();

                // Starts the query
                int response = conn.getResponseCode();
                Log.v(DEBUG_TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                return readIt(is);

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }

        }

        @Override
        protected String doInBackground(String... urls) {

            try {
                return downloadUrl(urls[0], urls[1]);
            } catch (IOException e) {
                Log.v(DEBUG_TAG, e.toString());
                return "Unable to retrieve web page. URL may be invalid.";
            }


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            textsync.setText(result);
            if (result.contains("\"status\":true")) {
                int count = db.numberOfOperationsRows("");
                SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt(LAST_SYNC_VALUE, count + 1);
                edit.apply();
            }
        }
    }


}
