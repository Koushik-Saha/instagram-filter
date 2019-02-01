package com.example.varianttecnology.androidinstagramfilter.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.varianttecnology.androidinstagramfilter.R;

import java.util.ArrayList;
import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {

    Context context;
    FontAdapterClickListener listener;
    List<String> fontList;

    int row_selected = -1;

    public FontAdapter(Context context, FontAdapterClickListener listener) {
        this.context = context;
        this.listener = listener;
        fontList = loadFontList();
    }

    private List<String> loadFontList() {
        List<String> result = new ArrayList<>();
        result.add("Cheque-Black.otf");
        result.add("Cheque-Regular.otf");
        result.add("Beautiful People Personal Use.ttf");
        result.add("GreatVibes-Regular.otf");
        result.add("Montserrat-Black.otf");
        result.add("Montserrat-Bold.otf");
        result.add("Montserrat-ExtraBoldItalic.otf");
        result.add("Montserrat-ExtraLightItalic.otf");
        result.add("Montserrat-Italic.otf");
        result.add("Montserrat-LightItalic.otf");
        result.add("Montserrat-SemiBold.otf");
        result.add("OpenSans-Bold.ttf");
        result.add("OpenSans-ExtraBoldItalic.ttf");
        result.add("OpenSans-LightItalic.ttf");
        result.add("Raleway-Black.ttf");
        result.add("Raleway-BoldItalic.ttf");
        result.add("Raleway-SemiBoldItalic.ttf");
        result.add("Roboto-Bold.ttf");
        result.add("RobotoCondensed-Regular.ttf");
        result.add("Roboto-ThinItalic.ttf");
        result.add("Roboto-Thin.ttf");
        result.add("Raleway-BoldItalic.ttf");
        result.add("OpenSans-Light.ttf");
        result.add("Roboto-MediumItalic.ttf");
        result.add("Raleway-SemiBoldItalic.ttf");
        result.add("MontserratAlternates-Regular.otf");
        result.add("OpenSans-LightItalic.ttf");
        
        return result;

    }

    @NonNull
    @Override
    public FontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.font_item,parent,false);
        return new FontViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FontViewHolder holder, int position) {
        if (row_selected == position)
            holder.img_check.setVisibility(View.VISIBLE);
        else
            holder.img_check.setVisibility(View.INVISIBLE);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),new StringBuilder("fonts/")
        .append(fontList.get(position)).toString());

        holder.txt_font_name.setText(fontList.get(position));
        holder.txt_font_demo.setTypeface(typeface);
    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }

    public class FontViewHolder extends RecyclerView.ViewHolder{
        TextView txt_font_name,txt_font_demo;
        ImageView img_check;
        public FontViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_font_name = (TextView)itemView.findViewById(R.id.txt_font_name);
            txt_font_demo = (TextView)itemView.findViewById(R.id.txt_font_demo);

            img_check = (ImageView) itemView.findViewById(R.id.img_check);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onFontSelected(fontList.get(getAdapterPosition()));
                    row_selected = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

    public interface FontAdapterClickListener{
        public void onFontSelected(String fontName);
    }
}
