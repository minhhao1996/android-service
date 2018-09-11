package com.example.mypc.service;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import model.SanPham;

public class HieuChinhSanPham extends AppCompatActivity {
    private ArrayAdapter<SanPham>SanPhamAdapter;
    private Button btnEdit,btnBack;
    private EditText editTen,editMa,editdonGia;
    private TextView txtThongBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hieu_chinh_san_pham);
        addContronls();
        addEvents();

    }
    private void addEvents(){
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuLyHieuChinh();


            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
    private void XuLyHieuChinh(){
        SanPham sp= new SanPham();
        editMa.setEnabled(false);
        sp.setMa(editMa.getText().toString());
        sp.setTen(editTen.getText().toString());
        sp.setDonGia(Integer.parseInt(editdonGia.getText().toString()));
        SuaSanPhamTask task = new SuaSanPhamTask();
        task.execute(sp);
    }

    private void addContronls(){
        editTen = (EditText) findViewById(R.id.editSuaTenSp);
        editMa = (EditText) findViewById(R.id.editSuaMaSp);
        editdonGia = (EditText) findViewById(R.id.editSuaDonGia);
        txtThongBao = (TextView) findViewById(R.id.txtThongbao);
        btnEdit = (Button) findViewById(R.id.btnEditSp);
        btnBack = (Button) findViewById(R.id.btnBack);
        Intent intent = getIntent();
        SanPham sp =(SanPham) intent.getSerializableExtra("SANPHAM");
        editTen.setText(sp.getTen()+"");
        editMa.setEnabled(false);
        editMa.setText(sp.getMa());
        editdonGia.setText(sp.getDonGia()+"");

    }
    class  SuaSanPhamTask extends  AsyncTask<SanPham,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean.booleanValue()== true){
                Toast.makeText(HieuChinhSanPham.this, "Hiệu Chỉnh Thành Công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HieuChinhSanPham.this,DS_SanPham.class);
                startActivity(intent);

            }else {
                txtThongBao.setText("Thất Bại!!!");
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(SanPham... sanPhams) {
            try{
                SanPham sp = sanPhams[0];
                String pramas = "?masanpham="+sp.getMa()+"&ten="+sp.getTen()+"&dongia="+sp.getDonGia();
                URL url = new URL("http://leminhhao.somee.com/api/sanpham"+pramas);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type","appliction/json;charset=UTF-8");
                StringBuilder builder = new StringBuilder();
                String line = null;
                InputStreamReader isr = new InputStreamReader(connection.getInputStream(),"UTF-8");
                BufferedReader br = new BufferedReader(isr);
                while ((line= br.readLine())!= null){
                    builder.append(line);
                }
                boolean kq = builder.toString().contains("true");
                return kq;
            }catch (Exception ex){
                Log.e("LOi",ex.toString());
            }
            return false;
        }
    }


}
