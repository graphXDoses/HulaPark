package com.parkingapp.hulapark.Utilities.DialogBoxes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.parkingapp.hulapark.R;

import org.jetbrains.annotations.NotNull;

public class WarningDialogBox extends Dialog
{
    public static void setBackground(Drawable background)
    {
        WarningDialogBox.background = background;
    }

    public static Drawable background;

    public WarningDialogBox(Context context)
    {
        super(context);
    }

    @SuppressLint("MissingInflatedId")
    public WarningDialogBox builder()
    {
        setCancelable(false);
        setContentView(R.layout.warning_dialog_box);
        getWindow().setBackgroundDrawable(background);
        findViewById(R.id.OKDB_OK).setOnClickListener(view -> {
            dismiss();
        });
        return this;
    }

    public WarningDialogBox setDescription(@NotNull String description)
    {
        ((TextView)findViewById(R.id.ok_db_body)).setText(description);
        return this;
    }
}
