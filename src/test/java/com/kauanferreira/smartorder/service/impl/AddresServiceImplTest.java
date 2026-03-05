package com.kauanferreira.smartorder.service.impl;

import com.kauanferreira.smartorder.entity.Address;
import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.enums.Role;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.AddressRepository;
import com.kauanferreira.smartorder.services.impl.AddressServiceImpl;
import com.kauanferreira.smartorder.services.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AddressServiceImpl}.
 *
 * <p>Uses Mockito to mock {@link AddressRepository} and {@link UserService},
 * testing all CRUD operations and query methods in isolation.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see AddressServiceImpl
 * @see AddressRepository
 * @see UserService
 * @see Address
 * @see User
 * @see Role
 */
@ExtendWith(MockitoExtension.class)
public class AddresServiceImplTest {
    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AddressServiceImpl addressService;

    private User user;
    private Address address1;
    private Address address2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@email.com");
        user.setPassword("password123");
        user.setRole(Role.CUSTOMER);

        address1 = new Address();
        address1.setId(1L);
        address1.setStreet("Rua A");
        address1.setNumber("100");
        address1.setComplement("Apt 1");
        address1.setCity("Aracaju");
        address1.setState("Sergipe");
        address1.setZipCode("49000-000");
        address1.setCountry("Brasil");
        address1.setUser(user);

