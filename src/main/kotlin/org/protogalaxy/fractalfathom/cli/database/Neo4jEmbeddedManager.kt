package org.protogalaxy.fractalfathom.cli.database

import org.neo4j.configuration.GraphDatabaseSettings
import org.neo4j.dbms.api.DatabaseManagementService
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder
import org.neo4j.graphdb.GraphDatabaseService
import java.nio.file.Paths

class Neo4jEmbeddedManager(private val dbPath: String) {
    private lateinit var managementService: DatabaseManagementService
    private lateinit var database: GraphDatabaseService

    init {
        setupDatabase()
    }

    private fun setupDatabase() {
        val dbPath = Paths.get(dbPath)
        managementService = DatabaseManagementServiceBuilder(dbPath)
            .build()
        database = managementService.database(GraphDatabaseSettings.DEFAULT_DATABASE_NAME)
        registerShutdownHook(managementService)
    }

    private fun registerShutdownHook(service: DatabaseManagementService) {
        Runtime.getRuntime().addShutdownHook(Thread { service.shutdown() })
    }

    fun getDatabase(): GraphDatabaseService = database

    fun shutdown() {
        managementService.shutdown()
    }
}