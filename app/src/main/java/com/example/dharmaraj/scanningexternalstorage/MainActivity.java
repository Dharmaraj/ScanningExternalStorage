package com.example.dharmaraj.scanningexternalstorage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dharmaraj.scanningexternalstorage.adapter.FileDetailsRecyclerAdapter;
import com.example.dharmaraj.scanningexternalstorage.model.FileDetails;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by dharmaraj on 2/29/16.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView fileDetailsRecyclerView;
    private Button startBtn, stopBtn, shareBtn;
    private TextView fileAverageSizeTV;
    private File root;
    private ArrayList<File> fileList;
    private Map<String, Long> biggestFileMap;
    private Map<String, Long> extensionFrequencyMap;
    private static final int EXTERNAL_STORAGE_REQUEST_CODE = 111;
    private double fileAvgSize = 0;
    private ScanExternalCardAsync scanExternalCardAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize list and maps
        fileList = new ArrayList<>();
        biggestFileMap = new HashMap<>();
        extensionFrequencyMap = new HashMap<>();

        fileDetailsRecyclerView = (RecyclerView) findViewById(R.id.fileDetailsRecyclerView);
        fileDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        startBtn = (Button) findViewById(R.id.startBtn);
        stopBtn = (Button) findViewById(R.id.stopBtn);
        shareBtn = (Button) findViewById(R.id.shareBtn);
        fileAverageSizeTV = (TextView) findViewById(R.id.fileAverageSizeTV);

        //Register click event listener
        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);

        stopBtn.setEnabled(false);
        shareBtn.setEnabled(false);

        // Prompt Android 6 user if permission is not yet granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST_CODE);
        }

        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Read External Storage Not Granted", Toast.LENGTH_LONG).show();
                    finish();
                }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startBtn:
                scanExternalCardAsync = new ScanExternalCardAsync();
                scanExternalCardAsync.execute();
                break;

            case R.id.stopBtn:
                scanExternalCardAsync.cancel(true);
                break;

            case R.id.shareBtn:

                break;
        }

    }

    private class ScanExternalCardAsync extends AsyncTask<Void, Float, List<FileDetails>> {
        private ProgressDialog progressDialog;

        private ScanExternalCardAsync() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Scanning External Storage Device");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgress(0);
            progressDialog.setMax(100);
        }

        @Override
        protected List<FileDetails> doInBackground(Void... params) {
            getAllFilesFromExternalStorage(root);
            List<FileDetails> fileDetailsList = new ArrayList<>();

            Map<String, Long> sortedBiggestFileMap = sortMapByValue(biggestFileMap);
            Map<String, Long> sortedBiggestExtensionMap = sortMapByValue(extensionFrequencyMap);
            Iterator<Map.Entry<String, Long>> biggestFileIterator = sortedBiggestFileMap.entrySet().iterator();
            Iterator<Map.Entry<String, Long>> extensionIterator = sortedBiggestExtensionMap.entrySet().iterator();

            for (int i = 0; i < 10; i++) {
                String fileName = "";
                String fileSize = "";
                String extension = "";
                String frequency = "";
                try {
                    Map.Entry<String, Long> biggestMap = biggestFileIterator.next();
                    fileName = biggestMap.getKey();
                    fileSize = humanReadableByteCount(biggestMap.getValue(), true);
                } catch (Exception e) {
                }
                if (i < 5) {
                    try {
                        Map.Entry<String, Long> extensionMap = extensionIterator.next();
                        extension = extensionMap.getKey();
                        frequency = String.valueOf(extensionMap.getValue());
                    } catch (Exception e) {
                    }
                }
                FileDetails fileDetails = new FileDetails(fileName, fileSize, extension, frequency);
                fileDetailsList.add(fileDetails);
            }

            return fileDetailsList;
        }

        @Override
        protected void onPreExecute() {
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(List<FileDetails> fileDetailses) {
            FileDetailsRecyclerAdapter fileDetailsRecyclerAdapter =
                    new FileDetailsRecyclerAdapter(fileDetailses);
            fileDetailsRecyclerView.setAdapter(fileDetailsRecyclerAdapter);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            fileAverageSizeTV.setText("Average File Size=" + humanReadableByteCount((long) (fileAvgSize / fileList.size()), true));
        }

        protected void onProgressUpdate(int progress) {
            progressDialog.setProgress(progress);
        }

        @Override
        protected void onCancelled(List<FileDetails> fileDetailsList) {
            super.onCancelled(fileDetailsList);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " Bytes";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private static Map<String, Long> sortMapByValue(Map<String, Long> unSortMap) {

        // Convert Map to List
        List<Map.Entry<String, Long>> list = new LinkedList<>(unSortMap.entrySet());

        // Descending Sort list with comparator, to compare the Map values.
        Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
            public int compare(Map.Entry<String, Long> o1,
                               Map.Entry<String, Long> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Convert sorted list to a Map.
        Map<String, Long> sortedMap = new LinkedHashMap<>();
        for (Iterator<Map.Entry<String, Long>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Long> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public ArrayList<File> getAllFilesFromExternalStorage(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    getAllFilesFromExternalStorage(listFile[i]);
                } else {
                    biggestFileMap.put(listFile[i].getName(), listFile[i].length());
                    String extension = MimeTypeMap.getFileExtensionFromUrl(listFile[i].toURI().toString());
                    if (extensionFrequencyMap.containsKey(extension)) {
                        extensionFrequencyMap.put(extension, extensionFrequencyMap.get(extension) + 1);
                    } else {
                        extensionFrequencyMap.put(extension, 1L);
                    }
                    fileAvgSize += listFile[i].length();
                    fileList.add(listFile[i]);
                    scanExternalCardAsync.onProgressUpdate(i);
                }
            }
        }
        return fileList;
    }


}
