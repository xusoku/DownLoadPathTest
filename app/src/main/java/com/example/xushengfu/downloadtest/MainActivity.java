package com.example.xushengfu.downloadtest;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    StringBuffer buffer;
    File innerDir;
    File root;
    File[] list;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.text);
        innerDir = Environment.getExternalStorageDirectory();
        root = innerDir.getParentFile();
        list = root.listFiles();
        buffer = new StringBuffer();
        buffer.append("data目录：" + Environment.getDataDirectory());
        buffer.append("\n");
        buffer.append("下载缓存目录：" + Environment.getDownloadCacheDirectory());
        buffer.append("\n");
        buffer.append("外部存储媒体目录：" + Environment.getExternalStorageDirectory());
        buffer.append("\n");
        buffer.append("android根目录：" + Environment.getRootDirectory());
        buffer.append("\n");
        if(list!=null&&list.length>0)
        for (File file : list) {
            if (file.compareTo(innerDir) != 0&&file.canWrite()) {
                buffer.append("外接SD卡目录：" + file);
                buffer.append("\n");
            }
        }
        tv.setText(buffer);

        tv.append("目录：");
        tv.append(getSDCardPath());

    }

    /**
     * 获取sd卡路径 双sd卡时，获得的是外置sd卡
     *
     * @return
     */
    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        BufferedInputStream in = null;
        BufferedReader inBr = null;
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            in = new BufferedInputStream(p.getInputStream());
            inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 获得命令执行后在控制台的输出信息
                if (lineStr.contains("sdcard")
                        && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure",
                                "");
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                }
            }
        } catch (Exception e) {
            // return Environment.getExternalStorageDirectory().getPath();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                if (inBr != null) {
                    inBr.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Environment.getExternalStorageDirectory().getPath();
    }
}
