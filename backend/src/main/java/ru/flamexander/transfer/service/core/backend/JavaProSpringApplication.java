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
	- Подготовить wiremock для мс лимитов в основном сервисе
	- Подготовить скрипты наполнения БД 100 переводами (чтобы можно было удобно историю проверять)
	- Пропишите README.md как новому разработчику запустить сервис
*/

@SpringBootApplication
public class JavaProSpringApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavaProSpringApplication.class, args);
	}
}
