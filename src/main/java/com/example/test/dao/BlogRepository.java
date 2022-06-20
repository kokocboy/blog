package com.example.test.dao;

import com.example.test.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.plaf.basic.BasicListUI;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long> {

    Page<Blog> findAll(Specification<Blog> blogSpecification, Pageable pageable);

    @Query("select b from Blog b")
    List<Blog> findTopBy(Pageable pageable);

    Page<Blog> findAll(Specification<Blog> blogSpecification);
}
