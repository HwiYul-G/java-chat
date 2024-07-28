package com.y.javachat.app.service;

import com.y.javachat.app.dto.FriendResponseDto;
import com.y.javachat.app.model.FriendRequest;
import com.y.javachat.app.model.Friendship;
import com.y.javachat.app.model.MyUserPrincipal;
import com.y.javachat.app.model.User;
import com.y.javachat.app.repository.FriendRequestRepository;
import com.y.javachat.app.repository.FriendshipRepository;
import com.y.javachat.app.repository.UserRepository;
import com.y.javachat.chat.event.PersonalChatRoomGeneratedEvent;
import com.y.javachat.system.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final FriendshipRepository friendshipRepository;
    private final FriendRequestRepository friendRequestRepository;

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

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
        List<Friendship> friendships = friendshipRepository.findAllByUserId(userId);
        return friendships.stream().map(friendship -> {
            User friend = userRepository.findById(friendship.getFriendId())
                    .orElseThrow(() -> new ObjectNotFoundException("friendId", friendship.getFriendId()));
            return new FriendResponseDto(friend.getId(), friend.getUsername(), friend.getEmail(), friendship.getChatRoomId());
        }).toList();
    }

    public List<FriendRequest> findFriendRequestsByUserId(Long userId) {
        return friendRequestRepository.findAllByReceiverId(userId);
    }

    public void createFriendRequest(Long userId, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        User friend = userRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("user", email));

        FriendRequest friendRequest = FriendRequest.builder()
                .createdAt(LocalDateTime.now())
                .senderId(userId)
                .receiverId(friend.getId())
                .senderName(user.getUsername())
                .build();

        friendRequestRepository.save(friendRequest);
    }

    public void acceptFriendRequest(Long friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository
                .findById(friendRequestId)
                .orElseThrow(() -> new ObjectNotFoundException("friend request", friendRequestId));

        LocalDateTime now = LocalDateTime.now();
        Friendship friendship1 = Friendship.builder()
                .userId(friendRequest.getSenderId())
                .friendId(friendRequest.getReceiverId())
                .createdAt(now)
                .build();
        Friendship friendship2 = Friendship.builder()
                .userId(friendRequest.getReceiverId())
                .friendId(friendRequest.getSenderId())
                .createdAt(now)
                .build();

        friendRequestRepository.deleteById(friendRequest.getId());
        friendshipRepository.save(friendship1);
        friendshipRepository.save(friendship2);
    }

    public void declineFriendRequest(Long friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository
                .findById(friendRequestId)
                .orElseThrow(() -> new ObjectNotFoundException("friendRequest", friendRequestId));
        friendRequestRepository.deleteById(friendRequest.getId());
    }


    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return userRepository.findByEmail(userEmail)
                .map(MyUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("userEmail " + userEmail + " 을 찾을 수 없습니다.."));
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void handlePersonalChatRoomGeneratedEvent(PersonalChatRoomGeneratedEvent event){
        Friendship friendship1 = friendshipRepository
                .findByUserIdAndAndFriendId(event.userId1(), event.userId2())
                .orElseThrow(() -> new ObjectNotFoundException("friendship user1 and user2", event.userId1() + event.userId2()));
        Friendship friendship2 = friendshipRepository
                .findByUserIdAndAndFriendId(event.userId2(), event.userId1())
                .orElseThrow(() -> new ObjectNotFoundException("friendship user2 and user1", event.userId2() + event.userId1()));

        friendship1.setChatRoomId(event.roomId());
        friendship2.setChatRoomId(event.roomId());

        friendshipRepository.save(friendship1);
        friendshipRepository.save(friendship2);
    }
}
