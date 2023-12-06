package org.example.model;

import lombok.Getter;

@Getter
public enum PaiementStatus {
    deleted,
    bankReconciliationDone,
    notValidated
}
