package com.harmoniq.backend.auth.service.serviceImp;

import com.harmoniq.backend.common.exception.ResourceNotFoundException;
import com.harmoniq.backend.user.entity.User;
import com.harmoniq.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCacheService {

    private final UserRepository userRepository;

    @Cacheable(value = "users", key = "#email")
    public User getUserByEmail(String email) {
        System.out.println("🔥 DB HIT: fetching user from DB");

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
