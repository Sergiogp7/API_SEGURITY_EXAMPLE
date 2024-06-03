package com.dwes.security.entities;

import jakarta.persistence.Entity;

@Entity
public class Repositorio extends Recurso{

	private Tipo_Repositorio tipo_repositorio;
	
	private String url;

	public Tipo_Repositorio getTipo_repositorio() {
		return tipo_repositorio;
	}

	public void setTipo_repositorio(Tipo_Repositorio tipo_repositorio) {
		this.tipo_repositorio = tipo_repositorio;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
