package com.paypal;

import java.io.IOException;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.beans.ComprarJuego;
import com.login.SessionUtils;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

@ManagedBean(name = "ExecutePayment")

@RequestScoped
public class ExecutePaymentBean {
	
	public String executePayment() throws IOException {
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String payerId = params.get("PayerID");
		String paymentId = params.get("paymentId");
		String id = params.get("id");
        try {
            PaymentServices paymentServices = new PaymentServices();
            Payment payment = paymentServices.executePayment(paymentId, payerId);
             
            PayerInfo payerInfo = payment.getPayer().getPayerInfo();
            Transaction transaction = payment.getTransactions().get(0);
            HttpSession ses = SessionUtils.getSession();
            String nick = (String)ses.getAttribute("username");
            System.out.println("El nick es: " + nick);
            ComprarJuego.comprar(id, nick);
            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    		sessionMap.put("payer",payerInfo);
    		sessionMap.put("transaction", transaction);
    		sessionMap.put("id", transaction.getCustom());
    		return "/faces/index.xhtml?faces-redirect=true";
        } catch (PayPalRESTException ex) {
            ex.printStackTrace();
            return "ERROR";
        }
	}
}
