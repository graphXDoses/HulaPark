package com.parkingapp.hulapark.Views;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.parkingapp.hulapark.R;

public class SpannableTextView extends androidx.appcompat.widget.AppCompatTextView
{
    public SpannableTextView(Context context)
    {
        super(context);
        init();
    }

    public SpannableTextView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public SpannableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        String textAsString = getText().toString();
        final SpannableString spannableString = new SpannableString(textAsString);
        int position = textAsString.indexOf('.');

        if(position == -1) return;
        position -= 1;

        spannableString.setSpan(new RelativeSizeSpan(2.0f), 0, position + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(spannableString, TextView.BufferType.SPANNABLE);
    }
}
