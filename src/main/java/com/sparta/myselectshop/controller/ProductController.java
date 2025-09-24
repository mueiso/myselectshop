package com.sparta.myselectshop.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

	private final ProductService productService;

	/**
	 * [관심상품 등록]
	 *
	 * @param requestDto 관심상품 등록 요청 데이터 (상품명, 링크, 이미지, 최저가 등)
	 * @return 등록된 상품 정보를 담은 ProductResponseDto
	 */
	@PostMapping("/products")
	public ProductResponseDto createProduct(
		@RequestBody ProductRequestDto requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		return productService.createProduct(requestDto, userDetails.getUser());
	}

	/**
	 * [관심상품 희망가격 수정]
	 *
	 * @param id 수정할 상품의 ID
	 * @param requestDto 수정할 희망가격을 담은 요청 데이터
	 * @return 희망가격이 수정된 상품 정보를 담은 ProductResponseDto
	 */
	@PutMapping("/products/{id}")
	public ProductResponseDto updateProduct(
		@PathVariable("id") Long id,
		@RequestBody ProductMypriceRequestDto requestDto) {

		return productService.updateProduct(id, requestDto);
	}

	/**
	 * [관심상품 전체 조회]
	 *
	 * @return 등록된 상품들의 리스트 (ProductResponseDtoList)
	 */
	@GetMapping("/products")
	public Page<ProductResponseDto> getProducts(
		@RequestParam("page") int page,
		@RequestParam("size") int size,
		@RequestParam("sortBy") String sortBy,
		@RequestParam("isAsc") boolean isAsc,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		return productService.getProducts(userDetails.getUser(), page-1, size, sortBy, isAsc);
	}

	/**
	 * [ADMIN 계정 전용 관심상품 전체 조회]
	 *
	 * @return 모든 계정에 등록된 관심상품 전체 리스트 (ProductResponseDtoList)
	 */
	@GetMapping("/admin/products")
	public List<ProductResponseDto> getAllProducts() {

		return productService.getAllProducts();
	}

	/**
	 * [상품에 폴더 추가]
	 *
	 * @param productId 폴더에 추가할 상품 ID
	 * @param folderId 상품을 추가할 폴더 ID
	 * @param userDetails 로그인한 유저의 정보
	 */
	@PostMapping("/products/{productId}/folder")
	public void addFolder(
		@PathVariable Long productId,
		@RequestParam Long folderId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		productService.addFolder(productId, folderId, userDetails.getUser());
	}

	// 해당 폴더에 등록이 되어있는 상품들 조회
	@GetMapping("/folders/{folderId}/products")
	public Page<ProductResponseDto> getProductsInFolder(
		@PathVariable Long folderId,
		@RequestParam("page") int page,
		@RequestParam("size") int size,
		@RequestParam("sortBy") String sortBy,
		@RequestParam("isAsc") boolean isAsc,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		return productService.getProductsInFolder(folderId, page-1, size, sortBy, isAsc, userDetails.getUser());
	}

}