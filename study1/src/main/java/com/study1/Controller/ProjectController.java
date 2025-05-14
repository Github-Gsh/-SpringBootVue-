package com.study1.Controller;

import com.study1.Repository.ProjectRepository;
import com.study1.entity.Project;
import com.study1.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProjectController {

    private static final Path BASE_UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads");

    @Autowired
    private ProjectRepository projectRepo;

    // 获取当前登录用户对象
    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    // 获取当前用户名
    private String getCurrentUsername(HttpSession session) {
        User user = getCurrentUser(session);
        return user != null ? user.getUsername() : null;
    }

    // ✅ 获取当前用户项目（管理员返回所有项目）
    @GetMapping("/projects")
    public List<Project> getProjects(HttpSession session) {
        User user = getCurrentUser(session);

        if (user == null) {
            return Collections.emptyList();
        }

        if ("0".equals(user.getRole())) {
            return projectRepo.findAll();
        } else {
            return projectRepo.findByName(user.getUsername());
        }
    }

    // ✅ 管理员获取全部项目（独立接口）
    @GetMapping("/admin/projects")
    public ResponseEntity<?> getAllProjectsForAdmin(HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null || !"0".equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("无权限访问");
        }
        return ResponseEntity.ok(projectRepo.findAll());
    }

    // ✅ 管理员审批项目接口
    @PutMapping("/admin/projects/{pid}/approve")
    public ResponseEntity<?> approveProject(@PathVariable Long pid, HttpSession session) {
        User user = getCurrentUser(session);
        if (user == null || !"0".equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("无权限操作");
        }

        Optional<Project> optional = projectRepo.findById(pid);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Project project = optional.get();
        project.setStatus("已审批");
        projectRepo.save(project);
        return ResponseEntity.ok("项目已审批");
    }

    // ✅ 创建项目
    @PostMapping("/projects")
    public ResponseEntity<?> createProject(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpSession session) {

        String username = getCurrentUsername(session);
        if (username == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");

        Project project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setName(username);
        project.setStatus("待审批"); // 默认状态

        if (file != null && !file.isEmpty()) {
            try {
                String filePath = saveFile(file);
                project.setFilePath("/api/files/" + filePath);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件保存失败");
            }
        }

        projectRepo.save(project);
        return ResponseEntity.ok("项目创建成功");
    }

    // ✅ 更新项目
    @PutMapping("/projects/{pid}")
    public ResponseEntity<?> updateProject(
            @PathVariable Long pid,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpSession session) {

        Optional<Project> optional = projectRepo.findById(pid);
        if (optional.isEmpty())
            return ResponseEntity.notFound().build();

        Project project = optional.get();
        if (!Objects.equals(project.getName(), getCurrentUsername(session))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("无权限修改此项目");
        }

        project.setTitle(title);
        project.setDescription(description);

        if (file != null && !file.isEmpty()) {
            try {
                String filePath = saveFile(file);
                project.setFilePath("/api/files/" + filePath);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件保存失败");
            }
        }

        projectRepo.save(project);
        return ResponseEntity.ok("项目更新成功");
    }

    // ✅ 删除项目
    @DeleteMapping("/projects/{pid}")
    public ResponseEntity<?> deleteProject(@PathVariable Long pid, HttpSession session) {
        Optional<Project> optional = projectRepo.findById(pid);
        if (optional.isEmpty())
            return ResponseEntity.notFound().build();

        Project project = optional.get();
        if (!Objects.equals(project.getName(), getCurrentUsername(session))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("无权限删除此项目");
        }

        projectRepo.delete(project);
        return ResponseEntity.ok("删除成功");
    }

    // ✅ 文件上传辅助函数
    private String saveFile(MultipartFile file) throws IOException {
        Files.createDirectories(BASE_UPLOAD_DIR);
        String filename = UUID.randomUUID() + "-" + StringUtils.cleanPath(file.getOriginalFilename());
        Path targetPath = BASE_UPLOAD_DIR.resolve(filename);
        file.transferTo(targetPath.toFile());
        System.out.println("✅ 文件已保存至：" + targetPath.toAbsolutePath());
        return filename;
    }

    // ✅ 文件下载接口
    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<org.springframework.core.io.Resource> serveFile(@PathVariable String filename) {
        try {
            Path filePath = BASE_UPLOAD_DIR.resolve(filename).normalize();
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
