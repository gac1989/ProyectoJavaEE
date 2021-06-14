package com.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FilesUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;

@ManagedBean(name = "fileUploadView")
@RequestScoped
public class FileUploadView {

    private UploadedFile file;
    private UploadedFiles files;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFiles getFiles() {
        return files;
    }

    public void setFiles(UploadedFiles files) {
        this.files = files;
    }

    public void upload() {
        if (file != null) {
            String message = "Successful " + file.getFileName() + " is uploaded.";
            System.out.println(message);
        }
    }

    public void uploadMultiple() {
        if (files != null) {
            for (UploadedFile f : files.getFiles()) {
                String message = "Successful" + f.getFileName() + " is uploaded.";
                System.out.println(message);
            }
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        String message = "Successful" + event.getFile().getFileName() + " is uploaded.";
        System.out.println(message);    }

    public void handleFilesUpload(FilesUploadEvent event) {
        for (UploadedFile f : event.getFiles().getFiles()) {
            String message = "Successful" + f.getFileName() + " is uploaded.";
            System.out.println(message);        }
    }
}