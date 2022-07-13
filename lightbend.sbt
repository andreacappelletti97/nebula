ThisBuild / resolvers += "lightbend-commercial-mvn" at "https://repo.lightbend.com/pass/myKey/commercial-releases"
ThisBuild / resolvers += Resolver.url("lightbend-commercial-ivy", url("https://repo.lightbend.com/pass/myPwd/commercial-releases"))(Resolver.ivyStylePatterns)
