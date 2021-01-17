package com.eventor.restController;

import com.eventor.model.Role;
import com.eventor.model.User;
import com.eventor.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Set;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockCustomUserSecurityContextFactory.class, setupBefore = TestExecutionEvent.TEST_EXECUTION)
@interface MockCustomUser {
    String username() default "rayb";
    String password() default "a12347";
}
public class MockCustomUserSecurityContextFactory implements WithSecurityContextFactory<MockCustomUser> {
    @Autowired
    PasswordEncoder encoder;

    @Override
    public SecurityContext createSecurityContext(MockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Set<Role> roles = new HashSet<>();
        Role role = new Role("USER");
        role.setId(0);
        roles.add(role);
        User basicUser = new User();
        basicUser.setId(1);
        basicUser.setUsername("rayb");
        basicUser.setPassword(encoder.encode("a12347"));
        basicUser.setEmail("rayb@gmail.com");
        basicUser.setRoles(roles);
        UserDetails u = UserDetailsImpl.build(basicUser);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(u, "a12347", u.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
