package com.fct.kosmos.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fct.kosmos.R;
import com.fct.kosmos.other.ManualActivity;
import com.fct.kosmos.other.ShareActivity;
import com.fct.kosmos.view.ui.CatalogActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ClienteActivity extends AppCompatActivity {
    private ExampleAdapter adapter;
    private List<ExampleItem> clienteLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        fillExampleList();
        setUpRecyclerView();
    }

    private void fillExampleList() {
        clienteLista = new ArrayList<>();
        clienteLista.add(new ExampleItem(R.drawable.empresa, "One", "Ten"));
        clienteLista.add(new ExampleItem(R.drawable.empresa, "Two", "Eleven"));
        clienteLista.add(new ExampleItem(R.drawable.empresa, "Three", "Twelve"));
        clienteLista.add(new ExampleItem(R.drawable.empresa, "Four", "Thirteen"));
        clienteLista.add(new ExampleItem(R.drawable.empresa, "Five", "Fourteen"));
        clienteLista.add(new ExampleItem(R.drawable.empresa, "Six", "Fifteen"));
        clienteLista.add(new ExampleItem(R.drawable.empresa, "Seven", "Sixteen"));
        clienteLista.add(new ExampleItem(R.drawable.empresa, "Eight", "Seventeen"));
        clienteLista.add(new ExampleItem(R.drawable.empresa, "Nine", "Eighteen"));
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(clienteLista);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cliente_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
//TODO:apartir de aqui
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i = new Intent(this, CatalogActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_cliente) {
            Intent i = new Intent(this, ClienteActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_nosotros) {

        } else if (id == R.id.nav_manualUsuario) {
            Intent i = new Intent(this, ManualActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_share) {
            Intent i = new Intent(this, ShareActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}