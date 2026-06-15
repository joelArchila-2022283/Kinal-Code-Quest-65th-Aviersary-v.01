package com.J.M_CODERS.KinalCodeQuest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class
KinalCodeQuestApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(KinalCodeQuestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("----------------------------------------");
		System.out.println(" KINAL CODE QUEST - SYSTEM ONLINE ");
		System.out.println("----------------------------------------");
	}
}
