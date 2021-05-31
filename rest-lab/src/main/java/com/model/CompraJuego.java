package com.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(CompraJuegoId.class)
@Table(name="comprajuego")
public class CompraJuego {
	
	 @Id
	 @ManyToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name = "jugador")
	 private Jugador user;
	 
	 @Id
	 @ManyToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name = "juego_id")
	 private Juego juego;
	 
	 @Column
	 private double precio;

	 @Column
	 private Date fecha;
	 

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Jugador getUser() {
		return user;
	}

	public void setUser(Jugador user) {
		user.agregarJuego(this);
		this.user = user;
	}

	public Juego getJuego() {
		return juego;
	}

	public void setJuego(Juego juego) {
		juego.agregarVenta(this);
		this.juego = juego;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	@Override
	public String toString() {
		return "CompraJuego [user=" + user.getNick() + ", juego=" + juego.getNombre() + ", precio=" + precio + "]";
	}
	 
	 
}
