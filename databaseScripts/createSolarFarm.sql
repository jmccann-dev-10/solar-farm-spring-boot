drop database if exists solar_farm;
create database solar_farm;
use solar_farm;

create table material (
                          material_id int primary key auto_increment,
                          `name` varchar(200) not null,
                          abbreviation varchar(10),
                          constraint unique
                              un_name_abbr(
                              `name`,
                              `abbreviation`)

);

create table solar_panel (
                             solar_panel_id int primary key auto_increment,
                             section varchar(50) not null,
                             `row` int not null,
                             `column` int not null,
                             year_installed int not null,
                             material_id int not null,
                             is_tracking bit not null,
                             constraint fk_solar_panel_material
                                 foreign key (material_id)
                                     references material(material_id),
                             constraint unique un_solar_panel(
                                 section,
                                 `row`,
                                 `column`)
);

insert into material values
                         (1,"Multicrystalline Silicon", "poly-Si"),
                         (2, "Monocrystalline Silicon", "mono-Si"),
                         (3, "Amorphous Silicon", "a-Si"),
                         (4, "Cadmium Telluride", "CdTe"),
                         (5, "Copper Indium Gallium Selenide", "CIGS");

insert into solar_panel
    (section,`row`,`column`,year_installed,material_id,is_tracking) values
    ("The Ridge",1,1,2020,1,1),
    ("The Ridge",1,2,2019,2,1),
    ("Flats",1,1,2017,3,1),
    ("Flats",2,6,2017,4,1),
    ("Flats",3,7,2000,5,0);