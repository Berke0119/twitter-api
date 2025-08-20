Twitter Clone Backend 🚀

Bu proje, Spring Boot ile geliştirilmiş bir Twitter Clone Backend uygulamasıdır.
Proje; PostgreSQL veritabanı, Redis cache, Docker Compose, Spring Security (JWT), JPA/Hibernate, Global Exception Handling gibi teknolojiler içerir.

----------------------------------------------------------------------------------------------------------------------
📌 Özellikler

Kullanıcı kaydı & login (JWT authentication)

Tweet paylaşma, silme, güncelleme

Like / Retweet / Comment işlemleri

Redis ile cache mekanizması

PostgreSQL üzerinde kalıcı veri saklama

Global Exception Handling ile hataların yönetimi

DTO – Entity dönüşümleri (Lombok + Mapper yapısı)

--------------------------------------------------------------------------------
🛠 Kullanılan Teknolojiler

Java 21

Spring Boot 3.x

Spring Web

Spring Security (JWT)

Spring Data JPA (Hibernate)

Validation

PostgreSQL

Redis

Docker & Docker Compose

Lombok

Maven

---------------------------------------------------------------------------
🧪 API Testleri

Proje, Postman ile test edilebilir.
Örnek endpointler:

Kayıt ol: POST /api/auth/register

Login: POST /api/auth/login

Tweet paylaş: POST /api/tweets

Tweetleri listele: GET /api/tweets

Like et: POST /api/tweets/{id}/like
Like et: POST /api/tweets/{id}/like
