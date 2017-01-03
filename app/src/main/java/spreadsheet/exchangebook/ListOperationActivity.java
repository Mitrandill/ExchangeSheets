package spreadsheet.exchangebook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class ListOperationActivity extends Activity {

    DictionaryDBHelper db;
    EditText fromDate;



    public void selectCategory(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_operations);
        db = new DictionaryDBHelper(this);

        fromDate = (EditText) findViewById(R.id.item_layout_date3);
        fromDate.setText("");

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ListOperationActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fromDate.setText(Integer.toString(year) + "/" + Integer.toString(monthOfYear + 1) + "/" + Integer.toString(dayOfMonth));
                    }
                }, 2017, 1, 1).show();
            }
        });
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.OperationsList);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final OperationAdapter mAdapter = new OperationAdapter(db, this);

        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        final String[] CurrencyNames = {"EUR", "USD", "RUB", "GBP", "PLN", " "};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CurrencyNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinnerFrom = (Spinner) findViewById(R.id.spinner);
        spinnerFrom.setAdapter(adapter);

        spinnerFrom.setPrompt("From Currency");

        spinnerFrom.setSelection(2);

        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        mAdapter.setCurrentFilter("EUR");
                        break;
                    case 1:
                        mAdapter.setCurrentFilter("USD");
                        break;
                    case 2:
                        mAdapter.setCurrentFilter("RUB");
                        break;
                    case 3:
                        mAdapter.setCurrentFilter("GBP");
                        break;
                    case 4:
                        mAdapter.setCurrentFilter("PLN");
                        break;
                    default:
                        mAdapter.setCurrentFilter("");
                        break;

                }
                mAdapter.notifyDataSetChanged();
                recyclerView.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        final String[] CurrencyOperation = {"Покупка", "Продажа"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CurrencyOperation);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinnerTo = (Spinner) findViewById(R.id.spinner2);
        spinnerTo.setAdapter(adapter2);

        spinnerTo.setPrompt("To Currency");

        spinnerTo.setSelection(1);

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        mAdapter.setCurrentOrder("from");
                        break;

                    default: // case 1:
                        mAdapter.setCurrentOrder("to");
                        break;
                 /*   default:
                        mAdapter.setCurrentOrder("date");
                        break;  */
                }
                mAdapter.notifyDataSetChanged();
                recyclerView.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory(view);
            }
        });


        this.setTitle(getString(R.string.view_operation));

    }


}


