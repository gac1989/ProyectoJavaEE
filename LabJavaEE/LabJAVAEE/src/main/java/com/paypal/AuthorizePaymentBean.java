package com.paypal;

import java.io.IOException;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.beans.ComprarJuego;
import com.beans.JuegoBean;
import com.login.SessionUtils;
import com.model.Juego;
import com.paypal.base.rest.PayPalRESTException;

@ManagedBean(name = "AutorizarPago")
@RequestScoped
public class AuthorizePaymentBean {

	

	public String AutorizarPago() throws IOException {
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("id");
		Juego j = JuegoBean.buscarJuego(Integer.parseInt(id));
		HttpSession session = SessionUtils.getSession();
		String nick = (String)session.getAttribute("username");
		if(j!=null) {
			String preciodescuento="";
			if(j.getPrecio()==0) {
				System.out.println("El juego es gratuito");
				ComprarJuego.comprar(id, nick);
				return "/faces/confirmar.xhtml?faces-redirect=true&precio=" + j.getPrecio() +"&id=" + j.getId();
			}
			if(j.getEvento()!=null && j.getEvento().getActivo()==1) {
				preciodescuento=String.valueOf(j.getPrecio()-(j.getPrecio()*j.getEvento().getDescuento()/100));
			}
			else {
				preciodescuento=String.valueOf(j.getPrecio());
			}
			System.out.println("El nombre del juego es: " + j.getNombre() + " El precio es: " + preciodescuento + " El id es: " + j.getId());
        	OrderDetail orderDetail = new OrderDetail(j.getNombre(), preciodescuento, id);
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
		else{
			System.out.println("No encontro el juego :/");
			return "/faces/error.xhtml";
		}
	}
	
}
