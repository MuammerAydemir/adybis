# A.D.Y.B.I.S - Acil Durum Yardım Noktası ve Bildirim Sistemi

Afet veya acil durumlarda insanların konum bildirimi yapabildiği, yardıma
ihtiyacı olanların en yakın destek noktalarına yönlendirildiği ve yetkililerin kullanıcı
konumlarına göre müdahale planlaması yapabildiği bir sistem.

Uygulama **roller(admin,dispatcher,viewer,victim,rescue_team)** arasında yetkilendirilmiş ve 2FA içeren bir yapıya sahiptir.

## 🧩 Özellikler

- 🔐 Role-based Access Control (RBAC) – Admin, Dispatcher, Victim, Rescue Team, Viewer
- 📧 İki Faktörlü Kimlik Doğrulama (2FA) – Email doğrulama ile (MailHog ile localde)
- 📍 Yardım Noktaları (HelpPoints) ve Yardım Talepleri (HelpRequests)
- 🚑 Kurtarma Ekip Yönetimi ve Atama Sistemi
- 🧬 MapStruct tabanlı DTO ↔ Entity dönüşümleri
- 🗃️ Veritabanı tohumlama (Database seeding) CommandLineRunner ile
- 🧾 Swagger UI ile API dokümantasyonu

## ⚙️ Kurulum Talimatları

### Gereksinimler

- Java 17 veya üzeri
- Maven 3.9+
- Docker ve Docker Compose

### Projeyi Klonla

```bash
git clone https://github.com/MuammerAydemir/adybis.git
cd adybis
```

### Uygulama Ayarlarını Yapılandır

Uygulamada kullanılan ortam değişkenleri (örneğin veritabanı bağlantı bilgileri, e-posta servisi kimlik bilgileri vb.)
`.env` dosyasında tanımlanmalıdır.

Bu dosya hem `src/main/resources/application.properties` dosyasında **Spring Boot tarafından** hem de
`compose.yml` dosyasında **container ortam değişkeni olarak** kullanılacaktır.

Proje dizininde örnek bir şablon dosyası olan `.env.example` bulunmaktadır.
Bu dosyayı kopyalayarak kendi `.env` dosyanızı oluşturun ve değerleri kendinize göre güncelleyin:

```bash
cp .env.example .env
```

### Ortam Değişkenlerini Tanımlama

Uygulama yapılandırmasında kullanılan tüm ortam değişkenlerini .env dosyasında tanımladıktan sonra, bu değişkenlerin IDE veya sistem tarafından okunmasını sağlamanız gerekir.

**🔷 IDE Üzerinden Yükleme**

IDE’niz .env dosyasını doğrudan kullanabiliyorsa, proje kök dizinindeki .env dosyasını IDE’ye tanımlayabilirsiniz.
Örneğin Visual Studio Code için .vscode/launch.json dosyasına aşağıdaki ayarları ekleyin:

```json
{
  "configurations": [
    {
      "type": "java",
      "name": "Spring Boot - adybis",
      "request": "launch",
      "mainClass": "com.muammer.adybis.AdybisApplication",
      "envFile": "${workspaceFolder}/.env",
      "cwd": "${workspaceFolder}",
      "console": "integratedTerminal"
    }
  ]
}
```

Bu yapılandırma sayesinde, uygulama başlatıldığında _.env_ dosyasındaki tüm değişkenler otomatik olarak yüklenecektir.

**🔷 Sistem Ortamına Manuel Olarak Yükleme**

Alternatif olarak, ortam değişkenlerini terminal üzerinden sisteme yükleyebilirsiniz:

1. Linux / macOS:

```bash
export $(grep -v '^#' .env | xargs)
```

2. Windows (PowerShell):

```powershell
Get-Content .env | ForEach-Object {
    if ($_ -match "^(.*?)=(.*)$") {
        [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2])
    }
}
```

Bu komutlar, .env içindeki tüm değişkenleri geçerli terminal oturumu boyunca kullanılabilir hale getirir.
Sonrasında uygulamayı doğrudan şu şekilde çalıştırabilirsiniz:

```bash
mvn spring-boot:run
```

### Veritabanı Tohumlama (Database Seeder)

Uygulama ilk çalıştığında, `DatabaseSeeder` sınıfı test verilerini otomatik olarak yükler.

## 🧑‍💻 Kullanılan Teknolojiler

```markdown
- Spring Boot 3.5.7
- Spring Security
- Spring Data JPA
- Spring Mail
- Flyway
- PostgreSQL
- MapStruct 1.6.3
- Lombok
- Swagger / OpenAPI 2.8.12
- JJWT 0.13.0
- JavaFaker 1.0.2
- Thymeleaf
- Docker Compose
- Devtools
```

## 🧠 Mimari Genel Bakış

```markdown
📦 adybis
┣ 📂 base – Temel auth, konfigürasyon(Secutiry,OpenAPI,Properties vs.), 2FA auth, exception yönetimi,yardımcı sınıflar ve seeder
┣ 📂 user – Kullanıcı modelleri, servisleri ve DTO’lar
┣ 📂 role – Rol modelleri, servisleri ve DTO’lar
┣ 📂 help – Yardım noktaları ve talepler
┣ 📂 rescue – Kurtarma ekibi ve atamaları
┣ 📂 common – Ortak soyut sınıflar
┗ 📂 security – JWT ve güvenlik yapılandırmaları
⚙️ resources
┣ 📂 db – Migration dosyaları
┗ 📂 templates – Email template html dosyası
```

## 🔑 Örnek API İstekleri

Uygulamayı çalıştırdıktan sonra [Swagger-ui](http://localhost:8080/swagger-ui/index.html#/) ile endpointlere erişebilirsiniz. Kullanmak için test kullanıcıları yada kendi kayıt olduğunuz kullanıcı ile login olmanız gerekmektedir. Hesap onayından(verify) geçmemiş kullanıcılar endpointlere erişemez.

## ☑️ Hesap Doğrulama

## 🧪 Test Kullanıcıları

| Rol         | Kullanıcı Adı  | Parola      |
| ----------- | -------------- | ----------- |
| admin       | adminuser      | admin123?   |
| dispatcher  | dispatcheruser | dispac123?  |
| viewer      | vieweruser     | viewer123?  |
| victim      | victimuser1    | vic1tim123? |
| victim      | victimuser2    | vic2tim123? |
| rescue team | rescueteam1    | res1cu123?  |
| rescue team | rescueteam2    | res2cu123?  |

## 🪪 Lisans

Bu proje [GPL Lisansı](LICENSE) ile lisanslanmıştır.
Kullanım, değiştirme ve dağıtım serbesttir ancak orijinal lisans korunmalıdır.
