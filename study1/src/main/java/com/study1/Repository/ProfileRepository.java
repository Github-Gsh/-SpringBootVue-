package com.study1.Repository;

import com.study1.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByName(String name);  // name 字段对应用户名
}
