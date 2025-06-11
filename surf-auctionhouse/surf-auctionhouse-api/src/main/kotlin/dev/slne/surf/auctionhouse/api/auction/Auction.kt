package dev.slne.surf.auctionhouse.api.auction

import dev.slne.surf.auctionhouse.api.auction.bid.Bid
import it.unimi.dsi.fastutil.objects.ObjectList
import java.time.ZonedDateTime
import java.util.*

interface Auction {

    val auctionId: UUID
    val owner: UUID
    val itemStackBase64: String

    var buyNowEnabled: Boolean
    var minimumBid: Long

    var disabled: Boolean
    var disabledBy: UUID?

    var closed: Boolean
    var closedBy: UUID?
    var closedReason: AuctionCloseReason?

    val bids: ObjectList<Bid>

    val endsAt: ZonedDateTime

    val createdAt: ZonedDateTime
    var updatedAt: ZonedDateTime
}