package com.example.motoBE.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.motoBE.model.Error;
import com.example.motoBE.model.Symptom;
import com.example.motoBE.repository.ErrorRepository;
import com.example.motoBE.repository.SymptomRepository;
import com.example.motoBE.subModel.RequestBody1;

@RestController
@RequestMapping("api/forward")
public class ForwardController {
	
//	
//	@PostMapping("/get/first")
//	public List<Error> getForwardFirst(@RequestBody List<Symptom> listS){
//		List<Error> filteredErrors = listS.stream()
//	            .flatMap(symptom -> symptom.getRelatedErrors().stream())
//	            .distinct() // Đảm bảo không có Error nào bị lặp lại
//	            .collect(Collectors.toList());
//		// Tiếp tục xử lý các Error trong filteredErrors
//		
//		Map<Error, Double> errorRatios = new HashMap<>();
//	    for (Error error : filteredErrors) {
//	        List<Symptom> relatedSymptoms = error.getRelatedSymptoms();
//
//	        // Đặt lại flag của Symptom trong Error thành 1 nếu có trong danh sách nhận vào
//	        for (Symptom symptom : relatedSymptoms) {
//	            if (listS.contains(symptom)) {
//	                symptom.setFlag(1);
//	            }
//	        }
//	        long countFlagOne = relatedSymptoms.stream()
//	                .filter(symptom -> symptom.getFlag() == 1)
//	                .count();
//	        long totalCount = relatedSymptoms.size();
//
//	        // Tính toán tỷ lệ và đặt vào thuộc tính để sử dụng trong việc sắp xếp
//	        double ratio = (double) countFlagOne / totalCount;
//	        errorRatios.put(error, ratio);
//	    }
//	    // Sắp xếp danh sách Error theo tỷ lệ giảm dần
//	    List<Error> sortedErrors = errorRatios.entrySet().stream()
//                .sorted(Map.Entry.<Error, Double>comparingByValue().reversed())
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toList());
//	    return sortedErrors;
//	}
	@Autowired
	SymptomRepository symptomRepository;
	@Autowired
	ErrorRepository errorRepository;
	
	@PostMapping("/get")
	public List<Error> getForwardSecond(@RequestBody RequestBody1 requestBody1  ){
		List<Symptom> listS = new ArrayList<>() ;
		List<Integer> listE = new ArrayList<>() ;
		// Lấy ra danh sách triệu chứng từ người dùng chọn
		for(Symptom s:requestBody1.getSymptoms()) {
			Symptom ss=symptomRepository.getById(s.getId());
			listS.add(ss);
		}
		// Lấy ra danh sách lỗi đã duyệt qua
		for(Error e:requestBody1.getErrors()) {
			listE.add(e.getId());
		}
		List<Error> errorsToRemove = new ArrayList<>();
		
		// lấy ra danh sách lỗi từ triệu chứng
		List<Error> filteredErrors = listS.stream()
	            .flatMap(symptom -> symptom.getRelatedErrors().stream())
	            .distinct() // Đảm bảo không có Error nào bị lặp lại
	            .collect(Collectors.toList());
		
		for(Error error:filteredErrors) {
			if(listE.contains(error.getId())) {
				errorsToRemove.add(error);
			}
		}
		filteredErrors.removeAll(errorsToRemove);
		// Tiếp tục xử lý các Error trong filteredErrors
		Map<Error, Double> errorRatios = new HashMap<>();
	    for (Error error : filteredErrors) {
	        List<Symptom> relatedSymptoms = error.getRelatedSymptoms();

	        // Đặt lại flag của Symptom trong Error thành 1 nếu có trong danh sách nhận vào
	        for (Symptom symptom : relatedSymptoms) {
	            if (listS.contains(symptom)) {
	                symptom.setFlag(1);
	            }
	        }
	        
	        // Sắp xếp relatedSymptoms sao cho các Symptom có flag = 1 được đưa lên đầu
	        relatedSymptoms.sort((s1, s2) -> {
	            if (s1.getFlag() == 1 && s2.getFlag() != 1) {
	                return -1; // s1 có flag = 1 nên đưa lên trước
	            } else if (s1.getFlag() != 1 && s2.getFlag() == 1) {
	                return 1; // s2 có flag = 1 nên đưa lên trước
	            }
	            return 0; // giữ nguyên vị trí
	        });
	        
	        long countFlagOne = relatedSymptoms.stream()
	                .filter(symptom -> symptom.getFlag() == 1)
	                .count();
	        long totalCount = relatedSymptoms.size();

	        // Tính toán tỷ lệ và đặt vào thuộc tính để sử dụng trong việc sắp xếp
	        double ratio = (double) countFlagOne / totalCount;
	        errorRatios.put(error, ratio);
	    }
	    // Sắp xếp danh sách Error theo tỷ lệ giảm dần
	    List<Error> sortedErrors = errorRatios.entrySet().stream()
                .sorted(Map.Entry.<Error, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
	    return sortedErrors;
	}
	@PostMapping("/getNoneInterRaction")
	public List<Error> getForwardNoneInterRaction(@RequestBody RequestBody1 requestBody1  ){
		List<Symptom> listS = new ArrayList<>() ;
		List<Integer> listE = new ArrayList<>() ;
		for(Symptom s:requestBody1.getSymptoms()) {
			Symptom ss=symptomRepository.getById(s.getId());
			listS.add(ss);
		}
		for(Error e:requestBody1.getErrors()) {
			listE.add(e.getId());
		}
		List<Error> errorsToRemove = new ArrayList<>();
		
		List<Error> filteredErrors = listS.stream()
	            .flatMap(symptom -> symptom.getRelatedErrors().stream())
	            .distinct() // Đảm bảo không có Error nào bị lặp lại
	            .collect(Collectors.toList());
	//	 xóa bỏ các Error có flag = 1 khỏi danh sách
		for(Error error:filteredErrors) {
			if(listE.contains(error.getId())) {
				errorsToRemove.add(error);
			}
		}
		filteredErrors.removeAll(errorsToRemove);
		// Tiếp tục xử lý các Error trong filteredErrors
		Map<Error, Double> errorRatios = new HashMap<>();
	    for (Error error : filteredErrors) {
	        List<Symptom> relatedSymptoms = error.getRelatedSymptoms();

	        // Đặt lại flag của Symptom trong Error thành 1 nếu có trong danh sách nhận vào
	        for (Symptom symptom : relatedSymptoms) {
	            if (listS.contains(symptom)) {
	                symptom.setFlag(1);
	            }
	        }
	        long countFlagOne = relatedSymptoms.stream()
	                .filter(symptom -> symptom.getFlag() == 1)
	                .count();
	        long totalCount = relatedSymptoms.size();

	        // Tính toán tỷ lệ và đặt vào thuộc tính để sử dụng trong việc sắp xếp
	        double ratio = (double) countFlagOne / totalCount;
	        errorRatios.put(error, ratio);
	    }
	    // Sắp xếp danh sách Error theo tỷ lệ giảm dần
	    List<Error> sortedErrors = errorRatios.entrySet().stream()
                .sorted(Map.Entry.<Error, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
	    return sortedErrors;
	}
}

