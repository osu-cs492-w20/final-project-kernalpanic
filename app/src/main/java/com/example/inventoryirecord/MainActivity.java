package com.example.inventoryirecord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.inventoryirecord.data.azure.ReceiptResult;
import com.google.gson.Gson;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    private TextView mTextView;
    private AzureViewModel showReceiptAnalyseViewModel;

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

        Button addItemButton = findViewById(R.id.btn_add_inventory);
        addItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToAddItemScreen();
            }
        });

        // text view to show the analyze result
        mTextView = findViewById(R.id.tv_test_network);
        showReceiptAnalyseViewModel = new ViewModelProvider(this).get(AzureViewModel.class);

        showReceiptAnalyseViewModel.getSearchResults().observe(this, new Observer<ReceiptResult>() {
            @Override
            public void onChanged(ReceiptResult gitHubRepos) {
                if (gitHubRepos != null)
                    mTextView.setText(new Gson().toJson(gitHubRepos));
            }
        });

        showReceiptAnalyseViewModel.getMatchObjects().observe(this, new Observer<Set<String>>() {
            @Override
            public void onChanged(Set<String> s) {
                if (s == null) {
                    return;
                }
                StringBuilder sb = new StringBuilder();

                for (String s1 : s) {
                    sb.append("object Detected: ").append(s1).append("\n");
                }

                mTextView.setText(sb.toString());
            }
        });

        //a button, when click, upload the file in the file path and analyze, showing the result in text view
        Button searchButton = findViewById(R.id.btn_test_network);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hard coding analyze path
                String filePath = "/res/drawable/test_receipt.jpg";
                showReceiptAnalyseViewModel.loadAnalyseResults(filePath);
            }
        });

        // end of test analyse

        Button detectButton = findViewById(R.id.btn_test_object);
        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filePath = "/storage/emulated/0/DCIM/Camera/IMG_20200226_195656.jpg";
                acquirePermission();
//
//                String filePath = "/res/drawable/test_tv.png";
                acquirePermission();
                showReceiptAnalyseViewModel.loadDetectObjects(filePath);
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


    private void acquirePermission() {
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

    }
}
