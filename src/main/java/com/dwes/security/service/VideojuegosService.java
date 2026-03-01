package com.dwes.security.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dwes.security.entities.Videojuego;

public interface VideojuegosService {

    Videojuego agregarVideojuego(Videojuego videojuego);

    Page<Videojuego> listarTodosLosVideojuegos(Pageable pageable);

    Videojuego obtenerVideojuegoPorId(Long id);

    Videojuego actualizarVideojuego(Long id, Videojuego videojuego);

    void eliminarVideojuego(Long id);
   
}