package org.nuxeo.gatling.sample

/*
 * (C) Copyright 2016 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Benoit Delbosc
 *     Antoine Taillefer
 */

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Actions {

  def createGroupIfNotExists = (groupName: String) => {
    exitBlockOnFail {
      exec(
        http("Check if group exists")
          .head("/api/v1/group/" + groupName)
          .headers(Headers.base)
          .header("Content-Type", "application/json")
          .basicAuth("${adminId}", "${adminPassword}")
          .check(status.in(404)))
        .exec(
          http("Create group")
            .post("/api/v1/group")
            .headers(Headers.nxProperties)
            .header("Content-Type", "application/json")
            .basicAuth("${adminId}", "${adminPassword}")
            .body(StringBody(
            """{"entity-type":"group","groupname":"""" + groupName + """", "groupLabel": "Gatling group"}"""))
            .check(status.in(201)))
    }
  }

  def deleteGroup = (groupName: String) => {
    http("Delete group")
      .delete("/api/v1/group/" + groupName)
      .headers(Headers.base)
      .header("Content-Type", "application/json")
      .basicAuth("${adminId}", "${adminPassword}")
      .check(status.in(204))
  }

  def createUserIfNotExists = (groupName: String) => {
    exitBlockOnFail {
      exec(
        http("Check if user exists")
          .head("/api/v1/user/${user}")
          .headers(Headers.base)
          .header("Content-Type", "application/json")
          .basicAuth("${adminId}", "${adminPassword}")
          .check(status.in(404)))
        .exec(
          http("Create user")
            .post("/api/v1/user")
            .headers(Headers.nxProperties)
            .header("Content-Type", "application/json")
            .basicAuth("${adminId}", "${adminPassword}")
            .body(StringBody(
            """{"entity-type":"user","id":"${user}","properties":{"firstName":null,"lastName":null,"password":"${password}","groups":["""" +
              groupName + """"],"company":null,"email":"devnull@nuxeo.com","username":"${user}"},"extendedGroups":[{"name":"members","label":"Members group","url":"group/members"}],"isAdministrator":false,"isAnonymous":false}"""))
            .check(status.in(201)))
    }
  }

  def deleteUser = () => {
    http("Delete user")
      .delete("/api/v1/user/${user}")
      .headers(Headers.base)
      .header("Content-Type", "application/json")
      .basicAuth("${adminId}", "${adminPassword}")
      .check(status.in(204))
  }

  def createDocumentIfNotExistsAsAdmin = (parent: String, name: String, docType: String) => {
    exitBlockOnFail {
      exec(
        http("Check if document exists")
          .head(Constants.API_PATH + parent + "/" + name)
          .headers(Headers.base)
          .header("Content-Type", "application/json")
          .basicAuth("${adminId}", "${adminPassword}")
          .check(status.in(404)))
        .exec(
          http("Create " + docType + " as admin")
            .post(Constants.API_PATH + parent)
            .headers(Headers.base)
            .header("Content-Type", "application/json")
            .basicAuth("${adminId}", "${adminPassword}")
            .body(StringBody(
            """{ "entity-type": "document", "name":"""" + name + """", "type": """" + docType +
              """","properties": {"dc:title":"""" + name + """", "dc:description": "Gatling bench workspace"}}""".stripMargin))
            .check(status.in(201)))
    }
  }

  def deleteFileDocumentAsAdmin = (path: String) => {
    http("Delete document as admin")
      .delete(Constants.API_PATH + path)
      .headers(Headers.base)
      .header("Content-Type", "application/json")
      .basicAuth("${adminId}", "${adminPassword}")
      .check(status.in(204))
  }
  
  def grantReadWritePermission = (path: String, principal: String) => {
    http("Grant ReadWrite permission")
      .post(Constants.API_PATH + path + "/@op/Document.SetACE")
      .basicAuth("${adminId}", "${adminPassword}")
      .headers(Headers.base)
      .header("Content-Type", "application/json")
      .basicAuth("${adminId}", "${adminPassword}")
      .body(StringBody("""{"params":{"permission": "ReadWrite", "user": """" + principal + """"}}""".stripMargin))
      .check(status.in(200))
  }
  
  def createDocument = (parent: String, name: String, docType: String) => {
    http("Create " + docType)
      .post(Constants.API_PATH + parent)
      .headers(Headers.base)
      .header("Content-Type", "application/json")
      .basicAuth("${user}", "${password}")
      .body(StringBody(
      """{ "entity-type": "document", "name":"""" + name + """", "type": """" + docType +
        """","properties": {"dc:title":"""" + name + """", "dc:description": "Gatling test document"}}""".stripMargin))
      .check(status.in(201))
  }

}

object Feeders {

  val usersQueue = csv("users.csv").queue
  val users = csv("users.csv").circular
  val admins = csv("admins.csv").circular

}
