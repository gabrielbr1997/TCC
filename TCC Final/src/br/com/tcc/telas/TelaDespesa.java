package br.com.tcc.telas;

import br.com.tcc.dal.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class TelaDespesa extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaDespesa() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    //Método para adicionar despesa
    private void adicionar() {
        String sql = "insert into tbdespesas(valor,tipo_gasto,setor) values(?, ?, ?);";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtValor.getText());
            pst.setString(2, txtTipoGasto.getText());
            pst.setString(3, cboSetor.getSelectedItem().toString());

            if (txtValor.getText().isEmpty() || txtTipoGasto.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha os campos obrigatorios");
            } else {
                if (Float.parseFloat(txtValor.getText()) <= 0) {
                    JOptionPane.showMessageDialog(null, "Favor preencher com um número positivo");
                } else {
                    int adicionado = pst.executeUpdate();
                    // se pst.executeUpdate() retornar 1, é porque deu certo, se voltar 0 é pq não deu
                    if (adicionado > 0) {
                        JOptionPane.showMessageDialog(null, "Despesa cadastrada com sucesso!");
                    }

                    //as linhas abaixo "limpam" os campos
                    txtValor.setText(null);
                    txtTipoGasto.setText(null);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    //Método para pesquisar usuarios pelo nome com filtro
    private void pesquisar_despesas() {
        String sql = "select * from tbdespesas where month(data_despesa) = month(?" + "?" + "?)";

        try {
            pst = conexao.prepareStatement(sql);
            // passando o conteudo da caixa de pesquisa para o ?
            // atenção ao '%" que é a continuação da Query sql            
            pst.setString(1, cboConsultaAno.getSelectedItem().toString());
            pst.setString(2, cboConsultaMes.getSelectedItem().toString());
            pst.setString(3, cboDespesaDia.getSelectedItem().toString());
            rs = pst.executeQuery();

            //a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblGasto.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Método para deletar despesas
    private void deletar() {
        String sql = "delete from tbdespesas where data_despesa = ?";

        try {

            int data = tblGasto.getSelectedRow();
            pst = conexao.prepareStatement(sql);
            pst.setString(1, (tblGasto.getModel().getValueAt(data, 2).toString()));

            int deletar = JOptionPane.showConfirmDialog(null, "Conforima exclusão?", "atenção", JOptionPane.YES_NO_OPTION);
            if (deletar == JOptionPane.YES_OPTION) {
                int deletado = pst.executeUpdate();
                if (deletado > 0) {
                    JOptionPane.showMessageDialog(null, "Despesa deletada com sucesso!");

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    //Método para comparar as despesas por mes
    private void comparar() {
        String sql = "SELECT SUM(valor) as valor FROM tbdespesas WHERE MONTH(data_despesa) = ? and year(data_despesa) =? and (setor) = ?";

        try {
            // mes 1
            pst = conexao.prepareStatement(sql);
            pst.setString(1, cboCompMes1.getSelectedItem().toString());
            pst.setString(2, anoComp1.getText());
            pst.setString(3, cboCompSetor.getSelectedItem().toString());

            if (anoComp1.getText().isEmpty() || anoComp2.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Informe o ano");
            } else {
                rs = pst.executeQuery();

                if (rs.next()) {
                    String resultado;
                    float mes1 = Float.parseFloat((rs.getString(1)));

                    // mes 2
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, cboCompMes2.getSelectedItem().toString());
                    pst.setString(2, anoComp2.getText());
                    pst.setString(3, cboCompSetor.getSelectedItem().toString());
                    ResultSet rs2 = pst.executeQuery();

                    if (rs2.next()) {
                        float mes2 = Float.parseFloat((rs2.getString(1)));

                        float diferenca1 = mes1 - mes2;
                        float diferenca2 = mes2 - mes1;

                        if (mes1 > mes2) {
                            resultado = ("As despesas do departamento de " + cboCompSetor.getSelectedItem().toString() + " no mês " + cboCompMes1.getSelectedItem().toString() + " (R$ " + mes1 + ")" + " é maior que do mês " + cboCompMes2.getSelectedItem().toString() + " (R$ " + mes2 + ")" + " com uma diferença de R$: " + diferenca1);
                            JOptionPane.showMessageDialog(null, resultado);
                        } else {
                            if (mes2 > mes1) {
                                resultado = ("As despesas do departamento de " + cboCompSetor.getSelectedItem().toString() + " no mês " + cboCompMes2.getSelectedItem().toString() + " (R$ " + mes2 + ")" + " é maior que do mês " + cboCompMes1.getSelectedItem().toString() + " (R$ " + mes1 + ")" + " com uma diferença de R$: " + diferenca2);
                                JOptionPane.showMessageDialog(null, resultado);
                            } else {
                                JOptionPane.showMessageDialog(null, "Os dois meses tiveram as mesmas despesas!");
                            }
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Nenhuma despesa cadastrada neste periodo");
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Nenhuma despesa cadastrada neste periodo");
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtValor = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cboSetor = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        btnPesquisa = new javax.swing.JButton();
        txtTipoGasto = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGasto = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        cboCompSetor = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        anoComp2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        cboConsultaMes = new javax.swing.JComboBox<>();
        cboConsultaAno = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cboDespesaDia = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        cboCompMes1 = new javax.swing.JComboBox<>();
        cboCompMes2 = new javax.swing.JComboBox<>();
        anoComp1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Despesas");
        setPreferredSize(new java.awt.Dimension(647, 393));

        jLabel1.setText("Valor:");

        jLabel2.setText("Tipo de gasto:");

        jLabel3.setText("Setor:");

        cboSetor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administração", "Controle Patrimonial", "Divisão de Pessoal", "Gerência de Créditos", "Hotel de Trânsito", "Intendência", "Núcleo do Serviço de Assistência Social", "Saúde e Educão Física", "Segurança", "Serviço de Recrutamento Distrital", "Serviços Gerais", "Subsistência", "Tecnologia da Informação", "Transporte" }));

        jButton2.setText("Adicionar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Deletar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btnPesquisa.setText("Pesquisar");
        btnPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisaActionPerformed(evt);
            }
        });

        jLabel4.setText("Data:");

        tblGasto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Setor", "Tipo de Gasto", "Data", "Valor"
            }
        ));
        jScrollPane2.setViewportView(tblGasto);

        jLabel5.setText("Comparação de depesas:");

        cboCompSetor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administração", "Controle Patrimonial", "Divisão de Pessoal", "Gerência de Créditos", "Hotel de Trânsito", "Intendência", "Núcleo do Serviço de Assistência Social", "Saúde e Educão Física", "Segurança", "Serviço de Recrutamento Distrital", "Serviços Gerais", "Subsistência", "Tecnologia da Informação", "Transporte" }));

        jLabel7.setText("Setor:");

        jLabel8.setText("*Mês:");

        jLabel9.setText("*Mês:");

        jLabel11.setText("*Ano:");

        jButton1.setText("Comparar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        cboConsultaMes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));

        cboConsultaAno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030" }));

        jLabel12.setText("Ano:");

        jLabel13.setText("Mês:");

        cboDespesaDia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        jLabel6.setText("Dia:");

        cboCompMes1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));

        cboCompMes2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));

        jLabel14.setText("*Ano:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cboCompSetor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cboCompMes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cboCompMes2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(24, 24, 24)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(anoComp2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel14)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(anoComp1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jButton2))
                        .addComponent(jLabel3)
                        .addComponent(cboSetor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(txtTipoGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 50, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(70, 70, 70))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cboDespesaDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel13)
                                        .addGap(2, 2, 2)
                                        .addComponent(cboConsultaMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cboConsultaAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnPesquisa))
                                    .addComponent(jLabel4)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jButton1)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTipoGasto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addGap(3, 3, 3)
                        .addComponent(cboSetor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addGap(32, 32, 32)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 14, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCompSetor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jButton1)
                            .addComponent(cboCompMes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(anoComp1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPesquisa)
                            .addComponent(cboConsultaAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(cboConsultaMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)
                            .addComponent(cboDespesaDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(anoComp2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCompMes2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(41, 41, 41))
        );

        setBounds(0, 0, 647, 393);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        comparar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        adicionar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisaActionPerformed
        pesquisar_despesas();
    }//GEN-LAST:event_btnPesquisaActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        deletar();
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField anoComp1;
    private javax.swing.JTextField anoComp2;
    private javax.swing.JButton btnPesquisa;
    private javax.swing.JComboBox<String> cboCompMes1;
    private javax.swing.JComboBox<String> cboCompMes2;
    private javax.swing.JComboBox<String> cboCompSetor;
    private javax.swing.JComboBox<String> cboConsultaAno;
    private javax.swing.JComboBox<String> cboConsultaMes;
    private javax.swing.JComboBox<String> cboDespesaDia;
    private javax.swing.JComboBox<String> cboSetor;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblGasto;
    private javax.swing.JTextField txtTipoGasto;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
