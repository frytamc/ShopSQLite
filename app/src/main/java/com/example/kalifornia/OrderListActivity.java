package com.example.kalifornia;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class OrderListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> ordersList;
    private ArrayList<String> detailedOrdersList;
    private DataBaseHelper dbHelper;

    FloatingActionButton addOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        setSupportActionBar(findViewById(R.id.toolbar));

        listView = findViewById(R.id.list_view);
        ordersList = new ArrayList<>();
        detailedOrdersList = new ArrayList<>();
        dbHelper = new DataBaseHelper(this);

        addOrder = findViewById(R.id.fab);

        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        loadOrders();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String orderDetails = detailedOrdersList.get(position);
                Intent intent = new Intent(OrderListActivity.this, OrderDetailActivity.class);
                intent.putExtra("order_details", orderDetails);
                startActivity(intent);
            }
        });
    }

    private void loadOrders() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DataBaseHelper.ORDER_TABLE, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int countryIndex = cursor.getColumnIndex(DataBaseHelper.COUNTRY);
                String country = cursor.getString(countryIndex);


                int idIndex = cursor.getColumnIndex(DataBaseHelper.ID);
                int priceIndex = cursor.getColumnIndex(DataBaseHelper.PRICE);
                int dateIndex = cursor.getColumnIndex(DataBaseHelper.DATE);
                int customerIndex = cursor.getColumnIndex(DataBaseHelper.CUSTOMER);
                int armyIndex = cursor.getColumnIndex(DataBaseHelper.ARMY);
                int mineralsIndex = cursor.getColumnIndex(DataBaseHelper.MINERALS);
                int teamsIndex = cursor.getColumnIndex(DataBaseHelper.TEAMS);

                String orderId = cursor.getString(idIndex);
                int price = cursor.getInt(priceIndex);
                String date = cursor.getString(dateIndex);
                String customer = cursor.getString(customerIndex);
                String army = cursor.getString(armyIndex);
                String minerals = cursor.getString(mineralsIndex);
                String teams = cursor.getString(teamsIndex);

                ordersList.add("ID: " + orderId + "\nCountry: " + country);

                String orderDetails = "ID: " + orderId +
                        "\nCountry: " + country +
                        "\nPrice: $" + price +
                        "\nDate: " + date +
                        "\nCustomer: " + customer +
                        "\nArmy: " + army +
                        "\nMinerals: " + minerals +
                        "\nTeams: " + teams;

                detailedOrdersList.add(orderDetails);
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ordersList);
        listView.setAdapter(adapter);
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
            Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.log_out) {
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


}
