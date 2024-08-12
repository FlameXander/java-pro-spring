package ru.flamexander.transfer.service.core.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// -Dspring.profiles.active=dev

/*
	Чего не хватает:
	- Сквозных логов
	- Заглушек вместо сервисов
	- Как быстро включаться в работу над новым сервисом
	- Обсудить как взаимодействует большое количество сервисов

	Домашнее задание:
	- Переведите интеграцию с мс лимитов на RestClient
	- Добавьте возможность задавать readTimeout и writeTimeout
	- Сделайте RestClientFactory, который умеет из Properties собирать готовый RestClient
*/

@SpringBootApplication
public class JavaProSpringApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavaProSpringApplication.class, args);
	}
}
