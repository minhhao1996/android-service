package com.example.mypc.service;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import model.DanhMuc;
import model.SanPham;

public class ThemDanhMuc extends AppCompatActivity {
    private EditText editMaDm,editTenDm,editDonGia;
    private Button btnLuuDm,btnBack;
    private TextView txtThongBao;
    ArrayAdapter<DanhMuc> DanhMucAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_danh_muc);
        addContronls();
        addEvents();
    }

    private void addEvents() {
        btnLuuDm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuLyLuuDm();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void XuLyLuuDm(){
        DanhMuc sp = new DanhMuc();
        sp.setTenDm(editTenDm.getText().toString());
        sp.setMaDm(editMaDm.getText().toString());
        ThemDanhMucTask task = new ThemDanhMucTask();
        task.execute(sp);

    }
    private void addContronls() {
        editMaDm= (EditText) findViewById(R.id.editThemMaDm);
        editTenDm= (EditText) findViewById(R.id.editThemTenDm);
        editDonGia= (EditText) findViewById(R.id.editDonGia);
        btnLuuDm = (Button) findViewById(R.id.btnThemDanhMuc);
        btnBack = (Button) findViewById(R.id.btnBack);
        txtThongBao = (TextView) findViewById(R.id.txtThongbao);
    }
    class ThemDanhMucTask extends AsyncTask<DanhMuc,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean.booleanValue()==true){
                txtThongBao.setText("Lưu Sản phẩm Thành Công ");
                Intent intent = new Intent(ThemDanhMuc.this,DS_DanhMuc.class);
                startActivity(intent);
            }else {
                txtThongBao.setText("Lưu Sản Phẩm Thất Bại !!");
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(DanhMuc... danhMucs) {
            try {
                DanhMuc sp = danhMucs[0];
                String Params ="?madanhmuc="+sp.getMaDm()+
                        "&tendanhmuc="+ URLEncoder.encode(sp.getTenDm());
                URL url = new URL("http://leminhhao.somee.com/api/danhmuc"+Params);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json;charset =UTF-8");

                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                boolean kq = builder.toString().contains("true");
                return kq;
            }catch (Exception ex){
                Log.e("LOI",ex.toString());
            }return  false;
        }
    }
}
