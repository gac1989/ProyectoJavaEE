package com.paypal;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.ocpsoft.pretty.faces.annotation.URLQueryParameter;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

@ManagedBean(name = "ReviewPayment")
@URLMappings(mappings = {
	    @URLMapping(id = "myAction",
	    pattern = "/review/myAction", // URL mapped to jsf file
	    viewId = "/faces/prueba.xhtml"),    // jsf file
	    @URLMapping(id = "myAction2",
	    pattern = "/page/myAction2", // URL mapped to jsf file
	    viewId = "/page.xhtml")})    // jsf file
@RequestScoped
public class ReviewPaymentBean {
	@URLQueryParameter("PayerID")
	private String payerId;
	@URLQueryParameter("paymentId")
	private String paymentId;
	
	
	
	public String getPayerId() {
		return payerId;
	}



	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}



	public String getPaymentId() {
		return paymentId;
	}



	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}



	@URLAction(mappingId = "myAction") 
	public void reviewPayment() {
		System.out.println("LLEGUE POR ACA VAMOOOO " + " El que pago es: " + payerId);
		
         
       try {
            PaymentServices paymentServices = new PaymentServices();
            Payment payment = paymentServices.getPaymentDetails(paymentId);
             
            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
            Transaction transaction = payment.getTransactions().get(0);
            ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    		sessionMap.put("payer",payerInfo);
    		sessionMap.put("transaction", transaction);
    		sessionMap.put("id", transaction.getCustom());
    		sessionMap.put("shippingAddress", shippingAddress);
             
        } catch (PayPalRESTException ex) {
            ex.printStackTrace();
            return;
        }    
	}
	
	
}
