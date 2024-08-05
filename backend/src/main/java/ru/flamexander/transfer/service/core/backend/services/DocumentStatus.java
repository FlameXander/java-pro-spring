package ru.flamexander.transfer.service.core.backend.services;

public enum DocumentStatus {
    CREATED(1), IN_PROGRESS(2), COMPLETED(3), ERROR(4);
    int statusId;
    private DocumentStatus(int statusId) {
        this.statusId = statusId;
    }
    public int getStatusId() {
        return statusId;
    }
}
