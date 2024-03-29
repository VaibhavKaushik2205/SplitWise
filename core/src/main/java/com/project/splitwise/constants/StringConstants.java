package com.project.splitwise.constants;

public interface StringConstants {

    interface Errors {
        String USER_NOT_FOUND = "User not found!";

        String GROUP_NOT_FOUND = "Split Group not found!";

        String INVALID_STATE_TO_CONTINUE = "Seems like we have hit a wall!";

        String INCORRECT_PHONE_NUMBER = "Phone number belongs to a different customer. Invalid "
            + "request";

        String INCORRECT_REFERENCE_ID = "Customer already exists. Invalid request";

        String REQUEST_TIMED_OUT = "Request Timed Out";

        String REQUEST_TYPE_NOT_CONFIGURED = "Request type not configured";

        String DATA_VALIDATION_FAILED = "Validation failed as property : %s has value : %s";

        String INVALID_REQUEST_ID = "Request already processed. Invalid Request Id";

        String USER_ID_MISSING = "User referenceId missing";

        String INVALID_REQUEST_FORMAT = "Invalid format for %s";

        String INCORRECT_SPLIT_DATA = "Split Data incorrect";

        String EXPENSE_NOT_FOUND = "Expense not found!";
    }

    interface Constants {

        String SUCCESS = "SUCCESS";

        String FAILURE = "FAILURE";
    }

    interface KYCService {

        String CLIENT_NAME = "kycServiceFeignClient";

        String API_HOST = "${kyc.service.base.url}/kyc-service";

        interface EndPoints {

            String VERIFY_PAN_PATH = "api/pan/verify";
        }
    }

}
