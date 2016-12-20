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
    private float num1 = 0;
    private float num2 = 0;
    private float result = 0;
    private Activity parent;
    private String currentOrder;

    OperationAdapter(DictionaryDBHelper db, Activity parent) {

        this.db = db;
        this.parent = parent;
        this.currentOrder = "date";

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
        ExchangeOperation el = db.getDataByPosition(position, this.currentOrder);

        num1 = Float.parseFloat(Float.toString(el.gettoUAH() / 100));
        num2 = Float.parseFloat(Float.toString(el.getFromValue() / 100));

        result = num1 / num2;






        final TextView fromValue = holder.fromValue;
        final TextView fromCurrency = holder.fromCurrency;
        final TextView toUAH = holder.toUAH;
     //   final TextView toCurrency = holder.toCurrency;
        final TextView datecurency = holder.created;
        final TextView curse = holder.curse;

        final Integer selected = el.getId();
        holder.itemcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSubCategory(selected);
            }
        });
/*
        final Integer selected = el.getToCurrency();
        holder.itemcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSubCategory(selected);
            }
        });
  */

        DateFormat formatFrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = null;
        try {
            date = formatFrom.parse(el.getCreated());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat formatTo = new SimpleDateFormat("dd MMM yyyy HH:mm");

        holder.fromValue.setText(Float.toString(el.getFromValue() / 100));
        holder.fromCurrency.setText(el.getFromCurrency());
        holder.toUAH.setText(Float.toString(el.gettoUAH() / 100));
      //  holder.toCurrency.setText(el.getToCurrency());
        holder.created.setText(formatTo.format(date));
        holder.curse.setText(Float.toString(result));
    }

    @Override
    public int getItemCount() {
        return db.numberOfOperationsRows();
    }

    void setCurrentOrder(String currentOrder) {
        this.currentOrder = currentOrder;
    }

    // inner class to hold a reference to each item of RecyclerView
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView fromValue;
        TextView fromCurrency;
        TextView toUAH;
     //   public TextView toCurrency;
     TextView created;
        RelativeLayout itemcontainer;
        TextView curse;


        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            fromValue = (TextView) itemLayoutView.findViewById(R.id.layout_item_from);
            fromCurrency = (TextView) itemLayoutView.findViewById(R.id.layout_item_fromcurrency);
            toUAH = (TextView) itemLayoutView.findViewById(R.id.layout_item_toUAH);
        //    toCurrency = (TextView) itemLayoutView.findViewById(R.id.layout_item_tocurency);
            created = (TextView) itemLayoutView.findViewById(R.id.layout_item_datecurency);
            itemcontainer = (RelativeLayout) itemLayoutView.findViewById(R.id.layout_item_container);
            curse = (TextView) itemLayoutView.findViewById(R.id.layout_item_tocurency);

        }


    }


}
