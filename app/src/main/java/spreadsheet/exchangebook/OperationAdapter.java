package spreadsheet.exchangebook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mixail on 12/6/16.
 */

class OperationAdapter extends RecyclerView.Adapter<OperationAdapter.ViewHolder> {

    private DictionaryDBHelper db;
    private Activity parent;
    private String currentOperation;
    private String currentCurrency;


    OperationAdapter(DictionaryDBHelper db, Activity parent) {

        this.db = db;
        this.parent = parent;
        this.currentOperation = "";
        this.currentCurrency = "";


    }

    private void selectSubCategory(Integer id) {
        Intent intent = new Intent(this.parent, Youroperation.class);
        intent.putExtra("id", id);
        this.parent.startActivity(intent);
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, null);

        // create ViewHolder
        return new ViewHolder(itemLayoutView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExchangeOperation el = db.getDataByPosition(position, this.currentOperation, this.currentCurrency);
        double num1;
        double num2;
        double result;
        num1 = (double) el.gettoUAH() / 100.0;
        num2 = (double) el.getFromValue() / 100.0;

        result = num1 / num2;

        String res = String.format("%.2f", result);

        final Integer selected = el.getId();
        holder.itemcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSubCategory(selected);
            }
        });

        DateFormat formatFrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = null;
        try {
            date = formatFrom.parse(el.getCreated());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat formatTo = new SimpleDateFormat("dd MMM yyyy HH:mm");

        holder.fromValue.setText(Double.toString((double) el.getFromValue() / 100.0));
        holder.fromCurrency.setText(el.getFromCurrency());
        holder.toUAH.setText(Double.toString((double) el.gettoUAH() / 100.0));
        holder.created.setText(formatTo.format(date));
        holder.curse.setText(res);
    }

    @Override
    public int getItemCount() {
        return db.numberOfOperationsRows(this.currentOperation, this.currentCurrency);
    }


    void setCurrentOperation(String currentOperation) {
        this.currentOperation = currentOperation;
    }


    void setCurrentCurrency(String currentCurrency) {
        this.currentCurrency = currentCurrency;
    }

    // inner class to hold a reference to each item of RecyclerView
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView fromValue;
        TextView fromCurrency;
        TextView toUAH;
        TextView created;
        RelativeLayout itemcontainer;
        TextView curse;


        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            fromValue = (TextView) itemLayoutView.findViewById(R.id.layout_item_from);
            fromCurrency = (TextView) itemLayoutView.findViewById(R.id.layout_item_fromcurrency);
            toUAH = (TextView) itemLayoutView.findViewById(R.id.layout_item_toUAH);
            created = (TextView) itemLayoutView.findViewById(R.id.layout_item_datecurency);
            itemcontainer = (RelativeLayout) itemLayoutView.findViewById(R.id.layout_item_container);
            curse = (TextView) itemLayoutView.findViewById(R.id.layout_item_tocurency);

        }


    }


}
