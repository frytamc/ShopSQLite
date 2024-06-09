package com.example.kalifornia;

import static com.example.kalifornia.DataBaseHelper.ORDER_TABLE;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    Spinner spinner;
    Spinner armySpinner;
    Spinner mineralsSpinner;
    Spinner teamsSpinner;

    EditText clientName;

    String[] country = { "USA", "GREAT BRITAIN", "SPAIN", "AUSTRALIA", "BRASIL"};
    int[] flags = { R.drawable.usa, R.drawable.wb, R.drawable.spain, R.drawable.australia, R.drawable.brasil };
    String[] countryPrices = {"$8300", "$6700","$6900","$6400","$5600"};

    String[] armyNames = {"Pies bojowy", "AK-47", "RPG"};
    int[] armyIcons = { R.drawable.cyber_dog, R.drawable.kalach, R.drawable.rpg };
    String[] armyPrices = {"$2700","$900","$1700"};

    String[] minNames = {"Gold", "Diament", "platyna"};
    int[] minPics = { R.drawable.zloto, R.drawable.diament, R.drawable.platina };
    String[] mineralPrices = {"$600","$1100","$200"};

    String[] teamNames = { "Kansas City CHIEFS", "Minnesota VIKINGS", "Houston TEXANS" };
    int[] teamPics = { R.drawable.kc, R.drawable.min, R.drawable.hou };
    String[] teamPrices = {"$3300","$2800","$2600"};

    TextView endPrice;
    CheckBox armyCheck, mineralCheck, teamCheck;
    Button addOrderBtn;

    DataBaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        dbHelper = new DataBaseHelper(this);

        armyCheck = findViewById(R.id.checkbox_army);
        mineralCheck = findViewById(R.id.checkbox_minerals);
        teamCheck = findViewById(R.id.checkbox_teams);

        clientName = findViewById(R.id.client_name);
        endPrice = findViewById(R.id.priceTag);
        addOrderBtn = findViewById(R.id.add_order_btn);

        // Initialize Spinners
        spinner = findViewById(R.id.spinner);
        armySpinner = findViewById(R.id.army_spinner);
        mineralsSpinner = findViewById(R.id.minerals_spinner);
        teamsSpinner = findViewById(R.id.teams_spinner);

        // Setup Spinners
        setupSpinner(spinner, flags, country, countryPrices);
        setupSpinner(armySpinner, armyIcons, armyNames, armyPrices);
        setupSpinner(mineralsSpinner, minPics, minNames, mineralPrices);
        setupSpinner(teamsSpinner, teamPics, teamNames, teamPrices);

        addOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });
    }

    private void setupSpinner(Spinner spinner, int[] images, String[] names, String[] prices) {
        MyAdapter adapter = new MyAdapter(getApplicationContext(), images, names, prices);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void insertData() {
        int price = 0;
        String customer = clientName.getText().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String army = "", min = "", team = "";


        if (customer.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("ERROR");
            builder.setIcon(R.drawable.baseline_error_24);
            builder.setMessage("ENTER KURWOZROXY");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            return;
        }

        ShopModel selectedCountry = (ShopModel) spinner.getSelectedItem();
        if (selectedCountry != null) {
            price += Integer.parseInt(selectedCountry.getPrice().replace("$", ""));
        } else {
            Toast.makeText(getApplicationContext(), "No country selected", Toast.LENGTH_SHORT).show();
            return;
        }

        if (armyCheck.isChecked()) {
            ShopModel selectedItem = (ShopModel) armySpinner.getSelectedItem();
            if (selectedItem != null) {
                army += selectedItem.getName();
                price += Integer.parseInt(selectedItem.getPrice().replace("$", ""));
            } else {
                Toast.makeText(getApplicationContext(), "No army selected", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (mineralCheck.isChecked()) {
            ShopModel selectedItem = (ShopModel) mineralsSpinner.getSelectedItem();
            if (selectedItem != null) {
                min += selectedItem.getName();
                price += Integer.parseInt(selectedItem.getPrice().replace("$", ""));
            } else {
                Toast.makeText(getApplicationContext(), "No mineral selected", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (teamCheck.isChecked()) {
            ShopModel selectedItem = (ShopModel) teamsSpinner.getSelectedItem();
            if (selectedItem != null) {
                team += selectedItem.getName();
                price += Integer.parseInt(selectedItem.getPrice().replace("$", ""));
            } else {
                Toast.makeText(getApplicationContext(), "No team selected", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        endPrice.setText("Total Price: $" + price);

        boolean isInserted = dbHelper.addOrder(selectedCountry.getName(), price, date, customer, army, min, team);
        if (isInserted) {
            Toast.makeText(getApplicationContext(), "Order added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Order addition failed", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        calculateTotalPrice();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void calculateTotalPrice() {
        int price = 0;

        ShopModel selectedCountry = (ShopModel) spinner.getSelectedItem();
        if (selectedCountry != null) {
            price += Integer.parseInt(selectedCountry.getPrice().replace("$", ""));
        }

        if (armyCheck.isChecked()) {
            ShopModel selectedItem = (ShopModel) armySpinner.getSelectedItem();
            if (selectedItem != null) {
                price += Integer.parseInt(selectedItem.getPrice().replace("$", ""));
            }
        }

        if (mineralCheck.isChecked()) {
            ShopModel selectedItem = (ShopModel) mineralsSpinner.getSelectedItem();
            if (selectedItem != null) {
                price += Integer.parseInt(selectedItem.getPrice().replace("$", ""));
            }
        }

        if (teamCheck.isChecked()) {
            ShopModel selectedItem = (ShopModel) teamsSpinner.getSelectedItem();
            if (selectedItem != null) {
                price += Integer.parseInt(selectedItem.getPrice().replace("$", ""));
            }
        }

        endPrice.setText("Total Price: $" + price);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.shop_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.order_list) {
            Intent intent = new Intent(getApplicationContext(),OrderListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.log_out) {
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.about) {
            showAbout();
            return true;
        }
        return false;
    }

    private void showAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("<---O autorze--->")
                .setMessage("Autorem jest Miłosz Król")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }
}