        address2 = new Address();
        address2.setId(2L);
        address2.setStreet("Rua B");
        address2.setNumber("200");
        address2.setComplement(null);
        address2.setCity("Salvador");
        address2.setState("Bahia");
        address2.setZipCode("40000-000");
        address2.setCountry("Brasil");
        address2.setUser(user);
    }

    // ========================
    // CREATE
    // ========================

    @Test
    @Order(1)
    @DisplayName("Should create address successfully")
    void shouldCreateAddress() {
        when(userService.findById(1L)).thenReturn(user);
        when(addressRepository.save(address1)).thenReturn(address1);

        Address result = addressService.create(address1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStreet()).isEqualTo("Rua A");
        assertThat(result.getUser().getId()).isEqualTo(1L);
        verify(userService).findById(1L);
        verify(addressRepository).save(address1);
    }

    @Test
    @Order(2)
    @DisplayName("Should throw exception when creating address with non-existent user")
    void shouldThrowExceptionWhenCreatingAddressWithNonExistentUser() {
        when(userService.findById(1L)).thenThrow(
                new ResourceNotFoundException("User not found. Id: 1"));

        assertThatThrownBy(() -> addressService.create(address1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userService).findById(1L);
        verify(addressRepository, never()).save(any());
    }

    // ========================
    // FIND BY ID
    // ========================

    @Test
    @Order(3)
    @DisplayName("Should find address by id successfully")
    void shouldFindAddressById() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address1));

        Address result = addressService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStreet()).isEqualTo("Rua A");
        verify(addressRepository).findById(1L);
    }

    @Test
    @Order(4)
    @DisplayName("Should throw exception when address not found by id")
    void shouldThrowExceptionWhenAddressNotFoundById() {
        when(addressRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Address with id 99 not found");

        verify(addressRepository).findById(99L);
    }

    // ========================
    // FIND ALL
    // ========================

    @Test
    @Order(5)
    @DisplayName("Should return all addresses")
    void shouldReturnAllAddresses() {
        when(addressRepository.findAll()).thenReturn(List.of(address1, address2));

        List<Address> result = addressService.findAll();

        assertThat(result).hasSize(2);
        verify(addressRepository).findAll();
    }

    @Test
    @Order(6)
    @DisplayName("Should return empty list when no addresses exist")
    void shouldReturnEmptyListWhenNoAddressesExist() {
        when(addressRepository.findAll()).thenReturn(Collections.emptyList());

        List<Address> result = addressService.findAll();

        assertThat(result).isEmpty();
        verify(addressRepository).findAll();
    }

    // ========================
    // FIND ALL PAGINATED
    // ========================

    @Test
    @Order(7)
    @DisplayName("Should return addresses with pagination")
    void shouldReturnAddressesWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Address> page = new PageImpl<>(List.of(address1, address2), pageable, 2);
        when(addressRepository.findAll(pageable)).thenReturn(page);

        Page<Address> result = addressService.findAll(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(addressRepository).findAll(pageable);
    }

    // ========================
    // FIND BY USER ID
    // ========================

    @Test
    @Order(8)
    @DisplayName("Should return addresses by user id")
    void shouldReturnAddressesByUserId() {
        when(userService.findById(1L)).thenReturn(user);
        when(addressRepository.findByUserId(1L)).thenReturn(List.of(address1, address2));

        List<Address> result = addressService.findByUserId(1L);

        assertThat(result).hasSize(2);
        verify(userService).findById(1L);
        verify(addressRepository).findByUserId(1L);
    }

    @Test
    @Order(9)
    @DisplayName("Should return empty list when user has no addresses")
    void shouldReturnEmptyListWhenUserHasNoAddresses() {
        when(userService.findById(1L)).thenReturn(user);
        when(addressRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        List<Address> result = addressService.findByUserId(1L);

        assertThat(result).isEmpty();
        verify(userService).findById(1L);
        verify(addressRepository).findByUserId(1L);
    }

    @Test
    @Order(10)
    @DisplayName("Should throw exception when finding addresses by non-existent user id")
    void shouldThrowExceptionWhenFindingByNonExistentUserId() {
        when(userService.findById(99L)).thenThrow(
                new ResourceNotFoundException("User not found. Id: 99"));

        assertThatThrownBy(() -> addressService.findByUserId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userService).findById(99L);
        verify(addressRepository, never()).findByUserId(any());
    }

    // ========================
    // FIND BY CITY
    // ========================

    @Test
    @Order(11)
    @DisplayName("Should return addresses by city")
    void shouldReturnAddressesByCity() {
        when(addressRepository.findByCityIgnoreCase("Aracaju")).thenReturn(List.of(address1));

        List<Address> result = addressService.findByCity("Aracaju");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCity()).isEqualTo("Aracaju");
        verify(addressRepository).findByCityIgnoreCase("Aracaju");
    }

    @Test
    @Order(12)
    @DisplayName("Should return empty list when no addresses found by city")
    void shouldReturnEmptyListWhenNoAddressesFoundByCity() {
        when(addressRepository.findByCityIgnoreCase("Manaus")).thenReturn(Collections.emptyList());

        List<Address> result = addressService.findByCity("Manaus");

        assertThat(result).isEmpty();
        verify(addressRepository).findByCityIgnoreCase("Manaus");
    }

    // ========================
    // FIND BY STATE
    // ========================

    @Test
    @Order(13)
    @DisplayName("Should return addresses by state")
    void shouldReturnAddressesByState() {
        when(addressRepository.findByStateIgnoreCase("Sergipe")).thenReturn(List.of(address1));

        List<Address> result = addressService.findByState("Sergipe");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getState()).isEqualTo("Sergipe");
        verify(addressRepository).findByStateIgnoreCase("Sergipe");
    }

    @Test
    @Order(14)
    @DisplayName("Should return empty list when no addresses found by state")
    void shouldReturnEmptyListWhenNoAddressesFoundByState() {
        when(addressRepository.findByStateIgnoreCase("Amazonas")).thenReturn(Collections.emptyList());

        List<Address> result = addressService.findByState("Amazonas");

        assertThat(result).isEmpty();
        verify(addressRepository).findByStateIgnoreCase("Amazonas");
    }

    // ========================
    // FIND BY ZIP CODE
    // ========================

    @Test
    @Order(15)
    @DisplayName("Should return addresses by zip code")
    void shouldReturnAddressesByZipCode() {
        when(addressRepository.findByZipCode("49000-000")).thenReturn(List.of(address1));

        List<Address> result = addressService.findByZipCode("49000-000");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getZipCode()).isEqualTo("49000-000");
        verify(addressRepository).findByZipCode("49000-000");
    }

    @Test
    @Order(16)
    @DisplayName("Should return empty list when no addresses found by zip code")
    void shouldReturnEmptyListWhenNoAddressesFoundByZipCode() {
        when(addressRepository.findByZipCode("99999-999")).thenReturn(Collections.emptyList());

        List<Address> result = addressService.findByZipCode("99999-999");

        assertThat(result).isEmpty();
        verify(addressRepository).findByZipCode("99999-999");
    }

    // ========================
    // COUNT BY USER ID
    // ========================

    @Test
    @Order(17)
    @DisplayName("Should count addresses by user id")
    void shouldCountAddressesByUserId() {
        when(addressRepository.countByUserId(1L)).thenReturn(2L);

        long result = addressService.countByUserId(1L);

        assertThat(result).isEqualTo(2L);
        verify(addressRepository).countByUserId(1L);
    }

    @Test
    @Order(18)
    @DisplayName("Should return zero when user has no addresses")
    void shouldReturnZeroWhenUserHasNoAddresses() {
        when(addressRepository.countByUserId(1L)).thenReturn(0L);

        long result = addressService.countByUserId(1L);

        assertThat(result).isZero();
        verify(addressRepository).countByUserId(1L);
    }

    // ========================
    // UPDATE
    // ========================

    @Test
    @Order(19)
    @DisplayName("Should update address successfully")
    void shouldUpdateAddress() {
        Address updatedData = new Address();
        updatedData.setStreet("Rua Nova");
        updatedData.setNumber("500");
        updatedData.setComplement("Bloco B");
        updatedData.setCity("Recife");
        updatedData.setState("Pernambuco");
        updatedData.setZipCode("50000-000");
        updatedData.setCountry("Brasil");

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address1));
        when(addressRepository.save(any(Address.class))).thenReturn(address1);

        Address result = addressService.update(1L, updatedData);

        assertThat(result).isNotNull();
        assertThat(address1.getStreet()).isEqualTo("Rua Nova");
        assertThat(address1.getCity()).isEqualTo("Recife");
        assertThat(address1.getState()).isEqualTo("Pernambuco");
        assertThat(address1.getZipCode()).isEqualTo("50000-000");
        verify(addressRepository).findById(1L);
        verify(addressRepository).save(address1);
    }

    @Test
    @Order(20)
    @DisplayName("Should throw exception when updating non-existent address")
    void shouldThrowExceptionWhenUpdatingNonExistentAddress() {
        when(addressRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.update(99L, address1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Address with id 99 not found");

        verify(addressRepository).findById(99L);
        verify(addressRepository, never()).save(any());
    }

    // ========================
    // DELETE
    // ========================

    @Test
    @Order(21)
    @DisplayName("Should delete address successfully")
    void shouldDeleteAddress() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address1));

        addressService.delete(1L);

        verify(addressRepository).findById(1L);
        verify(addressRepository).delete(address1);
    }

    @Test
    @Order(22)
    @DisplayName("Should throw exception when deleting non-existent address")
    void shouldThrowExceptionWhenDeletingNonExistentAddress() {
        when(addressRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Address with id 99 not found");

        verify(addressRepository).findById(99L);
        verify(addressRepository, never()).delete(any());
    }
}
