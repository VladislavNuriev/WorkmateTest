package com.example.generateuser.helper

enum class Nationality(val countryCode: String, val countryFullName: String) {
    AU("AU", "Australia"),
    BR("BR", "Brazil"),
    CA("CA", "Canada"),
    CH("CH", "Switzerland"),
    DE("DE", "Germany"),
    DK("DK", "Denmark"),
    ES("ES", "Spain"),
    FI("FI", "Finland"),
    FR("FR", "France"),
    GB("GB", "United Kingdom"),
    IE("IE", "Ireland"),
    IN("IN", "India"),
    IR("IR", "Iran"),
    MX("MX", "Mexico"),
    NL("NL", "Netherlands"),
    NO("NO", "Norway"),
    NZ("NZ", "New Zealand"),
    RS("RS", "Serbia"),
    TR("TR", "Turkey"),
    UA("UA", "Ukraine"),
    US("US", "United States");

    companion object {
        fun fromCode(code: String): Nationality? {
            return entries.find { it.countryCode == code }
        }
    }
}