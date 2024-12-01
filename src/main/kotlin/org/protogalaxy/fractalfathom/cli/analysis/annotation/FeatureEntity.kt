package org.protogalaxy.fractalfathom.cli.analysis.annotation

import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/**
 * Represents a feature entity with its properties and associated metadata.
 *
 * This class models the core attributes of a feature in the system, including its unique
 * identifier, descriptive details, type, and lifecycle timestamps. It provides factory
 * methods for consistent creation and utilities for safe updates.
 *
 * @property uuid A unique identifier for the feature, automatically generated.
 * @property name The name of the feature. This field is required and identifies the feature.
 * @property description An optional description of the feature, providing more context or details.
 * @property type The type of the feature, indicating whether it is functional or non-functional.
 * @property labels An optional list of tags or categories associated with the feature for filtering or grouping.
 * @property creationTime The timestamp when the feature was created. Automatically assigned during instantiation.
 * @property lastUpdatedTime The timestamp when the feature was last updated. Automatically updated during modifications.
 */
data class FeatureEntity(
    val uuid: UUID,
    val name: String,
    val description: String? = null,
    val type: FeatureType,
    val labels: List<String>? = null,
    val creationTime: ZonedDateTime,
    val lastUpdatedTime: ZonedDateTime
) {
    /**
     * Companion object containing factory methods for creating instances of MappingEntity.
     */
    companion object {
        /**
         * Creates a new FeatureEntity instance with the current timestamp for creation and update.
         *
         * This factory method simplifies the instantiation of FeatureEntity by automatically
         * assigning a UUID and setting lifecycle timestamps.
         *
         * @param name The name of the feature.
         * @param description Optional description providing additional context about the feature.
         * @param type The type of the feature, such as functional or non-functional.
         * @param labels Optional list of tags for categorization or grouping.
         * @param zone The time zone used for timestamps. Defaults to the system's default time zone.
         * @return A new instance of FeatureEntity with initialized timestamps.
         */
        fun create(
            name: String,
            description: String? = null,
            type: FeatureType,
            labels: List<String>? = null,
            zone: ZoneId = ZoneId.systemDefault()
        ): FeatureEntity {
            val now = ZonedDateTime.now(zone)
            return FeatureEntity(
                uuid = UUID.randomUUID(),
                name = name,
                description = description,
                type = type,
                labels = labels,
                creationTime = now,
                lastUpdatedTime = now
            )
        }
    }

    /**
     * Updates the feature's metadata and refreshes the last updated timestamp.
     *
     * This method creates a new instance of the FeatureEntity with the provided updates.
     * Fields that are not explicitly updated retain their previous values.
     *
     * @param name Optional updated name for the feature. If `null`, the current name is retained.
     * @param description Optional updated description for the feature. If `null`, the current description is retained.
     * @param type Optional updated type for the feature. If `null`, the current type is retained.
     * @param labels Optional updated list of tags for the feature. If `null`, the current labels are retained.
     * @param zone The time zone used for the updated timestamp. Defaults to the system's default time zone.
     * @return A new instance of FeatureEntity with the updated fields and refreshed `lastUpdatedTime`.
     */
    fun update(
        name: String? = null,
        description: String? = null,
        type: FeatureType? = null,
        labels: List<String>? = null,
        zone: ZoneId = ZoneId.systemDefault()
    ): FeatureEntity {
        return this.copy(
            name = name ?: this.name,
            description = description ?: this.description,
            type = type ?: this.type,
            labels = labels ?: this.labels,
            lastUpdatedTime = ZonedDateTime.now(zone)
        )
    }
}