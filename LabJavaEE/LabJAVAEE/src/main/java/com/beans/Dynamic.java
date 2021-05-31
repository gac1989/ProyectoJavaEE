package com.beans;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;

@ManagedBean(name = "Dynamic")
@URLMappings(mappings = {
	    @URLMapping(id = "dynamic",
	    pattern = "/dynamic/myAction", // URL mapped to jsf file
	    viewId = "/faces/ventas.xhtml"),    // jsf file
	    })    // jsf file
@RequestScoped
public class Dynamic {

	

	@URLAction(mappingId = "myAction") 
	public void reviewPayment() {
		System.out.println("LLEGUE POR ACA VAMOOOO " + " El que pago es: ");
	}
	
}
