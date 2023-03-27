package com.project.splitwise.utils;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Resilience4JUtils {

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;

    // throws CallNotPermittedException (circuitbreaker)
    // and FeignException (retry)
    public <T> T execute(Supplier<T> supplier, String serviceTag) {
        return Decorators.ofSupplier(supplier)
            .withCircuitBreaker(getCircuitBreakerFromRegistry(serviceTag))
            .withRetry(getRetryFromRegistry(serviceTag))
            .decorate()
            .get();
    }

    private CircuitBreaker getCircuitBreakerFromRegistry(String serviceTag) {
        return circuitBreakerRegistry.circuitBreaker(serviceTag);
    }

    private Retry getRetryFromRegistry(String serviceTag) {
        return retryRegistry.retry(serviceTag);
    }

}
