# Dokumentacja systemu do zarządzania małym hotelem/airbnb

## 1. Schemat Bazy

![](_img/schemat.png)

## 2. Tabele

### 2.1 Room

![](_img/schemat.png)

### 2.2 Person

![](_img/person.png)

### 2.3 Reservation

![](_img/reservation.png)

### 2.4 Extras

![](_img/extras.png)

### 2.5 Extras_Reservation

![](_img/extras_reservation.png)

### 2.6 Log

![](_img/log.png)

## 3. Obiekty i Kolekcje

### 3.1 EXTRAS_DETAILS

```sql
create or replace type extras_details
as object
(
    extras_id int,
    extras_name string,
    extras_number int,
    price int
)
```

### 3.2 EXTRAS_INFO

```sql
create type extras_info
as object
(
    extras_reservation_id int,
    extras_id int,
    extras_name varchar2(255),
    st char,
    price int
)
```

### 3.3 ROOM_DETAILS

```sql
create type room_details
as object
(
    room_id int,
    room_number int,
    common int,
    price int,
    max_no_places int,
    available_places int
)
```

### 3.4 EXTRAS_DETAILS_ARRAY

```sql
create type EXTRAS_DETAILS_ARRAY as table of EXTRAS_DETAILS
```

### 3.5 EXTRAS_INFO_ARRAY

```sql
create type EXTRAS_INFO_ARRAY as table of EXTRAS_INFO
```

### 3.6 ROOM_DETAILS_ARRAY

```sql
create type ROOM_DETAILS_ARRAY as table of ROOM_DETAILS
```

### 3.7 NUM_ARRAY

```sql
create type NUM_ARRAY as table of INTEGER
```

## 4. Funkcje

### 4.1 F_ROOM_EXISTS

```sql
create function f_room_exists(room_id_check room.room_id%type)
    return number
as
    result number;
begin
    select count(room_id) into result from room where room_id = room_id_check;

    if result is null then
        result := 0;
    end if;

    return result;
end;
```

### 4.2 F_PERSON_EXISTS

```sql
create or replace function f_person_exists(person_id_check person.person_id%type)
    return number
as
    person_exist int;
begin
    if person_id_check is null then
        raise_application_error(-20001, 'Parameter person_id_check cannot be null');
    end if;
    select count(person_id) into person_exist from person;

    if person_exist is null then
        return 0;
    end if;

    return person_exist;
end;
```

### 4.3 F_RESERVATION_EXISTS

```sql
create or replace function f_reservation_exists(reservation_id_check reservation.RESERVATION_ID%TYPE)
    return number
as
    reservation_exists int;
begin
    if reservation_id_check is null then
        raise_application_error(-2001, 'Parameter reservation_id_check cannot be null');
    end if;
    select count(r.reservation_id) into reservation_exists from reservation r where r.reservation_id = reservation_id_check;

    if reservation_exists is null then
        return 0;
    end if;

    return reservation_exists;
end;
```

### 4.4 F_EXTRAS_EXISTS

```sql
create function f_extras_exists(extras_id_check extras.extras_id%type)
    return number
as
    result number;
begin
    select count(extras_id) into result from EXTRAS where EXTRAS_ID = extras_id_check;

    if result is null then
        result := 0;
    end if;

    return result;
end;
```

### 4.5 F_EXTRAS_RESERVATION_EXISTS

```sql
create function f_extras_reservation_exists(extras_reservation_id_check extras_reservation.extras_reservation_id%type)
    return number
as
    result number;
begin
    select count(extras_reservation_id) into result from EXTRAS_RESERVATION where EXTRAS_ID = extras_reservation_id_check;

    if result is null then
        result := 0;
    end if;

    return result;
end;
```

### 4.6 F_ROOM_IS_COMMON

```sql
create function f_room_is_common(room_id_check ROOM.ROOM_ID%TYPE)
    RETURN number
as
    result number;
begin
    select common into result from room where room_id = room_id_check;
    return result;
end;
```

### 4.7 F_ROOM_OCCUPIED

```sql
create or replace function f_room_occupied(room_id_check ROOM.ROOM_ID%TYPE, date_from date, date_to date)
    RETURN number
as
    result number;
begin
    select count(r.RESERVATION_ID) into result
    from reservation r
    where r.ROOM_ID = room_id_check
      and r.START_DATE <= date_to
      and r.END_DATE >= date_from
      and r.STATUS != 'C';

    if result is null then
        return 0;
    end if;

    return result;
end;
```

### 4.8 F_COMMON_ROOM_AVAILABLE_PLACES

