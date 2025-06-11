package dev.slne.surf.auctionhouse.fallback.service

import com.google.auto.service.AutoService
import dev.slne.surf.auctionhouse.api.auction.Auction
import dev.slne.surf.auctionhouse.core.service.AuctionService
import dev.slne.surf.auctionhouse.fallback.entities.AuctionEntity
import dev.slne.surf.auctionhouse.fallback.entities.AuctionsTable
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import kotlinx.coroutines.Dispatchers
import net.kyori.adventure.util.Services
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.ZonedDateTime
import java.util.*

@AutoService(AuctionService::class)
class FallbackAuctionService : AuctionService, Services.Fallback {

    private val _auctions = mutableObjectSetOf<Auction>()
    override val auctions get() = _auctions.freeze()

    override suspend fun fetchAuctions() = newSuspendedTransaction(Dispatchers.IO) {
        val auctionEntities = AuctionEntity.all().toList()
        _auctions.clear()

        auctionEntities.forEach { entity ->
            _auctions.add(entity.toDto())
        }

        return@newSuspendedTransaction _auctions.freeze()
    }

    override suspend fun generateAuctionId(): UUID = newSuspendedTransaction(Dispatchers.IO) {
        var auctionId = UUID.randomUUID()

        while (AuctionEntity.find { AuctionsTable.auctionId eq auctionId }.count() > 0) {
            auctionId = UUID.randomUUID()
        }

        return@newSuspendedTransaction auctionId
    }

    override suspend fun createAuction(
        owner: UUID,
        itemStackBase64: String,
        buyNowEnabled: Boolean,
        minimumBid: Long,
        endsAt: ZonedDateTime,
    ): Auction = newSuspendedTransaction(Dispatchers.IO) {
        val auctionId = generateAuctionId()

        val auction = AuctionEntity.new {
            this.auctionId = auctionId
            this.owner = owner
            this.itemStack = itemStackBase64

            this.buyNowEnabled = buyNowEnabled
            this.minimumBid = minimumBid

            this.disabled = true
            this.disabledBy = owner

            this.closed = false

            this.endsAt = endsAt

            this.createdAt = ZonedDateTime.now()
            this.updatedAt = ZonedDateTime.now()
        }.toDto()

        _auctions.add(auction)
        
        return@newSuspendedTransaction auction
    }

    override suspend fun updateAuction(
        auction: Auction,
        block: Auction.() -> Unit
    ): Pair<Auction, Boolean> = newSuspendedTransaction(Dispatchers.IO) {
        val updated = auction.apply(block)

        val entity =
            AuctionEntity.findSingleByAndUpdate(AuctionsTable.auctionId eq auction.auctionId) {
                it.buyNowEnabled = updated.buyNowEnabled
                it.minimumBid = updated.minimumBid

                it.disabled = updated.disabled
                it.disabledBy = updated.disabledBy

                it.closed = updated.closed
                it.closedBy = updated.closedBy
                it.closedReason = updated.closedReason
            } ?: return@newSuspendedTransaction Pair(auction, false)

        return@newSuspendedTransaction Pair(entity.toDto(), true)
    }
}