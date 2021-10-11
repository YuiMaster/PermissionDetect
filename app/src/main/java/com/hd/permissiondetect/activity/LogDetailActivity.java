package com.hd.permissiondetect.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hd.permissiondetect.databinding.ActivityDetailBinding;
import com.hd.permissiondetect.util.FileUtils;
import com.hd.permissiondetect.util.ShareUtil;

import java.io.File;

public class LogDetailActivity extends AppCompatActivity {
    private ActivityDetailBinding mBinding;
    // log错误路径
    private String logFilePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initData();
        initView();
    }

    private void initData() {
        logFilePath = getIntent().getStringExtra("filePath");
    }

    private void initView() {
        mBinding.fileContent.setText(FileUtils.getTrackLogDetail(logFilePath));
        mBinding.btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickExportLogBtn(v);
            }
        });
    }

    public void onClickExportLogBtn(View view) {
        ShareUtil.shareFile(this, new File(logFilePath), "导出log错误");
    }
}
