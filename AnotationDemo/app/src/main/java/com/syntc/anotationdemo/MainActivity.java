package com.syntc.anotationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.SelfAnotation;

//该注解 在 build的时候会在 build/generated/source/apt/目录下生成一些代码
@SelfAnotation
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);








    }
}
