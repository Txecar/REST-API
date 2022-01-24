package com.capgemini.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraintvalidation.SupportedValidationTarget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.dao.IProductoDao;
import com.capgemini.entities.Producto;
import com.capgemini.service.IProductoService;

/*
 * Todas las peticiones (requests) se hacen a la Uri /productos
 * (que es el end point), y segun el verbo del producto HTTP utilizado,
 * que puede GET, POST, PUT, DELETE, ETC., se delegara la peticion a un
 * metodo u otro
 */

@RestController
@RequestMapping(value = "/productos")
public class MainController {

	@Autowired
	private IProductoService productoService;
	
	@GetMapping
	@Transactional(readOnly = true)
	public ResponseEntity<List<Producto>> findAll(@RequestParam(required = false) Integer page, 
			@RequestParam (required = false) Integer size){
		
		ResponseEntity<List<Producto>> responseEntity = null;
		List<Producto> productos = null;
		Sort sortByName = Sort.by("nombre");
		
		if (page != null && size != null) {	
			// Con paginacion	
			Pageable pageable;
			pageable = PageRequest.of(page, size, sortByName);		
			productos = productoService.findAll(pageable).getContent();
					
		} else {
			// Sin paginacion	
			productos = productoService.findAll(sortByName);		
		}
		
		// SI hay productos o no
		if (productos.size() > 0) {	
			responseEntity = new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);	
		} else {		
			responseEntity = new ResponseEntity<List<Producto>>(HttpStatus.NO_CONTENT);
		}
		
		return responseEntity;
	}
	
	// Recupera un producto por el id
	@GetMapping("/{id}")
	public ResponseEntity<Producto> findById(@PathVariable(name = "id")long id){
		
		ResponseEntity<Producto> responseEntity = null;
		
		Producto producto = null;
		
		producto = productoService.findById(id);
		
		if (producto != null) {
			responseEntity = new ResponseEntity<Producto>(producto, HttpStatus.OK);
		} else {
			responseEntity = new ResponseEntity<Producto>(HttpStatus.NO_CONTENT);
		}
		
		return responseEntity;
	}
	
	// Metodo que guarda un producto recibido por Post
	@PostMapping
	public ResponseEntity<Map<String, Object>> guardar(@Valid @RequestBody Producto producto, BindingResult result){
		
		Map<String, Object> responseAsMap = new HashMap<>();
		
		ResponseEntity<Map<String, Object>> responseEntity = null;
		
		List<String> errores = null;
		
		if (result.hasErrors()) {
			
			errores = new ArrayList<>();
			
			for (ObjectError error : result.getAllErrors()) {
				
				errores.add(error.getDefaultMessage());
			}
			
			// Salimos informando al que realizo la peticion (request) de los errores
			// que han tenido lugar
			responseAsMap.put("errorres", errores);
			responseEntity = new ResponseEntity <Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);
			
			return responseEntity;
		}
		
		// Si no hay errores, entonces persistimos (guardar) el producto)
		
		try {
			Producto productoDB = productoService.save(producto);
			
			if (productoDB != null) {
				responseAsMap.put("producto", productoDB);
				responseAsMap.put("mensaje", "El producto con id " + productoDB.getId() 
									+"se ha guardado exitosamente!!!");
				responseEntity = new ResponseEntity <Map<String, Object>>(responseAsMap, HttpStatus.OK);
			} else {
				responseAsMap.put("mensaje", "El producto no se ha podido guardar en la base de datos");
				responseEntity = new ResponseEntity <Map<String, Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (DataAccessException e) {
			responseAsMap.put("mensaje", "Error fatal, no se ha podido guardar el producto");
			responseAsMap.put("error", e.getMostSpecificCause());
			responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return responseEntity;
	}
	
	// Metodo que actualizar un producto
	// Practicamente similar al metodo que persiste el producto
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable(name = "id") long id, 
			@Valid @RequestBody Producto producto, BindingResult result){
		
		Map<String, Object> responseAsMap = new HashMap<>();
		
		ResponseEntity<Map<String, Object>> responseEntity = null;
		
		List<String> errores = null;
		
		if (result.hasErrors()) {
			
			errores = new ArrayList<>();
			
			for (ObjectError error : result.getAllErrors()) {
				
				errores.add(error.getDefaultMessage());
			}
			
			// Salimos informando al que realizo la peticion (request) de los errores
			// que han tenido lugar
			responseAsMap.put("errorres", errores);
			responseEntity = new ResponseEntity <Map<String, Object>>(responseAsMap, HttpStatus.BAD_REQUEST);
			
			return responseEntity;
		}
		
		// Si no hay errores, entonces persistimos (guardar) el producto)
		
		try {
			// Lo nuevo, añadido a partir del metodo de guardar
			producto.setId(id);
			
			Producto productoDB = productoService.save(producto);
			
			if (productoDB != null) {
				responseAsMap.put("producto", productoDB);
				responseAsMap.put("mensaje", "El producto con id " + productoDB.getId() 
									+"se ha ACTUALIZADO exitosamente!!!");
				responseEntity = new ResponseEntity <Map<String, Object>>(responseAsMap, HttpStatus.OK);
			} else {
				responseAsMap.put("mensaje", "El producto no se ha podido actualizar en la base de datos");
				responseEntity = new ResponseEntity <Map<String, Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (DataAccessException e) {
			responseAsMap.put("mensaje", "Error fatal, no se ha podido actualizar el producto");
			responseAsMap.put("error", e.getMostSpecificCause());
			responseEntity = new ResponseEntity<Map<String, Object>>(responseAsMap,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return responseEntity;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Producto> delete(@PathVariable(name = "id") long id, 
			@RequestBody Producto producto){
		
		ResponseEntity<Producto> responseEntity = null;
		
		// Si no hay errores, entonces persistimos (guardar) el producto)
		
		try {
			// Lo nuevo, añadido a partir del metodo de guardar
			
			Producto productoDB = productoService.findById(id);
			if(productoDB!=null) {
				productoService.delete(id);
				responseEntity = new ResponseEntity <Producto>(HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity <Producto>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (DataAccessException e) {
			responseEntity = new ResponseEntity<Producto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return responseEntity;
	}
	
	
	// El siguiente metodo no es Rest (Nada de REST tiene),
	// mas bien RPC (Remote Procedure Call)
//	@GetMapping
//	public List<Producto> findAll() {
//	
//		return productoService.findAll();
//	}
	
}
