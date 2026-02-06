package org.stefanoprivitera.klock.routes.util

import io.ktor.http.*
import org.stefanoprivitera.klock.domain.*
import org.stefanoprivitera.klock.domain.request.*
import org.stefanoprivitera.klock.routes.util.QueryParamParser.getBooleanOrNull
import org.stefanoprivitera.klock.routes.util.QueryParamParser.getLocalDateOrNull
import org.stefanoprivitera.klock.routes.util.QueryParamParser.getStringListOrNull
import org.stefanoprivitera.klock.routes.util.QueryParamParser.getStringOrNull
import org.stefanoprivitera.klock.routes.util.QueryParamParser.getUuidOrNull
import kotlin.uuid.ExperimentalUuidApi

/**
 * Extension functions to build Filter objects from query parameters
 */
@OptIn(ExperimentalUuidApi::class)
object FilterBuilder {

    fun Parameters.toContractFilter(): ContractRequest.Filter {
        return ContractRequest.Filter(
            customerId = getUuidOrNull("customerId")?.let { CustomerId(it) },
            billingDateFrom = getLocalDateOrNull("billingDateFrom"),
            billingDateTo = getLocalDateOrNull("billingDateTo"),
            status = getStringOrNull("status")
        )
    }

    fun Parameters.toCustomerFilter(): CustomerRequest.Filter {
        return CustomerRequest.Filter(
            companyName = getStringOrNull("companyName"),
            name = getStringOrNull("name"),
            email = getStringOrNull("email")
        )
    }

    fun Parameters.toDepartmentFilter(): DepartmentRequest.Filter {
        return DepartmentRequest.Filter(
            name = getStringOrNull("name"),
            description = getStringOrNull("description"),
            parentDepartmentId = getUuidOrNull("parentDepartmentId")?.let { DepartmentId(it) }
        )
    }

    fun Parameters.toExpenseFilter(): ExpenseRequest.Filter {
        return ExpenseRequest.Filter(
            userId = getUuidOrNull("userId")?.let { UserId(it) },
            dateFrom = getLocalDateOrNull("dateFrom"),
            dateTo = getLocalDateOrNull("dateTo"),
            category = getStringOrNull("category"),
            status = getStringOrNull("status")
        )
    }

    fun Parameters.toProjectFilter(): ProjectRequest.Filter {
        return ProjectRequest.Filter(
            name = getStringOrNull("name"),
            customerId = getUuidOrNull("customerId")?.let { CustomerId(it) },
            managerId = getUuidOrNull("managerId")?.let { UserId(it) },
            departmentId = getUuidOrNull("departmentId")?.let { DepartmentId(it) },
            workGroupId = getUuidOrNull("workGroupId")?.let { WorkGroupId(it) },
            active = getBooleanOrNull("active")
        )
    }

    fun Parameters.toRequestFilter(): RequestRequest.Filter {
        return RequestRequest.Filter(
            projectId = getUuidOrNull("projectId")?.let { ProjectId(it) },
            contractId = getUuidOrNull("contractId")?.let { ContractId(it) },
            requestType = getStringOrNull("requestType"),
            status = getStringOrNull("status")
        )
    }

    fun Parameters.toTimeEntryFilter(): TimeEntryRequest.Filter {
        return TimeEntryRequest.Filter(
            userId = getUuidOrNull("userId")?.let { UserId(it) },
            dateFrom = getLocalDateOrNull("dateFrom"),
            dateTo = getLocalDateOrNull("dateTo"),
            type = getStringOrNull("type"),
            status = getStringOrNull("status")
        )
    }

    fun Parameters.toTimeEntryItemFilter(): TimeEntryItemRequest.Filter {
        return TimeEntryItemRequest.Filter(
            timeEntryId = getUuidOrNull("timeEntryId")?.let { TimeEntryId(it) },
            projectId = getUuidOrNull("projectId")?.let { ProjectId(it) },
            approved = getBooleanOrNull("approved")
        )
    }

    fun Parameters.toUserFilter(): UserRequest.Filter {
        return UserRequest.Filter(
            email = getStringOrNull("email"),
            firstname = getStringOrNull("firstname"),
            lastname = getStringOrNull("lastname")
        )
    }

    fun Parameters.toWorkGroupFilter(): WorkGroupRequest.Filter {
        return WorkGroupRequest.Filter(
            name = getStringOrNull("name"),
            description = getStringOrNull("description"),
            users = getStringListOrNull("users")
        )
    }

    fun Parameters.toWorkGroupUserFilter(): WorkGroupUserRequest.Filter {
        return WorkGroupUserRequest.Filter(
            workGroupId = getUuidOrNull("workGroupId")?.let { WorkGroupId(it) },
            userId = getUuidOrNull("userId")?.let { UserId(it) }
        )
    }

    fun Parameters.toUserDepartmentFilter(): UserDepartmentRequest.Filter {
        return UserDepartmentRequest.Filter(
            userId = getUuidOrNull("userId")?.let { UserId(it) },
            departmentId = getUuidOrNull("departmentId")?.let { DepartmentId(it) }
        )
    }
}
