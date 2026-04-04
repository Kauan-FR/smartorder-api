package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.QuickReplyMapper;
import com.kauanferreira.smartorder.dto.request.QuickReplyRequest;
import com.kauanferreira.smartorder.dto.response.QuickReplyResponse;
import com.kauanferreira.smartorder.entity.QuickReply;
import com.kauanferreira.smartorder.services.interfaces.QuickReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing vendor quick reply templates.
 *
 * <p>All endpoints require authentication and operate on the
 * quick replies of the currently authenticated vendor.
 * Vendors can only access and modify their own quick replies.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see QuickReplyService
 * @see QuickReplyMapper
 */
@RestController
@RequestMapping("/api/quick-replies")
@RequiredArgsConstructor
@Tag(name = "Quick Replies", description = "Endpoints for managing vendor quick reply templates")
public class QuickReplyController {

    private final QuickReplyService quickReplyService;

    /**
     * Creates a new quick reply for the authenticated vendor.
     *
     * @param authentication the authenticated user's security context
     * @param request        the quick reply data (title and message)
     * @return HTTP 201 with the created quick reply and location header
     */
    @Operation(summary = "Create a quick reply", description = "Creates a new quick reply template for the authenticated vendor.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Quick reply created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<QuickReplyResponse> create(Authentication authentication,
                                                     @Valid @RequestBody QuickReplyRequest request) {
        QuickReply entity = QuickReplyMapper.toEntity(request);
        QuickReply created = quickReplyService.create(authentication.getName(), entity);
        QuickReplyResponse response = QuickReplyMapper.toResponse(created);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieves all quick replies for the authenticated vendor.
     *
     * @param authentication the authenticated user's security context
     * @return HTTP 200 with the list of quick replies
     */
    @Operation(summary = "Get my quick replies", description = "Retrieves all quick reply templates for the authenticated vendor.")
    @ApiResponse(responseCode = "200", description = "Quick replies retrieved successfully")
    @GetMapping
    public ResponseEntity<List<QuickReplyResponse>> getMyQuickReplies(Authentication authentication) {
        List<QuickReplyResponse> responses = quickReplyService.getMyQuickReplies(authentication.getName())
                .stream()
                .map(QuickReplyMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Updates an existing quick reply. Only the owner can update it.
     *
     * @param authentication the authenticated user's security context
     * @param id             the quick reply ID
     * @param request        the updated quick reply data
     * @return HTTP 200 with the updated quick reply
     */
    @Operation(summary = "Update a quick reply", description = "Updates an existing quick reply template. Only the owner can update it.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quick reply updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Quick reply not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<QuickReplyResponse> update(Authentication authentication,
                                                     @PathVariable Long id,
                                                     @Valid @RequestBody QuickReplyRequest request) {
        QuickReply entity = QuickReplyMapper.toEntity(request);
        QuickReply updated = quickReplyService.update(authentication.getName(), id, entity);
        return ResponseEntity.ok(QuickReplyMapper.toResponse(updated));
    }

    /**
     * Deletes a quick reply. Only the owner can delete it.
     *
     * @param authentication the authenticated user's security context
     * @param id             the quick reply ID
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Delete a quick reply", description = "Deletes a quick reply template. Only the owner can delete it.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Quick reply deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Quick reply not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication authentication,
                                       @PathVariable Long id) {
        quickReplyService.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}