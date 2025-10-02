package com.system.bank_manager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Bank Manager API",
                version = "1.0",
                description = "Sistema para la gestión de usuarios, cuentas y transacciones bancarias"
        )
)
public class BankManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankManagerApplication.class, args);
	}

}
