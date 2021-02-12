# Toteutus

### CellularAutomata

Algoritmi tuottaa kaksiulotteisen totuusarvotaulukon, joka kuvaa generoitua karttaa. 
Taulukossa totuusarvo `true` tarkoittaa lattiaa ja `false` seinää. Kartan generointi 
tapahtuu vaiheittain seuraavasti:

1. Taulukko alustetaan satunnaisilla totuusarvoilla.

2. Muodostetaan soluautomaatiolla varsinaiset lattia-alueet eli huoneet.

3. Selvitetään erilliset huoneet rekursiivisella flood-fill -algoritmilla.

4. Poistetaan kaikki huoneet, jotka eivät täytä minimikoon ehtoa.

5. Yhdistellään huoneita toisiinsa Bresenhamin algoritmilla.

Algoritmille annettavat parametrit:

- Kartan leveys: solujen määrä x-akselilla.

- Kartan korkeus: solujen määrä y-akselilla.

- Seed: satunnaislukugeneraattorin alkuarvo.

- Täyttöaste: määrää totuusarvojen jakauman alustusvaiheessa.

- Tasoituskertoja: määrää, montako kertaa karttaa prosessoidaan vaiheessa 2.

- Huoneiden minimikoko: tätä pienemmän solumäärän omaavat huoneet poistetaan vaiheessa 4.

- Käytävien koko: vaiheessa 5 muodostettavien käytävien koko.