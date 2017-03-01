create table movie (
  id integer primary key not null,
  title varchar(255) unique not null,
  year integer not null,
  description text not null,
  director_id int not null,
  foreign key(director_id) references director(id)
)
