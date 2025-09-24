package com.sparta.myselectshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	/**
	 * [메인화면]
	 *
	 * @return 메인 페이지 (index.html)
	 */
	@GetMapping("/")
	public String home() {

		return "index";
	}

}