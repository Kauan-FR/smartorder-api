package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.entity.Address;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.AddressRepository;
import com.kauanferreira.smartorder.services.interfaces.AddressService;
import com.kauanferreira.smartorder.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link AddressService} providing CRUD operations
 * and query methods for managing customer addresses.
 *
 * <p>Validates user existence through {@link UserService} before
 * creating addresses or querying by user id.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see AddressService
 * @see AddressRepository
 * @see UserService
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final UserService userService;

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the associated user does not exist
     */
    @Override
    @Transactional
    public Address create(Address address) {
        userService.findById(address.getUser().getId());
        return addressRepository.save(address);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no address is found with the given id
     */
    @Override
    @Transactional(readOnly = true)
    public Address findById(Long id) {
        return addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Address with id %d not found", id))
        );
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Page<Address> findAll(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }


    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the user does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public List<Address> findByUserId(Long userId) {
        userService.findById(userId);
        return addressRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> findByCity(String city) {
        return addressRepository.findByCityIgnoreCase(city);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Address> findByState(String state) {
        return addressRepository.findByStateIgnoreCase(state);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Address> findByZipCode(String zipCode) {
        return addressRepository.findByZipCode(zipCode);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long countByUserId(Long userId) {
        return addressRepository.countByUserId(userId);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Updates the following fields: street, number, complement,
     * city, state, zip code, and country.</p>
     *
     * @throws ResourceNotFoundException if no address is found with the given id
     */
    @Override
    @Transactional
    public Address update(Long id, Address address) {
        Address existing = findById(id);

        existing.setStreet(address.getStreet());
        existing.setNumber(address.getNumber());
        existing.setComplement(address.getComplement());
        existing.setCity(address.getCity());
        existing.setState(address.getState());
        existing.setZipCode(address.getZipCode());
        existing.setCountry(address.getCountry());
        return addressRepository.save(existing);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no address is found with the given id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Address existing = findById(id);
        addressRepository.delete(existing);
    }
}
