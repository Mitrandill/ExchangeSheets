package spreadsheet.exchangebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

public class NewOperation extends Activity {

    EditText fromValue;
    EditText toValue;
    DictionaryDBHelper db;
    Button saveButton;


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

    public void selectCategory(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    void checkFieldsForEmpyValues() {

        String fromText = fromValue.getText().toString();
        String toText = toValue.getText().toString();

        if (fromText.equals("") || toText.equals("")) {
            saveButton.setEnabled(false);
        } else {
            saveButton.setEnabled(true);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DictionaryDBHelper(this);
        setContentView(R.layout.activity_new_operation);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory(view);
            }
        });
        this.setTitle(R.string.new_exchange);


        //адаптер
        final String[] CurrencyNames = {"UAH", "EUR", "USD", "RUB", "GBP", "PLN"};

        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CurrencyNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinnerFrom = (Spinner) findViewById(R.id.spinner);
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


        final String[] CurrencyOperation = {"Покупка", "Продажа"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CurrencyOperation);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinnerTo = (Spinner) findViewById(R.id.spinner2);
        spinnerTo.setAdapter(adapter2);

        spinnerTo.setPrompt("To Currency");
        spinnerTo.setSelection(1);


        fromValue = (EditText) findViewById(R.id.amountValue);
        toValue = (EditText) findViewById(R.id.amountValue2);
        final EditText comment = (EditText) findViewById(R.id.amountValue3);
        final Button Save = (Button) findViewById(R.id.Save);

        fromValue.setText("");
        toValue.setText("");
        comment.setText("");

        fromValue.addTextChangedListener(mTextWacher);
        toValue.addTextChangedListener(mTextWacher);

        checkFieldsForEmpyValues();

        saveButton = (Button) findViewById(R.id.Save);
        saveButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Integer intFromValue = Integer.parseInt(fromValue.getText().toString()) * 100;
                                              String strFromValueCurrency = CurrencyNames[spinnerFrom.getSelectedItemPosition()];
                                              Integer intToValue = Integer.parseInt(toValue.getText().toString()) * 100;
                                              String strToValueCurrency = CurrencyOperation[spinnerTo.getSelectedItemPosition()];
                                              String strComment = comment.getText().toString();
                                              db.insertExchangeRecordWithHash(intFromValue,
                                                      strFromValueCurrency,
                                                      intToValue,
                                                      strToValueCurrency,
                                                      strComment);
                                              Toast toast = Toast.makeText(getApplicationContext(), "ДОБАВЛЕННО", Toast.LENGTH_LONG);
                                              toast.setGravity(Gravity.CENTER, 0, 0);
                                              toast.show();
                                              fromValue.setText("");
                                              toValue.setText("");
                                              comment.setText("");

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
}
