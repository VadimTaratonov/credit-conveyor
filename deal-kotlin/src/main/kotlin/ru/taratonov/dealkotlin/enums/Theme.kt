package ru.taratonov.dealkotlin.enums

enum class Theme(val title: String) {
    FINISH_REGISTRATION("finish-registration"),
    CREATE_DOCUMENTS("create-documents"),
    SEND_DOCUMENTS("send-documents"),
    SEND_SES("send-ses"),
    CREDIT_ISSUED("credit-issued"),
    APPLICATION_DENIED("application-denied"),
    AUDIT("audit");
}