package com.example.testserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller 
public class TestServerApplication {

	int visitCount = 0;

	void main(String[] args) {
		SpringApplication.run(TestServerApplication.class, args);
	}

	@GetMapping({"/", "/index.html"})
	String index() {
		visitCount++;

		return """
			<html>
				<head>
					<title>Test Server</title>
				</head>
				<body>
					<h1>Test Server</h1>
					<p>Visit count: %s</p>
					<p>Spring Framework: %s</p>
					<p>Java: %s %s</p>
					<p>OS: %s %s</p>
				</body>
			</html>
			""".formatted(
				visitCount,
				SpringVersion.getVersion(),
				System.getProperty("java.vendor"),
				System.getProperty("java.version"),
				System.getProperty("os.name"),
				System.getProperty("os.version")
			);
	}
}
