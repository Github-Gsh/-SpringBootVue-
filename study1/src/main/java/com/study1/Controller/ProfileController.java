package com.study1.Controller;

import com.study1.Repository.ProfileRepository;
import com.study1.entity.Profile;
import com.study1.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping
    public ResponseEntity<?> getProfile(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            String username = user.getUsername();
            Profile profile = profileRepository.findByName(username);
            if (profile != null) {
                return ResponseEntity.ok(profile);
            } else {
                return ResponseEntity.status(404).body("Profile not found");
            }
        } else {
            return ResponseEntity.status(401).body("Not logged in");
        }
    }
}
