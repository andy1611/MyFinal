package com.andyg.myfinal;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import modelos.Tienda;

public class MyAdapterStore extends BaseAdapter {

    //transformar la clase en una vista

    protected Activity activity;
    protected ArrayList<Tienda> lst;

    public MyAdapterStore(Activity activity, ArrayList<Tienda> lst) {
        this.activity = activity;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        //tamaño del arreglo
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return lst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View v = convertView;

        if (v==null)
        {
            LayoutInflater layoutInflater= (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = layoutInflater.inflate(R.layout.list_item, null);
        }

        Tienda tienda = lst.get(position);

        TextView lblNombre, lblDescrp;
        lblNombre = v.findViewById(R.id.lblNameTiendaItem);
        lblDescrp = v.findViewById(R.id.lblDescripcionItem);

        lblNombre.setText(tienda.getNombreTienda());
        lblDescrp.setText(tienda.getDescripcion());

        return v;
    }
}
