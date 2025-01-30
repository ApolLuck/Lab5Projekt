package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Objects.Entities.Address;
import com.example.lab2projekt.domain.Objects.Entities.Order;
import com.example.lab2projekt.domain.Objects.Entities.OrderItem;
import com.example.lab2projekt.domain.Objects.Entities.Pizza;
import com.example.lab2projekt.domain.repositories.AddressRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AddressService {
    private final AddressRepository addressRepository;


    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public void createAddress(Map<String, String> params, Order order){

        String city = params.get("city");
        String street = params.get("street");
        String houseNumber = params.get("houseNumber");
        String postalCode = params.get("postalCode");

        Address address = new Address();
        address.setStreet(street);
        address.setHouseNumber(houseNumber);
        address.setCity(city);
        address.setPostalCode(postalCode);
        address.setOrder(order);
        addressRepository.save(address);
    }
}
