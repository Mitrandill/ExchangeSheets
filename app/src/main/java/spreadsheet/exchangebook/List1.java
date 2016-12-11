package spreadsheet.exchangebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class List1 extends Activity {

    DictionaryDBHelper db;

    public void selectCategory(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list1);
        db = new DictionaryDBHelper(this);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.OperationsList);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        OperationAdapter mAdapter = new OperationAdapter(db, this);

        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        final String[] CurrencyNames = {"UAH", "EUR", "USD", "RUB", "GBP", "PLN"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CurrencyNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinnerFrom = (Spinner) findViewById(R.id.spinner);
        spinnerFrom.setAdapter(adapter);

        spinnerFrom.setPrompt("From Currency");

        spinnerFrom.setSelection(2);

        final String[] CurrencyOperation = {"Покупка", "Продажа"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CurrencyOperation);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinnerTo = (Spinner) findViewById(R.id.spinner2);
        spinnerTo.setAdapter(adapter2);

        spinnerTo.setPrompt("To Currency");

        spinnerTo.setSelection(1);





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


