package dev.slne.surf.auctionhouse.api.auction.bid

enum class BidResult {

    AUCTION_NOT_FOUND,
    AUCTION_CLOSED,
    AUCTION_DISABLED,
    BIDDER_IS_OWNER,
    BID_TOO_LOW,
    BID_NOT_HIGHER_THAN_CURRENT,
    SUCCESS,

}