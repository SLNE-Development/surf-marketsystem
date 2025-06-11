package dev.slne.surf.auctionhouse.fallback.utils

import dev.slne.surf.auctionhouse.api.auction.Auction
import dev.slne.surf.auctionhouse.fallback.entities.AuctionEntity
import dev.slne.surf.auctionhouse.fallback.entities.AuctionsTable
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun Auction.toEntity() = newSuspendedTransaction {
    AuctionEntity.find { AuctionsTable.auctionId eq auctionId }.firstOrNull()
}