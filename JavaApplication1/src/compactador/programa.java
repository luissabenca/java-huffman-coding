/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compactador;

import java.io.*;

/**
 *
 * @author Luis Sabença
 */
public class programa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try
        {
            System.out.println("Bem vindo ao compactador");
            System.out.println("1 - Compactar");
            System.out.println("2 - Descompactar");
            System.out.println("3 - Sair");

            BufferedReader teclado=new BufferedReader(new InputStreamReader(System.in));

            int opcao=Integer.parseInt(teclado.readLine());

            switch(opcao)
            {
                case 1:
                    System.out.println("Digite o local do arquivo : ");
                    compactador meuCompactador = new compactador(teclado.readLine());
                    meuCompactador.compactar();
                break;

                case 2:
                    System.out.println("Digite o local do arquivo : ");
                    descompactador meuDescompactador = new descompactador(teclado.readLine());
                    meuDescompactador.descompactar();
                break;

                default:
                    System.exit(0);
                break;
            }    
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } //C:\Users\TEMP\testeEssencial.txt
    }
}
