package com.example.inventoryirecord;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.inventoryirecord.data.ReceiptResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    private TextView mTextView;
    private ShowReceiptAnalyseViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button viewInventoryButton = findViewById(R.id.btn_view_edit_inventory);
        intent = new Intent(this, ViewInventoryActivity.class);
        viewInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        mTextView = findViewById(R.id.tv_test_network);

        Button addItemButton = findViewById(R.id.btn_add_inventory);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddItemScreen();
            }
        });

        mViewModel = new ViewModelProvider(this).get(ShowReceiptAnalyseViewModel.class);

        mViewModel.getSearchResults().observe(this, new Observer<ReceiptResult>() {
            @Override
            public void onChanged(ReceiptResult gitHubRepos) {
                if (gitHubRepos != null)
                    mTextView.setText(new Gson().toJson(gitHubRepos));
            }
        });

        Button searchButton = findViewById(R.id.btn_test_network);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.loadAnalyseResults("url", "https://www.snopes.com/tachyon/2016/06/fsfull.jpg");
            }
        });
    }

    private void goToAddItemScreen() {
        Intent intent = new Intent(this, addItemDetailActivity.class);
        startActivity(intent);
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
}
