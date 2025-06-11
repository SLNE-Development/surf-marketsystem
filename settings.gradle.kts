plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "surf-marketsystem"

val includes = mapOf(
    "surf-auctionhouse:surf-auctionhouse-api" to "surf-auctionhouse-api",
    "surf-auctionhouse:surf-auctionhouse-core" to "surf-auctionhouse-core",
    "surf-auctionhouse:surf-auctionhouse-fallback" to "surf-auctionhouse-fallback",
    "surf-auctionhouse:surf-auctionhouse-paper" to "surf-auctionhouse-paper",

    "surf-bazaar:surf-bazaar-api" to "surf-bazaar-api",
    "surf-bazaar:surf-bazaar-paper" to "surf-bazaar-paper",

    "surf-marketsystem-fallback" to "surf-marketsystem-fallback",
)

includes.forEach { (path, name) ->
    include(path)
    findProject(":$path")?.name = name
}