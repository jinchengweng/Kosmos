package com.fct.kosmos.view.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.fct.kosmos.R;

import java.util.UUID;

public class ProductActivity extends AppCompatActivity {
    private static final String EXTRA_PRODUCT_ID = "com.example.android.stockkeepingassistant.product_id";

    public static Intent newIntent(Context packageContext, UUID productId) {
        Intent intent = new Intent(packageContext, ProductActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().setTitle(R.string.product_fragment_title);

        UUID productId = (UUID) getIntent().getSerializableExtra(EXTRA_PRODUCT_ID);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fm.beginTransaction()
                    .add(R.id.fragment_container, ProductFragment.newInstance(productId))
                    .commit();
        }
    }
}
