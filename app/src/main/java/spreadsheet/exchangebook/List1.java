package spreadsheet.exchangebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

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

        // get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.OperationsList);

        // set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // create an adapter
        OperationAdapter mAdapter = new OperationAdapter(db, this);
        // set adapter
        recyclerView.setAdapter(mAdapter);
        // set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
