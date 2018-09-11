package com.example.mypc.service;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import model.DanhMuc;
import model.SanPham;

public class HieuChinhDanhMuc extends AppCompatActivity {
    private EditText editMaDm,editTenDm;
    private TextView txtThongBao;
    private Button btnHieuChinh,btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hieu_chinh_danh_muc);
        addContronls();
        addEvendls();
    }
    private void addEvendls(){
        btnHieuChinh.setOnClickListener(new View.OnClickListener() {
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

    private void XuLyHieuChinh() {
        DanhMuc sp= new DanhMuc();
        sp.setMaDm(editMaDm.getText().toString());
        sp.setTenDm(editTenDm.getText().toString());

        SuaDanhMucTask task = new SuaDanhMucTask();
        task.execute(sp);
    }

    private void addContronls(){
        editMaDm = (EditText) findViewById(R.id.editSuaMaDm);
        editTenDm = (EditText) findViewById(R.id.editSuaTenDm);
        txtThongBao = (TextView) findViewById(R.id.txtThongbao);
        btnHieuChinh = (Button) findViewById(R.id.btnEditDm);
        btnBack = (Button) findViewById(R.id.btnBack);
        Intent intent = getIntent();
        DanhMuc dm =(DanhMuc) intent.getSerializableExtra("DANHMUC");
        editTenDm.setText(dm.getTenDm()+"");
        editMaDm.setEnabled(false);
        editMaDm.setText(dm.getMaDm());

    }
    class SuaDanhMucTask extends AsyncTask<DanhMuc,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean.booleanValue()== true){
                txtThongBao.setText("Hiệu Chỉnh Thành Công");
                Intent intent = new Intent(HieuChinhDanhMuc.this,DS_DanhMuc.class);
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
        protected Boolean doInBackground(DanhMuc... danhMucs) {
            try{
                DanhMuc dm = danhMucs[0];
                String pramas = "?madanhmuc="+dm.getMaDm()+"&tendanhmuc="+dm.getTenDm();
                URL url = new URL("http://leminhhao.somee.com/api/danhmuc"+pramas);
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
