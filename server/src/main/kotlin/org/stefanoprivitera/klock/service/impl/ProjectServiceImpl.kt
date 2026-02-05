package org.stefanoprivitera.klock.service.impl

import org.koin.core.annotation.Single
import org.stefanoprivitera.klock.domain.Project
import org.stefanoprivitera.klock.domain.ProjectId
import org.stefanoprivitera.klock.domain.ProjectRequest
import org.stefanoprivitera.klock.repository.ProjectRepository
import org.stefanoprivitera.klock.service.ProjectService

@Single
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository
) : ProjectService {
    override fun create(project: ProjectRequest.Create): ProjectId {
        return projectRepository.create(project)
    }

    override fun findById(id: ProjectId): Project? {
        return projectRepository.findById(id)
    }

    override fun findAll(filter: ProjectRequest.Filter): List<Project> {
        return projectRepository.findAll(filter)
    }

    override fun update(project: ProjectRequest.Update): Int {
        return projectRepository.update(project)
    }

    override fun deleteById(id: ProjectId): Int {
        return projectRepository.deleteById(id)
    }
}
