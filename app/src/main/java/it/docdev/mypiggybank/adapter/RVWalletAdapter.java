package it.docdev.mypiggybank.adapter;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import it.docdev.mypiggybank.DataModel.Wallet;
import it.docdev.mypiggybank.R;

public class RVWalletAdapter extends RecyclerView.Adapter<RVWalletAdapter.ViewHolder> {
    private List<Wallet> wallets;
    private String[] countryNames = {"SAR", "ARS", "AUD", "BTC", "CAD", "ETH", "EUR", "JPY", "MXN", "XMR", "NOK", "RUB"
            , "CHF", "GBP", "USD"};
    private int flags[] = {R.drawable.arabia, R.drawable.argentina, R.drawable.australia, R.drawable.bitcoin,
            R.drawable.canada, R.drawable.ethereum, R.drawable.europe, R.drawable.japan
            , R.drawable.mexico, R.drawable.monero, R.drawable.norway, R.drawable.russia, R.drawable.switzerland
            , R.drawable.uk, R.drawable.usa};

    public RVWalletAdapter(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wallet_item, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder bvh, int i) {
        bvh.wallet_name.setText(wallets.get(i).getNome());
        bvh.amount.setText(String.format("%.2f", wallets.get(i).getSaldo_corrente()).replace('.', ','));
        bvh.wallet_currency.setText(countryNames[wallets.get(i).getValuta()]);
        bvh.wallet_avatar.setImageResource(wallets.get(i).getDrawable_immagine());
        bvh.wallet_cur_img.setImageResource(flags[wallets.get(i).getValuta()]);
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView wallet_name;
        TextView amount;
        TextView wallet_currency;
        ImageView wallet_avatar;
        ImageView wallet_cur_img;
        ConstraintLayout cl;
        LinearLayout ll;

        ViewHolder(View itemView) {
            super(itemView);

            wallet_name = itemView.findViewById(R.id.walletName);
            wallet_currency = itemView.findViewById(R.id.walletCurrency);
            cl = itemView.findViewById(R.id.cl1);
            ll = itemView.findViewById(R.id.linearLayout);
            amount = itemView.findViewById(R.id.amountWallet);
            wallet_avatar = itemView.findViewById(R.id.walletAvatar);
            wallet_cur_img = itemView.findViewById(R.id.currImg);
        }
    }

}

