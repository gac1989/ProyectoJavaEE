package com.paypal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.beans.ComprarJuego;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.login.SessionUtils;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

@ManagedBean(name = "ExecutePayment")

@RequestScoped
public class ExecutePaymentBean {
	
	
	private StreamedContent fileDownload = null;

	public StreamedContent getFileDownload() {
		return fileDownload;
	}

	public void setFileDownload(StreamedContent fileDownload) {
		this.fileDownload = fileDownload;
	}
	
	public StreamedContent createPDF() {
    try {       
        Document pdf = new Document(PageSize.LETTER);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer;
        writer = PdfWriter.getInstance(pdf, baos);
        if (!pdf.isOpen()) {
            pdf.open();
        }

       //Adding content to pdf
        System.out.println("opening the document..");
        
        pdf.close();
		String fileName = "factura.pdf" ;
		InputStream stream = new ByteArrayInputStream(baos.toByteArray());
		StreamedContent file = DefaultStreamedContent.builder().contentType("application/pdf").name(fileName).stream(() -> stream).build();
		return file;
		} catch (Exception e) {
		    System.out.println("Error: createPDF() " + e.getMessage());
		    return null;
	    }
	}
	
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
    		this.fileDownload=InvoiceGenerator.createPDF(payerInfo,transaction);
    		sessionMap.put("factura", fileDownload);
    		sessionMap.put("transaction", transaction);
    		sessionMap.put("id", transaction.getCustom());
    		return "/faces/confirmar.xhtml?faces-redirect=true";
        } catch (PayPalRESTException ex) {
            ex.printStackTrace();
            return "ERROR";
        }
	}
}
