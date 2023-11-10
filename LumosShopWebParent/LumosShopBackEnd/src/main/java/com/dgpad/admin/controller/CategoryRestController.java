package com.dgpad.admin.controller;

import com.dgpad.admin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

	@Autowired
	private CategoryService service;

	@PostMapping("/categories/check_unique")

	public String checkIfCategoryNameAndAliasAreUnique(Integer id,
													   @RequestParam("name") String name,
													   @RequestParam("alias") String alias) {
		return service.isCategoryNameAndAliasUnique(id, name, alias) ? "OK" : "Duplicated";
	}


}
