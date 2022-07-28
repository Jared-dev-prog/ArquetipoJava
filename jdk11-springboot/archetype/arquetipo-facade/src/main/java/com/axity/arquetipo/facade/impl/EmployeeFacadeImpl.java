package com.axity.arquetipo.facade.impl;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.axity.arquetipo.commons.request.graphql.EmployeeQueryDto;
import com.axity.arquetipo.commons.response.graphql.EmployeeGraphQLDto;
import com.axity.arquetipo.facade.EmployeeFacade;
import com.axity.arquetipo.service.EmployeeService;

import graphql.schema.DataFetchingEnvironment;

/**
 * Implementación de la interface {@link com.axity.arquetipo.facade.EmployeeFacade}
 * 
 * @author guillermo.segura@axity.com
 */
@Service
@Transactional
public class EmployeeFacadeImpl implements EmployeeFacade
{
  @Autowired
  private EmployeeService employeeService;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EmployeeGraphQLDto> getAllEmployees( String lastName, String firstName, String email, DataFetchingEnvironment env )
  {
    var query = new EmployeeGraphQLDto();
    query.setLastName( lastName );
    query.setFirstName( firstName );
    query.setEmail( email );
    
    var wrapper = createEmployeeQueryWrapper( query, env );
    
    return this.employeeService.getByExample( wrapper );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EmployeeGraphQLDto getEmployeeById( @NotNull Long employeeNumber, DataFetchingEnvironment env )
  {
    var query = new EmployeeGraphQLDto();
    query.setEmployeeNumber( employeeNumber );
    
    var wrapper = createEmployeeQueryWrapper( query, env );
    
    return this.employeeService.getByExample( wrapper ).stream().findFirst().orElse( null );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EmployeeGraphQLDto> getByExample( EmployeeGraphQLDto query, DataFetchingEnvironment env )
  {
    var wrapper = createEmployeeQueryWrapper( query, env );
    
    return this.employeeService.getByExample( wrapper );
  }

  /**
   * @param query
   * @param env
   * @return
   */
  private EmployeeQueryDto createEmployeeQueryWrapper( EmployeeGraphQLDto query, DataFetchingEnvironment env )
  {
    var wrapper = new EmployeeQueryDto( query );
    wrapper.setReportsToProjection( env.getSelectionSet().contains( "reportsTo" ) );
    wrapper.setOfficeProjection( env.getSelectionSet().contains( "office" ) );
    wrapper.setCustomersProjection( env.getSelectionSet().contains( "customers" ) );
    return wrapper;
  }
  
  

}
