package com.dwes.security.controller;

import com.dwes.security.entities.Videojuego;
import com.dwes.security.service.VideojuegosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/videojuegos")
public class VideojuegosController {

    private static final Logger logger = LoggerFactory.getLogger(VideojuegosController.class);
    private static final String API_BASE = "https://www.freetogame.com/api";

    @Autowired
    private VideojuegosService videojuegosService;

    private final RestTemplate restTemplate = new RestTemplate();

    // ── Métodos CRUD propios ────────────────────────────────────────────────

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<Videojuego>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(videojuegosService.listarTodosLosVideojuegos(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    public ResponseEntity<Videojuego> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(videojuegosService.obtenerVideojuegoPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Videojuego> crear(@RequestBody Videojuego v) {
        return new ResponseEntity<>(videojuegosService.agregarVideojuego(v), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Videojuego> actualizar(@PathVariable Long id, @RequestBody Videojuego v) {
        return ResponseEntity.ok(videojuegosService.actualizarVideojuego(id, v));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        videojuegosService.eliminarVideojuego(id);
        return ResponseEntity.noContent().build();
    }

    // ── Endpoints externos ──────────────────────────────────────────────────

    @GetMapping("/external/populares")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Videojuego>> popularesExternos(
            @RequestParam(defaultValue = "12") int limit) {

        String url = API_BASE + "/games?platform=pc&sort-by=popularity";

        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> raw = restTemplate.getForObject(url, List.class);

            if (raw == null || raw.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            List<Videojuego> juegos = raw.stream()
                    .limit(limit)
                    .map(this::mapToVideojuego)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(juegos);

        } catch (Exception e) {
            logger.error("Error al consultar API externa", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @GetMapping("/external/{id}")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    public ResponseEntity<Videojuego> detalleExterno(@PathVariable int id) {

        String url = API_BASE + "/game?id=" + id;

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> raw = restTemplate.getForObject(url, Map.class);

            if (raw == null || raw.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Videojuego v = mapToVideojuego(raw);
            if (v == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(v);

        } catch (Exception e) {
            logger.error("Error detalle externo id={}", id, e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    // Mapper privado – aquí está la magia
    private Videojuego mapToVideojuego(Map<String, Object> data) {
        try {
            Videojuego v = new Videojuego();
            // id de la API externa → lo ponemos como Long (aunque no sea pk real en tu BD)
            Number idNum = (Number) data.get("id");
            v.setId(idNum != null ? idNum.longValue() : null);

            v.setNombre((String) data.get("title"));
            v.setDesarrollador((String) data.getOrDefault("developer", "Desconocido"));

            // Lógica simple para esOnline
            String genre = ((String) data.getOrDefault("genre", "")).toLowerCase();
            String platform = ((String) data.getOrDefault("platform", "")).toLowerCase();
            boolean online = genre.contains("mmorpg") ||
                             genre.contains("multiplayer") ||
                             platform.contains("online") ||
                             platform.contains("browser"); // browser suele ser online

            v.setEsOnline(online);

            // Validación mínima (evitamos objetos inválidos)
            if (v.getNombre() == null || v.getNombre().trim().isEmpty() ||
                v.getDesarrollador() == null || v.getDesarrollador().trim().isEmpty()) {
                return null;
            }

            return v;
        } catch (Exception e) {
            logger.warn("No se pudo mapear juego: {}", data, e);
            return null;
        }
    }
}