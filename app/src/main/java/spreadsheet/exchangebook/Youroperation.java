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
        Intent intent = new Intent(this, List1.class);
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

        TextView fromvalue = (TextView) findViewById(R.id.youroperation_from_value);
        fromvalue.setText(Float.toString(el.getFromValue() / 100));

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

        TextView tovalue = (TextView) findViewById(R.id.youroperation_to_value);
        tovalue.setText(Float.toString(el.getToValue() / 100));

        TextView fromcurrency = (TextView) findViewById(R.id.youroperation_from_value_currency);
        fromcurrency.setText(el.getFromCurrency());

        TextView tocurrency = (TextView) findViewById(R.id.youroperation_to_value_currency);
        tocurrency.setText(el.getToCurrency());

        TextView comment = (TextView) findViewById(R.id.youroperation_to_value_comment);
        comment.setText(el.getComment());

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


