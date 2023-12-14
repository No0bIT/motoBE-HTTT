package com.example.motoBE.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.motoBE.model.Symptom;
import com.example.motoBE.model.SymptomType;
import com.example.motoBE.repository.SymptomRepository;
import com.example.motoBE.repository.SymptomTypeRepository;
import com.example.motoBE.subModel.Req;

@RestController
@RequestMapping("/api/symptomtype")
public class SymptomTypeController {

	
	@Autowired
	SymptomTypeRepository  symptomTypeRepository;
	@Autowired
	SymptomRepository symptomRepository;
	
	@PostMapping("/add")
	public SymptomType addST(@RequestBody SymptomType pathST) {
		symptomTypeRepository.save(pathST);
		return pathST;
	}
	 
	@GetMapping("/getAll")
	public List<SymptomType> getAll(){
		return symptomTypeRepository.findAll();
	}
	@GetMapping("/get/{id}")
	public SymptomType getOne(@PathVariable int id){
		return symptomTypeRepository.getById(id);
	}
	
	@PutMapping("/addS")
	public Symptom asdas(@RequestBody Req reg) {
		
		SymptomType st= symptomTypeRepository.getById(reg.getId1());
		Symptom s= symptomRepository.getById(reg.getId2());
		s.setSymptomType(st);
		symptomRepository.save(s);
		return symptomRepository.getById(reg.getId2()) ;
	}
}
