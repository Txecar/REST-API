package com.capgemini.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.entities.Presentacion;

@Repository
public interface IPresentacionDao extends JpaRepository<Presentacion, Long>{

}
