# Проект-презентация для JPoint 2018 "Реактивное программирование на Vert.x"

## Сборка приложения
```bash
gradle clean fatJar
```

## Запуск приложения
```bash
kubectl apply -f chat.yaml
```

## Использование
### Клиент
[http://192.168.99.100:30082/](http://192.168.99.100:30082/)

### История сообщений
[http://192.168.99.100:30081/message/1](http://192.168.99.100:30081/message/1)