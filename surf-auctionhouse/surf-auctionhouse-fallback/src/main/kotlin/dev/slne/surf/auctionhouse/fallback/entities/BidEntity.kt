package dev.slne.surf.auctionhouse.fallback.entities

import dev.slne.surf.auctionhouse.core.auction.bid.CoreBid
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class BidEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<BidEntity>(BidsTable)

    var auction by AuctionEntity referencedOn BidsTable.auction

    var bidder by BidsTable.bidder
    var bid by BidsTable.bid

    var createdAt by BidsTable.createdAt

    fun toDto() = CoreBid(
        bidder = bidder,
        bid = bid,
        createdAt = createdAt
    )

}