package com.example.konstantin.qiwi.UI.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;

import com.example.konstantin.qiwi.R;

/**
 *  ViewHolder для любого элемента, содержащего Spinner
 *
 * Created by Konstantin on 15.12.2017.
 */

public class SpinnerViewHolder extends RecyclerView.ViewHolder {
    private Spinner spinner;

    public SpinnerViewHolder(View itemView) {
        super(itemView);
        spinner = itemView.findViewById(R.id.spinner);
    }

    public Spinner getSpinnerView() {
        return spinner;
    }
}
