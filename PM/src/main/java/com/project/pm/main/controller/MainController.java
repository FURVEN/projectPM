package com.project.pm.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class MainController {
	
	@RequestMapping(value = "/")
	public String main() {
		return "redirect:login.pm";
	}
	
	@RequestMapping(value = "/index.pm")
	public String viewMain() {
		
		return "main.admin";
	}

}
