# TransitFinder

## 1. Opis projektu

TransitFinder to aplikacja umożliwiająca wyszukiwanie połączeń komunikacji miejskiej oraz porównywanie dostępnych wariantów podróży.

Backend odpowiada za komunikację z silnikiem trasowania OpenTripPlanner, udostępnianie danych o przystankach i połączeniach oraz analizowanie znalezionych tras.

Aplikacja wykorzystuje dane komunikacji miejskiej ZTM Gdańsk oraz dane geograficzne OpenStreetMap dla obszaru województwa pomorskiego. Aktualny zakres projektu obejmuje Gdańsk.

### Główne funkcjonalności

- Wyszukiwanie przystanków na podstawie nazwy.
- Wyszukiwanie połączeń komunikacji miejskiej pomiędzy wybranymi lokalizacjami.
- Zwracanie dostępnych wariantów podróży wraz z czasem przejazdu, przesiadkami i szczegółami poszczególnych odcinków.
- Ocenianie wariantów podróży na podstawie określonych kryteriów.
- Generowanie rekomendacji i podsumowań tras za pomocą agenta AI.

## 2. Technologie

Projekt został zrealizowany z wykorzystaniem następujących technologii:

| Technologia | Zastosowanie |
|---|---|
| Java 25 | Główny język programowania |
| Spring Boot 4.1.2 | Framework backendowy |
| Spring Web | Tworzenie REST API |
| Spring AI 2.0.0 | Integracja z modelem językowym |
| OpenTripPlanner 2 | Wyszukiwanie połączeń komunikacji miejskiej |
| GraphQL | Komunikacja z OpenTripPlanner |
| WebClient | Wysyłanie zapytań HTTP do OTP |
| Jackson | Odczytywanie i serializacja JSON |
| Lombok | Ograniczenie powtarzalnego kodu |
| Docker | Uruchamianie usług zewnętrznych |
| Ollama | Lokalny serwer modeli językowych |
| React | Biblioteka do budowania interfejsu użytkownika z wykorzystaniem komponentów. |
| React Hooks | Zarządzanie stanem komponentów i efektami ubocznymi, m.in. za pomocą useState i useEffect |
| npm | Zarządzanie zależnościami i skryptami projektu |
| TypeScript | Lokalny serwer modeli językowych |
| Vite | Lokalny serwer modeli językowych |
| Tailwind CSS | Stylowanie interfejsu z wykorzystaniem klas narzędziowych |
| Axios | Wysyłanie zapytań HTTP do backendowego REST API. |
| TanStack Query (React Query) | Zarządzanie zapytaniami, stanem pobierania danych, obsługą błędów i mutacjami |

### Ciekawsze rozwiązania techniczne

**Integracja z OpenTripPlanner**

Wyszukiwanie połączeń realizowane jest przez OpenTripPlanner, który wykorzystuje dane GTFS oraz OpenStreetMap. Backend komunikuje się z OTP za pomocą GraphQL i WebClient.

**Deterministyczna ocena tras**

Ocena wariantów podróży jest obliczana po stronie Javy przez `JourneyScoringService`. Wykorzystywane są cztery kryteria:

| Kryterium | Waga |
|---|---:|
| Czas podróży | 35% |
| Liczba przesiadek | 30% |
| Czas transferów | 20% |
| Komfort pieszy | 15% |

Każde kryterium otrzymuje ocenę w skali od 1 do 5. Ocena końcowa jest obliczana jako suma ważona.

**Niezależny agent AI**

Analiza AI działa pod osobnym endpointem i nie blokuje wyszukiwania połączeń.

Agent korzysta z modelu `hadad/LFM2.5-1.2B:Q4_K_M`, uruchamianego lokalnie przez Ollama.

Model otrzymuje warianty podróży wraz z ocenami obliczonymi przez Javę. Jego zadaniem jest porównanie wariantów, wskazanie rekomendacji i przedstawienie uzasadnienia. AI nie oblicza ani nie modyfikuje ocen.

## 3. Wymagania

Przed uruchomieniem projektu należy zainstalować:

- Java 25.
- Maven (jeżeli projekt nie korzysta z dołączonego Maven Wrappera).
- Docker Desktop.
- Node.js i npm – do uruchomienia frontendu.

