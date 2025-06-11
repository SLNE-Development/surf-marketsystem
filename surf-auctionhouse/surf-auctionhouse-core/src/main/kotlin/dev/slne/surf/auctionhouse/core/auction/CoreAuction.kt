package dev.slne.surf.auctionhouse.core.auction

import dev.slne.surf.auctionhouse.api.auction.Auction
import dev.slne.surf.auctionhouse.api.auction.AuctionCloseReason
import dev.slne.surf.auctionhouse.api.auction.bid.Bid
import dev.slne.surf.surfapi.core.api.util.mutableObjectListOf
import it.unimi.dsi.fastutil.objects.ObjectList
import java.time.ZonedDateTime
import java.util.*

class CoreAuction(
    override val auctionId: UUID,
    override val owner: UUID,
    override val itemStackBase64: String,

    override var buyNowEnabled: Boolean,
    override var minimumBid: Long,

    override var disabled: Boolean = false,
    override var disabledBy: UUID? = null,

    override var closed: Boolean = false,
    override var closedBy: UUID? = null,
    override var closedReason: AuctionCloseReason? = null,

    override var endsAt: ZonedDateTime,

    override val bids: ObjectList<Bid> = mutableObjectListOf(),

    override val createdAt: ZonedDateTime = ZonedDateTime.now(),
    override var updatedAt: ZonedDateTime = ZonedDateTime.now()
) : Auction