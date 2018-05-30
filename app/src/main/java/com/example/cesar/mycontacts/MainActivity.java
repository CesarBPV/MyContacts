package com.example.cesar.mycontacts;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.cesar.mycontacts.dao.ContactDao;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView nav;
    private final int REQUEST_PERMISSION_PHONE_CALL=1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    System.out.println("hola");
                    Intent intent = new Intent(getApplicationContext(), NewContactActivity.class);
                    intent.putExtra("edit", false);
                    startActivity(intent);
                    finish();
                    return true;
            }
            return false;
        }
    };

    ArrayList<Map<String, String>> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav = (BottomNavigationView) findViewById(R.id.navigation);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ContactDao cd = new ContactDao(this);
        contacts = cd.listAlumnos();
        cd.cerrarDB();
        ListAdapter adapter = new SimpleAdapter(this, contacts, R.layout.item_contact, new String[]{"nombrescompletos", "numero"}, new int[]{R.id.nomc, R.id.numc});
        final ListView listContacts = findViewById(R.id.list_contacts);
        listContacts.setAdapter(adapter);
        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                System.out.println("tel:" + contacts.get(position).get("numero"));
                callIntent.setData(Uri.parse("tel:" + contacts.get(position).get("numero")));
                System.out.println("holi");
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},REQUEST_PERMISSION_PHONE_CALL);
                    return;
                }else{
                    startActivity(callIntent);
                }
            }
        });
        SearchView search=findViewById(R.id.search_contact);
        listContacts.setTextFilterEnabled(true);
        search.setSubmitButtonEnabled(true);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    listContacts.clearTextFilter();
                } else {
                    listContacts.setFilterText(newText);
                }
                return true;
            }
        });
        registerForContextMenu(listContacts);
    }

    String[] menuItems={"Editar","Eliminar"};

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId()==R.id.list_contacts){
            AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(contacts.get(info.position).get("nombrescompletos"));
            for(int i =0;i<menuItems.length;i++){
                menu.add(Menu.NONE,i,i,menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ContactDao cd=new ContactDao(getApplicationContext());
        switch(item.getItemId()){
            case 0:
                Bundle bundle=new Bundle();
                bundle.putBoolean("edit",true);
                bundle.putString("nombres",contacts.get(info.position).get("nombres"));
                bundle.putString("apellidos",contacts.get(info.position).get("apellidos"));
                bundle.putString("numero",contacts.get(info.position).get("numero"));
                bundle.putString("idcontact",contacts.get(info.position).get("idcontact"));
                bundle.putString("correo",contacts.get(info.position).get("email"));
                Intent intent=new Intent(getApplicationContext(),NewContactActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case 1:
                System.out.println(contacts.get(info.position).get("idcontact"));
                cd.deleteContact(contacts.get(info.position).get("idcontact"));
                finish();
                startActivity(getIntent());
                break;
        }
        cd.cerrarDB();
        return true;
    }
}