```sql
create or replace function f_common_room_available_places(room_id_check ROOM.ROOM_ID%TYPE, date_from date, date_to date)
    RETURN number
as
    occupied_places number;
    max_occupied_places number;
begin
    select SUM(r.NO_OF_PEOPLE) into occupied_places
      from reservation r
     where r.ROOM_ID = room_id_check
       and r.START_DATE <= date_to
       and r.END_DATE >= date_from
       and r.STATUS != 'C';
    select MAX_NO_PLACES into max_occupied_places from room where ROOM_ID = room_id_check;

    if occupied_places is null then
        return max_occupied_places;
    end if;

    return max_occupied_places - occupied_places;
end;
```

### 4.9 F_EXTRAS_AVAILABLE

```sql
create function f_extras_available(extras_id_check extras.extras_id%type, date_from date, date_to date)
return number
as
    max_available_extras number;
    occupied_extras number;
    result number;
begin
    select NO_AVAILABLE into max_available_extras from EXTRAS where EXTRAS_ID = extras_id_check;
    select sum(r.NO_OF_PEOPLE) into occupied_extras from EXTRAS_RESERVATION er
       inner join RESERVATION R on R.RESERVATION_ID = er.RESERVATION_ID
       where er.EXTRAS_ID = extras_id_check
         and r.START_DATE <= date_to
         and r.END_DATE >= date_from
         and r.status != 'C';

    if occupied_extras is null then
        return max_available_extras;
    end if;

    result := max_available_extras - occupied_extras;

    return result;
end;
```

### 4.10 F_GET_RESERVATION_PRICE

```sql
create or replace function f_get_reservation_price(reservation_id_check RESERVATION.reservation_id%type)
    return number
as
    no_of_people number;
    extras_price number;
    room_price number;
    date_from date;
    date_to date;
    day_count number;
    final_price number;
begin
    select END_DATE, START_DATE into date_to, date_from from RESERVATION where RESERVATION_ID = reservation_id_check;
    select sum(e.PRICE) into extras_price from EXTRAS_RESERVATION er INNER JOIN EXTRAS e on e.EXTRAS_ID = er.EXTRAS_ID where er.RESERVATION_ID = reservation_id_check;
    select r.PRICE, reservation.NO_OF_PEOPLE into room_price, no_of_people  from RESERVATION inner join BD_411400.ROOM R on R.ROOM_ID = RESERVATION.ROOM_ID where RESERVATION_ID = reservation_id_check;

    day_count := date_to - date_from;

    if extras_price is null then
        final_price := day_count * room_price;
        return final_price;
    end if;

    final_price := extras_price * no_of_people + day_count * room_price;
    return final_price;
end;
```

### 4.11 F_SAME_EXTRA_RESERVATION_EXISTS

```sql
create or replace function f_same_extra_reservation_exists(r_id RESERVATION.RESERVATION_ID%type, e_id EXTRAS.EXTRAS_ID%type)
return number
as
    result number;
begin
    select count(*) into result from EXTRAS_RESERVATION
     where EXTRAS_ID = e_id and RESERVATION_ID = r_id;

    if result is null then
        result := 0;
    end if;

    return result;
end;
```

### 4.12 F_GET_PERSON_RESERVATION_IDS

```sql
create function f_get_person_reservation_ids(p_id person.person_id%type)
return NUM_ARRAY
as
    result NUM_ARRAY;
begin
    select reservation_id bulk collect into result
      from reservation where person_id = p_id;

    return result;
end;
```

### 4.13 F_AVAILABLE_ROOMS

```sql
create or replace FUNCTION f_available_rooms(date_from DATE, date_to DATE)
    RETURN room_details_array
AS
    result room_details_array := room_details_array();
    CURSOR available_rooms_cursor IS
        WITH ReservedRooms AS (
            SELECT
                r.ROOM_ID,
                SUM(r.NO_OF_PEOPLE) AS OCCUPIED_PLACES
            FROM
                BD_411400.RESERVATION r
            WHERE
                r.START_DATE < date_to
              AND r.END_DATE > date_from
              and r.STATUS != 'C'
            GROUP BY
                r.ROOM_ID
        )
        SELECT
            rm.ROOM_ID,
            rm.ROOM_NUMBER,
            rm.COMMON,
            rm.PRICE,
            rm.MAX_NO_PLACES,
            CASE
                WHEN rm.COMMON = 1 THEN (rm.MAX_NO_PLACES - NVL(rr.OCCUPIED_PLACES, 0))
                END AS AVAILABLE_PLACES
        FROM
            BD_411400.ROOM rm
                LEFT JOIN ReservedRooms rr ON rm.ROOM_ID = rr.ROOM_ID
        WHERE
            rr.ROOM_ID IS NULL
           OR (rm.COMMON = 1 AND (rm.MAX_NO_PLACES - NVL(rr.OCCUPIED_PLACES, 0)) > 0);
BEGIN
    FOR room_record IN available_rooms_cursor LOOP
            result.EXTEND;
            result(result.COUNT) := room_details(
                    room_record.ROOM_ID,
                    room_record.ROOM_NUMBER,
                    room_record.COMMON,
                    room_record.PRICE,
                    room_record.MAX_NO_PLACES,
                    room_record.AVAILABLE_PLACES
                                    );
        END LOOP;

    RETURN result;
END;
```

