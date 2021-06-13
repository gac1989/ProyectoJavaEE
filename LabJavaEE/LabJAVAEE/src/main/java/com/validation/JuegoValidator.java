package com.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.beans.ClientControl;

@FacesValidator("juegoValidator")
public class JuegoValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
        String juego = (String) value;
        if (usernameExists(juego)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","El nombre del juego ingresado ya ha sido registrado"));
        }
    }

    private boolean usernameExists(String juego) {
    	String urlRestService2 = "http://localhost:8080/rest-lab/api/ejemplo/ckjuego/" + juego;
    	return (new ClientControl().realizarPeticion(urlRestService2, "GET", null).readEntity(String.class).equals("true"));
    }
}