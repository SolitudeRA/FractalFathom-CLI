package org.protogalaxy.fractalfathom.cli.analysis.annotation

import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

/**
 * Represents a mapping entity to a higher-level concept or design pattern.
 *
 * This class models the mapping from code elements to higher-level concepts or design patterns.
 * It includes metadata such as creation and update timestamps, ensuring traceability of mappings.
 *
 * @property toConcept The target high-level concept or design pattern being mapped to.
 * @property type The type of mapping, indicating the purpose or nature of the mapping (e.g., concept mapping, dependency mapping).
 * @property creationTime The timestamp when the mapping was created. Automatically assigned during instantiation.
 * @property lastUpdatedTime The timestamp when the mapping was last updated. Automatically updated during modifications.
 */
data class MappingEntity(
    val uuid: UUID,
    val toConcept: String,
    val type: MappingType,
    val creationTime: ZonedDateTime,
    val lastUpdatedTime: ZonedDateTime
){
    /**
     * Companion object containing factory methods for creating instances of MappingEntity.
     */
    companion object {
        /**
         * Creates a new MappingEntity with the current timestamp for creation and update.
         *
         * @param toConcept The target high-level concept or design pattern being mapped to.
         * @param type The type of mapping, indicating the purpose or nature of the mapping.
         * @param zone The time zone used for timestamps. Defaults to the system's default time zone.
         * @return A new instance of MappingEntity with initialized timestamps.
         */
        fun create(
            toConcept: String,
            type: MappingType,
            zone: ZoneId = ZoneId.systemDefault()
        ): MappingEntity {
            val now = ZonedDateTime.now(zone)
            return MappingEntity(
                uuid = UUID.randomUUID(),
                toConcept = toConcept,
                type = type,
                creationTime = now,
                lastUpdatedTime = now
            )
        }
    }

    /**
     * Updates the mapping's metadata and refreshes the last updated timestamp.
     *
     * This method creates a new instance of the MappingEntity with updated properties.
     * Fields that are not explicitly updated retain their previous values.
     *
     * @param toConcept Optional updated target concept or design pattern.
     * @param type Optional updated type of mapping.
     * @param zone The time zone used for the updated timestamp. Defaults to the system's default time zone.
     * @return A new instance of MappingEntity with the updated fields and refreshed lastUpdatedTime.
     */
    fun update(
        toConcept: String? = null,
        type: MappingType? = null,
        zone: ZoneId = ZoneId.systemDefault()
    ): MappingEntity {
        return this.copy(
            toConcept = toConcept ?: this.toConcept,
            type = type ?: this.type,
            lastUpdatedTime = ZonedDateTime.now(zone)
        )
    }
}