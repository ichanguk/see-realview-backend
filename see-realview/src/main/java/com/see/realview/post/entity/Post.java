package com.see.realview.post.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "post_tb",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id", "email", "is_active"})
        },
        indexes = {
                @Index(name = "post_url_index", columnList = "url")
        })
@SQLDelete(sql = "UPDATE post_tb SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "url", length = 100, nullable = false)
    private String url;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Builder
    public Post(Long id, String name, String url, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.createdAt = (createdAt == null? LocalDateTime.now() : createdAt);
        this.modifiedAt = (modifiedAt == null? LocalDateTime.now() : modifiedAt);
        this.isActive = true;
    }

}