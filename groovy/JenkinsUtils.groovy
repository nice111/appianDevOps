#!/usr/bin/env groovy

void runTestsVNC(propertyFile) {
  sh "cp devops/f4a/" + propertyFile + " f4a/FitNesseForAppian/fitnesse-automation.properties"
  dir("f4a/FitNesseForAppian") {
    wrap([$class:'Xvnc', useXauthority: true]) {
      sh script: "bash ./runFitNesseTest.sh"
    }
  }
}

void runTestsDocker(propertyFile) {
  sh "cp devops/f4a/" + propertyFile + " f4a/FitNesseForAppian/fitnesse-automation.properties"
  sh "docker-compose -f docker/docker-compose.yml up &"
  timeout(2) { //timeout is in minutes
    waitUntil {
      def numExpectedContainers = "2"
      def runningContainers = sh script: "docker ps --format {{.Names}} | grep \"fitnesse-\\(chrome\\|firefox\\)\" | wc -l", returnStdout: true
      runningContainers = runningContainers.trim()
      return (runningContainers == numExpectedContainers)
    }
  }
  sleep(10)
  dir("f4a/FitNesseForAppian") {
    sh script: "bash ./runFitNesseTest.sh"
  }
}

void retrieveLogs(propertyFile) {
  def test = sh script: "cat \"devops/f4a/${propertyFile}\" | grep \"testPath=\" | cut -d'=' -f2", returnStdout: true
  test = test.trim().minus(~"\\?.*")
  def zipName = "${test}_Results.zip"
  dir("f4a/FitNesseForAppian/FitNesseRoot/files/testResults") {
    sh "zip -r ${zipName} ${test}/**"
  }
  return "f4a/FitNesseForAppian/FitNesseRoot/files/testResults/${zipName}"
}

void buildPackage(versionPropertyFile) {
  def status = bat(script:"robocopy devops/adm adm/appian-version-client " + versionPropertyFile,returnStatus:true)
  println "ROBOCOPY returned ${status}"
  dir("adm/appian-version-client") {
    //setProperty("version-manager.properties", "vcUsername", "${REPOUSERNAME}")
    //setProperty("version-manager.properties", "vcPassword", "${REPOPASSWORD}")
    //setProperty("version-manager.properties", "appianObjectsRepoPath", "appian/applications/${APPLICATIONNAME}")
    bat(script:"version-application.bat -vc_username \"$REPOUSERNAME\" -vc_password \"$REPOPASSWORD\" -package_path ../app-package.zip -local_repo_path ./local-repo" )
    bat "tar -xjf ../app-package.zip \"*.zip\""
    bat "rename \"../application_*.zip\" \"deploy-package.zip\""
    bat "if exist newBundle rmdir /Q /S newBundle"
    bat "mkdir newBundle"
    def y = unzip zipFile: "deploy-package.zip", dir: "newBundle"
    bat "if exist \"newBundle/appian\" rmdir /Q /S \"newBundle/appian\""
    def z = zip zipFile: "../finalPackage.zip" dir: "newBundle"
  }
}
void inspectPackage() {
  
  inspectionUrl = SITEBASEURL +"/deployment-management/v1/inspections"
  String response=bat( script:"curl --location  --request POST \"$inspectionUrl\" --header \"Appian-API-Key: $APIKEY\" --form \"zipFile=@\"./adm/finalPackage.zip\"\" --form \"json={\"packageFileName\":\"finalPackage.zip\"}\"", returnStdout: true).trim()
  newResponse = response.readLines().drop(1).join(" ")
  initiateInspectionJson = new groovy.json.JsonSlurperClassic().parseText(newResponse)
  println "Inspection Started"
  println initiateInspectionJson
  sleep 5
  String newUrl = SITEBASEURL + "/deployment-management/v1/inspections" + "/" + initiateInspectionJson.uuid +"/"
  String inspectionResponse = bat(script: "curl --silent --location --request GET \"$newUrl\" --header \"Appian-API-Key: $APIKEY\"" , returnStdout: true).trim()
  inspectionResponse = inspectionResponse.readLines().drop(1).join(" ")
  inspectionResponseJson = new groovy.json.JsonSlurperClassic().parseText(inspectionResponse)
  inspectionStatus = inspectionResponseJson.status
  while(inspectionStatus.equals("IN_PROGRESS")) {
    sleep 30
    inspectionResponse = bat(script: "curl --silent --location --request GET \"$newUrl\" --header \"Appian-API-Key: $APIKEY\"" , returnStdout: true).trim()
    inspectionResponse = inspectionResponse.readLines().drop(1).join(" ")
    inspectionResponseJson = new groovy.json.JsonSlurperClassic().parseText(inspectionResponse)
    inspectionStatus = inspectionResponseJson.status
  }
  println inspectionResponseJson
  /*warnings = inspectionResponseJson.summary.problems.totalWarnings
  errors = inspectionResponseJson.summary.problems.totalErrors
  if(warnings.equals(0) && errors.equals(0)) {
    println "Inspection Success"
  } else{
    error "Inspection Failed, Pipeline Stopped"
  }*/
        
}

void setProperty(filePath, property, propertyValue) {
  shNoTrace("sed -i -e 's|.\\?${property}=.*|${property}=${propertyValue}|' ${filePath}")
}

def shNoTrace(cmd) {
  bat '#!/bin/sh -e\n' + cmd
}

return this
