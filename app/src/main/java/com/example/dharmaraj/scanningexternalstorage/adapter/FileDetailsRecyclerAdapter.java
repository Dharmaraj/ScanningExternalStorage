package com.example.dharmaraj.scanningexternalstorage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dharmaraj.scanningexternalstorage.R;
import com.example.dharmaraj.scanningexternalstorage.model.FileDetails;

import java.util.List;

/**
 * Created by dharmaraj on 3/1/16.
 */
public class FileDetailsRecyclerAdapter extends RecyclerView.Adapter<FileDetailsRecyclerAdapter.CustomViewHolder> {
    private List<FileDetails> fileDetailsList;
    private Context context;

    public FileDetailsRecyclerAdapter(Context context, List<FileDetails> fileDetailsList) {
        this.fileDetailsList = fileDetailsList;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.file_details_row_item, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        FileDetails fileDetails = fileDetailsList.get(i);
        String fileName = fileDetails.getFileName();
        String fileSize = fileDetails.getFileSize();
        String extension = fileDetails.getFileExtension();
        String frequency = fileDetails.getExtensionFrequencies();

        if (fileName != null && fileName.length() > 0) {
            customViewHolder.fileNameTV.setText(fileName);
        } else {
            customViewHolder.fileNameTV.setVisibility(View.GONE);
        }
        if (fileSize != null && fileSize.length() > 0) {
            customViewHolder.fileSizeTV.setText(fileSize);
        } else {
            customViewHolder.fileSizeTV.setVisibility(View.GONE);
        }
        if (extension != null && extension.length() > 0) {
            customViewHolder.extensionNameTV.setText(extension);
        } else {
            customViewHolder.extensionNameTV.setVisibility(View.GONE);
        }
        if (frequency != null && frequency.length() > 0) {
            customViewHolder.frequencyTV.setText(frequency);
        } else {
            customViewHolder.frequencyTV.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (fileDetailsList != null ? fileDetailsList.size() : 0);
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView fileNameTV;
        protected TextView fileSizeTV;
        protected TextView extensionNameTV;
        protected TextView frequencyTV;


        public CustomViewHolder(View view) {
            super(view);
            this.fileNameTV = (TextView) view.findViewById(R.id.fileNameTV);
            this.fileSizeTV = (TextView) view.findViewById(R.id.fileSizeTV);
            this.extensionNameTV = (TextView) view.findViewById(R.id.extensionNameTV);
            this.frequencyTV = (TextView) view.findViewById(R.id.frequencyTV);

        }
    }
}