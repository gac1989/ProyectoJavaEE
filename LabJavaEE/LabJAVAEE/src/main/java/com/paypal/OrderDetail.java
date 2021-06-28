package com.paypal;

public class OrderDetail {
    private String productName;
    private float total;
    private String id;
 
    public OrderDetail(String productName, String total, String id) {
        this.productName = productName;
        this.total = Float.parseFloat(total);
        this.id=id;
    }
 
    
    public String getId() {
		return id;
	}

	public String getProductName() {
        return productName;
    }
 
    public String getSubtotal() {
    	System.out.println("El subtotal es: " + String.format("%.3f", total).replace(",", "."));
        return String.format("%.2f", total).replace(",", ".");
    }
 
    public String getShipping() {
    	System.out.println("El envio es: " + String.format("%.3f",  Float.parseFloat("0")).replace(",", "."));
        return String.format("%.2f",  Float.parseFloat("0")).replace(",", ".");
    }
 
    public String getTax() {
    	System.out.println("El impuesto es: " + String.format("%.3f",  Float.parseFloat("0")).replace(",", "."));
        return String.format("%.2f",  Float.parseFloat("0")).replace(",", ".");
    }
     
    public String getTotal() {
    	System.out.println("El total es: " + String.format("%.3f", total).replace(",", "."));
        return String.format("%.2f", total).replace(",", ".");
    }
}
