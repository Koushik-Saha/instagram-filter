package com.example.varianttecnology.androidinstagramfilter.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.varianttecnology.androidinstagramfilter.R;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewholder> {

    Context context;
    List<Integer> colorList;
    ColorAdapterListener listener;

    public ColorAdapter(Context context, ColorAdapterListener listener) {
        this.context = context;
        this.colorList = genColorList();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.color_item,parent,false);
        return new ColorViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewholder holder, int position) {
        holder.color_section.setCardBackgroundColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewholder extends RecyclerView.ViewHolder{

        public CardView color_section;

        public ColorViewholder(@NonNull View itemView) {
            super(itemView);
            color_section = (CardView)itemView.findViewById(R.id.color_section);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onColorSelected(colorList.get(getAdapterPosition()));
                }
            });
        }
    }

    private List<Integer> genColorList() {
        List<Integer> colorList = new ArrayList<>();

        colorList.add(Color.parseColor("#131722"));
        colorList.add(Color.parseColor("#ff69b4"));
        colorList.add(Color.parseColor("#db7093"));
        colorList.add(Color.parseColor("#9400d3"));
        colorList.add(Color.parseColor("#d8bfd8"));
        colorList.add(Color.parseColor("#ff0000"));
        colorList.add(Color.parseColor("#ff6347"));
        colorList.add(Color.parseColor("#ff8c00"));
        colorList.add(Color.parseColor("#fa8072"));
        colorList.add(Color.parseColor("#e9967a"));
        colorList.add(Color.parseColor("#a52a2a"));
        colorList.add(Color.parseColor("#d2691e"));
        colorList.add(Color.parseColor("#8b4513"));


        colorList.add(Color.parseColor("#bc8f8f"));
        colorList.add(Color.parseColor("#ffff00"));
        colorList.add(Color.parseColor("#b8860b"));
        colorList.add(Color.parseColor("#eee8aa"));
        colorList.add(Color.parseColor("#f0e68c"));
        colorList.add(Color.parseColor("#6b8e23"));
        colorList.add(Color.parseColor("#228b22"));
        colorList.add(Color.parseColor("#00fa9a"));
        colorList.add(Color.parseColor("#006400"));


        colorList.add(Color.parseColor("#2e8b57"));
        colorList.add(Color.parseColor("#4682b4"));
        colorList.add(Color.parseColor("#0000cd"));
        colorList.add(Color.parseColor("#00bfff"));
        colorList.add(Color.parseColor("#000080"));
        colorList.add(Color.parseColor("#483d8b"));
        colorList.add(Color.parseColor("#000000"));
        colorList.add(Color.parseColor("#778899"));
        colorList.add(Color.parseColor("#ffdab9"));
        colorList.add(Color.parseColor("#fffaf0"));
        colorList.add(Color.parseColor("#fffafa"));
        colorList.add(Color.parseColor("#556b2f"));


        return colorList;
    }

    public interface ColorAdapterListener{
        void onColorSelected(int color);
    }


}
