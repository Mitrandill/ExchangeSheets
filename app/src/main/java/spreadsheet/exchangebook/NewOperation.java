package spreadsheet.exchangebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NewOperation extends Activity {

    private static final String DEBUG_TAG = "HttpExample";
    final String[] CurrencyOperation = {"Покупка", "Продажа"};
    //адаптер
    final String[] CurrencyNames = {"EUR", "USD", "RUB", "GBP", "PLN"};
    private final String LAST_SYNC_VALUE = "LastSync";
    private final String PREFERENCES_NAME = "SyncSettings";
    EditText fromValue;
    EditText toUAH;
    Button saveButton;
    DictionaryDBHelper db;
    private TextWatcher mTextWacher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkFieldsForEmpyValues();
        }
    };
    private Spinner spinnerToOperation;
    private Spinner spinnerFrom;
    private EditText comment;
    private TextView textsync;
    private boolean alreadySyncGoing = false;

    void checkFieldsForEmpyValues() {

        String fromText = fromValue.getText().toString();
        String toText = toUAH.getText().toString();

        if (fromText.equals("") || toText.equals("")) {
            saveButton.setEnabled(false);
        } else {
            saveButton.setEnabled(true);
        }

    }

    private void goBack() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    private void saveValues() {
        Float Value = Float.parseFloat(fromValue.getText().toString()) * 100;
        int intFromValue = Math.round(Value);

        String strFromValueCurrency = CurrencyNames[spinnerFrom.getSelectedItemPosition()];

        Float ToUAH = Float.parseFloat(toUAH.getText().toString()) * 100;
        int intToUAH = Math.round(ToUAH);

        String strTooperation = CurrencyOperation[spinnerToOperation.getSelectedItemPosition()];
        String strComment = comment.getText().toString();
        db.insertExchangeRecordWithHash(intFromValue,
                strFromValueCurrency,
                intToUAH,
                strTooperation,
                strComment);

        Toast toast = Toast.makeText(getApplicationContext(), "ДОБАВЛЕННО", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        fromValue.setText("");
        toUAH.setText("");
        comment.setText("");
        doSync();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_operation);

        db = new DictionaryDBHelper(this);//создать экземпляр класса дб
/*
        textsync = (TextView) findViewById(R.id.new_operation_sync_text);
        Button buttonsync = (Button) findViewById(R.id.new_operation_sync_button);

        buttonsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSync();
            }
        });*/


        comment = (EditText) findViewById(R.id.amountValue3);
        saveButton = (Button) findViewById(R.id.Save);
        spinnerToOperation = (Spinner) findViewById(R.id.spinner2);
        Button button = (Button) findViewById(R.id.button);
        spinnerFrom = (Spinner) findViewById(R.id.spinner);
        fromValue = (EditText) findViewById(R.id.amountValue);
        toUAH = (EditText) findViewById(R.id.amountUAH);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();

            }
        });
        this.setTitle(R.string.new_exchange);


        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CurrencyNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(adapter);
        // заголовок
        spinnerFrom.setPrompt("From Currency");
        // выделяем элемент
        spinnerFrom.setSelection(2);
        // устанавливаем обработчик нажатия
        spinnerFrom.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Toast.makeText(getBaseContext(), "From Currency = " + CurrencyNames[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CurrencyOperation);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerToOperation.setAdapter(adapter2);

        spinnerToOperation.setPrompt("To Operation");
        spinnerToOperation.setSelection(1);


        fromValue.setText("");
        toUAH.setText("");
        comment.setText("");

        fromValue.addTextChangedListener(mTextWacher);
        toUAH.addTextChangedListener(mTextWacher);

        checkFieldsForEmpyValues();

        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveValues();
                    }
                }
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_main, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, FirstStepActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
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

    protected void doSync() {
        if (alreadySyncGoing)
            return;

        //final String domain = "192.168.1.3:8080";
        //final String domain = "api.dev.exchange.dmitriy.in.ua";
        final String domain = "api.exchange.dmitriy.in.ua";
        SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        int lastSyncValue = prefs.getInt(LAST_SYNC_VALUE, 1);
        String json = "";

        int count = db.numberOfOperationsRows("", "");

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
            alreadySyncGoing = true;
            new RequestTask().execute("http://" + domain + "/", "deviceHash=abcdef&action=saveOperations&data=" + data);
        }
/* else {
            textsync.setText("нет соединения ");
 }*/

    }

    class RequestTask extends AsyncTask<String, String, String> {

        private String readIt(InputStream stream) throws IOException {
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
            alreadySyncGoing = false;
            //         textsync.setText(result);
            if (result.contains("\"status\":true")) {
                int count = db.numberOfOperationsRows("", "");
                SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt(LAST_SYNC_VALUE, count + 1);
                edit.apply();
            }
        }
    }

}
