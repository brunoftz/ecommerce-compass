package com.compass.ecommerce.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.compass.ecommerce.dtos.ProductDto;
import com.compass.ecommerce.models.ProductModel;
import com.compass.ecommerce.repositories.ProductRepository;


@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public ProductModel addProduct(ProductDto productRecordDto) {
		var productModel = new ProductModel();
		BeanUtils.copyProperties(productRecordDto, productModel);
		return productRepository.save(productModel);
	}

	@Cacheable
	public List<ProductModel> getAllProducts() {
		return productRepository.findAll();
	}

	public Optional<ProductModel> getOneProduct(UUID id) {
		return productRepository.findById(id);
	}

	public Optional<ProductModel> updateProduct(UUID id, ProductDto productRecordDto) {
		Optional<ProductModel> productO = productRepository.findById(id);
		if (productO.isPresent()) {
			var productModel = productO.get();
			BeanUtils.copyProperties(productRecordDto, productModel);
			return Optional.of(productRepository.save(productModel));
		}
		return Optional.empty();
	}

	public Optional<ProductModel> deleteProduct(UUID id) {
		Optional<ProductModel> productO = productRepository.findById(id);
		if (productO.isPresent()) {
			productRepository.delete(productO.get());
			return productO;
		}
		return Optional.empty();
	}
}