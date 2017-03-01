create table role (
  id integer primary key not null,
  movie_id integer not null,
  actor_id integer not null,
  description text not null,
  unique(movie_id, actor_id),
  foreign key(movie_id) references movie(id),
  foreign key(actor_id) references actor(id)
)
