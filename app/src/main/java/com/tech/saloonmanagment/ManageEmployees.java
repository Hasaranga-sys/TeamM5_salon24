package com.tech.saloonmanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tech.saloonmanagment.Models.EmployeeDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageEmployees extends AppCompatActivity {

    Button button;
    ListView listView;
    private List<EmployeeDetails> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employees);

        button = (Button)findViewById(R.id.button);
        listView = (ListView)findViewById(R.id.listview);

        user = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageEmployees.this, AddEmployee.class);
                startActivity(intent);
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("Employees");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot taskDatasnap : dataSnapshot.getChildren()){

                    EmployeeDetails employeeDetails = taskDatasnap.getValue(EmployeeDetails.class);
                    user.add(employeeDetails);
                }

                MyAdapter adapter = new MyAdapter(ManageEmployees.this, R.layout.custom_employee, (ArrayList<EmployeeDetails>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {

        TextView COL1;
        TextView COL2;
        TextView COL3;
        TextView COL4;
        Button button1;
        Button button2;

    }

    class MyAdapter extends ArrayAdapter<EmployeeDetails> {
        LayoutInflater inflater;
        Context myContext;
        List<Map<String, String>> newList;
        List<EmployeeDetails> user;


        public MyAdapter(Context context, int resource, ArrayList<EmployeeDetails> objects) {
            super(context, resource, objects);
            myContext = context;
            user = objects;
            inflater = LayoutInflater.from(context);
            int y;
            String barcode;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.custom_employee, null);

                holder.COL1 = (TextView) view.findViewById(R.id.id);
                holder.COL2 = (TextView) view.findViewById(R.id.name);
                holder.COL3 = (TextView) view.findViewById(R.id.comtact);
                holder.COL4 = (TextView) view.findViewById(R.id.time);
                holder.button1=(Button)view.findViewById(R.id.delete);
                holder.button2=(Button)view.findViewById(R.id.edit);

                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getId());
            holder.COL2.setText(user.get(position).getName());
            holder.COL3.setText(user.get(position).getContact());
            holder.COL4.setText(user.get(position).getTime());

            System.out.println(holder);

            final String idd = user.get(position).getId();

            holder.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Do you want to delete this task?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    String userid = user.get(position).getId();

                                    FirebaseDatabase.getInstance().getReference("Employees").child(idd).removeValue();
                                    Toast.makeText(myContext, "Deleted successfully", Toast.LENGTH_SHORT).show();

                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                }
            });

            holder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view1 = inflater.inflate(R.layout.custom_update_employee,null);
                    dialogBuilder.setView(view1);

                    final EditText editText1 = (EditText)view1.findViewById(R.id.updateempName);
                    final EditText editText2 = (EditText)view1.findViewById(R.id.updateempAge);
                    final EditText editText3 = (EditText)view1.findViewById(R.id.updateempCoatcat);
                    final EditText editText4 = (EditText)view1.findViewById(R.id.updateempAddress);
                    final EditText editText5 = (EditText)view1.findViewById(R.id.updateempTime);
                    final EditText editText6 = (EditText)view1.findViewById(R.id.updateempsalary);
                    final Button button = (Button)view1.findViewById(R.id.updateempadd);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Employees").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("name").getValue();
                            String age = (String) snapshot.child("age").getValue();
                            String contact = (String) snapshot.child("contact").getValue();
                            String address = (String) snapshot.child("address").getValue();
                            String time = (String) snapshot.child("time").getValue();
                            String price = (String) snapshot.child("salary").getValue();

                            editText1.setText(name);
                            editText2.setText(age);
                            editText3.setText(contact);
                            editText4.setText(address);
                            editText5.setText(time);
                            editText6.setText(price);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = editText1.getText().toString();
                            String age = editText2.getText().toString();
                            String contact =editText3.getText().toString();
                            String address = editText4.getText().toString();
                            String time = editText5.getText().toString();
                            String price = editText6.getText().toString();


                            if (name.isEmpty()) {
                                editText1.setError("ID is required");
                            } else if (age.isEmpty()) {
                                editText2.setError("Name is required");
                            }  else if (contact.isEmpty()) {
                                editText3.setError("Age is required");
                            } else if (address.isEmpty()) {
                                editText4.setError("Contact is required");
                            }else if (time.isEmpty()) {
                                editText5.setError("Time Type is required");
                            }
                            else if (price.isEmpty()) {
                                editText6.setError("Salary is required");
                            }else {
//
                                HashMap map = new HashMap();
                                map.put("name",name);
                                map.put("age",age);
                                map.put("contact",contact);
                                map.put("address",address);
                                map.put("time",time);
                                map.put("salary",price);
                                reference.updateChildren(map);

                                Toast.makeText(ManageEmployees.this, "Employee updated successfully", Toast.LENGTH_SHORT).show();

                                alertDialog.dismiss();
                            }
                        }
                    });
                }
            });

            return view;
        }

    }
}