package com.nevermore.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoScrollTextView scrollTextView = findViewById(R.id.tv_scroll);

        scrollTextView.setSequenceList(Arrays.asList(
                "Hello world!",
                "XuNeverMore",
                "Android Developer"));

    }
}
