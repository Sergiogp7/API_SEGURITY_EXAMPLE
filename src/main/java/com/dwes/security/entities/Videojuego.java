package com.dwes.security.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "videojuegos")
public class Videojuego {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @NotBlank(message = "Se debe de poner un nombre")
	    private String nombre;

	    @NotBlank(message = "Se debe de poner un desarrollador")
	    private String desarrollador;

	    private boolean esOnline;

	    @ManyToOne
	    @JoinColumn(name = "admin_id")
	    private Usuario creador;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getDesarrollador() {
			return desarrollador;
		}

		public void setDesarrollador(String desarrollador) {
			this.desarrollador = desarrollador;
		}

		public boolean isEsOnline() {
			return esOnline;
		}

		public void setEsOnline(boolean esOnline) {
			this.esOnline = esOnline;
		}

		public Usuario getCreador() {
			return creador;
		}

		public void setCreador(Usuario creador) {
			this.creador = creador;
		}
}
