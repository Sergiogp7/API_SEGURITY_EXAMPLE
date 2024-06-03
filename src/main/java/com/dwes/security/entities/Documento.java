package com.dwes.security.entities;

import jakarta.persistence.Entity;

@Entity
public class Documento extends Recurso {
	private Tipo_Documento tipo_documento;
	private String doc_valor;
	private String hash;
	
	public Tipo_Documento getTipo_documento() {
		return tipo_documento;
	}
	public void setTipo_documento(Tipo_Documento tipo_documento) {
		this.tipo_documento = tipo_documento;
	}
	public String getDoc_valor() {
		return doc_valor;
	}
	public void setDoc_valor(String doc_valor) {
		this.doc_valor = doc_valor;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	
	
	
}
