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
    // check if in CI
    if (System.getenv("CI") != "true") return@publishing

    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github"
            artifactId = "version-catalog"
            // get gradle property "AUTO_VERSION"
            val autoVersion = project.properties["AUTO_VERSION"]
                ?: throw GradleException("AUTO_VERSION is not set")
            version = autoVersion as String
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
                    var pwd = System.getenv("GH_TOKEN")!!

                    if (pwd.length < 40) {
                        println("GH_TOKEN is not set, try to get from gradle property")
                        pwd = project.properties["GITHUB_PUBLISH_TOKEN"] as String
                    }

                    // validate token
                    require(pwd.length == 40) {
                        "GitHub token is invalid: length = ${pwd.length} != 40"
                    }
                    password = pwd
                }
            }
        }
    }
}
