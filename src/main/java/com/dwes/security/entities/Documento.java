package com.dwes.security.entities;

import jakarta.persistence.Entity;

@Entity
public class Documento extends Recurso {
	private Tipo_Documento tipo_documento;
	private String doc_valor;
	private String hash;
}
