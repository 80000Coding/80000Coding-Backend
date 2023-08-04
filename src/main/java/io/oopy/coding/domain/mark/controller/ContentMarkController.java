package io.oopy.coding.domain.mark.controller;

import io.oopy.coding.domain.mark.service.ContentMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;



@Controller
public class ContentMarkController {
    private final ContentMarkService contentMarkService;

    @Autowired
    public ContentMarkController(ContentMarkService contentMarkService) {
        this.contentMarkService = contentMarkService;
    }


}
