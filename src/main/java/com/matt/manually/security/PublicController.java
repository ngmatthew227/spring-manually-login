package com.matt.manually.security;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/public")
public class PublicController {

  private HttpSessionConfig httpSessionConfig;

  public PublicController(HttpSessionConfig httpSessionConfig) {
    this.httpSessionConfig = httpSessionConfig;
  }

  @GetMapping("/show-session-attr")
  public Map<String, Object> showSessionAttr(HttpServletRequest request) {
    Map<String, Object> sessionAttr = new HashMap<>();
    Enumeration<String> attrNames = request.getSession().getAttributeNames();
    while (attrNames.hasMoreElements()) {
      String attrName = attrNames.nextElement();
      Object attrValue = request.getSession().getAttribute(attrName);
      sessionAttr.put(attrName, attrValue);
    }
    return sessionAttr;
  }

  @GetMapping("/set-session-attr")
  public String setSessionAttr(HttpServletRequest request) {
    request.getSession().setAttribute("attr1", "value1");
    request.getSession().setAttribute("attr2", "value2");
    return "Session attributes set";
  }

  @GetMapping("/get-all-existing-session")
  public List<Map<String, Object>> getAllExistingSession(HttpServletRequest request) {
    List<Map<String, Object>> allSessions = new ArrayList<>();
    List<HttpSession> activeSessions = httpSessionConfig.getActiveSessions();
    for (HttpSession session : activeSessions) {
      Map<String, Object> sessionAttr = new HashMap<>();
      Enumeration<String> attrNames = session.getAttributeNames();
      while (attrNames.hasMoreElements()) {
        String attrName = attrNames.nextElement();
        Object attrValue = session.getAttribute(attrName);
        sessionAttr.put(attrName, attrValue);
      }
      allSessions.add(sessionAttr);
    }

    return allSessions;
  }

}
