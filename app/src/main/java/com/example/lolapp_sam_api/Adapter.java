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

import java.util.HashMap;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private HashMap<String,Champion> championsList = new HashMap<>();
    private List<Champion> champions;
    private Context context;
    private RecyclerViewOnClickListener listener;
    public Adapter(List<Champion> champions, Context context, RecyclerViewOnClickListener listener) {
        this.champions = champions;
        this.context = context;
        this.listener = listener;
    }


    public HashMap<String, Champion> getChampionsList() {
        return championsList;
    }


    public Champion getChampion(String id){
        return championsList.get(id);
    }
    public List<Champion> getChampions() {

        return champions;
    }

    public void setChampions(List<Champion> champ) {
        champions.forEach(champion -> championsList.put(champion.getName(), champion));
        this.champions = champ;
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
        Glide.with(context).load(champions.get(position).getImage()).placeholder(R.drawable.ic_launcher_background).into(holder.imageView).waitForLayout();
    }

    @Override
    public int getItemCount() {
        return champions.size();
    }

    public void saveChampions() {
        champions.forEach(champion -> championsList.put(champion.getName(), champion));
        champions.clear();
        championsList.forEach((s, champion) -> champions.add(champion));
        this.notifyDataSetChanged();
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
