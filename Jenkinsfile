pipeline {
  agent any
environment {

SITEBASEURL = null
APIKEY = null
PACKAGEFILENAME = null
initiateInspectionJson = null
deploymentResponseJson = null
warnings = null
errors = null
DEPLOYMENTNAME = null
DEPLOYMENTDESCRIPTION = null
}
  stages {
    
    stage("Install AVM and FitNesse for Appian") {
      steps {
        script {
          // Retrieve and setup AVM
          
          bat "if exist adm rmdir /Q /S adm"
          bat "if exist f4a rmdir /Q /S f4a"
          bat "mkdir adm\\appian-version-client"
          bat "mkdir f4a"
          //jenkinsUtils.shNoTrace("curl -H X-JFrog-Art-Api:$ARTIFACTORYAPIKEY -O $ARTIFACTORYURL/appian-devops/adm.zip")
          bat "tar -xf devops/adm.zip -C adm"
          bat "tar -xf adm/appian-adm-versioning-client-2.5.17.zip -C adm/appian-version-client" 
          def v = unzip zipFile: "adm/appian-adm-versioning-client-2.5.17.zip", dir: "adm/appian-version-client"
          // Retrieve and setup F4A
          //jenkinsUtils.shNoTrace("curl -H X-JFrog-Art-Api:$ARTIFACTORYAPIKEY -O $ARTIFACTORYURL/appian-devops/f4a.zip")
          //bat "tar -xf f4a.zip -C f4a"
          //sh "cp -a devops/f4a/test_suites/. f4a/FitNesseForAppian/FitNesseRoot/FitNesseForAppian/Examples/"
          //sh "cp devops/f4a/users.properties f4a/FitNesseForAppian/configs/users.properties"

          
        }
      }
    }
    stage("Build Package") {
      steps {
        script {
          def properties = readProperties file: "devops\\deploymentmanagement.test.properties"
          DEPLOYMENTDESCRIPTION = properties['deploymentDescription']
          DEPLOYMENTNAME = properties['deploymentName']
          SITEBASEURL = properties['url']
          APIKEY = properties['siteApiKey']
          PACKAGEFILENAME = properties['packageFileName']
          def jenkinsUtils = load "groovy/JenkinsUtils.groovy"
          jenkinsUtils.buildPackage("version-manager.properties")
        }
      }
    }

    stage("Inspect Package") {
      steps {
        script {
          def properties = readProperties file: "devops\\deploymentmanagement.test.properties"
          DEPLOYMENTDESCRIPTION = properties['deploymentDescription']
          DEPLOYMENTNAME = properties['deploymentName']
          SITEBASEURL = properties['url']
          APIKEY = properties['siteApiKey']
          PACKAGEFILENAME = properties['packageFileName']
          def jenkinsUtils = load "groovy/JenkinsUtils.groovy"
          jenkinsUtils.inspectPackage()
        }
      }
    }
    
    stage("Create Deployment Request") {
      steps {
        script {
          def properties = readProperties file: "devops\\deploymentmanagement.test.properties"
          DEPLOYMENTDESCRIPTION = properties['deploymentDescription']
          DEPLOYMENTNAME = properties['deploymentName']
          SITEBASEURL = properties['url']
          APIKEY = properties['siteApiKey']
          PACKAGEFILENAME = properties['packageFileName']
          def jenkinsUtils = load "groovy/JenkinsUtils.groovy"
          jenkinsUtils.createDeployment()
          


        }
      }
    }
    stage("Check Deployment Status") {
      steps {
        script {
          def properties = readProperties file: "devops\\deploymentmanagement.test.properties"
          DEPLOYMENTDESCRIPTION = properties['deploymentDescription']
          DEPLOYMENTNAME = properties['deploymentName']
          SITEBASEURL = properties['url']
          APIKEY = properties['siteApiKey']
          PACKAGEFILENAME = properties['packageFileName']
          def jenkinsUtils = load "groovy/JenkinsUtils.groovy"
          jenkinsUtils.checkDeploymentStatus()
        }
      }
    }
  }
}


