package com.arabteam;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private static ArrayList<String> arrayListBtnName = new ArrayList<String>();
    CustomScroller scroller;

    static {
        for (int i = 1; i < 7; i++)
            arrayListBtnName.add("Part" + i);
    }

    private Button b1, b2, b3, b4, b5;
    private ScrollView container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initUI() {
        container = (ScrollView) findViewById(R.id.scrollView);
        //
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        b5 = (Button) findViewById(R.id.button5);
        //
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar, null);

        actionBar.setCustomView(v);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, arrayListBtnName);
        final AutoCompleteTextView textView = (AutoCompleteTextView) v.findViewById(R.id.editText1);
        textView.setAdapter(adapter);
        textView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                String word = textView.getText().toString();
                if (!TextUtils.isEmpty(word) && arrayListBtnName.contains(word))
                    focusOnView(getBtn(word));
            }
        });

        try {
            Field mScroller = ScrollView.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new CustomScroller(container.getContext(), new AccelerateInterpolator());
            mScroller.set(container, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

    }

    private Button getBtn(String name) {
        if (name.equalsIgnoreCase("PART1")) return b1;
        if (name.equalsIgnoreCase("PART2")) return b2;
        if (name.equalsIgnoreCase("PART3")) return b3;
        if (name.equalsIgnoreCase("PART4")) return b4;
        if (name.equalsIgnoreCase("PART5")) return b5;
        else
            return null;
    }

    private final void focusOnView(final Button searchedBtn) {
        if (searchedBtn != null)
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    container.smoothScrollTo(0, searchedBtn.getTop());
                    searchedBtn.requestFocus();
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


}
