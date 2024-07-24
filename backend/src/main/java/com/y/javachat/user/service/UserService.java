package com.y.javachat.user.service;

import com.y.javachat.system.exception.ObjectNotFoundException;
import com.y.javachat.user.model.MyUserPrincipal;
import com.y.javachat.user.repository.UserRepository;
import com.y.javachat.user.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("user", email));
    }

    public User save(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    public User update(Long userId, User update) {
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        oldUser.setUsername(update.getUsername());
        oldUser.setEmail(update.getEmail());
        oldUser.setEnabled(update.isEnabled());
        oldUser.setRoles(update.getRoles());
        return userRepository.save(oldUser);
    }

    public void delete(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        userRepository.deleteById(userId);
    }


    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return userRepository.findByEmail(userEmail)
                .map(MyUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("userEmail " + userEmail + " 을 찾을 수 없습니다.."));
    }
}
