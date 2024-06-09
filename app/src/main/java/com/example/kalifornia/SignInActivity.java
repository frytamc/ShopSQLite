package com.example.kalifornia;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignInActivity extends AppCompatActivity {

    EditText login, password, repassword;

    TextView haveACC;
    Button signInBtn;

    DBLogin dbLogin;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        login = findViewById(R.id.new_username);
        password = findViewById(R.id.new_password);
        repassword = findViewById(R.id.new_repassword);
        signInBtn = findViewById(R.id.sign_in_btn);
        haveACC = findViewById(R.id.have_acc);

        haveACC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LogInActivity.class);
                startActivity(intent);
            }
        });

        dbLogin = new DBLogin(this);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = login.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if (login.getText().toString().length() <= 0){
                    login.setError("Please set username");
                }else if(password.getText().toString().length() <= 0){
                    password.setError("Please set password");
                }else if(repassword.getText().toString().length() <= 0){
                    repassword.setError("Please set repassword");
                } else if (login.equals("")||password.equals("")||repassword.equals("")) {
                    Toast.makeText(getApplicationContext(),"Empty fields", Toast.LENGTH_SHORT).show();
                }else {
                    if (pass.equals(repass)){
                        Boolean checkUser = checkUsername(user);
                        if (checkUser == false){
                            Boolean inserts = insertData(user,pass);
                            if (inserts == true){
                                Toast.makeText(getApplicationContext(),"Registration success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(),"Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                        builder.setTitle("Try again");
                        builder.setIcon(R.drawable.baseline_error_24);
                        builder.setMessage("Password not match");
                        builder.show();
                    }
                }

            }
        });

    }

    private Boolean insertData(String user, String pass) {
        sqLiteDatabase = dbLogin.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user);
        contentValues.put("password", pass);

        long rec = sqLiteDatabase.insert("users", null, contentValues);

        if (rec == -1){
            return false;
        }else {
            return true;
        }
    }

    private Boolean checkUsername(String user) {
        sqLiteDatabase = dbLogin.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Users WHERE username=?", new String[]{user});
        if (cursor.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }
}