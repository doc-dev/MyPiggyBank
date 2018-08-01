package it.docdev.mypiggybank.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import it.docdev.mypiggybank.DataModel.Transazione;
import it.docdev.mypiggybank.R;

public class RVWalletAdapterTransaction extends RecyclerView.Adapter<RVWalletAdapterTransaction.ViewHolder> {
    private List<Transazione> transazioni;

    public RVWalletAdapterTransaction(List<Transazione> transazioni) {
        this.transazioni = transazioni;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_item, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder bvh, int i) {
        bvh.transaction_amount.setText(String.format("%.2f", transazioni.get(i).getImporto()).replace('.', ','));
        bvh.transaction_causale.setText(transazioni.get(i).getCausale());
        bvh.transaction_data.setText(transazioni.get(i).getData().toString());
        int type = transazioni.get(i).getTipo();
        if (type == 0) bvh.transaction_type.setImageResource(R.drawable.entrata);
        else if (type == 1) bvh.transaction_type.setImageResource(R.drawable.uscita);
        else bvh.transaction_type.setImageResource(R.drawable.finish);
    }

    @Override
    public int getItemCount() {
        return transazioni.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView transaction_amount;
        TextView transaction_causale;
        TextView transaction_data;
        ImageView transaction_type;
        LinearLayout ll;

        ViewHolder(View itemView) {
            super(itemView);
            transaction_amount = itemView.findViewById(R.id.timporto);
            ll = itemView.findViewById(R.id.ll2);
            transaction_causale = itemView.findViewById(R.id.tcausa);
            transaction_data = itemView.findViewById(R.id.tdata);
            transaction_type = itemView.findViewById(R.id.tavatar);

        }
    }

}

