package dev.slne.surf.auctionhouse.fallback.entities

import dev.slne.surf.marketsystem.fallback.zonedDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import java.util.*

object BidsTable : LongIdTable("auction_bids") {

    val auction = reference("auction_id", AuctionsTable)
    
    val bidder = char("bidder", 36).transform({ UUID.fromString(it) }, { it.toString() })
    val bid = long("bid")

    val createdAt = zonedDateTime("created_at")
}