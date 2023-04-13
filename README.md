# Fmss e-commerce template

<br>

Bu projede, kullanıcıların kaydolabileceği, şifre değiştirebileceği, sepete ürün ekleyebileceği, sepetteki ürün sayısını düzenleyebileceği ve sipariş verip ödeme yapabileceği bir e-ticaret uygulaması geliştirilmiştir.

<br>

## Tech Stack
    Spring Boot
    Open Feign
    Eureka Server/Client
    Spring Data JPA
    Open API
    Lombok
    Mapstruct
    Redis
    Kafka
    ELK Logstash
    ElasticSearch
    MongoDB
    PostgreSQL
    Prometheus
    Grafana
    Kubernetes
    H2 Database
    Spring Config Server
    Zipkin
    Gradle

<br>

## Servisler ve İşlevleri

### User Servis
<ul>
<li>Kullanıcıların sisteme kayıt olmasını sağlar.</li>
<li>Kullanıcıların şifre değiştirme işlemini gerçekleştirir.</li>
</ul>

### Basket Servis
<ul>
<li>Sepete ürün ekleme işlemini gerçekleştirir.</li>
<li>Sepetteki ürün sayısını artırma veya azaltma işlemini gerçekleştirir.</li>
<li>Sepeti tamamen boşaltma işlemini gerçekleştirir.</li>
</ul>

### Order Servis
<ul>
<li>Sipariş işlemlerini yönetir ve gerçekleştirir.</li>
</ul>

### Payment Servis
<ul>
<li>Ödeme işlemlerini gerçekleştirir ve yönetir.</li>
</ul>

### Product Servis
<ul>
<li>Ürünleri ID ile getirme işlemini gerçekleştirir.</li>
<li>Tüm ürünleri getirme işlemini gerçekleştirir.</li>
</ul>

