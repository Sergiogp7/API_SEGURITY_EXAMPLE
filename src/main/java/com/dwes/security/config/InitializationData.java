package com.dwes.security.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
import com.dwes.security.repository.RecursoRepository;
import com.dwes.security.repository.UserRepository;
import com.github.javafaker.Faker;

@Profile("demo")
@Component
public class InitializationData implements CommandLineRunner {

    @Autowired
    private UserRepository usuarioRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private ProyectoRepository proyectoRepository;
    
    private final boolean borrarLibros = false; // Variable para controlar el borrado de datos
    
    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            
            // Test students
            List<Alumno> alumnos = new ArrayList<>();
            for (int i = 0; i < 5; i++) { // Add 5 test students
                Alumno alumno = new Alumno();
                alumno.setFirstName("StudentFirstName" + i);
                alumno.setLastName("StudentLastName" + i);
                alumno.setEmail("student" + i + "@example.com");
                alumno.setPassword(passwordEncoder.encode("studentpassword" + i));
                alumno.getRoles().add(Role.ROLE_USER); // Assuming students have USER role
                alumnos.add(alumno);
                usuarioRepository.save(alumno); // Save student as a user
            }
            
            // Test curso
            Curso curso = new Curso();
            curso.setValor("23-24"); // Setting the course value for the academic year 2023-2024
            cursoRepository.save(curso);
            

            // Crear proyecto asociado a un alumno y un curso
            for (Alumno alumno : alumnos) {
                Proyecto proyecto = new Proyecto("Proyecto de " + alumno.getFirstName(), curso); // Crear proyecto asociado al curso
                proyecto.getAlumnos().add(alumno); // Asociar alumno al proyecto
                proyectoRepository.save(proyecto); // Guardar proyecto
            }

    	}catch(Exception e) {
    		
    	}
    	Faker faker = new Faker(new Locale("es"));
        for (int i = 0; i < 10; i++) { // Generar 10 libros ficticios
            Libro libro = new Libro();
            libro.setTitulo(faker.book().title());
            libro.setAutor(faker.book().author());
            libro.setIsbn(faker.number().digits(10)); // Genera un ISBN ficticio
  
            libroRepository.save(libro);
        }
        
    }
}