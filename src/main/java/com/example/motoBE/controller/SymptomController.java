package com.example.motoBE.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.motoBE.model.Symptom;
import com.example.motoBE.model.SymptomType;
import com.example.motoBE.repository.SymptomRepository;
import com.example.motoBE.repository.SymptomTypeRepository;

@RestController
@RequestMapping("/api/symptom")
public class SymptomController {

	@Autowired
	SymptomRepository symptomRepository;
	@Autowired
	SymptomTypeRepository symptomTypeRepository;
	
	@PostMapping("/add")
	public Symptom addS(@RequestBody Symptom s)
	{
		
		symptomRepository.save(s);
		return s;
	}
	@GetMapping("/getAll")
	public List<Symptom> geta()
	{

		return symptomRepository.findAll();
	}
	@GetMapping("/get/{id}")
	public Symptom getOne(@PathVariable int id)
	{
		return symptomRepository.getById(id);
	}
	@GetMapping("/search/{stringSearch}")
	public List<Symptom> searchSymptoms(@PathVariable String stringSearch){
		if(stringSearch==null)
			stringSearch="";
		return symptomRepository.searchSymptomsByStringDescription(stringSearch);
	}
	
	@GetMapping("/getsByType/{id}")
	public List<Symptom> getsByType(@PathVariable int id ){
		SymptomType symptomType= symptomTypeRepository.getById(id);
		return symptomType.getSymptoms();
	}
	
}
