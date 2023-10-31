package io.oopy.coding.boardTest;

import io.oopy.coding.domain.content.repository.CategoryRepository;
import io.oopy.coding.domain.content.repository.ContentCategoryRepository;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.content.entity.Category;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.entity.ContentCategory;
import io.oopy.coding.domain.entity.User;
import io.oopy.coding.domain.mark.repository.ContentMarkRepository;
import io.oopy.coding.domain.mark.dto.CountMarkDTO;
import io.oopy.coding.domain.mark.dto.IsPressDTO;
import io.oopy.coding.domain.mark.entity.ContentMark;
import io.oopy.coding.api.mark.service.CountMarksService;
import io.oopy.coding.domain.repository.UserRepository;
import io.oopy.coding.api.mark.service.IsPressService;
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
import java.util.List;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@ExtendWith(SpringExtension.class)
public class ContentMarkTest {

    private CountMarksService countMarksService;

    private IsPressService isPressService;

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
        contentMarkRepository.save(testContentMark1);
        contentMarkRepository.save(testContentMark2);
        contentMarkRepository.save(testContentMark3);

        String jpql = "select cm from ContentMark cm join fetch cm.content c where c.id = :contentId";
        TypedQuery<ContentMark> query = em.createQuery(jpql, ContentMark.class);
        query.setParameter("contentId", testContent.getId());

        List<ContentMark> resultList = query.getResultList();

        System.out.println("=========================================");
        for (ContentMark contentMark : resultList) {
            System.out.println("contentMark.getId() = " + contentMark.getId());
            System.out.println("contentMark.getType() = " + contentMark.getType());
        }
        System.out.println("=========================================");
    }

    @DisplayName("좋아요 북마크 개수 test")
    @Test
    @Transactional
    public void countMarksTest() {
        userRepository.save(testUser);
        contentRepository.save(testContent);
        categoryRepository.save(testCategory);
        contentCategoryRepository.save(testContentCategory);
        contentMarkRepository.save(testContentMark1);
        contentMarkRepository.save(testContentMark2);
        contentMarkRepository.save(testContentMark3);

        countMarksService = new CountMarksService(contentMarkRepository);

        CountMarkDTO result = countMarksService.countMarks(testContent.getId());

        System.out.println("=========================================");
        System.out.println("result = " + result);
        System.out.println("=========================================");
    }

    @DisplayName("유저 별 press 여부 test")
    @Test
    @Transactional
    public void isPressByUserTest() {
        userRepository.save(testUser);
        contentRepository.save(testContent);
        contentMarkRepository.save(testContentMark1);
        contentMarkRepository.save(testContentMark2);

        isPressService = new IsPressService(contentMarkRepository);

        IsPressDTO result = isPressService.isPress(testContent.getId(), testUser.getId());

        System.out.println("=========================================");
        System.out.println("result = " + result);
        System.out.println("=========================================");
    }
}
