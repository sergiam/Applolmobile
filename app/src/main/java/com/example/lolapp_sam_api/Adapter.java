package com.example.lolapp_sam_api;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Champion> champions;
    private Context context;
    private RecyclerViewOnClickListener listener;
    public Adapter(List<Champion> champions, Context context){ //RecyclerViewOnClickListener listener) {
        this.champions = champions;
        this.context = context;
        //this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.champ_info,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombre.setText(champions.get(position).getName());
        holder.faction.setText(champions.get(position).getFaction());
        holder.release_date.setText(champions.get(position).getReleasedate());
        Glide.with(context).load(champions.get(position).getImageUrl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return champions.size();
    }

    public interface RecyclerViewOnClickListener{
        void onClick(View v, int id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nombre;
        private TextView faction;
        private TextView release_date;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.image);
            faction = itemView.findViewById(R.id.faction);
            release_date = itemView.findViewById(R.id.name2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
