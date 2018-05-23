package com.sung.uiimageview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int[] ids = {R.id.view1,R.id.view2,R.id.view3,R.id.view4,R.id.view5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UiImageView img = findViewById(R.id.uiv_img);
        List<Integer> pics = new ArrayList();
        pics.add(R.color.colorAccent);
        pics.add(R.color.colorPrimary);
        pics.add(android.R.color.holo_purple);
        pics.add(android.R.color.holo_orange_light);
        pics.add(android.R.color.darker_gray);
        img.addPicData(pics);

        for (int i = 0; i < ids.length; i++) {
            findViewById(ids[i]).setBackgroundResource(pics.get(i));
        }
    }
}
