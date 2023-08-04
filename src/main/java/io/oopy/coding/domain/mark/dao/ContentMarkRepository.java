package io.oopy.coding.domain.mark.dao;

import io.oopy.coding.domain.mark.entity.ContentMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentMarkRepository extends JpaRepository<ContentMark, Long> {
    ContentMark findById(Long id);
}
