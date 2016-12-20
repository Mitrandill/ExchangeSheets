package spreadsheet.exchangebook;

import android.app.Activity;
import android.content.Context;
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

public class HttpExampleActivity extends Activity {
    private static final String DEBUG_TAG = "HttpExample";
    DictionaryDBHelper db; //обьявить переменную
    private TextView textsync;
    private Button buttonsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_example);
        textsync = (TextView) findViewById(R.id.textsync);
        buttonsync = (Button) findViewById(R.id.buttonsync);
        db = new DictionaryDBHelper(this);//создать экземпляр класса дб
        String json = "";


        int count = db.numberOfOperationsRows();

        for (int x = 1; x <= count; x = x + 1) {

            ExchangeOperation exchangeOperation = db.getDataById(x);

            json += exchangeOperation.toJSON();

            if (x < count) {
                json = json + ",";
            }
        }

        json = "{\"operations\": [" + json + "]}";

        final String data = encodeData(json);

        //final String domain = "192.168.1.3:8080";
        //final String domain = "api.dev.exchange.dmitriy.in.ua";
        final String domain = "api.exchange.dmitriy.in.ua";

        buttonsync.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
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

        String encoded = "";

        try {
            encoded = URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encoded = "";
        }

        return Base64.encodeToString(encoded.getBytes(), Base64.DEFAULT);
    }

    class RequestTask extends AsyncTask<String, String, String> {

        public String readIt(InputStream stream, int len) throws IOException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

        private String downloadUrl(String myurl, String payload) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

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
                os.write(payload.toString().getBytes("UTF-8"));
                os.close();

                //conn.addRequestProperty("Accept-Encoding", "gzip");


                // Starts the query
                //conn.connect();
                int response = conn.getResponseCode();
                Log.v(DEBUG_TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                return contentAsString;

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
        }
    }


}