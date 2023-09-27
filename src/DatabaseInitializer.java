import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void createDatabaseAndTables() {
        if (!databaseExists()) {
            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "123");
                 Statement statement = connection.createStatement()) {

                // SQL para criar o banco "Vendas" se ele não existir
                String createDatabaseSQL = "CREATE DATABASE vendas WITH OWNER = postgres ENCODING = 'UTF8' CONNECTION LIMIT = -1 IS_TEMPLATE = False;\n";

                statement.executeUpdate(createDatabaseSQL);

                // Fecha a conexão atual
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Abre uma nova conexão para o banco de dados "Vendas" e cria as tabelas
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/vendas", "postgres", "123");
             Statement statement = connection.createStatement()) {

            // SQL para criar a tabela de produtos
            String createProdutoTableSQL = "CREATE TABLE IF NOT EXISTS produtos ("
                    + "id SERIAL PRIMARY KEY,"
                    + "descricao VARCHAR(255) NOT NULL,"
                    + "preco DECIMAL(10, 2) NOT NULL,"
                    + "quantidade INT NOT NULL"
                    + ")";

            statement.executeUpdate(createProdutoTableSQL);

            // SQL para criar a tabela de clientes
            String createClienteTableSQL = "CREATE TABLE IF NOT EXISTS clientes ("
                    + "id SERIAL PRIMARY KEY,"
                    + "nome VARCHAR(255) NOT NULL"
                    + ")";

            statement.executeUpdate(createClienteTableSQL);

            // SQL para criar a tabela de vendas
            String createVendaTableSQL = "CREATE TABLE IF NOT EXISTS vendas ("
                    + "id SERIAL PRIMARY KEY,"
                    + "data DATE NOT NULL,"
                    + "cliente_id INT REFERENCES clientes(id),"
                    + "valor_total DECIMAL(10, 2) NOT NULL,"
                    + "status VARCHAR(20) NOT NULL"
                    + ")";

            statement.executeUpdate(createVendaTableSQL);

            // SQL para criar a tabela de itens de venda
            String createItemVendaTableSQL = "CREATE TABLE IF NOT EXISTS itens_venda ("
                    + "id SERIAL PRIMARY KEY,"
                    + "venda_id INT REFERENCES vendas(id),"
                    + "produto_id INT REFERENCES produtos(id),"
                    + "quantidade INT NOT NULL,"
                    + "preco DECIMAL(10, 2) NOT NULL,"
                    + "valor_total DECIMAL(10, 2) NOT NULL"
                    + ")";

            statement.executeUpdate(createItemVendaTableSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private static boolean databaseExists() {
            // Verifique se o banco de dados "Vendas" já existe
            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/vendas", "postgres", "123")) {
                return true; // A conexão foi bem-sucedida, o banco de dados já existe
            } catch (SQLException e) {
                return false; // A conexão falhou, o banco de dados não existe
            }
        }
    }