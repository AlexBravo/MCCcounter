package com.google.android.mcccounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //private EditText word;
    //private String input;
    //private TextView result;
    private ListView listview;
    private View button;
    private ArrayList<String> list = new ArrayList<>();
    //private String[] values = new String[32];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //word = (EditText) findViewById(R.id.word);
        //word.addTextChangedListener(tWatcher);
        //result = (TextView) findViewById(R.id.result);
        //input = word.getText().toString();

        listview = (ListView) findViewById(R.id.listview);
        button = findViewById(R.id.button);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unused")
    private final TextWatcher tWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            String rs = s.toString();
//            output = MccCounter.calculateMCCs(rs).toString();
//
//            result.setText(output.substring(1, output.length() - 1));
        }

        public void onTextChanged(CharSequence s, int st, int before, int count) {
            list.clear();
            String rs = s.toString();
            String output = MccCounter.calculateMCCs(rs).toString();
//            result.setText(output.substring(1, output.length() - 1));

            if(output.length() > 2) {
                String r = output.substring(0, output.length());
                int start = 0;
                int k = 0;
                for (int j = 0; j < r.length(); j++) {
                    if (r.substring(j, j + 1).equals(",")) {
                        list.add(r.substring(start+1, j));
                        start = j + 1;

                    }
                    if( r.substring(j, j + 1).equals("}")){
                        list.add(r.substring(start+1, j));
                    }
                }


            }

        }

        public void afterTextChanged(Editable s) {
//            String rs = s.toString();
//            output = MccCounter.calculateMCCs(rs).toString();
//
//            result.setText(output.substring(1, output.length() - 1));
        }
    };

    public void onClick(View view){
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        button.setVisibility(View.GONE);
    }
}
