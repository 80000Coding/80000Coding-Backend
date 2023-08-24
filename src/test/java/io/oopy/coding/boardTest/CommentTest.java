package io.oopy.coding.boardTest;

import io.oopy.coding.comment.service.CreateComment;
import io.oopy.coding.comment.service.DeleteComment;
import io.oopy.coding.comment.service.UpdateComment;
import io.oopy.coding.domain.comment.dto.CreateCommentDTO;
import io.oopy.coding.domain.comment.repository.CommentRepository;
import io.oopy.coding.domain.comment.dto.GetCommentDTO;
import io.oopy.coding.domain.comment.entity.Comment;
import io.oopy.coding.comment.service.GetComment;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.entity.User;
import io.oopy.coding.domain.repository.UserRepository;
import jakarta.persistence.EntityManager;
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
public class CommentTest {

    private GetComment getCommentService;
    private CreateComment createCommentService;
    private UpdateComment updateCommentService;
    private DeleteComment deleteCommentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager em;

    private User testUser;
    private Content testContent;
    private Comment testComment1;
    private Comment testComment2;
    private Comment testComment3;

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
         * 테스트 comment 생성
         */
        testComment1 = Comment.builder()
                .content(testContent)
                .user(testUser)
                .commentBody("test1")
                .parentId(1L)
                .build();

        testComment2 = Comment.builder()
                .content(testContent)
                .user(testUser)
                .commentBody("test2")
                .parentId(1L)
                .build();

        testComment3 = Comment.builder()
                .content(testContent)
                .user(testUser)
                .commentBody("test3")
                .parentId(1L)
                .build();
    }

    @DisplayName("댓글 정보 확인 test")
    @Test
    @Transactional
    public void getCommentTest() {
        userRepository.save(testUser);
        contentRepository.save(testContent);
        commentRepository.save(testComment1);
        commentRepository.save(testComment2);
        commentRepository.save(testComment3);

        getCommentService = new GetComment(commentRepository);

        List<GetCommentDTO> result = getCommentService.getComments(testContent.getId());

        System.out.println("=========================================");
        for (GetCommentDTO getCommentDTO : result) {
            System.out.println("getCommentDTO = " + getCommentDTO);
        }
        System.out.println("=========================================");
    }

    /**
     * user token으로 댓글을 생성한 user가 어떤 user인지 파악을 하지 못하는 문제로 인해 진행안되는중
     */
//    @DisplayName("댓글 생성 확인 test")
//    @Test
//    @Transactional
//    public void postCommentTest() {
//        userRepository.save(testUser);
//        contentRepository.save(testContent);
//
//        createCommentService = new CreateComment(contentRepository, userRepository, commentRepository);
//        CreateCommentDTO result = createCommentService.createComment(testContent.getId(), "This is test Comment", null);
//        System.out.println("=========================================");
//        System.out.println("result = " + result);
//        System.out.println("=========================================");
//    }

    @DisplayName("댓글 수정 확인 test")
    @Test
    @Transactional
    public void patchCommentTest() {
        userRepository.save(testUser);
        contentRepository.save(testContent);
        commentRepository.save(testComment1);
        commentRepository.save(testComment2);
        commentRepository.save(testComment3);

        updateCommentService = new UpdateComment(commentRepository);
        getCommentService = new GetComment(commentRepository);
        updateCommentService.updateComment(testComment1.getId(), "update test body!!");
        List<GetCommentDTO> result = getCommentService.getComments(testContent.getId());
        System.out.println("=========================================");
        for (GetCommentDTO getCommentDTO : result) {
            System.out.println("getCommentDTO = " + getCommentDTO);
        }
        System.out.println("=========================================");
    }

    @DisplayName("댓글 삭제 확인 test")
    @Test
    @Transactional
    public void deleteCommentTest() {
        userRepository.save(testUser);
        contentRepository.save(testContent);
        commentRepository.save(testComment1);
        commentRepository.save(testComment2);
        commentRepository.save(testComment3);

        deleteCommentService = new DeleteComment(commentRepository);
        getCommentService = new GetComment(commentRepository);
        deleteCommentService.deleteComment(testComment1.getId());
        List<GetCommentDTO> result = getCommentService.getComments(testContent.getId());
        System.out.println("=========================================");
        for (GetCommentDTO getCommentDTO : result) {
            System.out.println("getCommentDTO = " + getCommentDTO);
        }
        System.out.println("=========================================");
    }
}
