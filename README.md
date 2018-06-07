# sis

A student information system made for a software engineering course.

## First time set-up:

* Set up application: Run -> Edit Configurations -> New "Application" -> set main class to: `com.kumuluz.ee.EeApplication` and "Use classpath of module:" to `api` 
* Set up local DB with Docker: 
	1.  `docker run -d --name postgres-jdbc -e POSTGRES\_PASSWORD=postgres -e POSTGRES\_DB=sis -p 5432:5432 postgres:latest`
	2.  `docker start postgres-jdbc` (if not already running)
* Add source in IntelliJ:
	1.  Database -> Add -> Postgres
	2.  Host: localhost or ip
	3.  Database: sis
	4.  User: postgres
	5.  Password: postgres
	6.  Test Connection
* In `entities/src/main/resources/config.yaml`, update jdbc/sisTestDB `connection-url` parameter's IP to match your docker IP.
* Optional: assign data source (Persistence Tab)

## Development cycle:
1.  `docker start postgres-jdbc`
2.  `maven clean package`

## API documentation
The project uses OpenAPI specification 3. Documentation hosted on:
http://localhost:8080/api-specs/ui (when running locally)

## Using API filters

Examples on how to use filters, offsets and limits are availiable at [kumuluzee-rest#examples](https://github.com/kumuluz/kumuluzee-rest#examples).

## Sample users
|     Role     | Role id |         Username        | Password | Enrolments | Study program |
|:------------:|:-------:|:-----------------------:|:--------:|:-------------:|:-------------:|
|  Skrbnik/ca  |    1    |          `joze1`          |    123   |       /       |       /       |
|  Študent/ka  |    2    |       `blazkablatnik@gmail.com`      |    123   |       none (no data)      |      whatever      |
|  Študent/ka  |    2    |    `peter@kopljem.net`    |    123   |       none (but with data)      |      whatever      |
|  Študent/ka  |    2    |       `janez@nov.ak`      |    123   |      1     |      BUN      |
|  Študent/ka  |    2    |     `marta@pod.streho`    |    123   |       1,2,2       |      BUN      |
|  Študent/ka  |    2    |       `steff@stef.anio`      |    123   |       1,2       |      BUN      |
| Profesor/ica |    3    | `prof@fri.uni-lj.si` |    123   |       /       |       /       |
| Profesor/ica |    3    | `gasper.fijavz@budget-studis.si` |    123   |       /       |       /       |
| Profesor/ica |    3    | `blaz.zupan@budget-studis.si` |    123   |       /       |       /       |
| Profesor/ica |    3    | `aleksandar.jurisic@budget-studis.si` |    123   |       /       |       /       |
| Profesor/ica |    3    | `miha.mraz@budget-studis.si` |    123   |       /       |       /       |
| Profesor/ica |    3    | `neza.mramor-kosta@budget-studis.si` |    123   |       /       |       /       |
| Profesor/ica |    3    | `bostjan.slivnik@budget-studis.si` |    123   |       /       |       /       |
|  Referent/ka |    4    |    `ref@fri.uni-lj.si`    |    123   |       /       |       /       |
|  Študent/ka  |    2    | `sz8003@student.uni-lj.si` | sz_63180003 | 1, 2, 3 |       BUN     |
|  Študent/ka  |    2    | `jd8004@student.uni-lj.si` | jd_63180004 | 1, 1 |     BUN     |
|  Študent/ka  |    2    | `mj8005@student.uni-lj.si` | mj_63180005 | 1, 2 | BUN |
|  Študent/ka  |    2    | `lh8006@student.uni-lj.si` | lh_63180006 | 1, 2 | BUN |
|  Študent/ka  |    2    | `žr8007@student.uni-lj.si` | žr_63180007 | 1, 2, 2 | BUN |
|  Študent/ka  |    2    | `tz8008@student.uni-lj.si` | tz_63180008 | 1, 2, 3, 3 | BUN |
|  Študent/ka  |    2    | `rb8009@student.uni-lj.si` | rb_63180009 | 1 | BUN |
|  Študent/ka  |    2    | `nk8010@student.uni-lj.si` | nk_63180010 | 1, 1, 2 | BUN |
|  Študent/ka  |    2    | `cl8011@student.uni-lj.si` | cl_63180011 | 1, 1 | BUN |
|  Študent/ka  |    2    | `ip8012@student.uni-lj.si` | ip_63180012 | 1, 2, 2, 3 | BUN |
|  Študent/ka  |    2    | `jj8013@student.uni-lj.si` | jj_63180013 | 1, 2, 3 | BUN |
| / | / | / | / | / | / |
|  Študent/ka  |    2    | `sb8100@student.uni-lj.si` | sb_63180100 | 1, 2 | BUN |
|  Študent/ka  |    2    | `rb8101@student.uni-lj.si` | rb_63180101 | 1, 2 | BUN |
|  Študent/ka  |    2    | `tg8102@student.uni-lj.si` | tg_63180102 | 1, 2 | BUN |
|  Študent/ka  |    2    | `dk8103@student.uni-lj.si` | dk_63180103 | 1, 2 | BUN |
|  Študent/ka  |    2    | `zr8104@student.uni-lj.si` | žr_63180104 | 1, 2 | BUN |
|  Študent/ka  |    2    | `nz8105@student.uni-lj.si` | nz_63180105 | 1, 2 | BUN |
|  Študent/ka  |    2    | `ja8119@student.uni-lj.si` | ja_63180119 | 1, 2 | BUN |
|  Študent/ka  |    2    | `rc8120@student.uni-lj.si` | rc_63180120 | 1, 2 | BUN |
|  Študent/ka  |    2    | `ld8121@student.uni-lj.si` | ld_63180121 | 1 | BUN |
|  Študent/ka  |    2    | `if8122@student.uni-lj.si` | if_63180122 | 1 | BUN |
|  Študent/ka  |    2    | `lf8123@student.uni-lj.si` | lf_63180123 | 1 | BUN |
|  Študent/ka  |    2    | `zh8124@student.uni-lj.si` | zh_63180124 | 1 | BUN |
|  Študent/ka  |    2    | `nj8125@student.uni-lj.si` | nj_63180125 | 1 | BUN |
|  Študent/ka  |    2    | `zj8126@student.uni-lj.si` | žj_63180126 | 1 | BUN |

## Pregled študentov
|#|Vpisna številka|Ime|Priimek|Notes|
|:------------:|:-------:|:-----------------------:|:--------:|:-------------:|
|1|63180092|Tom|Dekleva|1 2 3|
|2|63180091|Tom|Fernc|1 2 3|
|3|63180090|Tom|Jeras|1 2 3|
|4|63180089|Tom|Kacjan|1 2 3|
|5|63180088|Nina|Peternel|1 2 3|
|6|63180087|Nina|Jurgec|1 2 3|
|7|63180086|Nina|Destovnik|1 2 3|
|8|63180085|Nina|Bavec|1 2 3|
|9|63180084|Nik|Abram|1 2 3|
|10|63180083|Nik|Abram|1 2 3|
|11|63180082|Nik|Avbelj|1 2 3|
|12|63180081|Nik|Peternel|1 2 3|
|13|63180080|Milan|Bojc|1 2 3|
|14|63180079|Milan|Fabinc|1 2 3|
|15|63180078|Milan|Kacjan|1 2 3|
|16|63180077|Milan|Destovnik|1 2 3|
|17|63180076|Nejc|Ankel|1 2 3|
|18|63180075|Nejc|Ivanc|1 2 3|
|19|63180074|Nejc|Baraga|1 2 3|
|20|63180073|Nejc|Bavec|1 2 3|
|21|63180072|Luka|Bojc|1 2 3|
|22|63180071|Luka|Polec|1 2 3|
|23|63180070|Luka|Kacjan|1 2 3|
|24|63180069|Luka|Bizjak|1 2 3|
|25|63180068|Jan|Fabinc|1 2 3|
|26|63180067|Jan|Ferk|1 2 3|
|27|63180066|Jan|Pavlin|1 2 3|
|28|63180065|Jan|Baraga|1 2 3|
|29|63180064|Julijan|Peternel|1 2 3|
|30|63180063|Julijan|Baraga|1 2 3|
|31|63180062|Julijan|Bavec|1 2 3|
|32|63180061|Julijan|Abram|1 2 3|
|33|63180060|Iza|Peternel|1 2 3|
|34|63180059|Iza|Fabinc|1 2 3|
|35|63180058|Iza|Kacjan|1 2 3|
|36|63180057|Iza|Kacjan|1 2 3|
|37|63180056|Ida|Fernc|1 2 3|
|38|63180055|Ida|Polec|1 2 3|
|39|63180054|Ida|Jager|1 2 3|
|40|63180053|Ida|Dekleva|1 2 3|
|41|63180052|Emil|Jakac|1 2 3|
|42|63180051|Emil|Destovnik|1 2 3|
|43|63180050|Emil|Abram|1 2 3|
|44|63180049|Emil|Baraga|1 2 3|
|45|63180048|Ivo|Ferk|1 2 3|
|46|63180047|Ivo|Jager|1 2 3|
|47|63180046|Ivo|Bizjak|1 2 3|
|48|63180045|Ivo|Destovnik|1 2 3|
|49|63180044|Eva|Ivanc|1 2 3|
|50|63180043|Eva|Polec|1 2 3|
|51|63180042|Eva|Ferk|1 2 3|
|52|63180041|Eva|Dekleva|1 2 3|
|53|63180040|Anton|Kacjan|1 2 3|
|54|63180039|Anton|Dekleva|1 2 3|
|55|63180038|Anton|Jelinkar|1 2 3|
|56|63180037|Anton|Ankel|1 2 3|
|57|63180036|Andrej|Arh|1 2 3|
|58|63180035|Andrej|Bizjak|1 2 3|
|59|63180034|Andrej|Jelinkar|1 2 3|
|60|63180033|Andrej|Destovnik|1 2 3|
|61|63180032|Alen|Jeras|1 2 3|
|62|63180031|Alen|Kacjan|1 2 3|
|63|63180030|Alen|Baraga|1 2 3|
|64|63180029|Alen|Destovnik|1 2 3|
|65|63180028|Ana|Ankel|1 2 3|
|66|63180027|Ana|Jelinkar|1 2 3|
|67|63180026|Ana|Kacjan|1 2 3|
|68|63180025|Ana|Ivanc|1 2 3|
|69|63180024|Alenka|Peternel|1 2 3|
|70|63180023|Alenka|Bojc|1 2 3|
|71|63180022|Alenka|Kacjan|1 2 3|
|72|63180021|Alenka|Ferk|1 2 3|
|73|63180020|Blazka|llalalalal|1 2 3|
|74|63180019|Željko|Češneđović|1 2 3|
|75|63180018|Kaja|Tutaja|1 2 3|
|76|63180017|Cene|Cepec|1 2 3|
|77|63180016|Bojan|Novak|1 2 3|
|78|63180015|Ana|Lužnik|1 2 3|
|79|63180013|Jaka|Jakomin|1 2 3|
|80|63180012|Imenko|Priimkovič| 1 2 2 3|
|81|63180011|Cene|Lukavčič| 1 1 |
|82|63180010|Nik|Kos| 1 1 2 |
|83|63180009|Rok|Berdnik| 1 1 |
|84|63180008|Teja|Zupanc| 1 2 3 3 |
|85|63180007|Žan|Rink| 1 2 2|
|86|63180006|Luka|Hribar| 1 2|
|87|63180005|Marko|Jelenc| 1 2|
|88|63180004|John|Doe| 1 1 ni_vpisa|
|89|63180003|Simon|Zore| 1 2 3|
|90|63180002|Peter|Kramp| ni_vpisa|
|91|63180001|Blažka|Blatnik|  ni_vpisa|
|92|63130003|Štefan|Čolnar| 2 1 2 ????|
|93|63130002|Marta|Strešnik| 1 2(ponavljanje) ???|
|94|63130001|Janez|Novak| 1|

[Podatki za teste](doc/testi.md)