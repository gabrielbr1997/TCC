package br.com.tcc.dal;

import java.sql.*;

public class ModuloConexao {

    //metodo responsavel por estabelecer a conexão com o banco
    public static Connection conector() {
        java.sql.Connection conexao = null;
        // a linha abaixo chama o driver
        String driver = "com.mysql.jdbc.Driver";
        //armazenando informações referente ao banco
        String url = "jdbc:mysql://localhost:3306/tcc";
        String user = "root";
        String password = "";
        //estabelecendo a conexão com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            return null;
        }
    }
}
