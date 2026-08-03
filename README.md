# SmartTelco — Backend Staj Projesi

Telekom senaryosunda **iki servisli** bir Spring Boot backend uygulaması. Bir servis referans verisini (tarife, kampanya, vergi) yönetir; diğeri müşteri, fiyat teklifi ve abonelik iş akışlarını yürütür. İki servis birbirinin veritabanına dokunmaz, yalnızca HTTP üzerinden konuşur.

---

## Mimari

```
                 HTTP (RestClient)
  subscription-service  ───────────────►  reference-service
   (MongoDB, :8082)                         (PostgreSQL, :8081)
   müşteri / teklif / abonelik              tarife / kampanya / vergi
```

- **reference-service** — PostgreSQL üzerinde referans verisi. Salt okuma endpoint'leri sunar. Flyway migration'ları ile şema + seed verisi kurulur.
- **subscription-service** — MongoDB üzerinde iş akışları. Fiyat hesaplarken tarife ve vergiyi reference-service'ten HTTP ile çeker.

İki farklı veritabanı bilinçli bir tercihtir: referans verisi ilişkisel ve sabit olduğu için PostgreSQL; müşteri/teklif/abonelik ise snapshot içeren, büyüyen kayıtlar olduğu için MongoDB (document) ile modellenmiştir.

---

## Teknoloji

- Java 21, Spring Boot 3.5.16, Maven
- reference-service: Spring Data JPA, PostgreSQL, Flyway
- subscription-service: Spring Data MongoDB, Spring Web, Bean Validation
- springdoc-openapi (Swagger UI) — her iki serviste
- JUnit 5 — birim testleri

---

## Kurulum ve Çalıştırma

Gereksinimler: Java 21, Maven (ya da projedeki `mvnw` wrapper), Docker.

### 1. Veritabanlarını başlat (Docker)

PostgreSQL (reference-service için — reference-service klasöründeki compose ile):

```bash
cd reference-service
docker compose up -d
```

MongoDB (subscription-service için):

```bash
docker run -d --name smarttelco-mongo -p 27017:27017 -v smarttelco-mongo-data:/data/db mongo:7
```

### 2. reference-service'i çalıştır

```bash
cd reference-service
./mvnw spring-boot:run
```

Flyway şemayı ve seed verisini (4 tarife, 4 kampanya, kurallar, vergi) otomatik kurar. Sağlık kontrolü: `http://localhost:8081/actuator/health` → `UP`.

### 3. subscription-service'i çalıştır

```bash
cd subscription-service
./mvnw spring-boot:run
```

Sağlık kontrolü: `http://localhost:8082/actuator/health` → `UP`.

> Not: İki servisi ve MongoDB'yi tek komutla ayağa kaldıran kök `docker-compose` dosyası ileride eklenebilir. Şu an her servis ayrı çalıştırılır.

### API Dokümantasyonu (Swagger)

- reference-service: `http://localhost:8081/swagger-ui.html`
- subscription-service: `http://localhost:8082/swagger-ui.html`

### Postman

`postman/SmartTelco.postman_collection.json` dosyasını Postman'a import ederek uçtan uca akışı (müşteri → teklif → abonelik) çalıştırabilirsiniz.

---

## API Endpoint'leri

### reference-service (:8081)

| Metod | Yol | Açıklama |
| --- | --- | --- |
| GET | `/api/v1/plans` | Tüm tarifeler |
| GET | `/api/v1/plans/{planCode}` | Koda göre tek tarife |
| GET | `/api/v1/plans/{planId}/campaigns` | Tarifeye bağlı kampanya bağlantıları |
| GET | `/api/v1/campaigns` | Tüm kampanyalar |
| GET | `/api/v1/campaigns/{id}` | Id'ye göre tek kampanya |
| GET | `/api/v1/campaigns/{id}/rules` | Kampanyanın kuralları |
| GET | `/api/v1/taxes` | Tüm vergi tanımları |
| GET | `/api/v1/taxes/current` | Geçerli (aktif) vergi |

### subscription-service (:8082)

| Metod | Yol | Açıklama |
| --- | --- | --- |
| POST | `/api/v1/customers` | Müşteri oluştur |
| GET | `/api/v1/customers/{customerId}` | Müşteri getir |
| POST | `/api/v1/quotes` | Teklif oluştur (fiyat + uygunluk motoru) |
| GET | `/api/v1/quotes/{quoteId}` | Teklif getir |
| POST | `/api/v1/subscriptions` | Tekliften abonelik oluştur |
| GET | `/api/v1/subscriptions/{subscriptionId}` | Abonelik getir |
| GET | `/api/v1/subscriptions?customerId=...` | Müşterinin abonelikleri |
| POST | `/api/v1/subscriptions/{subscriptionId}/cancel` | Aboneliği iptal et |

---

## Örnek Akış (curl)

