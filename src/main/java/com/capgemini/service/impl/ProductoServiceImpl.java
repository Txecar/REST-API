package com.capgemini.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.capgemini.dao.IPresentacionDao;
import com.capgemini.dao.IProductoDao;
import com.capgemini.entities.Presentacion;
import com.capgemini.entities.Producto;
import com.capgemini.service.IProductoService;

@Service
public class ProductoServiceImpl implements IProductoService{

	@Autowired
	private IProductoDao daoProducto;
	
	@Autowired
	private IPresentacionDao daoPresentacion;
	
	@Override
	public List<Producto> findAll(Sort sort) {
		return daoProducto.findAll(sort);
	}

	@Override
	public Page<Producto> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return daoProducto.findAll(pageable);
	}

	@Override
	public Producto findById(long id) {
		// TODO Auto-generated method stub
		return daoProducto.findById(id);
	}

	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		daoProducto.deleteById(id);
	}

	@Override
	public Producto save(Producto producto) {
		
		Presentacion presentacion = daoPresentacion.getById(producto.getPresentacion().getId());
		
		producto.setPresentacion(presentacion);
		
		return daoProducto.save(producto);
	}

	@Override
	public List<Producto> findAll() {
		
		return daoProducto.findAll();
	}

}
