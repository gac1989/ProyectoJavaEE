package com.paypal;

import java.io.IOException;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.paypal.base.rest.PayPalRESTException;

@ManagedBean(name = "AutorizarPago")
@RequestScoped
public class AuthorizePaymentBean {

	

	public String AutorizarPago() throws IOException {
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String product = params.get("product");
		String precio = params.get("precio");
		String id = params.get("id");
        System.out.println("El producto es: " + product + " El precio es: " + precio + " El id es: " + id);
        OrderDetail orderDetail = new OrderDetail(product, precio, id);
        try {
            PaymentServices paymentServices = new PaymentServices();
            String approvalLink = paymentServices.authorizePayment(orderDetail);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(approvalLink);
            return approvalLink;
             
        } catch (PayPalRESTException ex) {
            ex.printStackTrace();
            return "/faces/error.xhtml";
        }
	}
	
}
