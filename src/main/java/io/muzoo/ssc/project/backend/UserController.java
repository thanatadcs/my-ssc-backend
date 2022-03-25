package io.muzoo.ssc.project.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @PostMapping("/api/create")
    public SimpleResponseDTO create(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // All new user if username does not already exists
        User user = userRepository.findFirstByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole("USER");
            userRepository.save(user);
            return SimpleResponseDTO
                    .builder()
                    .success(true)
                    .message(String.format("Created user %s successfully.", username))
                    .build();
        } else {
            return SimpleResponseDTO
                    .builder()
                    .success(false)
                    .message("Username already existed.")
                    .build();
        }
    }

    @Transactional
    @PostMapping("/api/delete")
    public SimpleResponseDTO delete(HttpServletRequest request) {
        String username = request.getParameter("username");
        User user = userRepository.findFirstByUsername(username);
        if (user == null) {
            return SimpleResponseDTO
                    .builder()
                    .success(false)
                    .message("User not found.")
                    .build();
        } else {
            Long count = userRepository.deleteByUsername(username);
            if (count > 0) {
                return SimpleResponseDTO
                        .builder()
                        .success(true)
                        .message("Successfully delete user.")
                        .build();
            } else {
                return SimpleResponseDTO
                        .builder()
                        .success(false)
                        .message("Fail to delete.")
                        .build();
            }
        }
    }
}
