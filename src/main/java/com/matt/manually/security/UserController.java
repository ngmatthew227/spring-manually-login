package com.matt.manually.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

  private UserRepo userRepo;
  private AuthenticationManager authenticationManager;

  public UserController(UserRepo userRepo, AuthenticationManager authenticationManager) {
    this.userRepo = userRepo;
    this.authenticationManager = authenticationManager;
  }

  @GetMapping("/login-as/{username}")
  public String loginAs(@PathVariable String username, HttpServletRequest request) {
    if (username.equals("app_admin")) {
      User user = userRepo.findByUsername(username);
      Authentication authentication = new PreAuthenticatedAuthenticationToken(user.getUsername(), user.getPassword(),
          AuthorityUtils.createAuthorityList(user.getRole()));
      Authentication authResult = authenticationManager.authenticate(authentication);
      SecurityContextHolder.getContext().setAuthentication(authResult);
      request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

      System.out.println("Logged in as admin");
      return "Login as admin";
    }
    return "user not found";
  }

  @GetMapping("/get-auth")
  public String getAuth(@AuthenticationPrincipal UserDetails userDetails) {
    System.out.println("User details: " + userDetails);
    if (userDetails != null) {
      String username = userDetails.getUsername();
      List<String> authorities = userDetails.getAuthorities().stream()
          .map(Object::toString)
          .collect(Collectors.toList());

      // Log the username and authorities
      System.out.println("Username: " + username);
      System.out.println("Authorities: " + authorities);

      return "Logged user details and authorities. Check console for details.";
    } else {
      return "User details not available";
    }
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/admin")
  public String admin(@AuthenticationPrincipal UserDetails userDetails) {
    System.out.println("User details: " + userDetails);
    return "Admin page";
  }

}
