package com.example.motoBE.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.motoBE.model.Error;
import com.example.motoBE.model.ErrorType;
import com.example.motoBE.model.Symptom;
import com.example.motoBE.repository.ErrorRepository;
import com.example.motoBE.repository.ErrorTypeRepository;
import com.example.motoBE.repository.SymptomRepository;
import com.example.motoBE.repository.SymptomTypeRepository;
import com.example.motoBE.subModel.ReqES;







@RestController
@RequestMapping("/api/error")
public class ErrorController {

	@Autowired
	ErrorTypeRepository errorTypeRepository;
	
	@Autowired
	ErrorRepository errorRepository;
	
	@Autowired
	SymptomTypeRepository symptomTypeRepository;
	
	@Autowired
	SymptomRepository symptomRepository;
	
	@PostMapping("/add")
	public Error error(@RequestBody Error pathError) {
		errorRepository.save(pathError);		
		return pathError;
		
	}
	@GetMapping("/get/{id}")
	public Error getE(@PathVariable int id) {
		return errorRepository.getById(id);
	}
	@GetMapping("/getAll")
	public List<Error> getAll() {
		return errorRepository.findAll();
	}
//	@PostMapping("/editById/{id}")
//	public Error editError(@RequestBody Error pathError,@) {
//		errorRepository.save(pathError);		
//		return pathError;
//		
//	}
//	@PostMapping("/addES/{ide}/{ids}")
//	public Error addES(@PathVariable int ide,@PathVariable int ids) {
//		Error e= errorRepository.getById(ide);
//		Symptom s= symptomRepository.getById(ids);
//		List<Symptom> listS= e.getRelatedSymptoms();
//		listS.add(s);
//		errorRepository.save(e);
//		return errorRepository.getById(ids);
//	}
	
	@PutMapping("/connect")
	public Error connect(@RequestBody ReqES reqES) {
		Error e= errorRepository.getById(reqES.getId1());
		List<Symptom> relatedSymptoms = new ArrayList<>();
		for(int ids:reqES.getId2()) {
			Symptom s= symptomRepository.getById(ids);
//			List<Symptom> relatedSymptoms = e.getRelatedSymptoms();
			relatedSymptoms.add(s);
			e.setRelatedSymptoms(relatedSymptoms);
			
			
			List<Error> relatedError= s.getRelatedErrors();
			relatedError.add(e);
			s.setRelatedErrors(relatedError);
			
			errorRepository.save(e);
			symptomRepository.save(s);
			
		}	
		return errorRepository.getById(reqES.getId1());
	}
	
	
	@GetMapping("/search/{stringSearch}")
	public List<Error> searchSymptoms(@PathVariable String stringSearch ){
		return errorRepository.searchErrorByStringName(stringSearch);
	}
	
	
	@GetMapping("/getReport")
	public String getReport() {
		String s="";
		int x=0;
		List<ErrorType> errorTypes=errorTypeRepository.findAll();
		
		for(ErrorType errorType:errorTypes) {
			x+=1;
			s+= x+". "+errorType.getName()    +"\n"	;
			int i=0;
			for(Error error: errorType.getErrors()) {
				i+=1;
				s+="	" + i+". "+error.getName()+"\n"+"		+ Ảnh minh họa:"+error.getLinkImg()+"\n"+"		+ Triệu chứng:\n"	;
				for(Symptom symptom: error.getRelatedSymptoms()) {
					s+="			- "+ symptom.getDescription()+"\n";
				}
				s+="		+ Cách sửa: "+error.getRepair()+"\n";
			}
		}
		return s;
	}
}
