pipeline {
  agent any
environment {
def propsTwo = readProperties interpolate: true, file: "C:\\Users\\nick.terweeme\\localAppian\\devops\\deploymentmanagement.test.properties"
SITEBASEURL = '5cg9014w3n.appiancorp.com:8080/suite'
APIKEY = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkMzYzOTQ2OS1mOTZmLTQ3Y2UtOGYyOS1kZTVhZDkwMTdhM2IifQ.eqfVl_7ASfMFOAkBnmUe7kdQOmK8ybJAV42HGbL5VUA'
PACKAGEFILENAME = 'app-package.zip'
initiateInspectionJson = null
deploymentResponseJson = null
warnings = null
errors = null
//DEPLOYMENTNAME = testProperties['deploymentName']
//DEPLOYMENTDESCRIPTION = testProperties['deploymentDescription']
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
          //bat "tar -xf adm/appian-adm-versioning-client-2.5.13.zip -C adm/appian-version-client" 
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
          def jenkinsUtils = load "groovy/JenkinsUtils.groovy"
          jenkinsUtils.buildPackage("version-manager.properties")
        }
      }
    }

    stage("Inspect Package") {
      steps {
        script {
          def jenkinsUtils = load "groovy/JenkinsUtils.groovy"
          jenkinsUtils.inspectPackage()
        }
      }
    }
    
    /*stage("Create Deployment Request") {
      steps {
        script {
          deploymentUrl = SITEBASEURL + "/deployment-management/v1/deployments"
          String response=bat( script:"curl --silent --location  --request POST \"$deploymentUrl\" --header \"Appian-API-Key: $APIKEY\" --form \"zipFile=@\"C:/Users/nick.terweeme/Downloads/testAppPackage.zip\"\" --form \"json={\"packageFileName\":\"$PACKAGEFILENAME\",\"name\":\"$DEPLOYMENTNAME\"}\"", returnStdout: true).trim()
          deploymentResponse = response.readLines().drop(1).join(" ")
          deploymentResponseJson = new groovy.json.JsonSlurperClassic().parseText(deploymentResponse)
          println "Deployment Requested"


        }
      }
    }
    stage("Check Deployment Status") {
      steps {
        script {
          sleep 15
          String newUrl = SITEBASEURL + "/deployment-management/v1/deployments" + "/" + deploymentResponseJson.uuid +"/"
          String deploymentStatus = bat(script: "curl --silent --location --request GET \"$newUrl\" --header \"Appian-API-Key: $APIKEY\"" , returnStdout: true).trim()
          deploymentStatus = deploymentStatus.readLines().drop(1).join(" ")
          deploymentStatusJson = new groovy.json.JsonSlurperClassic().parseText(deploymentStatus)
          statusVar = deploymentStatusJson.status
          while (statusVar.equals("IN_PROGRESS")) {
            sleep 30
            deploymentStatus = bat(script: "curl --silent --location --request GET \"$newUrl\" --header \"Appian-API-Key: $APIKEY\"" , returnStdout: true).trim()
            deploymentStatus = deploymentStatus.readLines().drop(1).join(" ")
            deploymentStatusJson = new groovy.json.JsonSlurperClassic().parseText(deploymentStatus)
            statusVar = deploymentStatusJson.status
          }
          println "Deployment Finished and Status is " + statusVar

        }
      }
    }*/
  }
}


