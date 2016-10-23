package com.example.djc.kanquimaniapark.Admin;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.djc.kanquimaniapark.Admin.fragments.GenerarReporte;
import com.example.djc.kanquimaniapark.Admin.fragments.GestionAtracciones;
import com.example.djc.kanquimaniapark.Admin.GestionEmpleados.GestionEmpleados;
import com.example.djc.kanquimaniapark.Admin.fragments.GestionEspeciales;
import com.example.djc.kanquimaniapark.Admin.fragments.GestionProductos;
import com.example.djc.kanquimaniapark.R;

import java.util.ArrayList;
import java.util.List;


public class SimpleTabsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_tabs);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        viewPager.setOffscreenPageLimit(6);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GestionEmpleados(), getString(R.string.gestion_empleados));
        adapter.addFragment(new GestionProductos(), getString(R.string.gestion_productos));
        adapter.addFragment(new GestionEspeciales(), getString(R.string.gestion_especiales));
        adapter.addFragment(new GestionAtracciones(), getString(R.string.gestion_atracciones));
        adapter.addFragment(new GenerarReporte(), getString(R.string.generar_reporte));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}