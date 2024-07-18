# Projeto de eCommerce com Spring Boot e MySQL

Este é um projeto de aplicação de eCommerce desenvolvido para o desafio 03 do programa de bolsas back-end da COMPASS, utilizando Java, Spring Boot e MySQL. O projeto implementa funcionalidades básicas para gerenciamento de produtos, vendas e controle de estoque, e apresenta endpoints com validações de usuários comuns e administradores (roles).

## Diagrama ER - Entidade Relacionamento
Diagrama utilizado como base para o desenvolvimento do projeto

![Diagrama em branco - Página 1](https://github.com/user-attachments/assets/9c9d481e-1a43-4c99-b904-f0ea99a6874e)

## Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas para testar a API:

- Java 17
- SpringBoot 3.3.1
- MySQL

## Configuração do Banco de Dados

1. **Criação do Banco de Dados:**
   - Crie um banco de dados MySQL chamado `ecommercetest`.

2. **Configuração do Spring Boot:**
   - As configurações do banco de dados podem ser encontradas no arquivo `application.properties`. Ajuste as configurações de URL, usuário e senha conforme necessário.

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update

.
