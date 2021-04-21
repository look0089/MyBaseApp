package org.jzs.mybaseapp.section.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import org.jzs.mybaseapp.R;
import org.jzs.mybaseapp.common.utils.GlideUtils;

/**
 * Activity that gets transitioned to
 */
public class ActivityTransitionToActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_to);
        ImageView iv_photo = (ImageView) findViewById(R.id.iv_photo);
        GlideUtils.setImage("http://img1.3lian.com/2015/a1/34/d/119.jpg", iv_photo);
    }
}
