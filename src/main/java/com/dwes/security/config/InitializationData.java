package com.dwes.security.config;

import java.util.HashSet;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dwes.security.entities.Alumno;
import com.dwes.security.entities.Curso;
import com.dwes.security.entities.Libro;
import com.dwes.security.entities.Proyecto;
import com.dwes.security.entities.Role;
import com.dwes.security.entities.Usuario;
import com.dwes.security.repository.CursoRepository;
import com.dwes.security.repository.LibroRepository;
import com.dwes.security.repository.ProyectoRepository;
import com.dwes.security.repository.UserRepository;
import com.github.javafaker.Faker;

@Profile("demo")
@Component
public class InitializationData implements CommandLineRunner {

    @Autowired
    private UserRepository usuarioRepository;
    
    private final boolean borrarLibros = false; // Variable para controlar el borrado de datos
    
    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private ProyectoRepository proyectoRepository;

    @Override
    public void run(String... args) throws Exception {
    	
    	if (borrarLibros) {
            libroRepository.deleteAll(); // Borra todos los libros existentes
        }
    	
    	try {
    		// Usuario 1 - Rol USER
            Usuario usuario1 = new Usuario();
            usuario1.setFirstName("Alice");
            usuario1.setLastName("Johnson");
            usuario1.setEmail("alice.johnson@example.com");
            usuario1.setPassword(passwordEncoder.encode("password123"));
            usuario1.getRoles().add(Role.ROLE_USER);
            usuarioRepository.save(usuario1);

            // Usuario 2 - Rol ADMIN
            Usuario usuario2 = new Usuario();
            usuario2.setFirstName("Bob");
            usuario2.setLastName("Smith");
            usuario2.setEmail("bob.smith@example.com");
            usuario2.setPassword(passwordEncoder.encode("password456"));
            usuario2.getRoles().add(Role.ROLE_ADMIN);
            usuarioRepository.save(usuario2);

            // Usuario 3 - Rol USER
            Usuario usuario3 = new Usuario();
            usuario3.setFirstName("Carol");
            usuario3.setLastName("Davis");
            usuario3.setEmail("carol.davis@example.com");
            usuario3.setPassword(passwordEncoder.encode("password789"));
            usuario3.getRoles().add(Role.ROLE_USER);
            usuarioRepository.save(usuario3);
            
            
            
            
    	}catch(Exception e) {
    		
    	}
    	Faker faker = new Faker(new Locale("es"));
    try {
    	 // Crear curso 23-24
        Curso curso = new Curso();
        curso.setValor("23-24");
        cursoRepository.save(curso);
    	
    	  // Crear tres alumnos y sus proyectos
        for (int i = 0; i < 3; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String password = passwordEncoder.encode("password123");

            // Crear usuario/alumno
            Alumno alumno = new Alumno();
            alumno.setFirstName(firstName);
            alumno.setLastName(lastName);
            alumno.setEmail(email);
            alumno.setPassword(password);
            alumno.getRoles().add(Role.ROLE_USER);
            usuarioRepository.save(alumno);

            // Crear proyecto asociado al curso
            Proyecto proyecto = new Proyecto("Proyecto de " + firstName, curso);
            proyecto.setAlumnos(new HashSet<Alumno>()); // Inicializar la colección de alumnos
            proyecto.getAlumnos().add(alumno); // Asociar alumno al proyecto

            // También debes actualizar el conjunto de proyectos del alumno
            if (alumno.getProyectos() == null) {
                alumno.setProyectos(new HashSet<>());
            }
            alumno.getProyectos().add(proyecto);

            // Guardar proyecto y actualizar alumno
            proyectoRepository.save(proyecto);
            usuarioRepository.save(alumno);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    
    
        for (int i = 0; i < 10; i++) { // Generar 10 libros ficticios
            Libro libro = new Libro();
            libro.setTitulo(faker.book().title());
            libro.setAutor(faker.book().author());
            libro.setIsbn(faker.number().digits(10)); // Genera un ISBN ficticio
  
            libroRepository.save(libro);
        }
        
    }
}