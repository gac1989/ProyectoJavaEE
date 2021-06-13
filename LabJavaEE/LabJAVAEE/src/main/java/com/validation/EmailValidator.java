package com.validation;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.beans.ClientControl;

@FacesValidator("emailValidator")
public class EmailValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
        String email = (String) value;
        if (usernameExists(email)) {
        	System.out.println("HA SUCEDIDO UNA TRAGEDIA");
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","El email ingresado ya ha sido registrado"));
        }
    }

    private boolean usernameExists(String email) {
    	String urlRestService2 = "http://localhost:8080/rest-lab/api/ejemplo/ckemail/" + email;
    	return (new ClientControl().realizarPeticion(urlRestService2, "GET", null).readEntity(String.class).equals("true"));
    }
}