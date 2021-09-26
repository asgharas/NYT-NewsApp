package com.asghar.adapterpractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataProcessor.ProcessorCallBack, NewsAdapter.NewsClickListener {
    private static final String TAG = "MainActivity";
    private NewsAdapter adapter;
    private String url = "https://rss.nytimes.com/services/xml/rss/nyt/%s.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        adapter = new NewsAdapter(new ArrayList<>());

        recyclerView.setAdapter(adapter);
        adapter.setNewsClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        MenuItem item = menu.findItem(R.id.action_bar_spinner);

        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        String[] sections = getResources().getStringArray(R.array.search_sections);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_dropdown, sections);
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedSect = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(getBaseContext(), "You Selected: " + selectedSect, Toast.LENGTH_SHORT).show();
                downlaodData(selectedSect);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bar_spinner) {
//            Log.d(TAG, "onOptionsItemSelected: Selected:--->>>");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(List<News> newsList) {
        adapter.updateDataset(newsList);
    }

    @Override
    public void onNewsClick(View view, int position) {
        Intent intent = new Intent(this, Article.class);
        intent.putExtra(Global.NEWS_KEY, adapter.getItem(position));
        startActivity(intent);
    }

    private void downlaodData(String newsSection){
        DataProcessor dataProcessor = new DataProcessor();
        dataProcessor.execute(String.format(url, newsSection), this);

    }
}