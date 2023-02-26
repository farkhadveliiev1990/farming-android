package com.dmy.farming.view.emojicon;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.dmy.farming.R;

import java.util.List;


public class EmojiconGridAdapter extends ArrayAdapter<EaseEmojicon> {

    public EmojiconGridAdapter(Context context, int textViewResourceId, List<EaseEmojicon> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.ease_row_expression, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_expression);
        EaseEmojicon emojicon = getItem(position);

        if (EaseSmileUtils.DELETE_KEY.equals(emojicon.getEmojiText())) {
            imageView.setImageResource(R.drawable.ease_delete_expression);
        } else {
            imageView.setImageResource(emojicon.getIcon());
        }

        return convertView;
    }

}
