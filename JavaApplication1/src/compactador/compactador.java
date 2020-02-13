/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compactador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis Sabença
 */
public class compactador 
{   
    protected String diretorio;
	protected RandomAccessFile arq;
	protected int[] frequencia;
	protected no<informacao> nos[];
    protected int cont = 0;
    protected codigo[] meusCodigos;
    protected int qtosDiferentes;

	public compactador(String diretorioArquivo) throws Exception
	{
        this.cont = 0;
        this.qtosDiferentes = 0;
        try
        {
			this.diretorio = diretorioArquivo;
			this.arq = new meuRandomAccessFile(this.diretorio, "r");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
	}

	public void compactar()
    {
        System.out.println("Lendo arquivo");
        
        this.frequencia = new int[256];

        try 
        {
            for (int i = 0; i < this.arq.length(); i++) 
            {
                this.frequencia[this.arq.read()]++;
            }

            this.arq.close();
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }

        this.montarArvore();
    }   
        
    protected void montarArvore()
    {
        System.out.println("Montando arvore");
        
        this.nos = new no[256];
        this.qtosDiferentes = 0;

        for (int i = 0; i < 256; i++) 
        {
            if (this.frequencia[i] != 0) 
            {
                this.nos[this.qtosDiferentes] = new no(new informacao(this.frequencia[i], i));

                this.qtosDiferentes++;

                //vai ordenando o ultimo que acabou de adicionar, 
                //assim nao precisa ordenar tudo depois
                this.mudPos( this.qtosDiferentes);
            }
        }

        //somar os dois ultimos e ordenando
        for (int qtd = this.qtosDiferentes; qtd > 1; qtd--) 
        {
            this.mudPos(qtd);
            int soma = nos[qtd - 2].getInfo().getFrequencia() + nos[qtd - 1].getInfo().getFrequencia();

            no<informacao> aux = null;
            aux = new no<informacao>(new informacao(soma));

            aux.setEsq(nos[qtd - 2]);
            aux.setDir(nos[qtd - 1]);
            nos[qtd - 2] = aux;
            nos[qtd - 1] = null;
        }
        
        this.montarCodigos();
    }

    protected void montarCodigos()
    {
        System.out.println("Montando codigo");
        
        this.meusCodigos = new codigo[256];

        if (this.nos[0].getEsq() == null && this.nos[0].getDir() == null && this.nos[0].getInfo().getBytee() >= 0) 
        {
            this.meusCodigos[this.nos[0].getInfo().getBytee()] = new codigo("0");
        } 
        else 
        {
            this.criarCodigos(this.nos[0], new codigo());
        }
                    
        try 
        {
            this.escreverArquivo();
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
            
    }
        
    protected void criarCodigos(no<informacao> raiz,codigo cod)
    {
        if (raiz!=null)
        {
            if(raiz.getInfo().getBytee() != -1)
                this.meusCodigos[raiz.getInfo().getBytee()] = (codigo)cod.clone();
            else
            {
               cod.mais(0);
               this.criarCodigos(raiz.getEsq(),cod);
               cod.tirarUltimo();
               cod.mais(1);
               this.criarCodigos(raiz.getDir(),cod);
               cod.tirarUltimo();
            }
        }
    }
        
    public void escreverArquivo() throws IOException
    {
        System.out.println("Escrevendo arquivo");
        try 
        {                
	        String aux[] = this.diretorio.split("\\.");
	        String extensao = aux[1];
	        
	        File novoArquivo  = new File(aux[0] + ".zuza");
	        
	        meuRandomAccessFile escritor = new meuRandomAccessFile(novoArquivo, "rw");
	        meuRandomAccessFile leitor = new meuRandomAccessFile(this.diretorio, "r");
	        
	        escritor.write(extensao.length());
	        for (int i = 0; i < extensao.length(); i++)
	        {
				escritor.write((int)extensao.charAt(i));
	        }
	        
	        long posLixo = escritor.getFilePointer();
	        escritor.write(0);
	        
	        escritor.writeInt(this.qtosDiferentes);
	        
	        for (int i = 0; i < this.frequencia.length ; i++) 
	        {
	            if (this.frequencia[i] > 0) 
	            {
	                escritor.write(i);
	                escritor.writeInt(this.frequencia[i]);
	            }
	        }
	        
	        for (int i = 0; i < leitor.length(); i++)
	        {
				int pos = leitor.read();
	            escritor.escreverCodigo(this.meusCodigos[pos]);
	        }
	        
	        int qtdLixo = escritor.getQtdLixo();
	        escritor.preencherLixo();
			
	        escritor.seek(posLixo);
	        escritor.write(qtdLixo);
			
	        escritor.close();
	        leitor.close();
	        
	        System.out.println("Finalizado!");           
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }
               
    protected void mudPos(int qtd) 
    {
	    for (int i = qtd - 1; i > 0; i--) 
	    {
	        if (this.nos[i].getInfo().getFrequencia() <=  this.nos[i - 1].getInfo().getFrequencia()) 
	        {
	            break;
	        } 
	        else 
	        {
	            no<informacao> aux = this.nos[i];
	            this.nos[i] = this.nos[i - 1];
	            this.nos[i - 1] = aux;
	        }
	    }
    }
}
