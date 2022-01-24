package com.capgemini.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Presentacion implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotEmpty(message = "El campo nombre no puede estar vacio")
	@Size(min = 4, max = 100, message = "El nombre entre 4 y 100 caracteres")
	private String nombre;
	
	@NotEmpty(message = "El campo descripcion no puede estar vacio")
	@Size(max = 255, message = "La descripcion no puede exceder de 255 caracteres")
	private String descripcion;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, mappedBy = "presentacion")
	private List<Producto> productos;

}
