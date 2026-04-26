package com.kauanferreira.smartorder.dto.projection;

/**
 * Projection used to carry aggregated review data (average rating and count)
 * for a given product. Built by ReviewRepository#findRatingsByProductIds in a
 * single batch query, then merged into ProductResponse to avoid N+1 queries
 * when listing products with their ratings.
 *
 * @param productId     the product the aggregation refers to
 * @param averageRating average of all review ratings for the product (0.0 if none)
 * @param reviewCount   total number of reviews for the product (0 if none)
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */
public record RatingProjection(
        Long productId,
        Double averageRating,
        Long reviewCount
){
}
