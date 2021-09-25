package com.tech.saloonmanagment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tech.saloonmanagment.Models.EmployeeDetails;

public class AddEmployee extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    EditText editText6;
    EditText editText7;
    EditText editText8;
    Button button;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        editText1=(EditText)findViewById(R.id.empid);
        editText2=(EditText)findViewById(R.id.empName);
        editText3=(EditText)findViewById(R.id.empAge);
        editText4=(EditText)findViewById(R.id.empCoatcat);
        editText5=(EditText)findViewById(R.id.empAddress);
        editText6=(EditText)findViewById(R.id.empTime);
        editText7=(EditText)findViewById(R.id.empappointmentPrice);
        editText8=(EditText)findViewById(R.id.empsalary);
        button=(Button)findViewById(R.id.empadd);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference("Employees");

                String id = editText1.getText().toString();
                String name = editText2.getText().toString();
                String age =editText3.getText().toString();
                String contact = editText4.getText().toString();
                String address = editText5.getText().toString();
                String time = editText6.getText().toString();
                String price = editText7.getText().toString();
                String salary = editText8.getText().toString();


                if (id.isEmpty()) {
                    editText1.setError("ID is required");
                } else if (name.isEmpty()) {
                    editText2.setError("Name is required");
                }  else if (age.isEmpty()) {
                    editText3.setError("Age is required");
                } else if (contact.isEmpty()) {
                    editText4.setError("Contact is required");
                } else if (address.isEmpty()) {
                    editText5.setError("Address is required");
                } else if (time.isEmpty()) {
                    editText6.setError("Shift time is required");
                }else if (price.isEmpty()) {
                    editText7.setError("Appointment Price is required");
                }else if (salary.isEmpty()) {
                    editText7.setError("Salary is required");
                } else {

                    EmployeeDetails employeeDetails = new EmployeeDetails(id,name,age,contact,address,time,price,salary);
                    reference.child(id).setValue(employeeDetails);

                    Toast.makeText(AddEmployee.this, "Employee added successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}