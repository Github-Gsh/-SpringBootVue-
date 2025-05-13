package com.study1.Controller;

import com.study1.Repository.ProfileRepository;
import com.study1.entity.Profile;
import com.study1.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @Value("${upload.avatar-path}")
    private String uploadDir;

    @Value("${upload.avatar-url-prefix}")
    private String urlPrefix;

    // 获取用户信息
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

    // 更新用户信息
    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody Profile updatedProfile, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).body("未登录");

        Profile profile = profileRepository.findByName(user.getUsername());
        if (profile == null) return ResponseEntity.status(404).body("未找到信息");

        profile.setAge(updatedProfile.getAge());
        profile.setSex(updatedProfile.getSex());
        profile.setPhone(updatedProfile.getPhone());
        profile.setAddress(updatedProfile.getAddress());
        profile.setEmail(updatedProfile.getEmail());
        profile.setHeadshot(updatedProfile.getHeadshot()); // 保存头像地址

        profileRepository.save(profile);
        return ResponseEntity.ok("修改成功");
    }

    // 上传头像接口
    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("上传失败：文件为空");
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File dest = new File(uploadDir, filename);
        try {
            file.transferTo(dest);
            String url = urlPrefix + filename;
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("上传失败：" + e.getMessage());
        }
    }
}
