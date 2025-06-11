package dev.slne.surf.auctionhouse.core.auction.bid

import dev.slne.surf.auctionhouse.api.auction.bid.Bid
import java.time.ZonedDateTime
import java.util.*

class CoreBid(
    override val bidder: UUID,
    override val bid: Long,

    override val createdAt: ZonedDateTime
) : Bid