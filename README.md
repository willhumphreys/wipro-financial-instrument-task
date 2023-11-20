# Financial Calculation Service

## Overview

The Financial Calculation Service is a Java 17 based application designed to process large volumes of financial time
series data. It efficiently calculates various statistics for different financial instruments, such as mean, standard
deviation, and sum of the latest values. The service is capable of handling datasets of significant size (up to
gigabytes of data) and operates within a scalable architecture.

## How to Run

1. **Build the Project**:

- Navigate to the project directory.
- Run `mvn install` to build the project and run unit tests.
- A GitHub hook builds the project on every push to the main branch. The latest build can be found
  [here](https://github.com/willhumphreys/wipro-financial-instrument-task/actions/workflows/build.yml).

## Key Components

- **`CalculationService`**: Central service for processing financial data. It reads time series from input files,
  applies multipliers from the database, and calculates required metrics.
- **Calculators**: Different calculators are used for various instruments:
  - `MeanCalculator`: Calculates the mean for `INSTRUMENT1` and `INSTRUMENT2` (specifically for November 2014).
  - `StandardDeviationCalculator`: Computes the standard deviation for `INSTRUMENT3`.
  - `LatestValuesCalculator`: Sums up the newest 10 elements for any other instruments.
- **`InstrumentMultiplierCache`**: Caches multipliers for instrument prices, reducing database queries. Refreshes every
  5 seconds.
- **Database Integration**: Utilizes an embedded h2 database to store and retrieve instrument price modifiers.

## Tests

- **`CalculationServiceIntegrationTest`**: Performs integration testing with the example_input.txt file to validate the
  correctness of calculations.

- **`CalculationPerformanceTest`**: Simulates processing of 10 million rows to test the performance and scalability of
  the service. (Takes about 30 seconds to complete.)

## Design Decisions

- **Scalability**: Streams data and uses primitive data types where possible to reduce memory footprint. Scales the data
  to perform integer arithmetic to avoid floating point errors.
- **Performance Optimization**: Utilizes caching for the multiplier retrival to reduce database queries.
- **Modularity**: Adheres to OO principles. The configuration for what calculations to perform is held in the two
  Factory classes. In this future this could be moved to a configuration file or stored in a database.

