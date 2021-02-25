# Testaus

Ohjelmalle on määritelty sekä yksikkö- että suorituskykytestejä.

## Yksikkötestaus

Yksikkötestit suoritetaan komennolla

```
gradlew test
```

Testikattavuusraportti luodaan kansioon _build/reports/jacoco/test/html_ komennolla

```
gradlew jacocoTestReport
```

## Suorituskykytestaus

Suorituskykytestit suoritetaan komennolla
```
gradlew performanceTest
```