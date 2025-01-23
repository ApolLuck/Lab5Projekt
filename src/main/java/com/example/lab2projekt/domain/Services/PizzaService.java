package com.example.lab2projekt.domain.Services;

import com.example.lab2projekt.domain.Exceptions.PizzaNotFoundException;
import com.example.lab2projekt.domain.Objects.Entities.CoverType;
import com.example.lab2projekt.domain.Objects.Entities.Pizza;
import com.example.lab2projekt.domain.Objects.PizzaFilter;
import com.example.lab2projekt.domain.Objects.PizzaSpecifications;
import com.example.lab2projekt.domain.repositories.CoverTypeRepository;
import com.example.lab2projekt.domain.repositories.PizzaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PizzaService {

    private final PizzaRepository pizzaRepository;
    private final CoverTypeRepository coverTypeRepository;

    public PizzaService(PizzaRepository pizzaRepository, CoverTypeRepository coverTypeRepository) {
        this.pizzaRepository = pizzaRepository;
        this.coverTypeRepository = coverTypeRepository;
    }

    // Pobranie wszystkich pizz
    @PreAuthorize("hasRole('ADMIN') or hasAnyRole('USER')")
    public List<Pizza> findAllPizzas() {
        return pizzaRepository.findAll();
    }

    // Pobranie pizzy po ID
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Optional<Pizza> findPizzaById(Integer id) {
        return pizzaRepository.findById(id);
    }

    // Zapisanie pizzy
    @PreAuthorize("hasRole('ADMIN')")
    public Pizza savePizza(Pizza pizza) {
        return pizzaRepository.save(pizza);
    }

    // Usunięcie pizzy
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePizza(Integer id) {
        if (!pizzaRepository.existsById(id)) {
            throw new PizzaNotFoundException("Pizza o id " + id + " nie istnieje");
        }
        pizzaRepository.deleteById(id);
    }

    // Wyszukiwanie pizzy po filtrze
    @PreAuthorize("hasRole('ADMIN') or hasAnyRole('USER')")
    public List<Pizza> findPizzasByFilter(String pharse, Double cenaOd, Double cenaDo, LocalDate dataOd,LocalDate dataDo) {
        PizzaFilter pizzaFilter = new PizzaFilter(pharse, cenaOd, cenaDo, dataOd, dataDo);
        Specification<Pizza> specification = Specification
                .where(PizzaSpecifications.findByPriceRange(cenaOd,cenaDo)
                        .and(PizzaSpecifications.findByPhrase(pharse))
                        .and(PizzaSpecifications.findByDateRange(dataOd,dataDo)));
        return pizzaRepository.findAll(specification);
    }

    // Wyszukiwanie pizzy po frazie
    @PreAuthorize("hasRole('ADMIN') or hasAnyRole('USER')")
    public List<Pizza> findPizzasByPhrase(String phrase) {
        String searchPhrase = "%" + phrase + "%";
        return pizzaRepository.findByPhraseInNameOrCoverType(searchPhrase);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAnyRole('USER')")
    public List<Pizza> findByNazwaContainingIgnoreCaseOrSkladnikiContainingIgnoreCase(String nazwa, String nazwa2){

        return pizzaRepository.findByNazwaContainingIgnoreCaseOrSkladnikiContainingIgnoreCase(nazwa, nazwa2);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAnyRole('USER')")
    public List<Pizza> findByCenaBetween(Float minCena, Float maxCena){

        return pizzaRepository.findByCenaBetween(minCena, maxCena);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAnyRole('USER')")
    public List<Pizza> findByCoverType(CoverType coverType){

        return pizzaRepository.findByCoverType(coverType);
    }

    public List<Pizza> filterPizzas(String nazwa, Float minCena, Float maxCena, Integer coverTypeId) {
        List<Pizza> pizzas;

        if (nazwa != null && !nazwa.isEmpty()) {
            pizzas = pizzaRepository.findByNazwaContainingIgnoreCaseOrSkladnikiContainingIgnoreCase(nazwa, nazwa);
        }
        // Filtracja po zakresie cen
        else if (minCena != null && maxCena != null) {
            pizzas = pizzaRepository.findByCenaBetween(minCena, maxCena);
        }
        // Filtracja po rodzaju opakowania
        else if (coverTypeId != null) {
            CoverType coverType = coverTypeRepository.findById(coverTypeId)
                    .orElse(null);
            pizzas = pizzaRepository.findByCoverType(coverType);
        } else {
            pizzas = pizzaRepository.findAll();
        }

        return pizzas;
    }

    public Pizza getPizzaById(Integer pizzaId) throws PizzaNotFoundException {
        Optional<Pizza> pizzaOptional = pizzaRepository.findById(pizzaId);
        if (pizzaOptional.isEmpty()) {
            throw new PizzaNotFoundException("Pizza o id " + pizzaId + " nie została znaleziona");
        }
        return pizzaOptional.get();
    }
}
