package io.github.hossensyedriadh.restdemo.repository.jpa;

import io.github.hossensyedriadh.restdemo.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String>, PagingAndSortingRepository<Comment, String> {
    @Query(value = "select * from comments where post_ref = ? order by comment_on;", nativeQuery = true)
    List<Comment> findCommentsOnPost(String postId);
}
