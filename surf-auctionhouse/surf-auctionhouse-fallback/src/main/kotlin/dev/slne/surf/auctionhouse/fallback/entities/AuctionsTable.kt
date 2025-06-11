package dev.slne.surf.auctionhouse.fallback.entities

import dev.slne.surf.auctionhouse.api.auction.AuctionCloseReason
import dev.slne.surf.marketsystem.fallback.zonedDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import java.util.*

object AuctionsTable : LongIdTable("auctions") {

    val auctionId = char("auction_id", 36).transform({ UUID.fromString(it) }, { it.toString() })
        .uniqueIndex("auctions_auction_id_unique")
    val owner = char("owner", 36).transform({ UUID.fromString(it) }, { it.toString() })
    val itemStack = largeText("item_stack")

    val buyNowEnabled = bool("buy_now_enabled").default(false)
    val minimumBid = long("minimum_bid").default(0L)

    val disabled = bool("disabled").default(false)
    val disabledBy = char("disabled_by", 36).nullable()
        .transform({ it?.let { UUID.fromString(it) } }, { it?.toString() })

    val closed = bool("closed").default(false)
    val closedBy = char("closed_by", 36).nullable()
        .transform({ it?.let { UUID.fromString(it) } }, { it?.toString() })
    val closedReason = enumerationByName("closed_reason", 32, AuctionCloseReason::class).nullable()

    val endsAt = zonedDateTime("ends_at")

    val createdAt = zonedDateTime("created_at")
    val updatedAt = zonedDateTime("updated_at")
}