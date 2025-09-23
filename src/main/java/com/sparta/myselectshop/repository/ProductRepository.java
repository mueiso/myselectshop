package com.sparta.myselectshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Page<Product> findAllByUser(User user, Pageable pageable);

}