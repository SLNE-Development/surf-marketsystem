package dev.slne.surf.auctionhouse.core.service

import dev.slne.surf.auctionhouse.api.auction.Auction
import dev.slne.surf.auctionhouse.api.auction.bid.Bid
import dev.slne.surf.auctionhouse.api.auction.bid.BidResult
import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.*

val auctionBidService get() = AuctionBidService.INSTANCE

interface AuctionBidService {

    suspend fun placeBid(auction: Auction, bidder: UUID, bid: Long): Pair<Bid?, BidResult>

    companion object {
        val INSTANCE = requiredService<AuctionBidService>()
    }
}