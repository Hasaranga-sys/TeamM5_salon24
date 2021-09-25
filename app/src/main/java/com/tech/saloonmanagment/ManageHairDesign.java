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
import com.tech.saloonmanagment.Models.DesignDetails;
import com.tech.saloonmanagment.Models.EmployeeDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageHairDesign extends AppCompatActivity {

    Button button;
    ListView listView;
    private List<DesignDetails> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_hair_design);

        button = (Button)findViewById(R.id.button);
        listView = (ListView)findViewById(R.id.listview);

        user = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageHairDesign.this, AddDesign.class);
                startActivity(intent);
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("Designs");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot taskDatasnap : dataSnapshot.getChildren()){

                    DesignDetails designDetails = taskDatasnap.getValue(DesignDetails.class);
                    user.add(designDetails);
                }

                MyAdapter adapter = new MyAdapter(ManageHairDesign.this, R.layout.custom_designs, (ArrayList<DesignDetails>) user);
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

    class MyAdapter extends ArrayAdapter<DesignDetails> {
        LayoutInflater inflater;
        Context myContext;
        List<Map<String, String>> newList;
        List<DesignDetails> user;


        public MyAdapter(Context context, int resource, ArrayList<DesignDetails> objects) {
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
            final ManageEmployees.ViewHolder holder;
            if (view == null) {
                holder = new ManageEmployees.ViewHolder();
                view = inflater.inflate(R.layout.custom_designs, null);

                holder.COL1 = (TextView) view.findViewById(R.id.id);
                holder.COL2 = (TextView) view.findViewById(R.id.name);
                holder.COL3 = (TextView) view.findViewById(R.id.price);
                holder.COL4 = (TextView) view.findViewById(R.id.description);
                holder.button1=(Button)view.findViewById(R.id.delete);
                holder.button2=(Button)view.findViewById(R.id.edit);

                view.setTag(holder);
            } else {

                holder = (ManageEmployees.ViewHolder) view.getTag();
            }

            holder.COL1.setText(user.get(position).getId());
            holder.COL2.setText(user.get(position).getName());
            holder.COL3.setText(user.get(position).getPrice());
            holder.COL4.setText(user.get(position).getDescription());

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

                                    FirebaseDatabase.getInstance().getReference("Designs").child(idd).removeValue();
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
                    View view1 = inflater.inflate(R.layout.update_design_details,null);
                    dialogBuilder.setView(view1);

                    final EditText editText1 = (EditText)view1.findViewById(R.id.updatedesName);
                    final EditText editText2 = (EditText)view1.findViewById(R.id.updatedesPrice);
                    final EditText editText3 = (EditText)view1.findViewById(R.id.updatedesDescription);
                    final Button button = (Button)view1.findViewById(R.id.update);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Designs").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("name").getValue();
                            String price = (String) snapshot.child("price").getValue();
                            String description = (String) snapshot.child("description").getValue();

                            editText1.setText(name);
                            editText2.setText(price);
                            editText3.setText(description);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = editText1.getText().toString();
                            String price = editText2.getText().toString();
                            String description =editText3.getText().toString();


                            if (name.isEmpty()) {
                                editText1.setError("Name is required");
                            } else if (price.isEmpty()) {
                                editText2.setError("Price is required");
                            }  else if (description.isEmpty()) {
                                editText3.setError("description is required");
                            }else {
//
                                HashMap map = new HashMap();
                                map.put("name",name);
                                map.put("price",price);
                                map.put("description",description);
                                reference.updateChildren(map);

                                Toast.makeText(ManageHairDesign.this, "Design updated successfully", Toast.LENGTH_SHORT).show();

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