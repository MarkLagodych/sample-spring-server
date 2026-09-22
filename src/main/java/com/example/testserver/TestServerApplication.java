package com.example.testserver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

@SpringBootApplication
@Controller 
@ControllerAdvice 
public class TestServerApplication {
    
    static private final LocalDateTime startupTime = LocalDateTime.now();

    static private final String env = System.getenv().entrySet().stream()
            .map(e -> e.getKey() + "=" + e.getValue())
            .sorted()
            .collect(Collectors.joining("\n"));

    public static void main(String[] args) {
        SpringApplication.run(TestServerApplication.class, args);
    }

    final AtomicLong visitCount = new AtomicLong(0);

    @GetMapping({"/", "/index.html"})
    @ResponseBody
    String index(HttpServletRequest request) {
        var now = LocalDateTime.now();
        var uptime = java.time.Duration.between(startupTime, now);

        return """
            <!DOCTYPE html>
            <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Test Server</title>
                </head>
                <body>
                    <h1>Hello world!</h1>
                    <b>Visit count:</b> %s<br/>
                    <b>Client IP:</b> %s<br/>
                    <b>Server IP:</b> %s<br/>
                    <hr/>
                    <b>Current time:</b> %s<br/>
                    <b>Startup time:</b> %s (uptime %s)<br/>
                    <hr/>
                    <b>Spring Framework:</b> %s<br/>
                    <b>JRE:</b> %s %s in <code>%s</code><br/>
                    <b>JVM:</b> %s %s %s<br/>
                    <b>OS:</b> %s %s %s<br/>
                    <b>Memory:</b> %.2f MiB preallocated (%.2f MiB free) / %.2f MiB max<br/>
                    <b>Processors:</b> %s<br/>
                    <b>Environment variables:</b><br/>
                    <pre>%s</pre>
                </body>
            </html>
            """.formatted(
                visitCount.incrementAndGet(),
                request.getRemoteAddr(),
                request.getLocalAddr(),
                // Time
                now,
                startupTime,
                uptime,
                // Versions
                SpringVersion.getVersion(),
                // Java Runtime
                System.getProperty("java.vendor"),
                System.getProperty("java.version"),
                System.getProperty("java.home"),
                // Java VM
                System.getProperty("java.vm.vendor"),
                System.getProperty("java.vm.name"),
                System.getProperty("java.vm.version"),
                // Operating System
                System.getProperty("os.name"),
                System.getProperty("os.version"),
                System.getProperty("os.arch"),
                (float) Runtime.getRuntime().totalMemory() / 1024.0 / 1024.0,
                (float) Runtime.getRuntime().freeMemory() / 1024.0 / 1024.0,
                (float) Runtime.getRuntime().maxMemory() / 1024.0 / 1024.0,
                Runtime.getRuntime().availableProcessors(),
                env
            );
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    String handleException(Exception e) {
        StringWriter trace = new StringWriter();
        e.printStackTrace(new PrintWriter(trace));

        return """
            <!DOCTYPE html>
            <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Test Server - Error</title>
                </head>
                <body>
                    <h1>%s occurred!</h1>
                    <pre>%s</pre>
                </body>
            </html>
            """.formatted(
                e.getClass().getSimpleName(),
                trace.toString()
            );
    }
}
