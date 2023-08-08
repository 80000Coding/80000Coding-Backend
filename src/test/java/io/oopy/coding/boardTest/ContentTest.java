package io.oopy.coding.boardTest;

import io.oopy.coding.domain.content.dao.CategoryRepository;
import io.oopy.coding.domain.content.dao.ContentCategoryRepository;
import io.oopy.coding.domain.content.dao.ContentRepository;
import io.oopy.coding.domain.content.dto.ContentDetailDTO;
import io.oopy.coding.domain.content.entity.Category;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.entity.ContentCategory;
import io.oopy.coding.domain.content.service.ContentService;
import io.oopy.coding.domain.entity.User;
import io.oopy.coding.domain.mark.dao.ContentMarkRepository;
import io.oopy.coding.domain.mark.entity.ContentMark;
import io.oopy.coding.domain.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@ExtendWith(SpringExtension.class)
public class ContentTest {

    private ContentService contentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private ContentCategoryRepository contentCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ContentMarkRepository contentMarkRepository;

    @Autowired
    private EntityManager em;

    private User testUser;
    private Content testContent;
    private Category testCategory;
    private ContentCategory testContentCategory;
    private ContentMark testContentMark1;
    private ContentMark testContentMark2;
    private ContentMark testContentMark3;

    @BeforeEach
    public void setUp() {

        /**
         * 테스트 user 생성
         */
        testUser = User.builder()
                .name("test user")
                .githubId(123123213)
                .profileImageUrl("test.com")
                .bio("test bio")
                .role("general")
                .build();

        /**
         * 테스트 content 생성
         */
        testContent = Content.builder()
                .type("post")
                .title("test title")
                .body("test body")
                .repoName(null)
                .repoOwner(null)
                .views(42L)
                .deleteAt(LocalDateTime.now())
                .user(testUser)
                .build();

        /**
         * 테스트 category 생성
         */
        testCategory = Category.builder()
                .name("test category")
                .color("red")
                .build();

        /**
         * 테스트 contentCategory 생성
         */
        testContentCategory = ContentCategory.builder()
                .content(testContent)
                .category(testCategory)
                .type("test type")
                .build();

        /**
         * 테스트 ContentMark 생성
         */
        testContentMark1 = ContentMark.builder()
                .content(testContent)
                .user(testUser)
                .type("like")
                .build();

        testContentMark2 = ContentMark.builder()
                .content(testContent)
                .user(testUser)
                .type("bookmark")
                .build();

        testContentMark3 = ContentMark.builder()
                .content(testContent)
                .user(testUser)
                .type("like")
                .build();
    }

    /**
     * jpql raw Qurey test field
     */
    @DisplayName("jpql Query test")
    @Test
    @Transactional
    public void jpqlTest() {
        userRepository.save(testUser);
        contentRepository.save(testContent);
        categoryRepository.save(testCategory);
        contentCategoryRepository.save(testContentCategory);

        String jpql = "select cat from Content c join c.contentCategories cc join cc.category cat where c.id = :contentId";
        TypedQuery<Category> query = em.createQuery(jpql, Category.class);
        query.setParameter("contentId", testContent.getId());

        Category result = query.getSingleResult();

        System.out.println("=========================================");
        System.out.println("result.getName() = " + result.getColor());
        System.out.println("=========================================");
    }

    @DisplayName("게시판 상세 정보 test")
    @Test
    @Transactional
    public void getContenDetails() {
        userRepository.save(testUser);
        contentRepository.save(testContent);
        categoryRepository.save(testCategory);
        contentCategoryRepository.save(testContentCategory);

        contentService = new ContentService(contentRepository, userRepository, contentCategoryRepository, categoryRepository);

        ContentDetailDTO test = contentService.getContentDetails(testContent.getId());

        assertEquals(testContent.getId(), test.getContentId());
        assertEquals(testContent.getTitle(), test.getContentTitle());
        assertEquals(testContent.getBody(), test.getContentBody());
        assertEquals(testContent.getType(), test.getContentType());
        assertEquals(testUser.getName(), test.getUserName());
        assertEquals(testUser.getProfileImageUrl(), test.getUserProfileImageUrl());
        assertEquals(testContentCategory.getId(), test.getContentCategoryCategoryId());
        assertEquals(testCategory.getColor(), test.getCategoryColor());
        assertEquals(testCategory.getName(), test.getCategoryName());

        System.out.println("=========================================");
        System.out.println("test = " + test);
        System.out.println("=========================================");
    }

}
