package dev.collegues.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Exception.CollegueNonTrouveException;
import dev.collegues.dto.CollegueUpdateDto;
import dev.collegues.entites.Collegue;
import dev.collegues.repositories.ColleguesRepository;

@RestController
@RequestMapping(value = "/collegues")
public class ColleguesController {

	@Autowired
	ColleguesRepository collegueRepository;

	/**
	 * GET /collegues?nom=XXX // recupere les matricules dont le nom est passe en
	 * parametre
	 */
	@GetMapping
	@CrossOrigin
	public ResponseEntity<Object> matriculesParNom(@Valid @RequestParam Optional<String> nom) {
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
	@CrossOrigin
	public ResponseEntity<Object> collegues() {
		List<Collegue> collegues = collegueRepository.findAll();
		return ResponseEntity.status(200).body(collegues);
	}

	/**
	 * GET : collegues/matricule/matricule ------- Recuperation d'un collegue par //
	 * son matricule
	 */
	@GetMapping("{matricule}")
	@CrossOrigin
	public ResponseEntity<Object> collegueParMatricule(@Valid @PathVariable Optional<String> matricule)
			throws CollegueNonTrouveException {
		if (matricule.isPresent()) {
			Optional<Collegue> collegue = collegueRepository.findByMatricule(matricule.get());
			if (collegue.isPresent())
				return ResponseEntity.status(200).body(collegue.get());
			else
				return CollegueNonTrouveException.CollegueNonTrouveException();

		} else {
			return ResponseEntity.status(404).body("Erreur : le matricule n'est pas present");
		}
	}

	/** POST : collegues/ ------ creation d'un collegue */
	@PostMapping
	@CrossOrigin
	public ResponseEntity<Object> creationClient(@RequestBody Collegue collegue) {
		collegue.setMatricule(UUID.randomUUID().toString());
		collegueRepository.save(collegue);
		Optional<Collegue> collegueSaved = collegueRepository.findByMatricule(collegue.getMatricule());
		if (collegueSaved.isPresent())
			return ResponseEntity.status(200).body(collegueSaved.get());
		else
			return ResponseEntity.status(500).body("Erreur : le collegue n'a pas ete sauvegarde");
	}

	/** met a jour les donnees d'un client en fonction de son matricule */
	@PatchMapping("{matricule}")
	@CrossOrigin
	public ResponseEntity<Object> updateClient(@RequestBody CollegueUpdateDto collegueUpdate,
			@Valid @PathVariable Optional<String> matricule) {
		Optional<Collegue> collegue = collegueRepository.findByMatricule(matricule.get());
		if (collegue.isPresent()) {
			if (collegueUpdate.getPhotoUrl() != null)
				collegue.get().setPhotoUrl(collegueUpdate.getPhotoUrl());
			if (collegueUpdate.getEmail() != null)
				collegue.get().setEmail(collegueUpdate.getEmail());
			collegueRepository.save(collegue.get());
			return ResponseEntity.status(200).body(collegue.get());
		} else {
			return ResponseEntity.status(404).body("Erreur : le collegue a mettre a jour n'a pas ete trouve");
		}
	}

}
