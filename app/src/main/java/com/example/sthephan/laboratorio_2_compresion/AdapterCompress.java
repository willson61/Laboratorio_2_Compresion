package com.example.sthephan.laboratorio_2_compresion;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterCompress extends BaseAdapter{
    protected Activity activity;
    protected ArrayList<Compress> items;

    public AdapterCompress (Activity activity, ArrayList<Compress> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Compress> compresiones) {
        for (int i = 0; i < compresiones.size(); i++) {
            items.add(compresiones.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            try{
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.item_compress, parent, false);
                holder.c = items.get(position);
                holder.nomOriginal = (TextView)v.findViewById(R.id.labelNombreOriginal);
                holder.nomCompress = (TextView)v.findViewById(R.id.labelNombreComprimido);
                holder.rutaCompress = (TextView)v.findViewById(R.id.labelRuta);
                holder.facCompress = (TextView)v.findViewById(R.id.labelFactorCompress);
                holder.razCompress = (TextView)v.findViewById(R.id.labelRazonCompress);
                holder.porcReduccion = (TextView)v.findViewById(R.id.labelPorcentajeReducc);
                holder.tipoCompress = (TextView)v.findViewById(R.id.labelTipoCompress);
                v.setTag(holder);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            holder = (ViewHolder) v.getTag();
        }
        holder.nomOriginal.setText(holder.c.getNombreOriginal() + "  ");
        holder.nomCompress.setText("  " + holder.c.getNombreComprimido());
        holder.rutaCompress.setText(holder.c.getRutaArchivoComprimido());
        holder.facCompress.setText(" " + String.valueOf(holder.c.getFactorCompresion()));
        holder.razCompress.setText(" " + String.valueOf(holder.c.getRazonCompresion()) + "%");
        holder.porcReduccion.setText(" " + String.valueOf(holder.c.getPorcentajeReduccion()) + "%");
        holder.tipoCompress.setText(" " + holder.c.getTipoCompresion());
        return v;
    }

    private class ViewHolder{
        private TextView nomOriginal;
        private TextView nomCompress;
        private TextView rutaCompress;
        private TextView razCompress;
        private TextView facCompress;
        private TextView porcReduccion;
        private TextView tipoCompress;
        private Compress c;
    }
}
