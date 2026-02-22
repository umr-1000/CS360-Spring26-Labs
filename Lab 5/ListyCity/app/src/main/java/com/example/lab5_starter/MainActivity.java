package com.example.lab5_starter;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CityDialogFragment.CityDialogListener {

    private Button addCityButton;
    private Button deleteCityButton;
    private ListView cityListView;

    private ArrayList<City> cityArrayList;
    private ArrayAdapter<City> cityArrayAdapter;

    private FirebaseFirestore db;
    private CollectionReference citiesRef;

    private int selectedPosition = -1; // Track selection for deletion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set views
        addCityButton = findViewById(R.id.buttonAddCity);
        deleteCityButton = findViewById(R.id.buttonDeleteCity);
        cityListView = findViewById(R.id.listviewCities);

        // create city array
        cityArrayList = new ArrayList<>();
        cityArrayAdapter = new CityArrayAdapter(this, cityArrayList);
        cityListView.setAdapter(cityArrayAdapter);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        citiesRef = db.collection("cities");

        citiesRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (value != null) {
                cityArrayList.clear();
                for (DocumentSnapshot snapshot : value) {
                    String name = snapshot.getString("name");
                    String province = snapshot.getString("province");
                    cityArrayList.add(new City(name, province));
                }
                cityArrayAdapter.notifyDataSetChanged();
                selectedPosition = -1; // Reset selection when data changes
            }
        });

        // set listeners
        addCityButton.setOnClickListener(view -> {
            CityDialogFragment cityDialogFragment = new CityDialogFragment();
            cityDialogFragment.show(getSupportFragmentManager(),"Add City");
        });

        deleteCityButton.setOnClickListener(view -> {
            if (selectedPosition != -1 && selectedPosition < cityArrayList.size()) {
                City cityToDelete = cityArrayList.get(selectedPosition);
                // Delete from Firebase using city name as document ID
                citiesRef.document(cityToDelete.getName())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firestore", "City successfully deleted!");
                            selectedPosition = -1;
                        })
                        .addOnFailureListener(e -> Log.w("Firestore", "Error deleting city", e));
            } else {
                Log.d("MainActivity", "No city selected for deletion");
            }
        });

        cityListView.setOnItemClickListener((adapterView, view, i, l) -> {
            selectedPosition = i;
        });

        // Long click to edit/view details
        cityListView.setOnItemLongClickListener((parent, view, position, id) -> {
            City city = cityArrayList.get(position);
            CityDialogFragment cityDialogFragment = CityDialogFragment.newInstance(city);
            cityDialogFragment.show(getSupportFragmentManager(),"City Details");
            return true;
        });
    }

    @Override
    public void updateCity(City city, String newName, String newProvince) {
        // If the name changed, we need to delete the old document and add a new one
        if (!city.getName().equals(newName)) {
            citiesRef.document(city.getName()).delete();
        }
        addCity(new City(newName, newProvince));
    }

    @Override
    public void addCity(City city){
        // Using city name as document ID for simplicity in deletion
        citiesRef.document(city.getName()).set(city)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "City successfully saved!"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error saving city", e));
    }
}