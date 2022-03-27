package io.muzoo.ssc.project.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
            user.setTimestamp(0);
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
                    .message(String.format("Username %s already exists", username))
                    .build();
        }
    }

    @Transactional
    @DeleteMapping("/api/delete")
    public SimpleResponseDTO delete(HttpServletRequest request) {
        String username = request.getParameter("username");
        User user = userRepository.findFirstByUsername(username);
        if (user == null) {
            return SimpleResponseDTO
                    .builder()
                    .success(false)
                    .message(String.format("Can't delete, username %s not found", username))
                    .build();
        } else {
            int count = userRepository.deleteByUsername(username);
            if (count > 0) {
                return SimpleResponseDTO
                        .builder()
                        .success(true)
                        .message(String.format("Successfully delete user %s", username))
                        .build();
            } else {
                return SimpleResponseDTO
                        .builder()
                        .success(false)
                        .message(String.format("Failed delete user %s", username))
                        .build();
            }
        }
    }

    @Transactional
    @PutMapping("/api/update")
    public SimpleResponseDTO update(HttpServletRequest request) {
        String username = request.getParameter("username");
        float timestamp = Float.parseFloat(request.getParameter("timestamp"));

        User user = userRepository.findFirstByUsername(username);
        if (user == null) {
            return SimpleResponseDTO
                    .builder()
                    .success(false)
                    .message(String.format("Can't update username %s not found", username))
                    .build();
        } else {
            int count = userRepository.updateTimestampByUsername(username, timestamp);
            if (count > 0) {
                return SimpleResponseDTO
                        .builder()
                        .success(true)
                        .message(String.format("Successfully update timestamp of %s", username))
                        .build();
            } else {
                return SimpleResponseDTO
                        .builder()
                        .success(false)
                        .message(String.format("Failed update timestamp of %s", username))
                        .build();
            }
        }
    }

}
