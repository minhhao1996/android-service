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

import model.DanhMuc;



public class DS_DanhMuc extends AppCompatActivity {
    private ListView LvDanhMuc;
    private Button btnHieuChinh,btnThoat;
    private ArrayAdapter<DanhMuc> DanhMucAdapter;
    int vitri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds__danh_muc);
        addcontronls();
        adđEvents();

    }
    private void adđEvents(){
       LvDanhMuc.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               DanhMuc dm =  DanhMucAdapter.getItem(position);
               XuLyXoa(dm);
               return false;
           }
       });

        LvDanhMuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vitri = position;
            }
        });
        btnHieuChinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DanhMuc dm = DanhMucAdapter.getItem(vitri);
                XuLySua(dm);
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DanhSachDanhMucTask task = new DanhSachDanhMucTask();
                task.execute();
            }
        });

    }

    private void XuLyXoa(final DanhMuc dm) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("XÁC NHẬN XÓA");
        builder.setMessage("Bạn có muốn xóa sản phẩm : "+dm.getTenDm()+ "?");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                XoaDanhMucTask task = new XoaDanhMucTask();
                task.execute(dm);
                Toast.makeText(DS_DanhMuc.this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
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
    private void XuLySua(DanhMuc dm){
        Intent intent = new Intent(DS_DanhMuc.this,HieuChinhDanhMuc.class);
       intent.putExtra("DANHMUC",dm);
        startActivity(intent);
    }


    private void addcontronls(){
        btnThoat= (Button) findViewById(R.id.btnThoatDm);
        btnHieuChinh = (Button) findViewById(R.id.btnHieuChinhDM);
        LvDanhMuc = (ListView) findViewById(R.id.LvDsDm);
        DanhMucAdapter = new ArrayAdapter<DanhMuc>(DS_DanhMuc.this,android.R.layout.simple_list_item_1);
        LvDanhMuc.setAdapter(DanhMucAdapter);



        DanhSachDanhMucTask task = new DanhSachDanhMucTask();
        task.execute();
    }



    public void MoManHinhThemDanhMuc(View view) {
        Intent intent = new Intent(DS_DanhMuc.this,ThemDanhMuc.class);
        startActivity(intent);
    }

    public void ManHinhChiTietSanPham(View view) {
        Intent intent = new Intent(DS_DanhMuc.this,ThemDanhMuc.class);
        startActivity(intent);
    }



    class DanhSachDanhMucTask extends AsyncTask<Void,Void,ArrayList<DanhMuc>> {
        @Override
        protected ArrayList<DanhMuc> doInBackground(Void... voids) {
            ArrayList<DanhMuc>dsdm = new ArrayList<>();
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
    class XoaDanhMucTask extends  AsyncTask<DanhMuc,Void,Boolean>{
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean.booleanValue()==true){
                DanhSachDanhMucTask task = new DanhSachDanhMucTask();
                task.execute();
            }else {
                Toast.makeText(DS_DanhMuc.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(DanhMuc... danhMucs) {
            try{
            DanhMuc sp = danhMucs[0];
            String Params ="?madanhmuc="+sp.getMaDm();
            URL url = new URL("http://leminhhao.somee.com/api/danhmuc"+Params);

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
        }
        return false;
        }

    }
   /* class TimDanhMucTask extends  AsyncTask<DanhMuc,Void,ArrayList<DanhMuc>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<DanhMuc> danhMucs) {
            super.onPostExecute(danhMucs);
            DanhMucAdapter.clear();
            DanhMucAdapter.addAll(danhMucs);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<DanhMuc> doInBackground(DanhMuc... danhMucs) {
            ArrayList<DanhMuc> dsdm = new ArrayList<>();
            try {

                String pramas = "?madanhmuc="+editTimDm.toString();
                URL url = new URL("http://leminhhao.somee.com/api/danhmuc" + pramas);
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
                JSONArray jsonArray = new JSONArray(builder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String madm = jsonObject.getString("MaDanhMuc");
                    String tendm = jsonObject.getString("TenDanhMuc");
                    DanhMuc dm = new DanhMuc();
                    dm.setMaDm(madm);
                    dm.setTenDm(tendm);
                    dsdm.add(dm);
                }
                br.close();

            } catch (Exception ex) {
                Log.e("Loi", ex.toString());
            }
            return dsdm;
        }
    }*/
}
