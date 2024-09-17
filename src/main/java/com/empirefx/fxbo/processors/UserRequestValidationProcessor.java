package com.empirefx.fxbo.processors;
import com.empirefx.fxbo.models.provider.UserRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class UserRequestValidationProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the UserRequest object from the exchange body
        UserRequest userRequest = exchange.getIn().getBody(UserRequest.class);

        // Perform validation
        if (userRequest == null) {
            throw new ValidationException(exchange, "UserRequest cannot be null");
        }

        if (userRequest.getUser() <= 0) {
            throw new ValidationException(exchange, "User ID must be a positive integer");
        }

        if (userRequest.getPassword() == null || userRequest.getPassword().isEmpty()) {
            throw new ValidationException(exchange, "Password must not be empty");
        }

        if (userRequest.getSid() == null || userRequest.getSid().isEmpty()) {
            throw new ValidationException(exchange, "SID must not be empty");
        }

        if (userRequest.getGroupName() == null || userRequest.getGroupName().isEmpty()) {
            throw new ValidationException(exchange, "Group name must not be empty");
        }

        if (userRequest.getLeverage() <= 0) {
            throw new ValidationException(exchange, "Leverage must be a positive integer");
        }

        if (userRequest.getInitialBalance() < 0) {
            throw new ValidationException(exchange, "Initial balance cannot be negative");
        }

        // Validation passed
        System.out.println("Validation passed for UserRequest: " + userRequest);
    }
}