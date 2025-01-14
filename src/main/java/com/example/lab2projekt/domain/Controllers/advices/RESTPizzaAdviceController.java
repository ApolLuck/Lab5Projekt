package com.example.lab2projekt.domain.Controllers.advices;

import com.example.lab2projekt.domain.Controllers.RESTControllers.RESTPizzaController;
import com.example.lab2projekt.domain.Exceptions.PizzaNotFoundException;
import com.example.lab2projekt.domain.Exceptions.RESTPizzaNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@RestControllerAdvice(assignableTypes = RESTPizzaController.class)
public class RESTPizzaAdviceController {


    @ExceptionHandler({RESTPizzaNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handlePizzaNotFoundException(RESTPizzaNotFoundException ex) {
        ModelAndView mav = new ModelAndView("errors/pizzaNotFound");
        mav.addObject("errorMessage", ex.getMessage());
        return mav;
    }

}
