# RabbitMQ Message Contracts

Diese Datei dokumentiert die Queues und Nachrichtenformate, die zwischen den
Komponenten der Energy Community ausgetauscht werden.

## Queues

| Queue Name        | Producer         | Consumer            | Zweck                                           |
|-------------------|------------------|---------------------|-------------------------------------------------|
| `energy.messages` | Producer, User   | Usage Service       | Minuten-Daten zu Energieproduktion/-verbrauch   |
| `usage.updates`   | Usage Service    | Percentage Service  | Signal, dass Stundendaten aktualisiert wurden   |

Beide Queues werden über die RabbitMQ Default Exchange angesprochen
(Routing Key = Queue Name).

## Connection (local development)

- Host: `localhost`
- Port: `5672`
- Username: `guest`
- Password: `guest`

Management UI: <http://localhost:15672> (gleiche Credentials)

## Message Format: `energy.messages`

Wird von Community Energy Producer und Community Energy User publiziert.

```json
{
  "type": "PRODUCER",
  "association": "COMMUNITY",
  "kwh": 0.003,
  "datetime": "2025-01-10T14:33:00"
}
```

| Feld          | Typ     | Werte                          |
|---------------|---------|--------------------------------|
| `type`        | String  | `PRODUCER` oder `USER`         |
| `association` | String  | `COMMUNITY`                    |
| `kwh`         | Number  | kWh pro Minute (z.B. 0.003)    |
| `datetime`    | String  | ISO-8601 ohne Zeitzone         |

## Message Format: `usage.updates`

Wird vom Usage Service nach jedem erfolgreichen DB-Update publiziert.
Der Percentage Service liest danach die aktuellen Werte für die genannte
Stunde aus der DB und berechnet die Prozentwerte neu.

```json
{
  "hour": "2025-01-10T14:00:00"
}
```

| Feld   | Typ    | Werte                                    |
|--------|--------|------------------------------------------|
| `hour` | String | ISO-8601, immer auf volle Stunde gerundet |

## Java Library

Alle Komponenten verwenden den nativen RabbitMQ AMQP Client, nicht Spring AMQP:

```xml
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>5.21.0</version>
</dependency>
```

Begründung: Producer und User sind als schlanke Standalone-Apps konzipiert
und sollen nicht durch Spring Boot aufgebläht werden. Auch Usage Service und
Percentage Service brauchen kein Spring (nur die REST API tut das).