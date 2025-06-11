package dev.slne.surf.auctionhouse.paper.utils

import org.bukkit.inventory.ItemStack
import java.util.*

fun ItemStack.serializeAsBase64(): String =
    Base64.getEncoder().encodeToString(serializeAsBytes())

fun itemStackFromBase64(base64: String) =
    ItemStack.deserializeBytes(Base64.getDecoder().decode(base64))