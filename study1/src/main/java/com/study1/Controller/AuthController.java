package com.study1.Controller;

import com.study1.Repository.ProfileRepository;
import com.study1.Repository.UserRepository;
import com.study1.entity.Profile;
import com.study1.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // 允许来自该地址的跨域请求，并携带凭证
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;

    //用户登录接口
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData, HttpSession session) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {  // 直接对比明文密码
            // 将用户信息存入 session
            session.setAttribute("user", user);

            // 返回用户角色信息，以便前端进行页面跳转
            return ResponseEntity.ok(Map.of("message", "Login successful", "role", user.getRole()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }



    // 用户注册接口
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerData) {
        String username = registerData.get("username");
        String password = registerData.get("password");

        // 检查用户是否已经存在
        if (userRepository.findByUsername(username) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        // 创建新用户对象并保存
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);  // 明文密码（建议使用加密）

        userRepository.save(newUser);

        // 创建用户信息 Profile，并将用户名写入 name 字段
        Profile profile = new Profile();
        profile.setName(username);   // 设置 name 为注册的用户名
        profileRepository.save(profile);  // 保存到 user_information 表

        return ResponseEntity.ok("User registered successfully");
    }


    // 检查当前登录用户
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
    }

    // 用户登出接口
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();  // 清除 session 中的所有数据
        return ResponseEntity.ok("Logged out");
    }
}
