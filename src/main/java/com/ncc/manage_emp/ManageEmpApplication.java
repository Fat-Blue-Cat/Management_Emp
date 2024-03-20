package com.ncc.manage_emp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class ManageEmpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManageEmpApplication.class, args);


	}


}
