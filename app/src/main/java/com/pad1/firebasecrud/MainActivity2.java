package com.pad1.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {
    EditText mUniversityName, mUniversityAddress,mUpdateUniName,mUpdateUniAddress;
    DatabaseReference mDatabaseReferences;
    University mUniversity;
    String key;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mUniversityName = findViewById(R.id.uniname_edittext);
        mUniversityAddress = findViewById(R.id.uniaddress_edittext);
        mUpdateUniName = findViewById(R.id.update_uniname);
        mUpdateUniAddress = findViewById(R.id.update_uniaddress);
        mDatabaseReferences = FirebaseDatabase.getInstance().getReference(University.class.getSimpleName());
        
        
        findViewById(R.id.uniinsert_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
        
        findViewById(R.id.read_data2_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
            }
        });

        findViewById(R.id.update_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        findViewById(R.id.delete_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });
    }

    private void deleteData() {
        mDatabaseReferences.child(key).removeValue();
    }

    private void updateData() {
        University updatedData = new University();
        updatedData.setUniversityName(mUpdateUniName.getText().toString());
        updatedData.setUniversityAddress(mUpdateUniAddress.getText().toString());
        mDatabaseReferences.child(key).setValue(updatedData);

    }

    private void readData() {
        mUniversity = new University();
        mDatabaseReferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for (DataSnapshot currentData : snapshot.getChildren()){
                        key = currentData.getKey();
                        mUniversity.setUniversityName(currentData.child("universityName").getValue().toString());
                        mUniversity.setUniversityAddress(currentData.child("universityAddress").getValue().toString());
                    }
                }

                mUpdateUniName.setText(mUniversity.getUniversityName());
                mUpdateUniAddress.setText(mUniversity.getUniversityAddress());
                Toast.makeText(MainActivity2.this,"Data Has been Shown!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void insertData() {
        University newUniversity = new University();
        String univname = mUniversityName.getText().toString();
        String univaddress = mUniversityAddress.getText().toString();
        if(univname!="" && univaddress != ""){
            newUniversity.setUniversityName(univname);
            newUniversity.setUniversityAddress(univaddress);

            mDatabaseReferences.push().setValue(newUniversity);
            Toast.makeText(this,"Successfully insert data",Toast.LENGTH_SHORT).show();
        }
    }
}