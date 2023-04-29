plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.mongodb:mongodb-driver-sync:4.1.2")
//    implementation("org.slf4j:slf4j-api:1.7.25")

}

tasks.test {
    useJUnitPlatform()
}