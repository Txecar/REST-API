package com.capgemini.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.capgemini.entities.Producto;

@Repository
public interface IProductoDao extends JpaRepository<Producto, Long>{
	
	// Crear consultas personalizadas, para mahejar registros con paginacion
	// y ordenamiento
	@Query(value = "select p from Producto p left join fetch p.presentacion")
	public List<Producto> findAll(Sort sort);
	
	// Para recuperar los registros, pero no todos, sino de 10 en 10, de 20 en 20, etc.,
	// es decir, paginados
	@Query (value = "select p from Producto p left join fetch p.presentacion",
			countQuery = "select count(p) from Producto p left join p.presentacion")
	public Page<Producto> findAll(Pageable pageable);
	
	@Query(value = "select p from Producto p left join fetch p.presentacion where p.id = :id")
	public Producto findById(long id);
}
