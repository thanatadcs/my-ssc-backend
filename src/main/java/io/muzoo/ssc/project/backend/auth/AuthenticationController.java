package io.muzoo.ssc.project.backend.auth;

import io.muzoo.ssc.project.backend.SimpleResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {

    @GetMapping("/api/test")
    public String test() {
        return "If this message is shown, it means login is successful because we didn't set to permit this path.";
    }

    @PostMapping("/api/login")
    public SimpleResponseDTO login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            request.login(username, password);
            return SimpleResponseDTO
                            .builder()
                            .success(true)
                            .message("You are logged in successfully.")
                            .build();
        } catch (ServletException e) {
            return SimpleResponseDTO
                            .builder()
                            .success(true)
                            .message("Incorrect username or password.")
                            .build();
        }
    }

    @GetMapping("/api/logout")
    public SimpleResponseDTO logout(HttpServletRequest request) {
        try {
            request.logout();
            return SimpleResponseDTO
                            .builder()
                            .success(true)
                            .message("You are successfully logged out")
                            .build();
        } catch (ServletException e) {
            return SimpleResponseDTO
                            .builder()
                            .success(true)
                            .message("Failed to log you out")
                            .build();
        }
    }
}
