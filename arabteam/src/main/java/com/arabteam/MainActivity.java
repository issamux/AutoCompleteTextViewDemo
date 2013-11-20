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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private static ArrayList<String> arrayListBtnName = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private LayoutInflater inflator;

    static {
        for (int i = 1; i < 6; i++)
            arrayListBtnName.add("Part" + i);
    }

    private CustomScroller scroller;
    private AutoCompleteTextView textView;
    private Button b1, b2, b3, b4, b5;
    private ScrollView container;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
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

        inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar, null);
        actionBar.setCustomView(v);

        adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, arrayListBtnName);
        textView = (AutoCompleteTextView) v.findViewById(R.id.editText1);
        textView.setAdapter(adapter);
        textView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                checkWord();
            }
        });

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkWord();
            }
        });
        textView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkWord();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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

    public void checkWord() {
        String word = textView.getText().toString();
        if (!TextUtils.isEmpty(word)) {
            if (arrayListBtnName.contains(word))
                focusOnView(getBtn(word));
            else
                Toast.makeText(context, "word not found!", Toast.LENGTH_LONG).show();
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
                }
            });
        else
            Toast.makeText(context,"Button not found!",Toast.LENGTH_LONG).show();
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
