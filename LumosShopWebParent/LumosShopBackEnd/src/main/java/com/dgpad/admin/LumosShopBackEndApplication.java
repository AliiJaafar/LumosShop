package com.dgpad.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.lumosshop.common.entity"})

public class LumosShopBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(LumosShopBackEndApplication.class, args);
	}

}
