package com.chaemil.hgms.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.chaemil.hgms.R;
import com.chaemil.hgms.utils.Basic;
import com.squareup.picasso.Picasso;

/**
 * Created by chaemil on 12.12.14.
 */
public class SinglePhoto extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_photo);

        Bundle extras = getIntent().getExtras();
        String photoUrl = extras.getString(Basic.PHOTO_URL);

        ImageView photo = (ImageView) findViewById(R.id.photo);
        Picasso.with(getApplicationContext()).load(photoUrl).into(photo);

    }
}
