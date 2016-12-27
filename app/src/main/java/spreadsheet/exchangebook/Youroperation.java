package spreadsheet.exchangebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Youroperation extends Activity {

    DictionaryDBHelper db;






    public void selectCategory(View view) {
        Intent intent = new Intent(this, ListOperationActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youroperation);
        Intent intent = getIntent();
        Integer id = intent.getIntExtra("id", 0);
        db = new DictionaryDBHelper(this);
        ExchangeOperation el = db.getDataById(id);

        double num1;
        double num2;
        double result;


        TextView fromValue = (TextView) findViewById(R.id.youroperation_from_value);
        fromValue.setText(Double.toString((double) el.getFromValue() / 100.0));

        DateFormat formatFrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = null;
        try {
            date = formatFrom.parse(el.getCreated());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat formatTo = new SimpleDateFormat("dd MMM yyyy HH:mm");

        TextView created = (TextView) findViewById(R.id.youroperation_date);
        created.setText(formatTo.format(date));

        TextView toUAH = (TextView) findViewById(R.id.youroperation_to_UAH);
        toUAH.setText(Double.toString((double) el.gettoUAH() / 100.0));

        TextView fromCurrency = (TextView) findViewById(R.id.youroperation_from_value_currency);
        fromCurrency.setText(el.getFromCurrency());

        TextView toCurrency = (TextView) findViewById(R.id.youroperation_to_value_currency);
        toCurrency.setText(el.getToCurrency());

        TextView comment = (TextView) findViewById(R.id.youroperation_to_value_comment);
        comment.setText(el.getComment());

        num1 = (double) el.gettoUAH() / 100.0;

        num2 = (double) el.getFromValue() / 100.0;



        result = num1 / num2;

        String res = String.format("%.2f", result);

        TextView curse = (TextView) findViewById(R.id.youroperation_curse);
        curse.setText(toString().format(res));



        Button button = (Button) findViewById(R.id.youroperation_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory(view);
            }
        });


        this.setTitle(getString(R.string.view_operation) + id.toString());
    }


}


