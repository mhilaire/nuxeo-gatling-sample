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

import scala.concurrent.duration.{Duration, FiniteDuration}

object Parameters {

  def getBaseUrl(default: String = "http://localhost:8080/nuxeo"): String = {
    System.getProperty("url", default)
  }

  def getSimulationDuration(default: Integer = 60): Duration = {
    val duration: Long = 0L + Integer.getInteger("duration", default)
    Duration(duration, "second")
  }

  def getPause(default: Integer = 1): Integer = {
    Integer.getInteger("pause", default)
  }
  
  def getConcurrentUsers(default: Integer = 10, prefix: String = ""): Integer = {
    Integer.getInteger(prefix + "users", default)
  }

  def getRampDuration(default: Integer = 5, prefix: String = ""): FiniteDuration = {
    val ramp: Long = 0L + Integer.getInteger(prefix + "ramp", default)
    FiniteDuration(ramp, "second")
  }

}
