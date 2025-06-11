package dev.slne.surf.auctionhouse.api.auction

enum class AuctionCloseReason {

    TIMEOUT,
    BUY_NOW,
    MANUAL,
    INVALID_ITEM,
    ERROR;

}