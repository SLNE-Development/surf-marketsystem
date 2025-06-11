package dev.slne.surf.auctionhouse.paper

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.kotlindsl.*
import dev.slne.surf.auctionhouse.core.service.auctionService
import dev.slne.surf.auctionhouse.fallback.AuctionHouseFallback
import dev.slne.surf.auctionhouse.paper.utils.itemStackFromBase64
import dev.slne.surf.auctionhouse.paper.utils.serializeAsBase64
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.plugin.java.JavaPlugin
import java.time.ZonedDateTime
import kotlin.io.path.div

val plugin: AuctionHousePlugin get() = JavaPlugin.getPlugin(AuctionHousePlugin::class.java)

class AuctionHousePlugin : SuspendingJavaPlugin() {

    override suspend fun onLoadAsync() {
        AuctionHouseFallback.init(dataPath, dataPath / "storage")

        val auctions = auctionService.fetchAuctions()

        println("Loaded ${auctions.size} auctions from the database.")
        auctions.forEach { auction ->
            println("Loaded auction: ${auction.auctionId} for owner: ${auction.owner}")
        }
    }

    override suspend fun onEnableAsync() {
        commandAPICommand("auction") {
            subcommand("list") {
                playerExecutor { player, _ ->
                    player.sendText {
                        appendMap(
                            auctionService.auctions.associateBy { it.auctionId },
                            { auctionId ->
                                buildText {
                                    variableKey(auctionId.toString())
                                }
                            },
                            { auction ->
                                run {
                                    val item = itemStackFromBase64(auction.itemStackBase64)
                                    val owner = server.getOfflinePlayer(auction.owner)
                                    val ownerComponent = buildText {
                                        variableValue(owner.name ?: "Unbekannt")

                                        hoverEvent(HoverEvent.showText(buildText {
                                            spacer(owner.uniqueId.toString())
                                        }))
                                    }

                                    buildText {
                                        spacer("Besitzer: ")
                                        append(ownerComponent)
                                        spacer(" | Preis: ")
                                        variableValue(auction.minimumBid.toString())
                                        spacer(" | Endet: ")
                                        variableValue(auction.endsAt.toLocalDateTime().toString())
                                        spacer(" | Item: ")
                                        append {
                                            variableValue(item.type.name)
                                            hoverEvent(HoverEvent.showText(buildText {
                                                spacer("Klicke, um das Item zu erhalten!")
                                            }))
                                            clickEvent(ClickEvent.callback { _ ->
                                                player.inventory.addItem(item)
                                            })
                                        }
                                    }
                                }
                            },
                            linePrefix = Component.empty(),
                        )
                    }
                }
            }
            subcommand("create") {
                longArgument("minimumBid")
                booleanArgument("buyNowEnabled")

                playerExecutor { player, args ->
                    val minimumBid: Long by args
                    val buyNowEnabled: Boolean by args

                    val holdingItem = player.inventory.itemInMainHand

                    if (holdingItem.type.isAir) {
                        player.sendText {
                            appendPrefix()

                            error("Du hältst kein Item in der Hand!")
                        }
                        return@playerExecutor
                    }

                    val holdingItemBase64 = holdingItem.serializeAsBase64()

                    plugin.launch {
                        auctionService.createAuction(
                            owner = player.uniqueId,
                            itemStackBase64 = holdingItemBase64,
                            buyNowEnabled = buyNowEnabled,
                            minimumBid = minimumBid,
                            endsAt = ZonedDateTime.now().plusDays(2)
                        )

                        player.sendText {
                            appendPrefix()

                            success("Deine Auktion wurde erfolgreich erstellt!")
                        }
                    }
                }
            }
        }
    }

    override suspend fun onDisableAsync() {

    }

}