package spreadsheet.exchangebook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mixail on 12/6/16.
 */

public class OperationAdapter  extends RecyclerView.Adapter<OperationAdapter.ViewHolder> {

    DictionaryDBHelper db;
    private Activity parent;


    public OperationAdapter(DictionaryDBHelper db, Activity parent) {

        this.db = db;
        this.parent = parent;

    }

    public void selectSubCategory(Integer id) {
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
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);


        return viewHolder;


    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExchangeOperation el = db.getDataByPosition(position);

        final TextView fromValue = holder.fromValue;
        final TextView fromCurrency = holder.fromCurrency;
        final TextView toValue = holder.toValue;
        final TextView toCurrency = holder.toCurrency;
        final TextView datecurency = holder.created;


        final Integer selected = el.getId();
        final TextView forClick = holder.txtViewTitle;
        holder.txtViewTitle.setText(el.getFromCurrency());
        holder.txtViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSubCategory(selected);
            }
        });


        holder.fromValue.setText(el.getFromValue().toString());
        holder.fromCurrency.setText(el.getFromCurrency());
        holder.toValue.setText(el.getToValue().toString());
        holder.toCurrency.setText(el.getToCurrency());
        holder.created.setText(el.getCreated().toString());
    }

    @Override
    public int getItemCount() {
        return db.numberOfOperationsRows();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView fromValue;
        public TextView fromCurrency;
        public TextView toValue;
        public TextView toCurrency;
        public TextView created;
        public TextView txtViewTitle;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            fromValue = (TextView) itemLayoutView.findViewById(R.id.layout_item_from);
            fromCurrency = (TextView) itemLayoutView.findViewById(R.id.layout_item_fromcurrency);
            toValue = (TextView) itemLayoutView.findViewById(R.id.layout_item_tovalue);
            toCurrency = (TextView) itemLayoutView.findViewById(R.id.layout_item_tocurency);
            created = (TextView) itemLayoutView.findViewById(R.id.layout_item_datecurency);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.button2);
        }


    }


}
