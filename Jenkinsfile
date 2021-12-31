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
DEPLOYMENTNAME = 'jenkinsDeployment'
//DEPLOYMENTDESCRIPTION = testProperties['deploymentDescription']
}
  stages {
    
    stage("Install AVM and FitNesse for Appian") {
      steps {
        script {
          // Retrieve and setup AVM
          println propsTwo
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
    
    stage("Create Deployment Request") {
      steps {
        script {
          def jenkinsUtils = load "groovy/JenkinsUtils.groovy"
          jenkinsUtils.createDeployment()
          


        }
      }
    }
    stage("Check Deployment Status") {
      steps {
        script {
          def jenkinsUtils = load "groovy/JenkinsUtils.groovy"
          jenkinsUtils.checkDeploymentStatus()
        }
      }
    }
  }
}


