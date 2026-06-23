# Energy Community Management System

Sechs eigenständige Komponenten, die über RabbitMQ und eine gemeinsame
PostgreSQL-Datenbank zusammenarbeiten. Nachrichtenformate siehe
[`RABBITMQ.md`](RABBITMQ.md).

## Komponenten starten

Die Komponenten kommunizieren nur über die Queue und die DB, deshalb
ist die Startreihenfolge nicht zwingend. Damit beim Anlauf keine
Fehlermeldungen auftreten, empfiehlt sich:

1. `docker compose up -d` (startet Postgres und RabbitMQ)
2. REST API. Beim Start legt Flyway die Tabellen `hourly_usage` und
   `current_percentage` an.
3. Usage Service und Percentage Service
4. Producer und User
5. GUI mit `mvn javafx:run` aus dem Ordner `gui/`

Wenn eine Flyway-Migration geändert oder neu hinzugefügt wird, muss
vor dem nächsten Start das DB-Volume zurückgesetzt werden, sonst
beschwert sich Flyway über Checksum-Fehler:

```bash
docker compose down -v
```

## Bauen

`mvn package` aus dem Root baut alle Komponenten. Der
`@SpringBootTest` der REST API braucht eine laufende
Postgres-Instanz. Wenn die DB nicht läuft, mit
`mvn package -DskipTests` bauen.