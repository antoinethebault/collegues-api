package dev.collegues.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.collegues.entites.Collegue;
import dev.collegues.repositories.ColleguesRepository;

@RestController
@RequestMapping(value = "/collegues")
public class ColleguesController {

	@Autowired
	ColleguesRepository collegueRepository;

	// GET /collegues?nom=XXX
	// recupere les matricules dont le nom est passe en parametre
	@GetMapping
	public ResponseEntity<Object> matriculesParNom(@RequestParam Optional<String> nom) {
		if (nom.isPresent()) {
			List<Collegue> collegues = collegueRepository.findByNom(nom.get());
			String[] matricules = new String[collegues.size()];
			int i = 0;
			for (Collegue collegue : collegues) {
				matricules[i] = collegue.getMatricule();
				i++;
			}
			return ResponseEntity.status(200).body(matricules);
		} else {
			return ResponseEntity.status(404).body("Erreur : le nom n'est pas present");
		}
	}

	@GetMapping
	@RequestMapping(value = "/all")
	public ResponseEntity<Object> collegues() {
		List<Collegue> collegues = collegueRepository.findAll();
		return ResponseEntity.status(200).body(collegues);
	}

}
