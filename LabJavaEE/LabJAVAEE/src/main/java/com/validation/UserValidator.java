package com.validation;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.utils.ClientControl;


@FacesValidator("userValidator")
public class UserValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
        String username = (String) value;
        if (usernameExists(username)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","El nick ingresado ya ha sido registrado"));
        }
    }

    private boolean usernameExists(String username) {
    	String urlRestService2 = "http://localhost:8080/rest-lab/api/recursos/ckusername/" + username;
    	return (new ClientControl().realizarPeticion(urlRestService2, "GET", null).readEntity(String.class).equals("true"));
    }
}