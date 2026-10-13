# SWE307 Proje 1 — Grup 2

Bu başlangıç projesi Java 17, GraalVM CE 22.3.0, FastR ve lattice için hazırlanmıştır.
Spring Boot 2.7.18 kullanılır. R, Java içindeki Polyglot Context ile çalışır.
Veriyi Java okur; R yalnızca grafik oluşturur. Her istek arasında en az bir saniye
olacak şekilde sıradaki MongoDB kaydı alınır. Tarayıcı açıkken grafik bir saniye
arayla yenilenir. 100 değer tamamlanınca son grafik korunur; uygulamayı yeniden
başlatmak gösterimi baştan başlatır. Tüm tarayıcılar aynı gösterimi paylaşır.

## Ubuntu'da çalıştırma

Projeyi Ubuntu içinde `~/swe307-grup2` klasörüne kopyalayın. Komutlar:

```bash
cd ~/swe307-grup2
export JAVA_HOME=/home/semih/graalvm-ce-java17-22.3.0
export PATH="$JAVA_HOME/bin:$PATH"
bash mvnw -version
bash mvnw clean package
java -jar target/grup2-0.0.1-SNAPSHOT.jar
```

Maven Wrapper ilk çalıştırmada Maven ve bağımlılıkları indirir. Ayrı bir Maven
kurulumu gerekmez. IntelliJ'de pom.xml dosyasını proje olarak açabilirsiniz;
çalıştırma için WSL içindeki GraalVM kullanılmalıdır.

Tarayıcı: http://localhost:8080
Başlangıç kontrolü: http://localhost:8080/api/status

## MongoDB verisi — sonraki adım

Varsayılan bağlantı `mongodb://localhost:27017/swe307_group2`.
MongoDB Windows'ta çalışıyorsa WSL'den bağlantı ayrıca doğrulanmalıdır.
Gerekirse MONGODB_URI ortam değişkeniyle bağlantı adresini belirtin.
Mevcut koleksiyonlara otomatik veri yazılmaz veya veri silinmez.

Beklenen koleksiyon: `measurements`. Hocanın CSV dosyasının ikinci sütunundaki
100 değer aşağıdaki biçime dönüştürülmelidir; bu iskelet henüz CSV aktarmamaktadır:

```json
{"index": 0, "value": 12.34}
```

Buradaki 12.34 sadece kayıt biçimini açıklayan örnektir, ödev verisi değildir.
index 0–99 arasında, value sayısal olmalıdır. Her index için tek kayıt kullanılmalı
ve index alanına benzersiz indeks konulmalıdır. İçe aktarma bağlantı ve CSV başlığı
doğrulandıktan sonra birlikte yapılacaktır. Veri yokken sayfa açıklayıcı hata gösterir.

## Doğrulama durumu

Proje XML ve Wrapper dosyaları kontrol edildi. Bağımlılık indirmesi yavaş olduğundan
derleme doğrulaması henüz tamamlanmadı. Ubuntu içinde bash mvnw clean package
ile doğrulanmalıdır. FastR ile
PNG üretimi ve MongoDB uçtan uca akışı kullanıcının Ubuntu ortamında doğrulanmalıdır.
