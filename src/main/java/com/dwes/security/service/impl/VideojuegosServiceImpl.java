package com.dwes.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dwes.security.entities.Videojuego;
import com.dwes.security.error.exception.VideojuegoNotFoundException;
import com.dwes.security.repository.VideojuegoRepository;
import com.dwes.security.service.VideojuegosService;


import jakarta.validation.Valid;

@Service
public class VideojuegosServiceImpl implements VideojuegosService {


    @Autowired
    private VideojuegoRepository videojuegoRepository;
 

    @Override
    public Videojuego agregarVideojuego(@Valid Videojuego videojuego) {
        return videojuegoRepository.save(videojuego);
    }

    @Override
    public Videojuego obtenerVideojuegoPorId(Long id) {
        return videojuegoRepository.findById(id)
                .orElseThrow(() -> new VideojuegoNotFoundException("Videojuego no encontrado"));
    }

    @Override
    public Videojuego actualizarVideojuego(Long id, @Valid Videojuego detallesVideojuego) {
        Videojuego videojuego = obtenerVideojuegoPorId(id);
        videojuego.setNombre(detallesVideojuego.getNombre());
        videojuego.setDesarrollador(detallesVideojuego.getDesarrollador());
        videojuego.setEsOnline(detallesVideojuego.isEsOnline());
        return videojuegoRepository.save(videojuego);
    }

    @Override
    public void eliminarVideojuego(Long id) {
        videojuegoRepository.deleteById(id);
    }


	@Override
	public Page<Videojuego> listarTodosLosVideojuegos(Pageable pageable) {
        return videojuegoRepository.findAll(pageable);
	}

}
