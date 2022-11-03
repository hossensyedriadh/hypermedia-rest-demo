package io.github.hossensyedriadh.restdemo.repository.jpa;

import io.github.hossensyedriadh.restdemo.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String>, PagingAndSortingRepository<Post, String> {
    @Query(value = "select p from Post p where p.postedBy.username = ?1 order by p.postedOn asc",
            countQuery = "select count(p) from Post p where p.postedBy.username = ?1")
    Page<Post> findPostsByPostedBy(String postedBy, Pageable pageable);

    @Query(value = "select * from posts as posts_of_user where posted_by = ? order by posted_on asc;", nativeQuery = true)
    List<Post> findPostsByPostedBy(String postedBy);
}
