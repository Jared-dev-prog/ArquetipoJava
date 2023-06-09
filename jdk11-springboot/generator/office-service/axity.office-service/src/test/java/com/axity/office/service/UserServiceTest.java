package com.axity.office.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import com.axity.office.commons.dto.RoleDto;
import com.axity.office.commons.dto.UserDto;
import com.axity.office.commons.enums.ErrorCode;
import com.axity.office.commons.exception.BusinessException;
import com.axity.office.commons.request.PaginatedRequestDto;

/**
 * Class UserServiceTest
 * 
 * @author username@axity.com
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
class UserServiceTest
{
  private static final Logger LOG = LoggerFactory.getLogger( UserServiceTest.class );

  @Autowired
  private UserService userService;

  /**
   * Method to validate the paginated search
   */
  @Test
  void testFindUsers()
  {
    var request = new PaginatedRequestDto();
    request.setLimit( 5 );
    request.setOffset( 0 );
    var users = this.userService.findUsers( request );

    LOG.info( "Response: {}", users );

    assertNotNull( users );
    assertNotNull( users.getData() );
    assertFalse( users.getData().isEmpty() );
  }

  /**
   * Method to validate the search by id
   * 
   * @param userId
   */
  @ParameterizedTest
  @ValueSource(ints = { 1 })
  void testFind( Integer userId )
  {
    var user = this.userService.find( userId );
    assertNotNull( user );
    LOG.info( "Response: {}", user );
  }

  /**
   * Method to validate the search by id inexistent
   */
  @Test
  void testFind_NotExists()
  {
    var user = this.userService.find( 999999 );
    assertNull( user );
  }

    /**
   * Test method for
   * @param id
   * @return Instance of RoleDto
   */
    private RoleDto createRole(int id) {
      var role = new RoleDto();
      role.setId(id);
      return role;
    }
  /**

  * Test method for create user happy path
  /**
   * Test method for
   * {@link com.axity.office.service.impl.UserServiceImpl#create(com.axity.office.commons.dto.UserDto)}.
   */
  @Test
  void testCreate()
  {
    var list = new ArrayList<RoleDto>();
    list.add(createRole(1));

    var dto = new UserDto();
    
    dto.setUsername("JaredProgrammer");
    dto.setEmail("jaredprogrammer@axity.com");
    dto.setName("Jared");
    dto.setLastName("Trejo");
    dto.setRoles(list);

    var response = this.userService.create(dto);

    assertNotNull(response);
    assertEquals("OK", response.getHeader().getMessage());
    assertNotNull(response.getBody());

    this.userService.delete(dto.getId());
  }

  /**
   * Method to validate update
   */
  @Test
  void testUpdate()
  {
    // Data to update
    String name = "Alexander";

    // Data inicial
    int idToUpdate = 1;
    var user = this.userService.find(idToUpdate).getBody();

    user.setName(name);

    var response = this.userService.update(user);

    assertNotNull( response );
    assertEquals("OK", response.getHeader().getMessage());
    assertTrue( response.getBody() );

    user = this.userService.find(idToUpdate).getBody();
    assertEquals(name, user.getName());
  }

  /**
   * Method to validate an inexistent registry
   */
  @Test
  void testUpdate_NotFound()
  {
    var user = new UserDto();
    user.setId(999999);
    var ex = assertThrows( BusinessException.class, () -> this.userService.update( user ) );

    assertEquals( ErrorCode.OFFICE_NOT_FOUND.getCode(), ex.getCode() );
  }

  /**
   * Test method for {@link com.axity.office.service.impl.UserServiceImpl#delete(java.lang.String)}.
   */
  @Test
  void testDeleteNotFound()
  {
    var ex = assertThrows( BusinessException.class, () -> this.userService.delete( 999999 ) );
    assertEquals( ErrorCode.OFFICE_NOT_FOUND.getCode(), ex.getCode() );
  }

    /**
   * Test method for create user with one role
   * {@link com.axity.office.service.impl.UserServiceImpl#create(com.axity.office.commons.dto.UserDto)}.
   */
  @Test
  void testCreateWithOneRole() {

    var list = new ArrayList<RoleDto>();
    list.add(createRole(1));

    var dto = new UserDto();
    dto.setUsername("JaredProgrammer");
    dto.setEmail("jaredprogrammer@axity.com");
    dto.setName("Jared");
    dto.setLastName("Trejo");
    dto.setRoles(list);

    var response = this.userService.create(dto);

    assertNotNull(response);
    assertEquals("OK", response.getHeader().getMessage());
    assertNotNull(response.getBody());

    this.userService.delete(dto.getId());
  }

  /**
   * Test method for create user with many roles
   * {@link com.axity.office.service.impl.UserServiceImpl#create(com.axity.office.commons.dto.UserDto)}.
   */
  @Test
  void testCreateWithManyRole() {
    // Data inicial
    var list = new ArrayList<RoleDto>();
    list.add(createRole(1));
    list.add(createRole(2));
    list.add(createRole(3));

    var dto = new UserDto();
    dto.setUsername("JaredProgrammer");
    dto.setEmail("jaredprogrammer@axity.com");
    dto.setName("Jared");
    dto.setLastName("Trejo");
    dto.setRoles(list);

    var response = this.userService.create(dto);

    assertNotNull(response);
    assertEquals("OK", response.getHeader().getMessage());
    assertNotNull(response.getBody());

    this.userService.delete(dto.getId());
  }

  /**
   * Test method for validate username already exist in bd
   * {@link com.axity.office.service.impl.UserServiceImpl#create(com.axity.office.commons.dto.UserDto)}.
   */
  @Test
  void testValidateUsernameAlreadyExist() {
    // Data inicial
    var list = new ArrayList<RoleDto>();
    list.add(createRole(1));

    var dto = new UserDto();
    dto.setUsername("denise.alford"); 
    dto.setEmail("jaredprogrammer@axity.com");
    dto.setName("Jared");
    dto.setLastName("Trejo");
    dto.setRoles(list);

    var response = this.userService.create(dto);

    assertNotNull(response);
    assertEquals(ErrorCode.USERNAME_ALREADY_EXISTS.getCode(), response.getHeader().getCode());
  }

  /**
   * Test method for validate email already exist in bd
   * {@link com.axity.office.service.impl.UserServiceImpl#create(com.axity.office.commons.dto.UserDto)}.
   */
  @Test
  void testValidateEmailAlreadyExist() {
    // Data inicial
    var list = new ArrayList<RoleDto>();
    list.add(createRole(1));

    var dto = new UserDto();
    dto.setUsername("JaredProgrammer");
    dto.setEmail("denise.alford@company.net"); 
    dto.setName("Jared");
    dto.setLastName("Trejo");
    dto.setRoles(list);

    var response = this.userService.create(dto);

    assertNotNull(response);
    assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS.getCode(), response.getHeader().getCode());
  }

  /**
   * Test method for validate if role selected does not exist
   * {@link com.axity.office.service.impl.UserServiceImpl#create(com.axity.office.commons.dto.UserDto)}.
   */
  @Test
  void testValidateRolesSelectedNotExist() {

    var list = new ArrayList<RoleDto>();
    list.add(createRole(99)); 

    var dto = new UserDto();
    dto.setUsername("JaredProgrammer");
    dto.setEmail("jaredprogrammer@axity.com");
    dto.setName("Jared");
    dto.setLastName("Trejo");
    dto.setRoles(list);

    var response = this.userService.create(dto);

    assertNotNull(response);
    assertEquals(ErrorCode.ROLE_NOT_FOUND.getCode(), response.getHeader().getCode());
  }

/**
   * Test method for validate if roles list is empty
   * {@link com.axity.office.service.impl.UserServiceImpl#create(com.axity.office.commons.dto.UserDto)}.
   */
  @Test
  void testValidateRolesEmpty() {
    var list = new ArrayList<RoleDto>();

    var dto = new UserDto();
    dto.setUsername("JaredProgrammer");
    dto.setEmail("jaredprogrammer@gmail.com");
    dto.setName("Jared");
    dto.setLastName("Trejo");
    dto.setRoles(list);

    var response = this.userService.create(dto);

    assertNotNull(response);
    assertEquals(ErrorCode.NOT_ROLE_SELECTED.getCode(), response.getHeader().getCode());
  }
}