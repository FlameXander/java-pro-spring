package ru.flamexander.transfer.service.core.backend.configurations;

import java.time.Duration;

public interface RestProperties {
    String getUrl();
    Duration getReadTimeout();
    Duration getWriteTimeout();
}
