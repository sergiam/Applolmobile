package com.example.lolapp_sam_api;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final List<Champion> champions = new ArrayList<>();
    private final RecyclerViewOnClickListener listener;

    public Adapter(List<Champion> champions, RecyclerViewOnClickListener listener) {
        this.listener = listener;
        setChampions(champions);
    }

        public Champion getChampion(int position) {
        return position >= 0 && position < champions.size() ? champions.get(position) : null;
    }

    public void setChampions(List<Champion> values) {
        champions.clear();
        if (values != null) champions.addAll(values);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.champ_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Champion champion = champions.get(position);
        holder.nombre.setText(champion.getName());
        holder.faction.setText(champion.getFaction());
        holder.releaseDate.setText(champion.getReleasedate());
        Glide.with(holder.imageView).load(champion.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return champions.size();
    }

    public interface RecyclerViewOnClickListener {
        void onClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nombre;
        final TextView faction;
        final TextView releaseDate;
        final ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.image);
            faction = itemView.findViewById(R.id.faction);
            releaseDate = itemView.findViewById(R.id.name2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) listener.onClick(view, position);
        }
    }
}
