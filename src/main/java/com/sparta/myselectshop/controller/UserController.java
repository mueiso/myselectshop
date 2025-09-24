package com.sparta.myselectshop.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sparta.myselectshop.dto.SignupRequestDto;
import com.sparta.myselectshop.dto.UserInfoDto;
import com.sparta.myselectshop.entity.UserRoleEnum;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.FolderService;
import com.sparta.myselectshop.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

	private final UserService userService;
	private final FolderService folderService;

	/**
	 * [로그인]
	 *
	 * @return 로그인 페이지 (login.html)
	 */
	@GetMapping("/user/login-page")
	public String loginPage() {

		return "login";
	}

	/**
	 * [회원가입]
	 *
	 * @return 회원가입 페이지 (signup.html)
	 */
	@GetMapping("/user/signup")
	public String signupPage() {

		return "signup";
	}

	/**
	 * [회원가입]
	 *
	 * @param requestDto 회원가입 필요 정보 (이름, 비밀번호)
	 * @param bindingResult ADMIN 권한 여부 / ADMIN인 경우 관리자 권한 가진 토큰값
	 * @return 회원가입 성공 시 로그인 페이지로 리다이렉트 (login.html)
	 */
	@PostMapping("/user/signup")
	public String signup(
		@Valid SignupRequestDto requestDto,
		BindingResult bindingResult) {

		// Validation 예외처리
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		if (fieldErrors.size() > 0) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
			}
			return "redirect:/api/user/signup";
		}

		userService.signup(requestDto);

		return "redirect:/api/user/login-page";
	}

	/**
	 * [회원 관련 정보 받기]
	 *
	 * @param userDetails 로그인한 유저의 정보
	 * @return 로그인한 유저의 정보 (이름, 관리자 권한 여부)
	 */
	@GetMapping("/user-info")
	@ResponseBody
	public UserInfoDto getUserInfo(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		String username = userDetails.getUser().getUsername();
		UserRoleEnum role = userDetails.getUser().getRole();
		boolean isAdmin = (role == UserRoleEnum.ADMIN);

		return new UserInfoDto(username, isAdmin);
	}

	/**
	 * [폴더의 회원 관련 정보 받기]
	 *
	 * @param model 로그인한 사용자의 폴더 목록을 조회하여 Model에 담음 (model = 뷰에 데이터를 전달하기 위한 객체)
	 * @param userDetails 로그인한 유저의 정보
	 * @return index.html에 내 fragment 영역을 반환
	 */
	@GetMapping("/user-folder")
	public String getUserInfo(
		Model model,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		model.addAttribute("folders", folderService.getFolders(userDetails.getUser()));

		return "index :: #fragment";
	}

}