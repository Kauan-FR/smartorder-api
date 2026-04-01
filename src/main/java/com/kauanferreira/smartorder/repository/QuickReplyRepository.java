package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.QuickReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link QuickReply} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing vendor quick reply templates.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Repository
public interface QuickReplyRepository extends JpaRepository<QuickReply, Long> {

    /**
     * Finds all quick replies belonging to a specific user.
     * Ordered by creation date descending (newest first).
     *
     * @param userId the ID of the user
     * @return a list of quick replies for the given user
     */
    @Query("SELECT qr FROM QuickReply qr WHERE qr.user.id = :userId ORDER BY qr.createdAt DESC")
    List<QuickReply> findByUserId(@Param("userId") Long userId);

    /**
     * Finds a quick reply by ID with eagerly fetched user.
     *
     * @param id the quick reply ID
     * @return an Optional containing the quick reply if found
     */
    @Query("SELECT qr FROM QuickReply qr JOIN FETCH qr.user WHERE qr.id = :id")
    Optional<QuickReply> findById(@Param("id") Long id);
}