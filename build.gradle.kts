plugins {
    `version-catalog`
    `maven-publish`
}

catalog {
    versionCatalog {
        from(files("centralized.version.catalog.toml"))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github"
            artifactId = "version-catalog"
            version = "0.1.0"
            from(components["versionCatalog"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/phamducduy1994/Centralized-Version-Catalog")
            credentials {
                credentials {
                    username = ""
                    password = System.getenv("GH_TOKEN")!!
                }
            }
        }
    }
}
