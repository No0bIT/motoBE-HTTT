package com.example.motoBE.subModel;

import java.util.List;

import com.example.motoBE.model.Error;
import com.example.motoBE.model.Symptom;

public class RequestBody1{
    private List<Error> errors;
    private List<Symptom> symptoms;
	public List<Error> getErrors() {
		return errors;
	}
	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}
	public List<Symptom> getSymptoms() {
		return symptoms;
	}
	public void setSymptoms(List<Symptom> symptoms) {
		this.symptoms = symptoms;
	}
    // getters, setters, constructors 
}
