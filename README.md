Documentação do Sistema de Gestão de Vendas
Introdução
A documentação a seguir descreve o Sistema de Gestão de Vendas, uma aplicação de 
desktop Java desenvolvida para gerenciar produtos, clientes, vendas e estoque em uma loja. 
Este sistema oferece recursos de cadastro, consulta e manipulação de vendas e produtos.
Requisitos do Sistema
Requisitos do Sistema
• JDK 8 ou superior.
• PostgreSQL 9.6 ou superior.
• Biblioteca Swing para a interface gráfica.
Configuração do Banco de Dados
O sistema utiliza um banco de dados PostgreSQL para armazenar dados. Você deve 
configurar a conexão com o banco de dados no arquivo DatabaseConfig.java. Certifique-se de 
fornecer as informações corretas de conexão, como URL do banco de dados, nome de 
usuário e senha.
Instalação e Execução
1. Clone o repositório Git do projeto em sua máquina local.
git clone https://github.com/seu-usuario/sistema-gestao-vendas.git 
2. Compile o código-fonte do sistema:
javac *.java 
3. Execute o sistema:
java Main
Uso do Sistema
Tela de Cadastro de Produtos
A tela de cadastro de produtos permite ao usuário cadastrar novos produtos. Preencha os 
campos obrigatórios, como descrição, preço e quantidade, e clique em "Cadastrar" para 
adicionar um novo produto ao sistema.
Tela de Cadastro de Clientes
A tela de cadastro de clientes permite ao usuário cadastrar novos clientes. Preencha os campos 
obrigatórios, como nome, e clique em "Cadastrar" para adicionar um novo cliente ao sistema.
Menu Principal
O sistema agora inclui um menu principal que oferece acesso rápido às principais 
funcionalidades:
• Cadastro de Produtos: Permite cadastrar novos produtos.
• Cadastro de Clientes: Permite cadastrar novos clientes.
• Vendas: Abre a tela de vendas para criar e efetivar vendas.
• Consulta de Vendas: Abre a tela de consulta de vendas para filtrar e visualizar detalhes 
de vendas.
Tela de Vendas
A tela de vendas permite ao usuário criar novas vendas, adicionar produtos à venda e efetivála. Você pode:
• Selecionar um cliente existente ou cadastrar um novo cliente.
• Adicionar produtos à venda, especificando a quantidade.
• Efetivar a venda, que atualiza o estoque de produtos e registra a venda no sistema.
Tela de Consulta de Vendas
A tela de consulta de vendas permite ao usuário filtrar as vendas por data e exibir os detalhes 
das vendas. Você pode escolher entre visualização consolidada (resumo) e visualização 
detalhada (com informações dos produtos vendidos).
Manutenção do Código
Estrutura do Código
O código-fonte do sistema está organizado nas seguintes classes:
• Main.java: Classe principal que inicia o sistema.
• DatabaseConfig.java: Configuração de conexão com o banco de dados PostgreSQL.
• Produto.java: Classe de modelo para produtos.
• Cliente.java: Classe de modelo para clientes.
• Venda.java: Classe de modelo para vendas.
• TelaCadastroProdutos.java: Interface gráfica para cadastro de produtos.
• TelaCadastroClientes.java: Interface gráfica para cadastro de clientes.
• TelaVendas.java: Interface gráfica para criação e efetivação de vendas.
• TelaConsultaVendas.java: Interface gráfica para consulta de vendas.
Testes Unitários
O sistema inclui testes unitários para as classes de serviço, como ProdutoService e 
VendaService. Certifique-se de manter uma cobertura de testes de pelo menos 80% ao 
adicionar ou modificar funcionalidades.
Suporte
Para obter suporte ou relatar problemas, entre em contato com nossa equipe de suporte em 
[email de suporte] ou visite nossa página de suporte em [link da página de suporte].
Conclusão
O Sistema de Gestão de Vendas é uma aplicação robusta e flexível para gerenciar produtos, 
clientes e vendas em uma loja. Esperamos que este documento tenha fornecido uma visão 
abrangente do sistema e como usá-lo. Agradecemos por escolher nosso software.
