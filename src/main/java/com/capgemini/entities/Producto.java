package com.capgemini.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Producto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@NotEmpty(message = "El campo nombre no puede estar vacio")
	@Size(min = 4, max = 100, message = "El nombre entre 4 y 100 caracteres")
	private String nombre;
	
	@NotEmpty(message = "El campo descripcion no puede estar vacio")
	@Size(max = 255, message = "La descripcion no puede exceder de 255 caracteres")
	private String descripcion;
	
	@Min(value = 1, message = "El precio no puede ser negativo")
	private double precio;
	
	@Min(value = 0, message = "El stock no puede ser negativo")
	private long stock;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Presentacion presentacion;

}
