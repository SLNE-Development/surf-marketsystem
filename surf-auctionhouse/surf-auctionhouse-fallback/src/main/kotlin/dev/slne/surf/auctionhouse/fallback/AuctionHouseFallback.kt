package dev.slne.surf.auctionhouse.fallback

import dev.slne.surf.auctionhouse.fallback.entities.AuctionsTable
import dev.slne.surf.auctionhouse.fallback.entities.BidsTable
import dev.slne.surf.database.DatabaseManager
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.nio.file.Path

object AuctionHouseFallback {

    private lateinit var databaseManager: DatabaseManager

    suspend fun init(configDirectory: Path, storageDirectory: Path) {
        databaseManager = DatabaseManager(configDirectory, storageDirectory)
        databaseManager.databaseProvider.connect()

        newSuspendedTransaction {
            SchemaUtils.create(
                AuctionsTable,
                BidsTable
            )
        }
    }

}