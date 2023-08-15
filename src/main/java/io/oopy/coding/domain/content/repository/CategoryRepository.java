package io.oopy.coding.domain.content.repository;

import io.oopy.coding.domain.content.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
