# Реализация кэширования 

Кэш - промежуточный буфер с быстрым доступом к нему, содержащий информацию, которая может быть запрошена с наибольшей вероятностью. Доступ к данным в кэше осуществляется быстрее, чем выборка исходных данных из более медленной памяти или удалённого источника, однако её объём существенно ограничен по сравнению с хранилищем исходных данных.

В корпоративных системах кэш зачастую реализуется In Memory key-value базой данных. Такой базой данных может выступать Redis.

Задачи на пару:
- Поднять Docker контейнер с Redis
- Подключиться к нему с использованием любого языка программирования 
- Выполнить операцию set
- Выполнить операцию get
- Выполнить операцию set с указанием TTL. Проверить, что ключ автоматически удаляется из БД по истечению времени. 
- Реализовать кеширование записей между базой данных/файлом и клиентом 

### Методические материалы
Скачать образ локально: ```docker pull redis```

Запустить контейнер: ```docker run --name some-redis -d redis```. При запуске нескольких контейнеров указать разные имена (флаг --name).

Работа с Redis на платформе Java: 

```
package org.example;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Main {
    public static void main(String[] args) {
        JedisPool pool = new JedisPool("localhost", 6379);

        try (Jedis jedis = pool.getResource()) {
            // Store & Retrieve a simple string
            jedis.set("foo", "bar");
            System.out.println(jedis.get("foo")); // prints bar
            
            // Store & Retrieve a HashMap
            Map<String, String> hash = new HashMap<>();;
            hash.put("name", "John");
            hash.put("surname", "Smith");
            hash.put("company", "Redis");
            hash.put("age", "29");
            jedis.hset("user-session:123", hash);
            System.out.println(jedis.hgetAll("user-session:123"));
            // Prints: {name=John, surname=Smith, company=Redis, age=29}
        }
    }
}

```

### Дополнительный материал для чтения
- https://codeahoy.com/2017/08/11/caching-strategies-and-how-to-choose-the-right-one/
- https://habr.com/ru/companies/manychat/articles/507136/
- https://timeweb.cloud/tutorials/redis/kak-kehshirovat-prilozheniya-node-js-s-redis