package com.moxiu.keyboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * ferris.xu
 * 对软键盘输入框进行封装 2016-10-26 14:11:15
 */
public class KeyBoardActivity extends AppCompatActivity {
    EmoticonsKeyBoard ek_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board);
        ek_bar= (EmoticonsKeyBoard) findViewById(R.id.ek_bar);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ek_bar.onResume();
    }
}
