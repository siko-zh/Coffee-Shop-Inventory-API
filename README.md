# ☕ CoffeeShopIMS

Система управления запасами для сети кофеен.

## Стек

| Технология | Назначение |
|---|---|
| Java 17 | Язык |
| Spring Boot 4.0.5 | Фреймворк |
| Spring Security + JWT | Аутентификация и авторизация |
| Spring Data JPA | Работа с БД |
| PostgreSQL | База данных |
| MapStruct | Маппинг Entity ↔ DTO |
| Lombok | Убирает boilerplate |
| Swagger (springdoc) | Документация API |
| Gradle | Сборка |
| Docker | Контейнеризация |

## Бизнес-логика

**Сущности:**
- **Branch** — филиал кофейни
- **User** — сотрудник (ADMIN / MANAGER / BARISTA)
- **Product** — товар (кофе, молоко, сироп…)
- **Category** — категория товара
- **Supplier** — поставщик
- **Stock** — остаток продукта в филиале
- **Supply** — поставка от поставщика
- **StockMovement** — движение товара (приход/расход)
- **Transfer** — перемещение между филиалами
- **Shift** — смена (открытие/закрытие)

**Роли и доступ:**

| Роль | Чтение | Создание/изменение |
|---|---|---|
| ADMIN | Всё | Всё |
| MANAGER | Всё | Сущности, поставки, перемещения |
| BARISTA | Склады, смены | Открытие/закрытие смены, списание продуктов |

## Быстрый старт

```bash
./gradlew bootRun
```

Swagger: [http://localhost:8080/swagger](http://localhost:8080/swagger)

## Docker

```bash
docker compose up --build
```

## Конфигурация

Все настройки через переменные окружения:

| Переменная | По умолчанию | Описание |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/coffeeshop_inventory` | URL базы данных |
| `DB_USERNAME` | `postgres` | Пользователь БД |
| `DB_PASSWORD` | — | Пароль БД |
| `DB_SCHEMA` | `coffeeshopims` | Схема БД |
| `JWT_SECRET` | — | Ключ подписи JWT (минимум 256 бит) |
| `JWT_EXPIRATION_MS` | `86400000` (24ч) | Время жизни токена |

## API

### Аутентификация

```
POST /auth/login
Body: { "username": "...", "password": "..." }
Response: { "token": "...", "username": "...", "role": "..." }
```

Все остальные эндпоинты требуют заголовок `Authorization: Bearer <token>`.

### Основные эндпоинты

| Метод | Путь | Роли |
|---|---|---|
| GET | `/branches` | Все роли |
| POST | `/branches` | ADMIN, MANAGER |
| GET | `/stocks?branchId=` | Все роли |
| POST | `/stocks/write-off` | Все роли |
| POST | `/shifts/open` | Все роли |
| POST | `/supplies` | ADMIN, MANAGER |
| POST | `/supplies/{id}/receive` | ADMIN, MANAGER |
| POST | `/transfers` | ADMIN, MANAGER |
| POST | `/transfers/{id}/complete` | ADMIN, MANAGER |
| GET | `/users` | ADMIN |
| POST | `/users` | ADMIN |
| PATCH | `/users/{id}/password` | Все роли |

Полный список — в Swagger UI после запуска.
