/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compactador;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis Sabença
 */


public class meuRandomAccessFile extends RandomAccessFile
{
	protected int bits;
	protected int qtdBitsUsados;
	
	public meuRandomAccessFile(File arq, String s) throws FileNotFoundException
	{
		super(arq, s);
		this.inicializarVariaveis();
	}

	public meuRandomAccessFile(String arq, String s) throws FileNotFoundException
	{      
        super(arq, s);
        this.inicializarVariaveis();
    }

	protected void inicializarVariaveis()
	{
		this.bits = 0;
		this.qtdBitsUsados = 0;
	}
	
	public void escreverCodigo(codigo c) throws Exception
	{
		if (c == null)
			throw new Exception("Codigo nulo");
		
		String codigo = c.getCodigo();
		for(int i = 0; i < codigo.length(); i++)
		{		    
		    this.bits = this.bits << 1;
	        int bit = Integer.parseInt(codigo.charAt(i) + "");
	        this.bits = this.bits | bit;
	        this.qtdBitsUsados++;
			
			if(this.qtdBitsUsados == 8)
		    {
                        //.println("escreveu : "+ this.bits);
		        this.write(this.bits);
		        this.inicializarVariaveis();
		    }
		}	
	}
	
	public int getQtdLixo()
	{
		return (8 - this.qtdBitsUsados);
	}
	
	public void preencherLixo()
	{
		this.bits = this.bits << (8 - this.qtdBitsUsados);
		
		try
		{
                        //System.out.println("escreveu : "+ this.bits);
			this.write(this.bits);
		}
		catch (Exception e) {}

		this.inicializarVariaveis();
	}
}