package com.epitech.Service;

import com.epitech.model.SpringSecurityUser;
import com.epitech.model.User;
import com.epitech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.DigestUtils;

import java.util.UUID;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No appUser found with username '%s'.", username));
        } else {
            return new SpringSecurityUser(
                    user.getEmail(),
                    user.getPassword(),
                    user.getEmail(),
                    null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities())
            );
        }
    }

    public User createUser(String email, String password) {
        return userRepository.save(new User(email, passwordEncoder.encode(password), "ROLE_ADMIN"));
    }
}
