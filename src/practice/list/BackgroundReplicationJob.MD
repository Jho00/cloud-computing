# Фоновая репликация баз данных

**Цель работы:** Научиться проектировать и реализовывать ETL-пайплайн (Extract → Transform → Load) для репликации данных между гетерогенными базами данных в фоновом режиме.


**Сценарии использования**:

| Сценарий               | Описание                                                   |
| ---------------------- | ---------------------------------------------------------- |
| **Аналитика**          | Тяжёлые запросы выполняются на реплике, не нагружая мастер |
| **Отказоустойчивость** | Резервная копия данных в реальном времени                  |
| **Эксперименты**       | Безопасное тестирование на реальных данных                 |
| **Масштабирование**    | Распределение нагрузки на чтение                           |

### Описание задачи
Необходимо поднять два контейнера с базами данных. БД должны быть персистентными, одна из этих баз должна быть реляционная, а другая - нет. 
Хорошие варианты: PostgreSQL + MongoDB, PostgreSQL + Clickhouse

В реляционной базе данных, которая будет являться источником данных, необходимо проинициализировать 2 таблички с имеющейся между ними связью один-ко-многим

Необходимо реализовать фоновую задачу (скрипт), которая раз в промежуток времени (задать можно при помощи cron) вычитывает новые данные из основной базы и записывает их во вторую базу данных

Выполнить преобразование структуры данных


### Методические материалы:

Файл docker-compose.yml может выглядеть так: 

```
version: '3.8'

services:
  postgres:
    image: postgres:15
    container_name: master_db
    environment:
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: secret
      POSTGRES_DB: shop
    ports:
      - "5432:5432"
    volumes:
      - pg_/var/lib/postgresql/data   
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql

  mongodb:
    image: mongo:6
    container_name: replica_db
    ports:
      - "27017:27017"
    volumes:
      - mongo_/data/db               

volumes:
  pg_
  mongo_

```

Предлагаемая реляционная структура:

```
-- Таблица покупателей (родительская)
CREATE TABLE customers (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) UNIQUE NOT NULL,
    created_at  TIMESTAMP DEFAULT NOW()
);

-- Таблица заказов (дочерняя)
CREATE TABLE orders (
    id           SERIAL PRIMARY KEY,
    customer_id  INT REFERENCES customers(id) ON DELETE CASCADE,
    product      VARCHAR(200) NOT NULL,
    amount       NUMERIC(10, 2) NOT NULL,
    status       VARCHAR(50) DEFAULT 'pending',
    created_at   TIMESTAMP DEFAULT NOW(),
    updated_at   TIMESTAMP DEFAULT NOW()
);

-- Индекс для быстрой выборки новых записей
CREATE INDEX idx_orders_updated ON orders(updated_at);
CREATE INDEX idx_customers_created ON customers(created_at);

-- Тестовые данные
INSERT INTO customers (name, email) VALUES
    ('Иван Петров',   'ivan@example.com'),
    ('Мария Сидорова','maria@example.com');

INSERT INTO orders (customer_id, product, amount, status) VALUES
    (1, 'Ноутбук',    75000.00, 'completed'),
    (1, 'Мышь',        1500.00, 'pending'),
    (2, 'Монитор',    35000.00, 'shipped');
```

Желательно нагенерировать побольше данных (>500тыс). Для этого можно написать скрипт генератор данных, совершенно не принципиально, чтобы данные имели смысл

Алгоритм репликации на псевдокоде:

```
PROCEDURE replicate():

    last_sync ← read_last_sync_time()        // читаем метку последней синхронизации

    // --- EXTRACT ---
    new_customers ← postgres.query(
        "SELECT * FROM customers WHERE created_at > last_sync"
    )
    new_orders ← postgres.query(
        "SELECT o.*, c.name, c.email
         FROM orders o
         JOIN customers c ON c.id = o.customer_id
         WHERE o.updated_at > last_sync"
    )

    // --- TRANSFORM ---
    FOR EACH customer IN new_customers:
        document ← {
            "_id":       customer.id,
            "name":      customer.name,
            "email":     customer.email,
            "orders":    [],                  // денормализация!
            "synced_at": NOW()
        }
        upsert(mongo.customers, document)

    FOR EACH order IN new_orders:
        // Встраиваем заказ внутрь документа покупателя
        mongo.customers.update(
            filter  = { "_id": order.customer_id },
            update  = { PUSH orders: transform(order) }
        )

    // --- SAVE STATE ---
    save_last_sync_time(NOW())

    LOG "Replicated: {count(new_customers)} customers, {count(new_orders)} orders"
```


Предлагаемая структура данных в mongodb:

```
{
  "_id": 1,
  "name": "Иван Петров",
  "email": "ivan@example.com",
  "synced_at": "2025-07-20T12:00:00Z",
  "orders": [
    { "order_id": 10, "product": "Ноутбук", "amount": 75000.0,
      "status": "completed", "placed_at": "2025-07-19T10:00:00" },
    { "order_id": 11, "product": "Мышь",    "amount": 1500.0,
      "status": "pending",   "placed_at": "2025-07-20T09:30:00" }
  ]
}
```


### Дополнительные задания:

- [ ] Добавить третью таблицу `products` и усложнить связь до `orders ↔ products` (многие-ко-многим)
- [ ] Вынести все настройки (пароли, интервал логирования) в `.env` файл
- [ ] Реализовать **отслеживание удалений**: добавить поле `deleted_at` (soft delete) и синхронизировать его
- [ ] Добавить **идемпотентность**: повторный запуск не должен создавать дубликаты


### Дополнительный материал для чтения
- https://ivan-shamaev.ru/data-engineering-etl-pipeline-data-warehouse-datalake/
- https://crontab.guru/
- https://habr.com/ru/companies/skillfactory/articles/656423/
- https://habr.com/ru/companies/postgrespro/articles/489308/
- https://postgrespro.ru/docs/postgresql/10/logical-replication
