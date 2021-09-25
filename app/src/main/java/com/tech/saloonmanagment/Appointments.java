package com.tech.saloonmanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tech.saloonmanagment.Models.AppointmentDeatils;
import com.tech.saloonmanagment.Models.EmployeeDetails;

import java.util.ArrayList;
import java.util.List;

public class Appointments extends AppCompatActivity {

    Button button;
    ListView listView;
    private List<EmployeeDetails> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        button = (Button)findViewById(R.id.button);
        listView = (ListView)findViewById(R.id.listview);

        user = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Appointments.this, AppointmentList.class);
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

                MyAdapter adapter = new MyAdapter(Appointments.this, R.layout.custom_appointment, (ArrayList<EmployeeDetails>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {

        ImageView imageView;
        TextView COL1;
        TextView COL2;
        TextView COL3;
        Button button;
    }

    class MyAdapter extends ArrayAdapter<EmployeeDetails> {
        LayoutInflater inflater;
        Context myContext;
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
                view = inflater.inflate(R.layout.custom_appointment, null);

                holder.COL1 = (TextView) view.findViewById(R.id.barber_name);
                holder.COL2 = (TextView) view.findViewById(R.id.barber_time);
                holder.COL3 = (TextView) view.findViewById(R.id.barber_price);
                holder.button = (Button) view.findViewById(R.id.addAppointment);


                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getName());
            holder.COL2.setText(user.get(position).getTime());
            holder.COL3.setText(user.get(position).getAppointmentPrice());
            System.out.println(holder);


            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view = inflater.inflate(R.layout.custom_add_appointment_details, null);
                    dialogBuilder.setView(view);

                    final TextView textView1 = (TextView) view.findViewById(R.id.cd_id);
                    final TextView textView2 = (TextView) view.findViewById(R.id.cd_name);
                    final TextView textView3 = (TextView) view.findViewById(R.id.cd_contact);
                    final TextView textView4 = (TextView) view.findViewById(R.id.cd_time);
                    final TextView textView5 = (TextView) view.findViewById(R.id.cd_price);
                    final EditText editText1 = (EditText) view.findViewById(R.id.cuname);
                    final EditText editText2 = (EditText) view.findViewById(R.id.cunic);
                    final EditText editText3 = (EditText) view.findViewById(R.id.cucontat);
                    final EditText editText4 = (EditText) view.findViewById(R.id.cuaddress);
                    final Button buttonAdd = (Button) view.findViewById(R.id.uaddnow);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Employees").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("name").getValue();
                            String contact = (String) snapshot.child("contact").getValue();
                            String time = (String) snapshot.child("time").getValue();
                            String price = (String) snapshot.child("appointmentPrice").getValue();

                            textView1.setText(id);
                            textView2.setText(name);
                            textView3.setText(contact);
                            textView4.setText(time);
                            textView5.setText(price);

                            buttonAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AppointmentRequests");

                                    final String username = editText1.getText().toString();
                                    final String nic = editText2.getText().toString();
                                    final String contact = editText3.getText().toString();
                                    final String address = editText4.getText().toString();
                                    String Id = textView1.getText().toString();
                                    String Name = textView2.getText().toString();
                                    String contactno = textView3.getText().toString();
                                    String time = textView4.getText().toString();
                                    Integer price = Integer.valueOf(textView5.getText().toString());

                                    Integer tax = (price*2) / 100 ;
                                    String total = String.valueOf(price+tax);

                                    if (username.isEmpty()) {
                                        editText1.setError("Name is required");
                                    }else if (nic.isEmpty()) {
                                        editText2.setError("NIC is required");
                                    }else if (contactno.isEmpty()) {
                                        editText3.setError("Contact Number is required");
                                    }else if (address.isEmpty()) {
                                        editText4.setError("Address is required");
                                    }else {

                                        AppointmentDeatils appointmentDetails = new AppointmentDeatils(Id,Name,time ,total,username,nic,contactno,address);
                                        reference.child(Id).setValue(appointmentDetails);

                                        Toast.makeText(Appointments.this, "Successfully added", Toast.LENGTH_SHORT).show();

                                        alertDialog.dismiss();
                                    }

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

                }

            });

            return view;

        }
    }
}