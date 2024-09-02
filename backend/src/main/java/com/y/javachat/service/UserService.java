package com.y.javachat.service;

import com.y.javachat.dto.FriendResponseDto;
import com.y.javachat.model.MyUserPrincipal;
import com.y.javachat.model.User;
import com.y.javachat.repository.FriendshipRepository;
import com.y.javachat.repository.UserRepository;
import com.y.javachat.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FriendshipRepository friendshipRepository;


    public User findByEmail(String email) {
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

    public List<FriendResponseDto> findFriendsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        return friendshipRepository.findAllByUser(user).stream()
                .map(friendship -> new FriendResponseDto(
                        friendship.getFriend().getId(),
                        friendship.getFriend().getUsername(),
                        friendship.getFriend().getEmail(),
                        friendship.getChatRoomId()
                )).toList();
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return userRepository.findByEmail(userEmail)
                .map(MyUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("userEmail " + userEmail + " 을 찾을 수 없습니다.."));
    }

}