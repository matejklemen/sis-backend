- Preveri vnos novega sklepa (datum, organ, besedilo). 
- Preveri urejanje obstoječega sklepa
- Preveri brisanje sklepa
- Preveri, da se vsi sklepi izpišejo v okviru zgodbe Osebni podatki študenta
    - vpiši se v račun Štefana Čolnarja: `steff@stef.anio`, `123`, videti bi morali 2 sklepa
    - študenti brez sklepov razdelka 'Sklepi' sploh nimajo med osebnimi podatki

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
- študent mora plačati za vse izpite, ker ni vpisan (zadnji vpis prešnje šolsko leto)

###### 13. Preveri za prijavo ponavljalca, ki se mu odštejejo polaganja iz prvega vpisa (za primer ko mu ni treba plačati izpita in za primer, ko mora plačati izpit).
`ip8012@student.uni-lj.si` - `ip_63180012`
- študent je ponavljal 2. letnik
- ko je redno delal 2. letnik, je 2-krat (neuspešno) opravljal predmet verjetnost in statistika in ko je ponavljal letik še 3-krat neuspešno opravljal predmet verjetnost in statistika
- prijavi se na rok verjetnost in statistika in pokaži, da je to 4. polaganje predmeta verjetnost in statistika, in ne 6., ker se odštejeta dve polaganji iz rednega vpisa v drugi letnik, študent mora vseeno plačati, ker je presegel kvoto treh polaganj
- če v bazi spremenimo exam_sign_up z id-jem 165 (kar je polaganje iz ponavljanja drugega letnika) in damo returned na true, študentu ni potrebno plačati, ker je potem to njegovo tretje polaganje (verjetno bo treba spremenit v init-db.sql, ker bere iz pomnilnika)

# Sprememba izpitnega roka (#19)

###
- [x] 1. Preveri veljavnost novih izvajalcev.
*Na uporabniškem vmesniku se pokažejo samo izvajalci za izbrani predmet*
- [x] 2. Preveri veljavnost novega datuma izpita (večji od trenutnega, ne sme biti sobota, nedelja ali praznik)
- [x] 3. Preveri, ali obstajajo prijave (sprememba roka je možna ob potrditvi uporabnika).
    1. *Probaj spremenit izpit TPO 13.6.2018 ob 08:00.* (bo pustilo čez brez opozorila)
    2. *Vpiši študenta Simona Zoreta (`sz8003@student.uni-lj.si` - `sz_63180003`) na TPO izpit 13.6.2018 ob 08:00. in sedaj probaj popravit isti izpit.*
    (bo izpisalo opozorilo)
- [X] 4. Preveri, ali so se prijave prenesle na nov rok. *(nadaljevanje scenarija prejšnjih testov) Spremeni rok izpita pri predmetu TPO, vpiši se v račun Simona Zoreta in preveri, da je datum spremenjen, študent pa je še vseeno prijavljen na rok.*
- [X] 5. Preveri, ali so za ta rok že vpisane ocene (sprememba roka ni možna). *Ocene lahko vpisujemo samo v izpitne roke, katerih datum izvajanja je že mimo (teh izpitnih rokov pa ne moramo več spreminjati). Sicer pa ima za potrebe demota študent Simon Zore hardcodano prijavo na PRPO 13. 6. ob 10:00 z vnešeno oceno (tako da mora sistem pri urejanju tega roka izpisati napako).*

# Brisanje izpitnega roka (#20)
- briši nek izpitni rok, za katerega ne obstaja nobena prijava (mora it čez brez opozoril)
- briši izpitni rok TPO ali PRPO, omenjen v testih za zgodbo #19 (mora izpisat opozorilo)
- poglej na račun študenta Simona Zoreta (izpitni rok se mora izbrisati)

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



# Kartotečni list (#30)

###
- [x] Preveri izpis kartotečnega lista takoj po vpisu. Izpis mora obsegati vse predmete, ki jih je študent vpisal, brez ocen:  
		- Za: `peter@kopljem.net` `123`  
		- REF: generiraj token  
		- PETER: prijavi  
		- REF: potrdi  
		- REF/PETER/UČITELJ: poglej oba izpisa  
- [x] Vnesi nekaj ocen in preveri izpis na oba načina (vsa polaganja, samo zadnje polaganje).   
		- Za: `janez@nov.ak` `123`  
		- Ocene:   
			- OMA: 5, 5   
			- FIZIKA: 5, 6  
			- ODV: 8  
			- P1: 5, 5   
