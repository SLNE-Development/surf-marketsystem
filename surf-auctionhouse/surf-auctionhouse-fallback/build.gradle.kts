plugins {
    id("dev.slne.surf.surfapi.gradle.core")
}

dependencies {
    compileOnly(project(":surf-auctionhouse:surf-auctionhouse-core"))
    implementation(libs.surf.database)
    implementation(project(":surf-marketsystem-fallback"))
}