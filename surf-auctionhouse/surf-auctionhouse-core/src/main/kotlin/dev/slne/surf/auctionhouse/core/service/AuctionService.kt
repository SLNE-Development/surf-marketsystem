package dev.slne.surf.auctionhouse.core.service

import dev.slne.surf.auctionhouse.api.auction.Auction
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.time.ZonedDateTime
import java.util.*

val auctionService get() = AuctionService.INSTANCE

interface AuctionService {

    suspend fun generateAuctionId(): UUID

    suspend fun createAuction(
        owner: UUID,
        itemStackBase64: String,
        buyNowEnabled: Boolean,
        minimumBid: Long,
        endsAt: ZonedDateTime,
    ): Auction

    suspend fun updateAuction(auction: Auction, block: Auction.() -> Unit): Pair<Auction, Boolean>

    suspend fun fetchAuctions(): ObjectSet<Auction>

    val auctions: ObjectSet<Auction>

    companion object {
        val INSTANCE = requiredService<AuctionService>()
    }

}