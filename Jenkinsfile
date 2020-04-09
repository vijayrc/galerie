node {
    def server = Artifactory.server "art7"
    def rtGradle = Artifactory.newGradleBuild()
    def buildInfo

    stage('Clone sources') {
        git url: 'https://github.com/vijayrc/galerie.git'
    }

    stage('Artifactory configuration') {
        // Tool name from Jenkins configuration
        rtGradle.tool = "gradle_6.3"
        // Set Artifactory repositories for dependencies resolution and artifacts deployment.
        rtGradle.deployer repo:'libs-snapshot-local'', server: server
        rtGradle.resolver repo:''libs-release'', server: server
    }

    stage('Gradle build') {
        buildInfo = rtGradle.run rootDir: ".", buildFile: 'build.gradle', tasks: 'clean jar'
    }

    stage('Publish build info') {
        server.publishBuildInfo buildInfo
    }
}