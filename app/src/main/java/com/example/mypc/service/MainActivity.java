package com.example.mypc.service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import model.SanPham;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void Danhsachsanpham(View view){
        Intent intent = new Intent(MainActivity.this,DS_SanPham.class);
        startActivity(intent);
    }

    public void DanhSachDanhMuc(View view) {
        Intent intent = new Intent(MainActivity.this,DS_DanhMuc.class);
        startActivity(intent);
    }
}
