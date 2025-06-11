package dev.slne.surf.auctionhouse.fallback.service

import com.google.auto.service.AutoService
import dev.slne.surf.auctionhouse.api.auction.Auction
import dev.slne.surf.auctionhouse.api.auction.bid.Bid
import dev.slne.surf.auctionhouse.api.auction.bid.BidResult
import dev.slne.surf.auctionhouse.core.service.AuctionBidService
import dev.slne.surf.auctionhouse.core.service.auctionService
import dev.slne.surf.auctionhouse.fallback.entities.BidEntity
import dev.slne.surf.auctionhouse.fallback.utils.toEntity
import kotlinx.coroutines.Dispatchers
import net.kyori.adventure.util.Services
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.ZonedDateTime
import java.util.*

@AutoService(AuctionBidService::class)
class FallbackAuctionBidService : AuctionBidService, Services.Fallback {

    override suspend fun placeBid(
        auction: Auction,
        bidder: UUID,
        bid: Long
    ): Pair<Bid?, BidResult> = newSuspendedTransaction(Dispatchers.IO) {
        val auctionEntity =
            auction.toEntity() ?: return@newSuspendedTransaction null to BidResult.AUCTION_NOT_FOUND

        if (auctionEntity.closed) {
            return@newSuspendedTransaction null to BidResult.AUCTION_CLOSED
        }

        if (auctionEntity.disabled || auctionEntity.endsAt.isBefore(ZonedDateTime.now())) {
            return@newSuspendedTransaction null to BidResult.AUCTION_DISABLED
        }

        if (bidder == auctionEntity.owner) {
            return@newSuspendedTransaction null to BidResult.BIDDER_IS_OWNER
        }

        if (bid < auctionEntity.minimumBid) {
            return@newSuspendedTransaction null to BidResult.BID_TOO_LOW
        }

        val currentBid = auctionEntity.bids.maxByOrNull { it.bid }?.bid ?: 0L

        if (bid <= currentBid) {
            return@newSuspendedTransaction null to BidResult.BID_NOT_HIGHER_THAN_CURRENT
        }

        val newBid = BidEntity.new {
            this.auction = auctionEntity
            this.bidder = bidder
            this.bid = bid
            this.createdAt = ZonedDateTime.now()
        }

        auction.bids.add(newBid.toDto())

        auctionService.updateAuction(auction) {
            this.updatedAt = ZonedDateTime.now()
        }

        return@newSuspendedTransaction newBid.toDto() to BidResult.SUCCESS
    }
}