- [x] Preveri za študenta, ki je vpisan v več študijskih letih.   
		- Za: `ni8075@student.uni-lj.si` `ni_63180075` (Nejc Ivanc)  
- [x] Preveri za študenta, ki je vpisan v več študijskih programov.   
		- Za: `nima logina` (Rok Berdnik)  
- [x] Preveri izpis glave (študijsko leto, študijski program, letnik, vrsta vpisa, način študija) in noge (za vsako študijsko leto posebej število doseženih kreditnih točk in povprečna ocena).   
- [x] Preveri izpis podatkov o vsakem polaganju (šifra predmeta, ime predmeta, izpraševalci, datum izpita, zaporedna številka polaganja v celoti, zaporedna številka polaganja v tekočem študijskem letu, število kreditnih točk, ocena).   
- [x] Preveri izvoz v PDF in CSV.

# Naročilo potrdila o vpisu (#16)

###
Študent lahko naroči potrdilo o vpisu. Naročila se zbirajo na posebni datoteki. 
- [x] Preveri za vpisanega študenta:
	- Za: `janez@nov.ak` `123` (Janez Novak)
	- Naroči
- [x] Preveri za študenta brez statusa. 
	- Za: `jd8004@student.uni-lj.si` `jd_63180004` (John Doe)
	- Naroči
- [x] Preveri za absolventa (dodatno leto). 
	- Za: `tz8008@student.uni-lj.si` `tz_63180008` (Teja Zupanc)
	- Naroči
- [x] Preveri za različna števila izvodov. 
- [x] Preveri vsebino datoteke z naročili.


# Naročilo potrdila o vpisu (#17)

###
Referentka lahko izpiše potrdila o vpisu, ki so jih naročili študenti. 
- [x] Preveri, da se izpišejo vsa naročila. 
- [x] Preveri za vpisanega študenta. (Janez Novak)
- [x] Preveri za študenta brez statusa. (ne more niti naročiti)
- [x] Preveri za absolventa (dodatno leto). (Teja Zupanc)
- [x] Preveri za različna števila izvodov. 
- [x] Preveri možnost ponovnega tiskanja v primeru, da se papir zatakne.


# Elektronski indeks (#29)

###
Študent/referentka/učitelj lahko izpiše seznam vseh opravljenih izpitov. 

Študent lahko izpiše elektronski indeks samo zase, referentka in učitelj pa lahko izpišeta elektronski indeks za kateregakoli študenta. 
Opravljeni izpiti se izpišejo v kronološkem vrstnem redu. Na koncu se izpišejo število opravljenih izpitov, dosežene kreditne točke in povprečne ocene po posameznih študijskih letih ter sumarni podatki za celoten študij. 

- [ ] Preveri za študenta z opravljenimi izpiti v enem samem študijskem letu. 
- [ ] Preveri za študenta z opravljenimi izpiti v več študijskih letih (vmes naj bo tudi študijsko leto, ko ni opravil nobenega izpita). 
- [ ] Preveri za študenta, ki ni opravil nobenega izpita. 
- [ ] Preveri izvoz v PDF in CSV.


# Naročilo potrdila o opravljenih izpitih (#31)

###
Študent lahko naroči potrdilo o opravljenih izpitih. Potrdila se zbirajo na posebni datoteki.
- [ ] Preveri za vpisanega študenta. Za: `janez@nov.ak` `123` (Janez Novak)
- [ ] Preveri za študenta brez statusa. Za: `jd8004@student.uni-lj.si` `jd_63180004` (John Doe)
- [ ] Preveri za absolventa (dodatno leto). Za: `tz8008@student.uni-lj.si` `tz_63180008` (Teja Zupanc)
- [ ] Preveri za študenta, ki je bil vpisan v več študijskih programov. Za: `nima logina` (Rok Berdnik)  
- [ ] Preveri za različna števila izvodov (spodnja in zgornja meja). [!] FE nima meje?!
- [ ] Preveri vsebino datoteke z naročili.


# Tiskanje potrdil o opravljenih izpitih (#32)

###
Referentka lahko izpiše potrdila o opravljenih izpitih, ki so jih naročili študenti. 

- [ ] Preveri, da se vsa potrdila ujemajo s stanjem v kartotečnem listu in elektronskem indeksu. 
- [ ] Preveri, da se izpišejo vsa naročena potrdila. 
- [ ] Preveri možnost ponovnega tiskanja v primeru, da se papir zatakne.
