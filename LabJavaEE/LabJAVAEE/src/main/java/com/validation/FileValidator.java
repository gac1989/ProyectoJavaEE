package com.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.model.file.UploadedFiles;


@FacesValidator("fileValidator")
public class FileValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
    	UploadedFiles files =(UploadedFiles) value;
    	if (files==null || files.getFiles()==null || files.getFiles().get(0).getFileName()==null){
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","Ingrese un archivo"));
        }
    }

   
}