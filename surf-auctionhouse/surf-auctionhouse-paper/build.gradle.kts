import dev.slne.surf.surfapi.gradle.util.withSurfApiBukkit

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

dependencies {
    api(project(":surf-auctionhouse:surf-auctionhouse-core"))
    api(project(":surf-auctionhouse:surf-auctionhouse-fallback"))
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.auctionhouse.paper.AuctionHousePlugin")

    generateLibraryLoader(false)
    authors.add("Ammo")

    serverDependencies {

    }

    runServer {
        withSurfApiBukkit()
    }
}