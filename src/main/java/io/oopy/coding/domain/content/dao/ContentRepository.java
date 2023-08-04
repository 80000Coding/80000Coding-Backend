package io.oopy.coding.domain.content.dao;

import io.oopy.coding.domain.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    Content findByIdAndType(Long contentId, String type);
}
