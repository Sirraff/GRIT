package com.rlsreis.grit.data_models

data class EnvironmentalData(
    var tag: String,
    var temperature: Double?, // Nullable, assuming all these data points might not always be available
    var planktonConcentration: Double?,
    // var noiseLevel: Long?,
    var date: Long, // Store as timestamp for easy sorting and formatting
    // var weatherCondition: String?, // Descriptive weather condition (e.g., "Sunny", "Cloudy")
    // var humidity: Double?, // Relative humidity as a percentage
    // var skyColor: String? // Descriptive color of the sky (e.g., "Blue", "Overcast")
)
