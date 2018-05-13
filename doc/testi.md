# Prijava na izpit (#21)

###
- [x] 1. Preveri regularen potek za prvo, drugo, tretje polaganje pred iztekom roka.
- [x] 2. Preveri izračun celotnega števila polaganj in števila polaganj v tekočem študijskem letu. 
- [x] 3. Preveri regularen potek za prijavo na izpit iz prejšnjega letnika.
- [x] 4. Preveri za prijavo po izteku roka.
- [x] 5. Preveri za prijavo na že opravljen izpit. 
- [x] 6. Preveri za prijavo, pri kateri še ni preteklo dovolj dni od zadnjega polaganja.
- [x] 7. Preveri za prekoračitev števila polaganj v tekočem študijskem letu (največ 3).
- [x] 8. Preveri za prekoračitev celotnega števila polaganj (največ 6).
- [x] 9. Preveri za prijavo na rok, za katerega že obstaja prijava.
- [x] 10. Preveri za prijavo, kjer za prejšnji rok še ni bila zaključena ocena.
- [x] 11. Preveri za prijavo vpisanega študenta, ki mora plačati izpit.
- [x] 12. Preveri za prijavo nevpisanega študenta, ki mora plačati izpit.
- [x] 13. Preveri za prijavo ponavljalca, ki se mu odštejejo polaganja iz prvega vpisa (za primer ko mu ni treba plačati izpita in za primer, ko mora plačati izpit). 

##### 1. Preveri regularen potek za prvo, drugo, tretje polaganje pred iztekom roka.
`sz8003@student.uni-lj.si` - `sz_63180003`
- študent je redno vpisan v 3. letnik in mora videti vse izpitne roke
- prijavi se na 1. rok za nek predmet
- prijavi se na enega izmed razpoložljivih izpitov za IS (to bo 2. polaganje)
- prijavi se na edini še razpoložljivi izpit za OUI (to bo 3. polaganje)

##### 2. Preveri izračun celotnega števila polaganj in števila polaganj v tekočem študijskem letu. 
- to izpiše pri vsaki prijavi na izpit

##### 3. Preveri regularen potek za prijavo na izpit iz prejšnjega letnika.

\[**MAIN**\]
`mj8005@student.uni-lj.si` - `mj_63180005`
- študent je redno vpisan v 2. letnik, poleg tega pa še za nazaj opravlja predmet **Fizika**
- prijavi se na enega izmed rokov za fiziko


\[**BONUS**\]
`žr8007@student.uni-lj.si` - `žr_63180007`
- študent ponavlja 2. letnik, videti mora izpite iz APS1, APS2, VIS, IRZ (**2. letnik**) in OUI (**3. letnik**)
- prijavi se na 1. rok za nek predmet iz 2. letnika (npr. APS2)

##### 4. Preveri za prijavo po izteku roka.
`žr8007@student.uni-lj.si` - `žr_63180007`
- pokaži, da je gumb za prijavo na izpit, ki je že mimo, onemogočen (vizualno je onemogočen, vendar se ob kliku še vedno odpre modalno okno - v primeru poskusa prijave se izpiše napaka "rok za prijavo je potekel". Če je prijavljen referent, se mu pokaže gumb "vseeno prijavi").
`sz8003@student.uni-lj.si` - `sz_63180003`
- poizkusi se odjaviti od izpita Ekonomika in podjetništvo 12.5.2018 ob 14:00

##### 5. Preveri za prijavo na že opravljen izpit. 
`lh8006@student.uni-lj.si` - `lh_63180006`
- študent ima že prej v bazo vstavljeno prijavo na APS1 in (pozitivno) oceno za ta izpit
- poizkusi se prijaviti na enega izmed preostalih možnih rokov APS1 (npr. 4.9.2018 ob 13:00) in pokaži, da prijava ni uspela
- izpitnih rokov za predmete, ki jih je študent že naredil **v lanskih letih**, sistem sploh ne prikaže

##### 6. Preveri za prijavo, pri kateri še ni preteklo dovolj dni od zadnjega polaganja.
`mj8005@student.uni-lj.si` - `mj_63180005`
- prijavi se na pisni izpit iz fizike 24.8.2018 ob 17:00
- poizkusi se prijaviti na ustni izpit iz fizike 29.8.2018 ob 15:00 in pokaži, da je prijava zavrnjena

