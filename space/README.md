## Задание на стажировку.

Нужно дописать приложение, которое ведет учет космических кораблей в далеком будущем (в 3019 году). Должны быть
реализованы следующие возможности:

1. получать список всех существующих кораблей;
2. создавать новый корабль;
3. редактировать характеристики существующего корабля;
4. удалять корабль;
5. получать корабль по id;
6. получать отфильтрованный список кораблей в соответствии с переданными фильтрами;
7. получать количество кораблей, которые соответствуют фильтрам.

Для этого необходимо реализовать REST API в соответствии с документацией.

В проекте должна использоваться сущность Ship, которая имеет поля:

<table style="width:100%">
  <tr>
    <td>Long id</td>
    <td>ID корабля</td>
  </tr>
  <tr>
    <td>String name</td>
    <td>Название корабля (до 50 знаков включительно)</td>
  </tr>
  <tr>
    <td>String name</td>
    <td>Название корабля (до 50 знаков включительно)</td>
  </tr>
  <tr>
    <td>String planet</td>
    <td>Планета пребывания (до 50 знаков включительно)</td>
  </tr>
  <tr>
    <td>ShipType shipType</td>
    <td>Тип корабля</td>
  </tr>
  <tr>
    <td>Date prodDate</td>
    <td>Дата выпуска. Диапазон значений года 2800..3019 включительно</td>
  </tr>
  <tr>
    <td>Boolean isUsed</td>
    <td>Использованный / новый</td>
  </tr>
  <tr>
    <td>Double speed</td>
    <td>Максимальная скорость корабля. Диапазон значений 0,01..0,99 включительно. Используй математическое округление до сотых.</td>
  </tr>
  <tr>
    <td>Integer crewSize</td>
    <td>Количество членов экипажа. Диапазон значений 1..9999 включительно.</td>
  </tr>
  <tr>
    <td>Double rating</td>
    <td>Рейтинг корабля. Используй математическое округление до сотых.</td>
  </tr>
</table>

Также должна присутствовать бизнес-логика:
Перед сохранением корабля в базу данных (при добавлении нового или при апдейте характеристик существующего), должен
высчитываться рейтинг корабля и сохраняться в БД. Рейтинг корабля рассчитывается по формуле: R = 80·v·k/(y0−y1+1), где:
v — скорость корабля; k — коэффициент, который равен 1 для нового корабля и 0,5 для использованного; y0 — текущий год (
не забудь, что «сейчас» 3019 год); y1 — год выпуска корабля.

В приложении используй технологии:

1. Maven (для сборки проекта);
2. Tomcat 9 (для запуска своего приложения);
3. Spring;
4. Spring Data JPA;
5. MySQL (база данных (БД)).

## Обрати внимание.

1. Если в запросе на создание корабля нет параметра “isUsed”, то считаем, что пришло значение “false”.
2. Параметры даты между фронтом и сервером передаются в миллисекундах (тип Long) начиная с 01.01.1970.
3. При обновлении или создании корабля игнорируем параметры “id” и “rating” из тела запроса.
4. Если параметр pageNumber не указан – нужно использовать значение 0.
5. Если параметр pageSize не указан – нужно использовать значение 3.
6. Не валидным считается id, если он:

- не числовой
- не целое число
- не положительный

7. При передаче границ диапазонов (параметры с именами, которые начинаются на «min» или «max») границы нужно
   использовать включительно.

## REST API

### Get ships list

<table style="width:100%">
  <tr>
    <td>URL</td>
    <td>/ships</td>
  </tr>
  <tr>
    <td>Method</td>
    <td>GET</td>
  </tr>
  <tr>
    <td>URL Params</td>
    <td>Optional: name=String
planet=String
shipType=ShipType
after=Long, before=Long
isUsed=Boolean
minSpeed=Double
maxSpeed=Double
minCrewSize=Integer
maxCrewSize=Integer
minRating=Double
maxRating=Double
order=ShipOrder
pageNumber=Integer
pageSize=Integer </td>
  </tr>
  <tr>
    <td>Data Params</td>
    <td>None</td>
  </tr>
  <tr>
    <td>Success Response</td>
    <td>Code: 200 OK
Content: [
{
“id”:[Long],
“name”:[String],
“planet”:[String],
“shipType”:[ShipType],
“prodDate”:[Long],
“isUsed”:[Boolean],
“speed”:[Double],
“crewSize”:[Integer],
“rating”:[Double]
},
…
]</td>
  </tr>
  <tr>
    <td>Notes</td>
    <td>Поиск по полям name и planet происходить по частичному 
соответствию. Например, если в БД есть корабль с именем 
«Левиафан», а параметр name задан как «иа» - такой корабль 
должен отображаться в результатах (Левиафан).
pageNumber – параметр, который отвечает за номер 
отображаемой страницы при использовании пейджинга. 
Нумерация начинается с нуля
pageSize – параметр, который отвечает за количество 
результатов на одной странице при пейджинге</td>
  </tr>
</table>

### Get ships count

<table style="width:100%">
  <tr>
    <td>URL</td>
    <td>/ships/count</td>
  </tr>
  <tr>
    <td>Method</td>
    <td>GET</td>
  </tr>
  <tr>
    <td>URL Params</td>
    <td>Optional:
