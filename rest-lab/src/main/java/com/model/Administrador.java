package com.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="administrador")
@PrimaryKeyJoinColumn(name="nick")
public class Administrador extends Usuario {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
