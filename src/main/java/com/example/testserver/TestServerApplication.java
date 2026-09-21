package com.example.testserver;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller 
public class TestServerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TestServerApplication.class, args);
    }

    int visitCount = 0;

    @GetMapping({"/", "/index.html"})
    @ResponseBody 
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
                    <p>Current time: %s</p>
                    <p>Spring framework: %s</p>
                    <p>Java: %s %s</p>
                    <p>Operating system: %s %s</p>
                </body>
            </html>
            """.formatted(
                visitCount,
                LocalDateTime.now(),
                SpringVersion.getVersion(),
                System.getProperty("java.vendor"),
                System.getProperty("java.version"),
                System.getProperty("os.name"),
                System.getProperty("os.version")
            );
    }
}
