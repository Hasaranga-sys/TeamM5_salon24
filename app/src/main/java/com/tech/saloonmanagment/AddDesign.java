package com.tech.saloonmanagment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tech.saloonmanagment.Models.DesignDetails;
import com.tech.saloonmanagment.Models.EmployeeDetails;

public class AddDesign extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    Button button;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_design);

        editText1=(EditText)findViewById(R.id.desid);
        editText2=(EditText)findViewById(R.id.desName);
        editText3=(EditText)findViewById(R.id.desPrice);
        editText4=(EditText)findViewById(R.id.desDescription);
        button=(Button)findViewById(R.id.desadd);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference("Designs");

                String id = editText1.getText().toString();
                String name = editText2.getText().toString();
                String price =editText3.getText().toString();
                String description = editText4.getText().toString();


                if (id.isEmpty()) {
                    editText1.setError("ID is required");
                } else if (name.isEmpty()) {
                    editText2.setError("Name is required");
                }  else if (price.isEmpty()) {
                    editText3.setError("Price is required");
                } else if (description.isEmpty()) {
                    editText4.setError("Description is required");
                } else {

                    DesignDetails designDetails = new DesignDetails(id,name,price,description);
                    reference.child(id).setValue(designDetails);

                    Toast.makeText(AddDesign.this, "Design added successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}