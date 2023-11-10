package com.dgpad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.lumosshop.common.entity"})
public class LumosShopFrontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(LumosShopFrontEndApplication.class, args);
	}

}
