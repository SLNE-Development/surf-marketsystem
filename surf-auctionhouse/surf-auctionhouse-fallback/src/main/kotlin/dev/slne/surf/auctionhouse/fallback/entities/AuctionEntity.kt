package dev.slne.surf.auctionhouse.fallback.entities

import dev.slne.surf.auctionhouse.core.auction.CoreAuction
import dev.slne.surf.surfapi.core.api.util.toObjectList
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AuctionEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<AuctionEntity>(AuctionsTable)

    var auctionId by AuctionsTable.auctionId
    var owner by AuctionsTable.owner
    var itemStack by AuctionsTable.itemStack

    var buyNowEnabled by AuctionsTable.buyNowEnabled
    var minimumBid by AuctionsTable.minimumBid

    var disabled by AuctionsTable.disabled
    var disabledBy by AuctionsTable.disabledBy

    var closed by AuctionsTable.closed
    var closedBy by AuctionsTable.closedBy
    var closedReason by AuctionsTable.closedReason

    var endsAt by AuctionsTable.endsAt

    val bids by BidEntity referrersOn BidsTable.auction

    var createdAt by AuctionsTable.createdAt
    var updatedAt by AuctionsTable.updatedAt

    fun toDto() = CoreAuction(
        auctionId = auctionId,
        owner = owner,
        itemStackBase64 = itemStack,

        buyNowEnabled = buyNowEnabled,
        minimumBid = minimumBid,

        disabled = disabled,
        disabledBy = disabledBy,

        closed = closed,
        closedBy = closedBy,
        closedReason = closedReason,

        endsAt = endsAt,

        bids = bids.map { it.toDto() }.toObjectList(),

        createdAt = createdAt,
        updatedAt = updatedAt
    )

}