### 4.14 F_GET_RESERVATION_EXTRAS

```sql
create function f_get_reservation_extras(r_id reservation.reservation_id%type)
    return extras_info_array
as
    extras_information EXTRAS_INFO_ARRAY;
begin

    select extras_info(extras_reservation_id, extras_id, name, price, status) bulk collect into extras_information
    from (select er.extras_reservation_id, e.extras_id, e.name, e.price, er.STATUS from EXTRAS_RESERVATION er
                                                                                            inner join EXTRAS E on E.EXTRAS_ID = er.EXTRAS_ID
          where er.RESERVATION_ID = r_id);

    return extras_information;
end;
```

### 4.15 F_RESERVATION_EXTRAS_AVAILABLE

```sql
create function f_reservation_extras_available(reservation_id_check reservation.RESERVATION_ID%type)
    return extras_details_array
as
    extras_info extras_details_array;
    date_from date;
    date_to date;
    no_places int;
begin
    if F_RESERVATION_EXISTS(reservation_id_check) = 0 then
        raise_application_error(-20001, 'Reservation with this id does not exist');
    end if;

    select START_DATE, END_DATE, NO_OF_PEOPLE into date_from, date_to, no_places
      from RESERVATION where RESERVATION_ID = reservation_id_check;

    select extras_details(EXTRAS_ID, NO_AVAILABLE, PRICE)
            bulk collect into extras_info
    from (
         select E.EXTRAS_ID, E.NO_AVAILABLE - NVL(SUM(R.NO_OF_PEOPLE), 0) as NO_AVAILABLE, E.PRICE
         from EXTRAS E
                  left join EXTRAS_RESERVATION ER on E.EXTRAS_ID = ER.EXTRAS_ID
                  left join RESERVATION R on ER.RESERVATION_ID = R.RESERVATION_ID
             and ER.STATUS = 'N'
             and R.RESERVATION_ID != reservation_id_check
             and ((R.START_DATE between date_from and date_to) or (R.END_DATE between date_from and date_to))
         group by E.EXTRAS_ID, E.NO_AVAILABLE, E.PRICE
         having E.NO_AVAILABLE - NVL(SUM(R.NO_OF_PEOPLE), 0) >= no_places
     );

    return extras_info;
end;
```

## 5. Procedury

### 5.1 P_ADD_ADMIN

```sql
create procedure p_add_admin(fn person.firstname%type, ln person.lastname%type,
    email person.email%type, password person.password%type)
as
begin
    insert into person (FIRSTNAME, LASTNAME, EMAIL, PASSWORD, admin)
    VALUES (fn, ln, email, password, 1);
end;
```

### 5.2 P_ADD_USER

```sql
create procedure p_add_user(fn person.firstname%type, ln person.lastname%type,
    email person.email%type, password person.password%type)
as
begin
    insert into person (FIRSTNAME, LASTNAME, EMAIL, PASSWORD)
    VALUES (fn, ln, email, password);
end;
```

### 5.3 P_PAY_FOR_RESERVATION

```sql
create or replace procedure p_pay_for_reservation(r_id RESERVATION.reservation_id%type)
as
    st char;
begin
    if r_id is null then
        raise_application_error(-20001, 'Reservation id cannot be null');
    end if;

    select status into st from reservation where reservation_id = r_id;

    if st = 'C' then
        raise_application_error(-20002, 'Cannot pay for canceled reservation');
    end if;

    if st = 'P' then
        raise_application_error(-20003, 'Cannot pay for paid reservation');
    end if;

    update reservation set status = 'P' where RESERVATION_ID = r_id;
    update EXTRAS_RESERVATION er set er.STATUS = 'P'
     where er.RESERVATION_ID = r_id and er.STATUS != 'C';
end;
```

### 5.4 P_CANCEL_RESERVATION

```sql
create procedure p_cancel_reservation(r_id RESERVATION.reservation_id%type)
as
    st char;
begin
    if r_id is null then
        raise_application_error(-20001, 'Reservation id cannot be null');
    end if;

    select status into st from reservation where reservation_id = r_id;

    if st = 'C' then
        raise_application_error(-20002, 'Cannot cancel reservation that is already canceled');
    end if;

    update reservation set status = 'C' where RESERVATION_ID = r_id;
    update EXTRAS_RESERVATION er set er.STATUS = 'C'
     where er.RESERVATION_ID = r_id;
end;
```

## 6. Triggery
