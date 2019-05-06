package ren.practice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ren.practice.client.DepartmentClient;
import ren.practice.client.EmployeeClient;
import ren.practice.model.Organization;
import ren.practice.repository.OrganizationRepository;

import java.util.List;

/**
 * Controller of the organization service with some simple endpoints.
 *
 * @author Attila Szőke
 */
@RestController
public class OrganizationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);

    private OrganizationRepository repository;
    private DepartmentClient departmentClient;
    private EmployeeClient employeeClient;

    /**
     * Instantiates a new Organization controller.
     *
     * @param repository       the repository
     * @param departmentClient the department client
     * @param employeeClient   the employee client
     */
    @Autowired
    public OrganizationController(OrganizationRepository repository, DepartmentClient departmentClient, EmployeeClient employeeClient) {
        this.repository = repository;
        this.departmentClient = departmentClient;
        this.employeeClient = employeeClient;
    }

    /**
     * Add organization.
     *
     * @param organization the organization
     * @return the organization
     */
    @PostMapping
    public Organization add(@RequestBody Organization organization) {
        LOGGER.info("Organization add: {}", organization);
        return repository.add(organization);
    }

    /**
     * Find all organizations.
     *
     * @return the list
     */
    @GetMapping
    public List<Organization> findAll() {
        LOGGER.info("Organization find");
        return repository.findAll();
    }

    /**
     * Find organization by id.
     *
     * @param id the id
     * @return the organization
     */
    @GetMapping("/{id}")
    public Organization findById(@PathVariable("id") Long id) {
        LOGGER.info("Organization find: id={}", id);
        return repository.findById(id);
    }

    /**
     * Find organization by id with departments with the help of feign client.
     *
     * @param id the id
     * @return the organization
     */
    @GetMapping("/{id}/with-departments")
    public Organization findByIdWithDepartments(@PathVariable("id") Long id) {
        LOGGER.info("Organization find: id={}", id);
        Organization organization = repository.findById(id);
        organization.setDepartments(departmentClient.findByOrganization(organization.getId()));
        return organization;
    }

    /**
     * Find organization by id with departments and employees with the help of feign client.
     *
     * @param id the id
     * @return the organization
     */
    @GetMapping("/{id}/with-departments-and-employees")
    public Organization findByIdWithDepartmentsAndEmployees(@PathVariable("id") Long id) {
        LOGGER.info("Organization find: id={}", id);
        Organization organization = repository.findById(id);
        organization.setDepartments(departmentClient.findByOrganizationWithEmployees(organization.getId()));
        organization.setEmployees(employeeClient.findByOrganization(organization.getId()));
        return organization;
    }

    /**
     * Find organization by id with employees with the help of feign client.
     *
     * @param id the id
     * @return the organization
     */
    @GetMapping("/{id}/with-employees")
    public Organization findByIdWithEmployees(@PathVariable("id") Long id) {
        LOGGER.info("Organization find: id={}", id);
        Organization organization = repository.findById(id);
        organization.setEmployees(employeeClient.findByOrganization(organization.getId()));
        return organization;
    }

}