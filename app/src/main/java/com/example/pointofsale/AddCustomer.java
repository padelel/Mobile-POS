package com.example.pointofsale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCustomer extends AppCompatActivity {

    EditText addName, addEmail, addAddress, addPhone;
    Button btn_Add, btnBack;

    //private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        addName = findViewById(R.id.etName);
        addEmail = findViewById(R.id.etEmail);
        addAddress = findViewById(R.id.etAddress);
        addPhone = findViewById(R.id.etPhone);
        btn_Add = findViewById(R.id.btnAdd);

        progressDialog = new ProgressDialog(AddCustomer.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Menyimpan...");

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(AddCustomer.this, CustomerPage.class);
                startActivity(back);
            }

        });

        btn_Add.setOnClickListener(v -> {
            if (addName.getText().length()>0 && addEmail.getText().length()>0 && addAddress.getText().length()>0 && addPhone.getText().length()>0){
                saveData(addName.getText().toString(), addEmail.getText().toString(),addAddress.getText().toString(),addPhone.getText().toString());
            }else{
                Toast.makeText(getApplicationContext(), "Silahkan isi semua data", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        if(intent!=null) {
            id = intent.getStringExtra("id");
            addName.setText(intent.getStringExtra("name"));
            addEmail.setText(intent.getStringExtra("email"));
            addAddress.setText(intent.getStringExtra("address"));
            addPhone.setText(intent.getStringExtra("phone"));
        }
    }

    private void saveData(String name, String email, String address, String phone){
        Map<String, Object> customer = new HashMap<>();
        customer.put("name", name);
        customer.put("email", email);
        customer.put("address", address);
        customer.put("phone", phone);

        progressDialog.show();
        if(id!=null) {
            db.collection("customer").document(id)
                    .set(customer)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            db.collection("customer")
                    .add(customer)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        }
    }
}