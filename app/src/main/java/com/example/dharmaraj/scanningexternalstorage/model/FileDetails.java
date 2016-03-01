package com.example.dharmaraj.scanningexternalstorage.model;

/**
 * Created by dharmaraj on 3/1/16.
 */
public class FileDetails {
    String fileName;
    String fileSize;
    String fileExtension;
    String extensionFrequencies;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getExtensionFrequencies() {
        return extensionFrequencies;
    }

    public void setExtensionFrequencies(String extensionFrequencies) {
        this.extensionFrequencies = extensionFrequencies;
    }
}
