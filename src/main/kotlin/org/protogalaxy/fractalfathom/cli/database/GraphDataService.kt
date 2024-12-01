package org.protogalaxy.fractalfathom.cli.database

import org.neo4j.graphdb.*
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureType
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import java.time.ZonedDateTime
import java.util.UUID

class GraphDataCRUDService(private val database: GraphDatabaseService) {

    /**
     * Creates a new feature node in the database.
     *
     * @param featureEntity The FeatureEntity object to be inserted.
     * @return True if the node is successfully created, false otherwise.
     */
    fun createFeature(featureEntity: FeatureEntity): Boolean {
        return try {
            database.beginTx().use { tx ->
                val featureNode = tx.createNode(Label { "Feature" })
                featureNode.setProperty("uuid", featureEntity.uuid.toString())
                featureNode.setProperty("name", featureEntity.name)
                featureNode.setProperty("description", featureEntity.description)
                featureNode.setProperty("type", featureEntity.type.name)
                featureNode.setProperty("creationTime", featureEntity.creationTime.toString())
                featureNode.setProperty("lastUpdatedTime", featureEntity.lastUpdatedTime.toString())
                tx.commit()
            }
            true
        } catch (e: Exception) {
            println("Error inserting class entity: ${e.message}")
            false
        }
    }

    /**
     * Retrieves a feature node by its UUID.
     *
     * @param uuid The UUID of the feature to retrieve.
     * @return The retrieved FeatureEntity, or null if not found.
     */
    fun retrieveFeatureWithUUID(uuid: UUID): FeatureEntity? {
        return try {
            database.beginTx().use { tx ->
                val featureNode = tx.findNode(Label { "Feature" }, "uuid", uuid.toString())
                if (featureNode != null) {
                    return FeatureEntity(
                        uuid = UUID.fromString(featureNode.getProperty("uuid") as String),
                        name = featureNode.getProperty("name") as String,
                        description = featureNode.getProperty("description") as String?,
                        type = FeatureType.valueOf(featureNode.getProperty("type") as String),
                        creationTime = ZonedDateTime.parse(featureNode.getProperty("creationTime") as String),
                        lastUpdatedTime = ZonedDateTime.parse(featureNode.getProperty("lastUpdatedTime") as String)
                    )
                }
            }
            null
        } catch (e: Exception) {
            println("Error retrieving feature by UUID: ${e.message}")
            null
        }
    }

    /**
     * Retrieves a feature node by its name and description.
     *
     * @param name The name of the feature to retrieve.
     * @return The retrieved FeatureEntity, or null if not found.
     */
    fun retrieveFeatureWithNameAndDescription(name: String, description: String? = null): FeatureEntity? {
        return try {
            database.beginTx().use { tx ->
                val result = tx.execute(
                    """
                    MATCH (f:Feature {name: $name})
                    WHERE f.description = $description
                    RETURN f
                    """.trimIndent()
                )
                if (result.hasNext()) {
                    val featureNode = result.next()["f"] as Node
                    return FeatureEntity(
                        uuid = UUID.fromString(featureNode.getProperty("uuid") as String),
                        name = featureNode.getProperty("name") as String,
                        description = featureNode.getProperty("description") as String?,
                        type = FeatureType.valueOf(featureNode.getProperty("type") as String),
                        creationTime = ZonedDateTime.parse(featureNode.getProperty("creationTime") as String),
                        lastUpdatedTime = ZonedDateTime.parse(featureNode.getProperty("lastUpdatedTime") as String)
                    )
                }
            }
            null
        } catch (e: Exception) {
            println("Error retrieving feature by name and description: ${e.message}")
            null
        }
    }

    fun createFeatureRelation(
        sourceFeature: FeatureEntity,
        targetFeature: FeatureEntity,
        relationshipType: String
    ): Boolean {
        return try {
            database.beginTx().use { tx ->
                val sourceNode = tx.findNode(Label { "Feature" }, "uuid", sourceFeature.uuid)
                val targetNode = tx.findNode(Label { "Feature" }, "uuid", targetFeature.uuid)

                if (sourceNode != null && targetNode != null) {
                    sourceNode.createRelationshipTo(targetNode, RelationshipType.withName(relationshipType))
                } else {
                    throw IllegalArgumentException("One or both features not found")
                }
                tx.commit()
            }
            true
        } catch (e: Exception) {
            println("Error relating features: ${e.message}")
            false
        }
    }

    fun createIRClassEntity(irClassEntity: IRClassEntity) {

    }

    fun createIRClassEntities(irClassEntities: List<IRClassEntity>) {

    }

    fun associateClassWithFeature(irClassEntity: IRClassEntity, featureEntity: FeatureEntity) {

    }
}