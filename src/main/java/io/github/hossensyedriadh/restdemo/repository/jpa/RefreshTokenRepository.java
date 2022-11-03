package io.github.hossensyedriadh.restdemo.repository.jpa;

import io.github.hossensyedriadh.restdemo.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}
