BEGIN;

CREATE TABLE IF NOT EXISTS public.users
(
    user_id serial PRIMARY KEY,
    email varchar NOT NULL UNIQUE,
    password varchar NOT NULL,
    admin boolean NOT NULL DEFAULT false,
    balance integer NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS public.bottoms
(
    bottom_id serial PRIMARY KEY,
    bottom_name varchar NOT NULL,
    price integer NOT NULL
);

CREATE TABLE IF NOT EXISTS public.toppings
(
    topping_id serial PRIMARY KEY,
    topping_name varchar NOT NULL,
    price integer NOT NULL
);

CREATE TABLE IF NOT EXISTS public.orders
(
    order_id serial PRIMARY KEY,
    user_id integer NOT NULL REFERENCES users(user_id),
    create_date timestamp NOT NULL DEFAULT now(),
    total_price integer NOT NULL,
    status varchar NOT NULL DEFAULT 'bestilt'
    );

CREATE TABLE IF NOT EXISTS public.orderlines
(
    orderline_id serial PRIMARY KEY,
    order_id integer NOT NULL REFERENCES orders(order_id) ON DELETE CASCADE,
    bottom_id integer NOT NULL REFERENCES bottoms(bottom_id),
    topping_id integer NOT NULL REFERENCES toppings(topping_id),
    amount integer NOT NULL,
    price integer NOT NULL
    );

END;