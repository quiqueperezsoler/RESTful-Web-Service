CREATE SCHEMA `UPMSocial` ;

CREATE TABLE IF NOT EXISTS `UPMSocial`.`Usuario` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  `descripcion` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

ALTER TABLE `UPMSocial`.`Usuario` 
CHANGE COLUMN `nombre` `nombre` VARCHAR(45) NOT NULL ;

INSERT INTO UPMSocial.Usuario (nombre, descripcion) VALUES ('Javier', 'Entrada de prueba');

CREATE TABLE `UPMSocial`.`Estado` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idUsuario` INT NOT NULL,
  `texto` VARCHAR(45) NULL DEFAULT NULL,
  `fecha` DATE,
  PRIMARY KEY (`id`),
  INDEX `idUsuario_idx` (`idUsuario` ASC),
  CONSTRAINT `idUsuario`
    FOREIGN KEY (`idUsuario`)
    REFERENCES `UPMSocial`.`Usuario` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);

CREATE TABLE `UPMSocial`.`esAmigo` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idUsuario1` INT NOT NULL,
  `idUsuario2` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_esAmigo_1_idx` (`idUsuario1` ASC),
  INDEX `fk_esAmigo_2_idx` (`idUsuario2` ASC),
  CONSTRAINT `fk_esAmigo_1`
    FOREIGN KEY (`idUsuario1`)
    REFERENCES `UPMSocial`.`Usuario` (`id`)
  CONSTRAINT `fk_esAmigo_2`
    FOREIGN KEY (`idUsuario2`)
    REFERENCES `UPMSocial`.`Usuario` (`id`)
    ON DELETE CASCADE    ON UPDATE NO ACTION);
