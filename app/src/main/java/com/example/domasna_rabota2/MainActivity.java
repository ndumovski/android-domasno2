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

    //private Map<String, String> dictionary = new HashMap<>();
    private static final Map<String, String> enToMkDictionary = new HashMap<>();
    private static final Map<String, String> mkToEnDictionary = new HashMap<>();
    private TableLayout resultTableLayout;

    String message = getString(R.string.not_found);
    TextView notFoundTextView = new TextView(this);

    public void setNotFoundTextView(TextView message) {
        this.notFoundTextView = message;
        notFoundTextView.setTextAppearance(R.style.notFoundMessage);
        notFoundTextView.setGravity(Gravity.CENTER);
    }


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
                displaySearchResults(searchTerm);
            }
        });

        // Set onClickListener for clearButton
        clearButton.setOnClickListener(v -> resultTableLayout.removeAllViews());
    }

    private void readDictionary() {
        InputStream inputStream = getResources().openRawResource(R.raw.en_mk_recnik);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    enToMkDictionary.put(parts[0], parts[1]);
                    mkToEnDictionary.put(parts[1], parts[0]);
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


    private void displaySearchResults(String searchTerm) {
        boolean exists = false;

        // Search in English to Macedonian
        if (enToMkDictionary.containsKey(searchTerm.toLowerCase())) {
            addResultRow(enToMkDictionary.get(searchTerm.toLowerCase()));
            exists = true;
        }

        // Search in Macedonian to English
        if (mkToEnDictionary.containsKey(searchTerm.toLowerCase())) {
            addResultRow(mkToEnDictionary.get(searchTerm.toLowerCase()));
            exists = true;
        }

        //If word doesn't exists
        if (!exists) {
            addResultRow(getString(R.string.not_found));
        }
    }
    private void addResultRow(String translation) {
        TableRow row = new TableRow(this);
        TextView textView = new TextView(this);

        textView.setText(translation);
        textView.setTextAppearance(R.style.TranslationTextViewStyle);
        textView.setGravity(Gravity.CENTER);

        row.addView(textView);
        resultTableLayout.addView(row);
    }
}