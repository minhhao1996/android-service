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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.util.ArrayList;

import model.DanhMuc;
import model.SanPham;

import static java.lang.Integer.parseInt;

public class ThemSanPham extends AppCompatActivity {
    private EditText editThemMa,editThemTen,editThemDonGia;
    private Spinner spinChonDM;
    private Button btnLuuSp,btnBack;
    private TextView txtThongBao;
    ArrayAdapter<DanhMuc> DanhMucAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san_pham);
        addContronls();
        addEvents();

    }
    private void  addEvents(){
        btnLuuSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuLyLuuSp();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void XuLyLuuSp(){
        SanPham sp = new SanPham();
        sp.setMa(editThemMa.getText().toString());
        sp.setTen(editThemTen.getText().toString());
        sp.setDonGia(Integer.parseInt(editThemDonGia.getText().toString()));

        DanhMuc dm = (DanhMuc) spinChonDM.getSelectedItem();
        sp.setMaDM(dm.getMaDm());

        LuuSanPhamTask task = new LuuSanPhamTask();
        task.execute(sp);

    }
    private void addContronls(){
        editThemDonGia= (EditText) findViewById(R.id.editThemDonGia);
        editThemMa= (EditText) findViewById(R.id.editThemMaSp);
        editThemTen= (EditText) findViewById(R.id.editThemTenSp);
        txtThongBao = (TextView) findViewById(R.id.txtThongbao);
        spinChonDM = (Spinner) findViewById(R.id.SpinChonDm);
        btnLuuSp = (Button) findViewById(R.id.btnThemSp);
        btnBack = (Button) findViewById(R.id.btnBack);
        DanhMucAdapter = new ArrayAdapter<>(ThemSanPham.this,android.R.layout.simple_spinner_item);
        DanhMucAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinChonDM.setAdapter(DanhMucAdapter);

        DanhSachDanhMucTask task = new DanhSachDanhMucTask();
        task.execute();


    }
    class DanhSachDanhMucTask extends AsyncTask<Void,Void,ArrayList<DanhMuc>> {
        @Override
        protected ArrayList<DanhMuc> doInBackground(Void... voids) {
           ArrayList<DanhMuc> dsdm = new ArrayList<>();
            try {
                URL url = new URL("http://leminhhao.somee.com/api/danhmuc");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type","application/json;charset =UTF-8");
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(),"UTF-8");
                BufferedReader br = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line= br.readLine())!=null){
                    builder.append(line);
                }
                JSONArray jsonArray = new JSONArray(builder.toString());
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String madm = jsonObject.getString("MaDanhMuc");
                    String tendm = jsonObject.getString("TenDanhMuc");
                    DanhMuc dm = new DanhMuc();
                    dm.setMaDm(madm);
                    dm.setTenDm(tendm);
                    dsdm.add(dm);
                }
                br.close();

            }catch (Exception ex){
                Log.e("Loi",ex.toString());
            }
            return dsdm;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<DanhMuc> danhMucs) {
            DanhMucAdapter.clear();
            DanhMucAdapter.addAll(danhMucs);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    class LuuSanPhamTask extends AsyncTask<SanPham,Void,Boolean> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean.booleanValue()==true){
                txtThongBao.setText("Lưu Sản phẩm Thành Công ");
                Intent intent = new Intent(ThemSanPham.this,DS_SanPham.class);
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
        protected Boolean doInBackground(SanPham... sanPhams) {
            try {
                SanPham sp = sanPhams[0];
                String Params ="?masanpham="+sp.getMa()+
                        "&ten="+ URLEncoder.encode(sp.getTen())+
                        "&dongia="+sp.getDonGia()+
                        "&madanhmuc="+sp.getMaDM();
                URL url = new URL("http://leminhhao.somee.com/api/sanpham"+Params);
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
