package com.epitech.Service;

import com.epitech.model.SpringSecurityUser;
import com.epitech.model.User;
import com.epitech.model.UserGroups;
import com.epitech.repository.UserGroupsRepository;
import com.epitech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.UUID;

@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupsRepository userGroupsRepository;

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
        User user = new User();

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setAuthorities("ROLE_USER");

        User savedUser = userRepository.save(user);
        UserGroups userGroups = new UserGroups();

        userGroups.setUserId(savedUser.getId());
        userGroups.setGroups(new ArrayList<>());

        userGroupsRepository.save(userGroups);

        return savedUser;
    }
}
