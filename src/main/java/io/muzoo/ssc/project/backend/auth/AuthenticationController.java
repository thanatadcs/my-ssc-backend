package io.muzoo.ssc.project.backend.auth;

import io.muzoo.ssc.project.backend.SimpleResponseDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {

    @PostMapping("/api/login")
    public SimpleResponseDTO login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            // Check if there is a current user logged in, if so log that user out first
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && principal instanceof org.springframework.security.core.userdetails.User) {
                request.logout();
            }
            request.login(username, password);
            return SimpleResponseDTO
                            .builder()
                            .success(true)
                            .message("You are logged in successfully.")
                            .build();
        } catch (ServletException e) {
            return SimpleResponseDTO
                            .builder()
                            .success(false)
                            .message("Failed to login.")
                            .build();
        }
    }

    @GetMapping("/api/logout")
    public SimpleResponseDTO logout(HttpServletRequest request) {
        try {
            // Check if there is a current user logged in, if so log that user out first
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && principal instanceof org.springframework.security.core.userdetails.User) {
                request.logout();
                return SimpleResponseDTO
                        .builder()
                        .success(true)
                        .message("You are logged out successfully.")
                        .build();
            } else {
                return SimpleResponseDTO
                        .builder()
                        .success(false)
                        .message("Can't logged out, you are not logged in.")
                        .build();
            }
        } catch (ServletException e) {
            return SimpleResponseDTO
                    .builder()
                    .success(false)
                    .message("Failed to logout.")
                    .build();
        }
    }
}
