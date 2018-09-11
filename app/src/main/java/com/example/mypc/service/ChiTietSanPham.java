package com.example.mypc.service;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import model.SanPham;

public class ChiTietSanPham extends AppCompatActivity {
    private EditText TimSp, TenSp, MaSp, DonGia;
    private Button btnSearch, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_san_pham);
        addContronls();
        addEvents();

    }

    private void addEvents() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuLyChiTiet();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void XuLyChiTiet() {
            ChiTietSPTask task = new ChiTietSPTask();
            task.execute(TimSp.getText().toString());
    }

    public void addContronls() {
        TimSp = (EditText) findViewById(R.id.editSearch);
        TenSp = (EditText) findViewById(R.id.editName);
        MaSp = (EditText) findViewById(R.id.editMa);
        DonGia = (EditText) findViewById(R.id.editDonGia);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    class ChiTietSPTask extends AsyncTask<String, Void, SanPham> {
        @Override
        protected SanPham doInBackground(String... strings) {
            try {
                URL url = new URL("http://leminhhao.somee.com/api/sanpham/"+strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json;charset =UTF-8");

                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                JSONObject jsonObject = new JSONObject(builder.toString());
                String masp = jsonObject.getString("Ma");
                String tensp = jsonObject.getString("Ten");
                int dongia = jsonObject.getInt("DonGia");
                SanPham sp = new SanPham();
                sp.setMa(masp);
                sp.setTen(tensp);
                sp.setDonGia(dongia);
                return sp;
            }catch (Exception ex){
                Log.e("LOI",ex.toString());
            }return  null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(SanPham sanPham) {
            super.onPostExecute(sanPham);
            if(sanPham != null){
                MaSp.setText(sanPham.getMa()+"");
                TenSp.setText(sanPham.getTen()+"");
                DonGia.setText(sanPham.getDonGia()+"");
            }
            else {
                Toast.makeText(ChiTietSanPham.this, "Không tìm thấy sản phẩm ", Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

}