package com.juniorjrc.orderservice.utils;

import com.juniorjrc.ordermodel.entity.Customer;
import com.juniorjrc.ordermodel.exception.OrderServiceBadRequestException;
import com.juniorjrc.ordermodel.exception.OrderServiceInternalServerErrorException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChecksumUtils {

    private static final String INTERNAL_SERVER_ERROR_IN_CHECKSUM = "Internal system error when generating checksum, try again later!";
    private static final String CHECKSUM_ALGORITHM = "SHA-256";
    private static final String INVALID_REQUEST = "Invalid request!";

    public static String generateCustomerChecksum(final Customer customer) {
        try {
            String input = customer.getPrivateKey() + customer.getId();
            MessageDigest digest = MessageDigest.getInstance(CHECKSUM_ALGORITHM);
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new OrderServiceInternalServerErrorException(INTERNAL_SERVER_ERROR_IN_CHECKSUM);
        }
    }

    public static void validateChecksum(final String checksum,
                                        final String requestChecksum) {
        if (!checksum.equals(requestChecksum)) {
            throw new OrderServiceBadRequestException(INVALID_REQUEST);
        }
    }
}
