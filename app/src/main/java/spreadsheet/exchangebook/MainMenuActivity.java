package spreadsheet.exchangebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends Activity {


    public void selectCategory(View view) {
        Intent intent = new Intent(this, NewOperation.class);
        startActivity(intent);
    }

    public void selectList(View view) {
        Intent intent = new Intent(this, List1.class);
        startActivity(intent);
    }

    public void selectFirst(View view) {
        Intent intent = new Intent(this, FirstStepActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategory(view);
            }
        });


        Button buttonList1 = (Button) findViewById(R.id.button3);
        buttonList1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectList(view);
            }
        });

        Button buttonFirst = (Button) findViewById(R.id.button6);
        buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFirst(view);
            }
        });


        this.setTitle(getString(R.string.selection_menu));
    }


}