```bash
# 1. Müşteri oluştur
curl -X POST http://localhost:8082/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{"customerId":"C-1001","age":23,"segment":"YOUTH","tenureMonths":14,"currentPlanCode":"PLAN_BASIC_10","averageDataUsageGb":22,"monthlyBudget":500.00,"commitmentEndDate":null}'

# 2. Teklif oluştur (uygunsa kampanya indirimi uygulanır)
curl -X POST http://localhost:8082/api/v1/quotes \
  -H "Content-Type: application/json" \
  -d '{"customerId":"C-1001","planCode":"PLAN_YOUTH_25"}'
# -> quoteId döner (ör. Q-xxxx)

# 3. Tekliften abonelik oluştur
curl -X POST http://localhost:8082/api/v1/subscriptions \
  -H "Content-Type: application/json" \
  -d '{"customerId":"C-1001","quoteId":"Q-xxxx"}'
```

C-1001 (yaş 23, YOUTH, 14 ay) YOUTH_20 kampanyasına uygundur: baz 450 → %20 indirim 90 → ara toplam 360 → %20 vergi 72 → **toplam 432**.

---

## Uygunluk Motoru (Öne Çıkan Özellik)

Teklif oluşturulurken, tarifeye bağlı her kampanyanın kuralları müşteri profiline karşı değerlendirilir. Motor **açıklanabilirdir**: her kuralın sonucu (geçti/kaldı) ve gerekçesi teklif cevabında döner.

Kural değerlendirme **Strategy pattern** ile kurulmuştur. Her kural tipi kendi sınıfındadır:

- `AgeRangeEvaluator` — yaş aralığı (AGE_RANGE)
- `CustomerSegmentEvaluator` — segment eşitliği (CUSTOMER_SEGMENT)
- `MinTenureMonthsEvaluator` — minimum üyelik süresi (MIN_TENURE_MONTHS)
- `NoActiveCommitmentEvaluator` — aktif taahhüt yokluğu (NO_ACTIVE_COMMITMENT)

Motor hangi somut kuralla uğraştığını bilmez; Spring tüm `RuleEvaluator` bean'lerini otomatik toplar. Yeni bir kural tipi eklemek, mevcut kodu değiştirmeden yeni bir sınıf yazmakla mümkündür. Bir kural tipi için değerlendirici yoksa motor çökmez; kuralı "değerlendirilemedi" sayar ve zorunluysa kampanyayı eler.

Kampanya seçimi önce `priority`, eşitlikte toplam indirim değerine göre yapılır.

---

## Tasarım Kararları

- **İki veritabanı:** İlişkisel referans verisi PostgreSQL; snapshot içeren iş verisi MongoDB. Aynı problemde iki veri modelinin bilinçli kullanımı.
- **Snapshot:** Teklif oluşturulurken tarifenin o anki fiyatı ve adı teklife kopyalanır; abonelik oluşurken teklifin fiyatı aboneliğe kopyalanır. Referans veri sonradan değişse bile geçmiş teklif/abonelik değişmez.
- **DTO ayrımı:** API modeli (request/response record'ları) ile persistence modeli (document sınıfları) ayrıdır. Reference-service'ten gelen veri için ayrı DTO'lar tanımlanır; entity paylaşılmaz.
- **Katmanlı yapı:** controller (HTTP kapısı) → service (iş mantığı) → repository (veritabanı). İş kuralları controller'da değil service'te.
- **BigDecimal:** Tüm parasal hesaplar BigDecimal ile, iki ondalık ve HALF_UP yuvarlama ile yapılır. İndirim sonrası ara toplam sıfırın altına düşemez.
- **Merkezi hata yönetimi:** `@RestControllerAdvice` ile tüm hatalar tutarlı bir `ErrorResponse` formatında ve anlamlı HTTP koduyla döner (404 / 409 / 400 / 503).
- **Servis erişilemezliği:** reference-service kapalıyken subscription-service kontrollü **503** döner (çıplak 500 değil).

## İş Kuralları

- Kampanya uygunluğu: `mandatory` işaretli tüm kurallar sağlanmalıdır.
- Teklif geçerliliği: teklif 15 dakika geçerlidir; süresi dolmuş teklif üzerinden abonelik başlatılamaz.
- Teklif tek kullanımlıktır: abonelik oluşunca teklif CONSUMED olur, tekrar kullanılamaz.
- Tek aktif abonelik: bir müşterinin aynı anda yalnızca bir ACTIVE aboneliği olabilir.
- Müşteri iş anahtarı (`customerId`) benzersizdir; aynı id ile ikinci kayıt reddedilir.

---

## Testler

```bash
cd subscription-service
./mvnw test
```

Kapsam: kural değerlendiriciler (yaş, segment, tenure — sınır durumları dahil), indirim hesabı (yüzde/sabit), ve fiyat alt sınırı (indirim bazı geçince ara toplam 0'a sabitlenir, negatif fiyat üretilmez).

---

## Test Verisini Sıfırlama

Test sırasında MongoDB verisini temizlemek için:

```bash
docker exec -it smarttelco-mongo mongosh smarttelco --eval "db.customer_profile.deleteMany({}); db.quote.deleteMany({}); db.subscription.deleteMany({})"
```

Reference-service'in PostgreSQL verisine dokunmaz.

---

## Bilinen Sınırlar / İleride Eklenebilecekler

- Kök `docker-compose` (tek komutla tüm sistem) henüz yok; servisler ayrı çalıştırılır.
- Kampanya indirimlerinin birleştirilmesi (combinable) uygulanmaz; tek en iyi kampanya seçilir.
- Kalan kural tipleri (ör. bütçe, mevcut tarife) ileride eklenebilir.
