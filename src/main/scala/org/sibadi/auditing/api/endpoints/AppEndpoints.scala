package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.endpoints.refucktor.{AdminAPI, FullApi}

//import org.sibadi.auditing.api.endpoints.GroupsAPI.groupsApi
//import org.sibadi.auditing.api.endpoints.KpiAPI.kpiApi
//import org.sibadi.auditing.api.endpoints.PublicAPI.publicApi
//import org.sibadi.auditing.api.endpoints.ReviewerActionsAPI.reviewerActionsApi
//import org.sibadi.auditing.api.endpoints.ReviewersAPI.reviewersApi
//import org.sibadi.auditing.api.endpoints.TeacherActionsAPI.teacherActionsApi
//import org.sibadi.auditing.api.endpoints.TeachersAPI.teachersApi
//import org.sibadi.auditing.api.endpoints.TopicsAPI.topicsApi
//import org.sibadi.auditing.api.endpoints.YamlDocAPI.yamlDocApi

object AppEndpoints {

  val allEndpoints =
//    teachersApi ++
//      topicsApi ++
//      kpiApi ++
//      reviewersApi ++
//      groupsApi ++
//      teacherActionsApi ++
//      publicApi ++
//      reviewerActionsApi ++
//      yamlDocApi ++
      FullApi.all ++ AdminAPI.all

}
