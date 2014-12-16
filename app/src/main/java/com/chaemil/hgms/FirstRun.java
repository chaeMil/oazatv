package com.chaemil.hgms;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaemil.hgms.utils.Basic;
import com.chaemil.hgms.utils.Utils;

import at.markushi.ui.CircleButton;

/**
 * Created by chaemil on 16.12.14.
 */
public class FirstRun extends Activity {

    Animation slideInRight;
    Animation slideOutLeft;
    Animation slideInLeft;
    Animation slideOutRight;
    RelativeLayout slide1;
    RelativeLayout slide2;
    RelativeLayout slide3;
    int currentSlidePos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_run);

        slideInRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        slideOutLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left);
        slideOutRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        slideInLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);

        slide1 = (RelativeLayout) findViewById(R.id.slide1);
        slide2 = (RelativeLayout) findViewById(R.id.slide2);
        slide3 = (RelativeLayout) findViewById(R.id.slide3);

        TextView logoText = (TextView) findViewById(R.id.logoText);
        logoText.setText(Utils.getStringWithRegularCustomFont(getApplicationContext(),
                getResources().getString(R.string.app_name),
                Basic.FONT_BOLD_UPRIGHT));

        TextView titleText1 = (TextView) findViewById(R.id.slideText1);
        titleText1.setText(Utils.getStringWithRegularCustomFont(getApplicationContext(),
                getResources().getString(R.string.title_slide_1),
                Basic.FONT_REGULAR_UPRIGHT));

        CircleButton slide1ButtonNext = (CircleButton) findViewById(R.id.slide1ButtonNext);
        slide1ButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSlide(slide1,slide2);
            }
        });

        TextView titleBigText2 = (TextView) findViewById(R.id.slideMainText2);
        titleBigText2.setText(Utils.getStringWithRegularCustomFont(getApplicationContext(),
                getResources().getString(R.string.title_slide_2_navigation),
                Basic.FONT_BOLD_UPRIGHT));

        TextView titleText2 = (TextView) findViewById(R.id.slideText2);
        titleText2.setText(Utils.getStringWithRegularCustomFont(getApplicationContext(),
                getResources().getString(R.string.title_slide_2),
                Basic.FONT_REGULAR_UPRIGHT));

        CircleButton slide2ButtonPrev = (CircleButton) findViewById(R.id.slide2ButtonPrev);
        slide2ButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevSlide(slide2,slide1);
            }
        });

        CircleButton slide2ButtonNext = (CircleButton) findViewById(R.id.slide2ButtonNext);
        slide2ButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSlide(slide2,slide3);
            }
        });

        TextView titleBigText3 = (TextView) findViewById(R.id.slideMainText3);
        titleBigText3.setText(Utils.getStringWithRegularCustomFont(getApplicationContext(),
                getResources().getString(R.string.title_slide_3_onthego),
                Basic.FONT_BOLD_UPRIGHT));

        TextView titleText3 = (TextView) findViewById(R.id.slideText3);
        titleText3.setText(Utils.getStringWithRegularCustomFont(getApplicationContext(),
                getResources().getString(R.string.title_slide_3),
                Basic.FONT_REGULAR_UPRIGHT));

        CircleButton slide3ButtonPrev = (CircleButton) findViewById(R.id.slide3ButtonPrev);
        slide3ButtonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevSlide(slide3,slide2);
            }
        });

        CircleButton slide3ButtonNext = (CircleButton) findViewById(R.id.slide3ButtonNext);
        slide3ButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                SharedPreferences settings = getSharedPreferences(Basic.MAIN_PREFS, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(Basic.FIRST_RUN, false);
                editor.apply();
                i.putExtra(Basic.FIRST_RUN,true);
                startActivity(i);
                finish();
            }
        });


    }

    public void nextSlide(final View currentSlide, final View nextSlide) {
        currentSlide.startAnimation(slideOutLeft);
        currentSlide.setVisibility(View.GONE);
        nextSlide.setVisibility(View.VISIBLE);
        nextSlide.startAnimation(slideInRight);
        currentSlidePos += 1;
    }

    public void prevSlide(final View currentSlide, final View nextSlide) {
        currentSlide.startAnimation(slideOutRight);
        currentSlide.setVisibility(View.GONE);
        nextSlide.setVisibility(View.VISIBLE);
        nextSlide.startAnimation(slideInLeft);
        currentSlidePos = currentSlidePos - 1;
    }

    public View getCurrentSlideView(int currentSlidePos) {
        switch (currentSlidePos) {
            case 1:
                return slide1;
            case 2:
                return slide2;
            default:
                return slide1;
        }
    }
}
