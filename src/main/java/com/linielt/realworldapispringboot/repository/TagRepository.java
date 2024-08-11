package com.linielt.realworldapispringboot.repository;

import com.linielt.realworldapispringboot.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    boolean existsTagByValue(String value);
    Tag getTagByValue(String value);
}
