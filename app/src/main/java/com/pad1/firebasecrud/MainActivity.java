package com.pad1.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText mNameEditText,mAddressEditText, mUpdateNameEditText,
            mUpdateAddressEditText;
    DatabaseReference mDatabaseReference;
    Student mStudent;
    String Key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNameEditText = findViewById(R.id.name_edittext);
        mAddressEditText = findViewById(R.id.address_edittext);
        mUpdateAddressEditText = findViewById(R.id.update_address);
        mUpdateNameEditText = findViewById(R.id.update_name);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Student.class.getSimpleName());


        findViewById(R.id.insert_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        findViewById(R.id.read_data_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
            }
        });
        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
        findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });


    }


    private void insertData() {
        Student newStudent = new Student();
        String name = mNameEditText.getText().toString();
        String address = mAddressEditText.getText().toString();
        if(name!="" && address != ""){
            newStudent.setName(name);
            newStudent.setAddress(address);

            mDatabaseReference.push().setValue(newStudent);
            Toast.makeText(this,"Successfully insert data",Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteData(){
        mDatabaseReference.child(Key).removeValue();
    }
    private void updateData() {
        Student updatedData = new Student();
        updatedData.setName(mUpdateNameEditText.getText().toString());
        updatedData.setAddress(mUpdateAddressEditText.getText().toString());
         mDatabaseReference.child(Key).setValue(updatedData);


    }

    private void readData() {
        mStudent = new Student();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for (DataSnapshot currentData : snapshot.getChildren()){
                        Key = currentData.getKey();
                        mStudent.setName(currentData.child("name").getValue().toString());
                        mStudent.setAddress(currentData.child("address").getValue().toString());
                    }
                }

                mUpdateNameEditText.setText(mStudent.getName());
                mUpdateAddressEditText.setText(mStudent.getAddress());
                Toast.makeText(MainActivity.this,"Data Has been Shown!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}