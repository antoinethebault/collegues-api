package dev.collegues.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.collegues.entites.Collegue;

public interface ColleguesRepository extends JpaRepository<Collegue, Integer> {

	List<Collegue> findByNom(String nom);

}
