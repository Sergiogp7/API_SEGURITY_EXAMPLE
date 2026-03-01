package com.dwes.security.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dwes.security.controller.user.AuthorizationAdminController;
import com.dwes.security.entities.Usuario;
import com.dwes.security.entities.Videojuego;
import com.dwes.security.service.VideojuegosService;
import org.springframework.security.core.context.SecurityContextHolder;

	@RestController
	@RequestMapping("/api/v1/videojuegos")
	public class VideojuegosController {

    	private static final Logger logger = LoggerFactory.getLogger(VideojuegosController.class);

	    @Autowired
	    private VideojuegosService videojuegosService;

	    @GetMapping
	    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
	    public ResponseEntity<Page<Videojuego>> listarTodosLosVideojuegos(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {
	        
	        logger.info("VideojuegosController :: listarTodosLosVideojuegos");
	        Pageable pageable = PageRequest.of(page, size);
	        return new ResponseEntity<>(videojuegosService.listarTodosLosVideojuegos(pageable), HttpStatus.OK);
	    }
	    
	    @GetMapping("/{id}")
	    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
	    public Videojuego getBookById(@PathVariable Long id) {
	        return videojuegosService.obtenerVideojuegoPorId(id);
	    }

	    @PostMapping
	    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	    public Videojuego createBook(@RequestBody Videojuego book) {
	        Usuario contextUser = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        book.setCreador(contextUser);
	        return videojuegosService.agregarVideojuego(book);
	    }

	    

	    @PutMapping("/{id}")
	    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	    public Videojuego updateBook(@PathVariable Long id, @RequestBody Videojuego bookDetails) {
	        return videojuegosService.actualizarVideojuego(id, bookDetails);
	    }

	    @DeleteMapping("/{id}")
	    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	    public void deleteBook(@PathVariable Long id) {
	        videojuegosService.eliminarVideojuego(id);
	    }
	}