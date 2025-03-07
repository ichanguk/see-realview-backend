package com.see.realview.user.service;

import com.see.realview.core.exception.BadRequestException;
import com.see.realview.core.exception.BaseException;
import com.see.realview.user.dto.request.LoginRequest;
import com.see.realview.user.dto.request.RegisterRequest;
import com.see.realview.user.dto.response.TokenPair;
import com.see.realview.user.entity.UserAccount;
import com.see.realview.user.repository.UserAccountJPARepository;
import com.see.realview.user.repository.UserAccountRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserAccountRepositoryImpl userAccountRepository;
    private final UserAccountJPARepository userAccountJPARepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void register(RegisterRequest request) {
        checkEmailAlreadyExist(request);
        checkEqualPassword(request);

        UserAccount userAccount = UserAccount.builder()
                .email(request.email())
                .name(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userAccountJPARepository.save(userAccount);
    }

    @Override
    public TokenPair login(LoginRequest request) {
        UserAccount userAccount = findUserAccountByEmail(request);

        checkPassword(request, userAccount);

        return new TokenPair("TODO", "TODO");
    }

    private void checkEmailAlreadyExist(RegisterRequest request) {
        userAccountRepository.findUserAccountByEmail(request.email())
                .ifPresent(user -> {
                    throw new BadRequestException(BaseException.EMAIL_ALREADY_EXIST);
                });
    }

    private static void checkEqualPassword(RegisterRequest request) {
        if (!request.password().equals(request.password2())) {
            throw new BadRequestException(BaseException.PASSWORD_NOT_EQUALS);
        }
    }

    private void checkPassword(LoginRequest request, UserAccount userAccount) {
        if (!passwordEncoder.matches(request.password(), userAccount.getPassword())) {
            throw new BadRequestException(BaseException.INVALID_PASSWORD);
        }
    }

    private UserAccount findUserAccountByEmail(LoginRequest request) {
        return userAccountRepository.findUserAccountByEmail(request.email())
                .orElseThrow(() -> new BadRequestException(BaseException.EMAIL_NOT_FOUND));
    }
}
