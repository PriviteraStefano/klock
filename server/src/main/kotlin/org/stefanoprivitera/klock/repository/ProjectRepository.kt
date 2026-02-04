package org.stefanoprivitera.klock.repository

import org.stefanoprivitera.klock.domain.Project
import org.stefanoprivitera.klock.domain.ProjectRequest
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface ProjectRepository {
    fun create(project: ProjectRequest.Create): String
    fun findById(id: Uuid): Project?
    fun findAll(filter: ProjectRequest.Filter): List<Project>
    fun update(project: ProjectRequest.Update): Int
    fun deleteById(id: Uuid): Int
}