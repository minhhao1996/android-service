package com.example.mypc.service;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import model.SanPham;

public class DS_SanPham extends AppCompatActivity {
    private EditText editTimMaDM,editGiaA,editGiaB;
    private Button btnSearch,btnSua,btnThoat,btnTimGia;
    ListView LvDssp;
    ArrayAdapter<SanPham> SanPhamAdapter;
    int vitri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds__san_pham);
        addContronls();
        addEvents();
    }
    private void addEvents(){
        LvDssp.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SanPham sp = SanPhamAdapter.getItem(position);
                 XulyXoa(sp);

                return false;
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SanPham sp = SanPhamAdapter.getItem(vitri);
                XuLySua(sp);
            }
        });
        LvDssp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               vitri = position;
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuLyTimMaDm();
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DanhSachSanPhamTask task = new DanhSachSanPhamTask();
               task.execute();

            }
        });
        btnTimGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XulyTimGiatri();
            }
        });



    }


    private void XuLySua(SanPham sp) {
        Intent intent = new Intent(DS_SanPham.this,HieuChinhSanPham.class);
        intent.putExtra("SANPHAM",sp);
        startActivity(intent);
    }

    private void XulyXoa(final SanPham sp){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("XÁC NHẬN XÓA");
        builder.setMessage("Bạn có muốn xóa sản phẩm : "+sp.getTen()+ "?");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               XoaSanPhamTask task = new XoaSanPhamTask();
               task.execute(sp);
                Toast.makeText(DS_SanPham.this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }
    private void XuLyTimMaDm(){
        DanhSachSanPhamTheoDanhMụcTask task = new DanhSachSanPhamTheoDanhMụcTask();
        task.execute(editTimMaDM.getText().toString());
    }
    private void XulyTimGiatri(){
        DanhSachSanPhamTheoGiaTriTask task = new DanhSachSanPhamTheoGiaTriTask();
        task.execute(editGiaA.getText().toString(),editGiaB.getText().toString());
    }


    private void  addContronls(){
        btnThoat = (Button) findViewById(R.id.btnThoatSp);
        btnSua = (Button) findViewById(R.id.btnSua);
        btnSearch = (Button) findViewById(R.id.btnSearchMaDm);
        editTimMaDM = (EditText) findViewById(R.id.editSearchMaDm);
        btnTimGia = (Button) findViewById(R.id.btnSearchGia);
        editGiaA= (EditText) findViewById(R.id.GiaA);
        editGiaB= (EditText) findViewById(R.id.GiaB);
        LvDssp = (ListView) findViewById(R.id.lvdssp);
        SanPhamAdapter= new ArrayAdapter<SanPham>(DS_SanPham.this,android.R.layout.simple_list_item_1);
        LvDssp.setAdapter(SanPhamAdapter);

        DanhSachSanPhamTask task = new DanhSachSanPhamTask();
        task.execute();

    }
    public void ManHinhThemSanPham(View view) {
        Intent intent = new Intent(DS_SanPham.this,ThemSanPham.class);
        startActivity(intent);
    }




    class  DanhSachSanPhamTask extends AsyncTask<Void,Void,ArrayList<SanPham>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<SanPham> sanPhams) {
            SanPhamAdapter.clear();
            SanPhamAdapter.addAll(sanPhams);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<SanPham> doInBackground(Void... voids) {
            ArrayList<SanPham>dssp = new ArrayList<>();
            try {
                URL url = new URL("http://leminhhao.somee.com/api/sanpham");
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
                    String masp = jsonObject.getString("Ma");
                    String tensp = jsonObject.getString("Ten");
                    int dongia = jsonObject.getInt("DonGia");
                    SanPham sp = new SanPham();
                    sp.setMa(masp);
                    sp.setTen(tensp);
                    sp.setDonGia(dongia);
                    dssp.add(sp);
                }
                br.close();

            }catch (Exception ex){
                Log.e("Loi",ex.toString());
            }
            return dssp;
        }
    }
    class  XoaSanPhamTask extends AsyncTask<SanPham,Void,Boolean>{

        @Override
        protected Boolean doInBackground(SanPham... sanPhams) {
            try {
                SanPham sp = sanPhams[0];
                String Params ="?masanpham="+sp.getMa();
                URL url = new URL("http://leminhhao.somee.com/api/sanpham"+Params);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");
                connection.setRequestProperty("Content-Type", "application/json;charset =UTF-8");

                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(inputStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                boolean kq= builder.toString().contains("true");
                return kq;

            }catch (Exception ex){
                Log.e("Lỗi",ex.toString());
            }return false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean.booleanValue()==true){
                DanhSachSanPhamTask task = new DanhSachSanPhamTask();
                task.execute();
            }else {
                Toast.makeText(DS_SanPham.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    class DanhSachSanPhamTheoDanhMụcTask extends AsyncTask<String,Void,ArrayList<SanPham>> {
        @Override
        protected ArrayList<SanPham> doInBackground(String... strings) {
            ArrayList<SanPham>dssp = new ArrayList<>();
            try {
                URL url = new URL("http://leminhhao.somee.com/api/sanpham/?madm="+strings[0]);
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
                    String masp = jsonObject.getString("Ma");
                    String tensp = jsonObject.getString("Ten");
                    int dongia = jsonObject.getInt("DonGia");
                    SanPham sp = new SanPham();
                    sp.setMa(masp);
                    sp.setTen(tensp);
                    sp.setDonGia(dongia);
                    dssp.add(sp);
                }
                br.close();

            }catch (Exception ex){
                Log.e("Loi",ex.toString());
            }
            return dssp;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<SanPham> sanPhams) {
            SanPhamAdapter.clear();
            SanPhamAdapter.addAll(sanPhams);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    class DanhSachSanPhamTheoGiaTriTask extends  AsyncTask<String,Void,ArrayList<SanPham>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<SanPham> sanPhams) {
            super.onPostExecute(sanPhams);
            SanPhamAdapter.clear();
            SanPhamAdapter.addAll(sanPhams);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<SanPham> doInBackground(String... strings) {

                ArrayList<SanPham>dssp = new ArrayList<>();
                try {
                    URL url = new URL("http://leminhhao.somee.com/api/sanpham/?a="+strings[0]+"&b="+strings[1]);
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
                        String masp = jsonObject.getString("Ma");
                        String tensp = jsonObject.getString("Ten");
                        int dongia = jsonObject.getInt("DonGia");
                        SanPham sp = new SanPham();
                        sp.setMa(masp);
                        sp.setTen(tensp);
                        sp.setDonGia(dongia);
                        dssp.add(sp);
                    }
                    br.close();

                }catch (Exception ex){
                    Log.e("Loi",ex.toString());
                }
                return dssp;
        }
    }
}