name=String
planet=String
shipType=ShipType
after=Long
before=Long
isUsed=Boolean
minSpeed=Double
maxSpeed=Double
minCrewSize=Integer
maxCrewSize=Integer
minRating=Double
maxRating=Double</td>
  </tr>
  <tr>
    <td>Data Params</td>
    <td>None</td>
  </tr>
  <tr>
    <td>Success Response</td>
    <td>Code: 200 OK
Content: Integer</td>
  </tr>
  <tr>
    <td>Notes</td>
    <td></td>
  </tr>
</table>

### Create ship

<table style="width:100%">
  <tr>
    <td>URL</td>
    <td>/ships</td>
  </tr>
  <tr>
    <td>Method</td>
    <td>POST</td>
  </tr>
  <tr>
    <td>Data Params</td>
    <td>{
 “name”:[String],
 “planet”:[String],
 “shipType”:[ShipType],
 “prodDate”:[Long],
 “isUsed”:[Boolean], --optional, default=false
 “speed”:[Double], 
 “crewSize”:[Integer]
}</td>
  </tr>
  <tr>
    <td>Data Params</td>
    <td>None</td>
  </tr>
  <tr>
    <td>Success Response</td>
    <td>Code: 200 OK
Content: {
 “id”:[Long],
 “name”:[String],
 “planet”:[String],
 “shipType”:[ShipType],
 “prodDate”:[Long],
 “isUsed”:[Boolean],
 “speed”:[Double], 
 “crewSize”:[Integer], 
 “rating”:[Double]
}</td>
  </tr>
  <tr>
    <td>Notes</td>
    <td>Мы не можем создать корабль, если:
- указаны не все параметры из Data Params (кроме isUsed);
- длина значения параметра “name” или “planet” превышает 
размер соответствующего поля в БД (50 символов);
- значение параметра “name” или “planet” пустая строка;
- скорость или размер команды находятся вне заданных 
пределов;
- “prodDate”:[Long] < 0;
- год производства находятся вне заданных пределов.
В случае всего вышеперечисленного необходимо ответить 
ошибкой с кодом 400.</td>
  </tr>
</table>

### Get ship

<table style="width:100%">
  <tr>
    <td>URL</td>
    <td>/ships/{id}</td>
  </tr>
  <tr>
    <td>Method</td>
    <td>GET</td>
  </tr>
  <tr>
    <td>URL Params</td>
    <td>id</td>
  </tr>
  <tr>
    <td>Data Params</td>
    <td>None</td>
  </tr>
  <tr>
    <td>Success Response</td>
    <td>Code: 200 OK
Content: {
 “id”:[Long],
 “name”:[String],
 “planet”:[String],
 “shipType”:[ShipType],
 “prodDate”:[Long],
 “isUsed”:[Boolean],
 “speed”:[Double], 
 “crewSize”:[Integer], 
 “rating”:[Double]
}</td>
  </tr>
  <tr>
    <td>Notes</td>
    <td>Если корабль не найден в БД, необходимо ответить ошибкой 
с кодом 404.
Если значение id не валидное, необходимо ответить ошибкой 
с кодом 400.</td>
  </tr>
</table>

### Update ship

<table style="width:100%">
  <tr>
    <td>URL</td>
    <td>/ships/{id}</td>
  </tr>
  <tr>
    <td>Method</td>
    <td>POST</td>
  </tr>
  <tr>
    <td>URL Params</td>
    <td>id</td>
  </tr>
  <tr>
    <td>Data Params</td>
    <td>{
 “name”:[String], --optional
 “planet”:[String], --optional
 “shipType”:[ShipType], --optional
 “prodDate”:[Long], --optional
 “isUsed”:[Boolean], --optional
 “speed”:[Double], --optional
 “crewSize”:[Integer] --optional
}
</td>
  </tr>
  <tr>
    <td>Success Response</td>
    <td>Code: 200 OK
Content: {
 “id”:[Long],
 “name”:[String],
 “planet”:[String],
 “shipType”:[ShipType],
 “prodDate”:[Long],
 “isUsed”:[Boolean],
 “speed”:[Double], 
 “crewSize”:[Integer], 
 “rating”:[Double]
}
</td>
  </tr>
  <tr>
    <td>Notes</td>
    <td>Обновлять нужно только те поля, которые не null.
Если корабль не найден в БД, необходимо ответить ошибкой 
с кодом 404.
Если значение id не валидное, необходимо ответить ошибкой 
с кодом 400.</td>
  </tr>
</table>

### Delete ship

<table style="width:100%">
  <tr>
    <td>URL</td>
    <td>/ships/{id}</td>
  </tr>
  <tr>
    <td>Method</td>
    <td>DELETE</td>
  </tr>
  <tr>
    <td>URL Params</td>
    <td>id</td>
  </tr>
  <tr>
    <td>Data Params</td>
    <td></td>
  </tr>
  <tr>
    <td>Success Response</td>
    <td>Code: 200 OK</td>
  </tr>
  <tr>
    <td>Notes</td>
    <td>Если корабль не найден в БД, необходимо ответить ошибкой 
с кодом 404.
Если значение id не валидное, необходимо ответить ошибкой 
с кодом 400.</td>
  </tr>
</table>