package com.hd.permissiondetect.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hd.permissiondetect.R;
import com.hd.permissiondetect.databinding.ActivityLogBinding;
import com.hd.permissiondetect.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class LogActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListAdapter adapter;

    private int type = 0;
    private File hdLogFolderFile;
    private final ArrayList<File> arrayList = new ArrayList<>();

    private ActivityLogBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLogBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        hdLogFolderFile = new File(getExternalCacheDir().getAbsolutePath() + File.separator + "HdLog");
        if (!hdLogFolderFile.exists()) {
            hdLogFolderFile.mkdirs();
        }
        ListView listView = findViewById(R.id.listView);


        File[] files;
        files = hdLogFolderFile.listFiles();
        // 过滤0kb文件
        if (files != null && files.length > 0) {
            for (File file : files) {
                // 文件默认占用60字节
                if (file.length() > 60) {
                    arrayList.add(file);
                }
            }
        }
        adapter = new ListAdapter(this, arrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        initClickListener();
    }

    private void initClickListener() {
        mBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnBack(v);
            }
        });
        mBinding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnClear(v);
            }
        });
    }

    public void onClickBtnBack(View view) {
        finish();
    }

    public void onClickBtnClear(View view) {
        File[] files = hdLogFolderFile.listFiles();
        if (files != null) {
            for (File file : files) {
                FileUtils.deleteFileByNio(file);
            }
        }
        adapter.clear();
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file = adapter.getItem(position);
        Intent intent = new Intent(this, LogDetailActivity.class);
        intent.putExtra("filePath", file.getAbsolutePath());
        startActivity(intent);
    }

    private static class ListAdapter extends BaseAdapter {

        private final List<File> mList;
        private final Context context;

        public ListAdapter(Context context, List<File> list) {
            this.context = context;
            this.mList = list;
        }

        public void clear() {
            this.mList.clear();
            notifyDataSetChanged();
        }

        public void update(List<File> list) {
            list.clear();
            this.mList.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public File getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_log, null);
            TextView fileName = view.findViewById(R.id.fileName);
            File file = getItem(position);
            fileName.setText(file.getName());
            return view;
        }
    }
}
