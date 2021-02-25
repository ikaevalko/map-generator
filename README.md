# Karttageneraattori

Aineopintojen harjoitustyö: Tietorakenteet ja algoritmit (2021, periodi 3)
Tietojenkäsittelytieteen kandiohjelma

Harjoitustyön aiheena on tietokonepeleistä tuttu kaksiulotteinen karttageneraattori. Toteutus Java-kielellä ja käyttöliittymäkirjastona JavaFX.

## Dokumentaatio

[Määrittely](dokumentaatio/maarittelydokumentti.md)

[Toteutus](dokumentaatio/toteutusdokumentti.md)

[Testaus](dokumentaatio/testausdokumentti.md)

## Viikkoraportit

[Viikkoraportti 1](dokumentaatio/viikkoraportti1.md)

[Viikkoraportti 2](dokumentaatio/viikkoraportti2.md)

[Viikkoraportti 3](dokumentaatio/viikkoraportti3.md)

[Viikkoraportti 4](dokumentaatio/viikkoraportti4.md)

[Viikkoraportti 5](dokumentaatio/viikkoraportti5.md)

## Ohjelman suorittaminen

```
gradlew run
```

## Suoritettavan JAR-tiedoston generointi

```
gradlew shadowJar
```

Generoitu tiedosto löytyy kansiosta _build/libs_

## Yksikkötestaus

Yksikkötestit suoritetaan komennolla
```
gradlew test
```

Testikattavuusraportti luodaan kansioon _build/reports/jacoco/test/html_ komennolla
```
gradlew jacocoTestReport
```
Luotua raporttia voi tarkastella selaimella avaamalla tiedosto _index.html_

## Suorituskykytestaus

Suorituskykytestit suoritetaan komennolla
```
gradlew performanceTest
```

## Checkstyle

Tiedostossa _config/checkstyle/checkstyle.xml_ määritellyt tarkistukset suoritetaan komennolla

```
gradlew checkstyleMain
```

Checkstyle-tarkistuksen tulokset löytyvät tiedostosta _build/reports/checkstyle/main.html_

## Javadoc

Javadoc luodaan kansioon _build/docs/javadoc_ komennolla

```
gradlew javadoc
```