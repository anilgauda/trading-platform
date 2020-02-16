
name := "trading_platform"
version := "0.1"
scalaVersion := "2.13.1"
organization      := "ie.ncirl.TradingPlatform"
resolvers += Resolver.url("bintray-sbt-plugins", url("https://dl.bintray.com/sbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns) 
libraryDependencies += "org.springframework.boot" % "spring-boot-starter-web" % "2.2.2.RELEASE"