##### 7. Preveri za prekoračitev števila polaganj v tekočem študijskem letu (največ 3).
`lh8006@student.uni-lj.si` - `lh_63180006`
- študent ima že prej v bazo vstavljene 3 prijave na predmet **Verjetnost in statistika** (vse 3 prijave imajo določeno negativno oceno)
- poizkusi se prijaviti na 4. rok za predmet **Verjetnost in statistika** in pokaži, da je prijava zavrnjena
##### 8. Preveri za prekoračitev celotnega števila polaganj (največ 6).
`ip8012@student.uni-lj.si` - `ip_63180012`
- študent je izredno vpisan v 2. letnik, izbral je samo predmet **Verjetnost in statistika**
- predmet je polagal že 6-krat
- poizkusi se prijaviti na izpit pri predmetu VIS in pokaži, da je prijava zavrnjena

##### 9. Preveri za prijavo na rok, za katerega že obstaja prijava.
`sz8003@student.uni-lj.si` - `sz_63180003`
- vpiši se na 1. rok nekega predmeta, nato pa se poizkusi vpisati na 2. rok tega istega predmeta in pokaži, da je prijava zavrnjena

##### 10. Preveri za prijavo, kjer za prejšnji rok še ni bila zaključena ocena.
`sz8003@student.uni-lj.si` - `sz_63180003`
- vpiši se na 1. rok nekega predmeta, nato pa se poizkusi vpisati na 2. rok tega istega predmeta
- (med drugim) bo napisalo, da prijava ni mogoča zato, ker za prejšnji rok še ni vnešena ocena

##### 11. Preveri za prijavo vpisanega študenta, ki mora plačati izpit.
`žr8007@student.uni-lj.si` - `žr_63180007`
- študent je že 3-krat opravljal izpit pri predmetu APS1
- vpiši se na enega izmed rokov pri predmetu APS1 (to bo 4. polaganje)

###### 12. Preveri za prijavo nevpisanega študenta, ki mora plačati izpit.
`jd8004@student.uni-lj.si` - `jd_63180004`
- študent mora plačati za vse izpite

##### 13. Preveri za prijavo ponavljalca, ki se mu odštejejo polaganja iz prvega vpisa (za primer ko mu ni treba plačati izpita in za primer, ko mora plačati izpit). 
`žr8007@student.uni-lj.si` - `žr_63180007`
- študent ponavlja 2. letnik
- ko je redno delal 2. letnik, je 2-krat (neuspešno) opravljal predmet IRZ
- prijavi se na rok IRZ in pokaži, da je to 3. polaganje predmeta IRZ (in ne 1.)


# Odjava izpita (študent) (#22)

###
- [ ] 1. Preveri možnost odjave pred iztekom roka za odjavo.
- [ ] 2. Preveri možnost odjave po izteku roka za odjavo.
`sz8003@student.uni-lj.si` - `sz_63180003`
    - poizkusi se odjaviti od izpita Ekonomika in podjetništvo 12.5.2018 ob 15:00
- [ ] 3. Preveri, da se zabeležijo podatki o odjavi (odjavitelj in točen čas odjave).
- [ ] 4. Preveri, da odjavljenega študenta ni več v seznamu prijavljenih za izpit.*

# Prijava na izpit (referentka) (#23)

###
- [x] 1. Preveri za vse sprejemne teste iz zgodbe #21


# Odjava izpita (referentka) (#24)

###
Vsi testi: `janez@nov.ak` `123`

- [x] Preveri možnost odjave pred iztekom roka za odjavo. (Računalniške komunikacije -> 15.6.2018 ob 08:00)
- [x] Preveri možnost odjave po izteku roka za odjavo. (Fizika -> 26.1.2018 ob 17:00)
- [x] Preveri za prijavo z že vpisano oceno pisnega dela izpita. (Osnove matematične analize -> 26.1.2018 ob 08:00)
- [x] Preveri za prijavo z že vpisano končno oceno. (Osnove matematične analize -> 26.1.2018 ob 08:00)
