package dev.slne.surf.auctionhouse.api.auction.bid

import java.time.ZonedDateTime
import java.util.*

interface Bid {

    val bidder: UUID
    val bid: Long

    val createdAt: ZonedDateTime

}