DROP DATABASE loja;
CREATE DATABASE loja;
USE loja;
	
CREATE TABLE tb_produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    estoque INT NOT NULL
);

CREATE TABLE tb_clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cpf VARCHAR(14) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    rua VARCHAR(100),
    numero VARCHAR(10),
    bairro VARCHAR(100),
    cep VARCHAR(10),
    cidade VARCHAR(100),
    estado CHAR(2)
);

CREATE TABLE tb_pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    data DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES tb_clientes(id)
);

CREATE TABLE tb_itens_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES tb_pedidos(id),
    FOREIGN KEY (produto_id) REFERENCES tb_produtos(id)
);



