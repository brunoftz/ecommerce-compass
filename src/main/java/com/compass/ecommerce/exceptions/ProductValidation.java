package com.compass.ecommerce.exceptions;

public class ProductValidation extends RuntimeException {
	 private static final long serialVersionUID = 1L;
	
	 public ProductValidation(String message) {
	        super(message);
	    }

}
