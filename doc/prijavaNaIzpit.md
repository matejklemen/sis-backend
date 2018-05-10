# Prijava na izpit (#21)
##### 1. Preveri regularen potek za prvo, drugo, tretje polaganje pred iztekom roka.
U: sz8003@student.uni-lj.si, P: sz_63180003
- študent je redno vpisan v 3. letnik in mora videti vse izpitne roke
- prijavi se na 1. rok za nek predmet in nato odjavi in enako za 2. in 3. rok

##### 2. Preveri izračun celotnega števila polaganj in števila polaganj v tekočem študijskem letu. 
TODO: kako bomo to pokazali? Nek izpis na frontendu? V ozadju se to že upošteva.

##### 3. Preveri regularen potek za prijavo na izpit iz prejšnjega letnika.
U: žr8007@student.uni-lj.si, P: žr_63180007
- študent ponavlja 2. letnik, videti mora izpite iz APS1, APS2, VIS, IRZ (**2. letnik**) in OUI (**3. letnik**)
- prijavi se na 1. rok za nek predmet iz 2. letnika (npr. APS2)

##### 4. Preveri za prijavo po izteku roka.
U: žr8007@student.uni-lj.si, P: žr_63180007
- pokaži, da je gumb za prijavo na izpit, ki je že mimo, onemogočen

##### 5. Preveri za prijavo na že opravljen izpit. 
U: žr8007@student.uni-lj.si, P: žr_63180007
- izpitnih rokov za predmete, ki jih je študent že naredil, sistem sploh ne prikaže

##### 6. Preveri za prijavo, pri kateri še ni preteklo dovolj dni od zadnjega polaganja.
TODO

##### 7. Preveri za prekoračitev števila polaganj v tekočem študijskem letu (največ 3).
TODO

##### 8. Preveri za prekoračitev celotnega števila polaganj (največ 6).
U: ip8012@student.uni-lj.si, P: ip_63180012
- študent je izredno vpisan v 2. letnik, izbral je samo predmet **Verjetnost in statistika**
- predmet je polagal že 6-krat
- poizkusi se prijaviti na izpit pri predmetu VIS in pokaži, da je prijava zavrnjena

##### 9. Preveri za prijavo na rok, za katerega že obstaja prijava.
U: sz8003@student.uni-lj.si, P: sz_63180003
- vpiši se na 1. rok nekega predmeta, nato pa se poizkusi vpisati na 2. rok tega istega predmeta in pokaži, da je prijava zavrnjena

##### 10. Preveri za prijavo, kjer za prejšnji rok še ni bila zaključena ocena.
U: sz8003@student.uni-lj.si, P: sz_63180003
- vpiši se na 1. rok nekega predmeta, nato pa se poizkusi vpisati na 2. rok tega istega predmeta
- (med drugim) bo napisalo, da prijava ni mogoča zato, ker za prejšnji rok še ni vnešena ocena

##### 11. Preveri za prijavo vpisanega študenta, ki mora plačati izpit.
U: žr8007@student.uni-lj.si, P: žr_63180007
- študent je že 3-krat opravljal izpit pri predmetu APS1
- vpiši se na enega izmed rokov pri predmetu APS1 (to bo 4. polaganje)

###### 12. Preveri za prijavo nevpisanega študenta, ki mora plačati izpit.
U: jd8004@student.uni-lj.si, P: jd_63180004
- študent je že 5-krat opravljal izpit pri predmetu FIZ in je izredno vpisan zgolj na predmet Fizika
- vpiši se na enega izmed rokov pri predmetu FIZ (to bo 6. polaganje)

##### 13. Preveri za prijavo ponavljalca, ki se mu odštejejo polaganja iz prvega vpisa (za primer ko mu ni treba plačati izpita in za primer, ko mora plačati izpit). 
TODO