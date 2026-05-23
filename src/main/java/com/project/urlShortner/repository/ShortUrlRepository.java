package com.project.urlShortner.repository;

import com.project.urlShortner.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByShortCode(String shortCode);
    @Transactional
    @Modifying
    @Query("""
       UPDATE ShortUrl s
       SET s.clickCount = s.clickCount + 1
       WHERE s.shortCode = :shortCode
       """)
    int incrementClickCount(String shortCode);
    List<ShortUrl> findByUserEmail(
            String email
    );
}
