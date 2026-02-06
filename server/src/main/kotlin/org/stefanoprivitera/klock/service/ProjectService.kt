package org.stefanoprivitera.klock.service

import org.stefanoprivitera.klock.domain.Project
import org.stefanoprivitera.klock.domain.ProjectId
import org.stefanoprivitera.klock.domain.request.ProjectRequest

interface ProjectService {
    fun create(project: ProjectRequest.Create): ProjectId
    fun findById(id: ProjectId): Project?
    fun findAll(filter: ProjectRequest.Filter): List<Project>
    fun update(project: ProjectRequest.Update): Int
    fun deleteById(id: ProjectId): Int
}