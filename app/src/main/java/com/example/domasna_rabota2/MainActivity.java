package com.example.domasna_rabota2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Map<String, String> dictionary = new HashMap<>();
    private TableLayout resultTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Read dictionary from file and populate the map
        readDictionary();

        // Get references to UI elements
        EditText searchEditText = findViewById(R.id.findText);
        Button searchButton = findViewById(R.id.searchButton);
        resultTableLayout = findViewById(R.id.queryTableLayout);
        Button clearButton = findViewById(R.id.clearTagsButton);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set onClickListener for searchButton
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchEditText.getText().toString();
                displaySearchResults(searchTerm, resultTableLayout);
            }
        });

        // Set onClickListener for clearButton
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultTableLayout.removeAllViews();
            }
        });
    }

    private void readDictionary() {
        InputStream inputStream = getResources().openRawResource(R.raw.en_mk_recnik);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    dictionary.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displaySearchResults(String searchTerm, TableLayout resultTableLayout) {
        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            if (entry.getKey().toLowerCase().contains(searchTerm.toLowerCase()) ||
                    entry.getValue().toLowerCase().contains(searchTerm.toLowerCase())) {
                addResultRow(entry.getKey(), entry.getValue(), resultTableLayout);
            }
        }
    }

    private void addResultRow(String english, String macedonian, TableLayout resultTableLayout) {
        TableRow row = new TableRow(this);
        TextView englishTextView = new TextView(this);
        TextView macedonianTextView = new TextView(this);

        englishTextView.setText(english);
        macedonianTextView.setText(macedonian);

        englishTextView.setTextAppearance(R.style.EnglishTextViewStyle);
        englishTextView.setGravity(Gravity.CENTER);
        macedonianTextView.setTextAppearance(R.style.MacedonianTextViewStyle);
        macedonianTextView.setGravity(Gravity.CENTER);

        row.addView(englishTextView);
        row.addView(macedonianTextView);
        resultTableLayout.addView(row);
    }
}