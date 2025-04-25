package com.example.booking.service;

import com.example.booking.DTO.UserRegistrationDTO;
import com.example.booking.model.Role;
import com.example.booking.model.User;
import com.example.booking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.util.InputMismatchException;
import java.util.regex.Pattern;


@Service
public class UserService {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public User createUser(UserRegistrationDTO userRegistrationDTO) throws InstanceAlreadyExistsException {
        String email = userRegistrationDTO.getEmail();
        String password = userRegistrationDTO.getPassword();
        Role role = null;
        if (userRegistrationDTO.getRole().equalsIgnoreCase("USER")){
            role = Role.USER;
        } else {
            role = Role.ADMIN;
        }
        validateInput(email, password, role);

        if (userRepository.existsByEmail(email)) {
            throw new InstanceAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    private void validateInput(String email, String password, Role role) {
        if (email == null || email.trim().isEmpty()) {
            throw new InputMismatchException("Email cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new InputMismatchException("Password cannot be empty");
        }
        if (role == null) {
            throw new InputMismatchException("Role cannot be null");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InputMismatchException("Invalid email format");
        }
        if (password.length() < 8) {
            throw new InputMismatchException("Password must be at least 8 characters long");
        }
    }

    private void validatePasswordStrength(String password) {
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{" +
                MIN_PASSWORD_LENGTH + ",}$")) {
            throw new InputMismatchException(
                    "Password must contain uppercase, lowercase, numbers and be at least "
                            + MIN_PASSWORD_LENGTH + " characters long");
        }
    }

}