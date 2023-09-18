package io.oopy.coding.content.service;

import io.oopy.coding.content.dto.GetContentDTO;
import io.oopy.coding.domain.content.entity.Category;
import io.oopy.coding.domain.content.entity.Content;
import io.oopy.coding.domain.content.entity.ContentCategory;
import io.oopy.coding.domain.content.repository.ContentRepository;
import io.oopy.coding.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetContent {
    private final ContentRepository contentRepository;

    @Transactional
    public GetContentDTO.Res getContent(GetContentDTO.Req request) {

        Content content = contentRepository.findById(request.getContent_id()).orElse(null);

        if (content == null) {
            GetContentDTO.Res.ContentEmpty failureData = GetContentDTO.Res.ContentEmpty.builder()
                    .content_id(request.getContent_id())
                    .build();

            return GetContentDTO.Res.builder()
                    .status("fail")
                    .data(failureData)
                    .message("Content does not exist")
                    .build();
        } else if (content.getDeleteAt() != null) {
            GetContentDTO.Res.DeletedContent failureData = GetContentDTO.Res.DeletedContent.builder()
                    .content_id(request.getContent_id())
                    .deleted_at(content.getDeleteAt())
                    .build();

            return GetContentDTO.Res.builder()
                    .status("fail")
                    .data(failureData)
                    .message("Deleted Content")
                    .build();
        }

        User user = content.getUser();
        if (user == null) {
            GetContentDTO.Res.UserEmpty failureData = GetContentDTO.Res.UserEmpty.builder()
                    .user_id(user.getId())
                    .build();

            return GetContentDTO.Res.builder()
                    .status("fail")
                    .data(failureData)
                    .message("User does not exist")
                    .build();
        }

        ContentCategory contentCategory = contentRepository.findContentCategoryById(request.getContent_id());
//        if (contentCategory == null) {
//
//        }

        Category category = contentRepository.findCategoryById(request.getContent_id());
//        if (category == null) {
//
//        }


        updateViews(request.getContent_id());

        GetContentDTO.Res result = getResponse(content, user, category, contentCategory);

        return result;
    }

    private GetContentDTO.Res getResponse(Content content, User user, Category category, ContentCategory contentCategory) {

        GetContentDTO.Res.Content resContent;
        GetContentDTO.Res.User resUser;
        GetContentDTO.Res.ContentCategory resContentCategory;
        GetContentDTO.Res.Category resCategory;

        resContent = GetContentDTO.Res.Content.builder()
                .id(content.getId())
                .title(content.getTitle())
                .body(content.getBody())
                .type(content.getType())
                .repo_name(content.getRepoName())
                .repo_owner(content.getRepoOwner())
                .views(content.getViews())
                .complete(content.isComplete())
                .content_image_url(content.getContentImageUrl())
                .created_at(content.getCreatedAt())
                .updated_at(content.getUpdatedAt())
                .deleted_at(content.getDeleteAt())
                .build();

        resUser = GetContentDTO.Res.User.builder()
                .name(user.getName())
                .profile_image_url(user.getProfileImageUrl())
                .build();

        if (contentCategory != null) {
            resContentCategory = GetContentDTO.Res.ContentCategory.builder()
                    .category_id(contentCategory.getId())
                    .build();
        } else {
            resContentCategory = null;
        }

        if (category != null) {
            resCategory = GetContentDTO.Res.Category.builder()
                    .name(category.getName())
                    .color(category.getColor())
                    .build();
        } else {
            resCategory = null;
        }

        GetContentDTO.Res.Data resData = GetContentDTO.Res.Data.builder()
                .content(resContent)
                .user(resUser)
                .category(resCategory)
                .contentCategory(resContentCategory)
                .build();

        GetContentDTO.Res response = GetContentDTO.Res.builder()
                .status("success")
                .data(resData)
                .build();

        return response;
    }

    @Transactional
    public int updateViews(Long contentId) {
        return contentRepository.incrementViewsById(contentId);
    }


}
