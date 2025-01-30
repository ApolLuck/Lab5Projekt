package com.example.lab2projekt.domain.Controllers.advices;

import com.example.lab2projekt.domain.Exceptions.PizzaNotFoundException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PizzaNotFoundException.class)
    public ModelAndView handlePizzaNotFoundException(PizzaNotFoundException ex) {
        ModelAndView mav = new ModelAndView("errors/pizzaNotFound");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(JDBCConnectionException.class)
    public ModelAndView handleJDBCConnectionException(JDBCConnectionException ex) {
        ModelAndView mav = new ModelAndView("errors/databaseError");
        mav.addObject("errorMessage", "Błąd połączenia z bazą danych: " + ex.getMessage());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception ex) {
        ModelAndView mav = new ModelAndView("errors/generalError");
        mav.addObject("errorMessage", "Wystąpił błąd: " + ex.getMessage());
        return mav;
    }

}
