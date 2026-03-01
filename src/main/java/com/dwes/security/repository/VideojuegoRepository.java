package com.dwes.security.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.dwes.security.entities.Videojuego;
@Repository
public interface VideojuegoRepository extends JpaRepository<Videojuego, Long> {
	
}