Wymagane są również przygotowane dane (gtfsgoogle.zip i pomorskie-261108.osm.pbf) i graf OpenTripPlanner. Graf sam się wygeneruje za pomocą tych dwóch plików.

## 4. Uruchomienie projektu lokalnie

Projekt składa się z backendu, frontendu oraz usług zewnętrznych.

### Krok 1. Uruchomienie usług zewnętrznych

OpenTripPlanner i Ollama mogą działać w kontenerach Docker.

Uruchom Docker Desktop, a następnie z katalogu zawierającego odpowiedni plik `docker-compose.yml` wykonaj:

```bash
docker compose up -d
```
Plik compose powinien wygenerować potrzebny graf dla OpenTripPlanner oraz pobrać model agenta ai dla Ollama.

Wymagane usługi:

| Usługa | Adres |
|---|---|
| Backend Spring Boot | http://localhost:8080 |
| OpenTripPlanner | http://localhost:8081 |
| Ollama | http://localhost:11434 |

OpenTripPlanner musi mieć wcześniej przygotowany graf zawierający dane GTFS ZTM Gdańsk oraz dane OpenStreetMap.

Model AI wykorzystywany przez agenta:

```text
hadad/LFM2.5-1.2B:Q4_K_M
```

Jeżeli model nie został jeszcze pobrany, można wykonać:

```bash
docker exec -it transitfinder-ollama ollama pull hadad/LFM2.5-1.2B:Q4_K_M
```

Ollama jest potrzebna do działania endpointu agenta AI. Wyszukiwanie przystanków i tras nie wymaga uruchomienia modelu językowego.

### Krok 2. Uruchomienie backendu

Otwórz terminal w katalogu `backend`.

Jeżeli projekt zawiera Maven Wrapper, na Windows wykonaj:

```powershell
.\mvnw.cmd spring-boot:run
```

Alternatywnie, jeśli Maven jest zainstalowany globalnie:

```bash
mvn spring-boot:run
```
Jeśli używasz środowiska Intelij wystarczy kliknąć zielony przycisk "run" dla pliku BackendApplication.java

Po uruchomieniu API będzie dostępne pod adresem:

```text
http://localhost:8080
```

### Krok 3. Uruchomienie frontendu

Otwórz drugi terminal i przejdź do katalogu `frontend`.

Zainstaluj zależności:

```bash
npm install
```

Uruchom aplikację:

```bash
npm run dev
```

Frontend powinien wyświetlić adres, pod którym jest dostępny w przeglądarce.

```text
http://localhost:5173
```

## 4. REST API

### Wyszukiwanie przystanków

**GET** `/api/stops/search`

Parametry:
- `query` – wyszukiwana nazwa przystanku.

Przykład:

```http
GET http://localhost:8080/api/stops/search?query=Gdańsk
```

Zwraca listę pasujących przystanków wraz z ich identyfikatorami i współrzędnymi geograficznymi.

### Wyszukiwanie połączeń

**POST** `/api/journey/search`

Endpoint przyjmuje dane dotyczące przystanku początkowego, docelowego oraz czasu rozpoczęcia podróży.

Przykładowe zapytanie:

```json
{
  "originName": "Dworzec Główny",
  "originLatitude": 54.3548956,
  "originLongitude": 18.6450828,
  "destinationName": "Pruszcz Gdański Osiedle Kasprowicza",
  "destinationLatitude": 54.2653419,
  "destinationLongitude": 18.6623404,
  "departureTime": "2026-11-11T08:00:00+01:00"
}
```

Odpowiedź zawiera listę dostępnych wariantów podróży, obejmującą m.in. czas rozpoczęcia i zakończenia podróży, całkowity czas przejazdu, liczbę przesiadek oraz szczegóły poszczególnych odcinków.

### Analiza tras przez agenta AI

**POST** `/api/agent/analysis`

Endpoint przyjmuje listę wariantów podróży otrzymanych wcześniej z endpointu wyszukiwania połączeń.

Agent ocenia warianty za pomocą `JourneyScoringService`, a następnie przekazuje wyniki do modelu językowego Ollama.

Odpowiedź zawiera tekstowe podsumowanie, rekomendację najlepszego wariantu oraz porównanie pozostałych możliwości.

Analiza jest wykonywana niezależnie od wyszukiwania tras i uruchamiana na żądanie użytkownika.
